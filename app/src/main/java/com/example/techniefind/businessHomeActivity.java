package com.example.techniefind;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class businessHomeActivity extends Fragment implements View.OnClickListener {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference, bUsersRef;
    private RecyclerView recyclerView;
    ImageView imageView;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.business_home_page, container, false);
        return view;


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView = getActivity().findViewById(R.id.rv_display_jobs);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        databaseReference = database.getReference("Jobs");



        FirebaseRecyclerOptions<JobsInfoHolder> options =
                new FirebaseRecyclerOptions.Builder<JobsInfoHolder>()
                        .setQuery(databaseReference, JobsInfoHolder.class)
                        .build();

        FirebaseRecyclerAdapter<JobsInfoHolder, businessHomeActivity.ViewHolderJobs_Business> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<JobsInfoHolder, businessHomeActivity.ViewHolderJobs_Business>(options) {

                    @NonNull
                    @Override
                    public businessHomeActivity.ViewHolderJobs_Business onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.jobs_view_layout, parent, false);
                        return new businessHomeActivity.ViewHolderJobs_Business(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull businessHomeActivity.ViewHolderJobs_Business holder, int position, @NonNull JobsInfoHolder model) {


                        holder.probelem_to_fix.setText(model.getProblemToFix());


                    }

                };


    }

    public static class ViewHolderJobs_Business extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView location, price_range,probelem_to_fix;


        public ViewHolderJobs_Business(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.j_iv_profile);
            price_range = itemView.findViewById(R.id.j_range);
            probelem_to_fix = itemView.findViewById(R.id.j_probleTobeSolved);

        }


    }




    @Override
    public void onClick(View view) {

    }
}
