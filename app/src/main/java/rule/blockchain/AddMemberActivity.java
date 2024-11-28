package rule.blockchain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddMemberActivity extends AppCompatActivity {

    private EditText edtMemberName, edtMemberAge, edtMemberMobile, edtMemberEmail, edtMemberPassword,
            edtMedicalAdhar, edtMemberPan, edtMemberAddress;
    private Button btnSubmit;
    private FirebaseAuth mauth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);


        // Initialize EditText fields
        edtMemberName = findViewById(R.id.edtMemberName);
        edtMemberAge = findViewById(R.id.edtMemberAge);
        edtMemberMobile = findViewById(R.id.edtMemberMobile);
        edtMemberEmail = findViewById(R.id.edtMemberEmail);
        edtMemberPassword = findViewById(R.id.edtMemeberPassword);
        edtMedicalAdhar = findViewById(R.id.edtMedicalAdhar);
        edtMemberPan = findViewById(R.id.edtMemberPan);
        edtMemberAddress = findViewById(R.id.edtMemberAddress);
        mauth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");


        // Initialize submit button
        btnSubmit = findViewById(R.id.confirm_button);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform validation
                if (validateFields()) {
                    // If all fields are valid, create Member object and set data
                    Member member = new Member();
                    member.setName(edtMemberName.getText().toString());
                    member.setAge(Integer.parseInt(edtMemberAge.getText().toString()));
                    member.setMobile(edtMemberMobile.getText().toString());
                    member.setEmail(edtMemberEmail.getText().toString());
                    member.setPassword(edtMemberPassword.getText().toString());
                    member.setAdharNumber(edtMedicalAdhar.getText().toString());
                    member.setPanNumber(edtMemberPan.getText().toString());
                    member.setAddress(edtMemberAddress.getText().toString());


                    // Show a loading dialog
                    ProgressDialog progressDialog = new ProgressDialog(AddMemberActivity.this);
                    progressDialog.setMessage("Saving Member data...");
                    progressDialog.setCancelable(false); // Prevent user from dismissing the dialog
                    progressDialog.show();

                    // Now, you can save the data to Firebase Realtime Database
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Members");
                    String prescriptionId = databaseReference.push().getKey();
                    member.setKey(prescriptionId);
                    databaseReference.child(prescriptionId).setValue(member)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Prescription data saved successfully
                                    // Dismiss the loading dialog
                                    progressDialog.dismiss();
                                    Toast.makeText(AddMemberActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                                    register_user(member);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Failed to save prescription data
                                    // Dismiss the loading dialog
                                    progressDialog.dismiss();
                                    Toast.makeText(AddMemberActivity.this, "Error " + e.toString(), Toast.LENGTH_SHORT).show();

                                    // You can show an error message to the user
                                }
                            });

                }
            }
        });
    }

    // Method to validate all fields
    private boolean validateFields() {
        if (TextUtils.isEmpty(edtMemberName.getText().toString()) ||
                TextUtils.isEmpty(edtMemberAge.getText().toString()) ||
                TextUtils.isEmpty(edtMemberMobile.getText().toString()) ||
                TextUtils.isEmpty(edtMemberEmail.getText().toString()) ||
                TextUtils.isEmpty(edtMemberPassword.getText().toString()) ||
                TextUtils.isEmpty(edtMedicalAdhar.getText().toString()) ||
                TextUtils.isEmpty(edtMemberPan.getText().toString()) ||
                TextUtils.isEmpty(edtMemberAddress.getText().toString())) {
            // Show error message if any field is empty
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void register_user(Member member) {
        // Show a loading dialog
        ProgressDialog progressDialog = new ProgressDialog(AddMemberActivity.this);
        progressDialog.setMessage("Adding a member...");
        progressDialog.setCancelable(false); // Prevent user from dismissing the dialog
        progressDialog.show();

        mauth.createUserWithEmailAndPassword(member.getEmail(), member.getPassword()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                //------IF USER IS SUCCESSFULLY REGISTERED-----
                if (task.isSuccessful()) {

                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    final String uid = current_user.getUid();

                    Map userMap = new HashMap();
                    userMap.put("uid", uid);
                    userMap.put("name", member.getName());
                    userMap.put("email", member.getEmail());
                    userMap.put("password", member.getPassword());
                    userMap.put("type", "member");
                    userMap.put("member", member.toString());


                    mDatabase.child(uid).setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task1) {
                            progressDialog.dismiss();
                            if (task1.isSuccessful()) {

                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Member User is also created", Toast.LENGTH_SHORT).show();


                            } else {
                                progressDialog.dismiss();

                                Toast.makeText(getApplicationContext(), "MEMBER NAME IS NOT REGISTERED... MAKE NEW ACCOUNT-- ", Toast.LENGTH_SHORT).show();

                            }

                        }
                    });


                }
                //---ERROR IN ACCOUNT CREATING OF NEW USER---
                else {
                    progressDialog.dismiss();
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        Toast.makeText(getApplicationContext(), e.getReason().toString(), Toast.LENGTH_SHORT).show();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    } catch (FirebaseAuthUserCollisionException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


}

