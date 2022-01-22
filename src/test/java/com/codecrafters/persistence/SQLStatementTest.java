package com.codecrafters.persistence;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Ingo on 25.05.2016.
 */
public class SQLStatementTest {

    @Test
    public void select() throws Exception {
        System.out.println(SQLStatement.select("*").from("votes")
                .where("surveyId = 1").or("surveyId = 2").and("question like '%How%'"));
    }

}