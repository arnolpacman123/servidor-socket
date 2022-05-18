package client;

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
        try {
            InetAddress ip = InetAddress.getByName("localhost");
            Socket socket = new Socket(ip, 5000);
            while (true) {
                Scanner reader = new Scanner(System.in);
                System.out.println("Escribe el destinatario:");
                String id = reader.nextLine();
                System.out.println("Escribe la temperatura:");
                int temperature = Integer.parseInt(reader.nextLine());
                System.out.println("Escribe la humedad:");
                int humidity = Integer.parseInt(reader.nextLine());
                OutputStream outputStream = socket.getOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                objectOutputStream.writeObject(new DataClient(id, temperature, humidity));
                InputStream inputStream = socket.getInputStream();
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                Data data = (Data) objectInputStream.readObject();
                System.out.println("EL SERVIDOR RESPONDE:");
                System.out.println(data.getTemperature());
                System.out.println(data.getHumidity());
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
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

    }
}
