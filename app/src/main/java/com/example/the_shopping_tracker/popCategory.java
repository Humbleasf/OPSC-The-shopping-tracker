package com.example.the_shopping_tracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

public class popCategory extends Activity {
    private static int PICK_IMAGE = 1;
    final int PERMISSION_CODE = 1001;
    Uri uImage;

    ImageView im;
    EditText txtCName, txtCDes;
    String strName;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_category);

        im = findViewById(R.id.imAddCategory);
        txtCName = findViewById(R.id.txtCatName);
        txtCDes = findViewById(R.id.txtCatDescrip);

        Intent getName = getIntent();
        strName = getName.getExtras().getString("UName");

        setTitle("Add Category");

        Log.d("test", " " + strName);

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            mAuth.signInWithCustomToken("96450314-94CC-426A-A113-09E6A058B6C0");
        } else {
            signInAnonymously();
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);

        TextView tv = new TextView(this);
        tv.setText(title);
    }

    public void getCatImage(View v) {
        //this aparently checks if the permission was granted by the user to let the app access the gallery
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //navigating to manifest                        if this permision exists
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                //need to request for permission if this if is true
                String[] Perms = {Manifest.permission.READ_EXTERNAL_STORAGE};
                //this will request for permission
                requestPermissions(Perms, PERMISSION_CODE);
            } else {
                //they had the permission
                PickFromGallery();
            }
        }
    }

    private void PickFromGallery() {
        /*Patel, J. 2017. No activity found to handle intent, StackOverflow. 16 August 2017. [Blog]. Available at:
        https://stackoverflow.com/questions/45707678/no-activity-found-to-handle-intent-act-android-intent-action-pick-dat-content [Accessed 15 May 2021]
        Pick an image from the gallery-Android studio-Java. 2018. Youtube video, added by Perviaz, A.
        [Online]. Available at: https://www.youtube.com/watch?v=O6dWwoULFI8 [Accessed 15 May 2021]*/

        //gets the gallery apps from the device not sim card storage
        Intent Gallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Gallery.setType("image/*");//this gets the image path
        startActivityForResult(Gallery, PICK_IMAGE);
    }

    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // do your stuff
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("signInAnonymously:FAILURE", "" + exception);
            }
        });
    }

    public void SaveCategory(View v) {
        try {
            //this gets the reference to the storage on firebase realtime
            FirebaseStorage Fstore = FirebaseStorage.getInstance();
            StorageReference rStore = Fstore.getReference().child("Images/" + uImage.getLastPathSegment());

            //this uploads the image uri to the storage
            UploadTask up = rStore.putFile(uImage);

            //this will check if the image was successfully
            up.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Error: " + e.toString(), Toast.LENGTH_LONG).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //seeing if the images was uploaded by downloading it and seeing what it returns
                    rStore.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String image = uri.toString();
                            String strCatName = txtCName.getText().toString();

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference dbRef = database.getReference().child(strName + "/category/" + strCatName + "/Image");
                            dbRef.setValue(image);
                            Toast.makeText(getApplicationContext(), "New category successfully saved!", Toast.LENGTH_LONG).show();
                            Intent cate = new Intent(popCategory.this, CategoryPage.class);
                            cate.putExtra("Name", strName);
                            startActivity(cate);
                        }
                    });
                }
            });
        }
        catch (Exception e)
        {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            uImage = data.getData();
            im.setImageURI(uImage);
        } else {
            Toast.makeText(getApplicationContext(), "There was an error loading your image", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] perms, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PickFromGallery();
                } else {
                    Toast.makeText(this, "Permission Denied..!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
