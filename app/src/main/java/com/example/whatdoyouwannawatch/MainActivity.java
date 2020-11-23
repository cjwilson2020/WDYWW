package com.example.whatdoyouwannawatch;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.*;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class
MainActivity extends AppCompatActivity {
    // Write info to the Realtime database
    private static final String TAG = "MainActivity";
    public static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static DatabaseReference myRef = database.getReference();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        deleteCurrentGuest();
    }

    // For cleaning up leftover guest sessions
    @Override
    public void onResume() {
        super.onResume();
        deleteCurrentGuest();
    }

    public void deleteCurrentGuest() {
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // if leftover guest session, delete guest and do not redirect to homepage
            MainActivity.pullData('u', currentUser.getDisplayName(), new DataCallback() {
                @Override
                public void onCallback(Object obj) {
                    if (obj != null) {
                        User u = (User) obj;
                        if (u.isGuest()) {
                            // delete user
                            MainActivity.pullData('u', currentUser.getDisplayName(), new DataCallback() {
                                @Override
                                public void onCallback(Object obj) {
                                    if (obj != null) {
                                        User u = (User) obj;
                                        MainActivity.deleteData(u);
                                    }
                                }
                            });
                            //Delete guest in FB Auth
                            FirebaseAuth.getInstance().getCurrentUser().delete();
                        }
                    }
                }
            });
        }
    }

    static void removeUserFromTheatre(String hostID, final String username) {
        myRef = database.getReference();
        pullData('t', hostID, new DataCallback() {
            @Override
            public void onCallback(Object obj) {
                if (obj != null) {
                    Theatre t = (Theatre) obj;
                    t.removeUser(username);
                    pushData(t);
                }
            }
        });
    }

    static void removeTheatre(String hostID) {
        myRef = database.getReference();
        DatabaseReference tRef = myRef.child("theatres").child(hostID);
        tRef.removeValue();
    }

    static void pushData(Object obj) {
        // A HashMap is used to upload information to firebase, the String is the location in
        // firebase and the Object is the Object to be put in firebase
        HashMap<String, Object> map = new HashMap<>();
        myRef = database.getReference();
        if (obj.getClass().getName().equals("com.example.whatdoyouwannawatch.User")) {
            User tmp = (User) obj;
            String u = "users";

            String folder = u + "/" + (tmp).getUsername();
            map.put(folder.toLowerCase(), obj);
        } else if (obj.getClass().getName().equals("com.example.whatdoyouwannawatch.Theatre")) {
            Theatre tmp = (Theatre) obj;
            String t = "theatres";

            String folder = t + "/" + (tmp).getHostID();
            map.put(folder.toLowerCase(), obj);
        }
        myRef.updateChildren(map)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("pushData", "Error Adding User/Theatre", e);
                    }
                })
                .addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Log.d("pushData", "Successfully added user to firebase");
                    }
                });
    }
    public static Object data = null;
    static Object pullData(char type, String id, final DataCallback dcb) {
        String t = "theatres";
        String u = "users";
        id = id.toLowerCase().trim();

        if (type == 'u') {
            myRef = database.getReference().child(u).child(id);
            Log.d("pull", myRef.toString());

            ValueEventListener userListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user == null) {
                        dcb.onCallback(null);
                    } else {
                        if (user.getUID() == null) {
                            dcb.onCallback(null);
                        } else {
                            MainActivity.data = user;
                            dcb.onCallback(user);
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting User failed, log a message
                    Log.i("PullData", "Failed to Load User from Firebase", databaseError.toException());
                }
            };

            myRef.addListenerForSingleValueEvent(userListener);
            myRef.removeEventListener(userListener);
        } else if (type == 't') {
            myRef = database.getReference().child(t).child(id);
            Log.d("pull", myRef.toString());

            ValueEventListener theatreListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Theatre theatre = dataSnapshot.getValue(Theatre.class);
                    if (theatre == null) {
                        dcb.onCallback(null);
                    } else {
                        if (theatre.getHostID() == null) {
                            dcb.onCallback(null);
                        } else {
                            MainActivity.data = theatre;
                            dcb.onCallback(theatre);
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting User failed, log a message
                    Log.i("PullData", "Failed to Load User from Firebase", databaseError.toException());
                }
            };

            myRef.addListenerForSingleValueEvent(theatreListener);
            myRef.removeEventListener(theatreListener);
        }
        if (data != null){
            return (Object)data;
        }
        return null;
    }

    static void deleteData(Object obj){
        // A HashMap is used to upload information to firebase, the String is the location in
        // firebase and the Object is the Object to be put in firebase
        HashMap<String, Object> map = new HashMap<>();
        myRef = database.getReference();
        if (obj.getClass().getName().equals("com.example.whatdoyouwannawatch.User")) {
            User tmp = (User) obj;
            String u = "users";

            String folder = u + "/" + (tmp).getUsername();
            map.put(folder.toLowerCase(), null);
        } else if (obj.getClass().getName().equals("com.example.whatdoyouwannawatch.Theatre")) {
            Theatre tmp = (Theatre) obj;
            String t = "theatres";

            String folder = t + "/" + (tmp).getHostID();
            map.put(folder.toLowerCase(), null);
        }
        myRef.updateChildren(map)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("delete", "Error deleting User/Theatre", e);
                    }
                })
                .addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Log.d("delete", "Successfully deleted from firebase");
                    }
                });
    }

    public void onClickLogIn(View v) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void onClickSignUp(View v) {
        Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    public void onClickContinueAsGuest(View v) {
        // create anonymous user
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            setGuestUsername(user, "Guest " + user.getUid());
                        }
                    }
                });
    }

    private void setGuestUsername(final FirebaseUser user, String username) {
        UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .build();
        user.updateProfile(userProfileChangeRequest)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            MainActivity.pullData( 'u', user.getDisplayName(), new DataCallback() {
                                @Override
                                public void onCallback(Object usr) {
                                    if(usr== null){
                                        User newUser = new User(user.getDisplayName());
                                        MainActivity.pushData(newUser);
                                    }
                                }
                            });
                            Intent intent = new Intent(MainActivity.this, JoinTheatre.class);
                            startActivity(intent);
                        }
                    }
                });
    }

    // This is a method to asynchronously call our API, Entertainment Data Hub on RapidAPI,
    // https://rapidapi.com/IVALLC/api/entertainment-data-hub and wait for a response
    // To implement this method, I need to use a callback function
    public static void apiCallSearch(String progTypes, String genres, String providers, final ApiCallback acb) throws IOException {

        OkHttpClient client = new OkHttpClient(); //A client for networking with the Api online
        //Log.d("search", "Title: " + title );
        genres = genres.replaceAll("\\s+", "");
        providers = providers.replaceAll("\\s+", "");


        Log.d("search", "genres: " + genres);
        Log.d("search", "providers: " + providers);
        Log.d("search", "progTypes: " + progTypes);
        /*
            .url("https://ivaee-internet-video-archive-entertainment-v1.p.rapidapi.com/entertainment/
            search/?
            Genres=Crime%2CDrama&
            SortBy=Relevance&
            Includes=Images%2CGenres%2CDescriptions&
            ProgramTypes=Movie%2CShow&
            Providers=AmazonPrimeVideo%2c
            ")
        */

        String address = "https://ivaee-internet-video-archive-entertainment-v1.p.rapidapi.com/entertainment/search/?";

        String gl[] = genres.split(",");
        Log.d("search","gl[0]: " + gl[0]);
        if (gl == null || gl[0].equals("")) {
            gl = new String[]{};
        }
        if (gl.length > 0) {
            address = address.concat("Genres=");
            for (String g : gl) {
                if (gl[gl.length - 1].equals(g)) {
                    address = address.concat(g + "&");
                } else {
                    address = address.concat(g + "%2C");
                }
            }
        }


        address = address.concat("SortBy=Relevance&");
        address = address.concat("Includes=Images%2CGenres%2CDescriptions&");

        String pt[] = progTypes.split(",");

        if (pt == null || pt[0].equals("")) {
            pt = new String[]{};
        }
        if (pt.length > 0) {
            address = address.concat("ProgramTypes=");
            for (String p : pt) {
                if (pt[pt.length - 1].equals(p)) {
                    address = address.concat(p + "&");
                } else {
                    address = address.concat(p + "%2C");
                }
            }
        }

        String prv[] = providers.split(",");
        if (prv == null || prv[0].equals("")) {
            prv = new String[]{};
        }
        if (prv.length > 0) {
            address = address.concat("Providers=%20");
            for (String p : prv) {
                if (prv[prv.length - 1].equals(p)) {
                    address = address.concat(p);
                } else {
                    address = address.concat(p + "%2C");
                }
            }
        }

        Request request = new Request.Builder()
                .url(address)
                .get()
                .addHeader("Content-Type", "application/json")
                .addHeader("X-RapidAPI-Key", "0781c4e67fmsh14845fdab783a92p1a799ejsna0098cb737dd")
                .addHeader("X-RapidAPI-Host", "ivaee-internet-video-archive-entertainment-v1.p.rapidapi.com")
                .build();

        Log.d("search", request.toString());

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Call
                Log.d("search", "call from response: " + call.request().toString());

                //A Response has a headers and a body
                //Headers just contain info or metadata about the response like number of calls left for the free trial
                // or the Access control methods allowed like GET, POST, PUT, etc

                //The body has all of the data about the shows and movies found, if any..
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) {
                        throw new IOException("Unexpected code " + response);
                    }
                    String results = responseBody.string();
                    try {
                        acb.onCallback(results);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (IOException i) {
                    i.printStackTrace();
                }
            }
        });
    }

    public static void apiCallImage(String path, final ApiCallback acb) throws IOException {
        OkHttpClient client = new OkHttpClient(); //A client for networking with the Api online
        //Log.d("search", "Title: " + title );
        Request request = new Request.Builder() // This is the query we build
                .url("https://ivaee-internet-video-archive-entertainment-v1.p.rapidapi.com/Images/%7Bfilepath%7D/Redirect?Redirect=false")
                .get()
                .addHeader("x-rapidapi-host", "ivaee-internet-video-archive-entertainment-v1.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "0781c4e67fmsh14845fdab783a92p1a799ejsna0098cb737dd")
                .addHeader("accept", "application/json")
                .addHeader("filepath", path) //String title
                .addHeader("providers", "Netflix,Hulu,AmazonPrimeVideo,HBO,GooglePlay,iTunes")
                .addHeader("expirationminutes", "Relevance") //Options: Relevance, Timestamp, IvaRating, ReleaseDate
                .build();

        Log.d("search", request.toString());
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //A Response has a headers and a body
                //Headers just contain info or metadata about the response like number of calls left for the free trial
                // or the Access control methods allowed like GET, POST, PUT, etc

                //The body has all of the data about the shows and movies found, if any..
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) {
                        throw new IOException("Unexpected code " + response);
                    }
//
                    //Here is where we get the query results
                    String results = responseBody.string();
                    try {
                        acb.onCallback(results);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (IOException i) {
                    i.printStackTrace();
                }
            }
        });
    }

    static void checkProfileImg(final CheckCallBack ccb, final String username) {
        Log.d("img", "In checkProfileImg");
        Log.d("img", username);
        FirebaseStorage storage = FirebaseStorage.getInstance();

        ///Urw3ICjs0edn1hsM7ACFPWDMTeG3/profile_image.jpg
        StorageReference storageRef = storage.getReference();

        StorageReference userRef = storageRef.child(username);
        final StorageReference picRef = storageRef.child(username).child("profile_image.jpg");
        Log.d("img", picRef.toString());
        picRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ccb.onCallback(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.d("img", "Something went wrong");
                ccb.onCallback(false);
            }
        });
    }

    static void setProfileImg(String username, byte[] data) {
        Log.d("img", "In setProfileImg");

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference profileRef = storageRef.child(username);
        StorageReference profileImgRef = storageRef.child(username + "/profile_image.jpg");
        Log.d("img", "Reference created: " + profileImgRef);

        UploadTask uploadTask = profileImgRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.d("file", "upload FAILED");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Log.d("file", "upload SUCCESSFUL");
            }
        });
    }

    public static void downloadProfileImg(final ImageCallBack icb, String username) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child(username + "/profile_image.jpg");
        Log.d("file", "Downloading profile image");
        final long ONE_MEGABYTE = 1024 * 1024;
        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                icb.onCallback(bytes);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.d("check", "No Profile Image");
            }
        });
    }


}