package com.example.techniefind;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ViewHolder_Businesses extends RecyclerView.ViewHolder  {
    ImageView imageView;
    TextView location,name,tvreview,tvchat,tvreport;
    Button Add_job;
    
    private FirebaseAuth mAuth;
    private DatabaseReference Usersref,postsRef;
    String currentUserID;

    public ViewHolder_Businesses(@NonNull View itemView) {
        super(itemView);
    }

    public void setitem(FragmentActivity activity,String Image,String BusinessName,String BusinessLocation) {
        imageView = itemView.findViewById(R.id.iv_profile);
        location = itemView.findViewById(R.id.location);
        name = itemView.findViewById(R.id.user_name);
        Add_job = itemView.findViewById(R.id.btn_search_technician);
        tvreview = itemView.findViewById(R.id.review);
        tvchat = itemView.findViewById(R.id.chat);
        tvreport = itemView.findViewById(R.id.report);


        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        Usersref = FirebaseDatabase.getInstance().getReference().child("All_Businesses");

        Picasso.get().load(Image).placeholder(R.drawable.ic_baseline_person_24).into(imageView);
        location.setText("Location:" + " " + BusinessLocation);
        name.setText("Business Name:" + " " + BusinessName);

    }
}
