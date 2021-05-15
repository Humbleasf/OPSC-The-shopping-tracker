package com.example.the_shopping_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CategoryPage extends AppCompatActivity
{
    ImageView im;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_page);

        im = findViewById(R.id.imgCaptureC);

        setTitle("The Shopping Tracker");
    }
//Android Studio Tutorial - Take picture with Camera learn android programming. 2016. Youttube video, added by EDMT Dev. [Online]. Available at:
//https://www.youtube.com/watch?v=ondCeqlAwEI []
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        Bitmap bm = (Bitmap) data.getExtras().get("data");
        im.setImageBitmap(bm);
    }

    //must be public for layout to pickup
    public void Capture(View view)
    {
        Intent in = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //this is the same as saying that the new intent or page that should display is the camera option on your phone

        startActivityForResult(in,0);
    }
    @Override
    public void setTitle(CharSequence title)
    {
        super.setTitle(title);

        TextView tv = new TextView(this);
        tv.setText(title);
    }
}