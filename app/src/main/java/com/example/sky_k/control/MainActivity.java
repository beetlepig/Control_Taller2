package com.example.sky_k.control;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.RunnableFuture;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class MainActivity extends AppCompatActivity implements Observer {

    private TextView posicionX;
    private TextView posicionY;
    private TextView angulin;
    private TextView fuerzin;
    private Button fuegooo;
    private String posiciones= "0:0:0:0:0:0:0";
    private String x;
    private String y;
    private String angulo;
    private String anguloAntesDeSoltar="0";
    private String[] posAntesDeSoltar= new String[2];
    private String fuerza;
    private String ipServidor;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        ipServidor = intent.getStringExtra("ipeson");
     //   posicionX= (TextView) findViewById(R.id.posX);
      //  posicionY= (TextView) findViewById(R.id.posY);
        fuegooo= (Button) findViewById(R.id.button);
     //   angulin= (TextView) findViewById(R.id.ag);
    //    fuerzin= (TextView) findViewById(R.id.fz);

        final JoystickView joystick = (JoystickView) findViewById(R.id.JOY);
        ComunicacionUDP.getInstance(ipServidor).addObserver(this);
        ComunicacionUDP.getInstance(ipServidor).startSeding();


        fuegooo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ComunicacionUDP.getInstance(ipServidor).MandarDisparo("shoot");
            }
        });

        joystick.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength, int posx, int posy) {
                x= String.valueOf(posx);
                y=String.valueOf(posy);
                angulo= String.valueOf(angle);
                fuerza= String.valueOf(strength);
            //    posicionX.setText("PosX: " + x);
             //   posicionY.setText("PosY: " + y);
              //  angulin.setText("Angulo: " + angulo);
              //  fuerzin.setText("Fuerza: " + fuerza);

if(joystick.isReleased()){
    anguloAntesDeSoltar=String.valueOf(joystick.getAngleBeforeRelease());
    posAntesDeSoltar[0]=String.valueOf(joystick.getPosBeforeRelease()[0]);
    posAntesDeSoltar[1]= String.valueOf(joystick.getPosBeforeRelease()[1]);

}else {
    anguloAntesDeSoltar="0";
    posAntesDeSoltar[0]="0";
    posAntesDeSoltar[1]="0";
}

                posiciones =x+":"+y+":"+angulo+":"+anguloAntesDeSoltar+":"+fuerza+":"+posAntesDeSoltar[0]+":"+posAntesDeSoltar[1];
ComunicacionUDP.getInstance(ipServidor).setPosiciones(posiciones);




            }
        });
    }

    //no usado ya que la comunicacion no recibira paquetes
    public void update(Observable observable, Object o) {

    }







}
