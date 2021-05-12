package com.example.fingerhero;

import android.graphics.Color;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Handler handler = new Handler();
    String response;
    TextView textView;
    DocumentReference docRef;
    ListenerRegistration registration;
    Comment com = null;
    boolean flag = false;
    FireStoreManager fsm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);

        //docRef = db.collection("Room").document("1");
        //setSnapshotListener();

        fsm = new FireStoreManager();
        fsm.setArticle("test");
        fsm.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {
                    Log.w("Snap", "Listen failed.", error);
                    return;
                }

                if (value != null && value.exists()) {
                    Log.d("Snap", "Current data: " + value.getData());

                    Article article = value.toObject(Article.class);

                    String text = "day : " + article.getDate()
                            + "\nfavor : " + article.getFavor()
                            + "\ntag : "   + article.getTag()
                            + "\nurl : "   + article.getUrl();

                    for (Comment com : article.getComments()) {
                        text += "\n\nUser : " + com.getUser()
                                + "\n\ttext : " + com.getText();
                    }

                    textView.setText(text);
                } else {
                    Log.d("Snap", "Current data: null");
                }
            }
        });

        findViewById(R.id.button1).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                addValue();
            }
        });

        findViewById(R.id.button2).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                //getValue();
                new FireStoreManager();
            }
        });

        findViewById(R.id.button3).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                fsm.freeSnapshotListener();
            }
        });

        findViewById(R.id.button4).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                String txt = ((EditText)findViewById(R.id.editText)).getText().toString();
                com = new Comment("", "" , "", txt);
                new FireStoreManager().addComment("test", com);
            }
        });
        findViewById(R.id.button6).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                new FireStoreManager().removeComment("test", com);
            }
        });
        findViewById(R.id.button7).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                new FireStoreManager().getUserInfo(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("success", "DocumentSnapshot written with ID: " + documentReference.getId());
                        ((TextView)findViewById(R.id.textView)).setText(documentReference.getId());
                    }
                });
            }
        });

        findViewById(R.id.button5).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                update();
            }
        });

        findViewById(R.id.textViewFavor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!flag) {
                    new FireStoreManager().addFavor("test");
                    flag = true;
                    v.setBackgroundColor(Color.parseColor("#FFFF5E5E"));
                }

                else {
                    new FireStoreManager().removeFavor("test");
                    flag = false;
                    v.setBackgroundColor(Color.parseColor("#FF6200EE"));
                }
            }
        });
    }

    public void setUserCode(String user) {
        ((TextView)findViewById(R.id.textView)).setText(user);
    }

    void setSnapshotListener() {
        registration =
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("Snap", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d("Snap", "Current data: " + snapshot.getData());
                    textView.setText(snapshot.getData().toString());
                } else {
                    Log.d("Snap", "Current data: null");
                }
            }
        });
    }

    void freeSnapshotListener() {
        registration.remove();
    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    void addValue() {
        // Create a new user with a first and last name

        Article article = new Article("today", "refref", "scholar", 0);
        article.addComment(new Comment("tommorrow", "31", "0", "firestore on fire"));
        article.addComment(new Comment("4 days later..", "22", "0", "Escape from here"));
        article.addComment(new Comment("someday", "200100", "0", "Run bitch!! Run!!!!!"));

        // Add a new document with a generated ID
        db.collection("Articles").document("test")
                .set(article)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("success", "DocumentSnapshot added with ID: 1");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("failed", "Error adding document", e);
                    }
                });
    }

    void getValue() {
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("success", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w("failed", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    int cnt = 0;
    void update() {
        DocumentReference docRef = db.collection("Articles").document("default");

        Map<String, Object> comment = new HashMap<>();

        comment.put("date", "21:05:11:15");
        comment.put("text", "update!");
        comment.put("mention", 0);
        comment.put("user", ++cnt);

        docRef.update("comments", FieldValue.arrayUnion(comment));
    }
}