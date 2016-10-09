package com.example.sky_k.control;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
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
import serializable.InstruccionAndroid;

public class MainActivity extends AppCompatActivity implements Observer {

    private TextView posicionX;
    private TextView posicionY;
    private TextView angulin;
    private TextView fuerzin;
    private Button fuegooo;
    private String posiciones= "0:0:0:0:0:0:0";
    private String x="0";
    private String y="0";
    private String angulo="0";
    private String anguloAntesDeSoltar="0";
    private String[] posAntesDeSoltar= new String[2];
    private String fuerza="0";
    private String ipServidor="0";
    private InstruccionAndroid mensaje;
    float posReleased[];
    boolean isReleased;
    String shoot="0";
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
      posReleased = new float[2];
        posReleased[0]=0;
        posReleased[1]=0;
        mensaje= new InstruccionAndroid(0,0,0,0,0,posReleased,false,"0");
        final JoystickView joystick = (JoystickView) findViewById(R.id.JOY);

        ComunicacionUDP.getInstance(ipServidor).addObserver(this);
        ComunicacionUDP.getInstance(ipServidor).startSeding();



        fuegooo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    shoot="shoot";
                    System.out.println("disparo");
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    shoot="";
                    System.out.println("no Disparo");
                }
                return true;
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
                    isReleased=true;
                    anguloAntesDeSoltar=String.valueOf(joystick.getAngleBeforeRelease());
                    posAntesDeSoltar[0]=String.valueOf(joystick.getPosBeforeRelease()[0]);
                    posAntesDeSoltar[1]= String.valueOf(joystick.getPosBeforeRelease()[1]);

                }else {
                    anguloAntesDeSoltar="0";
                    posAntesDeSoltar[0]="0";
                    posAntesDeSoltar[1]="0";
                    posReleased[0]=0;
                    posReleased[1]=0;
                    isReleased=false;
                }

                setParametros(Float.parseFloat(x), Float.parseFloat(y), Float.parseFloat(angulo), Float.parseFloat(fuerza), Float.parseFloat(anguloAntesDeSoltar), posReleased, isReleased, shoot);
                ComunicacionUDP.getInstance(ipServidor).instruccion = mensaje;
                posiciones =x+":"+y+":"+angulo+":"+anguloAntesDeSoltar+":"+fuerza+":"+posAntesDeSoltar[0]+":"+posAntesDeSoltar[1];





            }
        });






    }

    //no usado ya que la comunicacion no recibira paquetes
    public void update(Observable observable, Object o) {

    }


    private void setParametros(float x, float y, float angulo, float fuerza, float releaseAngle, float[]posRelease, boolean isReleased, String instruccion){
        mensaje.x=x;
        mensaje.y=y;
        mensaje.angulo=angulo;
        mensaje.fuerza=fuerza;
        mensaje.releaseAngle=releaseAngle;
        mensaje.posRelease=posRelease;
        mensaje.isReleased=isReleased;
        mensaje.instrucccionExtra=instruccion;
    }







}
