package sample;

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

    private String question;
    private AnswerType answerType;

    public Survey(String question) {
        this(question, AnswerType.FACE);
    }

    public Survey(String question, AnswerType answerType) {
        this.question = question;
        this.answerType = answerType;
    }

    public String getQuestion() {
        return question;
    }

    public AnswerType getAnswerType() {
        return answerType;
    }
}
