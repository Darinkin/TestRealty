package com.example.darina.testrealty;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;
import android.widget.ListView;
import android.widget.Toast;

import static com.example.darina.testrealty.R.color.darkGreen;
import static com.example.darina.testrealty.R.color.lightGreen;

public class ObjectsListActivity extends AppCompatActivity {

    SimpleCursorAdapter scAdapter;
    ListView listViewData;
    int currentRealtyPosition;
    int currentRealtyId;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_objects);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Недвижимость");

        updateListView();

        listViewData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int position, long id)
            {
                currentRealtyId = (int) id;
                DBHelper.getInstance(ObjectsListActivity.this).set_id_realty(currentRealtyId);
                Intent intObj = new Intent(ObjectsListActivity.this, ViewRealtyActivity.class);
                startActivity(intObj);
            }
        });
        listViewData.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> av, View v, int position, long id)
            {
                if (currentRealtyPosition == (int) position)
                {
                    v.setBackgroundColor(getResources().getColor(lightGreen));
                    currentRealtyPosition = -1;
                    menu.findItem(R.id.edit_item).setVisible(false);
                    menu.findItem(R.id.delete_item).setVisible(false);
                    return true;
                }
                else if (currentRealtyPosition!=-1) listViewData.getChildAt(currentRealtyPosition).setBackgroundColor(getResources().getColor(lightGreen));
                v.setBackgroundColor(getResources().getColor(darkGreen));
                currentRealtyPosition = (int) position;
                currentRealtyId = (int) id;
                DBHelper.getInstance(ObjectsListActivity.this).set_id_realty((int)id);

                if (DBHelper.getInstance(ObjectsListActivity.this).hasRights())
                {
                    menu.findItem(R.id.edit_item).setVisible(true);
                    menu.findItem(R.id.delete_item).setVisible(true);
                }
                else
                {
                    menu.findItem(R.id.edit_item).setVisible(false);
                    menu.findItem(R.id.delete_item).setVisible(false);
                }
                return true;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_objects_menu, menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            updateListView();
            menu.findItem(R.id.edit_item).setVisible(false);
            menu.findItem(R.id.delete_item).setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_item:
            {
                Intent intObj = new Intent(ObjectsListActivity.this, AddRealtyActivity.class);
                startActivity(intObj);
                return true;
            }
            case R.id.edit_item:
            {
                if (currentRealtyPosition > -1)
                {
                    Intent intObj = new Intent(ObjectsListActivity.this, EditRealtyActivity.class);
                    startActivityForResult(intObj, 1);
                }
                return true;
            }
            case R.id.delete_item:
            {
                if (currentRealtyPosition > -1)
                {
                    DBHelper.getInstance(ObjectsListActivity.this).deleteRealty();

                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Пам! Удалили объект №" + String.valueOf(currentRealtyId),
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                    updateListView();
                    menu.findItem(R.id.edit_item).setVisible(false);
                    menu.findItem(R.id.delete_item).setVisible(false);
                }
                return true;
            }
            case R.id.logout_item:
            {
                DBHelper.getInstance(ObjectsListActivity.this).logout();
                Intent intObj = new Intent(ObjectsListActivity.this, LoginActivity.class);
                startActivity(intObj);
                return true;
            }
            default:
                return false;
        }
    }

    public void updateListView() {
        Cursor cursor = DBHelper.getInstance(ObjectsListActivity.this).getAllRealty();
        currentRealtyPosition = -1;

        cursor.moveToFirst();
        String[] from = new String[] { "address", "area", "cost"};
        int[] to = new int[] {R.id.item_headerText , R.id.item_subHeaderText, android.R.id.text1};
        scAdapter = new SimpleCursorAdapter(this, R.layout.activity_list_objects, cursor, from, to);
        listViewData = (ListView) findViewById(android.R.id.list);
        listViewData.setAdapter(scAdapter);

    }
    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
