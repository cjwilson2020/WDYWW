package com.example.whatdoyouwannawatch;

import org.json.JSONException;

public interface ApiCallback {
    //In this callback interface, the onCallback function will
    //be called back and loaded with a parameter that I can set
    void onCallback(String res) throws JSONException;
}