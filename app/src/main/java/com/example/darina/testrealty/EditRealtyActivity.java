package com.example.darina.testrealty;

import android.database.Cursor;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditRealtyActivity extends AppCompatActivity {

    EditText Area, Cost, Rooms, Floor;
    TextView Address;
    Button Edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_realty);

        showInfo();

        Edit = (Button) findViewById(R.id.button2);
        Edit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (check())
                {
                    int area = Integer.valueOf(Area.getText().toString());
                    int cost = Integer.valueOf(Cost.getText().toString());
                    int rooms = Integer.valueOf(Rooms.getText().toString());
                    int floor = Integer.valueOf(Floor.getText().toString());

                    DBHelper.getInstance(EditRealtyActivity.this).editRealty(area, cost, rooms, floor);
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Редактирование");
    }

    public void showInfo()
    {
        Cursor realtyInfo = DBHelper.getInstance(EditRealtyActivity.this).getRealtyInfo();
        realtyInfo.moveToFirst();
        if (realtyInfo != null && realtyInfo.getCount() > 0) {
            // Узнаем номера колонок
            int addressColumn = realtyInfo.getColumnIndex("address");
            int areaColumn = realtyInfo.getColumnIndex("area");
            int costColumn = realtyInfo.getColumnIndex("cost");
            int roomsColumn = realtyInfo.getColumnIndex("rooms");
            int floorColumn = realtyInfo.getColumnIndex("floor");

            // Сохранение в переменные
            String address = realtyInfo.getString(addressColumn);
            int area = realtyInfo.getInt(areaColumn);
            int cost = realtyInfo.getInt(costColumn);
            int rooms = realtyInfo.getInt(roomsColumn);
            int floor = realtyInfo.getInt(floorColumn);

            // Привязка
            Address = (TextView) findViewById(R.id.editText);
            Area = (EditText) findViewById(R.id.editText6);
            Cost = (EditText) findViewById(R.id.editText8);
            Rooms = (EditText) findViewById(R.id.editText10);
            Floor = (EditText) findViewById(R.id.editText14);

            // Заполнение
            Address.setText(address);
            Area.setText(String.valueOf(area));
            Cost.setText(String.valueOf(cost));
            Rooms.setText(String.valueOf(rooms));
            Floor.setText(String.valueOf(floor));
        }
    }


    public boolean check()
    {
        try {

            int area = Integer.valueOf(Area.getText().toString());
            int cost = Integer.valueOf(Cost.getText().toString());
            int rooms = Integer.valueOf(Rooms.getText().toString());
            int floor = Integer.valueOf(Floor.getText().toString());


            if ((area < 10) || (cost < 100000) || (rooms < 1) || (floor < 0))
            {
                showErrorMessage();
                return false;
            }
            else
            {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Объект успешно отредактирован",
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
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
}
