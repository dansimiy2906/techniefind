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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    private Button LoginButton,RegisterButton;
    private EditText userEmail,userPassword;
    private TextView forgotPasswordlink;
    private CheckBox checkBox;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    private FirebaseFirestore fStore;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        RegisterButton = (Button) findViewById(R.id.login_to_signup);
        forgotPasswordlink = (TextView) findViewById(R.id.resetPassword);
        LoginButton = (Button) findViewById(R.id.button_login);
        userEmail = (EditText) findViewById(R.id.login_email_et);
        userPassword = (EditText) findViewById(R.id.login_password_et);
        checkBox = (CheckBox) findViewById(R.id.login_checkbox);
        loadingBar = new ProgressDialog(this);


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b){
                    userPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    //    confirm_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else {

                    userPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    //  confirm_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllowingUserToLogin();
            }
        });

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserToRegisterActivity();
            }
        });
    }

    private void AllowingUserToLogin() {
        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please Input your email Address", Toast.LENGTH_SHORT).show();

        }
        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please Key In Your Passowrd to Login", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingBar.setTitle("Logging In User");
            loadingBar.setMessage("Please Wait while we try to log you in...");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();

            mAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(LoginActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                    CheckUserAccessLevel(authResult.getUser().getUid());
                    loadingBar.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LoginActivity.this, "Error Occured While Logging In!!", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                }
            });
        }
    }

    private void CheckUserAccessLevel(String uid) {
        DocumentReference df = fStore.collection("Users").document(uid);
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.getString("AdminRole") !=null){
                    Intent toAdmin = new Intent(LoginActivity.this,AdminActivity.class);
                    startActivity(toAdmin);
                    finish();
                }
                if (documentSnapshot.getString("CustomerRole") != null){
                    Intent toMain = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(toMain);
                    finish();
                }
                else if (documentSnapshot.getString("TechnicianRole") != null){
                    Intent toBusinessPage = new Intent(LoginActivity.this,BusinessPageActivity.class);
                    startActivity(toBusinessPage);
                    finish();
                }
                else{
                    Toast.makeText(LoginActivity.this, "UserRole Unavailable", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private void sendUserToRegisterActivity() {
        Intent goToRegister = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(goToRegister);
        finish();
    }
}
