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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class EditProfileActivity extends AppCompatActivity {
    private EditText edit_bName,edit_bNumber,edit_bLocation,edit_bPhoneNumber,edit_bBio;
    private Button edit_b_registerBusiness;
    private CircleImageView edit_b_profileImage;
    final static int Gallery_pick = 1;
    private FirebaseAuth mAuth;
    private DatabaseReference editbusinessRef;
    private StorageReference businessImageRef;
    private ProgressDialog loadingbar;
    private FirebaseFirestore fStore;
    private DocumentReference documentReference;

    BusinessInfo info;

    Uri imageUri;
    UploadTask uploadTask;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    String currentUserID;
    final static int Gallery_Pick = 1;

    private StorageReference UserProfileImageRef;
    private Uri ImageUri;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        editbusinessRef = FirebaseDatabase.getInstance().getReference().child("All_Businesses").child(currentUserID);

        edit_bName = (EditText) findViewById(R.id.edit_businessName);
        edit_bNumber = (EditText) findViewById(R.id.edit_businessNumber);
        edit_bLocation = (EditText) findViewById(R.id.edit_businessLocation);
        edit_bPhoneNumber = (EditText) findViewById(R.id.edit_businessContact);
        edit_bBio = (EditText) findViewById(R.id.edit_businessBio);
        edit_b_profileImage = (CircleImageView) findViewById(R.id.edit_businessImage);

        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("cPrifileImage");

        edit_b_registerBusiness = (Button) findViewById(R.id.edit_btnBusinessRegistration);
        loadingbar = new ProgressDialog(this);

        editbusinessRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String previous_bName = snapshot.child("businessName").getValue().toString();
                    String previous_bLocation = snapshot.child("businessLocation").getValue().toString();
                    String previous_bPhoneNumber = snapshot.child("businessContact").getValue().toString();
                    String previous_bBio = snapshot.child("businessBio").getValue().toString();
                    String previous_profileImage = snapshot.child("image").getValue().toString();

                    Picasso.get().load(previous_profileImage).placeholder(R.drawable.ic_baseline_person_24).into(edit_b_profileImage);
                    edit_bName.setText(previous_bName);
                    edit_bBio.setText(previous_bBio);
                    edit_bLocation.setText(previous_bLocation);
                    edit_bPhoneNumber.setText(previous_bPhoneNumber);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        edit_b_registerBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DoEdit();
            }
        });

        edit_b_profileImage.setOnClickListener(new View.OnClickListener() {
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

            Picasso.get().load(imageUri).into(edit_b_profileImage);
        }
    }
    private String getFileExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType((contentResolver.getType(uri)));
    }


    private void DoEdit() {

        String updated_bName = edit_bName.getText().toString();
        String updated_bBio = edit_bBio.getText().toString();
        String updated_bLocation = edit_bLocation.getText().toString();
        String updated_bPhoneNumber = edit_bPhoneNumber.getText().toString();

        HashMap updatedprofile = new HashMap();
        updatedprofile.put("customerName",updated_bName);
        updatedprofile.put("customerIDNumber",updated_bBio);
        updatedprofile.put("customerLocation",updated_bLocation);
        updatedprofile.put("customerContact",updated_bPhoneNumber);

        editbusinessRef.updateChildren(updatedprofile).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()){
                    Toast.makeText(EditProfileActivity.this, "Profile Has Been Edited", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(EditProfileActivity.this, "Error Occured While Editing Profile", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}