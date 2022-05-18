package server;

import models.Data;
import server.events.*;
import server.info.ThreadInfo;
import server.interfaces.*;
import server.listeners.*;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements SocketListenerI {
    ServerSocket serverSocket;
    HashMap<String, ThreadInfo> clientsConnected;
    ConnectionListener connectionListener;
    OutputStream output;

    public Server(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
            this.clientsConnected = new HashMap<String, ThreadInfo>();
            this.connectionListener = new ConnectionListener(serverSocket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        this.connectionListener.addConnectionListener(this);
        this.connectionListener.start();
    }

    @Override
    public void onClientConnect(EventConnection e) {
        DataListener dataListener = new DataListener(e.getClient().getClientSocket());
        dataListener.addDataListener(this);
        dataListener.start();
        ThreadInfo threadInfo = new ThreadInfo(e.getClient().getClientSocket(), dataListener);
        this.clientsConnected.put(e.getClient().getId(), threadInfo);
        System.out.println("Cliente conectado: " + e.getClient().getId());
        System.out.println("Clientes conectados: " + this.clientsConnected.size());
    }

    @Override
    public void onClientDisconnect(EventDisconnection e) {
        System.out.println("Un cliente se desconecto");
        this.clientsConnected.remove(e.getClient().getId());
        System.out.println("Clientes conectados: " + this.clientsConnected.size());
    }

    @Override
    public void onSendMessage(EventSend e) {

    }

    public void sendMessage(String id, Data data) {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        threadPool.submit(() -> taskSendMessage(id, data));
    }

    public void taskSendMessage(String id, Data data) {
        try {
            ThreadInfo threadInfo = clientsConnected.get(id);
            Socket clientSocket = threadInfo.getClientSocket();
            output = clientSocket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(output);
            objectOutputStream.writeObject(data);
            output.flush();
        } catch (IOException e) {
            try {
                output.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onReceiveMessage(EventReceive e) {
        String id = e.getDataClient().getId();
        Data data = new Data(e.getDataClient().getTemperature(), e.getDataClient().getHumidity());
        sendMessage(id, data);
        System.out.println(e.getDataClient().getId());
        System.out.println(e.getDataClient().getTemperature());
        System.out.println(e.getDataClient().getHumidity());
    }
}
