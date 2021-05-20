package com.example.the_shopping_tracker;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

public class Camera_Gallery extends Activity
{
    Intent in = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    //this is the same as saying that the new intent or page that should display is the camera option on your phone
    Bitmap cbm;
    String picturePath;

    public String getGallery(Intent data)
    {
        //Hamad. 2013. Get selected image from gallery into imageView, StackOverflow. 25 November 2013. [Blog]. Available at:
        //https://stackoverflow.com/questions/20197487/get-selected-image-from-gallery-into-imageview/20197713 [Accessed 17 May 2021]
        Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        //on a way this selects all the items in the gallery and sorts them.
        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();

        //gets the selected item in the index declared
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        picturePath = cursor.getString(columnIndex);
        cursor.close();

        return picturePath;
    }
}
