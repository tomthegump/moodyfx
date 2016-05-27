package sample.export;

import java.io.*;

/**
 * Created by Ingo on 27.05.2016.
 */
public class JsonExportFile {

    private final BufferedWriter fileWriter;
    private boolean firstEntry = true;

    public static JsonExportFile createExportFile(String key, File exportFile) throws IOException {
        exportFile.getAbsoluteFile().getParentFile().mkdirs();
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(exportFile)));
        bufferedWriter.append("{\"").append(key).append("\":[");
        return new JsonExportFile(bufferedWriter);
    }

    private JsonExportFile(BufferedWriter fileWriter) {
        this.fileWriter = fileWriter;
    }

    public JsonExportFile append(String jsonString) {
        try {
            if(!firstEntry) {
                fileWriter.append(",");
            }
            fileWriter.append(jsonString);
            firstEntry = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public void close() throws IOException {
        fileWriter.append("]}");
        fileWriter.flush();
        fileWriter.close();
    }
}
