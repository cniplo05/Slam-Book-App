package com.sherlock504.projectandroid;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class register extends AppCompatActivity {
    mydbHelper myDb;
    EditText passwordField;
    EditText passwordField2;
    EditText usernameField;
    EditText fnameField;
    EditText lnameField;
    EditText birthdateField;
    TextView loginBtn;
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button regBtn;
    Context c = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        myDb = new mydbHelper(c);
        passwordField = findViewById(R.id.passwordField);
        passwordField2 = findViewById(R.id.passwordField2);
        usernameField = findViewById(R.id.usernameField);
        lnameField = findViewById(R.id.lnameField);
        fnameField = findViewById(R.id.fnameField);
        radioGroup = findViewById(R.id.radioGroup);
        birthdateField = findViewById(R.id.birthdateField);
        loginBtn = findViewById(R.id.loginBtn);
        regBtn = findViewById(R.id.regBtn);
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputPassword = passwordField.getText().toString();
                String inputPassword1 = passwordField2.getText().toString();
                String inputUsername = usernameField.getText().toString();
                String inputFname = fnameField.getText().toString();
                String inputLname = lnameField.getText().toString();
                String inputBirthdate = birthdateField.getText().toString();
                if(inputPassword.equals("") || inputUsername.equals("") || inputLname.equals("") || inputFname.equals("") || inputBirthdate.equals("")){
                    Toast.makeText(c,"All fields are required!",Toast.LENGTH_SHORT).show();

                }
                else if(inputPassword.length() < 6 || inputUsername.length() < 6){
                    Toast.makeText(c,"Password/Username must have at least 6 characters!!",Toast.LENGTH_SHORT).show();

                }
                else if(!inputPassword.equals(inputPassword1)){
                    Toast.makeText(c,"Passwords mismatch!",Toast.LENGTH_SHORT).show();
                }
                else{
                    boolean checkIfExists = myDb.checkIfExists(inputUsername);
                    if( checkIfExists == true){
                        Toast.makeText(c,"Account email already taken!",Toast.LENGTH_LONG).show();
                    }
                    else{
                        int selectedId = radioGroup.getCheckedRadioButtonId();

                        // find the radiobutton by returned id
                        radioButton = (RadioButton) findViewById(selectedId);
                        String selectedGender = radioButton.getText().toString();
                        boolean isAdded = myDb.insertRow(inputUsername,inputPassword,inputFname,inputLname,inputBirthdate,selectedGender,null,"inactive");
                        if(isAdded == true){
                            passwordField.setText("");
                            passwordField2.setText("");
                            usernameField.setText("");
                            fnameField.setText("");
                            lnameField.setText("");
                            birthdateField.setText("");
                            Toast.makeText(c,"Sucessfully Registered!",Toast.LENGTH_LONG).show();

                        }
                        else{
                            Toast.makeText(c,"Anyare!",Toast.LENGTH_LONG).show();
                        }

                    }


                }
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent i = new Intent(c,MainActivity.class);
                    startActivity(i);
            }
        });
    }
}
