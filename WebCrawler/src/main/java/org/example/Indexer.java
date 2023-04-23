package org.example;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Indexer {
    static Connection connection = null;
    Indexer(Document document, String url) {
        //Select Important element of Document
        String title = document.title();
        String link = url;
        String text = document.text();

        try{
        connection = DataBaseConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("Insert into pages values(?,?,?);");
        preparedStatement.setString(1, title);
        preparedStatement.setString(2, link);
        preparedStatement.setString(3, text);
        preparedStatement.executeUpdate();
    }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }

    }
}
