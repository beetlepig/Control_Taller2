package com.example.sky_k.control;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;

import serializable.InstruccionAndroid;

public class ComunicacionUDP extends Observable implements Runnable {

        private static final String TAG = "CommunicationManager";
        private static ComunicacionUDP ref;
        // Default destination address - emulator host IP address

        // A multicast IP address
        private String dirServidor;
        // Default destination port
        public static  final int DEFAULT_PORT = 5000;
        InetAddress ia;
        private DatagramSocket ms;

        public InstruccionAndroid instruccion;
        private boolean running;
        private boolean connecting;
        private boolean reset;
        private boolean errorNotified;




        private ComunicacionUDP(String ipS) {
            this.dirServidor=ipS;
            running = true;
            connecting = true;
            reset = false;
            errorNotified = false;
            try {
                ia= InetAddress.getByName(dirServidor);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }

        }


        public static ComunicacionUDP getInstance(String ip) {
            if (ref == null) {
                ref = new ComunicacionUDP(ip);
                Thread runner = new Thread(ref);
                runner.start();
            }

            return ref;
        }


  public void startSeding(){
        new Thread(sendMessage()).start();
    }


        //metodo no usado actualmente ya que el cliente no recibe paquetes
        public void run() {
            Log.d(TAG, "[ Communication Thread Started ]");
            while (running) {
                if (connecting) {
                    if (reset) {
                        if (ms != null) {
                            ms.close();
                            Log.d(TAG, "[Communication was reset]");
                        }
                        reset = false;
                    }
                    connecting = !attemptConnection();
                } else {
                    if (ms != null) {
                        DatagramPacket p = receiveMessage();


                        if (p != null) {

                            String message = new String(p.getData(), 0, p.getLength());


                            setChanged();
                            notifyObservers(message);
                            clearChanged();
                        }
                    }
                }
            }
            ms.close();
        }

        private boolean attemptConnection() {
            try {
                ms = new DatagramSocket();
                setChanged();
                notifyObservers("Connection started");
                clearChanged();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "[ Error starting Communication]");
                if (!errorNotified) {
                    setChanged();
                    notifyObservers("Connection failed");
                    clearChanged();
                    errorNotified = true;
                }
                return false;
            }
        }

        private Runnable sendMessage() {
           Runnable r=  new Runnable() {

                public void run() {
                    while (true){
                        if (ms != null) {

                            try {
                                // Validate destAddress





                                    ms.send(enviarObjeto(instruccion,ia,DEFAULT_PORT));


                                 //   System.out.println("objeto enviado");
                                Thread.sleep(15);
                            } catch (UnknownHostException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (InterruptedException  e){
                                e.printStackTrace();
                            }
                        } else {
                            setChanged();
                            notifyObservers("Not connected");
                            clearChanged();
                        }
                }
                }
            };
return r;
        }


    private DatagramPacket enviarObjeto(Object ob, InetAddress destAddress, int destPort) throws IOException {
        byte[] data= serializar(ob);
        DatagramPacket packet = new DatagramPacket(data, data.length, destAddress, destPort);

        return  packet;

    }


    private byte[] serializar(Object data) {
        byte[] bytes = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(data);
            bytes = baos.toByteArray();

            // Close streams
            oos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }
//metono no usado actualmente, pero posiblemente usado para futura revisiones
        public DatagramPacket receiveMessage() {
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            try {
                ms.receive(packet);
                System.out.println("Data received from " + packet.getAddress() + ":" + packet.getPort());
                return packet;
            } catch (IOException e) {

                e.printStackTrace();
            }
            return null;
        }




    }

