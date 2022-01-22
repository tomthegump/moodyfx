package com.codecrafters.load;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.codecrafters.data.Config;
import com.codecrafters.data.Survey;

import java.io.File;
import java.io.IOException;

/**
 * Created by Ingo on 05.06.2016.
 */
public class SurveyLoader {

    public static Survey loadSurveyFromJson(File jsonFile) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonFile, Survey.class);
    }

    public static Config loadConfigFromJson(File jsonFile) throws IOException {
        if(jsonFile.exists()) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(jsonFile, Config.class);
        } else {
            return new Config();
        }
    }
}
