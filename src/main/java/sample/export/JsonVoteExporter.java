package sample.export;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import sample.data.Vote;

import java.io.*;

/**
 * Created by Ingo on 27.05.2016.
 */
public class JsonVoteExporter implements VoteExporter {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final BufferedWriter fileWriter;

    private boolean containsData = false;

    public static JsonVoteExporter create(File exportFile) throws IOException {
        exportFile.getAbsoluteFile().getParentFile().mkdirs();
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(exportFile)));
        return new JsonVoteExporter(bufferedWriter);
    }

    private JsonVoteExporter(BufferedWriter fileWriter) throws IOException {
        this.fileWriter = fileWriter;
        this.fileWriter.write("[");
        this.fileWriter.newLine();
    }

    @Override
    public JsonVoteExporter append(Vote value) throws IOException {
        if(containsData) {
            fileWriter.append(",");
            fileWriter.newLine();
        }
        fileWriter.append("   ");
        fileWriter.append(toJson(value));
        containsData = true;
        return this;
    }

    @Override
    public void close() throws IOException {
        fileWriter.newLine();
        fileWriter.write("]");
        fileWriter.flush();
        fileWriter.close();
    }

    private static String toJson(Object object) throws IOException {
        return OBJECT_MAPPER.writeValueAsString(object);
    }
}
