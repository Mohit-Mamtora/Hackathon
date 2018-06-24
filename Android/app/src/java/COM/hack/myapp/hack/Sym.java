package com.hack.myapp.hack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Sym extends AppCompatActivity {


    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sym);
    }


    public void clkk(View v){



        i= new Intent(getApplicationContext(),AddLoc.class);
        switch (v.getId()){

            case R.id.i1:
                i.putExtra("img","1");
                break;
            case R.id.i2:
                i.putExtra("img","2");
                break;
            case R.id.i3:
                i.putExtra("img","3");
                break;
            case R.id.i4:
                i.putExtra("img","4");
                break;
    /*      case R.id.i5:
              i.putExtra("img","5");
              break;
          case R.id.i6:
              i.putExtra("img","6");
              break;
          case R.id.i7:
              i.putExtra("img","7");
              break;
          case R.id.i8:
              i.putExtra("img","8");
              break;
          case R.id.i9:
              i.putExtra("img","9");
              break;
          case R.id.i10:
              i.putExtra("img","10");
              break;
          case R.id.i11:
              i.putExtra("img","11");
              break;
*/
        }
        startActivity(i);


    }
}
