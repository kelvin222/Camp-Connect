package com.tellme.kelvin.campconnect;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatRoomActivity extends AppCompatActivity {
    private String mPlatoon,mState,mBatch,mName,mRegno;
    private Toolbar mainToolbar;

    private DatabaseReference mCRootRef;

    private TextView mTitleView;
    private TextView mLastSeenView;
    private CircleImageView mProfileImage;
    private CircleImageView mOptionsend;
    private CircleImageView mOptionreceive;
    private FirebaseAuth mAuth;
    private String mCurrentUserId;


    private List<Chatroom> mChatroomList;
    private ImageButton mChatAddBtn;
    private ImageButton mChatroomSendBtn;
    private EditText mChatroomMessageView;

    private RecyclerView mChatroomsList;
    private SwipeRefreshLayout mRefreshLayout;

    private final List<Chatroom> ChatroomsList = new ArrayList<>();
    private LinearLayoutManager mLinearLayout;
    private ChatroomAdapter mAdapter;

    private String saveCurrentTime, saveCurrentDate;

    private static final int TOTAL_ITEMS_TO_LOAD = 10;
    private int mCurrentPage = 1;

    private static final int GALLERY_PICK = 1;

    // Storage Firebase
    private StorageReference mImageStorage;


    //New Solution
    private int itemPos = 0;

    private String mLastKey = "";
    private String mPrevKey = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        mainToolbar = (Toolbar) findViewById(R.id.cRoom_toolbar);
        setSupportActionBar(mainToolbar);
        mPlatoon = getIntent().getStringExtra("platoon");
        mState = getIntent().getStringExtra("state");
        mBatch = getIntent().getStringExtra("batch");
        mName = getIntent().getStringExtra("name");
        mRegno = getIntent().getStringExtra("regno");
        mainToolbar = (Toolbar) findViewById(R.id.cRoom_toolbar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setTitle("Platoon "+mPlatoon+" Chatroom");

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        mCRootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = mAuth.getCurrentUser().getUid();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(calendar.getTime());


        // ---- Custom Action bar Items ----


        mChatroomSendBtn = findViewById(R.id.cRoom_send_btn);
        mChatroomMessageView = findViewById(R.id.cRoom_message_view);

        mAdapter = new ChatroomAdapter(ChatroomsList);

        mChatroomsList = findViewById(R.id.cRoom_list);
        mRefreshLayout = findViewById(R.id.cRoom_swipe_layout);
        mLinearLayout = new LinearLayoutManager(this);

        mChatroomsList.setHasFixedSize(true);
        mChatroomsList.setLayoutManager(mLinearLayout);

        mChatroomsList.setAdapter(mAdapter);


        //------- IMAGE STORAGE ---------
        mImageStorage = FirebaseStorage.getInstance().getReference();


        loadMessages();









        mChatroomSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendMessage();

            }
        });




        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mCurrentPage++;

                itemPos = 0;

                loadMoreMessages();


            }
        });


    }

    private void loadMoreMessages() {

        DatabaseReference messageRef = mCRootRef.child("chatroom").child(mState).child(mBatch);

        Query messageQuery = messageRef.orderByKey().endAt(mLastKey).limitToLast(10);

        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                Chatroom chatroom = dataSnapshot.getValue(Chatroom.class);
                String messageKey = dataSnapshot.getKey();

                if(!mPrevKey.equals(messageKey)){

                    ChatroomsList.add(itemPos++, chatroom);

                } else {

                    mPrevKey = mLastKey;

                }


                if(itemPos == 1) {

                    mLastKey = messageKey;

                }


                Log.d("TOTALKEYS", "Last Key : " + mLastKey + " | Prev Key : " + mPrevKey + " | Message Key : " + messageKey);

                mAdapter.notifyDataSetChanged();

                mRefreshLayout.setRefreshing(false);

                mLinearLayout.scrollToPositionWithOffset(10, 0);

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

    }

    private void loadMessages() {

        DatabaseReference messageRef = mCRootRef.child("chatroom").child(mState).child(mBatch);
        messageRef.keepSynced(true);

        Query messageQuery = messageRef.limitToLast(mCurrentPage * TOTAL_ITEMS_TO_LOAD);
        messageQuery.keepSynced(true);


        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Chatroom chatroom = dataSnapshot.getValue(Chatroom.class);

                itemPos++;

                if(itemPos == 1){

                    String messageKey = dataSnapshot.getKey();

                    mLastKey = messageKey;
                    mPrevKey = messageKey;

                }

                ChatroomsList.add(chatroom);
                mAdapter.notifyDataSetChanged();

                mChatroomsList.scrollToPosition(ChatroomsList.size() - 1);

                mRefreshLayout.setRefreshing(false);

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

    }

    private void sendMessage() {


        String message = mChatroomMessageView.getText().toString();

        if(!TextUtils.isEmpty(message)){

            String room_ref = "chatroom/" + mState + "/" + mBatch;

            DatabaseReference user_message_push = mCRootRef.child("chatroom").child(mState).child(mBatch).push();
            String messagePushID = user_message_push.getKey();

            String push_id = user_message_push.getKey();

            Map messageRoomMap = new HashMap();
            messageRoomMap.put("message", message);
            messageRoomMap.put("type", "text");
            messageRoomMap.put("mtime", saveCurrentTime);
            messageRoomMap.put("mdate", saveCurrentDate);
            messageRoomMap.put("from", mCurrentUserId);
            messageRoomMap.put("name", mName);
            messageRoomMap.put("regno", mRegno);
            messageRoomMap.put("state", mState);
            messageRoomMap.put("batch", mBatch);
            messageRoomMap.put("messageID", messagePushID);
            messageRoomMap.put("time", ServerValue.TIMESTAMP);

            Map messageUserMap = new HashMap();
            messageUserMap.put(room_ref + "/" + push_id, messageRoomMap);

            mChatroomMessageView.setText("");


            mCRootRef.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                    if(databaseError != null){

                        Log.d("CHAT_LOG", databaseError.getMessage().toString());

                    }

                }
            });

        }

    }

}
