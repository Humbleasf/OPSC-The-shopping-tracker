package com.example.the_shopping_tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Profile extends AppCompatActivity
{
    Camera_Gallery cam = new Camera_Gallery();
    EditText txtChangepass,DelUser;
    Button btnChangePass;
    ImageView imProfile;
    private static int PICK_IMAGE = 1;
    final int PERMISSION_CODE = 1001;
    Login L = new Login();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        DelUser = findViewById(R.id.txtDelName);
        btnChangePass = findViewById(R.id.btnChangePass);
//SnapshotName.getRef().removeValue();
        setTitle("Profile");
    }
    public void Gallery(View v)
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
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Intent Gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Gallery.setType("image/*");

        Intent SelectIntent = Intent.createChooser(Gallery,"Select from gallery");
        SelectIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {camera});
        startActivityForResult(SelectIntent,PICK_IMAGE);
    }
    public void Change(View v)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference userNameRef = database.getReference().child(L.Uname.toString());
        userNameRef.setValue(txtChangepass);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);

        //for gallery
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE)
        {
            try
            {
                cam.cbm = (Bitmap) data.getExtras().get("data");
                imProfile.setImageBitmap(cam.cbm);
                Toast.makeText(this, "Profile image set",Toast.LENGTH_LONG).show();
            }
            catch(Exception e)
            {
                Toast.makeText(this, "error fetching camera",Toast.LENGTH_LONG).show();
            }
            try
            {
                cam.getGallery(data);
                //sets the image from the file selected in the index
                imProfile.setImageBitmap(BitmapFactory.decodeFile(cam.picturePath));
                Toast.makeText(this, "Profile image set",Toast.LENGTH_LONG).show();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            //set image view
            imProfile.setImageURI(data.getData());
            startActivity(new Intent(Profile.this, Profile.class));
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
    @Override
    public void setTitle(CharSequence title)
    {
        super.setTitle(title);

        TextView tv = new TextView(this);
        tv.setText(title);
    }
}