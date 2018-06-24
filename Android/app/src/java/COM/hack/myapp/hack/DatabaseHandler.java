package com.hack.myapp.hack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by mohit on 8/10/17.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "GeoLocationManager";
    private Context context;
    private Cursor c;
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String q="CREATE TABLE GEO(" +
                " ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " LAT REAL NOT NULL," +
                " LOG REAL NOT NULL," +
                " ALT REAL NOT NULL," +
                " SYS_ID INTEGER NOT NULL)";
        sqLiteDatabase.execSQL(q);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS GEO");
        onCreate(sqLiteDatabase);
    }

    public long AddLocation(double lat,double log,double alt,int sys_id){
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues val=new ContentValues();
        val.put("LAT",lat);
        val.put("LOG",log);
        val.put("ALt",alt);
        val.put("SYS_ID",sys_id);
        long temp=db.insert("GEO",null,val);
        db.close();
        return temp;
    }
    public void reset(){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS GEO");
    }

    public void getLocation() {

//        String q = "SELECT ID, ( 5379 * acos( cos( radians(" + lat + ") ) * cos( radians( lat ) ) " +
//                "* cos( radians( LOG ) - radians(" + log + ") ) " +
//                "+ sin( radians(" + lat + ") ) * sin( radians( LAt ) ) ) ) " +
//                "AS distance FROM GEO HAVING distance > 1 ORDER BY distance";

        String q1 = "SELECT * FROM GEO";

        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery(q1, null);

        if(c.moveToFirst()){

            do{
               Toast.makeText(context,"id : "+c.getString(0)+" LAT : "+c.getString(1)+" LOG : "+c.getString(2)+" ALT : "+c.getString(3)+" ",
                       Toast.LENGTH_SHORT ).show();

            }while(c.moveToNext());
        }
        else{
            Toast.makeText(context,"EMPTY",Toast.LENGTH_LONG).show();
        }

    }
    public int inDistance(double Ulat,double Ulog,TextView txt){
        if(c.moveToFirst()) {
            do {

                double R = 6371e3; // metres

                double d = R * (2 * Math.atan2(Math.sqrt(Math.sin(Math.toRadians(Math.toRadians(Ulat)-Math.toRadians(Double.parseDouble(c.getString(1))))/2) * Math.sin(Math.toRadians(Math.toRadians(Ulat)-Math.toRadians(Double.parseDouble(c.getString(1))))/2) +
                        Math.cos(Math.toRadians(Ulat)) * Math.cos(Math.toRadians(Double.parseDouble(c.getString(1)))) *
                                Math.sin(Math.toRadians(Ulog-Double.parseDouble(c.getString(2)))/2) * Math.sin(Math.toRadians(Ulog-Double.parseDouble(c.getString(2)))/2)), Math.sqrt(1-Math.sin(Math.toRadians(Math.toRadians(Ulat)-Math.toRadians(Double.parseDouble(c.getString(1))))/2) * Math.sin(Math.toRadians(Math.toRadians(Ulat)-Math.toRadians(Double.parseDouble(c.getString(1))))/2) +
                        Math.cos(Math.toRadians(Ulat)) * Math.cos(Math.toRadians(Double.parseDouble(c.getString(1)))) *
                                Math.sin(Math.toRadians(Ulog-Double.parseDouble(c.getString(2)))/2) * Math.sin(Math.toRadians(Ulog-Double.parseDouble(c.getString(2)))/2))));

                //txt.append("\n"+String.valueOf(d)+"\n");
                txt.setText(String.valueOf(d));
//
//                txt.append("\n"+String.valueOf((Math.acos(
//                        Math.cos(Math.toRadians(Ulat))
//                                * Math.cos(Math.toRadians(Double.parseDouble(c.getString(1))))
//                                * Math.cos(Math.toRadians(Double.parseDouble(c.getString(2))) - Math.toRadians(Ulog))
//                                + Math.sin(Math.toRadians(Ulat)) * Math.sin(Math.toRadians(Double.parseDouble(c.getString(1))))) * 6379))+"\n");

//                if ((Math.acos(
//                        Math.cos(Math.toRadians(Ulat))
//                                * Math.cos(Math.toRadians(Double.parseDouble(c.getString(1))))
//                                * Math.cos(Math.toRadians(Double.parseDouble(c.getString(2))) - Math.toRadians(Ulog))
//                                + Math.sin(Math.toRadians(Ulat)) * Math.sin(Math.toRadians(Double.parseDouble(c.getString(1))))) * 6379) < 1) {
//                    //return 1;
//                }

            } while (c.moveToNext());
        }
        return 0;

    }
}
