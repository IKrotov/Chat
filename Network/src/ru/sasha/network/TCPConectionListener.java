package ru.sasha.network;

public interface TCPConectionListener {

    void onConectionReady(TCPConection tcpConection);
    void onReceiveString(TCPConection tcpConection, String value);
    void onDisconect(TCPConection tcpConection);
    void onExeption(TCPConection tcpConection, Exception e);



}
