package com.example.ym.ecommerce;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ym.ecommerce.Model.Users;
import com.example.ym.ecommerce.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button login,register;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Paper.init(this);

        loadingbar = new ProgressDialog(this);

        login = (Button) findViewById(R.id.login_button);
        register = (Button) findViewById(R.id.signup_button);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, loginActivity.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, registerActivity.class);
                startActivity(intent);
            }
        });

        String phonekey = Paper.book().read(Prevalent.phonekey);
        String password2 = Paper.book().read(Prevalent.passwordkey);

        if (phonekey != "" && password2 != "")
        {
            if(!TextUtils.isEmpty(phonekey) && !TextUtils.isEmpty(password2))
            {
                allowaccess(phonekey,password2);

                loadingbar.setTitle("Already Logged in");
                loadingbar.setMessage("Please wait while we are logging into your account");
                loadingbar.setCanceledOnTouchOutside(true);
                loadingbar.show();
            }
        }
    }

    private void allowaccess(final String phone,final String passowrd1) {

        final DatabaseReference rootref;
        rootref = FirebaseDatabase.getInstance().getReference();

        rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Users").child(phone).exists())
                {

                    Users usersdata = dataSnapshot.child("Users").child(phone).getValue(Users.class);
                    if(usersdata.getPhone().equals(phone))
                    {
                        if(usersdata.getPassword().equals(passowrd1))
                        {
                            Toast.makeText(MainActivity.this,"Logged in Successfully", Toast.LENGTH_SHORT).show();
                            loadingbar.dismiss();

                            Intent intent = new Intent(MainActivity.this,homeActivity.class);
                            Prevalent.onlineuser = usersdata;
                            startActivity(intent);
                        }

                    }

                }
                else
                {
                    Toast.makeText(MainActivity.this, "Account with this " + phone + " do not exists",Toast.LENGTH_SHORT ).show();
                    loadingbar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
