package com.example.shreyesh;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ChatFragment extends Fragment {

    private RecyclerView recyclerView;
    private MessageAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        System.out.println("Chat Fragment Loaded");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);

    }

}
