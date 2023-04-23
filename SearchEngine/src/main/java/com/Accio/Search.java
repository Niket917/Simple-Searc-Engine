package com.Accio;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet("/Search")
public class Search extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //Geting Keyboard fron frontend

        String keyboard = request.getParameter("keyboard");

        //Setting up connection to database
        Connection connection = DataBaseConnection.getConnection();
        try {
            //Store the query of user in history
          PreparedStatement preparedStatement = connection.prepareStatement("Insert into history values(?,?);");
            preparedStatement.setString(1, keyboard);
            preparedStatement.setString(2,"http://localhost:8080/SearchEngine/Search?keyboard="+keyboard);
            preparedStatement.executeUpdate();


            //Getting result after running the ranking query
        ResultSet resultSet = connection.createStatement().executeQuery("select pageTitle,pageLink,(length(lower(pageText)) - length(replace(lower(pageText), '" + keyboard.toLowerCase() + "', '')))/length('" + keyboard.toLowerCase() + "') as countoccurences from pages order by countoccurences desc limit 30;\n");

        ArrayList<SearchResult> results = new ArrayList<SearchResult>();
        //Transffering values from resultset to result ArrayList
        while (resultSet.next()) {
            SearchResult searchResult = new SearchResult();
            searchResult.setTitle(resultSet.getString("pageTitle"));
            searchResult.setLink(resultSet.getString("pageLink"));
            results.add(searchResult);
        }

        //Displaying results arraylist is consel
        for(SearchResult result : results){
            System.out.println(result.getTitle()+"\n"+result.getLink()+"\n");
        }

        request.setAttribute("results" ,results);
        request.getRequestDispatcher("Search.jsp").forward(request,response);

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
    }
        catch (SQLException | ServletException sqlException){
            sqlException.printStackTrace();
        }

    }
}
