package thirdrdhand.smacktutorial.activities.transactions;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import thirdrdhand.smacktutorial.R;

/**
 * Created by pacit on 2017/08/30.
 */

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    List<Transaction> transactionList;

    public TransactionAdapter(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    @Override
    public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_card, parent, false);
        return new TransactionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TransactionViewHolder holder, int position) {

        Transaction trans = transactionList.get(position);
        holder.tvTitle.setText(trans.title);
        holder.tvStatus.setText(trans.statatus);
        holder.tvTo.setText(trans.transTo);
        holder.tvType.setText(trans.type);

    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {


        protected TextView tvTitle;
        protected TextView tvType;
        protected TextView tvTo;
        protected TextView tvStatus;

        public TransactionViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTransTitle);
            tvType = itemView.findViewById(R.id.tvTransType);
            tvTo = itemView.findViewById(R.id.tvTransTo);
            tvStatus = itemView.findViewById(R.id.tvTransStatus);
        }
    }

}
