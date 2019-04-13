package com.sherlock504.projectandroid;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button loginBtn;
    TextView regBtn;
    EditText usernameField;
    EditText passwordField;
    Context c = this;
    mydbHelper myDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginBtn = findViewById(R.id.loginBtn);
        regBtn = findViewById(R.id.regBtn);
        usernameField = findViewById(R.id.usernameField);
        passwordField = findViewById(R.id.passwordField);
        myDb = new mydbHelper(c);
        Cursor checkSession = myDb.checkSession();
        if (!(checkSession.moveToFirst()) || checkSession.getCount() ==0){
            Toast.makeText(c,"Welcome to projectAndroid :)",Toast.LENGTH_LONG).show();
        }
        else{
            Intent i = new Intent(c,welcome.class);
            i.putExtra("userName", checkSession.getString(1));
            i.putExtra("password", checkSession.getString(2));
            i.putExtra("fname",checkSession.getString(3));
            i.putExtra("lname", checkSession.getString(4));
            i.putExtra("birthdate", checkSession.getString(5));
            i.putExtra("gender", checkSession.getString(6));
            i.putExtra("status", "active");
            i.putExtra("id", checkSession.getString(0));
            if(checkSession.getString(7) == null){
                i.putExtra("display_photo", "wala");
            }
            else{
                i.putExtra("display_photo", checkSession.getString(7));
            }
            startActivity(i);
        }
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputPassword = passwordField.getText().toString();
                String inputUsername = usernameField.getText().toString();
                if(inputPassword.equals("") || inputUsername.equals("")){
                    Toast.makeText(c,"All fields are required!",Toast.LENGTH_SHORT).show();

                }
                else{
                    Cursor attmpt = myDb.login(usernameField.getText().toString(),passwordField.getText().toString());
                    if (!(attmpt.moveToFirst()) || attmpt.getCount() ==0){
                        Toast.makeText(c,"Account does not exist!",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        boolean isUpdated = myDb.updateRow(attmpt.getString(0),attmpt.getString(1),attmpt.getString(2),
                                attmpt.getString(3),attmpt.getString(4),attmpt.getString(5),attmpt.getString(6),attmpt.getString(7),"active");
                        if(isUpdated == true){
                            Toast.makeText(c,"Account Successfully Logged In!",Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(c,"Anyare?!",Toast.LENGTH_LONG).show();
                        }
                        Intent i = new Intent(c,welcome.class);
                        i.putExtra("userName", usernameField.getText().toString());
                        i.putExtra("password", passwordField.getText().toString());
                        i.putExtra("fname", attmpt.getString(3));
                        i.putExtra("lname", attmpt.getString(4));
                        i.putExtra("birthdate", attmpt.getString(5));
                        i.putExtra("gender", attmpt.getString(6));
                        i.putExtra("status", "active");
                        i.putExtra("id", attmpt.getString(0));
                        if(attmpt.getString(7) == null){
                            i.putExtra("display_photo", "wala");
                        }
                        else{
                            i.putExtra("display_photo", attmpt.getString(7));
                        }
                        startActivity(i);
                    }


                }

            }
        });
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(c,register.class);
                startActivity(i);
            }
        });
    }


}
