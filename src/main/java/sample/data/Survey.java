package sample.data;

/**
 * Created by Ingo on 19.05.2016.
 */
public class Survey {

    public enum AnswerType {
        HAND("hand"),
        FACE("face");

        private String resourceName;

        AnswerType(String resourceName) {
            this.resourceName = resourceName;
        }

        public String getResourceName(int index) {
            return index + "-" + resourceName + ".png";
        }
    }

    private int id;
    private String question;
    private AnswerType answerType;

    public Survey(int id, String question) {
        this(id, question, AnswerType.FACE);
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
