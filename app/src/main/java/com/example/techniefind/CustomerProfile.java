package com.example.techniefind;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerProfile extends Fragment implements View.OnClickListener {

    CircleImageView cProfImage;
    TextView cProfName,cProfContact,cProfNumber,cProfLocation;
    ImageButton cProfMenu,cProfEdit;
    private DatabaseReference cProfRef;
    private FirebaseAuth mAuth;
    String currentUserID;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.customer_profile_display,container,false);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        cProfRef = FirebaseDatabase.getInstance().getReference().child("All_Customers");

        cProfImage= getActivity().findViewById(R.id.customer_dp);
        cProfName = getActivity().findViewById(R.id.customer_name);
        cProfContact = getActivity().findViewById(R.id.customer_phone_number);
        cProfNumber = getActivity().findViewById(R.id.customer_id_number);
        cProfLocation = getActivity().findViewById(R.id.customer_location);
        cProfMenu = getActivity().findViewById(R.id.customer_menu);
        cProfEdit = getActivity().findViewById(R.id.customer_edit);
        cProfMenu.setOnClickListener(this);
        cProfEdit.setOnClickListener(this);

        cProfRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    if (snapshot.hasChild("customerName")){
                        String cName = snapshot.child("customerName").getValue().toString();
                        cProfName.setText(cName);
                    }
                    if (snapshot.hasChild("image")){
                        String cImage = snapshot.child("image").getValue().toString();
                        Picasso.get().load(cImage).placeholder(R.drawable.ic_baseline_person_24).into(cProfImage);
                    }
                    if (snapshot.hasChild("customerContact")){
                        String contact = snapshot.child("customerContact").getValue().toString();
                        cProfContact.setText(contact);
                    }
                    if (snapshot.hasChild("customerLocation")){
                        String location = snapshot.child("customerLocation").getValue().toString();
                        cProfLocation.setText(location);
                    }
                    if (snapshot.hasChild("customerIDNumber")){
                        String nationalID = snapshot.child("customerIDNumber").getValue().toString();
                        cProfNumber.setText(nationalID);
                    }

                    else {
                        Toast.makeText(getActivity(), "Profile Does Not Exist", Toast.LENGTH_SHORT).show();
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.customer_edit:
                Intent intent = new Intent(getActivity(),EditCustomerProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.customer_menu:
                BottomSheetMenu bottomSheetMenu = new BottomSheetMenu();
                bottomSheetMenu.show(getFragmentManager(),"bottomsheet");
                break;
        }

    }
}
