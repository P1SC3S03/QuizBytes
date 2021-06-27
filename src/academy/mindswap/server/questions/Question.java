package academy.mindswap.server.questions;

import java.util.LinkedList;

/**
 * Question class
 */
public class Question {
    private String question;
    private String correctAnswer;
    private LinkedList<String> answers;

    /**
     * Constructor for the Question class.
     * Creates a linked list that is going to contain the answers to a given question.
     */
    public Question() {
        this.answers = new LinkedList<>();
    }

    /**
     * Setter for the answers list
     * [Used in Util package -> FilesLoad Class]
     *
     * @param answer answer String to be added to the answers list
     */
    public void setAnswers(String answer) {
        this.answers.add(answer + "\n");
    }

    /**
     * Setter for the questions
     *
     * @param question sets the question to the given String
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * Getter for the correct answer
     *
     * @return returns the correct answer
     */
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    /**
     * Setter for the correct answer
     *
     * @param answer sets the answer to the given String.
     */
    public void setCorrectAnswer(String answer) {
        this.correctAnswer = answer;
    }

    /**
     * Override of the toString method to our quizz format.
     *
     * @return the questions and answers in a specific structure.
     */
    @Override
    public String toString() {
        String questionsStructure = "\n" + question + "\n\n";

        for (int i = 0; i < answers.size(); i++) {
            questionsStructure += answers.get(i);
        }
        return questionsStructure;
    }
}