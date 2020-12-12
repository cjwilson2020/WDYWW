# WDYWW User Guide 

## General Instructions

After pulling the tagged branch from github, you may need to sync your project with the gradle files using File> Sync Project with Gradle Files

If you don't already have an emulator, you will need to make one. Instructions for doing so can be found here (please use API level >=25 and a device with a 16:9 aspect ratio): https://developer.android.com/studio/run/managing-avds

## Run the App

After successful gradle sync and creation of emulator, you can run the app by clicking the Run button (it looks like a play button) found to the right of the emulator information displayed in the upper-middle of the screen

Users will be required to sign up for an account. Please provide your email and select a password + username

After sign-up, a corresponding User object will be added to firebase and the user will be directed to the User Home where they can select what they'd like to do

For iteration 1 and 2, we have implemented the following functionality:

1) Watch Now

  a) Create Theatre
  
    i) Upon clicking "Create Theatre", a Theatre with the user's username will be created in firebase, the user's object will be added to the theatre's list of users
    
    ii) The user is redirected to the Host Landing Page. From there, they can end the Theatre session or access the User Landing Page and view all users in the theatre
  
  b) Join Theatre
  
    i) Upon clicking "Join Theatre", the user will be prompted for a theatre code. If the code provided is valid, the user's object will be added to that theatre's list of users
    
    ii) The user is redirected to the User Landing Page, where they can view all users in the theatre or leave the session
    
  c) Shared functionality between Create/Join Theatre
  
    i) After reconvening at the User Landing Page, both hosts and users act in the same manner. From now on, they will all be referred to as users
    
    ii) Clicking "I'm All Set" brings users to Choose Genres Activity, where they select the genres that interest them
    
    iii) After selecting genres, users are redirected to select the streaming services they have available
    
    iiii) After streaming services, users select their desired watch time (min and max)
    
    v) After all data has been collected from all users in the theatre (genres, services, lengths), the BackStage object calls the theatre's helper methods and retrieves a list of 5 media that meet these criteria
    
    vi) All users rank these media from most desireable to least desireable, by long clicking and dragging to rearrange the order. After all users have responded BackStage calls the calcResult() method to determine the final Result
    
    vii) The final Result is displayed to all users, at which point they exit the app to begin watching
  
2) Log Out

  a) After signing up or logging in, a user may Log Out and be redirected to the Sign Up Activity where they can log in or create another account
  
3) Log in

  a) After creating an account and signing out, a user may log in using their email and password by clicking "Log in"
  b) A User is now able to "Log in" as a Guest. Guest Users are only able to join Theatres, and are not able to save any preferences or history.
  
4) Profile

  a) Users will be able to see their preferred genres in the User Profile page after either entering a Theatre for the first time, or editing their watch preferences in the settings.
  
5) Watch History

  a) Users will be able to see all media they have previously watched through What Do You Wanna Watch.

6) Friends List 

  a) Users will be able see all of their friends and add new friends by username. 
  
7) Settings

  a) Watch Preferences 
  
      i) User is able to edit which genres they prefer to watch, which they are able to use while in the theatre rather than selecting genres each time.
      
  b) FAQ 
  
      i) User is able to look through a set of questions that are frequently asked. For example, they could find the answer for how to edit their watch preferences. 
      
  c) Contact Us
  
      i) Each group member's name, picture, and GitHub link is listed. 
      
  d) Delete Account 
  
      i) User is able to delete their account from the application and Firebase.


## Run Tests

Tests are coded and performanced in the yuanTest branch

Unit tests can be found in the java folder under app> src> test

UI tests are not complete, but can be found in the java folder under app> src> androidTest 

Run files by right-clicking on them and selecting "Run"

Test results should be displayed in the "Run" tab of the console
