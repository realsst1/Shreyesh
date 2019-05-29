package com.example.shreyesh;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class ChatFragment extends Fragment {

    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    private List<Message> messageList;
    private DatabaseReference messageRef;


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


        messageRef.keepSynced(true);
        return view;

    }

}
