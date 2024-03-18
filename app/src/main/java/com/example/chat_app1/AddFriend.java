package com.example.chat_app1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chat_app1.database.DBHandler;
import com.example.chat_app1.event.NewUser;
import com.example.chat_app1.event.NewUserListener;
import com.example.chat_app1.firebase.RealtimeDatabase;
import com.example.chat_app1.model.UserProfile;
import com.example.chat_app1.util.Constants;
import com.example.chat_app1.util.Preferences;
import com.squareup.picasso.Picasso;

import java.util.Date;

public class AddFriend extends AppCompatActivity {

    EditText emailField;
    TextView emailLabel,userNameLabel;
    ConstraintLayout searchUser,gotUser;
    ImageView userProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        emailField = findViewById(R.id.email_input_field);
        emailLabel = findViewById(R.id.textView3);
        userNameLabel = findViewById(R.id.textView2);
        searchUser = findViewById(R.id.find_user);
        gotUser = findViewById(R.id.found_user);
        userProfileImage = findViewById(R.id.profile_picture);

        showLayout(true);
    }

    public void SearchUser(View view) {

        NewUserListener userListener = new NewUserListener();
        userListener.addListener(new NewUser() {
            @Override
            public void user(UserProfile user) {
                if(user!=null){
                    Log.d("FireBase",user.getName()+" fromListener");
                    emailLabel.setText(user.getEmail());
                    userNameLabel.setText(user.getName());
                    Picasso.with(getApplicationContext()).load(user.getImageURL()).centerCrop().fit().into(userProfileImage);
                    DBHandler db = new DBHandler(getApplicationContext());
                    Preferences preferences = new Preferences(getApplicationContext());
                    int userID = preferences.getInt(Constants.UserID);
                    db.addNewFriend(
                            user.getImageURL(),
                            user.getName(),
                            "Welcome to WIRED.",
                            user.getEmail(),
                            new Date().getTime(),
                            user.getLastUpdated(),
                            userID,
                            user.getFCMToken(),
                            user.getAbout()
                    );
                    db.CreateUserCommunicationTable(user.getEmail());
                    preferences.writeDataInt(Constants.UserID,++userID);
                    showLayout(false);
                }else{
                    Log.d("FireBase","user not found fromListener");
                }
            }
        });
        RealtimeDatabase.getFirebaseInstance(this).getUserByEmail(emailField.getText().toString(),userListener);
    }

    public void MessageUser(View view) {
        if(emailLabel.getText().length()>0){
            startActivity(new Intent(this, MainActivity.class));
            finish();
            //TODO
        }else{
            //TODO
        }
    }

    private void showLayout(boolean search){
        if(search){
            searchUser.setVisibility(View.VISIBLE);
            gotUser.setVisibility(View.GONE);

        }else{
            searchUser.setVisibility(View.GONE);
            gotUser.setVisibility(View.VISIBLE);

        }
    }
}