package server.events;

import models.Data;
import models.DataClient;

import java.net.Socket;
import java.util.EventObject;

public class EventReceive extends EventObject {
    Socket clientSocket;
    Object pack;
    DataClient dataClient;

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public Object getPack() {
        return pack;
    }

    public DataClient getDataClient() {
        return dataClient;
    }

    public void setDataClient(DataClient dataClient) {
        this.dataClient = dataClient;
    }

    public void setPack(Object pack) {
        this.pack = pack;
    }

    public EventReceive(Object source, Socket clientSocket, Object pack) {
        super(source);
        this.clientSocket = clientSocket;
        this.pack = pack;
        this.dataClient = (DataClient) pack;
    }
}
