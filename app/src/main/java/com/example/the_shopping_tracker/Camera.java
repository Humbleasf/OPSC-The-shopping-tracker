package com.example.the_shopping_tracker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;

public class Camera
{
    Intent in = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    //this is the same as saying that the new intent or page that should display is the camera option on your phone
    Bitmap cbm;
}
