package com.pythonanywhere.frutsh;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity {

    EditText emailEditText, passwordEditText;
    private FirebaseAuth mAuth;

    EditText enterEmailInDialog;
    Button forgetPasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();
        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        enterEmailInDialog = new EditText(this);
        forgetPasswordButton = findViewById(R.id.forgetPasswordButton);
    }


    public void signInClicked(View view) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (passwordEditText.length() > 6 && emailEditText.getText().toString().trim().matches(emailPattern)) {
            login();
        } else if (passwordEditText.length() > 6 && !emailEditText.getText().toString().trim().matches(emailPattern)) {
            Toast.makeText(this, "Enter a valid email", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Incorrect email or password", Toast.LENGTH_SHORT).show();
        }
    }

    public void newUser(View view) {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    public void forgetPasswordClicked(View view) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        new AlertDialog.Builder(LoginActivity.this).setTitle("Enter your registered email")
                .setView(enterEmailInDialog).setPositiveButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!enterEmailInDialog.getText().toString().isEmpty() && enterEmailInDialog.getText().toString().trim().matches(emailPattern)) {
                    mAuth.sendPasswordResetEmail(enterEmailInDialog.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, "Password reset link sent to your registered email", Toast.LENGTH_SHORT).show();
                                    } else {
                                        try {
                                            throw task.getException();
                                        } catch (FirebaseAuthInvalidUserException userException) {
                                            Toast.makeText(LoginActivity.this, "Email not registered !", Toast.LENGTH_SHORT).show();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    if (enterEmailInDialog.getParent() != null) {
                                        enterEmailInDialog.getText().clear();
                                        ((ViewGroup) enterEmailInDialog.getParent()).removeView(enterEmailInDialog); // <- fix for the crash when button clicked once again
                                    }
                                }

                            });
                } else {
                    Toast.makeText(LoginActivity.this, "Enter a valid email", Toast.LENGTH_SHORT).show();
                    if (enterEmailInDialog.getParent() != null) {
                        enterEmailInDialog.getText().clear();
                        ((ViewGroup) enterEmailInDialog.getParent()).removeView(enterEmailInDialog); // <- fix for the crash when button clicked once again
                    }
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (enterEmailInDialog.getParent() != null) {
                    enterEmailInDialog.getText().clear();
                    ((ViewGroup) enterEmailInDialog.getParent()).removeView(enterEmailInDialog); // <- fix
                }
                dialogInterface.dismiss();
            }
        }).show();

    }

    public void login() {
        mAuth.signInWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException userException) {
                        Toast.makeText(LoginActivity.this, "No such user found !", Toast.LENGTH_SHORT).show();
                    }
                    // if user enters wrong password.
                    catch (FirebaseAuthInvalidCredentialsException incorrectPassword) {
                        Toast.makeText(LoginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}