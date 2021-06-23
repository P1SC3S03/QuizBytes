package Question;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private String question;
    private String correctAnswer;
    private List<String> answers;

    public Question(){

    }

    public Question(String question, String rightAnswer){
        this.question = question;
        this.correctAnswer = rightAnswer;
        this.answers = new ArrayList<>();
    }

    public List<String> getAnswers() {
        return new ArrayList<>(answers);
    }

    public void setAnswers(String answer) {
        this.answers.add(answer);
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

    public boolean isCorrectAnswer(String ans){
        return correctAnswer.equals(ans);
    }
}