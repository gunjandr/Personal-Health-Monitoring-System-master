package com.example.dell_pc.health_first;


        import android.app.Activity;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.preference.PreferenceActivity;
        import android.preference.PreferenceManager;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.GenericTypeIndicator;
        import com.google.firebase.database.ValueEventListener;
        import com.google.firebase.auth.FirebaseAuth;

        import java.util.HashMap;
        import java.util.Iterator;
        import java.util.List;
        import java.util.Map;

public class Setting extends MainActivity {
    Button settingButton;
    private static final int SETTINGS_RESULT = 1;

    private DatabaseReference databaseReference;
    Map<String, Object> taskMap = new HashMap<String, Object>();
    List<settingFields> settingList;
    Map<String, Object> settingMap = new HashMap<String, Object>();
    boolean tempenable =false;
    String tempphone = "";
    String tempemail ="";
    String tempmethod = "SMS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
     FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();//get the current user UID

           // databaseReference = FirebaseDatabase.getInstance().getReference().child(uid).child("settings");
            databaseReference = FirebaseDatabase.getInstance().getReference().child(uid);
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
                      //  String key = itr.next().getKey();
                        tempenable = (boolean) dataSnapshot.child("enable").getValue();
                        tempphone = dataSnapshot.child("phone").getValue() + "";
                        tempemail = dataSnapshot.child("email").getValue() + "";
                        tempmethod = dataSnapshot.child("method").getValue() + "";

                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }

            });
           Intent i = new Intent(Setting.this, userSettingActivity.class);
            // startActivity(i);
            startActivityForResult(i, SETTINGS_RESULT);
           // Toast.makeText(this,"uid while saving data is "+uid,Toast.LENGTH_SHORT).show();
            System.out.println("tempenable"+tempenable);
            System.out.println("tempphone"+tempphone);
            System.out.println("tempemail"+tempemail);
            System.out.println("tempmethod"+tempmethod);



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
        int  enable ;
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


        Toast.makeText(this,"Save info called",Toast.LENGTH_SHORT).show();
        settingFields field = new settingFields(enable,phone,email,method);
        //foodList.add(field);

        // foodMap.put("","")


        settingMap.put("Enable",enable);
        settingMap.put("Phone",phone);
        settingMap.put("Email",email);
        settingMap.put("Method",method);



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {




            String uid = user.getUid();//get the current user UID
            Toast.makeText(this,"uid while saving data is "+uid,Toast.LENGTH_SHORT).show();
            //databaseReference.updateChildren(dietvals);



            DatabaseReference ref=databaseReference.push();
            ref.setValue(settingMap);



            Toast.makeText(this, " Medication Updated Successfully", Toast.LENGTH_SHORT).show();
            // Intent in=new Intent(Diet.this,HomeActivity.class);
            //startActivity(in);

        }

    }
}
