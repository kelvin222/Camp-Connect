package com.tellme.kelvin.campconnect;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatroomAdapter extends RecyclerView.Adapter<ChatroomAdapter.MessageViewHolder>{

    public Context context;

    private FirebaseAuth mAuth;
    private List<Chatroom> mChatroomList;
    private DatabaseReference mUserDatabase;

    public ChatroomAdapter(List<Chatroom> mChatroomList) {

        this.mChatroomList = mChatroomList;


    }

    @Override
    public ChatroomAdapter.MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mAuth = FirebaseAuth.getInstance();
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_chat ,parent, false);
        context = parent.getContext();


        return new ChatroomAdapter.MessageViewHolder(v);

    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView rmessageText;
        public TextView smessageText;
        public TextView rmessageName;
        public TextView smessageName;
        public TextView rmessageDate;
        public TextView smessageDate;
        public CircleImageView profileImage;
        public CircleImageView profileImage2;
        public CardView rcardView;
        public CardView scardView;
        public RelativeLayout rel;

        public MessageViewHolder(View view) {
            super(view);

            rel = view.findViewById(R.id.relativeLayout);
            smessageText = view.findViewById(R.id.schat);
            smessageName = view.findViewById(R.id.sname);
            smessageDate = view.findViewById(R.id.sdate);
            rmessageText = view.findViewById(R.id.echat);
            rmessageName = view.findViewById(R.id.ename);
            rmessageDate = view.findViewById(R.id.edate);
            profileImage = view.findViewById(R.id.message_profile_image);
            profileImage2 = view.findViewById(R.id.message_profile_image2);
            rcardView = view.findViewById(R.id.cardr);
            scardView = view.findViewById(R.id.cards);

        }
    }

    @Override
    public void onBindViewHolder(final ChatroomAdapter.MessageViewHolder viewHolder, final int i) {
        String messageSenderId = mAuth.getCurrentUser().getUid();

        Chatroom c = mChatroomList.get(i);

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
            viewHolder.rcardView.setVisibility(View.VISIBLE);
            viewHolder.scardView.setVisibility(View.GONE);
            viewHolder.profileImage.setVisibility(View.GONE);
            viewHolder.profileImage2.setVisibility(View.VISIBLE);

            viewHolder.rmessageName.setText(c.getName());
            viewHolder.rmessageText.setText(c.getMessage());
            viewHolder.rmessageDate.setText(c.getMtime() + " - " + c.getMdate());
        }
        else
        {
            viewHolder.rel.setVisibility(View.VISIBLE);
            viewHolder.rcardView.setVisibility(View.GONE);
            viewHolder.scardView.setVisibility(View.VISIBLE);
            viewHolder.profileImage.setVisibility(View.VISIBLE);
            viewHolder.profileImage2.setVisibility(View.GONE);

            viewHolder.smessageName.setText(c.getName());
            viewHolder.smessageText.setText(c.getMessage());
            viewHolder.smessageDate.setText(c.getMtime() + " - " + c.getMdate());
        }


        if (from_user.equals(messageSenderId)){
            viewHolder.profileImage2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    CharSequence options[] = new CharSequence[]
                            {

                                    "Delete this",
                                    "Cancel"
                            };
                    final AlertDialog.Builder builder = new AlertDialog.Builder(viewHolder.itemView.getContext());
                    builder.setTitle("");

                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int b) {
                            if (b == 0)
                            {
                                deleteMessages(i, viewHolder);
                                mChatroomList.remove(i);
                                notifyDataSetChanged();

                            }
                            else if (b == 1)
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
                                newIntent.putExtra("user_id", mChatroomList.get(i).getFrom());
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
        return mChatroomList.size();
    }



    private void deleteMessages(final int i, final ChatroomAdapter.MessageViewHolder holder){
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.child("chatroom")
                .child(mChatroomList.get(i).getState())
                .child(mChatroomList.get(i).getBatch())
                .child(mChatroomList.get(i).getMessageID())
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








}
