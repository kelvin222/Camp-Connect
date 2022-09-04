package com.tellme.kelvin.campconnect;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment {

    private RecyclerView mConvList;

    private DatabaseReference mConvDatabase;
    private DatabaseReference mMessageDatabase;
    private DatabaseReference mUsersDatabase;

    private FloatingActionButton Platoon;

    private String user_id;
    private String user_thumb;
    private String user_name;

    private FirebaseAuth mAuth;
    private LinearLayoutManager mLinearLayout;
    private String mCurrent_user_id;

    private View mMainView;

    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMainView = inflater.inflate(R.layout.fragment_chats, container, false);

        mConvList = mMainView.findViewById(R.id.conv_list);
        mAuth = FirebaseAuth.getInstance();
        Platoon = (FloatingActionButton) mMainView.findViewById(R.id.flotButton);

        mCurrent_user_id = mAuth.getCurrentUser().getUid();

        mConvDatabase = FirebaseDatabase.getInstance().getReference().child("Chat").child(mCurrent_user_id);
        mConvDatabase.keepSynced(true);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mUsersDatabase.keepSynced(true);
        mMessageDatabase = FirebaseDatabase.getInstance().getReference().child("messages").child(mCurrent_user_id);
        mMessageDatabase.keepSynced(true);

        mUsersDatabase.child(mCurrent_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String regno = dataSnapshot.child("regno").getValue().toString();
                final String state = dataSnapshot.child("state").getValue().toString();
                final String batch = dataSnapshot.child("batch").getValue().toString();
                final String name = dataSnapshot.child("name").getValue().toString();
                final String cregno = dataSnapshot.child("regno").getValue().toString();

                Platoon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent chatroomIntent = new Intent(getContext(), ChatRoomActivity.class);
                        String reg = regno.substring(regno.length() - 1);
                        chatroomIntent.putExtra("platoon", reg);
                        chatroomIntent.putExtra("state", state);
                        chatroomIntent.putExtra("batch", batch);
                        chatroomIntent.putExtra("name", name);
                        chatroomIntent.putExtra("regno", cregno);
                        startActivity(chatroomIntent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mLinearLayout = new LinearLayoutManager(getContext());
        mLinearLayout.setReverseLayout(true);
        mLinearLayout.setStackFromEnd(true);

        mConvList.setHasFixedSize(true);
        mConvList.setLayoutManager(mLinearLayout);


        // Inflate the layout for this fragment
        return mMainView;
    }


    @Override
    public void onStart() {
        super.onStart();

        Query conversationQuery = mConvDatabase.orderByChild("timestamp");
        FirebaseRecyclerOptions<Conv> options =
                new FirebaseRecyclerOptions.Builder<Conv>()
                        .setIndexedQuery(conversationQuery,mConvDatabase, Conv.class)
                        .build();

        FirebaseRecyclerAdapter<Conv, ConvViewHolder> adapter = new FirebaseRecyclerAdapter<Conv, ConvViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ConvViewHolder holder, int i, @NonNull final Conv model) {


                final String list_user_id = getRef(i).getKey();
                user_id = list_user_id;
                holder.itemView.setTag(i);

                Query lastMessageQuery = mMessageDatabase.child(list_user_id).limitToLast(1);
                lastMessageQuery.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        String data = dataSnapshot.child("message").getValue().toString();
                        holder.setMessage(data, model.isSeen());

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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
                mUsersDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final String userName = dataSnapshot.child("name").getValue().toString();
                        user_name = userName;
                        String userThumb = dataSnapshot.child("image").getValue().toString();
                        user_thumb = userThumb;
                        String userReg = dataSnapshot.child("regno").getValue().toString();
                        String userOnline = dataSnapshot.child("online").getValue().toString();


                        holder.setUserOnline(userOnline);
                        holder.userNameView.setText(userName);
                        holder.setUserImage(userThumb, getContext());
                        holder.userRegView.setText(userReg);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int position = (int) view.getTag();
                                String user_id = getRef(position).getKey();
                                Intent chatIntent = new Intent(getContext(), ChatActivity.class);
                                chatIntent.putExtra("user_id1", user_id);
                                chatIntent.putExtra("user_name", user_name);
                                chatIntent.putExtra("user_thumb", user_thumb);
                                startActivity(chatIntent);



                            }
                        });

                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
            @NonNull
            @Override
            public ConvViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_single_layout, viewGroup, false);
                ConvViewHolder viewHolder = new ConvViewHolder(view);


                return viewHolder;
            }
        };




        mConvList.setAdapter(adapter);
        adapter.startListening();

    }



    public static class ConvViewHolder extends RecyclerView.ViewHolder {

        TextView userMessageView,userNameView,userRegView;
        ImageView userOnlineView;
        CircleImageView userImageView;

        public ConvViewHolder(@NonNull View itemView) {
            super(itemView);

            userMessageView = itemView.findViewById(R.id.user_single_status);
            userNameView = itemView.findViewById(R.id.user_single_name);
            userImageView = itemView.findViewById(R.id.user_single_image);
            userOnlineView = itemView.findViewById(R.id.user_single_online_icon);
            userRegView = itemView.findViewById(R.id.user_single_reg);

        }

        public void setUserImage(final String thumb_image, final Context ctx){
            Picasso.with(ctx).load(thumb_image).networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.default_avatar).into(userImageView, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                    Picasso.with(ctx).load(thumb_image).placeholder(R.drawable.default_avatar).into(userImageView);

                }
            });
        }
        public void setMessage(String message, boolean isSeen){


            userMessageView.setText(message);
            if(!isSeen){
                userMessageView.setTypeface(userMessageView.getTypeface(), Typeface.BOLD);
            } else {
                userMessageView.setTypeface(userMessageView.getTypeface(), Typeface.NORMAL);
            }

        }

        public void setUserOnline(String online) {

            ImageView userOnlineView = itemView.findViewById(R.id.user_single_online_icon);

            if(online.equals("true")){

                userOnlineView.setVisibility(View.VISIBLE);

            } else {

                userOnlineView.setVisibility(View.INVISIBLE);

            }

        }


    }



}
