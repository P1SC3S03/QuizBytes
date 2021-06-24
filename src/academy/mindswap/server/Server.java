package academy.mindswap.server;

import academy.mindswap.server.commands.Command;
import academy.mindswap.server.util.Messages;

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

    }  //TODO -> Select the playerHandlers pair in game to send academy.mindswap.player.server.questions to players.

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
        if (playerHandler.playerScore == 0) {
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
                    message = in.readLine();

                    if (!wantsToPlay(message)) {
                        dealWithNoPlaying(message);
                        continue;
                    }

                    if (isCommand(message)) {
                        dealWithCommand(message);
                        continue;
                    }

                    if (message.equals("")) {
                        continue;
                    }
                    play(message);
                }
            } catch (IOException | InterruptedException e) {
                System.err.println(Messages.CLIENT_ERROR + e.getMessage());
            }
        }

        private boolean wantsToPlay(String message) {
            return message.equals("1") || message.equals("2");
        }

        private void play(String message) {
            Command command = Command.getCommandFromMessage(message);

            if (command == null) {
                send(Messages.NO_SUCH_COMMAND);
                return;
            }

            command.getHandler().execute(Server.this, this);
        }

        private void dealWithNoPlaying(String message) throws InterruptedException {

            switch (message) {
                case "3":
                    send(Messages.COMMAND_LIST);
                    Thread.sleep(2000);
                    send(Messages.MAIN_MENU);
                    break;
                case "4":
                    listOfTop10(this);
                    Thread.sleep(2000);
                    send(Messages.MAIN_MENU);
                    break;
                case "0":
                    send(Messages.GOODBYE_MESSAGE);
                    close();
                    break;
                default:
                    send(Messages.READ_RULES);
                    Thread.sleep(2000);
                    send(Messages.MAIN_MENU);
                    break;
            }
        }

        private boolean isCommand(String message) {
            return message.startsWith("/");
        }

        private void dealWithCommand(String message) throws IOException {

            switch(message){
                case "/quit":
                    send(Messages.GOODBYE_IN_LOVE);
                    close();
                    break;
                default:
                    send(Messages.READ_RULES);
                    break;
            }
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
                removePlayerHandler(this);
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