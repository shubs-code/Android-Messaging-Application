package com.example.chat_app1.firebase;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.nio.charset.StandardCharsets;

public class Firestore {
    Context context;
    FirebaseStorage storage = FirebaseStorage.getInstance("gs://messaging-application-f71c9.appspot.com");

    public Firestore(Context context) {
        this.context = context;
    }
    public void test(){
        StorageReference storageRef = storage.getReference();
        StorageReference mountainImagesRef = storageRef.child("text/hello.txt");
        mountainImagesRef.putBytes("hello world".getBytes(StandardCharsets.UTF_8));

    }
    public UploadTask uploadImageFile(Uri imagUri) {
        StorageReference sref = storage.getReference();
        if (imagUri != null) {
            final StorageReference imageRef = sref.child("android/media").child(imagUri.getLastPathSegment());
             return imageRef.putFile(imagUri);
        }
        return  null;
    }
    public StorageReference getFileRef(Uri imagUri){
        StorageReference sref = storage.getReference();
        return sref.child("android/media").child(imagUri.getLastPathSegment());
        }

}
