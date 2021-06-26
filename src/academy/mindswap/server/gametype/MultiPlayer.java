package academy.mindswap.server.gametype;

import academy.mindswap.server.Server;
import academy.mindswap.server.questions.Question;
import academy.mindswap.server.util.FilesLoad;
import academy.mindswap.server.util.Messages;
import academy.mindswap.server.util.Timer;

import java.io.IOException;
import java.util.LinkedList;

public class MultiPlayer {


    public void play(Server server, Server.PlayerHandler playerHandler) throws IOException {

        Timer timer = new Timer(1, playerHandler.getPlayerSocket());
        Thread timerThread = new Thread(timer);

        FilesLoad fileReader = new FilesLoad();
        LinkedList<Question> questions = fileReader.LoadQuestionsFromFile("resources/easy.txt");

        timerThread.start();  //Print currentScore + Time + Messages -> Clean console + PrintState -> Actualizar timer na consola \r -> Reescrever

        while (timerThread.isAlive()) {

            for (Question question : questions) {
                String questionBuild = question.toString() + "\n";
                playerHandler.send("Time left: " + timer.getSeconds());
                playerHandler.send(questionBuild);
                String answer = playerHandler.getIn().readLine();


                if (answer.equals(question.getCorrectAnswer())) {
                    playerHandler.setPlayerScore(playerHandler.getPlayerScore() + 1); // add score by difficulty
                    playerHandler.send(Messages.CORRECT_ANSWER);
                    playerHandler.send(("" + playerHandler.getPlayerScore()));
                } else {
                    playerHandler.send(Messages.INCORRECT_ANSWER);
                }
            }

        }
        playerHandler.send("GAME OVER!\nYour score is " + playerHandler.getPlayerScore() + " out of "
                + questions.size() + "!");
        server.addScoreToList(playerHandler);
    }
}
















