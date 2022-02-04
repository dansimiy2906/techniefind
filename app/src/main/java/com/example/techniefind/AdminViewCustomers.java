package com.example.techniefind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AdminViewCustomers extends AppCompatActivity {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference, bUsersRef;
    private RecyclerView recyclerView;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_customers);


        recyclerView = (RecyclerView) findViewById(R.id.rv_ViewCustomers);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseReference = database.getReference("All_Customers");


        FirebaseRecyclerOptions<CustomerInfoHolder> options =
                new FirebaseRecyclerOptions.Builder<CustomerInfoHolder>()
                        .setQuery(databaseReference, CustomerInfoHolder.class)
                        .build();

        FirebaseRecyclerAdapter<CustomerInfoHolder, AdminViewCustomers.ViewHolderAdmin_Customers> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<CustomerInfoHolder, AdminViewCustomers.ViewHolderAdmin_Customers>(options) {

                    @NonNull
                    @Override
                    public AdminViewCustomers.ViewHolderAdmin_Customers onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.customer_view_admin, parent, false);
                        return new AdminViewCustomers.ViewHolderAdmin_Customers(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull AdminViewCustomers.ViewHolderAdmin_Customers holder, int position, @NonNull CustomerInfoHolder model) {
                        holder.location.setText(model.getCustomerLocation());
                        holder.name.setText(model.getCustomerName());

                        Picasso.get().load(model.getImage()).into(holder.imageView);
                    }

                };
        firebaseRecyclerAdapter.startListening();

        recyclerView.setAdapter(firebaseRecyclerAdapter);


    }

    public static class ViewHolderAdmin_Customers extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView location, name, tvreview, tvchat, tvreport;


        public ViewHolderAdmin_Customers(@NonNull View itemView) {
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

