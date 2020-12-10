package com.example.whatdoyouwannawatch;

public interface AsyncResponse {
    void processFinish(String result) throws InterruptedException;
}
