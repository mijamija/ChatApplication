package com.example.interns.chatapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class chatRoom extends AppCompatActivity {

    Button send;
    EditText message;
    TextView text;
    String user, roomName, tempKey, userMessage, userName;
    DatabaseReference root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        send = (Button) findViewById(R.id.send);
        message = (EditText) findViewById(R.id.message);
        text = (TextView) findViewById(R.id.textView);

        user = getIntent().getExtras().get("userName").toString();
        roomName = getIntent().getExtras().get("roomName").toString();

        root = FirebaseDatabase.getInstance().getReference().child(roomName);

        setTitle("Room - " + roomName);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map = new HashMap<>();
                tempKey = root.push().getKey();
                root.updateChildren(map);

                DatabaseReference messageRoot = root.child(tempKey);

                Map<String, Object> map2 = new HashMap<>();
                map2.put("name", user);
                map2.put("msg", message.getText().toString());

                messageRoot.updateChildren(map2);
            }
        });

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                appendConversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                appendConversation(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void appendConversation( DataSnapshot dataSnapshot)
    {

        Iterator i = dataSnapshot.getChildren().iterator();

        while(i.hasNext())
        {
            userMessage = (String) (((DataSnapshot)i.next()).getValue());
            userName = (String) (((DataSnapshot)i.next()).getValue());

            text.append(userName + " : " + userMessage + "\n");
        }
    }
}
