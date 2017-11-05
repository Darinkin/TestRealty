package com.example.darina.testrealty;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddRealtyActivity extends AppCompatActivity {

    EditText Address, Area, Cost, Rooms, Floor;
    Button Add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_realty);

        Add = (Button) findViewById(R.id.button2);
        Add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (check())
                {
                    String address = Address.getText().toString();
                    int area = Integer.valueOf(Area.getText().toString());
                    int cost = Integer.valueOf(Cost.getText().toString());
                    int rooms = Integer.valueOf(Rooms.getText().toString());
                    int floor = Integer.valueOf(Floor.getText().toString());

                    DBHelper.getInstance(AddRealtyActivity.this).addRealty(address, area, cost, rooms, floor);
                    finish();
                }
            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Новый объект");
    }

    public boolean check()
    {
        Address = (EditText) findViewById(R.id.editText);
        Area = (EditText) findViewById(R.id.editText6);
        Cost = (EditText) findViewById(R.id.editText8);
        Rooms = (EditText) findViewById(R.id.editText10);
        Floor = (EditText) findViewById(R.id.editText14);

        try {

        String address = Address.getText().toString();
        int area = Integer.valueOf(Area.getText().toString());
        int cost = Integer.valueOf(Cost.getText().toString());
        int rooms = Integer.valueOf(Rooms.getText().toString());
        int floor = Integer.valueOf(Floor.getText().toString());


            if ((address.length() < 5) || (area < 10) || (cost < 100000) || (rooms < 1) || (floor < 0))
            {
                showErrorMessage();
                return false;
            }
            else
            {
                showSuccessMessage();
                return true;
            }
        }
        catch (Exception e)
        {
            showErrorMessage();
            return false;
        }


    }

    public void showErrorMessage()
    {
        Toast toast = Toast.makeText(getApplicationContext(),
                "Заполните корректно поля",
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
    public void showSuccessMessage() {
        Toast toast = Toast.makeText(getApplicationContext(),
                "Объект успешно добавлен",
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}


