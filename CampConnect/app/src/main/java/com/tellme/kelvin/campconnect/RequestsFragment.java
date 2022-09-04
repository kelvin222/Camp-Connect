package com.tellme.kelvin.campconnect;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class RequestsFragment extends Fragment {

    private RecyclerView mRequestsList;

    private DatabaseReference mRequestsDatabase;
    private DatabaseReference mUsersDatabase;

    private FirebaseAuth mAuth;
    private String user_id;
    private String mCurrent_user_id;

    private View mMainView;


    public RequestsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView = inflater.inflate(R.layout.fragment_requests, container, false);

        mRequestsList = mMainView.findViewById(R.id.request_list);
        mAuth = FirebaseAuth.getInstance();

        mCurrent_user_id = mAuth.getCurrentUser().getUid();

        mRequestsDatabase = FirebaseDatabase.getInstance().getReference().child("Friend_req").child(mCurrent_user_id);
        mRequestsDatabase.keepSynced(true);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mUsersDatabase.keepSynced(true);


        mRequestsList.setHasFixedSize(true);
        mRequestsList.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inflate the layout for this fragment
        return mMainView;
    }






    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Requests> options =
                new FirebaseRecyclerOptions.Builder<Requests>()
                        .setQuery(mRequestsDatabase, Requests.class)
                        .build();
        FirebaseRecyclerAdapter<Requests, RequestsViewHolder> adapter =
                new FirebaseRecyclerAdapter<Requests, RequestsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(final RequestsViewHolder holder, final int position, @NonNull Requests model) {

                        final String list_user_id = getRef(position).getKey();
                        user_id = list_user_id;

                        DatabaseReference getTypeRef = getRef(position).child("request_type").getRef();
                        holder.itemView.setTag(position);
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int position = (int) view.getTag();
                                String Requestee = getRef(position).getKey();
                                Intent proIntent = new Intent(getContext(), ProfileActivity.class);
                                proIntent.putExtra("user_id", Requestee);
                                startActivity(proIntent);


                            }
                        });
                        getTypeRef.addValueEventListener(new ValueEventListener() {
                          @Override
                           public void onDataChange(DataSnapshot dataSnapshot) {
                              if (dataSnapshot.exists()){
                                  final String type = dataSnapshot.getValue().toString();

                                  mUsersDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                                      @Override
                                      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                          if (dataSnapshot.hasChild("name")){
                                              final String requestUserName = dataSnapshot.child("name").getValue().toString();
                                              final String requestProfileImage = dataSnapshot.child("image").getValue().toString();
                                              final String requestProfileOnline = dataSnapshot.child("online").getValue().toString();
                                              final String requestUserReg = dataSnapshot.child("regno").getValue().toString();
                                              holder.userRequestStatus.setText(type);
                                              holder.userName.setText(requestUserName);
                                              holder.userReg.setText(requestUserReg);
                                              holder.setUserImage(requestProfileImage, getContext());


                                          }
                                          }

                                          @Override
                                          public void onCancelled(DatabaseError databaseError) {

                                          }


                                          });


                                  }
                                  }

                                  @Override
                                  public void onCancelled(DatabaseError databaseError) {

                              }
                              }
                              );


                    }

                    @Override
                    public RequestsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_single_layout, viewGroup, false);

                        return new RequestsViewHolder(view);
                    }
                };




        mRequestsList.setAdapter(adapter);
        adapter.startListening();

    }

    public static class RequestsViewHolder extends RecyclerView.ViewHolder {

        TextView userName,userRequestStatus, userReg, onlineProfile;
        CircleImageView profileImage;


        public RequestsViewHolder(View itemView){
            super(itemView);


            userName = itemView.findViewById(R.id.user_single_name);
            userRequestStatus = itemView.findViewById(R.id.user_single_status);
            userReg = itemView.findViewById(R.id.user_single_reg);
            profileImage = itemView.findViewById(R.id.user_single_image);




        }
        public void setUserImage(String thumb_image, Context ctx){

            profileImage = itemView.findViewById(R.id.user_single_image);
            Picasso.with(ctx).load(thumb_image).placeholder(R.drawable.default_avatar).into(profileImage);

        }



    }


}