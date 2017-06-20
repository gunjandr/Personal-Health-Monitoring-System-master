package com.example.dell_pc.health_first;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ForgotPasswordActivity extends Activity {
    private DatabaseReference databaseReference;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    // private String email = "ashish_22@hotmail.com";
    Button b1, b2, b3;
    EditText ed1, ed2, ed3,ed4;

    TextView tx1;
    int counter = 3;

    public boolean isvalidemailid(final String email) {
        Pattern pattern;
        Matcher matcher;

        // final String email_pattern="^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$";
        final String email_pattern = ".+@.+\\.[a-z]+";
        pattern = Pattern.compile(email_pattern);
        matcher = pattern.matcher(email);

        return matcher.matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        b1 = (Button) findViewById(R.id.button1);
        b2 = (Button) findViewById(R.id.button2);
        b3 = (Button) findViewById(R.id.button3);
        ed4 = (EditText) findViewById(R.id.email);
        ed1 = (EditText) findViewById(R.id.enterpwd);
        ed2 = (EditText) findViewById(R.id.newpwd);
        ed3 = (EditText) findViewById(R.id.confirmpwd);

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(ForgotPasswordActivity.this, MainActivity.class);
                startActivity(in);
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("EMail:",ed4.getText().toString());
                Log.v("PWD:",ed1.getText().toString());
                //write code for retrieving pwd from db and checking if record exists in db

                Log.v("NEWPWD:",ed2.getText().toString());

                if(ed2.getText().toString().length()<8 &&!isValidPassword(ed2.getText().toString())){
                    Toast.makeText(getApplicationContext(), " Password Does Not Meet Specifications", Toast.LENGTH_SHORT).show();

                }else{
                    System.out.println("New Password Valid");

                    if(ed2.getText().toString().equals(ed3.getText().toString())){


                        //write code to store in DB
                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        AuthCredential credential = EmailAuthProvider
                                .getCredential(ed4.getText().toString(), ed2.getText().toString());

                        if (user != null) {
                            mAuth.signInWithEmailAndPassword(ed4.getText().toString(), ed2.getText().toString());
                            user.reauthenticate(credential)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                user.updatePassword(ed2.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            //   Log.d(TAG, "Password updated");
                                                        } else {
                                                            //   Log.d(TAG, "Error password not updated");
                                                        }
                                                    }
                                                });
                                            } else {
                                                //  Log.d(TAG, "Error auth failed");
                                            }
                                        }
                                    });
                        }


                        Toast.makeText(getApplicationContext(), " Password Updated Successfully", Toast.LENGTH_SHORT).show();
                        Intent in = new Intent(ForgotPasswordActivity.this, MainActivity.class);
                        startActivity(in);


                    }
                    else{
                        Toast.makeText(getApplicationContext(), " Passwords Do not Match", Toast.LENGTH_SHORT).show();

                    }


                }
                Log.v("CONPWD:",ed3.getText().toString());









            }
        });

    }
    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
}