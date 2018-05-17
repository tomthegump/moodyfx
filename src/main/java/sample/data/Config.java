package sample.data;

public class Config {

    private String iconSet = "hand";
    private String site = "";
    private int thanksDisplayDuration = 5;

    public int getThanksDisplayDuration() {
        return thanksDisplayDuration;
    }

    public String getSite() {
        return site;
    }

    public IconSet getIconSet() {
        return new IconSet(iconSet);
    }
}
