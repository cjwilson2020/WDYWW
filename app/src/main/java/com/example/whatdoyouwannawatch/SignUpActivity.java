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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private InputValidator inputValidator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize objects
        mAuth = FirebaseAuth.getInstance();
        inputValidator = new InputValidator();
    }

    public void onClickSignUp(View v) {
        EditText editTextEmailAddress = findViewById(R.id.editTextEmailAddress);
        final String email = editTextEmailAddress.getText().toString();
        if (!inputValidator.emailIsValid(email)) {
            Toast.makeText(this, "You've entered an invalid email address. " +
                    "Please try again." , Toast.LENGTH_SHORT).show();
            return;
        }
        EditText editTextPassword = findViewById(R.id.editTextPassword);
        final String password = editTextPassword.getText().toString();
        if (!inputValidator.passwordIsValid(password)) {
            Toast.makeText(this, "You've entered an invalid password. " +
                    "Please try again." , Toast.LENGTH_SHORT).show();
            return;
        }
        EditText editTextUsername = findViewById(R.id.editText_username);
        final String username = editTextUsername.getText().toString();
        if (!inputValidator.usernameIsValid(username)) {
            Toast.makeText(this, "You've entered an invalid username. " +
                    "Please try again." , Toast.LENGTH_SHORT).show();
            return;
        }
        MainActivity.pullData( 'u', username, new DataCallback() {
            @Override
            public void onCallback(Object usr) {
                if(usr== null){
                    createUser(email, password, username);
                }else{
                    Toast.makeText(SignUpActivity.this, "Sign up failed. " +
                            "A user with this username already exists." , Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void createUser(String email, String password, final String username) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign up success, update username on new user object
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUsername(user, username);

                        } else {
                            // If sign up fails, display a message to the user.
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidUserException e) {
                                Toast.makeText(SignUpActivity.this, "Sign up failed. " +
                                        "This user does not exist." , Toast.LENGTH_SHORT).show();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                Toast.makeText(SignUpActivity.this, "Sign up failed. " +
                                        "You have entered invalid credentials.", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(SignUpActivity.this, "Sign up failed. " +
                                        e, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void updateUsername(final FirebaseUser user, String username) {
        UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .build();
        user.updateProfile(userProfileChangeRequest)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // all fields for new account filled, go to home

                            MainActivity.pullData( 'u', user.getDisplayName(), new DataCallback() {
                                @Override
                                public void onCallback(Object usr) {
                                    if(usr== null){
                                        User newUser = new User(user.getEmail(), user.getDisplayName(), user.getUid());
                                        MainActivity.pushData(newUser);
                                    }
                                }
                            });


                            Intent intent = new Intent(SignUpActivity.this, UserHomeActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }


}