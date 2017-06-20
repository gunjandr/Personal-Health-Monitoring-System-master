package com.example.dell_pc.health_first;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.dell_pc.health_first.MainActivity.email;

/**
 * Created by ashish pc on 28-Mar-17.
 */


public class Dialog extends AppCompatActivity {


    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;
    //need this part for send SMS
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    String phoneNo;
    String emailee;
    //Ashsis: add these
    int enable=0;
    int option;
    // End

    //end of part for SMS
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);


        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                Dialog.this);

// Setting Dialog Title
        alertDialog2.setTitle("Its Time to Take your Medicine");

// Setting Dialog Message
        alertDialog2.setMessage("Medicine Name:  Crocin \nDosage: 1 Tablet \nInstructions: Please take with water");



        alertDialog2.setPositiveButton("TAKEN",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        Intent in = new Intent(Dialog.this, HomeActivity.class);
                        startActivity(in);
                        dialog.cancel();
                    }
                });


        alertDialog2.setNegativeButton("NOT TAKEN",
                new DialogInterface.OnClickListener() {




                    public void onClick(DialogInterface dialog, int which) {

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {

                            String uid = user.getUid();//get the current user UID
                            //get refrence to database
                            databaseReference = FirebaseDatabase.getInstance().getReference().child(uid);
                            databaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String emeconno = (String) dataSnapshot.child("emerContact").getValue();
                                    String emailemer = (String) dataSnapshot.child("emailemer").getValue();
                                    Integer enable1 = Integer.parseInt(dataSnapshot.child("settings").child("enable").getValue() + "");
                                    Integer option1 = Integer.parseInt(dataSnapshot.child("settings").child("method").getValue() + "");                                   // String enable1 = (String) dataSnapshot.child("Enable11").getValue();
                                    // String option1 = (String) dataSnapshot.child("Method111").getValue();


                                    if (enable1 != null) {
                                        enable = (int) enable1;
                                    }
                                    if (option1 != null) {
                                        option = (int) option1;
                                    }

                                   /* Toast.makeText(getApplicationContext(), "enabled "+enable,
                                            Toast.LENGTH_SHORT).show();

                                    Toast.makeText(getApplicationContext(), "option "+option,
                                            Toast.LENGTH_SHORT).show();*/
                                    //End
                                    //check if the user has emergency contact, if yes store in the varib phoneNo
                                    if (emeconno != null) {
                                        phoneNo = emeconno;
                                     /*   Toast.makeText(getApplicationContext(), "phoneNo "+phoneNo,
                                                Toast.LENGTH_SHORT).show();*/
                                    }
                                    if (emailemer != null) {
                                        emailee = emailemer;
                                      /*  Toast.makeText(getApplicationContext(), "emailee "+emailee,
                                                Toast.LENGTH_SHORT).show();*/
                                    }


                                    //Ashsis: add these
                                    if (enable == 1) {
                                        if (option == 1) {
                                            System.out.println("sms triggered " + option);
                                            sendSMSMessage();
                                        } else {
                                            System.out.println("email triggered " + option);
                                            sendEmail();
                                        }
                                    }
                                    //End


                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                        }


                        if(enable==1){  AlertDialog.Builder alertDialog3 = new AlertDialog.Builder(
                                Dialog.this);

                        // Setting Dialog Title
                        alertDialog3.setTitle("Medication Missed Alert!");

                        // Setting Dialog Message
                        alertDialog3.setMessage("An Email has been sent to the Emergency Contact Listed ");
                        alertDialog3.setPositiveButton("Acknowledge",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Write your code here to execute after dialog
                                        Intent in = new Intent(Dialog.this, HomeActivity.class);
                                        startActivity(in);


                                        dialog.cancel();
                                    }
                                });
                        // Write your code here to execute SMS and EMail Services here after dialog
                        alertDialog3.show();


                    }

                                     }
                });

// Showing Alert Dialog
        alertDialog2.show();




    }


    //start of sending the message code
    String message = "Tariq did did not take medicine as per the scheduled.";

    protected void sendSMSMessage() {

        Toast.makeText(getApplicationContext(), "sms triggered ",
                Toast.LENGTH_LONG).show();
        if (ContextCompat.checkSelfPermission(Dialog.this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
           /* Toast.makeText(getApplicationContext(), "Permission denied",
                    Toast.LENGTH_LONG).show();*/
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.SEND_SMS)) {

            } else {

                ActivityCompat.requestPermissions(Dialog.this,
                        new String[]{android.Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, message, null, null);
                    /*Toast.makeText(getApplicationContext(), "SMS sent.",
                            Toast.LENGTH_LONG).show();*/
                } else {
                   /* Toast.makeText(getApplicationContext(),
                            "SMS failed, please try again.", Toast.LENGTH_LONG).show();*/
                    return;
                }
            }
        }

    }

    public void sendEmail(){

       /* Toast.makeText(getApplicationContext(), "email triggered ",
                Toast.LENGTH_LONG).show();*/
        FirebaseAuth mAuth;
        FirebaseAuth.AuthStateListener mAuthListener;
        mAuth = FirebaseAuth.getInstance();


        String email =emailee.toString();// emai.toString();
        /* Toast.makeText(getApplicationContext(), "email triggered to "+email,
                Toast.LENGTH_LONG).show();*/

        if (mAuth != null && email != null) {
            Log.w(" if Email authenticated", "Recovery Email has been  sent to " + email);
            mAuth.sendPasswordResetEmail(email);
          /*  Toast.makeText(getApplicationContext(), "Emergency e-mail has be sent", Toast.LENGTH_SHORT).show();*/
            //Intent in = new Intent(Dialog.this, MainActivity.class);
            //startActivity(in);
        } else {
           /* Toast.makeText(getApplicationContext(), "Not Sent EMail", Toast.LENGTH_SHORT).show();*/
            Log.w(" error ", " bad entry ");
        }
    }


}