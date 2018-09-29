package ru.sasha.server;

import ru.sasha.network.TCPConection;
import ru.sasha.network.TCPConectionListener;
import sun.rmi.transport.tcp.TCPConnection;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ChatServer implements TCPConectionListener {

    public static void main(String[] args) {

        new ChatServer();

    }

    private final ArrayList<TCPConection> conections = new ArrayList<>();

    private ChatServer(){

        System.out.println("Server is runnig!");

        try(ServerSocket serverSocket = new ServerSocket(8189)) {

            while (true) {

                try {

                    new TCPConection (this, serverSocket.accept());

                } catch (IOException e) {
                    System.out.println("TCPConection Exepted: " + e);
                }
            }


        } catch (IOException e){

            throw new RuntimeException(e);

        }


    }

    @Override
    public synchronized void onConectionReady(TCPConection tcpConection) {

        conections.add(tcpConection);
        sendToAllClients("Client conected: " + tcpConection);
    }

    @Override
    public synchronized void onReceiveString(TCPConection tcpConection, String value) {

        sendToAllClients(value);

    }

    @Override
    public synchronized void onDisconect(TCPConection tcpConection) {


        conections.remove(tcpConection);
        sendToAllClients("Client disconected: " + tcpConection);
    }

    @Override
    public synchronized void onExeption(TCPConection tcpConection, Exception e) {

        System.out.println("TCPConection exeption :" + e);
    }

    private void sendToAllClients(String value){

        System.out.println(value);

        final int conectionSize = conections.size();
        for (int i = 0; i < conectionSize; i++){

            conections.get(i).setString(value);
        }
    }
}
