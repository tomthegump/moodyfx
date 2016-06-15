package sample.export;

import java.io.*;

/**
 * Created by Ingo on 27.05.2016.
 */
public class JsonExportFile {

    private final BufferedWriter fileWriter;

    public static JsonExportFile createExportFile(File exportFile) throws IOException {
        exportFile.getAbsoluteFile().getParentFile().mkdirs();
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(exportFile)));
        return new JsonExportFile(bufferedWriter);
    }

    private JsonExportFile(BufferedWriter fileWriter) {
        this.fileWriter = fileWriter;
    }

    public JsonExportFile append(String value) {
        try {
            fileWriter.append(value);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public void close() throws IOException {
        fileWriter.flush();
        fileWriter.close();
    }
}
