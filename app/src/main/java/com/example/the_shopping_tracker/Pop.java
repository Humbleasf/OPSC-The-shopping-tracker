package com.example.the_shopping_tracker;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//How to create pop up window in android. 2015. Youtube video, added by Vujovic, F.
// [Online]. Available at: https://www.youtube.com/watch?v=fn5OlqQuOCk&t=211s [Accessed 14 May 2021]
public class Pop extends Activity//go to manifest and type in <activity name = "pop"/> to incluse this
{

    EditText name,password;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_popup);

        name = (EditText) findViewById(R.id.txtRegName);
        password = findViewById(R.id.txtPass);
        btnRegister = findViewById(R.id.btnRegUser);

        //below metrics get the size of the curret display pixels of the device being used
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        //setting the width and height of the display into variables
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.9),(int) (height * 0.5));
        //we cast to an int so we can use it in calculations, we say 0.5 to make the width = 0.5 of current display

        /*with the above we can have a popup, but it will make the background black.
         * we fix this by going to the left hand files and say values->Themes.xml and create a new theme called popTheme
         * we then go to manifest and create a android:theme = "@style/AppTheme.popTheme" to include the new style*/
        setTitle("Register");

        btnRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String strName = name.getText().toString();
                String strPass = password.getText().toString();

                if(strName.equals("") || strPass.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Please enter user name and password ", Toast.LENGTH_LONG).show();
                }
                else
                {
                    // Write a message to the database
                    FirebaseDatabase database = FirebaseDatabase.getInstance();

                    DatabaseReference userNameRef = database.getReference().child("" + strName);//i still need to make a firebase database for this, i know how to just busy with other things atm
                    userNameRef.setValue(strPass);
                }
            }
        });
    }
    //overide method to change the title of the pop up window
    @Override
    public void setTitle(CharSequence title)
    {
        super.setTitle(title);

        TextView tv = new TextView(this);
        tv.setText(title);
    }
}
