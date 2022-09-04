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

public class FedViewActivity extends AppCompatActivity {


    private String head;
    private String time,date;
    private String office;
    private String body;
    private String state;


    private Toolbar mainToolbar;
    private TextView fHead;
    private TextView fOffice;
    private TextView fBody;

    private TextView fDate;
    private TextView fTime;
    private Button fAck;
    private AdView mAdView;

    private static final String TAG = "FedViewActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fed_view);

        MobileAds.initialize(this, "ca-app-pub-4481331290406181~7584090098");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("D26ED472E92D50E4CE3429D7249BA534").build();
        mAdView.loadAd(adRequest);

        mainToolbar = (Toolbar) findViewById(R.id.fedview_toolbar);
        setSupportActionBar(mainToolbar);
        head = getIntent().getStringExtra("head");
        office = getIntent().getStringExtra("office");
        body = getIntent().getStringExtra("body");
        date = getIntent().getStringExtra("date");
        time = getIntent().getStringExtra("time");

        getSupportActionBar().setTitle("NYSC HQ Update");
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        fBody = (TextView) findViewById(R.id.fedbody);
        fHead = (TextView) findViewById(R.id.fedhead);
        fOffice = (TextView) findViewById(R.id.fedoffice);
        fDate = (TextView) findViewById(R.id.feddate);
        fTime = (TextView) findViewById(R.id.fedtime);
        fAck = (Button) findViewById(R.id.sourceBtn);
        fHead.setText(head);
        fBody.setText(body);
        fOffice.setText(office);
        fDate.setText(date);
        fTime.setText(time);

        fAck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(FedViewActivity.this, FederalActivity.class);
                startActivity(startIntent);
                finish();
            }
        });
    }
}
