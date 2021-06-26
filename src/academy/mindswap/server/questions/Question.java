package academy.mindswap.server.questions;

import java.util.LinkedList;

public class Question {
    private String question;
    private String correctAnswer;
    private LinkedList<String> answers;

    public Question() {
        this.answers = new LinkedList<>();
    }

    public void setAnswers(String answer) {
        this.answers.add(answer + "\n");
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String answer) {
        this.correctAnswer = answer;
    }

    @Override
    public String toString() {
        String questionsStructure = "\n" + question + "\n";

        for (int i = 0; i < answers.size(); i++){
            questionsStructure += answers.get(i);
        }
        return questionsStructure;
    }
}