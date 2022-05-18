package server.listeners;

import server.events.EventDisconnection;
import server.events.EventReceive;
import server.events.EventSend;
import server.info.ConnectionInfo;
import server.interfaces.SocketListenerI;

import javax.swing.event.EventListenerList;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

public class DataListener extends Thread {
    Socket clientSocket;
    boolean condition = true;
    EventListenerList listenerList;
    InputStream input;

    public DataListener(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.listenerList = new EventListenerList();
    }

    @Override
    public void run() {
        super.run();
        this.listen();
    }

    private void listen() {
        System.out.println("Cliente nuevo conectado");
        try {
            while (condition) {
                System.out.println("Escuchando el mensaje del cliente");
                input = clientSocket.getInputStream();
                ObjectInputStream objectInputStream = new ObjectInputStream(input);
                EventReceive eventReceive = new EventReceive(this, clientSocket, objectInputStream.readObject());
                dispatchOnReceive(eventReceive);
            }
        } catch (ClassNotFoundException | IOException e) {
            this.close();
//            throw new RuntimeException(e);
        }
    }

    public void addDataListener(SocketListenerI listener) {
        listenerList.add(SocketListenerI.class, listener);
    }

    public void removeDataListener(SocketListenerI listener) {
        listenerList.remove(SocketListenerI.class, listener);
    }

    public void dispatchOnReceive(EventReceive e) {
        Object[] listeners = listenerList.getListeners(SocketListenerI.class);
        for (Object o : listeners) {
            SocketListenerI listener = (SocketListenerI) o;
            listener.onReceiveMessage(e);
        }
    }

    public void dispatchOnSend(EventSend e) {
        Object[] listeners = listenerList.getListeners(SocketListenerI.class);
        for (Object o : listeners) {
            SocketListenerI listener = (SocketListenerI) o;
            listener.onSendMessage(e);
        }
    }

    public void dispatchOnDisconnect(EventDisconnection e) {
        Object[] listeners = listenerList.getListeners(SocketListenerI.class);
        for (Object o : listeners) {
            SocketListenerI listener = (SocketListenerI) o;
            listener.onClientDisconnect(e);
        }
    }

    private void close() {
        try {
            input.close();
            EventDisconnection eventDisconnection = new EventDisconnection(this, new ConnectionInfo(clientSocket.getRemoteSocketAddress().hashCode() + "", clientSocket.getInetAddress().toString(), clientSocket));
            dispatchOnDisconnect(eventDisconnection);
            this.clientSocket.close();
        } catch (IOException ignored) {
        }
    }
}
