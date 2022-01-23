package com.codecrafters;

import java.io.File;

public final class Files {

    private final static File BASE_DIR = new File(System.getProperty("base.dir"));

    public final static File SURVEY = new File(BASE_DIR, "config/survey.json");
    public final static File CONFIG = new File(BASE_DIR, "config/config.json");
    public final static File ICONS_FOLDER = new File(BASE_DIR, "icons");
    public final static File EXPORT_TARGET_DIR = new File(BASE_DIR, "exports");
    public final static File PERSISTENCE_DIR = new File(BASE_DIR, "data");

    private Files() {
        //no instance
    }

}
