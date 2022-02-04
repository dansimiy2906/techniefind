package com.example.techniefind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddJobActivity extends AppCompatActivity {
    private String id;
    private EditText deviceType,pricerange,issue;
    private Button addBtn;
    private DatabaseReference jobRef;
    String currentUserID;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        jobRef = FirebaseDatabase.getInstance().getReference().child("Jobs");
        
        id = getIntent().getExtras().get("business_id").toString();
        addBtn = (Button) findViewById(R.id.button_jobs) ;

        deviceType = (EditText) findViewById(R.id.device) ;
        pricerange = (EditText) findViewById(R.id.PriceRange);
        issue = (EditText) findViewById(R.id.issue);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddJob();

            }
        });


    }

    private void AddJob() {
        String type = deviceType.getText().toString();
        String dissue = issue.getText().toString();

        if (TextUtils.isEmpty(type)){
            Toast.makeText(this, "Input Device Type", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(dissue)){
            Toast.makeText(this, "Input Problem", Toast.LENGTH_SHORT).show();
        }

        else{

            Map jobsave = new HashMap();
            jobsave.put("Type",type);
            jobsave.put("ProblemToFix",dissue);
            jobsave.put("From",currentUserID);
            jobsave.put("To",id);



            jobRef.updateChildren(jobsave).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()){
                        Toast.makeText(AddJobActivity.this, "Job Added Succcessfully", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        String message = task.getException().toString();
                        Toast.makeText(AddJobActivity.this, "Error Occured:"+ message, Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }

        
    }
}