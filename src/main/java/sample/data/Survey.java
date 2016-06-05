package sample.data;

/**
 * Created by Ingo on 19.05.2016.
 */
public class Survey {

    public enum IconType {
        HAND("hand"),
        FACE("face");

        private String resourceName;

        IconType(String resourceName) {
            this.resourceName = resourceName;
        }

        public String getResourceName(int index) {
            return index + "-" + resourceName + ".png";
        }
    }

    public enum AnswerType {
        YES_NO, YES_NO_MEEH, RATING
    }

    private final int id;
    private final String question;
    private final AnswerType answerType;
    private final IconType iconType;

    private Survey() {
        // used by json
        this(0, null, null, null);
    }

    public Survey(int id, String question) {
        this(id, question, AnswerType.RATING);
    }

    public Survey(int id, String question, AnswerType answerType) {
        this(id, question, answerType, IconType.FACE);
    }

    public Survey(int id, String question, AnswerType answerType, IconType iconType) {
        this.id = id;
        this.question = question;
        this.answerType = answerType;
        this.iconType = iconType;
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

    public IconType getIconType() {
        return iconType;
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
