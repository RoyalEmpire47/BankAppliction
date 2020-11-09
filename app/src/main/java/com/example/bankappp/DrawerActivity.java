package com.example.bankappp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.example.bankappp.Model.Profile;
import com.example.bankappp.Model.db.ApplicationDB;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;


public class DrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    public enum manualNavID {
        DASHBOARD_ID,
        ACCOUNTS_ID
    }

    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;

    private SharedPreferences userPreferences;
    private Gson gson;
    private String json;

    private Profile userProfile;
    private DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            if (i == DialogInterface.BUTTON_POSITIVE) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("DisplayAccountDialog", true);
                manualNavigation(manualNavID.ACCOUNTS_ID, bundle);
            }
        }
    };

    public void manualNavigation(manualNavID id, Bundle bundle) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (id == manualNavID.DASHBOARD_ID) {
            ft.replace(R.id.flContent, new DashboardFragment()).commit();
            navView.setCheckedItem(R.id.nav_dashboard);
            setTitle("Dashboard");
        } else if (id == manualNavID.ACCOUNTS_ID) {
            AccountOverviewFragment accountOverviewFragment = new AccountOverviewFragment();
            if (bundle != null) {
                accountOverviewFragment.setArguments(bundle);
            }
            ft.replace(R.id.flContent, accountOverviewFragment).commit();
            navView.setCheckedItem(R.id.nav_accounts);
            setTitle("Accounts");
        }

        drawerLayout.closeDrawers();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        drawerLayout = findViewById(R.id.drawer_layout1);

        toolbar = findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);

        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
       // drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        userPreferences = this.getSharedPreferences("LastProfileUsed", MODE_PRIVATE);
        gson = new Gson();
        json = userPreferences.getString("LastProfileUsed", "");
        userProfile = gson.fromJson(json, Profile.class);

        loadFromDB();

        SharedPreferences.Editor prefsEditor = userPreferences.edit();
        json = gson.toJson(userProfile);
        prefsEditor.putString("LastProfileUsed", json).apply();

        setupHeader();

        manualNavigation(manualNavID.DASHBOARD_ID, null);
    }

    private void setupHeader() {

        View headerView = navView.getHeaderView(0);

        //ImageView imgProfilePic = findViewById(R.id.img_profile); //TODO: set the profile image
        TextView txtName = headerView.findViewById(R.id.txt_name);
        TextView txtUsername = headerView.findViewById(R.id.txt_username);

        String name = userProfile.getFirstName() + " " + userProfile.getLastName();
        txtName.setText(name);

        txtUsername.setText(userProfile.getUsername());
    }

    private void loadFromDB() {
        ApplicationDB applicationDb = new ApplicationDB(getApplicationContext());

        userProfile.setPayeesFromDB(applicationDb.getPayeesFromCurrentProfile(userProfile.getDbId()));
        userProfile.setAccountsFromDB(applicationDb.getAccountsFromCurrentProfile(userProfile.getDbId()));

        for (int iAccount = 0; iAccount < userProfile.getAccounts().size(); iAccount++) {
            userProfile.getAccounts().get(iAccount).setTransactions(applicationDb.getTransactionsFromCurrentAccount(userProfile.getDbId(), userProfile.getAccounts().get(iAccount).getAccountNo()));
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    public void showDrawerButton() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        toggle.syncState();
    }

    public void showUpButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void displayAccountAlertADialog(String option) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(String.format("%s Error", option))
                .setMessage(String.format("You do not have enough accounts to make a %s. Add another account if you want to make a %s.", option, option.toLowerCase()))
                .setNegativeButton("Cancel", dialogClickListener)
                .setPositiveButton("Add Account", dialogClickListener);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    public void displayHelpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Help")
                .setMessage("This Bank App Demo was made by Akshay KAdapa.");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            displayHelpDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        userPreferences = this.getSharedPreferences("LastProfileUsed", MODE_PRIVATE);
        gson = new Gson();
        json = userPreferences.getString("LastProfileUsed", "");
        userProfile = gson.fromJson(json, Profile.class);

        FragmentManager fragmentManager = getSupportFragmentManager();

        // Handle navigation view item clicks here.
        Class fragmentClass = null;
        String title = item.getTitle().toString();

        switch(item.getItemId()) {
            case R.id.nav_dashboard:
                fragmentClass = DashboardFragment.class;
                break;
            case R.id.nav_accounts:
                fragmentClass = AccountOverviewFragment.class;
                break;
            case R.id.nav_deposit:
                if (userProfile.getAccounts().size() > 0) {
                    title = "Deposit";
                    fragmentClass = DepositeFragment.class;
                } else {
                   displayAccountAlertADialog("Deposit");
                }
                break;
            case R.id.nav_transfer:
                if (userProfile.getAccounts().size() < 2) {
                    displayAccountAlertADialog("Transfer");
                } else {
                    title = "Transfer";
                    fragmentClass = TransferFragment.class;
                }
                break;
            case R.id.nav_payment:
                if (userProfile.getAccounts().size() < 1) {
                    displayAccountAlertADialog("Payment");
                } else {
                    title = "Payment";
                    fragmentClass = PaymentFragment.class;
                }
                break;
            case R.id.nav_logout:
                Toast.makeText(this, "Logging out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), LoginAcitviy.class);
                startActivity(intent);
                finish();
                break;
            default:
                fragmentClass = DashboardFragment.class;
        }

        try {
            Fragment fragment = (Fragment) fragmentClass.newInstance();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

            item.setChecked(true);
            setTitle(title);
            drawerLayout.closeDrawers();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }


}
