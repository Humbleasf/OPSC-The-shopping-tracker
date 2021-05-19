package com.example.the_shopping_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity
{

    EditText Uname, Password;
    Button btnRegi, btnLog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Uname = findViewById(R.id.txtUserName);
        Password = findViewById(R.id.txtPass);
        btnRegi = findViewById(R.id.btnReg);
        btnLog = findViewById(R.id.btnLogin);

        btnRegi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(Login.this,Pop.class));
            }
        });
        btnLog.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                final String strName = Uname.getText().toString();
                final String strPass = Password.getText().toString();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference userRef = database.getReference().child(strName + "/Password");

                //reading from database
                userRef.addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot ds)
                    {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        String ValidPass = ds.getValue().toString();
                        //see if i can get login to happen in here

                        if(strPass.equals(ValidPass))
                        {
                            Toast.makeText(getApplicationContext(), "Enjoy your shopping " + strName,
                                    Toast.LENGTH_LONG).show();


                            startActivity(new Intent(Login.this, CategoryPage.class));
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "error, wrong password or username",
                                    Toast.LENGTH_LONG).show();
                        }

                        Log.d("Firebase", "Value is: " + ValidPass);
                    }
                    @Override
                    public void onCancelled(DatabaseError error)
                    {
                        // Failed to read value
                        Log.w("Firebase", "Failed to read value.", error.toException());
                    }
                });

            }

        });
        setTitle("The Shopping Tracker");
    }

    public void Exit(View v)
    {
        System.exit(0);
    }

    @Override
    public void setTitle(CharSequence title)
    {
        super.setTitle(title);

        TextView tv = new TextView(this);
        tv.setText(title);
    }

}