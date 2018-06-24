package com.hack.myapp.hack;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity  implements LocationListener {

    Button btn,btn2;
    DatabaseHandler dh;
    TextView txt;

    boolean flag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn=(Button)findViewById(R.id.buttonAdd);
        btn2=(Button)findViewById(R.id.button2);
        txt= (TextView) findViewById(R.id.textView);

        CountDownTimer c=new CountDownTimer(3000,1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                Intent i=new Intent(getApplicationContext(),AddLoc.class);
                startActivity(i);

            }
        }.start();

        dh=new DatabaseHandler(getApplicationContext());
        dh.getLocation();
        LocationManager locationManager=(LocationManager)this.getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(getApplicationContext(),"problem",Toast.LENGTH_SHORT).show();

        }
        try{
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 0.1f, this);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1,0.1f,this);
        }
        catch(SecurityException e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),AddLoc.class);
                startActivity(i);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dh.getLocation();
                flag=true;

                Intent i=new Intent(getApplicationContext(),Sym.class);
                startActivity(i);


            }
        });
        btn2.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view) {

                dh.reset();
                return true;

            }
        });

    }

    @Override
    public void onLocationChanged(Location location) {
        try {

            //txt.append(String.valueOf(location.getLatitude())+" "+String.valueOf(location.getLongitude()));

            if(!flag) {
                if (dh.inDistance(location.getLatitude(), location.getLongitude(),txt) == 1) {
                    Toast.makeText(getApplication(), "OYYYYYYYY", Toast.LENGTH_SHORT).show();
                }
                else{
                    //txt.append("\nh\n");
                }
            }
        }
        catch(Exception e){
            txt.setText(e.getMessage());
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
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
