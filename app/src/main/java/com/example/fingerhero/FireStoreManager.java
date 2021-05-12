package com.example.fingerhero;

import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FireStoreManager {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference docRef;
    ListenerRegistration registration;
    Map<String, Article> articleMap = new HashMap<>();
    String articleCode;

    String userCode;

    public FireStoreManager() {
        //initialize();
    }

    public void getUserInfo(OnSuccessListener<DocumentReference> event) {
        db.collection("clients")
                .add(new UserInformation())
                .addOnSuccessListener(event)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("failed", "Error adding document", e);
                    }
                });
    }

    void initialize() {
        db.collection("Articles")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Article temp;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                temp = document.toObject(Article.class);
                                Log.d("success", document.getId() + " => " + document.getData());
                                articleMap.put(document.getId(), document.toObject(Article.class));
                                Log.d("Article Instance", document.getId() + " => " +
                                        "date : " + temp.getDate() + "favor : " + temp.getFavor());
                                for (Comment c : temp.getComments()) {
                                    Log.d("Comment", "text : " + c.getText());
                                }
                            }
                        } else {
                            Log.w("failed", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    Map<String, Article> getArticles() {
        return articleMap;
    }

    void setArticle(String articleCode) {
        this.articleCode = articleCode;
        docRef = db.collection("Articles").document(articleCode);
    }

    void addComment(String articleCode, Comment comment) {
        DocumentReference docRef = db.collection("Articles").document(articleCode);

        docRef.update("comments", FieldValue.arrayUnion(comment));
    }

    void removeComment(String articleCode, Comment comment) {
        DocumentReference docRef = db.collection("Articles").document(articleCode);

        docRef.update("comments", FieldValue.arrayRemove(comment));
    }

    void addFavor(String articleCode) {
        DocumentReference washingtonRef = db.collection("Articles").document(articleCode);

        washingtonRef.update("favor", FieldValue.increment(1));
    }

    void removeFavor(String articleCode) {
        DocumentReference washingtonRef = db.collection("Articles").document(articleCode);

        washingtonRef.update("favor", FieldValue.increment(-1));
    }

    void addSnapshotListener(EventListener<DocumentSnapshot> event) {
        registration = docRef.addSnapshotListener(event);
    }

    void freeSnapshotListener() {
        registration.remove();
    }
}
