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

        import com.google.android.gms.common.api.Status;
        import com.google.android.gms.tasks.Continuation;
        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.android.gms.tasks.Task;
        import com.google.android.libraries.places.api.Places;
        import com.google.android.libraries.places.api.model.Place;
        import com.google.android.libraries.places.widget.Autocomplete;
        import com.google.android.libraries.places.widget.AutocompleteActivity;
        import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
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

        import java.util.Arrays;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;

        import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerProfileActivity extends AppCompatActivity {
    private EditText cName,cIDNumber,cLocation,cPhoneNumber;
    private Button registerCustomer;
    private CircleImageView profileImage;
    final static int Gallery_pick = 1;
    private FirebaseAuth mAuth;
    private DatabaseReference customerRef;
    private StorageReference customerImageRef;
    private ProgressDialog loadingbar;
    private FirebaseFirestore fStore;
    private DocumentReference documentReference;

    CustomerInfo info;

    Uri imageUri;
    UploadTask uploadTask;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    String currentUserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);

        info = new CustomerInfo();

        fStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        customerRef = FirebaseDatabase.getInstance().getReference("All_Customers");
        customerImageRef = FirebaseStorage.getInstance().getReference().child("cPrifileImage");
        documentReference = fStore.collection("Users").document(currentUserID);

        Places.initialize(getApplicationContext(),"AIzaSyCXNBhw7sGEzpFGU552v90VlEjn8twls6A");


        cName = (EditText) findViewById(R.id.customerName);
        cIDNumber = (EditText) findViewById(R.id.customerIDNumber);
        cLocation = (EditText) findViewById(R.id.customerLocation);

        cLocation.setFocusable(false);
        cLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS
                ,Place.Field.LAT_LNG,Place.Field.NAME);

                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,
                        fieldList).build(CustomerProfileActivity.this);

                startActivityForResult(intent,100);

            }
        });
        cPhoneNumber = (EditText) findViewById(R.id.customerContact);
        registerCustomer = (Button) findViewById(R.id.btnCustomerRegistration);
        profileImage = (CircleImageView) findViewById(R.id.customerImage);
        loadingbar = new ProgressDialog(this);

        registerCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCustomerProfile();
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

        if (requestCode == 100 && resultCode == RESULT_OK){
            Place place = Autocomplete.getPlaceFromIntent(data);

            cLocation.setText(place.getAddress());



        }
        else if (resultCode == AutocompleteActivity.RESULT_ERROR){
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getApplicationContext(), status.getStatusMessage(), Toast.LENGTH_LONG).show();
        }

       else if (requestCode == Gallery_pick || resultCode == RESULT_OK || data !=null || data.getData() != null){
            imageUri = data.getData();

            Picasso.get().load(imageUri).into(profileImage);
        }
    }
    private String getFileExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType((contentResolver.getType(uri)));
    }

    private void saveCustomerProfile() {
        final String name = cName.getText().toString();
        final String number = cIDNumber.getText().toString();
        final String location = cLocation.getText().toString();
        final String phone = cPhoneNumber.getText().toString();


        if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "Customer Name Field Cannot be Empty!!", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(number)){
            Toast.makeText(this, "Customer Number Field Cannot be Empty!!", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(location)){
            Toast.makeText(this, "Customer Location Field Cannot be Empty!!", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Customer Phone Number Field Cannot be Empty!!", Toast.LENGTH_SHORT).show();
        }

        else{
            loadingbar.setTitle("Uploading Customer Profile Info");
            loadingbar.setMessage("This might take a few seconds...");
            loadingbar.show();
            loadingbar.setCanceledOnTouchOutside(true);

            final StorageReference reference = customerImageRef.child(System.currentTimeMillis()+ "."+getFileExt(imageUri));
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
                        Map<String,String> customerInfo = new HashMap<>();
                        customerInfo.put("Image",downloaduri.toString());
                        customerInfo.put("CustomerName",name);
                        customerInfo.put("CustomerIDNumber",number);
                        customerInfo.put("CustomerLocation",location);
                        customerInfo.put("CustomerContact",phone);
                        customerInfo.put("CustomerPk",currentUserID);
                        customerInfo.put("CustomerRole","1");


                        info.setCustomerContact(phone);
                        info.setCustomerIDNumber(number);
                        info.setCustomerLocation(location);
                        info.setCustomerName(name);
                        info.setCustomerPk(currentUserID);
                        info.setImage(downloaduri.toString());
                        info.setRole("Customer");

                        customerRef.child(currentUserID).setValue(info);
                        documentReference.set(customerInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                loadingbar.dismiss();
                                Toast.makeText(CustomerProfileActivity.this, "Data Uploaded Successfully", Toast.LENGTH_SHORT).show();

                                Intent goToAudit = new Intent(CustomerProfileActivity.this,MainActivity.class);
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