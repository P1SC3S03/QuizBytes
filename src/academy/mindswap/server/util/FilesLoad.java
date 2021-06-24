package academy.mindswap.server.util;

import academy.mindswap.server.questions.Question;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class FilesLoad {

    public List LoadQuestionsFile(String path) throws IOException {

        List<Question> listOfQuestions = new LinkedList<>();
        String strCurrentLine;

        BufferedReader reader = new BufferedReader(new FileReader(path));

        String[] strs = null;
        while ((strCurrentLine = reader.readLine()) != null) {
            strs = strCurrentLine.split(";");
            System.out.println(strs.length);
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