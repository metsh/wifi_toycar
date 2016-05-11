
package net.shrarm.tes;

import java.net.*;
import android.app.Activity;


import android.os.AsyncTask;
;
import android.view.View;
import android.widget.TextView;


class UdpSend  extends  AsyncTask <String, Void, String>{



    private TextView tw;

    public UdpSend( TextView tw3)
    {
        tw = tw3;
    }


    @Override
    protected String doInBackground(String... params)
    {
        String str = params[0];
        String res = "";
        DatagramSocket socket = null;
        try
        {
            socket = new DatagramSocket(8888);
            byte[] trdata = str.getBytes();

            DatagramPacket packet = new DatagramPacket(trdata, trdata.length, InetAddress.getByName("3.1.2.0"),8888);
            socket.send(packet);
            res = "Send ok";

            if(str.contains("d")) {
                byte[] data = new byte[128];
                DatagramPacket rpacket = new DatagramPacket(data, data.length);
                socket.setSoTimeout(Integer.parseInt(params[1]));
                socket.receive(rpacket);
                res = new String(data, 0, packet.getLength());
            }
            socket.close();
        }catch(Exception e){
            System.out.println(e.toString());
            res = "err";
            if( socket != null) socket.close();
        }

        return  res;
    }

    protected void onPostExecute(String res)
    {
        tw.setText(res);
    }

}
