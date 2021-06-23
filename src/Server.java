import GameMode.MultiplayerMode;
import GameMode.SingleplayerMode;
import Util.Messages;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private final List<PlayerHandler> playersHandlerList; //List or Double Array ??? PlayerHandlers
    private final int listLimit = 10;
    private ServerSocket serverSocket;
    private ExecutorService threadPool;
    private int port;
    private int highestScore;
    private LinkedList<Integer> top10Scores;


    public Server() {
        playersHandlerList = new LinkedList<>();
        port = 8080;
        top10Scores = new LinkedList<>();
    }

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        threadPool = Executors.newCachedThreadPool();
        int numberOfConnections = 0;

        while (true) {
            acceptConnection(numberOfConnections);
            ++numberOfConnections;
        }
    }

    private void acceptConnection(int numberOfConnections) throws IOException {
        Socket clientSocket = serverSocket.accept();
        threadPool.submit(new PlayerHandler(clientSocket, Messages.DEFAULT_NAME + numberOfConnections)); //VER CONSTRUTOR DO PLAYERHANDLER
    }

    private synchronized void addPlayerHandler(PlayerHandler playerHandler) {   //If for Array one way if for list another.
        playersHandlerList.add(playerHandler);
        playerHandler.send(Messages.WELCOME_MESSAGE);
        playerHandler.send(Messages.MAIN_MENU);
        //(playerHandler.getName(), Messages.CLIENT_ENTERED_CHAT);
    }

    public synchronized void broadcast(String name, String message) {

    }  //TODO -> Select the playerHandlers pair in game to send questions to players.

    public void sortTop10() {
        top10Scores.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });
    }

    public void listOfTop10(PlayerHandler playerHandler) {
        StringBuilder message = new StringBuilder();
        int counter = 1;

        sortTop10(); //CHECK IF IT IS SORTING THE LIST

        if (top10Scores.isEmpty()) {
            playerHandler.send("There is no top players yet. You are lucky! ;)");
        }

        for (int i = 0; i < listLimit; i++) {
            message.append(counter).append(":").append(top10Scores.get(i)).append("\n"); // Equals to:  message += ....
            counter++;
        }
        playerHandler.send(message.toString());
    } // COMANDO PARA A LISTA DOS TOP 10 JOGADORES

    public void addScoreToList(PlayerHandler playerHandler) {
        //GUARANTEE SORTED MAJOR TO MINOR!
        if (playerHandler.playerScore == 0){
            return;
        }
        top10Scores.addLast(playerHandler.playerScore);
        sortTop10();
    }

    public synchronized void removePlayerHandler(PlayerHandler playerHandler) {
        playersHandlerList.remove(playerHandler);
    }

   /* public Optional<PlayerHandler> getClientByName(String name) {
        return playersHandlerList.stream()
                .filter(clientConnectionHandler -> clientConnectionHandler.getName().equalsIgnoreCase(name))
                .findFirst();
*/

    public class PlayerHandler implements Runnable {

        private String name;
        private Socket playerSocket;
        private BufferedWriter out;
        private BufferedReader in;
        private String message;
        private int playerScore;


        public PlayerHandler(Socket playerSocket, String name) throws IOException {
            this.playerSocket = playerSocket;
            this.name = name;
            this.out = new BufferedWriter(new OutputStreamWriter(playerSocket.getOutputStream()));
            playerScore = 0;
        }

        @Override
        public void run() {
            addPlayerHandler(this);

            try {
                in = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));

                while (!playerSocket.isClosed()) {
                    dealWithMainMenu(in.readLine());
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

        private void dealWithMainMenu(String message){

            do {
                switch (message) {
                    case "1":
                        new SingleplayerMode(this).execute();
                        break;
                    case "2":
                        new MultiplayerMode(this).execute();
                        break;
                    case "3":
                        send(Messages.COMMAND_LIST);
                        break;
                    case "4":
                        listOfTop10(this);
                    case "0":
                        send(Messages.GOODBYE_MESSAGE);
                        close();
                    default:
                        send(Messages.READ_RULES);
                        return;
                }
            }while (!message.equals(0));
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

            //command.getHandler().execute(Server.this, this);
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
                addScoreToList(this);
                playerSocket.close();
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

        public int getPlayerScore() {
            return playerScore;
        }
    }
}