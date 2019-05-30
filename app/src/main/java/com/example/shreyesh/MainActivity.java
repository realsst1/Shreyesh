package com.example.shreyesh;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import id.zelory.compressor.Compressor;

public class MainActivity extends AppCompatActivity {

    private static final String currentUserID = "eHTD1GfzsJMkxyvtZcaO2O1Nq9q1";
    private static final String chatUserID = "yaZg0RepRPMOr9lt3bpLLRqx1e03";
    private ChatFragment chatFragment;
    private ImageView send, sendImage;
    private EditText message;
    private Toolbar mainToolbar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressDialog dialog;
    private StorageReference storageReference;
    private DatabaseReference messageRef, databaseReference;

    private BroadcastReceiver mConnReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            String reason = intent.getStringExtra(ConnectivityManager.EXTRA_REASON);
            boolean isFailover = intent.getBooleanExtra(ConnectivityManager.EXTRA_IS_FAILOVER, false);

            NetworkInfo currentNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            NetworkInfo otherNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);

            if (currentNetworkInfo.isConnected()) {
                //Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_LONG).show();
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, chatFragment).commitAllowingStateLoss();
                send.setEnabled(true);
                sendImage.setEnabled(true);
            } else {
                //Toast.makeText(getApplicationContext(), "Not Connected", Toast.LENGTH_LONG).show();
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new NoInternetFragment()).commitAllowingStateLoss();
                send.setEnabled(false);
                sendImage.setEnabled(false);
            }


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.refresh);
        send = (ImageView) findViewById(R.id.sendMessage);
        sendImage = (ImageView) findViewById(R.id.addImage);
        message = (EditText) findViewById(R.id.messageToSend);

        mainToolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);

        chatFragment = new ChatFragment();
        dialog=new ProgressDialog(this);
        dialog.setTitle("Sending your image");
        dialog.setCanceledOnTouchOutside(false);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.frag_container, chatFragment);
        transaction.commit();


        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        messageRef = FirebaseDatabase.getInstance().getReference().child("messages");
        messageRef.keepSynced(true);
        databaseReference.keepSynced(true);

        this.registerReceiver(this.mConnReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));


        //send button
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("Clicked");
                String msg = message.getText().toString();
                if (msg.length() == 0) {
                    Toast.makeText(MainActivity.this, "Please enter some text", Toast.LENGTH_LONG).show();
                    return;
                }

                String currentUserRef = "messages/" + currentUserID + "/" + chatUserID;
                String chatUserRef = "messages/" + chatUserID + "/" + currentUserID;
                String from = currentUserID;

                DatabaseReference userMessagePush = FirebaseDatabase.getInstance().getReference().child("messages").child(currentUserID).child(chatUserID).push();

                String pushID = userMessagePush.getKey();
                Map messageMap = new HashMap();
                messageMap.put("message", msg);
                messageMap.put("from", from);
                messageMap.put("type", "text");
                messageMap.put("time", ServerValue.TIMESTAMP);

                Map messageUserMap = new HashMap();
                messageUserMap.put(currentUserRef + "/" + pushID, messageMap);
                messageUserMap.put(chatUserRef + "/" + pushID, messageMap);

                FirebaseDatabase.getInstance().getReference().updateChildren(messageUserMap);
                message.setText("");

            }
        });


        //send Image
        sendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .setMinCropResultSize(512, 512)
                        .start(MainActivity.this);
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                dialog.show();
                Uri resultUri = result.getUri();
                String userID = currentUserID;
                File thumnbailFile = new File(resultUri.getPath());
                Bitmap thumbnail = null;

                try {
                    thumbnail = new Compressor(this)
                            .setMaxWidth(200)
                            .setMaxHeight(200)
                            .setQuality(50)
                            .compressToBitmap(thumnbailFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                final byte[] dataValue = byteArrayOutputStream.toByteArray();

                DatabaseReference userMessagePush = databaseReference.child("messages").child(currentUserID).child(chatUserID).push();
                final String pushID = userMessagePush.getKey();
                final StorageReference thumbPath = storageReference.child("images").child(pushID + ".jpg");

                final StorageReference filepath = storageReference.child("images").child(pushID + ".jpg");
                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {

                            final String downloadURI = task.getResult().toString();
                            UploadTask uploadTask = thumbPath.putBytes(dataValue);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        final String currentUserRef = "messages/" + currentUserID + "/" + chatUserID;
                                        final String chatUserRef = "messages/" + chatUserID + "/" + currentUserID;
                                        final String from = currentUserID;
                                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                Map messageMap = new HashMap();
                                                messageMap.put("message", uri.toString());
                                                messageMap.put("from", from);
                                                messageMap.put("type", "image");
                                                messageMap.put("time", ServerValue.TIMESTAMP);

                                                Map messageUserMap = new HashMap();
                                                messageUserMap.put(currentUserRef + "/" + pushID, messageMap);
                                                messageUserMap.put(chatUserRef + "/" + pushID, messageMap);

                                                databaseReference.updateChildren(messageUserMap).addOnCompleteListener(new OnCompleteListener() {
                                                    @Override
                                                    public void onComplete(@NonNull Task task) {
                                                        if (task.isSuccessful())
                                                            dialog.dismiss();
                                                            System.out.println("Uploaded");
                                                    }
                                                });
                                            }
                                        });

                                    }
                                }
                            });


                        }

                    }
                });


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

}
