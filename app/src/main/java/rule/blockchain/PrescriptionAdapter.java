package rule.blockchain;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;


public class PrescriptionAdapter extends RecyclerView.Adapter<PrescriptionAdapter.ItemViewHolder> {

    private Context mcontext;
    private List<Member> mItemList;

    public PrescriptionAdapter(@NonNull List<Member> ItemList, Context context) {
        this.mcontext = context;
        this.mItemList = ItemList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.prescription_item, parent, false);
        return new ItemViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, int position) {

        Member request = mItemList.get(position);
        Log.e("request ", request.toString());

        holder.txtPatientName.setText("Name: " + request.getName());

        holder.txtAge.setText("Age: " + request.getAge() + " Yrs.");
        holder.txtWeight.setText("Mobile: " + request.getMobile());
        holder.txtHeight.setText("Email: " + request.getEmail());
        holder.txtDate.setText("PAN No.: " + request.getPanNumber());
        holder.txtHospital.setText("Adhar No.: " + request.getAdharNumber());
        holder.txtMedicalCondition.setText("Address: " + request.getAddress());

//        holder.btnDownloadPrescription.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String prescriptionDownloadUrl = request.getPrescriptionDownloadUrl();
//                if (prescriptionDownloadUrl == null) {
//                    Toast.makeText(mcontext, "File is not available", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if (prescriptionDownloadUrl.isEmpty()) {
//                    Toast.makeText(mcontext, "File is not available", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                openLinkInBrowser(mcontext, prescriptionDownloadUrl);
//            }
//        });
//
//        holder.btnDownloadReport.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String reportDownloadUrl = request.getReportDownloadUrl();
//                if (reportDownloadUrl == null) {
//                    Toast.makeText(mcontext, "File is not available", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if (reportDownloadUrl.isEmpty()) {
//                    Toast.makeText(mcontext, "File is not available", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                openLinkInBrowser(mcontext, reportDownloadUrl);
//            }
//        });


    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public static void openLinkInBrowser(Context context, String url) {
        // Create an Intent with ACTION_VIEW and the URL
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));

        context.startActivity(intent);

    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView txtPatientName;
        TextView txtAge;
        TextView txtWeight;
        TextView txtHeight;
        TextView txtDate;
        TextView txtHospital;
        TextView txtMedicalCondition;
        TextView txtDescription;
        Button btnDownloadPrescription, btnDownloadReport;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPatientName = itemView.findViewById(R.id.txtPatientName);
            txtAge = itemView.findViewById(R.id.txtAge);
            txtWeight = itemView.findViewById(R.id.txtWeight);
            txtHeight = itemView.findViewById(R.id.txtHeight);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtHospital = itemView.findViewById(R.id.txtHospital);
            txtMedicalCondition = itemView.findViewById(R.id.txtMedicalCondition);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            btnDownloadReport = itemView.findViewById(R.id.btnDownloadReport);
            btnDownloadPrescription = itemView.findViewById(R.id.btnDownloadPrescription);
        }
    }
}


