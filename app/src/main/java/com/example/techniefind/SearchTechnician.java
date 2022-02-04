package com.example.techniefind;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SearchTechnician extends Fragment implements View.OnClickListener {
    private Button search;
    private EditText search_technician;
    private FirebaseDatabase database  = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference,cUsersRef;
    private RecyclerView recyclerView;
    ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.search_technician,container,false);

        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String CurrentUserID = user.getUid();
        cUsersRef = FirebaseDatabase.getInstance().getReference().child("All_Customers").child(CurrentUserID);

        recyclerView = getActivity().findViewById(R.id.rv_search);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        search = (Button) getActivity().findViewById(R.id.btn_search_technician);
        search_technician = (EditText) getActivity().findViewById(R.id.search);
        databaseReference = database.getReference("All_Businesses");

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textinput = search_technician.getText().toString();
                Query query = databaseReference.orderByChild("businessLocation").
                        equalTo(textinput);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            FirebaseRecyclerOptions<BusinessInfoHolder>options =
                                    new FirebaseRecyclerOptions.Builder<BusinessInfoHolder>()
                                    .setQuery(query,BusinessInfoHolder.class)
                                    .build();


                            FirebaseRecyclerAdapter<BusinessInfoHolder,ViewHolder_Businesses> firebaseRecyclerAdapter =
                                    new FirebaseRecyclerAdapter<BusinessInfoHolder, ViewHolder_Businesses>(options) {
                                        @Override
                                        protected void onBindViewHolder(@NonNull ViewHolder_Businesses holder, int position, @NonNull BusinessInfoHolder model) {
                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                            final String currentUserID = user.getUid();
                                            final String postkey = getRef(position).getKey();

                                            holder.setitem(getActivity(),model.getImage(),model.getBusinessName(), model.getBusinessLocation());
                                            holder.Add_job.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    String business_id = getRef(position).getKey();
                                                    Intent job = new Intent(getActivity(),AddJobActivity.class);
                                                    job.putExtra("business_id",business_id);

                                                    startActivity(job);
                                                }
                                            });

                                            holder.tvchat.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    String business_id = getRef(position).getKey();
                                                    Intent toChat = new Intent(getActivity(),ChatActivity.class);
                                                    toChat.putExtra("business_id",business_id);
                                                    startActivity(toChat);
                                                }
                                            });

                                            holder.tvreport.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {

                                                }
                                            });

                                            holder.tvreview.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    String business_id = getRef(position).getKey();
                                                    Intent toReview = new Intent(getActivity(),ReviewActivity.class);
                                                    toReview.putExtra("business_id",business_id);
                                                    startActivity(toReview);
                                                }
                                            });

                                        }

                                        @NonNull
                                        @Override
                                        public ViewHolder_Businesses onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                            View view = LayoutInflater.from(parent.getContext())
                                                    .inflate(R.layout.businesses_display,parent,false);
                                            return new ViewHolder_Businesses(view);
                                        }
                                    };
                            firebaseRecyclerAdapter.startListening();
                            recyclerView.setAdapter(firebaseRecyclerAdapter);
                        }
                        else{
                            Toast.makeText(getActivity(), "This Location Has No Technicians", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }

    @Override
    public void onClick(View view) {

    }
}
