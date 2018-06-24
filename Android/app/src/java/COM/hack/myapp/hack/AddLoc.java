package com.hack.myapp.hack;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.accessibility.AccessibilityManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;

public class AddLoc extends AppCompatActivity implements LocationListener {

    TextView lat,log,alt,bearing,vi;
    Button btn1,btn2;
    EditText city,num;
    DatabaseHandler db;
    double ln;
    double lg;
    WebView web;
    volatile boolean flag=false;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_loc);

web=(WebView)findViewById(R.id.web);
        lat=(TextView)findViewById(R.id.textView2);
        log=(TextView)findViewById(R.id.textView4);
        city=(EditText)findViewById(R.id.city);
        num=(EditText)findViewById(R.id.num);

        alt=(TextView)findViewById(R.id.textView6);
        bearing=(TextView)findViewById(R.id.textView8);
        db = new DatabaseHandler(this);
        vi= (TextView) findViewById(R.id.textView9);

        try{
            id=Integer.parseInt(getIntent().getExtras().getString("img"));

            Toast.makeText(this, ""
                    +id, Toast.LENGTH_SHORT).show();


        }catch (Exception e){

        }


        btn1=(Button)findViewById(R.id.button);

        btn2=(Button)findViewById(R.id.button1);

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             vi.append("\nanswer Lat :"+(Double.parseDouble(lat.getText().toString())-ln));
        vi.append("\nanswer Log :"+(Double.parseDouble(log.getText().toString()) -lg) );
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            vi.append("1) LAt"+lat.getText().toString()+"\n log"+log.getText().toString()+"\n"+"");
            ln=Double.parseDouble(lat.getText().toString());
            lg=Double.parseDouble(log.getText().toString());

            new Thread(new Runnable() {
                @Override
                public void run() {


                  Download  download=new Download(getApplicationContext());

                    String ur="https://aitsmca.000webhostapp.com/index.php?id=1&sym_id="
                            +id+"&longt="+Double.parseDouble(log.getText().toString())+"" +
                            "&latt="+Double.parseDouble(lat.getText().toString())+
                            "&city=rajkot&apt="
                            +Double.parseDouble(log.getText().toString())+"";
                   try {
                       URL url = new URL(ur);
                       // Send POST data request
                       URLConnection conn = url.openConnection();
                   }catch (Exception e){

                   }

                    //System.err.print(ur);
                    URL obj = null;
                    try {
                        obj = new URL(ur);
//                        obj=new URL("https://www.google.com");
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    HttpURLConnection con = null;


                    try {
                        con = (HttpURLConnection) obj.openConnection();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        con.setRequestMethod("GET");
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    }

                    try {
                        int responseCode = con.getResponseCode();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }).start();


            }
        });

        vi.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                vi.setText("");
                return true;
            }
        });

        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(getApplicationContext(),"problem",Toast.LENGTH_SHORT).show();

        }
        try{
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 0.1f, this);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1,0.1f,this);

        }
        catch(SecurityException e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }





    }


    public void btnclk(View v){

        web=(WebView)findViewById(R.id.web);

        web.getSettings().setJavaScriptEnabled(true);
        web.setWebChromeClient(new WebChromeClient());
        web.loadUrl("https://aitsmca.000webhostapp.com/index.php?id=1&sym_id="+id+"&longt="+log.getText()+"&latt="+lat.getText()+"&sym_side="+num.getText()+"&city="+city.getText()+"&apt="+alt.getText()+"");

    }

    @Override
    public void onLocationChanged(Location location) {


        lat.setText(String.valueOf(location.getLatitude()));
        log.setText(String.valueOf(location.getLongitude()));
        alt.setText(String.valueOf(location.getAltitude()));



        bearing.setText(String.valueOf(location.getBearing()));

        if(flag){

            if(db.AddLocation(location.getLatitude(),location.getLongitude(),location.getAltitude(),2)>0){
                Toast.makeText(getApplicationContext(),"True",Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getApplicationContext(),"False",Toast.LENGTH_LONG).show();
            }
            flag=false;
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
