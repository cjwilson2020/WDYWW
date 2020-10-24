package com.example.whatdoyouwannawatch;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
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

//        for (int i = 0; i < users.size(); i++) {
//            User u = users.get(i);
//            if (u.getMinLength() > minTime && u.getMinLength() < maxTime) {
//                minTime = u.getMinLength();
//            }
//            if (u.getMaxLength() < maxTime && u.getMaxLength() > minTime) {
//                maxTime = u.getMaxLength();
//            }
//        }

        //Edited by Yuan, use compareTo() instead of '>', '<'
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            if (u.getMinLength().compareTo(minTime) > 0 && u.getMinLength().compareTo(maxTime) < 0) {
                minTime = u.getMinLength();
            }
            if (u.getMaxLength().compareTo(maxTime) > 0 && u.getMaxLength().compareTo(minTime) < 0) {
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
            for (int j = 0; j< u.getGenres().size(); j++){
                String genre = u.getGenres().get(j);
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
        int majority = (int) (users.size()/2.0) +1;
        List<Media> copy = new ArrayList(choices);
        
        for(int i =0; i< users.size(); i++){
            for(int j = 0; j< choices.size(); j++){
                String firstChoice = users.get(i).getRankings().get(0);
                if(firstChoice.equals(choices.get(j).getId())){
                    choices.get(j).addVoter(users.get(i));
                }
            }
        }

        do{
            int maxVoters = 0;
            int maxIndex = -1;
            int minVoters = 999999;
            int minIndex = -1;
            for(int k =0; k< choices.size(); k++){
                if(choices.get(k).getNumVoters()> maxVoters){
                    maxVoters = choices.get(k).getNumVoters();
                    maxIndex = k;
                }
                if(choices.get(k).getNumVoters() < minVoters){
                    minVoters = choices.get(k).getNumVoters();
                    minIndex = k;
                }
            }
            if(maxVoters>= majority) {
                Date date = new Date();
                Result result = new Result(choices.get(maxIndex), users, copy, date);
                return result;
            }else{
                Media leastPopular = choices.get(minIndex);
                List <User> voters = leastPopular.getCurrentVoters();
                choices.remove(minIndex);
                for(int j =0; j< voters.size(); j++){
                    voters.get(j).removeRanking(leastPopular.getId());
                    String newFirstChoice = voters.get(j).getRankings().get(0);
                    for(int k =0; k< choices.size(); k++){
                        if(newFirstChoice.equals(choices.get(k).getId())){
                            choices.get(k).addVoter(voters.get(j));
                        }
                    }
                }
            }
        }while(choices.size()>1);


        Date date = new Date();
        Result r = new Result(choices.get(0), users, copy, date);
        return r;

        
    }


}
