package com.example.riverconditionsreporter;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Blob;

public class DataManager {
    private SQLiteDatabase db;

    public DataManager(Context context) {

        MySQLiteOpenHelper helper = new MySQLiteOpenHelper(context);
        db = helper.getWritableDatabase();
        //db.delete("favorites", null, null);
    }

    public Cursor selectAll() {

        Cursor cursor = null;
        String query = "select * from favorites order by name";

        try {

            cursor = db.rawQuery(query, null);
            //Log.i ("info", "In Datamanager selectAll try statement");
        } catch (Exception e) {
            Log.i("info", "In DataManager selectAll method");
            Log.i("info", e.getMessage());
        }

        Log.i("info", "Loaded data " + cursor.getCount());
        return cursor;
    }


    public void insert (String id, String name, String category, String agency, Double longitude, Double latitude,
                        String url, int favorite, int _index) {
        String query = "insert into favorites" +
                "(id, name, category, agency, longitude, latitude, url, favorite, _index) values " +
                "('" + id + "', '" + name + "', '" + category + "' , '" + agency +
                "' , '" + longitude + "' , '" + latitude + "', '" + url + "', '"+ favorite + "', '" + _index + "')";

        try {

            db.execSQL(query);
        } catch (SQLException e) {
            Log.i("info", "In DataManager insert method");
            Log.i("info", e.getMessage());
        }
        Log.i("info", "Added new favorite " + name);
    }

    public void deleteFavorite(River river) {

        try {
            String query = "DELETE FROM " + "favorites" +  " WHERE " + "name" +
                    " = '" + river.getName() + "';";
            Log.i("delete() = ", query);
            db.execSQL(query);
        } catch (SQLException e) {
            Log.i("info", "In DataManager deleteFavorite method");
            Log.i("info", e.getMessage());
        }
        Log.i("info", "Removed favorite " + river.getName());

        //db.close();

    }

    public Cursor contains(String aName ) {
        Cursor cursor = null;
        try {
            String query =  "SELECT EXISTS (SELECT 1 FROM favorites WHERE name = aName)";
            cursor = db.rawQuery(query, null);
        } catch(SQLException e) {
            Log.i("info", "In DataManager contains method");
            Log.i("info", e.getMessage());
        }
        return cursor;
    }

    private class MySQLiteOpenHelper extends SQLiteOpenHelper {
        public MySQLiteOpenHelper (Context context) {
            super(context, "favorites", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            String newTable = "create table favorites ("
                    + "id text,"
                    + "name text not null,"
                    + "category text,"
                    + "agency text,"
                    + "longitude real,"
                    + "latitude real,"
                    + "url text,"
                    + "favorite integer,"
                    + "_index integer)";





            try {
                db.execSQL(newTable);
            }
            catch (SQLException e) {
                Log.i ("info", "In MySQLiteOpenHelper class onCreate Method");
                Log.i ("info", e.getMessage());
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }
}
