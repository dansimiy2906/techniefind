package com.example.techniefind;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditCustomerProfileActivity extends AppCompatActivity {
    private EditText edit_cName,edit_cIDNumber,edit_cLocation,edit_cPhoneNumber;
    private Button edit_registerCustomer;
    private CircleImageView edit_profileImage;
    final static int Gallery_pick = 1;
    private FirebaseAuth mAuth;
    private DatabaseReference editcustomerRef;
    private StorageReference customerImageRef;
    private ProgressDialog loadingbar;
    private FirebaseFirestore fStore;
    private DocumentReference documentReference;

    final static int Gallery_Pick = 1;

    private StorageReference UserProfileImageRef;
    private Uri ImageUri;


    CustomerInfo info;

    Uri imageUri;
    UploadTask uploadTask;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    String currentUserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_customer_profile);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        editcustomerRef = FirebaseDatabase.getInstance().getReference().child("All_Customers").child(currentUserID);

        edit_cName = (EditText) findViewById(R.id.edit_customerName);
        edit_cIDNumber = (EditText) findViewById(R.id.edit_customerIDNumber);
        edit_cLocation = (EditText) findViewById(R.id.edit_customerLocation);
        edit_cPhoneNumber = (EditText) findViewById(R.id.edit_customerContact);
        edit_profileImage = (CircleImageView) findViewById(R.id.edit_customerImage);

        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("cPrifileImage");

        edit_registerCustomer = (Button) findViewById(R.id.edit_btnCustomerRegistration);
        loadingbar = new ProgressDialog(this);

        editcustomerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String previous_cName = snapshot.child("customerName").getValue().toString();
                    String previous_cIDNumber = snapshot.child("customerIDNumber").getValue().toString();
                    String previous_cLocation = snapshot.child("customerLocation").getValue().toString();
                    String previous_cPhoneNumber = snapshot.child("customerContact").getValue().toString();
                    String previous_profileImage = snapshot.child("image").getValue().toString();

                    Picasso.get().load(previous_profileImage).placeholder(R.drawable.ic_baseline_person_24).into(edit_profileImage);
                    edit_cName.setText(previous_cName);
                    edit_cIDNumber.setText(previous_cIDNumber);
                    edit_cLocation.setText(previous_cLocation);
                    edit_cPhoneNumber.setText(previous_cPhoneNumber);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        edit_registerCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DoEdit();
            }
        });

        edit_profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, Gallery_Pick);
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


         if (requestCode == Gallery_pick || resultCode == RESULT_OK || data !=null || data.getData() != null){
            imageUri = data.getData();

            Picasso.get().load(imageUri).into(edit_profileImage);
        }
    }
    private String getFileExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType((contentResolver.getType(uri)));
    }


    private void DoEdit() {

        String updated_cName = edit_cName.getText().toString();
        String updated_cIDNumber = edit_cIDNumber.getText().toString();
        String updated_cLocation = edit_cLocation.getText().toString();
        String updated_cPhoneNumber = edit_cPhoneNumber.getText().toString();

        HashMap updatedprofile = new HashMap();
        updatedprofile.put("customerName",updated_cName);
        updatedprofile.put("customerIDNumber",updated_cIDNumber);
        updatedprofile.put("customerLocation",updated_cLocation);
        updatedprofile.put("customerContact",updated_cPhoneNumber);

        editcustomerRef.updateChildren(updatedprofile).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()){
                    Toast.makeText(EditCustomerProfileActivity.this, "Profile Has Been Edited", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(EditCustomerProfileActivity.this, "Error Occured While Editing Profile", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}