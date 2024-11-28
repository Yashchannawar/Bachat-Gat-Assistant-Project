package rule.blockchain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.UUID;

public class AddPrescriptionActivity extends AppCompatActivity {

    private EditText edtPatientName, edtPatientAge, edtPatientWeight, edtPatientHeight, edtMedicalCondition, edtHospitalName, edtDescription;
    private Button btnUploadPrescription, btnUploadReport, confirmButton;
    private TextView txtDate, txtTime;
    private ImageView btnDate, btnTime;
    private int mYear, mMonth, mDay, mHour, mMinute = 0;
    private static final int PICK_FILE_REQUEST = 1;

    // Firebase
    private StorageReference mStorageRef;
    private String prescriptionDownloadUrl="";
    private String reportDownloadUrl="";
    private boolean isPrescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_prescription);

        edtPatientName = findViewById(R.id.edtPatientName);
        edtPatientAge = findViewById(R.id.edtPatientAge);
        edtPatientWeight = findViewById(R.id.edtPatientWeight);
        edtPatientHeight = findViewById(R.id.edtPatientHeight);
        btnUploadPrescription = findViewById(R.id.btnUploadPrescription);
        btnUploadReport = findViewById(R.id.btnUploadReport);
        confirmButton = findViewById(R.id.confirm_button);
        txtDate = findViewById(R.id.txtDate);
        txtTime = findViewById(R.id.txtTime);
        btnDate = findViewById(R.id.btnDate);
        btnTime = findViewById(R.id.btnTime);
        edtMedicalCondition = findViewById(R.id.edtMedicalCondition);
        edtHospitalName = findViewById(R.id.edtHospitalName);
        edtDescription = findViewById(R.id.edtDescription);


        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(AddPrescriptionActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                txtDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddPrescriptionActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                txtTime.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();

            }
        });


        // Initialize Firebase Storage
        mStorageRef = FirebaseStorage.getInstance().getReference();


        // Set onClickListener for btnUploadPrescription
        btnUploadPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilePicker(true); // True indicates prescription file upload
            }
        });

        // Set onClickListener for btnUploadReport
        btnUploadReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilePicker(false); // False indicates report file upload
            }
        });

        // Set onClickListener for confirmButton
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save prescription data and upload files
                savePrescriptionData();
            }
        });

    }

    // Method to open file picker
    private void openFilePicker(final boolean isPrescription1) {
        isPrescription = isPrescription1;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_FILE_REQUEST);

        // Handle the file selection in onActivityResult
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            final Uri fileUri = data.getData();
            // Handle the file Uri here
            // Now you can prompt the user with an alert dialog

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Save File");
            builder.setMessage("Do you want to upload this file?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Upload the file to Firebase Storage
                    uploadFileToFirebaseStorage(fileUri, isPrescription);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }
    }

    private void uploadFileToFirebaseStorage(Uri fileUri, final boolean isPrescription) {
        // Create a reference to 'prescriptions' folder in Firebase Storage
        final StorageReference fileRef = mStorageRef.child("prescriptions/" + UUID.randomUUID().toString());

        // Show loading bar
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading file...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Upload file to Firebase Storage
        fileRef.putFile(fileUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // File uploaded successfully, now get the download URL
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri downloadUri) {
                                // Hide loading bar
                                progressDialog.dismiss();

                                // Handle the download URL here
                                // You can save the URL or perform any further actions
                                if (isPrescription) {
                                    prescriptionDownloadUrl = downloadUri.toString();
                                } else {
                                    reportDownloadUrl = downloadUri.toString();
                                }
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Hide loading bar
                        progressDialog.dismiss();

                        // Handle unsuccessful uploads
                        Toast.makeText(getApplicationContext(), "Failed to upload file", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void savePrescriptionData() {
        // Retrieve other prescription data
        String patientName = edtPatientName.getText().toString();
        String patientAge = edtPatientAge.getText().toString();
        String patientWeight = edtPatientWeight.getText().toString();
        String patientHeight = edtPatientHeight.getText().toString();
        String medicalCondition = edtMedicalCondition.getText().toString();
        String hospitalName = edtHospitalName.getText().toString();
        String description = edtDescription.getText().toString();
        String date = txtDate.getText().toString();
        String time = txtTime.getText().toString();

        // You can perform validation here before saving the data

        // Show a loading dialog
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving prescription data...");
        progressDialog.setCancelable(false); // Prevent user from dismissing the dialog
        progressDialog.show();

        // Now, you can save the data to Firebase Realtime Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("prescriptions");
        String prescriptionId = databaseReference.push().getKey();
        Prescription prescription = new Prescription(
                prescriptionId,
                patientName,
                patientAge,
                patientWeight,
                patientHeight,
                medicalCondition,
                hospitalName,
                description,
                date,
                time,
                prescriptionDownloadUrl,
                reportDownloadUrl,
                LoginActivity.userSession.getEmail(),
                LoginActivity.userSession.getEmail()
        );

        databaseReference.child(prescriptionId).setValue(prescription)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Prescription data saved successfully
                        // Dismiss the loading dialog
                        progressDialog.dismiss();
                        Toast.makeText(AddPrescriptionActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                        // You can show a success message or navigate to another activity
                        // Clear the form fields if needed
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to save prescription data
                        // Dismiss the loading dialog
                        progressDialog.dismiss();
                        // You can show an error message to the user
                    }
                });
    }


}