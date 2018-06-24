package com.hack.myapp.hack;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;

/**
 * Created by unknown on 13/3/18.
 */



public class Download extends AsyncTask<String, String, String> {



        Context context;
        public Download(Context context){
            this.context=context;
        }

    @Override
    protected String doInBackground(String... strings) {

//        MYDB mydb=new MYDB(context);
  //      SQLiteDatabase sqLiteDatabase=mydb.getWritableDatabase();
        StringBuilder temp=new StringBuilder();


        URL url= null;
        try {
            url = new URL( "https://aitsmca.000webhostapp.com/index.php?id=1&sym_id=101&longt=10.253555&latt=10.253555&city=rajkot&apt=10.253555");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        String str= null;
        try {
            HttpURLConnection urlConnection= (HttpURLConnection) url.openConnection();
            BufferedReader reader=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            while ((str=reader.readLine())!=null){
                temp.append(str);

            }


        } catch (IOException e) {
            e.printStackTrace();
        }


        String data[] =temp.toString().split(":");
        String t = null;

        return temp.toString();
    }


    @Override
    protected void onPostExecute(String s) {
        Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
    }
}
