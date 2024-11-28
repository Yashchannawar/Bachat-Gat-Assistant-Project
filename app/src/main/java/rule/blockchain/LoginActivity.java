package rule.blockchain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import rule.breaker.rto.RtoHomeActivity;

public class LoginActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    TextInputLayout emailTextInputLayout, passTextInputLayout;
    DatabaseReference mDatabaseReference;
    FirebaseAuth mauth;
    private static int RC_SIGN_IN = 100;
    public static GoogleSignInClient mGoogleSignInClient;
    public static UserSession userSession;
    String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        if (new UserSession(getApplicationContext()).getUserId().isEmpty() == false) {
//            finish();
//            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
//        }

        userSession = new UserSession(getApplicationContext());

        if (userSession.getEmail().isEmpty() == false) {

            if (userSession.getReferCode().isEmpty()) {
                finish();
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            }else{
                finish();
                startActivity(new Intent(LoginActivity.this, MemberHomeActivity.class));

            }
        }

        emailTextInputLayout = (TextInputLayout) findViewById(R.id.email);
        passTextInputLayout = (TextInputLayout) findViewById(R.id.editText2);
        progressDialog = new ProgressDialog(LoginActivity.this);

        mauth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");


        findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailTextInputLayout.getEditText().getText().toString().trim();
                String password = passTextInputLayout.getEditText().getText().toString().trim();

                //---CHECKING IF EMAIL AND PASSWORD IS NOT EMPTY----
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Please Fill all blocks", Toast.LENGTH_SHORT).show();
                    return;
                }


                progressDialog.setTitle("Logging in");
                progressDialog.setMessage("Please wait while we are checking the credentials..");
                progressDialog.setCancelable(false);
                progressDialog.setProgress(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();


                login_user(email, password);

            }
        });

        findViewById(R.id.dontHaveAccount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
            }
        });

        // Google Sign In Options
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();
//
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1027321555698-2fups0srnmkcioqj9mepvj8tb2radud8.apps.googleusercontent.com")
                .requestEmail()
                .build();
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mGoogleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();

        findViewById(R.id.signInGoogle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGoogleSignInClient.signOut();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            try {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAGtoken", "firebaseAuthWithGoogle:" + account.getId());
//                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAGFailed", "Google sign in failed", e);
            }
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {

                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(LoginActivity.this.getApplicationContext());
                Log.e("Acct ", acct.toString());

//                data.putString("personFirstName", acct.getGivenName());
//                data.putString("personSurname", acct.getFamilyName());
//                data.putString("personEmail", acct.getEmail());

            } catch (Exception e) {
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                Log.d("TAG", "signInResult:failed code=" + e.toString());
                e.printStackTrace();
                //updateUI(null);
            }
        }
    }


    //----SHOWING ALERT DIALOG FOR EXITING THE APP----
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage("Really Exit ??");
        builder.setTitle("Exit");
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();

    }


    private void login_user(String email, String password) {

        //---SIGN IN FOR THE AUTHENTICATE EMAIL-----
        mauth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        type = "";

                        if (task.isSuccessful()) {

                            //---ADDING DEVICE TOKEN ID AND SET ONLINE TO BE TRUE---
                            //---DEVICE TOKEN IS USED FOR SENDING NOTIFICATION----


                            DatabaseReference users = FirebaseDatabase.getInstance().getReference().child("users").child(mauth.getCurrentUser().getUid());
                            users.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        // dataSnapshot is the "notepad" node with all children with id
                                        for (DataSnapshot notepad : dataSnapshot.getChildren()) {
                                            Log.e("dataSnapshot.getC", notepad.toString());


                                            if (notepad.getKey().equalsIgnoreCase("type")) {
                                                type = notepad.getValue().toString();
                                                userSession.setReferCode(type);
                                            }

                                            if (notepad.getKey().equals("name")) {
                                                userSession.setEmail(email);
                                                userSession.setPassword(password);
                                                userSession.setFirstName(notepad.getValue().toString());
                                                userSession.setUserId(mauth.getCurrentUser().getUid());
                                            }

                                        }


                                        if (type.equalsIgnoreCase("member")) {
                                            Toast.makeText(getApplicationContext(), "Logged in successfully." +
                                                    "", Toast.LENGTH_SHORT).show();
                                            finish();
                                            startActivity(new Intent(getApplicationContext(), MemberHomeActivity.class));
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Logged in successfully." +
                                                    "", Toast.LENGTH_SHORT).show();
                                            finish();
                                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));

                                        }


                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Toast.makeText(getApplicationContext(), "Error Occur try again", Toast.LENGTH_SHORT).show();
                                    Log.e("Err ", databaseError.toString());
                                }
                            });


                            userSession.setEmail(email);


                        } else {
                            //---IF AUTHENTICATION IS WRONG----
                            String error = "Wrong Credentials ";
                            try {
                                task.getException();
                            } catch (Exception e) {
                                error = error + e.toString();
                            }

                            Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }


}