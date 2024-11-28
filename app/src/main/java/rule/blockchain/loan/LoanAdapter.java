package rule.blockchain.loan;

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

import rule.blockchain.Member;
import rule.blockchain.R;


public class LoanAdapter extends RecyclerView.Adapter<LoanAdapter.ItemViewHolder> {

    private Context mcontext;
    private List<Loan> mItemList;

    public LoanAdapter(@NonNull List<Loan> ItemList, Context context) {
        this.mcontext = context;
        this.mItemList = ItemList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loan_item, parent, false);
        return new ItemViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, int position) {

        Loan loan = mItemList.get(position);
        Member request = loan.getMember();
        Log.e("request ", request.toString());

        holder.txtPatientName.setText("Name: " + request.getName());

        holder.txtAge.setText("Adhar No.: " + request.getAdharNumber());
        holder.txtWeight.setText("Mobile: " + request.getMobile());
        holder.txtHeight.setText("Email: " + request.getEmail());
        holder.txtDate.setText("Loan Amount : " + loan.getLoanAmount() + " Rs.");
        holder.txtHospital.setText("Interest Rate : " + loan.getLoanIntrest());
        holder.txtMedicalCondition.setText("Loan Tenure: " + loan.getLoanTenure());


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


