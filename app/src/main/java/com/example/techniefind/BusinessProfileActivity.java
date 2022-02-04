package com.example.techniefind;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import javax.annotation.Nonnull;

import de.hdodenhof.circleimageview.CircleImageView;

public class BusinessProfileActivity extends Fragment implements View.OnClickListener {
    CircleImageView bProfImage;
    TextView bProfName,bProfBio,bProfContact,bProfNumber,bProfLocation;
    ImageButton bProfMenu,bProfEdit;
    private DatabaseReference bProfRef;
    private FirebaseAuth mAuth;
    String currentUserID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_business_profile,container,false);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        bProfRef = FirebaseDatabase.getInstance().getReference().child("All_Businesses");

        bProfImage= getActivity().findViewById(R.id.businessProfilePicture);
        bProfName = getActivity().findViewById(R.id.businessProfileName);
        bProfBio = getActivity().findViewById(R.id.businessProfileBio);
        bProfContact = getActivity().findViewById(R.id.businessProfileContact);
        bProfNumber = getActivity().findViewById(R.id.businessProfileNumber);
        bProfLocation = getActivity().findViewById(R.id.businessProfileLocation);
        bProfMenu = getActivity().findViewById(R.id.businessProfileMenu);
        bProfEdit = getActivity().findViewById(R.id.businessProfileEdit);
        bProfMenu.setOnClickListener(this);
        bProfEdit.setOnClickListener(this);

        bProfRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    if (snapshot.hasChild("businessName")){
                        String bName = snapshot.child("businessName").getValue().toString();
                        bProfName.setText(bName);
                    }
                    if (snapshot.hasChild("image")){
                        String bImage = snapshot.child("image").getValue().toString();
                        Picasso.get().load(bImage).placeholder(R.drawable.ic_baseline_person_24).into(bProfImage);
                    }
                    if (snapshot.hasChild("businessContact")){
                        String contact = snapshot.child("businessContact").getValue().toString();
                        bProfContact.setText(contact);
                    }
                    if (snapshot.hasChild("businessLocation")){
                        String location = snapshot.child("businessLocation").getValue().toString();
                        bProfLocation.setText(location);
                    }
                    if (snapshot.hasChild("businessBio")){
                        String bio = snapshot.child("businessBio").getValue().toString();
                        bProfBio.setText(bio);
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
            case R.id.businessProfileEdit:
                Intent intent = new Intent(getActivity(),EditProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.businessProfileMenu:
                BottomSheetMenu bottomSheetMenu = new BottomSheetMenu();
                bottomSheetMenu.show(getFragmentManager(),"bottomsheet");
                break;
        }
    }



}
