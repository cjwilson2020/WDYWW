package com.example.whatdoyouwannawatch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private InputValidator inputValidator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d("login", "LoginActivity");

        // Initialize objects
        mAuth = FirebaseAuth.getInstance();
        inputValidator = new InputValidator();

        // If user already logged in redirect to homepage
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(LoginActivity.this, UserHomeActivity.class);
            startActivity(intent);
        }
    }


    public void onClickLogIn(View view) {
        EditText editTextEmail = findViewById(R.id.editTextEmailAddressLogin);
        String email = editTextEmail.getText().toString();
        if (!inputValidator.emailIsValid(email)) {
            Toast.makeText(this, "You've entered an invalid email address. " +
                    "Please try again." , Toast.LENGTH_SHORT).show();
            return;
        }
        EditText editTextPassword = findViewById(R.id.editTextPasswordLogin);
        String password = editTextPassword.getText().toString();
        if (!inputValidator.passwordIsValid(password)) {
            Toast.makeText(this, "You've entered an invalid password. " +
                    "Please try again." , Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "Logging you in. Hang on tight...", Toast.LENGTH_SHORT).show();
        logInUser(email, password);
    }

    private void logInUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Log in success, update UI with the signed-in user's information
                            Intent intent = new Intent(LoginActivity.this, UserHomeActivity.class);
                            startActivity(intent);
                        } else {
                            // If log in fails, display a message to the user.
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidUserException e) {
                                    Toast.makeText(LoginActivity.this, "Log in failed. " +
                                            "This user does not exist." , Toast.LENGTH_SHORT).show();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                    Toast.makeText(LoginActivity.this, "Log in failed. " +
                                            "You have entered invalid credentials.", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(LoginActivity.this, "Log in failed. User does not exist. " +
                                        "Please try again or sign up.", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });
    }



}