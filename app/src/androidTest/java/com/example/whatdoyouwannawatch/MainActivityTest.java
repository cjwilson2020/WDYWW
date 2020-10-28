package com.example.whatdoyouwannawatch;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class MainActivityTest {
    public static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static DatabaseReference myRef = database.getReference("users");

    @Test
    public void pushData() {
        User user = new User("ted123@gmail.com","ted123", "lhsdfiwer9uiruiry78rufh");
        HashMap<String, Object> map = new HashMap<>();
        String folder = "/users/" + user.getUID();
        map.put(folder, user);
        myRef.updateChildren(map)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("pushData", "Error Adding User", e);
                    }
                });
    }

    @Test
    public void pullData() {

    }
}