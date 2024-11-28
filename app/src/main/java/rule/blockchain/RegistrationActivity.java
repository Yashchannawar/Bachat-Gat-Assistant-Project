package rule.blockchain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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

public class RegistrationActivity extends AppCompatActivity {


    TextInputLayout etdisplayname, etemail, etpassword;
    ProgressDialog progressDialog;

    FirebaseAuth mauth;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        etdisplayname = (TextInputLayout) findViewById(R.id.editText3);
        etemail = (TextInputLayout) findViewById(R.id.editText4);
        etpassword = (TextInputLayout) findViewById(R.id.editText5);
        progressDialog = new ProgressDialog(RegistrationActivity.this);


        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });


        mauth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

    }


    public void register() {

        String displayname = etdisplayname.getEditText().getText().toString().trim();
        String email = etemail.getEditText().getText().toString().trim();
        String password = etpassword.getEditText().getText().toString().trim();

        //----CHECKING THE EMPTINESS OF THE EDITTEXT-----
        if (displayname.equals("")) {
            Toast.makeText(this, "Please Fill the name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (email.equals("")) {
            Toast.makeText(this, "Please Fill the email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Password is too short", Toast.LENGTH_SHORT).show();
            return;
        }


        progressDialog.setTitle("Registering User");
        progressDialog.setMessage("Please wait while we are creating your account... ");
        progressDialog.setCancelable(false);
        progressDialog.setProgress(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        register_user(displayname, email, password);


    }


    //-----REGISTERING THE NEW USER------
    private void register_user(final String displayname, String email, String password) {

        mauth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                //------IF USER IS SUCCESSFULLY REGISTERED-----
                if (task.isSuccessful()) {

                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    final String uid = current_user.getUid();

                    Map userMap = new HashMap();
                    userMap.put("uid", uid);
                    userMap.put("name", displayname);
                    userMap.put("email", email);
                    userMap.put("password", password);

                    TextInputLayout model = findViewById(R.id.editText6);
                    TextInputLayout registrationNo = findViewById(R.id.editText7);

                    if (model.getEditText().getText().toString().isEmpty() == false) {
                        userMap.put("model", model.getEditText().getText().toString());
                    } else {
                        userMap.put("model", "");
                    }


                    if (registrationNo.getEditText().getText().toString().isEmpty() == false) {
                        userMap.put("vehicleNo", registrationNo.getEditText().getText().toString());
                    } else {
                        userMap.put("vehicleNo", "");
                    }


                    mDatabase.child(uid).setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task1) {
                            if (task1.isSuccessful()) {

                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "New User is created", Toast.LENGTH_SHORT).show();



                                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                                //----REMOVING THE LOGIN ACTIVITY FROM THE QUEUE----
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();


                            } else {

                                Toast.makeText(getApplicationContext(), "YOUR NAME IS NOT REGISTERED... MAKE NEW ACCOUNT-- ", Toast.LENGTH_SHORT).show();

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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }
}