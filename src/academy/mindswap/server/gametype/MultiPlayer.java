package academy.mindswap.server.gametype;

import academy.mindswap.player.Player;
import academy.mindswap.server.Server;
import academy.mindswap.server.questions.Question;
import academy.mindswap.server.util.FilesLoad;
import academy.mindswap.server.util.Messages;
import academy.mindswap.server.util.Timer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.LinkedList;

public class MultiPlayer {
    private LinkedList<Question> questions;
    private FilesLoad fileReader;
    private int difficultyScore;
    private int maxEasyCorrectScore = 3;
    private int maxHardCorrectScore = 6;
    private int answeredQuestions;
    private int correctAnsweredQuestions;

    public MultiPlayer() {
        questions = new LinkedList<>();
        fileReader = new FilesLoad();
    }


//     for (Question question : questions) {
//        while (timer.getSeconds() != 0) {
//
//            timer.setSeconds(timer.getSeconds() - 1);
//
//            String questionBuild = question.toString() + "\n";
//            playerHandler.send("Time left: " + timer.getSeconds());
//            playerHandler.send(questionBuild);
//            String answer = playerHandler.getIn().readLine();
//
//            if (answer.equals(question.getCorrectAnswer())) {
//                playerHandler.setPlayerScore(playerHandler.getPlayerScore() + 1); // add score by difficulty
//                playerHandler.send(Messages.CORRECT_ANSWER);
//                playerHandler.send(("" + playerHandler.getPlayerScore()));
//            } else {
//                playerHandler.send(Messages.INCORRECT_ANSWER);
//            }
//            break;
//        }
//
//    }
//    gameOver();


    public void play(Server server, Server.PlayerHandler playerHandler) throws IOException, InterruptedException {

        Timer timer = new Timer(1, playerHandler.getPlayerSocket());
        Thread timerThread = new Thread(timer);
        timerThread.start();

        while (timer.getSeconds() > 0) {

            if (playerHandler.getPlayerScore() < maxEasyCorrectScore) {
                loadEasyQuestions();
                difficultyScore = 1;
                askQuestions(timer, server, playerHandler, difficultyScore, maxEasyCorrectScore);
            } else if (playerHandler.getPlayerScore() < maxHardCorrectScore) {
                loadHardQuestions();
                difficultyScore = 2;
                askQuestions(timer, server, playerHandler, difficultyScore, maxHardCorrectScore);
            } else {
                loadInsaneQuestions();
                difficultyScore = 5;
                int insaneScore = Integer.MAX_VALUE;
                playerHandler.send(Messages.INSANE_MODE);
                Thread.sleep(7000);
                timer.setSeconds(timer.getSeconds() + 7);
                askQuestions(timer, server, playerHandler, difficultyScore, insaneScore);
            }
        }
        gameOver(server, playerHandler);
    }

    public void header(Timer timer, Server server, Server.PlayerHandler playerHandler) {
        playerHandler.send("--------------------------------------------------------------------------");
        playerHandler.send("Your Score: " + playerHandler.getPlayerScore() + "          Time left: " + timer.getSeconds() + "          Top Player Score: " + server.getHighestScore());
        playerHandler.send("--------------------------------------------------------------------------");
    }

    public void askQuestions(Timer timer, Server server, Server.PlayerHandler playerHandler, int difficultyScore, int maxCorrectScore) throws IOException, InterruptedException {
        for (Question question : questions) {

            while (timer.getSeconds() > 0) {

                String questionBuild = question.toString() + "\n";
                header(timer, server, playerHandler);
                playerHandler.send(questionBuild);

                    String answer = playerHandler.getIn().readLine();
                    Thread.sleep(10);
                    answeredQuestions++;

                if (answer.equals("/menu") || answer.equals("/quit")) {
                    playerHandler.dealWithCommand(answer);
                    return;
                }
                if (answer.equals("/support")) {
                    playerHandler.dealWithCommand(answer);
                    continue;
                }
                if (answer.equals(question.getCorrectAnswer())) {
                    playerHandler.setPlayerScore(playerHandler.getPlayerScore() + difficultyScore); // add score by difficulty
                    correctAnsweredQuestions++;
                    playerHandler.send(Messages.CORRECT_ANSWER);
                    Thread.sleep(500);

                    if (playerHandler.getPlayerScore() >= maxCorrectScore) {
                        return;
                    }

                } else {
                    if (!answer.equals("")){
                        playerHandler.send(Messages.INCORRECT_ANSWER);
                        Thread.sleep(500);
                    }
                }
                break;
            }
        }
    }


    public boolean isGameOver(Timer timer) {
        if (timer.getSeconds() == 0) {
            return true;
        }
        return false;
    }


    //TODO definir quantas questões
    public void gameOver(Server server, Server.PlayerHandler playerHandler) {
        playerHandler.send("Your final score is " + playerHandler.getPlayerScore() + ".\n" + "You got "
                + correctAnsweredQuestions + " correct answers out of " + answeredQuestions + "!");
        server.addScoreToList(playerHandler);
        server.defineHighestScore();
        //e voltar ao menu?
    }


    private LinkedList loadEasyQuestions() throws IOException {
        questions = fileReader.LoadQuestionsFromFile("resources/easy.txt");
        return questions;
    }

    private LinkedList loadHardQuestions() throws IOException {
        questions = fileReader.LoadQuestionsFromFile("resources/hard.txt");
        return questions;
    }

    private LinkedList loadInsaneQuestions() throws IOException {
        questions = fileReader.LoadQuestionsFromFile("resources/insane.txt");
        return questions;
    }



}
















