package com.example.whatdoyouwannawatch;

import java.util.ArrayList;

public interface MediaCallback {

    //callback for getting media objects from search
    public void onCallback(ArrayList<Media> m);
}
