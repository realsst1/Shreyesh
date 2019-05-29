package com.example.shreyesh;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter {

    private List<Messages> messageList;
    private static final int VIEW_TYPE_MESSAGE_SENT=1;
    private static final int VIEW_TYPE_MESSAGE_RECIEVED=2;
    private static final int VIEW_TYPE_MESSAGE_RECIEVED_IMAGE=4;
    private static final int VIEW_TYPE_MESSAGE_SENT_IMAGE=5;
    private static final int VIEW_TYPE_MIDDLE_MESSAGE=3;
    private static final String currentUserID = "eHTD1GfzsJMkxyvtZcaO2O1Nq9q1";
    private static final String chatUserID = "yaZg0RepRPMOr9lt3bpLLRqx1e03";

    public MessageAdapter(List<Messages> messageList){
        this.messageList=messageList;
    }

    @Override
    public int getItemViewType(int position) {

        String fromUserID=messageList.get(position).getFrom();
        String type=messageList.get(position).getType();
        if(fromUserID.equals(currentUserID) && type.equals("text"))
            return VIEW_TYPE_MESSAGE_SENT;
        else if(fromUserID.equals(currentUserID) && type.equals("image"))
            return VIEW_TYPE_MESSAGE_SENT_IMAGE;
        else if(!fromUserID.equals(currentUserID) && type.equals("text"))
            return VIEW_TYPE_MESSAGE_RECIEVED;
        else
            return VIEW_TYPE_MESSAGE_RECIEVED_IMAGE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view;
        if(i==1){
            view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_single_layout_s,viewGroup,false);
            return new SentViewHolder(view);
        }else if(i==2){
            view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_single_layout_r,viewGroup,false);
            return new RecievedViewHolder(view);
        }
        else if(i==4){
            view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_single_image_r,viewGroup,false);
            return new RecievedViewHolderImage(view);
        }
        else{
            view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_single_image_s,viewGroup,false);
            return new SentViewHolderImage(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        String message=messageList.get(i).getMessage();
        switch (viewHolder.getItemViewType()){
            case VIEW_TYPE_MESSAGE_RECIEVED:
                ((RecievedViewHolder)viewHolder).setMessage(message);
                break;
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentViewHolder)viewHolder).setMessage(message);
                break;
            case VIEW_TYPE_MESSAGE_RECIEVED_IMAGE:
                ((RecievedViewHolderImage)viewHolder).setImage(message);
                break;
            case VIEW_TYPE_MESSAGE_SENT_IMAGE:
                ((SentViewHolderImage)viewHolder).setImage(message);
                break;
        }
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
        public void setMessage(String msg){
            TextView textView=(TextView)view.findViewById(R.id.messageRecieved);
            textView.setText(msg);
        }

    }

    public class SentViewHolder extends RecyclerView.ViewHolder{
        View view;
        public SentViewHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
        }
        public void setMessage(String msg){
            TextView textView=(TextView)view.findViewById(R.id.messageSent);
            textView.setText(msg);
        }
    }

    public class SentViewHolderImage extends RecyclerView.ViewHolder{
        View view;
        public SentViewHolderImage(@NonNull View itemView) {
            super(itemView);
            view=itemView;
        }
        public void setImage(final String url){
            final ImageView imageView=(ImageView)view.findViewById(R.id.imageSent);
            System.out.println(imageView);
            Picasso.get().load(url).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.placeholder)
                    .into(imageView, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    Picasso.get().load(url).placeholder(R.drawable.placeholder).into(imageView);
                }
            });
        }
    }

    public class RecievedViewHolderImage extends RecyclerView.ViewHolder{
        View view;
        public RecievedViewHolderImage(@NonNull View itemView) {
            super(itemView);
            view=itemView;
        }
        public void setImage(final String url){
            final ImageView imageView=(ImageView)view.findViewById(R.id.imageRecieved);
           Picasso.get().load(url).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.placeholder)
                   .into(imageView, new Callback() {
                       @Override
                       public void onSuccess() {

                       }

                       @Override
                       public void onError(Exception e) {
                            Picasso.get().load(url).placeholder(R.drawable.placeholder).into(imageView);
                       }
                   });
        }
    }
}
