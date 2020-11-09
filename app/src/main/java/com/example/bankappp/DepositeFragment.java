package com.example.bankappp;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bankappp.Model.Account;
import com.example.bankappp.Model.Profile;
import com.example.bankappp.Model.db.ApplicationDB;
import com.google.gson.Gson;

import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.VISIBLE;

public class DepositeFragment extends Fragment {
    private Spinner spnAccounts;
    private ArrayAdapter<Account> accountAdapter;
    private EditText edtDepositAmount;
    private Button btnCancel;
    private Button btnDeposit;

    private Profile userProfile;

    private SharedPreferences userPreferences;
    private Gson gson;
    private String json;

    private TextView coundowntimer;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_deposite, container, false);

        userPreferences = getContext().getSharedPreferences("LastProfileUsed", MODE_PRIVATE);
        gson = new Gson();
        json = userPreferences.getString("LastProfileUsed", "");
        userProfile = gson.fromJson(json, Profile.class);


        spnAccounts = v.findViewById(R.id.dep_spn_accounts1);
        accountAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, userProfile.getAccounts());
        accountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnAccounts.setAdapter(accountAdapter);
        spnAccounts.setSelection(0);

        edtDepositAmount = v.findViewById(R.id.edt_deposit_amount1);

        btnCancel = v.findViewById(R.id.btn_cancel_deposit1);
        btnDeposit = v.findViewById(R.id.btn_deposit1);
        coundowntimer =v.findViewById(R.id.CoundowntimerD);


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Deposit Cancelled", Toast.LENGTH_SHORT).show();
                //////CoundownTImer
                coundownTimer();
            }
        });

        btnDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeDeposit();
            }
        });

        //////////////BAck to DashBoard
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                //////CoundownTImer
                coundownTimer();

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        return v;
    }


    private void makeDeposit() {

        int selectedAccountIndex = spnAccounts.getSelectedItemPosition();

        double depositAmount = 0;
        boolean isNum = false;

        try {
            depositAmount = Double.parseDouble(edtDepositAmount.getText().toString());
            isNum = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (depositAmount < 0.01 && !isNum) {
            Toast.makeText(getContext(), "Please enter a valid amount", Toast.LENGTH_SHORT).show();
        } else {

            Account account = userProfile.getAccounts().get(selectedAccountIndex);
            account.addDepositTransaction(depositAmount);

            SharedPreferences.Editor prefsEditor = userPreferences.edit();
            gson = new Gson();
            json = gson.toJson(userProfile);
            prefsEditor.putString("LastProfileUsed", json).apply();

            ApplicationDB applicationDb = new ApplicationDB(getContext());
            applicationDb.overwriteAccount(userProfile, account);
            applicationDb.saveNewTransaction(userProfile, account.getAccountNo(),
                    account.getTransactions().get(account.getTransactions().size()-1));

            Toast.makeText(getContext(), "Deposit of RS." + String.format(Locale.getDefault(), "%.2f",depositAmount) + " " + "made successfully", Toast.LENGTH_SHORT).show();

            accountAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, userProfile.getAccounts());
            accountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnAccounts.setAdapter(accountAdapter);
            ///////CoundownTImer
            coundownTimer();

        }
    }

    private void coundownTimer()
    {
        ///////CoundownTImer
        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
                coundowntimer.setVisibility(VISIBLE);
                coundowntimer.setText("Please Wait It will Be Redirected To Home Page : " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }
            public void onFinish() {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flContent, new DashboardFragment())
                        .commit();
                getActivity().setTitle("Dashboard");
            }

        }.start();
    }

}