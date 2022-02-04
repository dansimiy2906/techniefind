package com.example.techniefind;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
//import com.theartofdev.edmodo.cropper.CropImage;
//import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
private EditText bName,bNumber,bLocation,bPhoneNumber,bBio;
private Button registerBusiness;
private CircleImageView profileImage;
final static int Gallery_pick = 1;
private FirebaseAuth mAuth;
private DatabaseReference businessRef;
private StorageReference businessImageRef;
private ProgressDialog loadingbar;
private FirebaseFirestore fStore;
private DocumentReference documentReference;

BusinessInfo info;

Uri imageUri;
UploadTask uploadTask;
FirebaseDatabase database = FirebaseDatabase.getInstance();
String currentUserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        info = new BusinessInfo();

        fStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        businessRef = FirebaseDatabase.getInstance().getReference("All_Businesses");
        businessImageRef = FirebaseStorage.getInstance().getReference().child("bPrifileImage");
        documentReference = fStore.collection("Users").document(currentUserID);


        bName = (EditText) findViewById(R.id.businessName);
        bNumber = (EditText) findViewById(R.id.businessNumber);
        bLocation = (EditText) findViewById(R.id.businessLocation);
        bPhoneNumber = (EditText) findViewById(R.id.businessNumber);
        bBio = (EditText) findViewById(R.id.businessBio);
        registerBusiness = (Button) findViewById(R.id.btnBusinessRegistration);
        profileImage = (CircleImageView) findViewById(R.id.businessImage);
        loadingbar = new ProgressDialog(this);

        registerBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveBusinessProfile();
            }
        });
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,Gallery_pick);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Gallery_pick || resultCode == RESULT_OK || data !=null || data.getData() != null){
            imageUri = data.getData();

            Picasso.get().load(imageUri).into(profileImage);
                    }
    }
    private String getFileExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType((contentResolver.getType(uri)));
    }

    private void saveBusinessProfile() {
        final String name = bName.getText().toString();
        final String number = bNumber.getText().toString();
        final String location = bLocation.getText().toString();
        final String phone = bPhoneNumber.getText().toString();
        final String bio = bBio.getText().toString();

        if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "Business Name Field Cannot be Empty!!", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(number)){
            Toast.makeText(this, "Business Number Field Cannot be Empty!!", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(location)){
            Toast.makeText(this, "Business Location Field Cannot be Empty!!", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Business Phone Number Field Cannot be Empty!!", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(bio)){
            Toast.makeText(this, "Business Bio Field Cannot be Empty!!", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingbar.setTitle("Uploading Business Profile Info");
            loadingbar.setMessage("This might take a few seconds...");
            loadingbar.show();
            loadingbar.setCanceledOnTouchOutside(true);

            final StorageReference reference = businessImageRef.child(System.currentTimeMillis()+ "."+getFileExt(imageUri));
            uploadTask = reference.putFile(imageUri);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloaduri = task.getResult();
                        Map<String,String> businessInfo = new HashMap<>();
                        businessInfo.put("Image",downloaduri.toString());
                        businessInfo.put("BusinessName",name);
                        businessInfo.put("BusinessNumber",number);
                        businessInfo.put("BusinessLocation",location);
                        businessInfo.put("BusinessContact",phone);
                        businessInfo.put("BusinessBio",bio);
                        businessInfo.put("BusinessPk",currentUserID);
                        businessInfo.put("TechnicianRole","3");

                        info.setBusinessBio(bio);
                        info.setBusinessContact(number);
                        info.setBusinessLocation(location);
                        info.setBusinessName(name);
                        info.setBusinessPk(currentUserID);
                        info.setImage(downloaduri.toString());
                        info.setRole("Technician");

                        businessRef.child(currentUserID).setValue(info);
                        documentReference.set(businessInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                loadingbar.dismiss();
                                Toast.makeText(ProfileActivity.this, "Data Uploaded", Toast.LENGTH_SHORT).show();

                                Intent goToAudit = new Intent(ProfileActivity.this,BusinessPageActivity.class);
                                startActivity(goToAudit);
                                finish();

                            }
                        });


                    }
                }
            });


               
        }
    }
}