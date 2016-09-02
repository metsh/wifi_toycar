package net.shrarm.WiFiCar;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import android.widget.TextView;

import net.shrarm.tes.R;

public class MainActivity extends AppCompatActivity {

    private char LastCmd = '\0';
    private boolean connect = false;
    Button Lb;
    Button Rb;
    Button Ub;
    Button Db;
    SeekBar Sp;
    SeekBar To;
    TextView tw3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        Lb = (Button) findViewById(R.id.buttonL);
        Rb = (Button) findViewById(R.id.buttonR);
        Db = (Button) findViewById(R.id.buttonD);
        Ub = (Button) findViewById(R.id.buttonU);
        Sp =(SeekBar) findViewById(R.id.seekBar);
        To = (SeekBar) findViewById(R.id.seekBar2);
        tw3 = (TextView)findViewById(R.id.textView3);


        View.OnClickListener Listen = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.buttonD: send("b ");  break;
                    case R.id.buttonU: send("f ");  break;
                    case R.id.buttonR: send("r "); break;
                    case R.id.buttonL: send("l "); break;
                    default:break;
                }
            }

        };
        Lb.setOnClickListener(Listen);
        Rb.setOnClickListener(Listen);
        Ub.setOnClickListener(Listen);
        Db.setOnClickListener(Listen);
    }

    private void send(String arg)
    {

        String str = String.valueOf((short)(Sp.getProgress()*150/Sp.getMax() +105) )+" " +String.valueOf((short)(To.getProgress()*2000/Sp.getMax()));
        UdpSend sender = new UdpSend(tw3);
        sender.execute(arg + str, "3000");

    }

}
