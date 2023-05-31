package com.MajorProject;

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
//        Getting keyWord from frontEnd
        String keyWord = request.getParameter("Keyword");
//        setting up connection with dataBase
        Connection connection = DatabaseConnection.getConnection();
        try {
//            Storing the querry for user in history
            PreparedStatement preparedStatement = connection.prepareStatement("Insert into history values(?, ?);");
            preparedStatement.setString(1, keyWord);
            preparedStatement.setString(2, "http://localhost:8080/SearchEngine/Search?keyword="+keyWord);
            preparedStatement.executeUpdate();

//            Getting results afterrunning the ranking query
            ResultSet resultSet = connection.createStatement().executeQuery("select pageTitle, pageLink, (length(lower(pageText))-length(replace(lower(pageText), '" + keyWord.toLowerCase() + "', '')))/length('" + keyWord.toLowerCase() + "') as countoccurance from pages order by countoccurance desc limit 30;");
            ArrayList<SearchResult> results = new ArrayList<SearchResult>();
//            transfering values from resultset to result arrayList
            while (resultSet.next()) {
                SearchResult searchResult = new SearchResult();
                searchResult.setTitle(resultSet.getString("pageTitle"));
                searchResult.setLink(resultSet.getString("pageLink"));
                results.add(searchResult);

            }
//            printing the arraylist
            for(SearchResult result : results){
                System.out.println(result.getTitle()+"/n"+result.getLink()+"/n");
            }
            request.setAttribute("results", results);
            request.getRequestDispatcher("Search.jsp").forward(request, response);
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
        }catch (SQLException | ServletException sqlException){
            sqlException.printStackTrace();
        }
    }
}
