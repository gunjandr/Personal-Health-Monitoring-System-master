package com.example.dell_pc.health_first;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity{
    //private DrawerLayout mDrawerLayout;
    //private ActionBarDrawerToggle mToggle;
    Button b1, b2, b3, b4, b5, b6,b7;

//START: THIS LOGIC BELONGS TO HAMEDS CODE : SETTING.JAVA
    private static final int SETTINGS_RESULT = 1;

    private DatabaseReference databaseReference;
    Map<String, Object> taskMap = new HashMap<String, Object>();
    List<settingFields> settingList;
    Map<String, Object> settingMap = new HashMap<String, Object>();
//END: THIS LOGIC BELONGS TO HAMEDS CODE : SETTING.JAVA

    private static final String TAG = "EmailPassword";

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;
    // [END declare_auth_listener]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        b1 = (Button) findViewById(R.id.activity_view_profile);
        b2 = (Button) findViewById(R.id.buttonvitals);
        b3 = (Button) findViewById(R.id.buttondiet);
        b4 = (Button) findViewById(R.id.buttonmedication);
        b5 = (Button) findViewById(R.id.buttonnotepad);
        b6 = (Button) findViewById(R.id.buttonsearch);
        b7 = (Button) findViewById(R.id.settings);



        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // [START_EXCLUDE]
                //  updateUI(user);
                // [END_EXCLUDE]

            }
        };
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(HomeActivity.this, ViewProfileActivity.class);
                startActivity(in);

            }
        });
                b2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent in = new Intent(HomeActivity.this, vitalSigns.class);
                        startActivity(in);
                    }
                });
                b3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                            Intent in = new Intent(HomeActivity.this, Diet.class);
                            startActivity(in);

                    }
                });
                b4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                            Intent in = new Intent(HomeActivity.this, Medication.class);
                            startActivity(in);

                    }
                });
                b5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                            Intent in = new Intent(HomeActivity.this, Monitoring.class);
                            startActivity(in);

                    }
                });
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(HomeActivity.this, search.class);
                startActivity(in);

            }
        });




//START: THIS LOGIC BELONGS TO HAMEDS CODE : SETTING.JAVA


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();//get the current user UID

            databaseReference = FirebaseDatabase.getInstance().getReference().child(uid).child("settings");
           // databaseReference = FirebaseDatabase.getInstance().getReference().child(uid);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                   /* GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {};
                    List messages = dataSnapshot.getValue(t);*/

                    GenericTypeIndicator<Map<String, Object>> t = new GenericTypeIndicator<Map<String, Object>>() {
                    };
                    // Map messages = dataSnapshot.getChildren();

                    Iterator<DataSnapshot> itr = dataSnapshot.getChildren().iterator();

                    while (itr.hasNext()) {
                        String key = itr.next().getKey();

                        String tempenable =  dataSnapshot.child(key).child("enable").getValue()+"";
                        String tempphone = dataSnapshot.child(key).child("phone").getValue() + "";
                        String tempemail = dataSnapshot.child(key).child("email").getValue() + "";
                        String tempmethod = dataSnapshot.child(key).child("method").getValue() + "";

                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }

            });
           // Intent i = new Intent(Home.this, userSettingActivity.class);
            // startActivity(i);
           // startActivityForResult(i, SETTINGS_RESULT);
        }

  /*  settingButton=(Button) findViewById(R.id.buttonSettings);
        settingButton.setOnClickListener(new View.OnClickListener()

    {

        public void onClick (View v){
        // TODO Auto-generated method stub
        Intent i = new Intent(Setting.this, userSettingActivity.class);
        // startActivity(i);
        startActivityForResult(i, SETTINGS_RESULT);
    }
    });*/

        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(HomeActivity.this, userSettingActivity.class);
                startActivityForResult(in,SETTINGS_RESULT);

            }
        });
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==SETTINGS_RESULT)
        {
            displayUserSettings();
        }

    }

    private void displayUserSettings()
    {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String  phone = "";
        String  email = "";
        int  enable= 0 ;
        boolean enablebool=false;
        String  method = "";

        phone=sharedPrefs.getString("prefUserPhone", "NoPhone");
        enablebool = sharedPrefs.getBoolean("enable", false);
        if(enablebool)
        {
            enable=1;
        }
        else
        {
            enable=0;
        }
        email= sharedPrefs.getString("prefUserEmail", "NoEmail");

        method=sharedPrefs.getString("prefUpdate", "NOUPDATE");

        TextView textViewSetting = (TextView) findViewById(R.id.textViewSettings);
        TextView textViewSetting1 = (TextView) findViewById(R.id.textViewSettings1);

        // textViewSetting.setText(Phone);
        // textViewSetting1.setText(Email);
        saveSettingInfo(enable,phone,email,method);
    }


    public void saveSettingInfo(int enable, String phone, String email, String method)
    {



        settingFields field = new settingFields(enable,phone,email,method);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {




            String uid = user.getUid();//get the current user UID

            databaseReference.setValue(field);

            Toast.makeText(this, " Settings Updated Successfully", Toast.LENGTH_SHORT).show();


        }

    }
//END: THIS LOGIC BELONGS TO HAMEDS CODE : SETTING.JAVA



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_popupmenu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.logout:
                Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT)
                        .show();
                mAuth.signOut();
                Intent in = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(in);



                break;
            default:
                break;
        }

        return true;
    }

        }


      /*  mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        //       and use variable actionBar instead
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

  //      getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        b2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent in=new Intent(HomeActivity.this,vitalSigns.class);
                startActivity(in);
            }

        });
          }

    @Override
    public boolean onOptionsItemSelected(MenuItem Item) {
        if (mToggle.onOptionsItemSelected(Item)) {
            return true;
        }
        return super.onOptionsItemSelected(Item);

    }

*/




                          /* Intent in=new Intent(HomeActivity.this,ViewProfileActivity.class);
                           startActivity(in);*/






