package rule.blockchain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class AllMembersActivity extends AppCompatActivity {


    DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_prescription);

        String ghatKey = (String) getIntent().getExtras().get("ghatKey");

        final ProgressDialog progressDialog = new ProgressDialog(AllMembersActivity.this);
        progressDialog.setMessage("Fetching Data");
        progressDialog.setCancelable(false);
        progressDialog.show();


        if (ghatKey.equalsIgnoreCase("pass")) {
            mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Members");
            ArrayList<Member> allEvents = new ArrayList<>();
            mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    allEvents.clear();

                    progressDialog.dismiss();
                    progressDialog.cancel();


                    if (snapshot.exists()) {
                        // dataSnapshot is the "notepad" node with all children with id
                        for (DataSnapshot notepad : snapshot.getChildren()) {

                            Log.e("Note[pad", notepad.toString());

                            Member event = notepad.getValue(Member.class);
                            allEvents.add(event);
                        }

                        RecyclerView recyclerView = findViewById(R.id.recycle_view);
                        PrescriptionAdapter itemAdapter = new PrescriptionAdapter(allEvents, AllMembersActivity.this);
                        GridLayoutManager gridLayout = new GridLayoutManager(AllMembersActivity.this, 1);
                        recyclerView.setAdapter(itemAdapter);
                        recyclerView.setLayoutManager(gridLayout);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    progressDialog.dismiss();
                    progressDialog.cancel();

                    Toast.makeText(AllMembersActivity.this, "Please check you internet connection Or it may be server error please try after some time !!!", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("MembersGhatMap");
            ArrayList<Member> allEvents = new ArrayList<>();

            mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    allEvents.clear();

                    progressDialog.dismiss();
                    progressDialog.cancel();


                    if (snapshot.exists()) {
                        // dataSnapshot is the "notepad" node with all children with id
                        for (DataSnapshot notepad : snapshot.getChildren()) {


                            MapGhatMember objectHashMap = notepad.getValue(MapGhatMember.class);

                            if (objectHashMap.getGhatKey().equalsIgnoreCase(ghatKey)){
                            Member event = objectHashMap.getMember();
                            allEvents.add(event);
                            }
                        }

                        RecyclerView recyclerView = findViewById(R.id.recycle_view);
                        PrescriptionAdapter itemAdapter = new PrescriptionAdapter(allEvents, AllMembersActivity.this);
                        GridLayoutManager gridLayout = new GridLayoutManager(AllMembersActivity.this, 1);
                        recyclerView.setAdapter(itemAdapter);
                        recyclerView.setLayoutManager(gridLayout);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    progressDialog.dismiss();
                    progressDialog.cancel();

                    Toast.makeText(AllMembersActivity.this, "Please check you internet connection Or it may be server error please try after some time !!!", Toast.LENGTH_LONG).show();
                }
            });

        }

    }
}