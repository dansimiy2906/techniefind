package com.example.techniefind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    private String id;
    private EditText textMessage;
    private ImageButton sendText;
    private DatabaseReference jobRef;
    private String currentUserID;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        jobRef = FirebaseDatabase.getInstance().getReference();

        id = getIntent().getExtras().get("business_id").toString();
        sendText = (ImageButton) findViewById(R.id.send_message_btn) ;

        textMessage = (EditText) findViewById(R.id.input_message) ;

        sendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddJob();

            }
        });


    }

    private void AddJob() {
        String text = textMessage.getText().toString();


        if (TextUtils.isEmpty(text)){
            Toast.makeText(this, "Input Message", Toast.LENGTH_SHORT).show();
        }

        else{
            String jobsentref = "Messages/" + currentUserID + "/" + id;
            String jobrecievedref = "Messages/" + id + "/" + currentUserID;

            //create job unique key
            DatabaseReference jobspostedkey = jobRef.child("Messages").child(currentUserID).child(id)
                    .push();
            String jobs_push_id = jobspostedkey.getKey();

            Map jobsave = new HashMap();
            jobsave.put("Message:",text);
            jobsave.put("From",currentUserID);

            Map jobDetails = new HashMap();
            jobDetails.put(jobsentref + "/" + jobs_push_id, jobsave);
            jobDetails.put(jobrecievedref + "/" + jobs_push_id, jobsave);

            jobRef.updateChildren(jobDetails).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()){
                        Toast.makeText(ChatActivity.this, "Message Sent Succcessfully", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        String message = task.getException().toString();
                        Toast.makeText(ChatActivity.this, "Error Occured:"+ message, Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }


    }
}