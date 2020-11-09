package com.example.bankappp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;

import com.example.bankappp.Model.Transaction;
import com.example.bankappp.R;

import java.util.ArrayList;

public class TransactionAdapter extends ArrayAdapter<Transaction> {

    private Context context;
    private int resource;

    public TransactionAdapter(Context context, int resource, ArrayList<Transaction> transactions) {
        super(context, resource, transactions);

        this.context = context;
        this.resource = resource;
    }

    @Override
    @NonNull
    public View getView (int position, View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(resource, parent, false);
        }

        Transaction transaction = getItem(position);

        ImageView imgTransactionIcon = convertView.findViewById(R.id.img_transaction);
        TextView txtTransactionTitle = convertView.findViewById(R.id.txt_transaction_type_id);
        TextView txtTransactionTimestamp = convertView.findViewById(R.id.txt_transaction_timestamp);
        TextView txtTransactionInfo = convertView.findViewById(R.id.txt_transaction_info);
        txtTransactionInfo.setVisibility(View.VISIBLE);
        TextView txtTransactionAmount = convertView.findViewById(R.id.txt_transaction_amount);

        txtTransactionTitle.setText(transaction.getTransactionType().toString() + " - " + transaction.getTransactionID());
        txtTransactionTimestamp.setText(transaction.getTimestamp());
        txtTransactionAmount.setText("RS." + String.format("%.2f",transaction.getAmount()));

        if (transaction.getTransactionType() == Transaction.TRANSACTION_TYPE.PAYMENT) {
            imgTransactionIcon.setImageResource(R.drawable.rupee);
            txtTransactionInfo.setText("To Payee: " + transaction.getPayee());
            txtTransactionAmount.setTextColor(Color.RED);
        } else if (transaction.getTransactionType() == Transaction.TRANSACTION_TYPE.TRANSFER) {
            imgTransactionIcon.setImageResource(R.drawable.lst_transfer_icon);
            txtTransactionInfo.setText("From: " + transaction.getSendingAccount() + " - " + "To: " + transaction.getDestinationAccount());
            txtTransactionAmount.setTextColor(getContext().getResources().getColor(android.R.color.holo_blue_light));
        } else if (transaction.getTransactionType() == Transaction.TRANSACTION_TYPE.DEPOSIT) {
            imgTransactionIcon.setImageResource(R.drawable.deposit);
            txtTransactionInfo.setVisibility(View.GONE);
            txtTransactionAmount.setTextColor(getContext().getResources().getColor(android.R.color.holo_green_dark));
        }

        return convertView;
    }
}
