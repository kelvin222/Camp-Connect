package com.tellme.kelvin.campconnect;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class FederalActivity extends AppCompatActivity {
    private RecyclerView mFederalList;

    private DatabaseReference mFederalDatabase;
    private DatabaseReference mMessageDatabase;
    private DatabaseReference mUsersDatabase;

    private String fed_office;
    private String fed_date;
    private String fed_time;
    private String fed_head;
    private String fed_body;
    private String fed_ruser;
    private String fed_state;
    private String state;
    private String level;
    private String fedpost_id;
    private FirebaseAuth mAuth;
    private Toolbar mainToolbar;
    private String mCurrent_user_id;
    private LinearLayoutManager mLinearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_federal);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            mCurrent_user_id = mAuth.getCurrentUser().getUid();
        }
        mainToolbar = (Toolbar) findViewById(R.id.fed_toolbar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setTitle("NYSC HQ FEEDS");
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        mFederalList = (RecyclerView) findViewById(R.id.fed_list);
        mLinearLayout = new LinearLayoutManager(this);
        mLinearLayout.setReverseLayout(true);
        mLinearLayout.setStackFromEnd(true);

        mFederalList.setHasFixedSize(true);
        mFederalList.setLayoutManager(mLinearLayout);



    }


    @Override
    public void onStart() {
        super.onStart();

        mFederalDatabase = FirebaseDatabase.getInstance().getReference().child("Federal");
        mFederalDatabase.keepSynced(true);

        Query conversationQuery = mFederalDatabase.orderByChild("timestamp").limitToLast(25);
        FirebaseRecyclerOptions<Federal> options =
                new FirebaseRecyclerOptions.Builder<Federal>()
                        .setIndexedQuery(conversationQuery,mFederalDatabase, Federal.class)
                        .build();

        FirebaseRecyclerAdapter<Federal, FederalActivity.FederalViewHolder> adapter = new FirebaseRecyclerAdapter<Federal, FederalActivity.FederalViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final FederalActivity.FederalViewHolder holder, int i, @NonNull final Federal model) {


                final String camp_id = getRef(i).getKey();
                fed_body = model.getBody();
                fed_date = model.getDate();
                fed_head = model.getHead();
                fed_office = model.getOffice();
                fed_ruser = model.getUser();
                fed_time = model.getTime();
                holder.cHead.setText(model.getHead());
                holder.cOffice.setText(model.getOffice());
                holder.cDate.setText(model.getDate());
                fedpost_id = camp_id;
                holder.itemView.setTag(i);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent campIntent = new Intent(FederalActivity.this, FedViewActivity.class);
                        campIntent.putExtra("office", model.getOffice());
                        campIntent.putExtra("date", model.getDate());
                        campIntent.putExtra("time", model.getTime());
                        campIntent.putExtra("head", model.getHead());
                        campIntent.putExtra("body", model.getBody());
                        campIntent.putExtra("ruser", model.getUser());
                        startActivity(campIntent);



                    }
                });


            }
            @NonNull
            @Override
            public FederalActivity.FederalViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adpost_list, viewGroup, false);
                FederalActivity.FederalViewHolder viewHolder = new FederalActivity.FederalViewHolder(view);


                return viewHolder;
            }
        };

        mFederalList.setAdapter(adapter);
        adapter.startListening();




    }

    public static class FederalViewHolder extends RecyclerView.ViewHolder {

        TextView cOffice;
        TextView cHead;
        TextView cDate;
        ImageView cImage;
        ImageView cRed;
        ImageView cGreen;
        ImageView cDelete;
        CardView cCardView;

        public FederalViewHolder(@NonNull View itemView) {
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
