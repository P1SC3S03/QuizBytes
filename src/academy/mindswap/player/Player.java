package academy.mindswap.player;

import academy.mindswap.server.util.Messages;

import java.io.*;
import java.net.Socket;

/**
 * Player Class
 */
public class Player {
    /**
     * Creates the player and tried to connect to the designated server host name and port, in this case, 8080.
     *
     * @param args - can receive input arguments from the user via console
     */
    public static void main(String[] args) {
        Player player = new Player();
        try {
            player.start("localhost", 8080);
        } catch (IOException e) {
            System.out.println(Messages.CONNECTION_CLOSED);
        }
    }

    /**
     * Creates the output Thread and runs it
     *
     * @param host - server host address/name for the socket creation
     * @param port - server port for the socket creation
     * @throws IOException If an input or output exception occurs
     */
    private void start(String host, int port) throws IOException {
        Socket socket = new Socket(host, port);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        new Thread(new OutputHandler(out, socket)).start();

        while (!socket.isClosed()) {
            String line = in.readLine();

            if (line == null) {
                socket.close();
                continue;
            }
            System.out.println(line);
        }
    }

    /**
     * Runnable inner class for the Thread that is going to be responsible for the output stream to the server.
     */
    private class OutputHandler implements Runnable {
        private BufferedWriter out;
        private Socket socket;
        private BufferedReader in;

        /**
         * Constructor of the Output Handler
         *
         * @param out    - Buffered Writer to send messages to the server.
         * @param socket - Player socket to be received because it is needed to send messages/input to the server.
         */
        public OutputHandler(BufferedWriter out, Socket socket) {
            this.out = out;
            this.socket = socket;
            this.in = new BufferedReader(new InputStreamReader(System.in));
        }

        /**
         * Reads input from the player and sends it to the server.
         * Also it checks if the input is either '/quit' or '0' since those are the two cases in which the player socket
         * needs to be closed and, if so, closes them.
         */
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
