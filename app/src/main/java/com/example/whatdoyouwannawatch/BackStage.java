package com.example.whatdoyouwannawatch;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BackStage {
    private static Theatre theatre;

    public BackStage() {
    }

    public BackStage(Theatre t) {
        this.theatre = t;
    }

    public static List<Time> calcLength() {
        List<Time> returnList = new ArrayList<Time>();
        List<User> users = theatre.getUsers();
        Time minTime = new Time(0, 0, 0);
        Time maxTime = new Time(23, 59, 59);

        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            if (u.getMinLength() > minTime && u.getMinLength() < maxTime) {
                minTime = u.getMinLength();
            }
            if (u.getMaxLength() < maxTime && u.getMaxLength() > minTime) {
                maxTime = u.getMaxLength();
            }
        }

        returnList.add(minTime);
        returnList.add(maxTime);

        return returnList;
    }

    public static List<String> calcGenre(){
        List <String> returnList = new ArrayList<String>();
        List<User> users = theatre.getUsers();
        HashMap <String, Integer> hashMap = new HashMap<>();
        for(int i= 0; i< users.size(); i++){
            User u= users.get(i);
            for (int j = 0; j< u.getGenres.size(); j++){
                String genre = u.getGenres.get(j);
                if(hashMap.containsKey(genre)){
                    hashMap.put(genre, hashMap.get(genre)+1);
                }
                else{
                    hashMap.put(genre, 1);
                }
            }
        }

        Object [] genres =  hashMap.keySet().toArray();
        String firstGenre = "";
        String secondGenre = "";
        int firstCount =0;
        int secondCount =0;
        for(int i =0; i< genres.length; i++){
            String s = (String) (genres[i]);
            if(hashMap.get(s) > firstCount){
                secondCount = firstCount;
                firstCount = hashMap.get(s);
                secondGenre = firstGenre;
                firstGenre = s;
            }
            else if(hashMap.get(s)> secondCount){
                secondCount =hashMap.get(s);
                secondGenre = s;
            }
        }
        if(!firstGenre.equals("")) {
            returnList.add(firstGenre);
        }
        if(!secondGenre.equals("")){
            returnList.add(secondGenre);
        }
        return returnList;
    }

    public static Result calcResult(List<Media> choices){
        List<User> users = theatre.getUsers();
        HashMap hashMap = new HashMap<Media, Integer>();
        for( int i=0; i< choices.size(); i++){
            hashMap.put(choices.get(i), 0);
        }
        int majority = (int) (users.size()/2.0) +1;
        int mostPopularCount = 0;
        int round =0;

    }


}
