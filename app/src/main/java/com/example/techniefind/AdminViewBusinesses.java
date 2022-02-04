package com.example.techniefind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AdminViewBusinesses extends AppCompatActivity {
    private FirebaseDatabase database  = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference,bUsersRef;
    private RecyclerView recyclerView;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_businesses);


        recyclerView = (RecyclerView)findViewById(R.id.rv_ViewBusinesses);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseReference = database.getReference("All_Businesses");


        FirebaseRecyclerOptions<BusinessInfoHolder> options =
                new FirebaseRecyclerOptions.Builder<BusinessInfoHolder>()
                        .setQuery(databaseReference,BusinessInfoHolder.class)
                        .build();

        FirebaseRecyclerAdapter<BusinessInfoHolder,ViewHolderAdmin_Businesses> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<BusinessInfoHolder, ViewHolderAdmin_Businesses>(options) {

                    @NonNull
                    @Override
                    public ViewHolderAdmin_Businesses onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.businesses_view_admin,parent,false);
                        return new ViewHolderAdmin_Businesses(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull ViewHolderAdmin_Businesses holder, int position, @NonNull BusinessInfoHolder model) {
                        holder.location.setText(model.getBusinessLocation());
                        holder.name.setText(model.getBusinessName());

                       Picasso.get().load(model.getImage()).into(holder.imageView);
                    }

                };
        firebaseRecyclerAdapter.startListening();

        recyclerView.setAdapter(firebaseRecyclerAdapter);


    }
    public static class ViewHolderAdmin_Businesses extends RecyclerView.ViewHolder  {
        ImageView imageView;
        TextView location,name,tvreview,tvchat,tvreport;


        public ViewHolderAdmin_Businesses(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_profile);
            location = itemView.findViewById(R.id.location);
            name = itemView.findViewById(R.id.user_name);
            tvreview = itemView.findViewById(R.id.review);
            tvchat = itemView.findViewById(R.id.chat);
            tvreport = itemView.findViewById(R.id.report);

        }


    }



}
