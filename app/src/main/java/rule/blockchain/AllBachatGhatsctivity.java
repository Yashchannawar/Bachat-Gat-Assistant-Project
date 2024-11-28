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

public class AllBachatGhatsctivity extends AppCompatActivity {

    DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_bachat_ghatsctivity);

        final ProgressDialog progressDialog = new ProgressDialog(AllBachatGhatsctivity.this);
        progressDialog.setMessage("Fetching Data");
        progressDialog.setCancelable(false);
        progressDialog.show();


        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("BachatGhat");
        ArrayList<GhatGroup> allEvents = new ArrayList<>();

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

                        GhatGroup event = notepad.getValue(GhatGroup.class);
                        allEvents.add(event);
                    }

                    RecyclerView recyclerView = findViewById(R.id.recycle_view);
                    BachaGhatsAdapter itemAdapter = new BachaGhatsAdapter(allEvents, AllBachatGhatsctivity.this);
                    GridLayoutManager gridLayout = new GridLayoutManager(AllBachatGhatsctivity.this, 1);
                    recyclerView.setAdapter(itemAdapter);
                    recyclerView.setLayoutManager(gridLayout);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                progressDialog.cancel();

                Toast.makeText(AllBachatGhatsctivity.this, "Please check you internet connection Or it may be server error please try after some time !!!", Toast.LENGTH_LONG).show();
            }
        });

    }
}

