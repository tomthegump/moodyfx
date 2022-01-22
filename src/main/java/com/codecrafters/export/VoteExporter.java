package com.codecrafters.export;

import com.codecrafters.data.Vote;

import java.io.IOException;


public interface VoteExporter extends AutoCloseable {

    VoteExporter append(Vote vote) throws IOException;

}
