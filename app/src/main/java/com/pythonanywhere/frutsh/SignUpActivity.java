package com.pythonanywhere.frutsh;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseUser user;
    EditText emailEditText, passwordEditText, usernameEditText;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
        usernameEditText = findViewById(R.id.editTextUsername);
    }

    public void signUp(View view) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (passwordEditText.length() > 6 && usernameEditText.length() > 0 && emailEditText.getText().toString().trim().matches(emailPattern)) {
            createUser();
        } else if (passwordEditText.length() <= 6 && usernameEditText.length() > 0 && emailEditText.getText().toString().trim().matches(emailPattern)) {
            Toast.makeText(this, "Password must be more than 6 characters", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Enter valid username or email", Toast.LENGTH_SHORT).show();
        }
    }

    public void createUser() {
        mAuth.createUserWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
                    try {
                        mDatabase.child("Frutsh_User").child(task.getResult().getUser().getUid()).child("Email").setValue(emailEditText.getText().toString());
                        mDatabase.child("Frutsh_User").child(task.getResult().getUser().getUid()).child("Username").setValue(usernameEditText.getText().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    emailVerification();
                    verificationDialogBox();

                } else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidCredentialsException incorrectPassword) {
                        Toast.makeText(SignUpActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    } catch (FirebaseAuthUserCollisionException existEmail) {
                        Toast.makeText(SignUpActivity.this, "Email already exist", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void emailVerification() {
        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.i("Verification", "Email sent");
                } else {
                    Log.i("Verification", "Email not sent");
                }
            }
        });
    }

    public void signIn() {
        Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    public void signOut() {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void verificationDialogBox() {
        new AlertDialog.Builder(SignUpActivity.this).setTitle("Verify Your Email").setMessage("A verification link has been sent to your email address. Do you want to verify?").setPositiveButton("Verify now", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new AlertDialog.Builder(SignUpActivity.this).setTitle("Proceed to GMail").setMessage("Please click on the link sent to your email address and login again to complete the verification.").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            Intent intent = getPackageManager().getLaunchIntentForPackage("com.google.android.gm");
                            if (intent != null) {
                                startActivity(intent);
                                finishAffinity();
                            }
                        } catch (Exception e) {
                            Toast.makeText(SignUpActivity.this, "There was a problem in opening the GMail app", Toast.LENGTH_SHORT).show();
                        }
                        dialogInterface.dismiss();
                    }
                }).show();
                dialogInterface.dismiss();
            }
        }).setNegativeButton("Verify later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(SignUpActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                signIn();
            }
        }).show();


    }
}