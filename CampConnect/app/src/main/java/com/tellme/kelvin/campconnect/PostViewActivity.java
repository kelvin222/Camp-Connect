package com.tellme.kelvin.campconnect;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class PostViewActivity extends AppCompatActivity {

    private String head;
    private String time,date;
    private String office;
    private String body;
    private String state;


    private Toolbar mainToolbar;
    private TextView mHead;
    private TextView mOffice;
    private TextView mBody;

    private TextView mDate;
    private TextView mTime;
    private Button mAck;
    private AdView mAdView;

    private static final String TAG = "PostViewActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_view);

        MobileAds.initialize(this, "ca-app-pub-4481331290406181~7584090098");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("D26ED472E92D50E4CE3429D7249BA534").build();
        mAdView.loadAd(adRequest);

        mainToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);
        head = getIntent().getStringExtra("head");
        office = getIntent().getStringExtra("office");
        body = getIntent().getStringExtra("body");
        date = getIntent().getStringExtra("date");
        time = getIntent().getStringExtra("time");
        state = getIntent().getStringExtra("state");

        getSupportActionBar().setTitle(office + " Update");
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        mBody = (TextView) findViewById(R.id.postbody);
        mHead = (TextView) findViewById(R.id.posthead);
        mOffice = (TextView) findViewById(R.id.postoffice);
        mDate = (TextView) findViewById(R.id.postdate);
        mTime = (TextView) findViewById(R.id.posttime);
        mAck = (Button) findViewById(R.id.sourceBtn);
        mHead.setText(head);
        mBody.setText(body);
        mOffice.setText(office);
        mDate.setText(date);
        mTime.setText(time);

        mAck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(PostViewActivity.this, MainActivity.class);
                startActivity(startIntent);
                finish();
            }
        });
    }
}
