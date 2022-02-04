package com.example.techniefind;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private EditText userEmail,userPassword,userConfirmPassword;
    private Button registerbtn,goTologinbtn;
    private CheckBox hidepass;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private ProgressDialog loadingBar;
    private CheckBox isCustomer,isTechnician;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userEmail = (EditText) findViewById(R.id.register_email_et);
        userPassword = (EditText) findViewById(R.id.register_password_et);
        userConfirmPassword = (EditText) findViewById(R.id.register_confirmpassword_et);
        registerbtn = (Button) findViewById(R.id.button_register);
        goTologinbtn = (Button) findViewById(R.id.signup_to_login);
        hidepass = (CheckBox) findViewById(R.id.register_checkbox);

        isCustomer = (CheckBox) findViewById(R.id.userCustomer);
        isTechnician = (CheckBox) findViewById(R.id.userTechnician);

        loadingBar = new ProgressDialog(this);

       //hide pass
        hidepass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    userPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    userConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {

                    userPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    userConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }

        });

        //check a box at a time
        isCustomer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()){
                    isTechnician.setChecked(false);
                }
            }
        });
        isTechnician.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()){
                    isCustomer.setChecked(false);
                }
            }
        });

        goTologinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(login);
                finish();

            }
        });

        //Setting onclick Listener on register button.
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNewAccount();
            }
        });

        //goTologinbtn.setOnClickListener(new View.OnClickListener() {
          //  @Override
          //  public void onClick(View view) {
             //   Intent login = new Intent(RegisterActivity.this,LoginActivity.class);
              //  startActivity(login);
                //finish();
           // }
        //});

    }

    //@Override
   // protected void onStart() {
       // super.onStart();
       // FirebaseUser currentUser = mAuth.getCurrentUser();
       // if (currentUser != null){
          //  sendUserToMainActivity();
       // }
   // }

    private void sendUserToCustomerProfileActivity() {
        Intent goToProfile = new Intent(RegisterActivity.this,CustomerProfileActivity.class);
        startActivity(goToProfile);
        finish();
    }


    private void CreateNewAccount() {
        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();
        String confirmPassword = userConfirmPassword.getText().toString();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Input Your Email Address!!", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Input Password!!", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(confirmPassword)){
            Toast.makeText(this, "Please Confirm Password!!", Toast.LENGTH_SHORT).show();
        }
        else if (!password.equals(confirmPassword)){
            Toast.makeText(this, "Passwords do not Match", Toast.LENGTH_SHORT).show();
        }
        else if (!(isCustomer.isChecked()||isTechnician.isChecked())){
            Toast.makeText(this, "Please Select Your Account Type!!", Toast.LENGTH_SHORT).show();
            return;
        }

        else{
            loadingBar.setTitle("Creating an Account");
            loadingBar.setMessage("This might take a few seconds...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(RegisterActivity.this, "Registration Sucessful", Toast.LENGTH_SHORT).show();

                                DocumentReference db = fStore.collection("Users").document(user.getUid());
                                Map<String,Object> userInfo = new HashMap<>();
                                userInfo.put("UserEmail",userEmail.getText().toString());

                                if (isCustomer.isChecked()){
                                    userInfo.put("Customer Checked","1");
                                    sendUserToCustomerProfileActivity();
                                }
                                if (isTechnician.isChecked()){
                                    userInfo.put("Technician Checked","0");
                                    sendUserToProfileActivity();
                                }
                                db.set(userInfo);


                                loadingBar.dismiss();

                            }
                            else{
                                String message = task.getException().getMessage();
                                Toast.makeText(RegisterActivity.this, "Error Occured:"+message, Toast.LENGTH_LONG).show();
                                loadingBar.dismiss();
                            }
                        }
                    });



        }



    }

    private void sendUserToProfileActivity() {
        Intent goToProfile = new Intent(RegisterActivity.this,ProfileActivity.class);
        startActivity(goToProfile);
        finish();
    }
}
