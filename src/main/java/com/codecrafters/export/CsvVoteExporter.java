package com.codecrafters.export;

import com.codecrafters.data.Vote;

import java.io.*;

/**
 * Created by Ingo on 27.05.2016.
 */
public class CsvVoteExporter implements VoteExporter {

    private final BufferedWriter fileWriter;

    public static CsvVoteExporter create(File exportFile) throws IOException {
        exportFile.getAbsoluteFile().getParentFile().mkdirs();
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(exportFile)));
        return new CsvVoteExporter(bufferedWriter);
    }

    private CsvVoteExporter(BufferedWriter fileWriter) throws IOException {
        this.fileWriter = fileWriter;
        this.fileWriter.write("timestamp,surveyId,votedPoints,site");
        this.fileWriter.newLine();
    }

    @Override
    public CsvVoteExporter append(Vote vote) throws IOException {
        fileWriter.append(String.valueOf(vote.getTimestamp().getTime()));
        fileWriter.append(",");
        fileWriter.append(String.valueOf(vote.getSurveyId()));
        fileWriter.append(",");
        fileWriter.append(String.valueOf(vote.getVotedPoints()));
        fileWriter.append(",");
        fileWriter.append(vote.getSite());
        fileWriter.newLine();
        return this;
    }

    @Override
    public void close() throws IOException {
        fileWriter.flush();
        fileWriter.close();
    }

}
