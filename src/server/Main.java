package server;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Main {
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
}
