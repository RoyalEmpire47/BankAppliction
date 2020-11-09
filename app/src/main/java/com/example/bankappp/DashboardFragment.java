package com.example.bankappp;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bankappp.Model.Profile;
import com.google.gson.Gson;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;


public class DashboardFragment extends Fragment {

    private TextView txtWelcome;
    private TextView txtMessage;
    private Button btnAddAccount;

    private ImageView imgaccount,imagdeposit,imgpayment,imgfundtansf;

    public DashboardFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =   inflater.inflate(R.layout.fragment_dashboard, container, false);
        txtWelcome = rootView.findViewById(R.id.txt_welcome);
        txtMessage = rootView.findViewById(R.id.txt_details_msg);
        btnAddAccount = rootView.findViewById(R.id.btn_add_account);


        imgaccount =rootView.findViewById(R.id.img_account_dash);
        imagdeposit=rootView.findViewById(R.id.img_deposit);
        imgpayment=rootView.findViewById(R.id.img_payment);
        imgfundtansf=rootView.findViewById(R.id.img_fundtransfer);

        /////GO to Accounts
        imgaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flContent, new AccountOverviewFragment())
                        .commit();
                getActivity().setTitle("Accounts");

            }
        });

        /////////Go to Deposit
        imagdeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flContent, new DepositeFragment())
                        .commit();
                getActivity().setTitle("Deposite");
            }
        });

        //////////Go to Payment
        imgpayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flContent, new PaymentFragment())
                        .commit();
                getActivity().setTitle("Payment");
            }
        });

        ///////////Go to fund transfer
        imgfundtansf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flContent, new TransferFragment())
                        .commit();
                getActivity().setTitle("Transfer");
            }
        });
        setupViews();
        return rootView;

    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return super.getLifecycle();
    }

    /**
     * method used to setup the values for the views and fields
     */
    private void setupViews() {

        SharedPreferences userPreferences = getActivity().getSharedPreferences("LastProfileUsed", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = userPreferences.getString("LastProfileUsed", "");
        Profile userProfile = gson.fromJson(json, Profile.class);

        btnAddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("DisplayAccountDialog", true);
                ((DrawerActivity) getActivity()).manualNavigation(DrawerActivity.manualNavID.ACCOUNTS_ID, bundle);
            }
        });

        if (userProfile.getAccounts().size() == 0) {
            txtMessage.setVisibility(View.VISIBLE);
            btnAddAccount.setVisibility(View.VISIBLE);
            txtMessage.setText("You do not have any accounts, click below to add an account");
        } else {
            txtMessage.setVisibility(View.GONE);//TEMP to clear field
            btnAddAccount.setVisibility(View.GONE);

        }

        StringBuilder welcomeString = new StringBuilder();

        Calendar calendar = Calendar.getInstance();

        int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 5 && timeOfDay < 12) {
            welcomeString.append("Good Morning");
        } else if (timeOfDay >= 12 && timeOfDay < 17) {
            welcomeString.append("Good afternoon");
        } else {
            welcomeString.append("Good evening");
        }

        welcomeString.append(", ")
                .append(userProfile.getFirstName())
                .append(". Welcome to the Bank App Demo. ")
                .append(getString(R.string.happy))
                .append(" ");

        int day = calendar.get(Calendar.DAY_OF_WEEK);

        String[] days = getResources().getStringArray(R.array.days);
        String dow = "";

        switch(day) {
            case Calendar.SUNDAY:
                dow = days[0];
                break;
            case Calendar.MONDAY:
                dow = days[1];
                break;
            case Calendar.TUESDAY:
                dow = days[2];
                break;
            case Calendar.WEDNESDAY:
                dow = days[3];
                break;
            case Calendar.THURSDAY:
                dow = days[4];
                break;
            case Calendar.FRIDAY:
                dow = days[5];
                break;
            case Calendar.SATURDAY:
                dow = days[6];
                break;
            default:
                break;
        }

        welcomeString.append(dow)
                .append(".");

        txtWelcome.setText(welcomeString.toString());
    }

}
