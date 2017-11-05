package com.example.darina.testrealty;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class ViewRealtyActivity extends AppCompatActivity {

    TextView Address, Area, Cost, Rooms, Floor, CostPerMeter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_realty);

        showInfo();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Просмотр объекта");
    }

    public void showInfo()
    {
        Cursor realtyInfo = DBHelper.getInstance(ViewRealtyActivity.this).getRealtyInfo();
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
            Area = (TextView) findViewById(R.id.editText6);
            Cost = (TextView) findViewById(R.id.editText8);
            Rooms = (TextView) findViewById(R.id.editText10);
            Floor = (TextView) findViewById(R.id.editText14);
            CostPerMeter = (TextView) findViewById(R.id.editText12);

            // Заполнение
            Address.setText(address);
            Area.setText(String.valueOf(area));
            Cost.setText(String.valueOf(cost));
            Rooms.setText(String.valueOf(rooms));
            Floor.setText(String.valueOf(floor));
            CostPerMeter.setText(String.valueOf(cost / area));
        }
    }
}
