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

/**
 * Server Class
 * This class creates the blueprint for the Quizz_Bytes application.
 */
public class Server {

    private final LinkedList<PlayerHandler> multiPLayerList;
    private ServerSocket serverSocket;
    private ExecutorService threadPool;
    private int port;
    private int highestScore;
    private LinkedList<Integer> scoreList;

    /**
     * Constructor method for the Server
     */
    public Server() {
        port = 8080;
        scoreList = new LinkedList<>();
        multiPLayerList = new LinkedList<>();
    }

    /**
     * Creates the server and starts it in the designated port.
     *
     * @param args - can receive input arguments from the user via console
     */
    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.start(8080);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts the server socket and the thread pool.
     *
     * @param port - Receives and designates the server port to be used.
     * @throws IOException If an input or output exception occurs.
     */
    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        threadPool = Executors.newCachedThreadPool();

        while (true) {
            acceptConnection();
        }
    }

    /**
     * Accepts the player socket connection and creates a thread for each player connection.
     *
     * @throws IOException If an input or output exception occurs.
     */
    private void acceptConnection() throws IOException {
        Socket playerSocket = serverSocket.accept();
        threadPool.submit(new PlayerHandler(playerSocket));
    }

    /**
     * Sorts the score list from highest to lowest.
     */
    public void sortScoreList() {
        scoreList.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        });
    }

    /**
     * Sends top 10 score list to the requesting player.
     *
     * @param playerHandler - Player Handler whose player is asking for the list.
     */
    public void listOfTop10(PlayerHandler playerHandler) {
        String message = "";
        int counter = 1;

        if (scoreList.isEmpty()) {
            playerHandler.send(Messages.NO_TOP_PLAYERS);
            return;
        }

        for (int score : scoreList) {
            message += counter + ": " + score + " points" + "\n";
            counter++;
        }
        playerHandler.send(message);
    }

    /**
     * Adds Player's score to the score list.
     *
     * @param playerHandler - Player
     */
    public void addScoreToList(PlayerHandler playerHandler) {
        scoreList.addLast(playerHandler.playerScore);
        sortScoreList();
    }

    /**
     * Removes the Player Handler from the MultiPlayer List.
     *
     * @param playerHandler - Player Handler to be removed.
     */
    public void removePlayerHandlerFromMultiPlayerList(PlayerHandler playerHandler) {
        multiPLayerList.remove(playerHandler);
    }

    /**
     * Checks if a player can start a MultiPlayer game by checking if the list has an even amount of players.
     * If it does not, a message is sent to the player and it needs to wait until condition is verified.
     *
     * @param playerHandler - Player Handler whose player is trying to go into a MultiPlayer game.
     */
    private void checkMultiPlayer(PlayerHandler playerHandler) {
        if (getMultiPlayerList().size() % 2 != 0) {
            playerHandler.send(Messages.WAIT_FOR_ANOTHER_PLAYER);
            while (getMultiPlayerList().size() % 2 != 0) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Defines the Highest Score by getting the first element of the top 10 scores list.
     */
    public void defineHighestScore() {
        this.highestScore = scoreList.getFirst();
    }

    /**
     * Steals the amount of points from a random player from the MultiPlayer list.
     *
     * @param playerHandler - Player Handler whose player is invoking this method.
     * @return the total amount of points being stolen from a random player.
     */
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

    /**
     * Getter for the MultiPlayer List.
     *
     * @return Return the MultiPlayer list.
     */
    public List<PlayerHandler> getMultiPlayerList() {
        return multiPLayerList;
    }

    /**
     * Getter for Highest Score
     *
     * @return Returns the Highest Score
     */
    public int getHighestScore() {
        return highestScore;
    }

    /**
     * Runnable Class needed to support the interaction between players and the server.
     */
    public class PlayerHandler implements Runnable {

        private Socket playerSocket;
        private BufferedWriter out;
        private BufferedReader in;
        private String message;
        private int playerScore;

        /**
         * Constructor method for the Player Handler.
         *
         * @param playerSocket - Player socket to be received because it is needed to send messages back to the player.
         * @throws IOException - If an input or output exception occurs
         */
        public PlayerHandler(Socket playerSocket) throws IOException {
            this.playerSocket = playerSocket;
            this.out = new BufferedWriter(new OutputStreamWriter(playerSocket.getOutputStream()));
            playerScore = 0;
        }

        /**
         * Sends the player a welcome message and after that presents him the main menu.
         * Gets the user input and delegates tasks between the different inner class methods.
         */
        @Override
        public void run() {
            send(Messages.WELCOME_SERVER);
            try {
                Thread.sleep(1000);
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

        /**
         * Deals with the players input within the Main Menu options.
         * Cases in this method represent the different possible choices.
         *
         * @param message - message to be analysed
         * @throws InterruptedException If an interruption exception occurs
         * @throws IOException          If an input or output exception occurs
         */
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
                    Thread.sleep(10000);
                    send(Messages.MAIN_MENU);
                    break;
                case "4":
                    listOfTop10(this);
                    Thread.sleep(3000);
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

        /**
         * Checks if the players input starts with a "/" and returns true if it does.
         *
         * @param message - message to be analysed.
         * @return true if it does, false it is does not.
         */
        public boolean isCommand(String message) {
            return message.startsWith("/");
        }

        /**
         * Deals with Commands started with "/".
         *
         * @param message - message to be analysed
         * @throws IOException          If an input or output exception occurs
         * @throws InterruptedException If an interruption exception occurs
         */
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
                    Thread.sleep(4000);
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

        /**
         * Sends message to the Player via its Player Handler's Buffered Writer.
         *
         * @param message - message to be sent
         */
        public void send(String message) {
            try {
                out.write(message);
                out.newLine();
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * Closes the Player Socket.
         * Adds Player Score to the list by using its Player Handler.
         * Removes Player Handler from the MultiPlayer List.
         */
        public void close() {
            try {
                addScoreToList(this);
                removePlayerHandlerFromMultiPlayerList(this);
                playerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * Getter for the Player Score
         *
         * @return Player Score
         */
        public int getPlayerScore() {
            return playerScore;
        }

        /**
         * Getter for the Buffered Reader
         *
         * @return Buffered Reader
         */
        public BufferedReader getIn() {
            return in;
        }

        /**
         * Getter for the Player Socket
         *
         * @return Player Socket
         */
        public Socket getPlayerSocket() {
            return playerSocket;
        }

        /**
         * Setter for the Player Score
         *
         * @param playerScore - the score to be set
         */
        public void setPlayerScore(int playerScore) {
            this.playerScore = playerScore;
        }
    }
}