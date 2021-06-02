package com.example.the_shopping_tracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.storage.StorageReference;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Shopping_Cart extends AppCompatActivity {
    List<Shopping_Details> lstShopping;
    List<String> lDescription = new ArrayList<String>();
    HashMap<String,String> hmCategory = new HashMap<String, String>();
    HashMap<String,String> hmCItem = new HashMap<String, String>();
    HashMap<String,Long> hmCItem2 = new HashMap<String, Long>();
    String[] strCKey = new String[999];
    String[] strItemKey = new String[999];
    String[] strItem = new String[999];
    String strName;
    StorageReference iImageRef;
    int iToBuy;
    int d = 0;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping__cart);

        if(lstShopping != null) {
            lstShopping.clear();
        }
        setTitle("Shopping List");
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) { //96450314-94CC-426A-A113-09E6A058B6C0
            mAuth.signInWithCustomToken("96450314-94CC-426A-A113-09E6A058B6C0");
        } else {
            signInAnonymously();
        }
        Intent IShopping = getIntent();
        strName = IShopping.getExtras().getString("Name");
        lstShopping = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        DatabaseReference cateRef = database.getReference().child(strName+"/category/" );
        cateRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                for (int a = 0; a < 40; a++) {
                    final int x = a;
                    {
                        //StorageReference gsReference = storage.getReferenceFromUrl("gs://the-shopping-tracker.appspot.com/download.jpg");
                        //This method is called once with the initial value and again
                        //whenever data at this location is updated.
                        hmCategory = (HashMap<String, String>) ds.getValue();
                        //hCategory.values().toArray(strCValue);
                        //children = (int)ds.getChildrenCount();
                        hmCategory.keySet().toArray(strCKey);
                        Long lCCount = ds.getChildrenCount();
                        int iCCount = lCCount.intValue();
                        if (iCCount <= x ) {
                            break;
                        }
                        DatabaseReference cChildrenRef = database.getReference().child(strName + "/category/" + strCKey[x] + "/");
                        cChildrenRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dsItem) {
                                hmCItem = (HashMap<String, String>)  dsItem.getValue();
                                hmCItem.keySet().toArray(strItemKey);
                                Long lChildCount = dsItem.getChildrenCount();
                                final int iChildCount = lChildCount.intValue() - 1;
                                for (int q = 0; q < strItemKey.length; q++)
                                {
                                    if(strItemKey[q] == null) {
                                        strItemKey[q] = "";
                                    }
                                }
                                int b = 0;

                                    for (int q = 0; q < strItemKey.length; q++) {
                                        if (!strItemKey[q].equals("Image") && !strItemKey[q].equals("")) {
                                            strItem[b] = strItemKey[q];
                                            b++;
                                        }
                                    }
                                for( int k = 0; k < strItem.length; k++)
                                {
                                    for (int q = 0; q < strItem.length; q++)
                                    {
                                        if(strItem[q] == null) {
                                            strItem[q] = "";
                                        }
                                    }
                                    if (strItem[k].equals(""))
                                    {
                                        continue;
                                    }
                                    if (lDescription.contains(strItem))
                                    {
                                        continue;
                                    }
                                    lDescription.add(strItem[k]);
                                    DatabaseReference cCItemRef = database.getReference().child(strName + "/category/" + strCKey[x] + "/" + strItem[k]);
                                    cCItemRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dsItemCount) {
                                            hmCItem2 = (HashMap<String, Long>) dsItemCount.getValue();
                                            if(hmCItem2 != null) {
                                                String strProgress = String.valueOf(hmCItem2.get("stock"));
                                                String strGoal = String.valueOf(hmCItem2.get("goal"));
                                                String strIValue = String.valueOf(hmCItem2.get("image"));
                                                String strDescription = String.valueOf(hmCItem2.get("description"));
                                                String strPrice = "R " + String.valueOf(hmCItem2.get("price"));

                                                int iGoal = Integer.parseInt(strGoal);
                                                int iProgress = Integer.parseInt(strProgress);
                                                iToBuy = iGoal - iProgress;
                                                if (iToBuy != 0) {
                                                    try {
                                                        StorageReference imageRef = storage.getReferenceFromUrl(strIValue + "");
                                                        //Log.d("firebase", "key imageRef:  " + imageRef);

                                                        lstShopping.add(new Shopping_Details(lDescription.get(d), imageRef, strPrice + "  " + strDescription, iToBuy));

                                                        RecyclerView myRecycleView = (RecyclerView) findViewById(R.id.recyclershoppingview_id2);
                                                        Shopping_List_Adapter myAdapter = new Shopping_List_Adapter(Shopping_Cart.this, lstShopping);
                                                        myRecycleView.setLayoutManager(new GridLayoutManager(Shopping_Cart.this, 1));
                                                        myRecycleView.setAdapter(myAdapter);
                                                    } catch (Exception e) {

                                                    }
                                                }
                                                d++;
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                        }
                                    });
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled (DatabaseError error){
            }
        });

        /*
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Home = new Intent(Shopping_Cart.this, CategoryPage.class);
                Home.putExtra("Name", strName);
                startActivity(Home);
            }
        });*/




    }
    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(this, new  OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // do your stuff
            }
        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d( "signInAnonymously:FAILURE", ""+exception);
                    }
                });
    }

    public void ShoppingPage(View v)
    {
        Intent shopping = new Intent(Shopping_Cart.this, Shopping_Cart.class);
        shopping.putExtra("Name", strName);
        startActivity(shopping);
    }

    public void Profile(View v)
    {
        Intent profile = new Intent(Shopping_Cart.this, Profile.class);
        profile.putExtra("getName", strName);
        startActivity(profile);
    }

    //navigation buttons
    public void HomePage(View v)
    {
        Intent homepage = new Intent(Shopping_Cart.this, CategoryPage.class);
        homepage.putExtra("Name", strName);
        startActivity(homepage);
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
        Intent logout = new Intent(Shopping_Cart.this, Login.class);
        logout.putExtra("getName", strName);
        finish();
        startActivity(logout);
    }

}