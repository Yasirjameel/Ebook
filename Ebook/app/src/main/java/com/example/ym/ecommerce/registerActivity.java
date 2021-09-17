package com.example.ym.ecommerce;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class registerActivity extends AppCompatActivity {

    private EditText name,password,phonenumber;
    private Button registerbutton;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        name = (EditText) findViewById(R.id.register_email);
        password = (EditText) findViewById(R.id.register_password);
        phonenumber = (EditText) findViewById(R.id.register_confirm_password);
        registerbutton = (Button) findViewById(R.id.register_create_account);
        loadingbar = new ProgressDialog(this);


        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createnewaccount();
            }
        });

    }

    private void createnewaccount() {
        String username = name.getText().toString();
        String passowrd1 = password.getText().toString();
        String phone = phonenumber.getText().toString();

        if(TextUtils.isEmpty(username))
        {
            Toast.makeText(registerActivity.this,"Please write your email", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(passowrd1))
        {
            Toast.makeText(registerActivity.this,"Please write your passowrd", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(registerActivity.this,"Please write your confirm password", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingbar.setTitle("Creating account");
            loadingbar.setMessage("Please wait while we are creating new account");
            loadingbar.setCanceledOnTouchOutside(true);
            loadingbar.show();

            Validateaccount(username,phone,passowrd1);
        }
    }

    private void Validateaccount(final String username, final String phone, final String passowrd1) {

        final DatabaseReference rootref;
        rootref = FirebaseDatabase.getInstance().getReference();

        rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.child("Users").child(phone).exists()))
                {

                    HashMap<String, Object> usermap = new HashMap<>();
                    usermap.put("phone",phone);
                    usermap.put("name",username);
                    usermap.put("password",passowrd1);

                    rootref.child("Users").child(phone).updateChildren(usermap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                               if(task.isSuccessful())
                               {
                                   Toast.makeText(registerActivity.this,"Your account is created",Toast.LENGTH_SHORT).show();
                                   loadingbar.dismiss();

                                   Intent intent = new Intent(registerActivity.this,loginActivity.class);
                                   startActivity(intent);
                               }
                               else
                               {
                                   Toast.makeText(registerActivity.this,"Network error try again",Toast.LENGTH_SHORT).show();
                               }

                                }
                            });

                }
                else
                {
                    Toast.makeText(registerActivity.this,"This Number " + phone + "already exists",Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
