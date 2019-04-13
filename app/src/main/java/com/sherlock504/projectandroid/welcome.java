package com.sherlock504.projectandroid;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class welcome extends AppCompatActivity {
    TextView userName,emailTextView,fullNameTextView,genderTextView,birthdateTextView;
    ImageView myPhoto;
    Button takePhoto;
    Button selectPhoto;
    Button logoutBtn;
    Context c = this;
    private static String MY_ID = "";
    private static String MY_FNAME = "";
    private static String MY_LNAME = "";
    private static String MY_BIRTHDATE = "";
    private static String MY_GENDER = "";
    private static String MY_USERNAME = "";
    private static String MY_PASSWORD = "";
    private static String MY_STATUS = "";
    private static String MY_DISPLAY_PHOTO = null;
    private File output=null;
    private static final int CAMERA_REQUEST = 3;
    private static final int PICK_FROM_GALLERY = 1;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 2;
    mydbHelper myDb;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PICK_FROM_GALLERY:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent galleryIntent = new Intent();
                    galleryIntent.setType("image/*");
                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), PICK_FROM_GALLERY);;
                } else {
                    Toast.makeText(c,"Access Denied!",Toast.LENGTH_LONG).show();
                }
                break;
            case CAMERA_REQUEST:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        Intent cameraIntent = new
                                Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        File dir=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                        String pictureFile = "/Camera/DelaCruz_" + timeStamp;
                        output=new File(dir, pictureFile+".jpg");
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    }catch (Exception e){
                        Toast.makeText(c,"Error: "+e,Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(c,"Access Denied!",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        myDb = new mydbHelper(c);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            String filePath = output.getPath();
            boolean isUpdated = myDb.updateRow(MY_ID,MY_USERNAME,MY_PASSWORD,MY_FNAME,MY_LNAME,MY_BIRTHDATE,MY_GENDER,filePath,MY_STATUS);
            if(isUpdated == true){
                MY_DISPLAY_PHOTO = filePath;
                Toast.makeText(c,"Sucessfully Updated!",Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(c,"Anyare?",Toast.LENGTH_LONG).show();
            }
            Bitmap photo = BitmapFactory.decodeFile(filePath);
            myPhoto.setImageBitmap(photo);

        }
        else if (requestCode == PICK_FROM_GALLERY && resultCode == Activity.RESULT_OK){
            if (data == null) {
                //Display an error
                return;
            }

            try {
                File dir=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                Uri myUri = data.getData();
                String myPath = myUri.getPath();
                String filename=myPath.substring(myPath.lastIndexOf("/")+1);
                String myAbsolutePath = dir.toString()+"/Camera/"+filename;
                boolean isUpdated = myDb.updateRow(MY_ID,MY_USERNAME,MY_PASSWORD,MY_FNAME,MY_LNAME,MY_BIRTHDATE,MY_GENDER, myAbsolutePath,MY_STATUS);
                if(isUpdated == true){

                    MY_DISPLAY_PHOTO = myAbsolutePath;
                    Toast.makeText(c,"Sucessfully Updated!" ,Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(c,"Anyare?",Toast.LENGTH_LONG).show();
                }
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), myUri);
                // Log.d(TAG, String.valueOf(bitmap));
                myPhoto.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
    @Override
    public void onBackPressed() {
       //
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        myDb = new mydbHelper(c);
        logoutBtn = findViewById(R.id.logoutBtn);
        myPhoto = findViewById(R.id.myPhoto);
        selectPhoto = findViewById(R.id.selectPhoto);
        takePhoto = findViewById(R.id.takePhoto);
        userName = findViewById(R.id.userName);
        emailTextView = findViewById(R.id.emailTextView);
        birthdateTextView = findViewById(R.id.birthdateTextView);
        genderTextView = findViewById(R.id.genderTextView);
        fullNameTextView = findViewById(R.id.fullNameTextView);
        Intent i = getIntent();
        MY_ID = i.getStringExtra("id");
        MY_USERNAME = i.getStringExtra("userName");
        MY_PASSWORD = i.getStringExtra("password");
        MY_STATUS = i.getStringExtra("status");
        String nameUser = i.getStringExtra("userName");
        String mygender = i.getStringExtra("gender");
        String mybirthday = i.getStringExtra("birthdate");
        String myfullname = i.getStringExtra("fname") +" "+i.getStringExtra("lname");
        MY_BIRTHDATE = mybirthday;
        MY_GENDER = mygender;
        MY_FNAME = i.getStringExtra("fname");
        MY_LNAME = i.getStringExtra("lname");
        if(i.getStringExtra("display_photo").equals("wala")){
        }
        else{
            MY_DISPLAY_PHOTO = i.getStringExtra("display_photo");
            String photoPath = i.getStringExtra("display_photo");
            Bitmap photo = BitmapFactory.decodeFile(photoPath);
            myPhoto.setImageBitmap(photo);
        }
        userName.setText(i.getStringExtra("fname")+"!");
        emailTextView.setText(nameUser+"");
        genderTextView.setText(mygender);
        birthdateTextView.setText(mybirthday);
        fullNameTextView.setText(myfullname);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isUpdated = myDb.updateRow(MY_ID,MY_USERNAME,MY_PASSWORD,MY_FNAME,MY_LNAME,MY_BIRTHDATE,MY_GENDER,MY_DISPLAY_PHOTO,"inactive");
                if(isUpdated == true){
                    Toast.makeText(c,"Account Successfully Logged Out!",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(c,"Anyare?!",Toast.LENGTH_LONG).show();
                }
            Intent i = new Intent(c, MainActivity.class);
            startActivity(i);
            }
        });

        selectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (ActivityCompat.checkSelfPermission(c, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(welcome.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
                    } else {
                        Intent galleryIntent = new Intent();
                        galleryIntent.setType("image/*");
                        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), PICK_FROM_GALLERY);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (ActivityCompat.checkSelfPermission(c, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ) {
                        ActivityCompat.requestPermissions(welcome.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, CAMERA_REQUEST);
                    } else {
                        try {
                            if (ActivityCompat.checkSelfPermission(c, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(welcome.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST);
                            }
                            else{
                                Intent cameraIntent = new
                                        Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                File dir=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                                String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                                String pictureFile = "/Camera/DelaCruz_" + timeStamp;
                                output=new File(dir, pictureFile+".jpeg");
                                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
                                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                            }

                        }catch (Exception e){
                            Toast.makeText(c,"Error: "+e,Toast.LENGTH_LONG).show();
                        }


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

}
