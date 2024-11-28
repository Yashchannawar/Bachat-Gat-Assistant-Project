package rule.blockchain.loan;

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

public class AddLoanActivity extends AppCompatActivity {

    private Spinner spnSelectMember, spnSelectInterestRate;
    private EditText edtLoanAmount, editLoanTenure;
    private Button confirmButton;
    DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_loan);

        // Initialize views
        spnSelectMember = findViewById(R.id.spnSelectMember);
        spnSelectInterestRate = findViewById(R.id.spnSelectInterestRate);
        edtLoanAmount = findViewById(R.id.edtLoanAmount);
        editLoanTenure = findViewById(R.id.editLoanTenure);
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


        List<String> interestRate = new ArrayList<>();
        interestRate.add("Select Interest Rate");
        interestRate.add("1 %");
        interestRate.add("2 %");
        interestRate.add("3 %");
        interestRate.add("4 %");
        interestRate.add("5 %");
        interestRate.add("6 %");
        interestRate.add("7 %");
        interestRate.add("8 %");
        interestRate.add("9 %");
        interestRate.add("10 %");
        interestRate.add("11 %");
        interestRate.add("12 %");
        interestRate.add("13 %");
        interestRate.add("14 %");
        interestRate.add("15 %");
        interestRate.add("16 %");
        interestRate.add("17 %");
        interestRate.add("18 %");
        interestRate.add("19 %");
        interestRate.add("20 %");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, interestRate);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSelectInterestRate.setAdapter(adapter);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    // If validation passes, create and populate Loan model
                    Loan loan = new Loan();
                    loan.setMemberName(spnSelectMember.getSelectedItem().toString());
                    loan.setLoanIntrest(spnSelectInterestRate.getSelectedItem().toString());
                    loan.setLoanAmount(edtLoanAmount.getText().toString());
                    loan.setLoanTenure(editLoanTenure.getText().toString());
                    loan.setMember((Member) allData.get(spnSelectMember.getSelectedItem().toString()));

                    submitForm(loan);
                }
            }
        });


    }

    // Validation method to ensure all fields are selected and not empty
    private boolean validateInput() {
        if (spnSelectMember.getSelectedItemPosition() == 0) {
            showToast("Please select a member");
            return false;
        } else if (spnSelectInterestRate.getSelectedItemPosition() == 0) {
            showToast("Please select a loan interest");
            return false;
        } else if (edtLoanAmount.getText().toString().isEmpty()) {
            showToast("Please enter loan amount");
            return false;
        } else if (editLoanTenure.getText().toString().isEmpty()) {
            showToast("Please enter loan tenure");
            return false;
        }
        return true;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    private void submitForm(Loan loan) {

        // Show a loading dialog
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving data...");
        progressDialog.setCancelable(false); // Prevent user from dismissing the dialog
        progressDialog.show();

        // Now, you can save the data to Firebase Realtime Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Loans");
        String prescriptionId = databaseReference.push().getKey();


        databaseReference.child(prescriptionId).setValue(loan)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Prescription data saved successfully
                        // Dismiss the loading dialog
                        progressDialog.dismiss();
                        Toast.makeText(AddLoanActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(AddLoanActivity.this, "Error " + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
