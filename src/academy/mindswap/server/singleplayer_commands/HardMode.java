package academy.mindswap.server.singleplayer_commands;

import academy.mindswap.server.Server;
import academy.mindswap.server.questions.Question;
import academy.mindswap.server.util.FilesLoad;
import academy.mindswap.server.util.Messages;

import java.io.IOException;
import java.util.LinkedList;

public class HardMode implements CommandHandler{
    private int difficultyScore = 10;

    @Override
    public void execute(Server server, Server.PlayerHandler playerHandler) throws IOException, InterruptedException {
        runQuestions(server, playerHandler, loadHardQuestions());
    }

    public LinkedList<Question> loadHardQuestions() throws IOException{
        FilesLoad fileReader = new FilesLoad();
        LinkedList<Question> questions = fileReader.LoadQuestionsFromFile("resources/hard.txt");
        return questions;
    }

    public void runQuestions(Server server, Server.PlayerHandler playerHandler, LinkedList<Question> questions)
            throws IOException, InterruptedException {

        for (Question question : questions) {
            playerHandler.send(question.toString());
            String answer = playerHandler.getIn().readLine();

            if(answer.equals("/menu") || answer.equals("/quit")){
                playerHandler.dealWithCommand(answer);
                return;
            }
            if (answer.equals(question.getCorrectAnswer())) {
                playerHandler.setPlayerScore(playerHandler.getPlayerScore() + difficultyScore);
                playerHandler.send(Messages.CORRECT_ANSWER);
                playerHandler.send(("" + playerHandler.getPlayerScore()));
            } else {
                if(playerHandler.isCommand(answer)){
                playerHandler.dealWithCommand(answer);
            }
                playerHandler.send(Messages.INCORRECT_ANSWER);
            }
        }
        playerHandler.send("Your score is " + playerHandler.getPlayerScore() + " out of "
                + questions.size() + "!");
    }
}
