package com.example.the_shopping_tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.Edits;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class CategoryPage extends AppCompatActivity
{
    public List<Category> lstCategory;
    HashMap<String,String> hCategory = new HashMap<String, String>();
    HashMap<String,String> hCItem = new HashMap<String, String>();
    HashMap<String,Long> hCItem2 = new HashMap<String, Long>();
    List<String> lDescription = new ArrayList<String>();
    List<String> lItem = new ArrayList<String>();
    List<String> lCategory = new ArrayList<String>();

    String[] strCKey = new String[50];
    //String[] strCImage = new String[50];
    String[] strCIKey = new String[50];
    String[] strCICKey = new String[50];
    String[] strItemKey = new String[50];
    String strName;

    int[] iProgressBar = new int[50];
    int[] iGoalBar = new int[50];
    int iCountMatch = 0;


    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private static int PICK_IMAGE = 1;
    final int PERMISSION_CODE = 1001;//the code of the permission for reading gallery
    ImageView im;
    Button btnGal;
    Camera_Gallery cam = new Camera_Gallery();
    CategoryItemCount cic = new CategoryItemCount();
    ImageButton btnShopping, btnProfile, btnExit;
//try do it via a set on click listener like in vid
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_page);
        //btnExit = (ImageButton) findViewById(R.id.btnExit);
        btnShopping = (ImageButton) findViewById(R.id.btnShopping);
        btnProfile = (ImageButton) findViewById(R.id.btnProfile);
        Intent Home = getIntent();
        strName = Home.getExtras().getString("Name");
        Log.d("firebase", "key strName:  " + strName);
        lstCategory = new ArrayList<>();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) { //96450314-94CC-426A-A113-09E6A058B6C0
            mAuth.signInWithCustomToken("96450314-94CC-426A-A113-09E6A058B6C0");
        } else {
            signInAnonymously();
        }
        setTitle("Categories");



        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        DatabaseReference cateRef = database.getReference().child(strName+"/category/" );

        final String[] strCName = new String[50];
        //This method is called once with the initial value and again
        //whenever data at this location is updated.

            cateRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot ds) {
                    for (int a = 0; a < 40; a++) {
                        final int x = a;

                        {
                            try
                            {
                                //StorageReference gsReference = storage.getReferenceFromUrl("gs://the-shopping-tracker.appspot.com/download.jpg");
                                hCategory = (HashMap<String, String>) ds.getValue();
                                //hCategory.values().toArray(strCValue);
                                //children = (int)ds.getChildrenCount();
                                hCategory.keySet().toArray(strCKey);
                                Long lCCount = ds.getChildrenCount();
                                int iCCount = lCCount.intValue();
                                if (iCCount == x) {
                                    break;
                                }
                                //Log.d("firebase", "key strCIKey:  " + strCKey[x]);

                                DatabaseReference cChildrenRef = database.getReference().child(strName + "/category/" + strCKey[x] + "/");

                                cChildrenRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dsItem) {

                                        Long lChildCount = dsItem.getChildrenCount();

                                        final int iChildCount = lChildCount.intValue() - 1;

                                        if (iChildCount != 0) {
                                            hCItem = (HashMap<String, String>) dsItem.getValue();
                                            hCItem.keySet().toArray(strCIKey);

                                            Long lCICount = dsItem.getChildrenCount();
                                            int iCICount = lCICount.intValue() - 1;
                                            for (int q = 0; q < strCIKey.length; q++) {
                                                if (strCIKey[q] == null) {
                                                    strCIKey[q] = "";
                                                }
                                            }
                                            int b = 0;

                                            for (int q = 0; q < strCICKey.length; q++) {
                                                if (!strCIKey[q].equals("Image") && !strCIKey[q].equals("")) {
                                                    strItemKey[b] = strCIKey[q];
                                                    b++;
                                                }
                                            }

                                            for (int k = 0; k < strItemKey.length; k++) {
                                                final int y = k;
                                                for (int q = 0; q < strItemKey.length; q++) {
                                                    if (strItemKey[q] == null) {
                                                        strItemKey[q] = "";
                                                    }
                                                }
                                                if (strItemKey[k].equals("")) {
                                                    continue;
                                                }
                                                if (lDescription.contains(strItemKey)) {
                                                    continue;
                                                }
                                                //Log.d("firebase", "key children:  " + strItemKey[k]);
                                                lDescription.add(strItemKey[k]);
                                                DatabaseReference cCItemRef = database.getReference().child(strName + "/category/" + strCKey[x] + "/" + strItemKey[k]);
                                                cCItemRef.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dsItemCount) {
                                                        try {
                                                            hCItem2 = (HashMap<String, Long>) dsItemCount.getValue();
                                                            if(hCItem2 != null) {

                                                                String strProgress = String.valueOf(hCItem2.get("stock"));
                                                                String strGoal = String.valueOf(hCItem2.get("goal"));
                                                                lItem.add(strItemKey[y]);
                                                                if (lItem.get(y) == strItemKey[y]) {
                                                                    iCountMatch = 0;
                                                                }


                                                                int iGoal = Integer.parseInt(strGoal);
                                                                int iProgress = Integer.parseInt(strProgress);
                                                                if (iGoal == iProgress) {
                                                                    iCountMatch++;
                                                                }

                                                                iProgressBar[x] = iCountMatch;
                                                                iGoalBar[x] = iChildCount;
                                                            }
                                                        }
                                                        catch (Exception e)
                                                        {

                                                        }
                                                        DatabaseReference cImageRef = database.getReference().child(strName + "/category/" + strCKey[x] + "/Image");
                                                        cImageRef.addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                                String strCIValue = snapshot.getValue().toString();
                                                                StorageReference imageRef = storage.getReferenceFromUrl(strCIValue + "");
                                                                if (!lCategory.contains(strCKey[x])) {
                                                                    lCategory.add(strCKey[x]);
                                                                    lstCategory.add(new Category("" + strCKey[x], imageRef, iProgressBar[x], iGoalBar[x]));
                                                                    RecyclerView myRecycleView = (RecyclerView) findViewById(R.id.recycleview_id);
                                                                    RecycleViewAdapter myAdapter = new RecycleViewAdapter(CategoryPage.this, lstCategory, strName);
                                                                    myRecycleView.setLayoutManager(new GridLayoutManager(CategoryPage.this, 2));
                                                                    myRecycleView.setAdapter(myAdapter);
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {
                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                    }
                                                });
                                            }
                                        }
                                        else
                                        {
                                            DatabaseReference cImageRef = database.getReference().child(strName + "/category/" + strCKey[x] + "/Image");
                                            cImageRef.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    String strCIValue = snapshot.getValue().toString();
                                                    StorageReference imageRef = storage.getReferenceFromUrl(strCIValue + "");
                                                    if (!lCategory.contains(strCKey[x])) {
                                                        lCategory.add(strCKey[x]);
                                                        lstCategory.add(new Category("" + strCKey[x], imageRef, 0, 1));
                                                        RecyclerView myRecycleView = (RecyclerView) findViewById(R.id.recycleview_id);
                                                        RecycleViewAdapter myAdapter = new RecycleViewAdapter(CategoryPage.this, lstCategory, strName);
                                                        myRecycleView.setLayoutManager(new GridLayoutManager(CategoryPage.this, 2));
                                                        myRecycleView.setAdapter(myAdapter);
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                }
                                            });
                                        }
                                       // Log.d("firebase", "key children:  " + children[x]);

                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Log.d("firebase", "key Test  ");
                                    }
                                });
                            }
                            catch (Exception e)
                            {
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "An error has occurred when trying to reach the database. please check your internet connection ",
                            Toast.LENGTH_LONG).show();
                    Log.d("firebase", "key Exception:  " + error.toString());
                }
            });
        /*
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });*/
    }
    //pages
    public void ShoppingPage(View v)
    {
        Intent shopping = new Intent(CategoryPage.this, Shopping_Cart.class);
        shopping.putExtra("Name", strName);
        startActivity(shopping);
    }

    public void Profile(View v)
    {
        Intent profile = new Intent(CategoryPage.this, Profile.class);
        profile.putExtra("getName", strName);
        startActivity(profile);
    }

    //navigation buttons
    public void HomePage(View v)
    {
        Intent category = new Intent(CategoryPage.this, CategoryPage.class);
        category.putExtra("getName", strName);
        startActivity(category);
    }

    public void GraphPage(View v)
    {
        //message
        Toast.makeText(getApplicationContext(), "Feature not yet available!",
                Toast.LENGTH_SHORT).show();
    }
    public void LogoutPage(View v)
    {
        //check logout method
        Intent logout = new Intent(CategoryPage.this, Login.class);
        logout.putExtra("getName", strName);
        finish();
        startActivity(logout);

    }




//Android Studio Tutorial - Take picture with Camera learn android programming. 2016. Youttube video, added by EDMT Dev. [Online]. Available at:
//https://www.youtube.com/watch?v=ondCeqlAwEI [Accessed 14 May 2021]

    //must be public for layout to pickup
    public void Capture(View view)
    {
        startActivityForResult(cam.in,0);
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
    //firebase
    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // do your stuff
            }
        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d("signInAnonymously:FAILURE", "" + exception);
                    }
                });
    }
    public void addCategory(View v)
    {
        Intent pro = new Intent(CategoryPage.this,popCategory.class);
        pro.putExtra("UName", strName);
        startActivity(pro);
        lstCategory.clear();
    }
}