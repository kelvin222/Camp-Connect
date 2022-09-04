package com.tellme.kelvin.campconnect;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class FeedsActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private TextInputLayout mHead;
    private TextInputLayout mBody;
    private Button mPostbtn;

    private DatabaseReference mFeedDatabase;
    private FirebaseUser mCurrentUser;

    private String saveCurrentTime, saveCurrentDate, state, level, name, image;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeds);
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String current = mCurrentUser.getUid();

        mFeedDatabase = FirebaseDatabase.getInstance().getReference();

        level = getIntent().getStringExtra("level");
        state = getIntent().getStringExtra("state");
        image = getIntent().getStringExtra("image");
        name = getIntent().getStringExtra("name");
        mToolbar = findViewById(R.id.feed_appBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Report an Emergency");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProgress = new ProgressDialog(this);

        mHead = findViewById(R.id.feed_head);
        mBody = findViewById(R.id.feed_body);
        mPostbtn = findViewById(R.id.feed_btn);
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        mPostbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String head = mHead.getEditText().getText().toString();
                String body = mBody.getEditText().getText().toString();

                if(!TextUtils.isEmpty(head) || !TextUtils.isEmpty(body)){

                    mProgress.setTitle("Connecting...");
                    mProgress.setMessage("Please wait, Sending the Report !");
                    mProgress.setCanceledOnTouchOutside(false);
                    mProgress.show();
                    noticePost(head,body,current);
                }

            }
        });
    }
    private void noticePost(final String head, String body,String current) {

        String current_notice_ref = "Emergency/";
        DatabaseReference notice_push = mFeedDatabase.child("Emergency").push();
        String noticePushID = notice_push.getKey();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Emergency").child(level).child(state).child(noticePushID);

        String push_id = notice_push.getKey();

        Map noticeMap = new HashMap();

        noticeMap.put("head", head);
        noticeMap.put("body", body);
        noticeMap.put("name", name);
        noticeMap.put("image", image);
        noticeMap.put("user", current);
        noticeMap.put("time", saveCurrentTime);
        noticeMap.put("date", saveCurrentDate);
        noticeMap.put("feedid", noticePushID);

        Map noticeUserMap = new HashMap();
        noticeUserMap.put(current_notice_ref + noticePushID, noticeMap);

        mDatabase.setValue(noticeMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    mProgress.dismiss();

                    Intent mainIntent = new Intent(FeedsActivity.this, MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                    finish();

                }

            }
        });





    }
}
