package com.example.the_shopping_tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CategoryPage extends AppCompatActivity
{
    private static int PICK_IMAGE = 1;
    final int PERMISSION_CODE = 1001;//the code of the permission for reading gallery
    ImageView im;
    Button btnGal;
    Camera_Gallery cam = new Camera_Gallery();

//try do it via a set on click listener like in vid
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_page);

        im = findViewById(R.id.imgCaptureC);
        btnGal = findViewById(R.id.btnGalC);

        setTitle("Category");
    }
//Android Studio Tutorial - Take picture with Camera learn android programming. 2016. Youttube video, added by EDMT Dev. [Online]. Available at:
//https://www.youtube.com/watch?v=ondCeqlAwEI [Accessed 14 May 2021]


    //must be public for layout to pickup
    public void Capture(View view)
    {
        startActivityForResult(cam.in,0);
    }

    public void Profile(View v)
    {
        startActivity(new Intent(CategoryPage.this, Profile.class));
    }

    public void getGallery(View view)
    {
        //this aparently checks if the permission was granted by the user to let the app access the gallery
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            //navigating to manifest                        if this permision exists
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
            {
                //need to request for permission if this if is true
                String[] Perms = {Manifest.permission.READ_EXTERNAL_STORAGE};
                //this will request for permission
                requestPermissions(Perms, PERMISSION_CODE);
            }
            else
            {
                //they had the permission
                PickFromGallery();
            }
        }
    }

    private void PickFromGallery()
    {
        /*Patel, J. 2017. No activity found to handle intent, StackOverflow. 16 August 2017. [Blog]. Available at:
        https://stackoverflow.com/questions/45707678/no-activity-found-to-handle-intent-act-android-intent-action-pick-dat-content [Accessed 15 May 2021]
        Pick an image from the gallery-Android studio-Java. 2018. Youtube video, added by Perviaz, A.
        [Online]. Available at: https://www.youtube.com/watch?v=O6dWwoULFI8 [Accessed 15 May 2021]*/
        //gets the gallery apps from the device not sim card storage
        Intent Gallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Gallery.setType("image/*");//this gets the image path
        startActivityForResult(Gallery, PICK_IMAGE);
        /*Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        for some reason saying external content gets the internal storage
        pickIntent.setType("image/*");//sets the type of data

        startActivityForResult(pickIntent, PICK_IMAGE);*/
    }

    @Override
    public void setTitle(CharSequence title)
    {
        super.setTitle(title);

        TextView tv = new TextView(this);
        tv.setText(title);
    }
    //this is a method that waits for a resultfrom intents set in pick image from galley
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        //for camera
        try
        {
            cam.cbm = (Bitmap) data.getExtras().get("data");
            im.setImageBitmap(cam.cbm);
        }
        catch(Exception e)
        {
            startActivity(new Intent(CategoryPage.this, CategoryPage.class));
        }

        //for gallery
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE)
        {
            try
            {
                cam.getGallery(data);
                //sets the image from the file selected in the index
                im.setImageBitmap(BitmapFactory.decodeFile(cam.picturePath));

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            //set image view
            im.setImageURI(data.getData());
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] perms, @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case PERMISSION_CODE:
            {
                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    PickFromGallery();
                }
                else
                {
                    Toast.makeText(this, "Permission Denied..!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}