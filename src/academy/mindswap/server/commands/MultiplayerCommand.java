package academy.mindswap.server.commands;

import academy.mindswap.server.Server;
import academy.mindswap.server.questions.Question;
import academy.mindswap.server.util.FilesLoad;
import academy.mindswap.server.util.Messages;

import java.io.IOException;
import java.util.LinkedList;

public class MultiplayerCommand implements CommandHandler {
    @Override
    public synchronized void execute(Server server, Server.PlayerHandler playerHandler)
            throws IOException, InterruptedException {

        while (server.getMultiPlayerList().size() % 2 != 0) {
            playerHandler.send(Messages.WAIT_FOR_ANOTHER_PLAYER);
            playerHandler.wait();
        }
        if (server.getMultiPlayerList().size() >= 2) {
            notifyAll();
        }
        FilesLoad fr = new FilesLoad();
        LinkedList<Question> questions = fr.LoadQuestionsFile("resources/easy.txt");
        //Timer timer = new Timer(1);
        //timer.start();
        // while (!timer.getSeconds().equals("0")) {
        //   playerHandler.send((timer.getSeconds()));
        for (Question q : questions) {
            String questionBuild = "";
            //Timer timer = new Timer(1);
            //timer.start();
            questionBuild += q.toString();
            for (Server.PlayerHandler p : server.getMultiPlayerList()) {
                p.send(questionBuild);
                String answer = p.getIn().readLine();
                p.send(answer);
                if (answer.equals(q.getCorrectAnswer())) {
                    p.setPlayerScore(p.getPlayerScore() + 1); // add score by difficulty
                    p.send("Correct");
                    p.send(("" + p.getPlayerScore()));
                } else {
                    p.send("Incorrect");
                }
            }
        }
    }
    //}
}