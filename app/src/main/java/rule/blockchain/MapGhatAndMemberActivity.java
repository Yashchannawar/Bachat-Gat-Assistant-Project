package rule.blockchain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MapGhatAndMemberActivity extends AppCompatActivity {

    DatabaseReference mDatabaseReference;
    Spinner spnSelectMember, spnGroupName;

    private EditText edtMemberName, edtMemberAge, edtMemberMobile, edtMemberEmail, edtMemberPassword,
            edtMedicalAdhar, edtMemberPan, edtMemberAddress;

    private EditText edtGroupName, edtTotalAmount, edtMonthlyAmount, edtTotalMembers,
            edtNameOfPresident, edtMobilePresident, edtChairmanEmail, edtGroupPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_ghat_and_member);


        spnSelectMember = findViewById(R.id.spnSelectMember);
        spnGroupName = findViewById(R.id.spnGroupName);

        edtMemberName = findViewById(R.id.edtMemberName);
        edtMemberMobile = findViewById(R.id.edtMemberMobile);
        edtMemberEmail = findViewById(R.id.edtMemberEmail);
        edtGroupName = findViewById(R.id.edtGroupName);
        edtTotalAmount = findViewById(R.id.edtTotalAmount);
        edtMonthlyAmount = findViewById(R.id.edtMonthlyAmount);
        edtNameOfPresident = findViewById(R.id.edtNameOfPresident);

        List<String> members = new ArrayList<>();
        HashMap<String, Object> allData = new HashMap<>();
        List<Member> allMembers = new ArrayList<>();
        List<GhatGroup> allGhats = new ArrayList<>();

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


        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("BachatGhat");

        List<String> bachatGhats = new ArrayList<>();
        bachatGhats.add("Select Group Name");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if (snapshot.exists()) {
                    for (DataSnapshot notepad : snapshot.getChildren()) {
                        GhatGroup ghatGroup = notepad.getValue(GhatGroup.class);
                        bachatGhats.add(ghatGroup.getGroupName());
                        allGhats.add(ghatGroup);
                        allData.put(ghatGroup.getGroupName(), ghatGroup);
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, bachatGhats);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnGroupName.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Please check you internet connection Or it may be server error please try after some time !!!", Toast.LENGTH_LONG).show();
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, bachatGhats);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnGroupName.setAdapter(adapter);
            }
        });


        spnGroupName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (spnGroupName.getSelectedItem().toString().toLowerCase(Locale.ROOT).contains("select") == false) {

                    GhatGroup ghatGroup = (GhatGroup) allData.get(spnGroupName.getSelectedItem());

                    edtGroupName.setText(ghatGroup.getGroupName());
                    edtTotalAmount.setText(ghatGroup.getTotalAmount() + " Rs.");
                    edtMonthlyAmount.setText(ghatGroup.getMonthlyAmount() + " Rs.");
                    edtNameOfPresident.setText(ghatGroup.getNameOfPresident());

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spnSelectMember.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (spnSelectMember.getSelectedItem().toString().toLowerCase(Locale.ROOT).contains("select") == false) {

                    Member member = (Member) allData.get(spnSelectMember.getSelectedItem());
                    edtMemberName.setText(member.getName());
                    edtMemberMobile.setText(member.getMobile());
                    edtMemberEmail.setText(member.getEmail());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        findViewById(R.id.confirm_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spnGroupName.getSelectedItem().toString().toLowerCase(Locale.ROOT).contains("select")
                        || spnSelectMember.getSelectedItem().toString().toLowerCase(Locale.ROOT).contains("select")) {
                    Toast.makeText(MapGhatAndMemberActivity.this, "Please select Member and Group Name", Toast.LENGTH_SHORT).show();
                } else {
                    GhatGroup ghatGroup = (GhatGroup) allData.get(spnGroupName.getSelectedItem());
                    Member member = (Member) allData.get(spnSelectMember.getSelectedItem());
                    submitForm(ghatGroup, member);

                }
            }
        });
    }


    private void submitForm(GhatGroup ghatGroup, Member member) {

        // Show a loading dialog
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving Ghat data...");
        progressDialog.setCancelable(false); // Prevent user from dismissing the dialog
        progressDialog.show();

        // Now, you can save the data to Firebase Realtime Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("MembersGhatMap");
        String prescriptionId = databaseReference.push().getKey();

        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("key", prescriptionId);
        data.put("memberKey", member.getKey());
        data.put("GhatKey", ghatGroup.getGroupKey());
        data.put("member", member);
        data.put("ghat", ghatGroup);


        databaseReference.child(prescriptionId).setValue(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Prescription data saved successfully
                        // Dismiss the loading dialog
                        progressDialog.dismiss();
                        Toast.makeText(MapGhatAndMemberActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(MapGhatAndMemberActivity.this, "Error " + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}