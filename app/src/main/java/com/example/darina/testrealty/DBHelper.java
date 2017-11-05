package com.example.darina.testrealty;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Darinya on 03.11.2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper dbHelper;

    private Context context;
    private int id_acc;
    private int id_realty;

    public void set_id_realty(int value)
    {
        id_realty = value;
    }

    public static DBHelper getInstance(Context context) {
        if (dbHelper == null) {
            dbHelper = new DBHelper(context.getApplicationContext(), "DBRealty" , null, 1);
        }
        return dbHelper;
    }

    private DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase dbRealty) {
        dbRealty.execSQL("CREATE TABLE IF NOT EXISTS Accounts " +
                "(_id integer PRIMARY KEY AUTOINCREMENT," +
                "        login text,    "   +
                "        password text, "   +
                "        status integer)"  );

        Cursor IfAccsNotExist= dbRealty.rawQuery("SELECT * FROM Accounts", new String[] {});
        if (IfAccsNotExist.getCount() == 0) {
            dbRealty.execSQL("INSERT INTO Accounts VALUES (1, 'User1', 123, 0)");
            dbRealty.execSQL("INSERT INTO Accounts VALUES (2, 'User2', 321, 0)");
        }

        dbRealty.execSQL("CREATE TABLE IF NOT EXISTS Realty " +
                "(_id integer PRIMARY KEY AUTOINCREMENT,    " +
                " address text,     " +
                " area integer,     " +
                " cost integer,     " +
                " floor integer,    " +
                " rooms integer,    " +
                " _id_user integer) ");

        Cursor IfRealtyNotExist = dbRealty.rawQuery("SELECT * FROM Realty", new String[] {});
        if (IfRealtyNotExist.getCount() == 0)
        {
            dbRealty.execSQL("INSERT INTO Realty VALUES (3, 'Перекопская 15А, кв. 111', 100, 5000000, 1, 1, 2)");
            dbRealty.execSQL("INSERT INTO Realty VALUES (4, 'Свободы 86, кв. 33', 40, 1600000, 9, 1, 1)");
            dbRealty.execSQL("INSERT INTO Realty VALUES (5, 'Мельникайте 110, кв. 55', 60, 3000000, 5, 2, 2)");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public int is_online() {
        SQLiteDatabase realtyDb = dbHelper.getReadableDatabase();
        Cursor findAccountOnline = realtyDb.query("Accounts", null, "status = ?", new String[]{"1"}, null, null, null);
        if (findAccountOnline != null && findAccountOnline.getCount() > 0)
        {
            findAccountOnline.moveToFirst();
            int ind_id = findAccountOnline.getColumnIndex("_id");
            int id = findAccountOnline.getInt(ind_id);
            id_acc = id;
            realtyDb.close();
            return id;
        }
        realtyDb.close();
        return -1;
    }

    public boolean auth(String login, String password) {
        SQLiteDatabase realtyDb = dbHelper.getWritableDatabase();
        Cursor findAccount = realtyDb.query("Accounts", null, "login = ? and password = ?", new String[]{login, password}, null, null, null);
        if (findAccount != null && findAccount.getCount() > 0)
        {
            findAccount.moveToFirst();
            int ind_id = findAccount.getColumnIndex("_id");
            int id = findAccount.getInt(ind_id);
            ContentValues contentValues = new ContentValues();
            contentValues.put("status", 1);
            realtyDb.update("Accounts", contentValues, "_id = ?", new String[] { String.valueOf(id) } );
            realtyDb.close();
            id_acc = id;
            return true;
        }
        realtyDb.close();
        return false;
    }

    public void logout() {
        SQLiteDatabase realtyDb = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", 0);
        realtyDb.update("Accounts", contentValues, "_id = ?", new String[]{String.valueOf(id_acc)});
        realtyDb.close();
        id_acc = -1;
    }

    public Cursor getAllRealty()
    {
        SQLiteDatabase realtyDb = dbHelper.getWritableDatabase();
        String sqlQuery = "select * from Realty;";
        return realtyDb.rawQuery(sqlQuery, null);
    }

    public Cursor getRealtyInfo()
    {
        SQLiteDatabase realtyDb = dbHelper.getReadableDatabase();
        String sqlQuery = "select * from Realty where _id = ?";
        return realtyDb.rawQuery(sqlQuery, new String[] { String.valueOf(id_realty) });
    }

    public boolean hasRights()
    {
        SQLiteDatabase realtyDb = dbHelper.getReadableDatabase();
        String sqlQuery = "select * from Realty where _id = ? and _id_user = ?";
        Cursor result = realtyDb.rawQuery(sqlQuery, new String[] { String.valueOf(id_realty), String.valueOf(id_acc) });

        if (result != null && result.getCount() > 0)
        return true;
        else return false;
    }

    public void addRealty(String address, int area, int cost, int rooms, int floor)
    {
        SQLiteDatabase realtyDb = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("address", address);
        contentValues.put("area", area);
        contentValues.put("cost", cost);
        contentValues.put("rooms", rooms);
        contentValues.put("floor", floor);
        contentValues.put("_id_user", id_acc);
        realtyDb.insert("Realty", null, contentValues);
        realtyDb.close();
    }
    public void editRealty(int area, int cost, int rooms, int floor)
    {
        SQLiteDatabase realtyDb = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("area", area);
        contentValues.put("cost", cost);
        contentValues.put("rooms", rooms);
        contentValues.put("floor", floor);
        realtyDb.update("Realty", contentValues, "_id = ?", new String[]{String.valueOf(id_realty)} );
        realtyDb.close();
    }
    public void deleteRealty()
    {
        SQLiteDatabase realtyDb = dbHelper.getWritableDatabase();
        realtyDb.delete("Realty", "_id = ?", new String[]{String.valueOf(id_realty)} );
    }
}

