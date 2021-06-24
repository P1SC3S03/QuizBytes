package academy.mindswap;

import academy.mindswap.server.Server;

import java.io.IOException;


public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.start(8080);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
