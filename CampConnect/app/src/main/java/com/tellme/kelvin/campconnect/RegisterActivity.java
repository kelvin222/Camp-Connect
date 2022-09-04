package com.tellme.kelvin.campconnect;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import android.app.ProgressDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private TextInputLayout mDisplayName;
    private TextInputLayout mEmail;
    private TextInputLayout mPassword;
    private TextInputLayout mPhone;
    private Spinner mLevel;
    private Spinner mState;
    private Spinner mYear;
    private Spinner mStream;
    private Spinner mBatch;
    private TextInputLayout mRegNumber;
    private Button mCreateBtn;
    private FirebaseAuth mAuth;
    private Toolbar mToolbar;

    private DatabaseReference mDatabase;

    //ProgressDialog
    private ProgressDialog mRegProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        //Toolbar Set
        mToolbar = (Toolbar) findViewById(R.id.register_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mRegProgress = new ProgressDialog(this);



        mAuth = FirebaseAuth.getInstance();

        mDisplayName = (TextInputLayout) findViewById(R.id.reg_display_name);
        mEmail = (TextInputLayout) findViewById(R.id.reg_email);
        mPassword = (TextInputLayout) findViewById(R.id.reg_password);
        mPassword = (TextInputLayout) findViewById(R.id.reg_password);
        mPhone = (TextInputLayout) findViewById(R.id.reg_phone);
        mLevel = (Spinner) findViewById(R.id.reg_spin_level);
        mState = (Spinner) findViewById(R.id.reg_spin_state);
        mYear = (Spinner) findViewById(R.id.reg_spin_year);
        mStream = (Spinner) findViewById(R.id.reg_spin_stream);
        mBatch = (Spinner) findViewById(R.id.reg_spin_batch);
        mRegNumber = (TextInputLayout) findViewById(R.id.reg_number);
        mCreateBtn = (Button) findViewById(R.id.reg_create_btn);

        String[] state1 = {"", "Abia", "Adamawa", "Akwa Ibom", "Anambra", "Bauchi", "Bayelsa", "Benue", "Borno", "Cross River", "Delta", "Ebonyi", "Enugu", "Edo", "Ekiti", "Gombe", "Imo", "Jigawa", "Kaduna", "Kano", "Katsina", "Kebbi", "Kogi", "Kwara", "Lagos", "Nasarawa", "Niger", "Ogun", "Ondo", "Osun", "Oyo", "Plateau", "Rivers", "Sokoto", "Taraba", "Yobe", "Zamfara", "FCT Abuja"};
        String[] year1 = {"", "2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030"};
        String[] batch1 = {"", "Batch A", "Batch B", "Batch C"};
        String[] stream1 = {"", "Stream 1", "Stream 2", "Stream 3"};
        String[] level1 = {"", "Camp", "State"};


        ArrayAdapter<String> adapter1 =
                new ArrayAdapter<String>(RegisterActivity.this,
                        android.R.layout.simple_spinner_item, state1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mState.setAdapter(adapter1);

        ArrayAdapter<String> adapter4 =
                new ArrayAdapter<String>(RegisterActivity.this,
                        android.R.layout.simple_spinner_item, level1);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLevel.setAdapter(adapter4);

        ArrayAdapter<String> adapter5 =
                new ArrayAdapter<String>(RegisterActivity.this,
                        android.R.layout.simple_spinner_item, stream1);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mStream.setAdapter(adapter5);

        ArrayAdapter<String> adapter2 =
                new ArrayAdapter<String>(RegisterActivity.this,
                        android.R.layout.simple_spinner_item, batch1);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBatch.setAdapter(adapter2);

        ArrayAdapter<String> adapter3 =
                new ArrayAdapter<String>(RegisterActivity.this,
                        android.R.layout.simple_spinner_item, year1);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mYear.setAdapter(adapter3);

        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String display_name = mDisplayName.getEditText().getText().toString();
                String email = mEmail.getEditText().getText().toString();
                String password = mPassword.getEditText().getText().toString();
                String phone = mPhone.getEditText().getText().toString();
                String level = mLevel.getSelectedItem().toString();
                String state = mState.getSelectedItem().toString();
                String batches = mBatch.getSelectedItem().toString();
                String year = mYear.getSelectedItem().toString();
                String stream = mStream.getSelectedItem().toString();
                String regnumber = mRegNumber.getEditText().getText().toString();
                String batch = batches+"/"+stream;
                String search = state+"/"+year+"/"+batches+"/"+regnumber;

                if(!TextUtils.isEmpty(display_name) || !TextUtils.isEmpty(email) || !TextUtils.isEmpty(password) ||  !TextUtils.isEmpty(phone) || !TextUtils.isEmpty(level) ||  !TextUtils.isEmpty(state) || !TextUtils.isEmpty(batches) || !TextUtils.isEmpty(year) || !TextUtils.isEmpty(stream) ||  !TextUtils.isEmpty(regnumber)){

                    mRegProgress.setTitle("Registering User");
                    mRegProgress.setMessage("Please wait while we create your account !");
                    mRegProgress.setCanceledOnTouchOutside(false);
                    mRegProgress.show();
                    register_user(display_name,email,password,phone,level,state,batch,year,regnumber,search);
                }

            }
        });

    }



    private void register_user(final String display_name, String email, String password, final String phone, final String level, final String state, final String batch, final String year, final String regnumber, final String search) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){


                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = current_user.getUid();

                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                    String device_token = FirebaseInstanceId.getInstance().getToken();

                    HashMap<String, String> userMap = new HashMap<>();
                    userMap.put("name", display_name);
                    userMap.put("level", level);
                    userMap.put("state", state);
                    userMap.put("batch", batch);
                    userMap.put("year", year);
                    userMap.put("regno", regnumber);
                    userMap.put("search", search);
                    userMap.put("phone", phone);
                    userMap.put("status", "am Using NYSC Connect");
                    userMap.put("image", "default");
                    userMap.put("thumb_image", "default");
                    userMap.put("device_token", device_token);

                    mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){

                                mRegProgress.dismiss();

                                Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mainIntent);
                                finish();

                            }

                        }
                    });


                } else {

                    mRegProgress.hide();
                    Toast.makeText(RegisterActivity.this, "Cannot Sign in. Please check the form and try again.", Toast.LENGTH_LONG).show();

                }

            }
        });

    }
}

