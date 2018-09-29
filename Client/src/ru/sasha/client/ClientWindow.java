package ru.sasha.client;

import com.sun.xml.internal.ws.resources.SenderMessages;
import ru.sasha.network.TCPConection;
import ru.sasha.network.TCPConectionListener;
import sun.nio.ch.sctp.SendFailed;
import sun.plugin2.message.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ConnectException;

public class ClientWindow extends JFrame implements ActionListener, TCPConectionListener {

    private static final String IP_ADDRESS = "127.0.0.1";
    private static final int PORT = 8189;
    private static final int HEIGHT = 400;
    private static final int WIDHT = 600;

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientWindow();
            }
        });

    }

    private final JTextArea log = new JTextArea();
    private final JTextField fieldNickName = new JTextField("Your name");
    private final JTextField fieldInput = new JTextField();

    private TCPConection conection;

    private ClientWindow() {

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDHT, HEIGHT);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        log.setEditable(false);
        log.setLineWrap(true);
        add(log, BorderLayout.CENTER);

        fieldInput.addActionListener(this);
        add(fieldInput, BorderLayout.SOUTH);
        add(fieldNickName, BorderLayout.NORTH);

        setVisible(true);

        try {
            conection = new TCPConection(this, IP_ADDRESS, PORT);
        } catch (IOException e) {
            printMassege("ConnectException: " + e);
        }


    }


    @Override
    public void onConectionReady(TCPConection tcpConection) {

        printMassege("Conection ready...");
    }

    @Override
    public void onReceiveString(TCPConection tcpConection, String value) {

        printMassege(value);
    }

    @Override
    public void onDisconect(TCPConection tcpConection) {

        printMassege("Conection closed");
    }

    @Override
    public void onExeption(TCPConection tcpConection, Exception e) {

        printMassege("ConnectException: " + e);
    }

    private synchronized void printMassege(String massage) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.append(massage + "\n");
                log.setCaretPosition(log.getDocument().getLength());
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String massege = fieldInput.getText();
        if (massege.equals("")) return;
        fieldInput.setText(null);
        conection.setString(fieldNickName.getText() + ": " + massege);
    }
}

