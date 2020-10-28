package com.example.whatdoyouwannawatch;

import android.text.TextUtils;
import android.util.Patterns;

public class InputValidator {

    public boolean passwordIsValid(String password) {
        if (password.length() > 0) {
            return true;
        }
        return false;
    }

    public boolean emailIsValid(String email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    public boolean usernameIsValid(String username) {
        return (!TextUtils.isEmpty(username));
    }
}


