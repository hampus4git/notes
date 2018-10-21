package com.example.documentApp.core;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NoteMapper implements ResultSetMapper<Note> {

    private static final String ID = "ID";
    private static final String TITLE = "title";
    private static final String CONTENT = "content";
    private static final String USER = "user";

    @Override
    public Note map(int i, ResultSet resultSet, StatementContext statementContext) {
        return new Note(getInt(ID, resultSet, -1), getString(TITLE, resultSet, ""), getString(CONTENT, resultSet, ""), getString(USER, resultSet, ""));
    }

    private String getString(String columnName, ResultSet resultSet, String defaultValue){
        try{
            return resultSet.getString(columnName);
        }catch(SQLException e){
            return defaultValue;
        }
    }

    private int getInt(String columnName, ResultSet resultSet, int defaultValue){
        try{
            return resultSet.getInt(columnName);
        }catch(SQLException e){
            return defaultValue;
        }
    }
}
