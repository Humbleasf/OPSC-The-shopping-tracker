package com.example.the_shopping_tracker;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class AddItems extends AppCompatActivity {


    Button btnAdd, btnView;
    EditText txtName, txtDescription, txtPrice, txtStock, txtGoal;
    TextView txtDate;
    ImageView btnImage;
    String strName;
    String strCategory;
    Uri image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_items);

        Intent intent = getIntent();
        strName = intent.getExtras().getString("Name");
        strCategory = intent.getExtras().getString("Category");




        setTitle("Add New Items");

        btnView = findViewById(R.id.btnView);
        btnAdd = findViewById(R.id.btnAdd);
        txtName = findViewById(R.id.txtName);
        txtDescription = findViewById(R.id.txtDescription);
        txtPrice = findViewById(R.id.txtPrice);
        txtStock = findViewById(R.id.txtStockT);
        txtGoal = findViewById(R.id.txtGoal);
        txtDate = findViewById(R.id.txtDate);
        btnImage = findViewById(R.id.btnImage);


        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalendar();
            }
        });

        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        btnView.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddItems.this, ItemList.class);
                Log.d("firebase", "key strName:  " + strName);
                intent.putExtra("Name", strName);
                intent.putExtra("Category", strCategory);
                startActivity(intent);
            }
        }));




        btnAdd.setOnClickListener(new View.OnClickListener() {
                                      public void onClick(View v) {
                                          FirebaseStorage storage = FirebaseStorage.getInstance();

                                          StorageReference imageRef = storage.getReference().child("images/" + image.getLastPathSegment()) ;
                                          UploadTask uploadTask = imageRef.putFile(image);

                                            // Register observers to listen for when the download is done or if it fails
                                          uploadTask.addOnFailureListener(new OnFailureListener() {
                                              @Override
                                              public void onFailure(@NonNull Exception exception) {
                                                  // Handle unsuccessful uploads
                                                  String error = exception.getLocalizedMessage();
                                              }
                                          }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                              @Override
                                              public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                  // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                                                  // ...
                                                  imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                      @Override
                                                      public void onSuccess(Uri uri)
                                                      {
                                                          String strPName = txtName.getText().toString();
                                                          String strDescription = txtDescription.getText().toString();
                                                          String strPrice = txtPrice.getText().toString();
                                                          String strStock = txtStock.getText().toString();
                                                          String strGoal = txtGoal.getText().toString();
                                                          String strDate = txtDate.getText().toString();
                                                          int iStock = Integer.parseInt(strStock);
                                                          int iGoal = Integer.parseInt(strGoal);
                                                          int iPrice = Integer.parseInt(strPrice);
                                                          //String strImage = txtImage.getText().toString();
                                                          String strImage = uri.toString();
                                                          StorageReference imageRef = storage.getReferenceFromUrl(strImage + "");
                                                          //to get user id
                                                          //String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                                          //ItemModel model = new ItemModel(strName, strDescription, strPrice, strStock, strGoal, strDate, strImage, user_id);
                                                          //ItemModel model = new ItemModel(strDescription, imageRef, iGoal, iStock, iPrice);
                                                          FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                          DatabaseReference myRef = database.getReference(strName + "/category/"+ strCategory +"/"+ strPName);
                                                          DatabaseReference myDescriptionRef = database.getReference(strName + "/category/"+ strCategory +"/"+ strPName+"/description");
                                                          myDescriptionRef.setValue(strDescription);
                                                          //to save data to database, will push data to firebase
                                                          DatabaseReference myGoalRef = database.getReference(strName + "/category/"+ strCategory +"/"+ strPName+"/goal");
                                                          myGoalRef.setValue(iGoal);
                                                          DatabaseReference myImageRef = database.getReference(strName + "/category/"+ strCategory +"/"+ strPName+"/image");
                                                          myImageRef.setValue(strImage);
                                                          DatabaseReference myPriceRef = database.getReference(strName + "/category/"+ strCategory +"/"+ strPName+"/price");
                                                          myPriceRef.setValue(iPrice);
                                                          DatabaseReference myStockRef = database.getReference(strName + "/category/"+ strCategory +"/"+ strPName+"/stock");
                                                          myStockRef.setValue(iStock);

                                                          //myRef.child(new Date().getTime() + "").setValue(model);

                                                          Toast.makeText(com.example.the_shopping_tracker.AddItems.this,"Item : "  + strPName + " - saved!",
                                                                  Toast.LENGTH_SHORT).show();


                                                      }
                                                  });

                                              }
                                          });

                                      }
                                  }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            image = data.getData();
            btnImage.setImageURI(image);

        }
    }
    //https://github.com/Dhaval2404/ImagePicker
    //https://github.com/bumptech/glide
    //https://firebase.google.com/docs/reference/unity/class/firebase/storage/storage-exception

    //uploading img
    //https://firebase.google.com/docs/storage/android/upload-files#upload_from_a_local_file
    private void selectImage()
    {
        ImagePicker.with(this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }

    private void showCalendar() {
        final DatePickerDialog calendar = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                txtDate.setText(month + "/" + dayOfMonth + "/" + year);
            }
        }, 2021, 0, 0);
        calendar.show();
    }
    //https://www.youtube.com/watch?v=yp0ZahAXbzo



    @Override
    public void setTitle(CharSequence title)
    {
        super.setTitle(title);
        TextView tv = new TextView(this);
        tv.setText(title);
    }


}


