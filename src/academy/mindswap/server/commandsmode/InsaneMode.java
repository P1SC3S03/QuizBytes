package academy.mindswap.server.commandsmode;

import academy.mindswap.server.questions.Question;
import academy.mindswap.server.util.FilesLoad;

import java.io.IOException;
import java.util.LinkedList;

public class InsaneMode {

    public LinkedList<Question> loadInsaneQuestions() throws IOException {
        FilesLoad fr = new FilesLoad();
        LinkedList<Question> questions = fr.LoadQuestionsFromFile("resources/insane.txt");
        return questions;
    }
}
