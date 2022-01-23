package com.codecrafters.export;

import com.codecrafters.Files;

import java.io.File;
import java.io.IOException;

public class VoteExporterFactory {

    public static VoteExporter createExporterForJson(String fileName) throws IOException {
        return JsonVoteExporter.create(new File(Files.EXPORT_TARGET_DIR, fileName + ".json"));
    }

    public static VoteExporter createExporterForCsv(String fileName) throws IOException {
        return CsvVoteExporter.create(new File(Files.EXPORT_TARGET_DIR, fileName + ".csv"));
    }

    public static VoteExporter createExporterFor(VoteExportFormat format, String fileName) throws IOException {
        switch (format) {
            case CSV:
                return createExporterForCsv(fileName);
            case JSON:
                return createExporterForJson(fileName);
            default:
                return null;
        }
    }

}
