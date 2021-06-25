package academy.mindswap.server.gametype;

import academy.mindswap.server.Server;
import academy.mindswap.server.questions.Question;
import academy.mindswap.server.util.FilesLoad;
import academy.mindswap.server.util.Messages;

import java.io.IOException;
import java.util.LinkedList;

public class MultiPlayer {


    public void play(Server server, Server.PlayerHandler playerHandler) throws InterruptedException, IOException {

        FilesLoad fr = new FilesLoad();
        LinkedList<Question> questions = fr.LoadQuestionsFromFile("resources/easy.txt");
        for (Question question : questions) {
            String questionBuild = question.toString();
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
}

















