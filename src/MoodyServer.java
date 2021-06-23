import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MoodyServer {

    private final List<PlayerHandler> playersHandlerList; //List or Double Array ??? PlayerHandlers
    private ServerSocket serverSocket;
    private ExecutorService service;
    private int port;
    private int highestScore;
    private int [] top10Scores;
    

    public MoodyServer() {
        playersHandlerList = new LinkedList<>();
        port = 8080;
        top10Scores = new int[10];
    }

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        service = Executors.newCachedThreadPool();
        int numberOfConnections = 0;

        while (true) {
            acceptConnection(numberOfConnections);
            ++numberOfConnections;
        }
    }

    private void acceptConnection(int numberOfConnections) throws IOException {
        Socket clientSocket = serverSocket.accept();
        service.submit(new ClientConnectionHandler(clientSocket, Messages.DEFAULT_NAME + numberOfConnections));
    }

    private synchronized void addPlayerHandler(ClientConnectionHandler clientConnectionHandler) {
        players.add(clientConnectionHandler);
        clientConnectionHandler.send(Messages.WELCOME);
        broadcast(clientConnectionHandler.getName(), Messages.CLIENT_ENTERED_CHAT);
    }

    public synchronized void broadcast(String name, String message) {
        //TODO -> Select the playerHandlers pair in game to send questions to players.
    }

    public synchronized String listClients() {
        StringBuffer buffer = new StringBuffer();
        players.forEach(client -> buffer.append(client.getName()).append("\n"));
        return buffer.toString();
    }

    public synchronized void removeClient(ClientConnectionHandler clientConnectionHandler) {
        players.remove(clientConnectionHandler);
    }

    public Optional<ClientConnectionHandler> getClientByName(String name) {
        return players.stream()
                .filter(clientConnectionHandler -> clientConnectionHandler.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    public class ClientConnectionHandler implements Runnable {

        private String name;
        private Socket clientSocket;
        private BufferedWriter out;
        private String message;

        public ClientConnectionHandler(Socket clientSocket, String name) throws IOException {
            this.clientSocket = clientSocket;
            this.name = name;
            this.out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        }

        @Override
        public void run() {
            addPlayerHandler(this);
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                while (!clientSocket.isClosed()) {
                    message = in.readLine();

                    if (isCommand(message)) {
                        dealWithCommand(message);
                        continue;
                    }

                    if (message.equals("")) {
                        return;
                    }

                    broadcast(name, message);
                }
            } catch (IOException e) {
                System.err.println(Messages.CLIENT_ERROR + e.getMessage());
            }
        }

        private boolean isCommand(String message) {
            return message.startsWith("/");
        }

        private void dealWithCommand(String message) throws IOException {
            String description = message.split(" ")[0];
            Command command = Command.getCommandFromDescription(description);

            if (command == null) {
                out.write(Messages.NO_SUCH_COMMAND);
                out.newLine();
                out.flush();
                return;
            }

            command.getHandler().execute(Server.this, this);
        }

        public void send(String message) {
            try {
                out.write(message);
                out.newLine();
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void close() {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public String getName() {
            return name;
        }

        public String getMessage() {
            return message;
        }
    }
}
