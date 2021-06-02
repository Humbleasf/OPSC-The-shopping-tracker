package com.example.the_shopping_tracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemList extends AppCompatActivity {
    List<ItemModel> lstItems;
    List<String> lDescription = new ArrayList<String>();
    HashMap<String,String> hmCItem = new HashMap<String, String>();
    HashMap<String,Long> hmCItem2 = new HashMap<String, Long>();
    String[] strItemKey = new String[999];
    String[] strItem = new String[999];
    String strName;
    String strCategory;
    FloatingActionButton fltAddItem;
    RecyclerView rvItemList;
    List<ItemModel> itemList = new ArrayList<>();
    ItemListAdapter itemListAdapter;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemlist);
        Intent item = getIntent();
        strName = item.getExtras().getString("Name");
        strCategory = item.getExtras().getString("Category");
        fltAddItem = findViewById(R.id.fltAddItem);
        rvItemList = findViewById(R.id.rvItemList);
        //String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        lstItems = new ArrayList<>();
        setTitle("Items Currently In Stock");
        if(lstItems != null) {
            lstItems.clear();
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        DatabaseReference itemRef = database.getReference().child(strName+"/category/" + strCategory +"/");

        itemRef.addValueEventListener(new ValueEventListener() {
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
                                    final int d = k;
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
                                    if (lDescription.contains(strItem[k]))
                                    {
                                        continue;
                                    }
                                    lDescription.add(strItem[k]);
                                    try {
                                        DatabaseReference cCItemRef = database.getReference().child(strName + "/category/" + strCategory + "/" + strItem[k]);
                                        cCItemRef.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dsItemCount) {
                                                hmCItem2 = (HashMap<String, Long>) dsItemCount.getValue();
                                                String strProgress = String.valueOf(hmCItem2.get("stock"));
                                                String strGoal = String.valueOf(hmCItem2.get("goal"));
                                                String strIValue = String.valueOf(hmCItem2.get("image"));
                                                String strDescription = String.valueOf(hmCItem2.get("description"));
                                                String strPrice = String.valueOf(hmCItem2.get("price"));
                                                //Log.d("firebase", "key strIValue:  " + strIValue);
                                                int iGoal = Integer.parseInt(strGoal);
                                                int iProgress = Integer.parseInt(strProgress);
                                                int iPrice = Integer.parseInt(strPrice);
                                                StorageReference imageRef = storage.getReferenceFromUrl(strIValue + "");


                                                    lstItems.add(new ItemModel( strItem[d], strDescription, imageRef, iGoal , iProgress, iPrice));
                                                    Log.d("firebase", "key strIValue:  " + lstItems.get(0)  );
                                                    RecyclerView itemRecycleView = (RecyclerView) findViewById(R.id.rvItemList);

                                                    ItemListRecycler listAdapter = new ItemListRecycler(ItemList.this, lstItems);

                                                    itemRecycleView.setLayoutManager(new GridLayoutManager(ItemList.this, 1));
                                                    itemRecycleView.setAdapter(listAdapter);


                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                            }
                                        });
                                    }
                                    catch (Exception e)
                                    {

                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });






        /*rvItemList.setLayoutManager(new LinearLayoutManager(this));
        //to call on array
        itemListAdapter = new ItemListAdapter(itemList);
        rvItemList.setAdapter(itemListAdapter);*/
        /*getData();*/


        fltAddItem.setOnClickListener(new View.OnClickListener() {
                                          public void onClick(View v)
                                          {
                                              Intent addItem = new Intent(com.example.the_shopping_tracker.ItemList.this, AddItems.class);
                                              addItem.putExtra("Name", strName);
                                              addItem.putExtra("Category", strCategory);
                                              startActivity(addItem);
                                          }
                                      }
        );
    }

    /*private void getData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Intent item = getIntent();
        strName = item.getExtras().getString("Name");
        strCategory = item.getExtras().getString("Category");
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        database.getReference("item").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemList.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    try
                    {

                        ItemModel itemModel = dataSnapshot.getValue(ItemModel.class);

                        if (itemModel.getUser_id().equals(uid))
                        {
                            itemList.add(itemModel);
                        }

                    }
                    catch (Exception e)
                    {

                    }


                }
                itemListAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/
    //sort out
    @Override
    public void setTitle(CharSequence title)
    {
        super.setTitle(title);
        TextView tv = new TextView(this);
        tv.setText(title);
    }

    public void Logout(View view)
    {
        //FirebaseAuth.getInstance().signOut();
        finish();
        //to kill current process
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
    public void ShoppingPage(View view)
    {
        Intent shopping = new Intent(ItemList.this, Shopping_Cart.class);
        shopping.putExtra("Name", strName);
        startActivity(shopping);
    }

    public void Profile(View view)
    {
        Intent profile = new Intent(ItemList.this, Profile.class);
        profile.putExtra("getName", strName);
        startActivity(profile);
    }

    //navigation buttons
    public void HomePage(View v)
    {
        Intent homepage = new Intent(ItemList.this, CategoryPage.class);
        Log.d("firebase", "key strName:  " + strName);
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
        Intent logout = new Intent(ItemList.this, Login.class);
        logout.putExtra("getName", strName);
        finish();
        startActivity(logout);
    }


}
