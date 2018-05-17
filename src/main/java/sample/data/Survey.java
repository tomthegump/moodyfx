package sample.data;

/**
 * Created by Ingo on 19.05.2016.
 */
public class Survey {

    public enum AnswerType {
        YES_NO, YES_NO_MEEH, RATING
    }

    private final int id;
    private final String question;
    private final AnswerType answerType;

    private Survey() {
        // used by json
        this(0, null, null);
    }

    public Survey(int id, String question) {
        this(id, question, AnswerType.RATING);
    }

    public Survey(int id, String question, AnswerType answerType) {
        this.id = id;
        this.question = question;
        this.answerType = answerType;
    }

    public int getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public AnswerType getAnswerType() {
        return answerType;
    }

    @Override
    public String toString() {
        return "Survey{" +
                "id=" + id +
                ", question='" + question + '\'' +
                ", answerType=" + answerType +
                '}';
    }
}
