package com.tellme.kelvin.campconnect;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by AkshayeJH on 24/07/17.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{


    private FirebaseAuth mAuth;
    private List<Messages> mMessageList;
    private DatabaseReference mUserDatabase;
    private Context context;
    public MessageAdapter(List<Messages> mMessageList) {

        this.mMessageList = mMessageList;


    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mAuth = FirebaseAuth.getInstance();
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_messages_layout ,parent, false);
        context = parent.getContext();

        return new MessageViewHolder(v);

    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView rmessageText;
        public TextView smessageText;
        public CircleImageView profileImage;
        public CircleImageView profileImage2;
        public TextView displayName;
        public ImageView messageImage;
        public RelativeLayout rel;

        public MessageViewHolder(View view) {
            super(view);

            rel = view.findViewById(R.id.relativeLayout);
            smessageText = view.findViewById(R.id.sender_messsage_text);
            rmessageText = view.findViewById(R.id.receiver_message_text);
            profileImage = view.findViewById(R.id.message_profile_image);
            profileImage2 = view.findViewById(R.id.message_profile_image2);
            displayName = view.findViewById(R.id.name_text_layout);
            messageImage = view.findViewById(R.id.message_image_layout);

        }
    }

    @Override
    public void onBindViewHolder(final MessageViewHolder viewHolder, final int i) {
        String messageSenderId = mAuth.getCurrentUser().getUid();

        Messages c = mMessageList.get(i);

        String from_user = c.getFrom();
        String message_type = c.getType();


        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(from_user);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        if (from_user.equals(messageSenderId))
        {
            viewHolder.rel.setVisibility(View.VISIBLE);
            viewHolder.smessageText.setVisibility(View.VISIBLE);
            viewHolder.rmessageText.setVisibility(View.GONE);
            viewHolder.profileImage.setVisibility(View.GONE);
            viewHolder.profileImage2.setVisibility(View.VISIBLE);

            viewHolder.smessageText.setText(c.getMessage() + "\n" + c.getMtime() + " - " + c.getMdate());
        }
        else
        {
            viewHolder.rel.setVisibility(View.VISIBLE);
            viewHolder.smessageText.setVisibility(View.GONE);
            viewHolder.rmessageText.setVisibility(View.VISIBLE);
            viewHolder.profileImage.setVisibility(View.VISIBLE);
            viewHolder.profileImage2.setVisibility(View.GONE);

            viewHolder.rmessageText.setText(c.getMessage() + "\n" + c.getMtime() + "   " + c.getMdate());
        }


        if (from_user.equals(messageSenderId)){
           viewHolder.profileImage2.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(final View v) {
                   CharSequence options[] = new CharSequence[]
                           {
                                   "Delete for me",
                                   "Delete for everyone",
                                   "Cancel"
                           };
                   final AlertDialog.Builder builder = new AlertDialog.Builder(viewHolder.itemView.getContext());
                   builder.setTitle("");

                   builder.setItems(options, new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int b) {
                           if (b == 0)
                           {
                               deleteSentMessages(i, viewHolder);
                               mMessageList.remove(i);
                               notifyDataSetChanged();

                           }
                           else if (b == 1)
                               {
                                   deleteMessagesEveryone(i, viewHolder);
                                   mMessageList.remove(i);
                                   notifyDataSetChanged();
                               }
                               else if (b == 2)
                               {
                               }

                       }
                   });
                   builder.show();
               }
           });
        }else {
            viewHolder.profileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    CharSequence options[] = new CharSequence[]
                            {
                                    "View Profile",
                                    "Cancel"
                            };
                    final AlertDialog.Builder builder = new AlertDialog.Builder(viewHolder.itemView.getContext());
                    builder.setTitle("");

                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int b) {
                            if (b == 0)
                            {
                                Intent newIntent = new Intent(context, ProfileActivity.class);
                                newIntent.putExtra("user_id", mMessageList.get(i).getFrom());
                                context.startActivity(newIntent);

                            }
                            else if (b == 1)
                            {

                            }
                        }
                    });
                    builder.show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }



private void deleteSentMessages(final int i, final MessageViewHolder holder){
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.child("messages")
                .child(mMessageList.get(i).getFrom())
                .child(mMessageList.get(i).getTo())
                .child(mMessageList.get(i).getMessageID())
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(holder.itemView.getContext(), "Deleted Successfully",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(holder.itemView.getContext(), "Error Occured",Toast.LENGTH_LONG).show();
                }
            }
        });

}

    private void deleteRecieveMessages(final int i, final MessageViewHolder holder){
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.child("messages")
                .child(mMessageList.get(i).getTo())
                .child(mMessageList.get(i).getFrom())
                .child(mMessageList.get(i).getMessageID())
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(holder.itemView.getContext(), "Deleted Successfully",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(holder.itemView.getContext(), "Error Occured",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void deleteMessagesEveryone(final int i, final MessageAdapter.MessageViewHolder holder) {
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.child("messages")
                .child(mMessageList.get(i).getFrom())
                .child(mMessageList.get(i).getTo())
                .child(mMessageList.get(i).getMessageID())
                .removeValue();
        rootRef.child("messages")
                .child(mMessageList.get(i).getTo())
                .child(mMessageList.get(i).getFrom())
                .child(mMessageList.get(i).getMessageID())
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(holder.itemView.getContext(), "Deleted Successfully", Toast.LENGTH_LONG).show();
                }
            }
        });


    }






}
