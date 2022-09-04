package com.tellme.kelvin.campconnect;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class CampFragment extends Fragment {
    private RecyclerView mCampList;

    private DatabaseReference mCampDatabase;
    private DatabaseReference mMessageDatabase;
    private DatabaseReference mUsersDatabase;

    private String post_office;
    private String post_date;
    private String post_time;
    private String post_head;
    private String post_body;
    private String post_ruser;
    private String post_state;
    private String state;
    private String level;
    private String camppost_id;
    private FirebaseAuth mAuth;

    private String mCurrent_user_id;

    private View mMainView;

    private LinearLayoutManager mLinearLayout;

    public CampFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView = inflater.inflate(R.layout.fragment_camp, container, false);


        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            mCurrent_user_id = mAuth.getCurrentUser().getUid();
        }

        mCampList = (RecyclerView) mMainView.findViewById(R.id.camp_list);
        mLinearLayout = new LinearLayoutManager(getContext());
        mLinearLayout.setReverseLayout(true);
        mLinearLayout.setStackFromEnd(true);

        mCampList.setHasFixedSize(true);
        mCampList.setLayoutManager(mLinearLayout);


        // Inflate the layout for this fragment
        return mMainView;
    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid());
        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String State = dataSnapshot.child("state").getValue().toString();

        mCampDatabase = FirebaseDatabase.getInstance().getReference().child("Camp").child(State);
                mCampDatabase.keepSynced(true);


                Query conversationQuery = mCampDatabase.orderByChild("timestamp").limitToLast(25);
                FirebaseRecyclerOptions<Camp> options =
                        new FirebaseRecyclerOptions.Builder<Camp>()
                                .setIndexedQuery(conversationQuery,mCampDatabase, Camp.class)
                                .build();

                FirebaseRecyclerAdapter<Camp, CampFragment.CampViewHolder> adapter = new FirebaseRecyclerAdapter<Camp, CampFragment.CampViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final CampFragment.CampViewHolder holder, int i, @NonNull final Camp model) {


                        final String camp_id = getRef(i).getKey();
                        post_body = model.getBody();
                        post_date = model.getDate();
                        post_head = model.getHead();
                        post_office = model.getOffice();
                        post_ruser = model.getUser();
                        post_state = model.getState();
                        post_time = model.getTime();
                        holder.cHead.setText(model.getHead());
                        holder.cOffice.setText(model.getOffice());
                        holder.cDate.setText(model.getDate());
                        camppost_id = camp_id;
                        holder.itemView.setTag(i);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent campIntent = new Intent(getContext(), PostViewActivity.class);
                                campIntent.putExtra("office", model.getOffice());
                                campIntent.putExtra("date", model.getDate());
                                campIntent.putExtra("time", model.getTime());
                                campIntent.putExtra("head", model.getHead());
                                campIntent.putExtra("body", model.getBody());
                                campIntent.putExtra("ruser", model.getUser());
                                campIntent.putExtra("state", model.getState());
                                startActivity(campIntent);



                            }
                        });


                    }
                    @NonNull
                    @Override
                    public CampFragment.CampViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adpost_list, viewGroup, false);
                        CampFragment.CampViewHolder viewHolder = new CampFragment.CampViewHolder(view);


                        return viewHolder;
                    }
                };

                mCampList.setAdapter(adapter);
                adapter.startListening();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public static class CampViewHolder extends RecyclerView.ViewHolder {

         TextView cOffice;
         TextView cHead;
         TextView cDate;
         ImageView cImage;
         ImageView cRed;
         ImageView cGreen;
         ImageView cDelete;
         CardView cCardView;

        public CampViewHolder(@NonNull View itemView) {
            super(itemView);

            cCardView = (CardView) itemView.findViewById(R.id.main_blog_post);
            cOffice = (TextView) itemView.findViewById(R.id.office);
            cHead = (TextView) itemView.findViewById(R.id.heading);
            cDate = (TextView) itemView.findViewById(R.id.postdate);
            cImage = (ImageView) itemView.findViewById(R.id.c_image);
            cGreen = (ImageView) itemView.findViewById(R.id.imageGreen);
            cRed = (ImageView) itemView.findViewById(R.id.imageRed);

        }


    }



}
