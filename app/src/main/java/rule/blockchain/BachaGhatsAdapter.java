package rule.blockchain;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class BachaGhatsAdapter extends RecyclerView.Adapter<BachaGhatsAdapter.ItemViewHolder> {

    private Context mcontext;
    private List<GhatGroup> mItemList;

    public BachaGhatsAdapter(@NonNull List<GhatGroup> ItemList, Context context) {
        this.mcontext = context;
        this.mItemList = ItemList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ghat_item, parent, false);
        return new ItemViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, int position) {

        GhatGroup request = mItemList.get(position);
        Log.e("request ", request.toString());

        holder.txtPatientName.setText("Group Name: " + request.getGroupName());
        holder.txtAge.setText("Total Amount: " + request.getTotalAmount() + " Rs.");
        holder.txtWeight.setText("Monthly Amount: " + request.getMonthlyAmount() + " Rs.");
        holder.txtHeight.setText("Total Members: " + request.getTotalMembers());
        holder.txtDate.setText("Name Of President: " + request.getNameOfPresident());
        holder.txtHospital.setText("Email Of President: " + request.getChairmanEmail());
        holder.txtMedicalCondition.setText("Mobile Of President: " + request.getMobileOfPresident());
        holder.btnViewMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, AllMembersActivity.class);
                intent.putExtra("ghatKey",request.getGroupKey());
                mcontext.startActivity(intent);


            }
        });

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
        Button btnDownloadPrescription, btnDownloadReport, btnViewMembers;

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
            btnViewMembers = itemView.findViewById(R.id.confirm_button);
        }
    }
}


