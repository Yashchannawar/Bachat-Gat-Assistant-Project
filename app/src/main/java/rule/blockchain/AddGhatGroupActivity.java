package rule.blockchain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
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

import java.util.Calendar;

public class AddGhatGroupActivity extends AppCompatActivity {

    private EditText edtGroupName, edtTotalAmount, edtMonthlyAmount, edtTotalMembers,
            edtNameOfPresident, edtMobilePresident, edtChairmanEmail, edtGroupPassword;
    private Button confirmButton;
    private TextView txtDate, txtTime;
    private ImageView btnDate, btnTime;
    private int mYear, mMonth, mDay, mHour, mMinute = 0;
    private static final int PICK_FILE_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ghat_group);


        // Find EditText fields by their IDs
        edtGroupName = findViewById(R.id.edtGroupName);
        edtTotalAmount = findViewById(R.id.edtTotalAmount);
        edtMonthlyAmount = findViewById(R.id.edtMonthlyAmount);
        edtTotalMembers = findViewById(R.id.edtTotalMembers);
        edtNameOfPresident = findViewById(R.id.edtNameOfPresident);
        edtMobilePresident = findViewById(R.id.edtMobilePresident);
        edtChairmanEmail = findViewById(R.id.edtChairmanEmail);
        edtGroupPassword = findViewById(R.id.edtGroupPassword);
        txtDate = findViewById(R.id.txtDate);
        txtTime = findViewById(R.id.txtTime);
        btnDate = findViewById(R.id.btnDate);
        btnTime = findViewById(R.id.btnTime);

        // Find Button by its ID
        confirmButton = findViewById(R.id.confirm_button);

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(AddGhatGroupActivity.this,
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
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddGhatGroupActivity.this,
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


        // Set click listener for the submit button
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call method to validate EditText fields
                if (validateEditTexts()) {
                    // All EditText fields are valid, proceed with submission
                    submitForm();
                } else {
                    // Validation failed, show error message
                    Toast.makeText(AddGhatGroupActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Method to validate EditText fields
    private boolean validateEditTexts() {
        return !TextUtils.isEmpty(edtGroupName.getText().toString().trim()) &&
                !TextUtils.isEmpty(edtTotalAmount.getText().toString().trim()) &&
                !TextUtils.isEmpty(edtMonthlyAmount.getText().toString().trim()) &&
                !TextUtils.isEmpty(edtTotalMembers.getText().toString().trim()) &&
                !TextUtils.isEmpty(edtNameOfPresident.getText().toString().trim()) &&
                !TextUtils.isEmpty(edtMobilePresident.getText().toString().trim()) &&
                !TextUtils.isEmpty(edtChairmanEmail.getText().toString().trim()) &&
                !TextUtils.isEmpty(edtGroupPassword.getText().toString().trim());
    }

    // Method to handle form submission
    private void submitForm() {

        // Create a new GhatGroup object and set its properties with values from EditText fields
        GhatGroup ghatGroup = new GhatGroup(
                edtGroupName.getText().toString().trim(),
                Integer.parseInt(edtTotalAmount.getText().toString().trim()),
                Integer.parseInt(edtMonthlyAmount.getText().toString().trim()),
                Integer.parseInt(edtTotalMembers.getText().toString().trim()),
                edtNameOfPresident.getText().toString().trim(),
                edtMobilePresident.getText().toString().trim(),
                edtChairmanEmail.getText().toString().trim(),
                edtGroupPassword.getText().toString().trim()
        );


        // Show a loading dialog
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving Bachat Ghat data...");
        progressDialog.setCancelable(false); // Prevent user from dismissing the dialog
        progressDialog.show();

        // Now, you can save the data to Firebase Realtime Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("BachatGhat");
        String prescriptionId = databaseReference.push().getKey();
        ghatGroup.setGroupKey(prescriptionId);
        databaseReference.child(prescriptionId).setValue(ghatGroup)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Prescription data saved successfully
                        // Dismiss the loading dialog
                        progressDialog.dismiss();
                        Toast.makeText(AddGhatGroupActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(AddGhatGroupActivity.this, "Error " + e.toString(), Toast.LENGTH_SHORT).show();

                        // You can show an error message to the user
                    }
                });

    }
}