package academy.mindswap.server.util;

import academy.mindswap.server.questions.Question;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class FilesLoad {
    public LinkedList LoadQuestionsFile(String path) throws IOException {
        LinkedList<Question> listOfQuestions = new LinkedList<>();
        String strCurrentLine;
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String[] strs = null;
        while ((strCurrentLine = reader.readLine()) != null) {
            strs = strCurrentLine.split(";");
            Question question = new Question();
            question.setQuestion(strs[0]);
            for (int i = 1; i < 4; i++) {
                question.setAnswers(strs[i]);
            }
            question.setCorrectAnswer(strs[4]);
            listOfQuestions.add(question);
        }
        return listOfQuestions;
    }
}