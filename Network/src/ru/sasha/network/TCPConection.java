package ru.sasha.network;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

public class TCPConection {

    private final Socket socket;
    private final Thread rxThread;
    private final BufferedReader in;
    private final TCPConectionListener eventListener;
    private final BufferedWriter out;

    public TCPConection (TCPConectionListener eventListener, String ipAddress, int port) throws  IOException {

        this(eventListener, new Socket(ipAddress, port));

    }

    public TCPConection (TCPConectionListener eventListener, Socket socket) throws IOException {

        this.socket = socket;
        this.eventListener = eventListener;

        in = new BufferedReader( new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8")));

        rxThread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    eventListener.onConectionReady(TCPConection.this);
                    while (!rxThread.isInterrupted()){
                        String msg = in.readLine();
                        eventListener.onReceiveString(TCPConection.this, msg);
                    }


                }
                catch (IOException e) {

                    eventListener.onExeption(TCPConection.this, e);


                }
                finally {

                    eventListener.onDisconect(TCPConection.this);
                    disconnect();

                }

            }
        });
        rxThread.start();

    }

    public synchronized void setString(String value){

        try {
            if(value.equals("game"))
                out.write("  hui  ");
            else {
                out.write(value + "\r\n");
                out.flush();
            }
        } catch (IOException e){

            eventListener.onExeption(TCPConection.this, e);
            disconnect();
        }


    }

    public synchronized void disconnect(){

        rxThread.interrupt();
        try {
           socket.close();
        } catch (IOException e){

            eventListener.onExeption(TCPConection.this, e);
        }

    }

    @Override
    public String toString(){

        return ("TCPConection: " + socket.getInetAddress() + " ;" + socket.getPort());
    }
}
