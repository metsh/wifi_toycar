package net.shrarm.tes;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import android.widget.TextView;
public class MainActivity extends AppCompatActivity {

    private char LastCmd = '\0';
    private boolean connect = false;
    Button Lb;
    Button Rb;
    Button Ub;
    Button Db;
    Button Dst;
    SeekBar Sp;
    SeekBar To;
    TextView tw3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Lb = (Button) findViewById(R.id.buttonL);
        Rb = (Button) findViewById(R.id.buttonR);
        Db = (Button) findViewById(R.id.buttonD);
        Ub = (Button) findViewById(R.id.buttonU);
        Sp =(SeekBar) findViewById(R.id.seekBar);
        To = (SeekBar) findViewById(R.id.seekBar2);
        tw3 = (TextView)findViewById(R.id.textView3);
        Dst = (Button) findViewById(R.id.button);

        View.OnClickListener Listen = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.buttonD: send("b ");  break;
                    case R.id.buttonU: send("f ");  break;
                    case R.id.buttonR: send("r "); break;
                    case R.id.buttonL: send("l "); break;
                    case R.id.button: send("d "); break;
                    default:break;
                }
            }

        };
        Lb.setOnClickListener(Listen);
        Rb.setOnClickListener(Listen);
        Ub.setOnClickListener(Listen);
        Db.setOnClickListener(Listen);
        Dst.setOnClickListener(Listen);
    }

    private void send(String arg)
    {

        String str = String.valueOf((short)(Sp.getProgress()*150/Sp.getMax() +105) )+" " +String.valueOf((short)(To.getProgress()*2000/Sp.getMax()));
        UdpSend sender = new UdpSend(tw3);
        sender.execute(arg + str, "3000");

    }

}
