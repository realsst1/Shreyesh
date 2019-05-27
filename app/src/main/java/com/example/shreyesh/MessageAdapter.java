package com.example.shreyesh;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter {

    private List<Message> messageList;
    private static final int VIEW_TYPE_MESSAGE_SENT=1;
    private static final int VIEW_TYPE_MESSAGE_RECIEVED=2;
    private static final int VIEW_TYPE_MIDDLE_MESSAGE=3;

    public MessageAdapter(List<Message> messageList){
        this.messageList=messageList;
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class RecievedViewHolder extends RecyclerView.ViewHolder{
        View view;
        public RecievedViewHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
        }
    }

    public class SentViewHolder extends RecyclerView.ViewHolder{
        View view;
        public SentViewHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
        }
    }
}
