package server;

import models.Data;

import models.DataClient;
import server.events.*;
import server.interfaces.SocketListenerI;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Properties;
import java.util.Scanner;

public class Monitor implements SocketListenerI {
    public static void main(String[] args) {
        int port = getPortProperty(args);
        Server server = new Server(port);
        server.start();
    }

    public static int getPortProperty(String[] args) {
        try {
            Properties properties = new Properties();
            String fileName = args[0];
            String propertyPort = args[1];
            properties.load(new FileInputStream(fileName));
            String port = properties.getProperty(propertyPort);
            return Integer.parseInt(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onClientConnect(EventConnection e) {

    }

    @Override
    public void onClientDisconnect(EventDisconnection e) {

    }

    @Override
    public void onSendMessage(EventSend e) {

    }

    @Override
    public void onReceiveMessage(EventReceive e) {
        DataClient dataClient = e.getDataClient();
        String id = dataClient.getId();
        float temperature = dataClient.getTemperature();
        float humidity = dataClient.getHumidity();
        System.out.println("id: " + id);
        System.out.println("temperatura: " + temperature);
        System.out.println("humedad: " + humidity);
        // TODO: guardar en la base de datos
    }
}
