package com.codecrafters;

import java.io.File;

public final class Files {

    private final static File BASE_DIR = new File(System.getProperty("base.dir"));
    public final static File SURVEY = new File(BASE_DIR, "survey.json");
    public final static File CONFIG = new File(BASE_DIR, "config.json");
    public final static File ICONS_FOLDER = new File(BASE_DIR, "icons");

    private Files() {
        //no instance
    }

}
