package com.tellme.kelvin.campconnect;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static java.security.AccessController.getContext;

public class SearchActivity extends AppCompatActivity {
    private View mView;

    private TextInputEditText mSearchField;
    private ImageButton mSearchBtn;
    private String user_id;
    private Spinner sState;
    private Spinner sYear;
    private Spinner sBatch;
    private Toolbar mToolbar;

    private RecyclerView mResultList;

    private DatabaseReference mUserDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mToolbar = findViewById(R.id.search_page_toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setTitle("Find a Corper");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUserDatabase = FirebaseDatabase.getInstance().getReference("Users");


        mSearchField = findViewById(R.id.search_field);
        mSearchBtn = findViewById(R.id.search_btn);
        sState = findViewById(R.id.search_spin_state);
        sYear = findViewById(R.id.search_spin_year);
        sBatch = findViewById(R.id.search_spin_batch);

        mResultList = findViewById(R.id.result_list);
        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(this));

        String[] state1 = {"", "Abia", "Adamawa", "Akwa Ibom", "Anambra", "Bauchi", "Bayelsa", "Benue", "Borno", "Cross River", "Delta", "Ebonyi", "Enugu", "Edo", "Ekiti", "Gombe", "Imo", "Jigawa", "Kaduna", "Kano", "Katsina", "Kebbi", "Kogi", "Kwara", "Lagos", "Nasarawa", "Niger", "Ogun", "Ondo", "Osun", "Oyo", "Plateau", "Rivers", "Sokoto", "Taraba", "Yobe", "Zamfara", "FCT Abuja"};
        String[] year1 = {"", "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030"};
        String[] batch1 = {"", "Batch A", "Batch B", "Batch C"};


        ArrayAdapter<String> adapter1 =
                new ArrayAdapter<String>(SearchActivity.this,
                        android.R.layout.simple_spinner_item, state1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sState.setAdapter(adapter1);

        ArrayAdapter<String> adapter2 =
                new ArrayAdapter<String>(SearchActivity.this,
                        android.R.layout.simple_spinner_item, batch1);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sBatch.setAdapter(adapter2);

        ArrayAdapter<String> adapter3 =
                new ArrayAdapter<String>(SearchActivity.this,
                        android.R.layout.simple_spinner_item, year1);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sYear.setAdapter(adapter3);

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String searchText = mSearchField.getText().toString();
                String state = sState.getSelectedItem().toString();
                String batch = sBatch.getSelectedItem().toString();
                String year = sYear.getSelectedItem().toString();
                String msearch = state + "/" + year + "/" + batch + "/" + searchText;

                firebaseUserSearch(msearch);

            }
        });


    }
    private void firebaseUserSearch(String msearch) {
        Query firebaseSearchQuery = mUserDatabase.orderByChild("search").equalTo(msearch);
        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>().setIndexedQuery(firebaseSearchQuery,mUserDatabase, User.class).build();


        FirebaseRecyclerAdapter<User, UsersViewHolder> adapter =
                new FirebaseRecyclerAdapter<User, UsersViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull UsersViewHolder holder, int position, @NonNull User model) {
                       final String list_user_id = getRef(position).getKey();
                       user_id = list_user_id;
                        holder.userName.setText(model.getName());
                        holder.userStatus.setText(model.getStatus());
                        holder.setImage(getApplicationContext(),model.getImage());



                    }

                    @NonNull
                    @Override
                    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_layout, viewGroup,false);
                        UsersViewHolder viewHolder = new UsersViewHolder(view);
                        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent profileIntent = new Intent(SearchActivity.this, ProfileActivity.class);

                                profileIntent.putExtra("user_id", user_id);
                                startActivity(profileIntent);
                            }
                        });
                        return viewHolder;
                    }
                };
        mResultList.setAdapter(adapter);
        adapter.startListening();

    }


    // View Holder Class

    public static class UsersViewHolder extends RecyclerView.ViewHolder {

        TextView userName, userStatus;
        CircleImageView profileImage;


        public UsersViewHolder(View itemView) {
            super(itemView);

           userName = itemView.findViewById(R.id.name_text);
           userStatus = itemView.findViewById(R.id.status_text);
           profileImage = itemView.findViewById(R.id.profile_image);


        }

        public void setImage(final Context ctx, final String userImage){

            final ImageView user_image = itemView.findViewById(R.id.profile_image);


            Glide.with(ctx).load(userImage).into(user_image);
            if(!user_image.equals("default")) {

                //Picasso.with(SettingsActivity.this).load(image).placeholder(R.drawable.default_avatar).into(mDisplayImage);

                Picasso.with(ctx).load(userImage).networkPolicy(NetworkPolicy.OFFLINE)
                        .placeholder(R.drawable.default_avatar).into(user_image, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                        Picasso.with(ctx).load(userImage).placeholder(R.drawable.default_avatar).into(user_image);

                    }
                });

            }


        }




    }

}
