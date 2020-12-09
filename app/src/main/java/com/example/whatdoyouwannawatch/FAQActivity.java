package com.example.whatdoyouwannawatch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class FAQActivity extends AppCompatActivity {

    private ArrayList<String> listItems;
    private ArrayAdapter<String> arrayAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_f_a_q);

        listItems =  new ArrayList<String>();

        listItems.add("How do I create a new Theatre?\n\n" +
                "To create a new Theatre, click the 'Watch Now' button, then the 'Create Theatre' button. " +
                "You will now be in your newly created Theatre! Your username will appear at the top of the screen, and " +
                "will act as the join code for your friends.\n");

        listItems.add("How do I join my friend's Theatre?\n\n" +
                "From the home screen, click the 'Watch Now' button, and then click the 'Join Theatre' button. From " +
                "here, you will be prompted to enter a join code, which you will need to get from the host of the Theatre. Once " +
                "you enter the code, click the 'Join Theatre' button, and you will be added to your friend's Theatre!\n");

        listItems.add("How do we begin once all of our friends have joined the Theatre?\n\n" +
                "Once everyone in your group has joined the Theatre, click the 'I'm all set' button, and the " +
                "ranking process will begin!\n");

        listItems.add("How do I log out of my account?\n\n" +
                "To log out of your account, navigate to the home screen and click the 'Log Out' button at the bottom of the screen.\n");

        listItems.add("Can I save my preferred genres so that I don't have to add them each time?\n\n" +
                "Yes! To save your preferred genres, click on the 'Settings' button from the home screen, then click " +
                "the 'Watch Preferences' button. Here you will be able to choose which genres are your favorite! When you have finished " +
                "selecting your favorite genres, click the 'Save' button and your preferences are saved!\n");

        listItems.add("How do I add a profile image?\n\n" +
                "To add a profile image, click on the 'Profile' button from the home screen, and you will be navigated to your profile " +
                "page. Click on the profile image and you will be prompted to upload an image from your device.\n");


        arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, listItems);
        listView = findViewById(R.id.FrequentlyAskedQuestions);
        listView.setAdapter(arrayAdapter);
    }
}