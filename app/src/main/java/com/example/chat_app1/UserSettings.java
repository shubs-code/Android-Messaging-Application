package com.example.chat_app1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.chat_app1.firebase.Firestore;
import com.example.chat_app1.firebase.RealtimeDatabase;
import com.example.chat_app1.util.Codec;
import com.example.chat_app1.util.Constants;
import com.example.chat_app1.util.Preferences;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

public class UserSettings extends AppCompatActivity {

    EditText Name, About;
    Button Update ;
    ImageView ProfilePicture;
    ProgressBar progressBar;

    String UserEmail, UserName, UserProfileURL, UserAbout, changedName, changedAbout;
    Uri newImageUri;
    Preferences preferences;
    Firestore firestore;
    RealtimeDatabase realtimeDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        Name = findViewById(R.id.editTextTextPersonName);
        About = findViewById(R.id.editTextTextPersonName2);
        Update = findViewById(R.id.update);
        ProfilePicture = findViewById(R.id.profile_image);
        progressBar = findViewById(R.id.progressBar);

        firestore = new Firestore(this);
        preferences = new Preferences(this);
        realtimeDatabase = RealtimeDatabase.getFirebaseInstance(this);
        UserName = preferences.getString(Constants.UserName);
        UserAbout = preferences.getString(Constants.UserAbout);
        UserProfileURL = preferences.getString(Constants.UserProfileImageURL);
        UserEmail = preferences.getString(Constants.UserEmail);

        if(UserProfileURL!=null){Picasso.with(getApplicationContext()).load(UserProfileURL).into(ProfilePicture);}
        if(UserName!=null){Name.setText(UserName);}
        if(UserAbout!=null){About.setText(UserAbout);}

        Name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                changedName = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        About.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                changedAbout = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
                //Toast.makeText(getApplicationContext(),"clicked",Toast.LENGTH_SHORT).show();
            }
        });

        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(newImageUri!=null){
                    progressBar.setVisibility(View.VISIBLE);
                    firestore.uploadImageFile(newImageUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot snapshot) {
                                    firestore.getFileRef(newImageUri).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                                    {
                                        @Override
                                        public void onSuccess(Uri downloadUrl)
                                        {
                                            Log.d("Firestore",downloadUrl.toString());
                                            updateUserPofile(changedName,changedAbout,downloadUrl.toString());
                                            if(changedName!=null)preferences.writeDataString(Constants.UserName,changedName);
                                            if(changedAbout!=null)preferences.writeDataString(Constants.UserAbout,changedAbout);
                                            preferences.writeDataString(Constants.UserProfileImageURL,downloadUrl.toString());
                                            newImageUri = null;changedName = null; changedAbout = null;
                                            progressBar.setVisibility(View.INVISIBLE);
                                            Toast.makeText(getApplicationContext(),downloadUrl.toString(),Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // show message on failure may be network/disk ?
                                }
                            });
                }
            }
        });
    }

    private void updateUserPofile(String name , String about , String imageURL){
        String pathName = "users/" + Codec.Base64Encode(UserEmail) + "/profile/name";
        String pathAbout = "users/" + Codec.Base64Encode(UserEmail) + "/profile/about";
        String pathimageUrl = "users/" + Codec.Base64Encode(UserEmail) + "/profile/imageURL";
        String pathLastUpdated = "users/" + Codec.Base64Encode(UserEmail) + "/profile/lastUpdated";

        if(name!=null){realtimeDatabase.addString(pathName,name);}
        if(about!=null){realtimeDatabase.addString(pathAbout,about);}
        if(imageURL!=null){realtimeDatabase.addString(pathimageUrl,imageURL);}
        realtimeDatabase.addLong(pathLastUpdated,new Date().getTime());

    }

    int PICK_PHOTO_FOR_AVATAR = 11;
    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            try {
                InputStream inputStream = getApplicationContext().getContentResolver().openInputStream(data.getData());
                newImageUri = data.getData();
                Picasso.with(this).load(data.getData()).into(ProfilePicture);
                Toast.makeText(getApplicationContext(),"got image "+data.getData(),Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...
        }
    }
}