package rule.blockchain.penalty;

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

import rule.blockchain.R;
import rule.blockchain.UserSession;

public class AllPenaltyActivity extends AppCompatActivity {

    UserSession userSession;

    DatabaseReference mDatabaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_penalty);

        final ProgressDialog progressDialog = new ProgressDialog(AllPenaltyActivity.this);
        progressDialog.setMessage("Fetching Data");
        progressDialog.setCancelable(false);
        progressDialog.show();
        userSession = new UserSession(getApplicationContext());


        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Penalty");
        ArrayList<Penalty> allEvents = new ArrayList<>();

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

                        Penalty event = notepad.getValue(Penalty.class);
                        if (userSession.getReferCode().isEmpty()) {
                            allEvents.add(event);
                        } else if (event.getMemberName().equalsIgnoreCase(userSession.getFirstName())) {
                            allEvents.add(event);
                        }
                    }

                    RecyclerView recyclerView = findViewById(R.id.recycle_view);
                    PenaltyAdapter itemAdapter = new PenaltyAdapter(allEvents, AllPenaltyActivity.this);
                    GridLayoutManager gridLayout = new GridLayoutManager(AllPenaltyActivity.this, 1);
                    recyclerView.setAdapter(itemAdapter);
                    recyclerView.setLayoutManager(gridLayout);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                progressDialog.cancel();

                Toast.makeText(AllPenaltyActivity.this, "Please check you internet connection Or it may be server error please try after some time !!!", Toast.LENGTH_LONG).show();
            }
        });
    }
}