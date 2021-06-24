package academy.mindswap.server.questions;

import java.util.LinkedList;

public class Question {
    private String question;
    private String correctAnswer;
    private LinkedList<String> answers;

    public Question() {
        this.answers = new LinkedList<>();
    }

    public LinkedList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(String answer) {
        this.answers.add(answer + "\n");
    }

    public String getQuestion() {
        return question;
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

    public boolean isCorrectAnswer(String ans) {
        return correctAnswer.equals(ans);
    }

    @Override
    public String toString() {
        return "\n" + question + "\n" + answers.get(0) + answers.get(1) + answers.get(2) + "\n";
    }
}