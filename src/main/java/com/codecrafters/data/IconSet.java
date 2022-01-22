package com.codecrafters.data;

import com.codecrafters.Files;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class IconSet {
    private final String iconSetName;

    public IconSet(String iconSetName) {
        this.iconSetName = iconSetName;
    }

    public URL getImageUrl(int index) throws MalformedURLException {
        return new File(Files.ICONS_FOLDER, iconSetName + "/" + index + ".png").toURI().toURL();
    }
}
