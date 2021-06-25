package academy.mindswap.player;

import academy.mindswap.server.util.Messages;

import java.io.*;
import java.net.Socket;

public class Player {

    private String name;

    public Player(String name){
        this.name = name;
    }


    public static void main(String[] args) {
        Player player = new Player("Soraia");
        try {
            player.start("localhost", 8080);
        } catch (IOException e) {
            System.out.println(Messages.CONNECTION_CLOSED);
        }
    }

    private void start(String host, int port) throws IOException {
        Socket socket = new Socket(host, port);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        new Thread(new KeyboardHandler(out, socket)).start();

        while (!socket.isClosed()) {
            String line = in.readLine();

            if (line == null) {
                socket.close();
                continue;
            }

            System.out.println(line);
        }
    }
    public String getName() {
        return name;
    }

    private class KeyboardHandler implements Runnable {
        private BufferedWriter out;
        private Socket socket;
        private BufferedReader in;

        public KeyboardHandler(BufferedWriter out, Socket socket) {
            this.out = out;
            this.socket = socket;
            this.in = new BufferedReader(new InputStreamReader(System.in));
        }

        @Override
        public void run() {

            while (!socket.isClosed()) {
                try {
                    String line = in.readLine();

                    out.write(line);
                    out.newLine();
                    out.flush();

                    if (line.equals("/quit") || line.equals("0")) {
                        Thread.sleep(100);
                        socket.close();
                        System.exit(0);
                    }
                } catch (IOException | InterruptedException e) {
                    System.out.println(Messages.SOCKET_ERROR);
                }
            }
        }
    }
}
