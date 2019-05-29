package com.example.shreyesh;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ChatFragment extends Fragment {


    private ProgressDialog dialog;
    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    private List<Messages> messageList;
    private DatabaseReference messageRef;
    private static final String currentUserID = "eHTD1GfzsJMkxyvtZcaO2O1Nq9q1";
    private static final String chatUserID = "yaZg0RepRPMOr9lt3bpLLRqx1e03";
    private LinearLayoutManager layoutManager;
    private static final int TOTAL_ITEMS_TO_LOAD = 10;
    private int currentPage = 1;
    private int itemPos = 0;

    private String lastKey = "";
    private String prevKey = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        System.out.println("Chat Fragment Loaded");
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_chat, container, false);

        messageRef= FirebaseDatabase.getInstance().getReference().child("messages");
        recyclerView=(RecyclerView)view.findViewById(R.id.messageRecyclerView);
        messageList=new ArrayList<>();
        adapter=new MessageAdapter(messageList);
        layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

        messageRef=FirebaseDatabase.getInstance().getReference();
        messageRef.keepSynced(true);

        loadMessages();
        return view;

    }

    public void loadMessages(){
        final DatabaseReference messageDatabaseReference = messageRef.child("messages").child(currentUserID).child(chatUserID);
        Query messageQuery = messageDatabaseReference.limitToLast(currentPage * TOTAL_ITEMS_TO_LOAD);
        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Messages message=dataSnapshot.getValue(Messages.class);
                System.out.println("Message ggg:"+message.getMessage());
                messageList.add(message);
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(messageList.size()-1);
                itemPos++;
                if (itemPos == 1) {
                    String msgKey = dataSnapshot.getKey();
                    lastKey = msgKey;
                    prevKey = msgKey;
                }

                messageDatabaseReference.keepSynced(true);
                loadMoreMessages();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


        public void loadMoreMessages() {
            final DatabaseReference messageDatabaseReference = messageRef.child("messages").child(currentUserID).child(chatUserID);

            Query messageQuery = messageDatabaseReference.orderByKey().endAt(lastKey).limitToLast(10);

            messageQuery.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    Messages messages = dataSnapshot.getValue(Messages.class);
                    String msgKey = dataSnapshot.getKey();
                    if (!prevKey.equals(lastKey)) {
                        messageList.add(itemPos++, messages);

                    } else {
                        prevKey = lastKey;
                    }

                    if (itemPos == 1) {

                        lastKey = msgKey;
                    }


                    adapter.notifyDataSetChanged();

                    messageDatabaseReference.keepSynced(true);
                    layoutManager.scrollToPositionWithOffset(10, 0);


                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }
}
