package academy.mindswap.server.util;

import academy.mindswap.server.questions.Question;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Files Load Class
 */
public class FilesLoad {
    /**
     * Reads text files that contain several questions.
     * Divide each line into the proper format [question, answers & correct answer] using regex.
     * After every question is obtained, it returns a list of question objects.
     * This method can be used for any file path given.
     *
     * @param path - the file path to the text file.
     * @return list of questions.
     * @throws IOException If an input or output exception occurs.
     */
    public LinkedList LoadQuestionsFromFile(String path) throws IOException {
        LinkedList<Question> listOfQuestions = new LinkedList<>();
        String line;
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String[] questionStructure = null;

        while ((line = reader.readLine()) != null) {
            questionStructure = line.split(";");
            Question question = new Question();
            question.setQuestion(questionStructure[0]);

            for (int i = 1; i < questionStructure.length - 1; i++) {
                question.setAnswers(questionStructure[i]);
            }
            question.setCorrectAnswer(questionStructure[questionStructure.length - 1]);
            listOfQuestions.add(question);
        }
        return listOfQuestions;
    }
}