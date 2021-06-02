package com.example.the_shopping_tracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class Profile extends AppCompatActivity
{
    Camera_Gallery cam = new Camera_Gallery();
    EditText txtChangepass,DelUser;
    Button btnChangePass;
    ImageView imProfile;
    String strName;

    private static int PICK_IMAGE = 1;
    final int PERMISSION_CODE = 1001;
    Uri uImage;

    HashMap<String, String> hCItem = new HashMap<String, String>();

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        txtChangepass = findViewById(R.id.txtChangePass);
        //DelUser = findViewById(R.id.txtDelName);
        btnChangePass = findViewById(R.id.btnChangePass);
        imProfile = findViewById(R.id.imProfile);

        Intent getName = getIntent();
        strName = getName.getExtras().getString("getName");
//SnapshotName.getRef().removeValue();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null)
        {
            mAuth.signInWithCustomToken("96450314-94CC-426A-A113-09E6A058B6C0");
        }
        else
        {
            signInAnonymously();
        }

        try
        {
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference Refdb = db.getReference(strName + "/ProfileImage");

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference refStore = storage.getReference();

            Refdb.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot getProImg)
                {
                    try
                    {
                        String proImage = getProImg.getValue().toString();

                        StorageReference imageRef = storage.getReferenceFromUrl(proImage + "");
                        Glide.with(Profile.this).load(imageRef).into(imProfile);
                    }
                    catch(Exception e)
                    {

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error)
                {
                    Toast.makeText(getApplicationContext(),"Error loading user image", Toast.LENGTH_LONG).show();
                }
            });
        }
        catch(Exception e)
        {

        }

        setTitle("Profile");
    }
    public void Set_Profile_image(View v)
    {
        try
        {
            //this gets the reference to the storage on firebase realtime
            FirebaseStorage Fstore = FirebaseStorage.getInstance();
            StorageReference rStore = Fstore.getReference().child("Images/" + uImage.getLastPathSegment());

            //this uploads the image uri to the storage
            UploadTask up = rStore.putFile(uImage);

            //this will check if the image was successfully
            up.addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    Toast.makeText(getApplicationContext(), "Error: " + e.toString(),Toast.LENGTH_LONG).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
            {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    //seeing if the images was uploaded by downloading it and seeing what it returns
                    rStore.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                    {
                        @Override
                        public void onSuccess(Uri uri)
                        {
                            String image = uri.toString();

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference dbRef = database.getReference().child(strName + "/ProfileImage");
                            dbRef.setValue(image);
                            Toast.makeText(getApplicationContext(), "User profile has been set!",Toast.LENGTH_LONG).show();
                            //send the username back to the category page
                            Intent cate = new Intent(Profile.this,Profile.class);
                            cate.putExtra("getName",strName);
                            startActivity(cate);
                        }
                    });
                }
            });
        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(),"Please select a profile image first", Toast.LENGTH_LONG).show();
        }
    }
    public void getProImage(View v)
    {
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
                PickFromGallery();
            }
        }
    }

    private void PickFromGallery()
    {
        Intent Gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Gallery.setType("image/*");

        startActivityForResult(Gallery,PICK_IMAGE);
    }
    public void Change(View v)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference userNameRef = database.getReference().child(strName + "/Password");
        userNameRef.setValue(txtChangepass.getText().toString());
    }
    public void Logout(View v)
    {
        startActivity(new Intent(Profile.this, Login.class));
    }

    private void signInAnonymously()
    {
        mAuth.signInAnonymously().addOnSuccessListener(this, new  OnSuccessListener<AuthResult>()
        {
            @Override
            public void onSuccess(AuthResult authResult)
            {
                //this will still return a sign in even though we have no code here
            }
        }).addOnFailureListener(this, new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception exception)
            {
                Log.d( "signInAnonymously:FAILURE", ""+exception);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);

        //for gallery
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE)
        {
            if (resultCode == RESULT_OK)
            {
                uImage = data.getData();
                imProfile.setImageURI(uImage);
            }
            else
            {
                Toast.makeText(getApplicationContext(),"There was an error loading your image",Toast.LENGTH_LONG).show();
            }
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

    public void ShoppingPage(View view)
    {
        Intent shopping = new Intent(Profile.this, Shopping_Cart.class);
        shopping.putExtra("Name", strName);
        startActivity(shopping);
    }

    public void Profile(View view)
    {
        Intent profile = new Intent(Profile.this, Profile.class);
        profile.putExtra("getName", strName);
        startActivity(profile);
    }

    //navigation buttons
    public void HomePage(View v)
    {
        Intent homepage = new Intent(Profile.this, CategoryPage.class);
        homepage.putExtra("Name", strName);
        startActivity(homepage);
    }

    public void GraphPage(View v)
    {
        //message
        Toast.makeText(getApplicationContext(), "Feature not yet available!",
                Toast.LENGTH_SHORT).show();
    }


}