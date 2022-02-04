package com.example.techniefind;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class JobsAdapter  extends RecyclerView.Adapter<JobsAdapter.JobsViewHolder> {
    private List<Jobs> alljobs;
    private FirebaseAuth mAuth;
    private DatabaseReference jobsDbRef;

    public JobsAdapter(List<Jobs> alljobs){
        this.alljobs = alljobs;



    }

    public class JobsViewHolder extends RecyclerView.ViewHolder {
        public TextView v_name,v_location,v_dtype,v_prange,v_problem;
        public ImageView v_customer_profile_image;

        public JobsViewHolder(@NonNull View itemView) {
            super(itemView);

            v_name = (TextView) itemView.findViewById(R.id.j_user_name);
            v_location = (TextView) itemView.findViewById(R.id.j_location);
            v_dtype = (TextView) itemView.findViewById(R.id.j_deviceType);
            v_prange = (TextView) itemView.findViewById(R.id.j_range);
            v_problem = (TextView) itemView.findViewById(R.id.j_probleTobeSolved);
            v_customer_profile_image = (ImageView) itemView.findViewById(R.id.j_iv_profile);




        }

    }

    @NonNull
    @Override
    public JobsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.jobs_view_layout,parent,false);
        mAuth = FirebaseAuth.getInstance();
        return new JobsViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull JobsViewHolder holder, int position) {
        String userid = mAuth.getCurrentUser().getUid();
        Jobs jobs = alljobs.get(position);

        String fromUserId = jobs.getFrom();
        jobsDbRef = FirebaseDatabase.getInstance().getReference().child("All_Customers").child(fromUserId);
        jobsDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String dp = snapshot.child("image").getValue().toString();


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
