package com.example.whatdoyouwannawatch;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
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

    //public static List<LocalTime> calcLength() {
    @SuppressLint("NewApi")
    public static void calcLength() {
        List<User> users = theatre.getUsers();
        int minTime = -1;
        int maxTime = 999;

        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            if (u.getMinLength() > (minTime) && u.getMinLength() < (maxTime)) {
                minTime = u.getMinLength();
            }
            if (u.getMaxLength() < (maxTime) && u.getMaxLength() > (minTime)) {
                maxTime = u.getMaxLength();
            }
        }

        //returnList.add(minTime);
        //returnList.add(maxTime);
        theatre.setMinTime(minTime);
        theatre.setMaxTime(maxTime);
    }



    /*
    private void vote() {
        Result result;
        Date date = new Date();
        //copy the list of options
        List<Media> vOptions = new ArrayList<>();
        for (Media m : theatre.getOptions()) {
            vOptions.add(m);
        }

        //first round
        for (int i = 0; i < 5; i++) {
            for (User u : theatre.getUsers()) {
                if (u.getRankings().get(i).equals(vOptions.get(0))) {
                    vOptions.get(0).addVoter(u);
                } else if (u.getRankings().get(i).equals(vOptions.get(1))) {
                    vOptions.get(1).addVoter(u);
                } else if (u.getRankings().get(i).equals(vOptions.get(2))) {
                    vOptions.get(2).addVoter(u);
                } else if (u.getRankings().get(i).equals(vOptions.get(3))) {
                    vOptions.get(3).addVoter(u);
                } else if (u.getRankings().get(i).equals(vOptions.get(4))) {
                    vOptions.get(4).addVoter(u);
                }
            }
        }

        //four more rounds to eliminate other options until only one left
        for (int j = 1; j < 5; j++) {
            //sort options by voter size
            Collections.sort(vOptions);
            //remove first option and assign it to removedOption
            List<User> removedOption = vOptions.remove(0).getCurrentVoters();
            //choose the jth option in the removed users and cast their vote to the next choice
            for (User u : removedOption) {
                for (Media m : vOptions) {
                    u.getRankings().get(j).equalTo(m);
                    m.addVoter(u);
                }
            }
        }
        //return the last one as recommendation
        result = new Result(vOptions.get(0), theatre.getUsers(), theatre.getOptions(), date);

//        return result;
    }*/

        public static void calcGenre () {
            List<User> users = theatre.getUsers();
            HashMap<String, Integer> hashMap = new HashMap<>();
            for (int i = 0; i < users.size(); i++) {
                User u = users.get(i);
                for (int j = 0; j < u.getGenres().size(); j++) {
                    String genre = u.getGenres().get(j);
                    if (hashMap.containsKey(genre)) {
                        hashMap.put(genre, hashMap.get(genre) + 1);
                    } else {
                        hashMap.put(genre, 1);
                    }
                }
            }

            Object[] genres = hashMap.keySet().toArray();
            String firstGenre = "";
            String secondGenre = "";
            int firstCount = 0;
            int secondCount = 0;
            for (int i = 0; i < genres.length; i++) {
                String s = (String) (genres[i]);
                if (hashMap.get(s) > firstCount) {
                    secondCount = firstCount;
                    firstCount = hashMap.get(s);
                    secondGenre = firstGenre;
                    firstGenre = s;
                } else if (hashMap.get(s) > secondCount) {
                    secondCount = hashMap.get(s);
                    secondGenre = s;
                }
            }
            if (!firstGenre.equals("")) {
                theatre.addGenre(firstGenre);
                Log.i("Genre1", firstGenre);
            }
            if (!secondGenre.equals("")) {
                theatre.addGenre(secondGenre);
                Log.i("Genre2", secondGenre);
            }
            //return returnList;
        }

        public static void calcResult (List < Media > choices) {
            List<User> users = theatre.getUsers();
            int majority = (int) (users.size() / 2.0) + 1;
            List<Media> copy = new ArrayList(choices);

            for (int i = 0; i < users.size(); i++) {
                for (int j = 0; j < choices.size(); j++) {
                    Media firstChoice = users.get(i).getRankings().get(0);
                    if (firstChoice.equals(choices.get(j))) {
                        choices.get(j).addVoter(users.get(i));
                    }
                }
            }

            do {
                int maxVoters = 0;
                int maxIndex = -1;
                int minVoters = 999999;
                int minIndex = -1;
                for (int k = 0; k < choices.size(); k++) {
                    if (choices.get(k).getNumVoters() > maxVoters) {
                        maxVoters = choices.get(k).getNumVoters();
                        maxIndex = k;
                    }
                    if (choices.get(k).getNumVoters() < minVoters) {
                        minVoters = choices.get(k).getNumVoters();
                        minIndex = k;
                    }
                }
                if (maxVoters >= majority) {
                    Date date = new Date();
                    Result result = new Result(choices.get(maxIndex), users, copy, date);
                    theatre.setResult(result);
                    return;
                } else {
                    Media leastPopular = choices.get(minIndex);
                    List<User> voters = leastPopular.getCurrentVoters();
                    choices.remove(minIndex);
                    for (int j = 0; j < voters.size(); j++) {
                        voters.get(j).removeRanking(leastPopular);
                        Media newFirstChoice = voters.get(j).getRankings().get(0);
                        for (int k = 0; k < choices.size(); k++) {
                            if (newFirstChoice.equals(choices.get(k))) {
                                choices.get(k).addVoter(voters.get(j));
                            }
                        }
                    }
                }
            } while (choices.size() > 1);


            Date date = new Date();
            Result r = new Result(choices.get(0), users, copy, date);
            theatre.setResult(r);
            return;

        }


    }
