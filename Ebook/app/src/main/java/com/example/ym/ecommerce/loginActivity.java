package com.example.ym.ecommerce;

import android.app.ProgressDialog;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
//import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.widget.CheckBox;

import com.example.ym.ecommerce.Model.Users;
import com.example.ym.ecommerce.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class loginActivity extends AppCompatActivity {


    private EditText phonenumber, password;
    private Button loginbutton;

    private ProgressDialog loadingbar;
    private TextView admin, user;
    String parentdbname = "Users";
    private CheckBox rememberme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        rememberme = (CheckBox) findViewById(R.id.chkbox);
        Paper.init(this);
        phonenumber = (EditText) findViewById(R.id.login_email);
        password = (EditText) findViewById(R.id.login_password);
        loginbutton = (Button) findViewById(R.id.login_button1);
        loadingbar = new ProgressDialog(this);
        admin = (TextView) findViewById(R.id.admin_link);
        user = (TextView) findViewById(R.id.user_link);

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginbutton.setText("Login Admin");
                admin.setVisibility(View.INVISIBLE);
                user.setVisibility(View.VISIBLE);
                parentdbname = "Admins";
            }
        });

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginbutton.setText("Login");
                admin.setVisibility(View.VISIBLE);
                user.setVisibility(View.INVISIBLE);
                parentdbname = "Users";
            }
        });

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allowusertologin();
            }
        });
    }


    private void allowusertologin() {


        String passowrd1 = password.getText().toString();
        String phone = phonenumber.getText().toString();


        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(loginActivity.this, "Please write your phone number", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(passowrd1)) {
            Toast.makeText(loginActivity.this, "Please write your passowrd", Toast.LENGTH_SHORT).show();
        } else {
            loadingbar.setTitle("Logging into your account");
            loadingbar.setMessage("Please wait while we are logging into your account");
            loadingbar.setCanceledOnTouchOutside(true);
            loadingbar.show();

            allowaccestoaccount(passowrd1, phone);

        }
    }

    private void allowaccestoaccount(final String passowrd1, final String phone) {


        if (rememberme.isChecked()) {
            Paper.book().write(Prevalent.phonekey, phone);
            Paper.book().write(Prevalent.passwordkey, passowrd1);
        }


        final DatabaseReference rootref;
        rootref = FirebaseDatabase.getInstance().getReference();

        rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentdbname).child(phone).exists()) {

                    Users usersdata = dataSnapshot.child(parentdbname).child(phone).getValue(Users.class);
                    if (usersdata.getPhone().equals(phone)) {
                        if (usersdata.getPassword().equals(passowrd1)) {
                            if (parentdbname.equals("Admins")) {
                                Toast.makeText(loginActivity.this, "Admin you are Logged in Successfully", Toast.LENGTH_SHORT).show();
                                loadingbar.dismiss();

                                Intent intent = new Intent(loginActivity.this, admincatogeryActivity.class);
                                startActivity(intent);
                            } else if (parentdbname.equals("Users")) {
                                Toast.makeText(loginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                loadingbar.dismiss();
                                Prevalent.onlineuser = usersdata;
                                Intent intent = new Intent(loginActivity.this, homeActivity.class);
                                startActivity(intent);
                            }

                        }

                    }

                } else {
                    Toast.makeText(loginActivity.this, "Account with this " + phone + " do not exists", Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


}
