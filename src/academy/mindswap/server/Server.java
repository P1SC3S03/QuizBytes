package academy.mindswap.server;

import academy.mindswap.server.gametype.MultiPlayer;
import academy.mindswap.server.gametype.SinglePlayer;
import academy.mindswap.server.util.Messages;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private final LinkedList<PlayerHandler> multiPLayerList;
    private ServerSocket serverSocket;
    private ExecutorService threadPool;
    private int port;
    private int highestScore;
    private LinkedList<Integer> top10Scores;


    public Server() {
        port = 8080;
        top10Scores = new LinkedList<>();
        multiPLayerList = new LinkedList<>();
    }

    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.start(8080);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        threadPool.submit(new PlayerHandler(clientSocket, Messages.DEFAULT_NAME + numberOfConnections));
    }

    public void sortTop10() {
        top10Scores.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        });
    }

    public void listOfTop10(PlayerHandler playerHandler) {
        String message = "";
        int counter = 1;

        if (top10Scores.isEmpty()) {
            playerHandler.send(Messages.NO_TOP_PLAYERS);
            return;
        }

        for (int score : top10Scores) {
            message += counter + ": " + score + " points" + "\n"; // Equals to:  message += ....
            counter++;
        }
        playerHandler.send(message);
    }

    public void addScoreToList(PlayerHandler playerHandler) {
        top10Scores.addLast(playerHandler.playerScore);
        sortTop10();
    }

    public synchronized void removePlayerHandlerFromMultiPlayerList(PlayerHandler playerHandler) {
        multiPLayerList.remove(playerHandler);
    }

    private void checkMultiPlayer(PlayerHandler handler) {
        if (getMultiPlayerList().size() % 2 != 0) {
            handler.send(Messages.WAIT_FOR_ANOTHER_PLAYER);
            while (getMultiPlayerList().size() % 2 != 0) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
           }
        }
    }

    public List<PlayerHandler> getMultiPlayerList() {
        return multiPLayerList;
    }

    public void defineHighestScore() {
        this.highestScore = top10Scores.getFirst();
    }

    public int stealFromRandomPlayer(PlayerHandler playerHandler) {
        int index = multiPLayerList.indexOf(playerHandler);
        int randomPlayerIndex = index;
        int stolenPoints;

        while (randomPlayerIndex == index) {
            randomPlayerIndex = (int) (Math.random() * (multiPLayerList.size()));
        }
        stolenPoints = multiPLayerList.get(randomPlayerIndex).getPlayerScore();
        multiPLayerList.get(randomPlayerIndex).setPlayerScore(0);

        return stolenPoints;
    }

    public int getHighestScore() {
        return highestScore;
    }

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
            send(Messages.WELCOME_SERVER);
            try {
                Thread.sleep(1000); //CHANGE BACK
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            send(Messages.MAIN_MENU);

            try {
                in = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));

                while (!playerSocket.isClosed()) {
                    message = in.readLine();

                    if (isCommand(message)) {
                        dealWithCommand(message);
                        continue;
                    }
                    if (message.equals("")) {
                        continue;
                    }
                    dealWithMainMenu(message);
                }
            } catch (IOException |
                    InterruptedException e) {
                System.err.println(Messages.CLIENT_ERROR);
            }
        }

        private void dealWithMainMenu(String message) throws InterruptedException, IOException {
            switch (message) {
                case "1":
                    SinglePlayer singlePlayer = new SinglePlayer();
                    singlePlayer.play(this);
                    break;
                case "2":
                    playerScore = 0;
                    multiPLayerList.add(this);
                    checkMultiPlayer(this);
                    MultiPlayer multiPlayer = new MultiPlayer();
                    multiPlayer.play(Server.this, this);
                    break;
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
                    Thread.sleep(10);
                    close();
                    break;
                default:
                    send(Messages.READ_RULES);
                    send(Messages.MAIN_MENU);
                    break;
            }
        }


        public boolean isCommand(String message) {
            return message.startsWith("/");
        }

        public void dealWithCommand(String message) throws IOException, InterruptedException {

            switch (message) {
                case "/quit":
                    send(Messages.GOODBYE_MESSAGE);
                    close();
                    break;
                case "/menu":
                    send(Messages.MAIN_MENU);
                    break;
                case "/support":
                    send(Messages.EMOTIONAL_SUPPORT);
                    Thread.sleep(3000);
                    break;
                case "/hit me":
                    send(Messages.HIT_ME);
                    Thread.sleep(5000);
                    break;
                case "/steal":
                    send(Messages.STEAL);
                    Thread.sleep(3000);
                    break;
                default:
                    send(Messages.READ_RULES);
                    Thread.sleep(3000);
                    send(Messages.MAIN_MENU);
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
                removePlayerHandlerFromMultiPlayerList(this);
                playerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public String getName() { // Necessário??
            return name;
        }

        public int getPlayerScore() {
            return playerScore;
        }

        public BufferedReader getIn() {
            return in;
        }

        public Socket getPlayerSocket() {
            return playerSocket;
        }

        public void setPlayerScore(int playerScore) {
            this.playerScore = playerScore;
        }
    }


}