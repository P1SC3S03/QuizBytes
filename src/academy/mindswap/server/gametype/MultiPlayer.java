package academy.mindswap.server.gametype;

import academy.mindswap.server.Server;
import academy.mindswap.server.questions.Question;
import academy.mindswap.server.util.FilesLoad;
import academy.mindswap.server.util.Messages;

import java.io.IOException;
import java.util.LinkedList;

public class MultiPlayer {


    public synchronized void play(Server server, Server.PlayerHandler playerHandler) throws InterruptedException, IOException {

        while (server.getMultiPlayerList().size() % 2 != 0) {
            playerHandler.send(Messages.WAIT_FOR_ANOTHER_PLAYER);
            playerHandler.wait();
        }
        if (server.getMultiPlayerList().size() >= 2) {
            notifyAll();
        }
        FilesLoad fr = new FilesLoad();
        LinkedList<Question> questions = fr.LoadQuestionsFromFile("resources/easy.txt");

        for (Question question : questions) {
            String questionBuild = question.toString();
            server.broadcast(questionBuild);

            for (Server.PlayerHandler eachPlayerHandler : server.getMultiPlayerList()) {
                String answer="";
                    answer = eachPlayerHandler.getIn().readLine();
                if (answer.equals(question.getCorrectAnswer())) {
                    eachPlayerHandler.setPlayerScore(eachPlayerHandler.getPlayerScore() + 10); // add score by difficulty
                    eachPlayerHandler.send("Correct");
                    eachPlayerHandler.send(("" + eachPlayerHandler.getPlayerScore()));
                } else {
                    eachPlayerHandler.send("Incorrect");
                }
            }
        }
    }
}

















