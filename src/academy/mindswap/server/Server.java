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

    private final List<PlayerHandler> playersHandlerList; //List or Double Array ???
    private final LinkedList<PlayerHandler> multiPLayerList;
    private final int listLimit = 10;
    private ServerSocket serverSocket;
    private ExecutorService threadPool;
    private int port;
    private int highestScore;
    private LinkedList <Integer> top10Scores;


    public Server() {
        playersHandlerList = new LinkedList<>();
        port = 8080;
        top10Scores = new LinkedList<>();
        multiPLayerList = new LinkedList<>();
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

    /*public void broadcast(String message) throws IOException{
        for (PlayerHandler multiplayer : multiPLayerList){
            multiplayer.send(message);
        }

    }  //TODO -> Select the playerHandlers pair in game to send academy.mindswap.player.server.questions to players.*/

    public void sortTop10() {
        top10Scores.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });
    }

    public void listOfTop10(PlayerHandler playerHandler) {
        String message = "";
        int counter = 1;

        //sortTop10(); //CHECK IF IT IS SORTING THE LIST
        if (top10Scores.isEmpty()) {
            System.out.println("Batatinhas com arroz");
            playerHandler.send(Messages.NO_TOP_PLAYERS);
            return;
        }

        //for (int i = 0; i < listLimit; i++) {
        for(int i : top10Scores) {
            System.out.println("Hoje joga o Benfica e vai comer batatinhas!");
            message += counter +": " + i + "\n"; // Equals to:  message += ....
            counter++;
        }
        playerHandler.send(message);
    } // COMANDO PARA A LISTA DOS TOP 10 JOGADORES

    public void addScoreToList(PlayerHandler playerHandler) {
        //GUARANTEE SORTED MAJOR TO MINOR!
        top10Scores.addLast(playerHandler.playerScore);
        //sortTop10();
    }

    public synchronized void removePlayerHandler(PlayerHandler playerHandler) {
        playersHandlerList.remove(playerHandler);
    }

    private void checkMultiPlayer(PlayerHandler handler) {
        if (getMultiPlayerList().size() % 2 != 0) {
            handler.send(Messages.WAIT_FOR_ANOTHER_PLAYER);
            while (getMultiPlayerList().size() < 2) {
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

    public int defineHighestScore() {
        this.highestScore = top10Scores.getFirst();
        return highestScore;
    }

   /* public Optional<PlayerHandler> getClientByName(String name) {
        return playersHandlerList.stream()
                .filter(clientConnectionHandler -> clientConnectionHandler.getName().equalsIgnoreCase(name))
                .findFirst();
                }
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

                    if (isCommand(message)) {
                        dealWithCommand(message);
                        continue;
                    }
                    if (message.equals("")) {
                        continue;
                    }

                    dealWithMainMenu(message);

                    //play(message);
                }
            } catch (IOException |
                    InterruptedException e) {
                System.err.println(Messages.CLIENT_ERROR);
            }
        }

        /*//CHANGED
        public void oneEasyMultiplayerQuestion() throws IOException {
            FilesLoad fileReader = new FilesLoad();
            LinkedList<Question> questions = fileReader.LoadQuestionsFromFile("resources/easy.txt");
            send(questions.getFirst().toString());
        }*/


        private void dealWithMainMenu(String message) throws InterruptedException, IOException {
            switch (message) {
                case "1":
                    SinglePlayer singlePlayer = new SinglePlayer();
                    singlePlayer.play(Server.this, this);
                    break;
                case "2":
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


        private boolean isCommand(String message) {
            return message.startsWith("/");
        }

        private void dealWithCommand(String message) throws IOException, InterruptedException {

            switch (message) {
                case "/quit":
                    send(Messages.GOODBYE_MESSAGE);
                    close();
                    break;
                case "/menu":
                    send(Messages.MAIN_MENU);
                    break;
                default:
                    send(Messages.READ_RULES);
                    Thread.sleep(2000);
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