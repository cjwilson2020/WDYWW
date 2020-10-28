package com.example.whatdoyouwannawatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private InputValidator inputValidator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize
        mAuth = FirebaseAuth.getInstance();
        inputValidator = new InputValidator();
    }

    public void onClickSignUp(View v) {
        EditText editTextEmailAddress = findViewById(R.id.editTextEmailAddress);
        String email = editTextEmailAddress.getText().toString();
        if (!inputValidator.emailIsValid(email)) {
            Toast.makeText(this, "You've entered an invalid email address. " +
                    "Please try again." , Toast.LENGTH_SHORT).show();
            return;
        }
        EditText editTextPassword = findViewById(R.id.editTextPassword);
        String password = editTextPassword.getText().toString();
        if (!inputValidator.passwordIsValid(password)) {
            Toast.makeText(this, "You've entered an invalid password. " +
                    "Please try again." , Toast.LENGTH_SHORT).show();
            return;
        }
        createUser(email, password);
    }

    private void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign up success, update UI with the signed-in user's information
                            Intent intent = new Intent(SignUpActivity.this, UserHomeActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign up fails, display a message to the user.
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidUserException e) {
                                Toast.makeText(SignUpActivity.this, "Log up failed. " +
                                        "This user does not exist." , Toast.LENGTH_SHORT).show();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                Toast.makeText(SignUpActivity.this, "Log in failed. " +
                                        "You have entered invalid credentials.", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(SignUpActivity.this, "Log in failed. " +
                                        e, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }


}