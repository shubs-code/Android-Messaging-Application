package com.example.chat_app1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.chat_app1.firebase.RealtimeDatabase;
import com.example.chat_app1.model.UserProfile;
import com.example.chat_app1.util.Constants;
import com.example.chat_app1.util.Preferences;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SignIn extends AppCompatActivity {

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private ProgressBar loading ;
    private ConstraintLayout process;
    private ConstraintLayout complete;
    private ImageView imageView1 ;
    private TextView username;
    private TextView useremail;
    // [START declare_auth]
    private FirebaseAuth mAuth;

    private Context context;
    // [END declare_auth]


    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        imageView1 = findViewById(R.id.profile_picture);
        loading = findViewById(R.id.progressBar);
        process = findViewById(R.id.sign_in_process);
        complete = findViewById(R.id.sign_in_succes);
        process = findViewById(R.id.sign_in_process);
        complete = findViewById(R.id.sign_in_succes);
        username = findViewById(R.id.textView2);
        useremail = findViewById(R.id.textView3);

        context = getApplicationContext();
        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // [END config_signin]

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    // [END on_start_check_user]

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }
                    }
                });
    }
    // [END auth_with_google]

    // [START signin]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]

    private void updateUI(FirebaseUser user) {
        if(user!=null){
            UserProfile newUser = new UserProfile(user.getEmail());
            newUser.setImageURL(user.getPhotoUrl().toString());
            InitializeApplicaion(newUser);
            RealtimeDatabase.getFirebaseInstance(this).setUserProfile(newUser);
            Log.d(TAG, "User Signed in");
            Log.d(TAG, "User data : "+user.getDisplayName()+user.getEmail()+user.getPhotoUrl().toString());
            complete.setVisibility(View.INVISIBLE);
            String url = user.getPhotoUrl().toString();
            //Picasso.with(this).load(url).into(imageView1);
            Picasso.with(this).load(url).centerCrop().fit().into(imageView1);
            username.setText(user.getDisplayName());
            useremail.setText(user.getEmail());
            loading.setVisibility(View.GONE );
            process.setVisibility(View.GONE);
            complete.setVisibility(View.VISIBLE);
        }
        else{
            Log.d(TAG, "User Un signed");
        }
    }

    public void buttonpress(View view) {
        loading.setVisibility(View.VISIBLE);
        signIn();
    }

    public void logOut(View view) {
        mAuth.getCurrentUser().delete().addOnCompleteListener(this,new OnCompleteListener<Void>(){
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                startActivity(new Intent(context, MainActivity.class));
                finish();
            }
        });
    }

    public void back(View view) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void InitializeApplicaion(UserProfile user){//TODO
        Preferences preferences = new Preferences(context);
        preferences.writeDataString(Constants.UserName,user.getName());
        preferences.writeDataString(Constants.UserAbout,user.getAbout());
        preferences.writeDataString(Constants.UserEmail,user.getEmail());
        preferences.writeDataString(Constants.UserProfileImageURL,user.getImageURL());
        if(!preferences.getBool("Initialized")){
            preferences.writeDataInt(Constants.UserID,0);
        }
    }
}