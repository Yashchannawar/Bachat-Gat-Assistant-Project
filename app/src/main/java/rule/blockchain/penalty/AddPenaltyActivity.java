package rule.blockchain.penalty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rule.blockchain.Member;
import rule.blockchain.R;

public class AddPenaltyActivity extends AppCompatActivity {
    private Spinner spnSelectMember;
    private EditText edtAmount, editPenaltyDescription;
    private Button confirmButton;
    DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_penalty);

        // Initialize views
        spnSelectMember = findViewById(R.id.spnSelectMember);
        edtAmount = findViewById(R.id.edtAmount);
        editPenaltyDescription = findViewById(R.id.editPenaltyDescription);
        confirmButton = findViewById(R.id.confirm_button);
        List<String> members = new ArrayList<>();
        HashMap<String, Object> allData = new HashMap<>();
        List<Member> allMembers = new ArrayList<>();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Members");

        members.add("Select Member");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if (snapshot.exists()) {
                    for (DataSnapshot notepad : snapshot.getChildren()) {
                        Member member = notepad.getValue(Member.class);
                        members.add(member.getName());
                        allMembers.add(member);
                        allData.put(member.getName(), member);

                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, members);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnSelectMember.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Please check you internet connection Or it may be server error please try after some time !!!", Toast.LENGTH_LONG).show();
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, members);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnSelectMember.setAdapter(adapter);
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    // If validation passes, create and populate Loan model
                    Penalty penalty = new Penalty();
                    penalty.setMemberName(spnSelectMember.getSelectedItem().toString());
                    penalty.setPenaltyAmount(edtAmount.getText().toString());
                    penalty.setPenaltyDescription(editPenaltyDescription.getText().toString());
                    penalty.setMember((Member) allData.get(spnSelectMember.getSelectedItem().toString()));

                    submitForm(penalty);
                }
            }
        });


    }

    // Validation method to ensure all fields are selected and not empty
    private boolean validateInput() {
        if (spnSelectMember.getSelectedItemPosition() == 0) {
            showToast("Please select a member");
            return false;
        } else if (edtAmount.getText().toString().isEmpty()) {
            showToast("Please enter amount");
            return false;
        } else if (editPenaltyDescription.getText().toString().isEmpty()) {
            showToast("Please enter description");
            return false;
        }
        return true;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    private void submitForm(Penalty penalty) {

        // Show a loading dialog
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving data...");
        progressDialog.setCancelable(false); // Prevent user from dismissing the dialog
        progressDialog.show();

        // Now, you can save the data to Firebase Realtime Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Penalty");
        String prescriptionId = databaseReference.push().getKey();


        databaseReference.child(prescriptionId).setValue(penalty)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Prescription data saved successfully
                        // Dismiss the loading dialog
                        progressDialog.dismiss();
                        Toast.makeText(AddPenaltyActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(AddPenaltyActivity.this, "Error " + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
