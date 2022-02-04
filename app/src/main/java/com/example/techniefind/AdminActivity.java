package com.example.techniefind;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AdminActivity extends AppCompatActivity {
    private CardView viewCustomers,viewBusinesses,viewReports,complaints,addCustomers,addBusinesses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        viewCustomers = (CardView) findViewById(R.id.View_users);
        viewBusinesses = (CardView) findViewById(R.id.View_Businesses);
        viewReports = (CardView) findViewById(R.id.reports);
        complaints = (CardView) findViewById(R.id.complaints);
        addCustomers = (CardView) findViewById(R.id.add_user);
        addBusinesses = (CardView) findViewById(R.id.add_business);

        viewCustomers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent vCustomers = new Intent(AdminActivity.this,AdminViewCustomers.class);
                startActivity(vCustomers);
                finish();
            }
        });

        viewBusinesses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent vBusinesses = new Intent(AdminActivity.this,AdminViewBusinesses.class);
                startActivity(vBusinesses);
                finish();
            }
        });

        viewReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent vReports = new Intent(AdminActivity.this,AdminViewSystemReports.class);
                startActivity(vReports);
                finish();
            }
        });

        complaints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent vComplaints = new Intent(AdminActivity.this,AdminComplaintsActivity.class);
                startActivity(vComplaints);
                finish();
            }
        });
        addCustomers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent aCustomers = new Intent(AdminActivity.this,AdminAddCustomers.class);
                startActivity(aCustomers);
                finish();
            }
        });
        addBusinesses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent aBusinesses = new Intent(AdminActivity.this,AdminAddBusinesses.class);
                startActivity(aBusinesses);
                finish();
            }
        });






    }
}