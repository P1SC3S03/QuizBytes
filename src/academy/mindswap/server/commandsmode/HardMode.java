package academy.mindswap.server.commandsmode;

import academy.mindswap.server.Server;
import academy.mindswap.server.questions.Question;
import academy.mindswap.server.util.FilesLoad;

import java.io.IOException;
import java.util.LinkedList;

public class HardMode implements CommandHandler{
    @Override
    public void execute(Server server, Server.PlayerHandler playerHandler) throws IOException, InterruptedException {

    }

    public LinkedList<Question> loadHardQuestions() throws IOException{
        FilesLoad fr = new FilesLoad();
        LinkedList<Question> questions = fr.LoadQuestionsFromFile("resources/hard.txt");
        return questions;
    }

}
