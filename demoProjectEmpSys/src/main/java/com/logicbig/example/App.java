package com.logicbig.example;
import com.google.gson.Gson;
import com.model.movies.*;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import com.mysql.cj.jdbc.Driver;
import com.mysql.cj.jdbc.result.ResultSetMetaData;

@ApplicationPath("/api")
public class App extends Application {

    private static final Logger logger = Logger.getLogger(Resource.class.getName());
    private static final String USER = "root";
    private static final String PASSWORD = "admin1234";

    private static final String URL = "jdbc:mysql://localhost:3306/test";

    public static List<String> getMovieTitleFromDatabase(){

        List<String> listOfMovieNames = new ArrayList<>();

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL,USER,PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rs;

            rs = stmt.executeQuery("SELECT * FROM MOVIES" + " ORDER by _userRatings DESC LIMIT 5");
            while ( rs.next() ) {
                String title = rs.getString("_title");
                listOfMovieNames.add(title);
            }
            conn.close();

        }
        catch (Exception e){
            e.printStackTrace();
        }

        return listOfMovieNames;
    }


    public static String createMovieEntryInDatabase(String movieDetails) {
        MovieModel movie = new Gson().fromJson(movieDetails,MovieModel.class);

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL,USER,PASSWORD);

            String sqlInsertQuery = "INSERT INTO movies (_title, _description, _releaseYear, _geners, _userRatings)" + "VALUES (?,?,?,?,?)";

            PreparedStatement pstmt = conn.prepareStatement(sqlInsertQuery,Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, movie.getTitle());
            pstmt.setString(2, movie.getDescription());
            pstmt.setString(3, movie.getReleaseYear());
            pstmt.setString(4, movie.getGenres());
            pstmt.setDouble(5, movie.getUserRatings());

            int affectedRows =  pstmt.executeUpdate();

            int id = 0;

            if(affectedRows > 0){
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if(generatedKeys.next()){
                  id = generatedKeys.getInt(1);
                }
            }

            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM MOVIES WHERE id = ?");
            stmt.setInt(1,id);

            ResultSet rs = stmt.executeQuery();



            if (rs.next()) {
                int movieId = rs.getInt("id");
                movie.setId(movieId);
            }


            conn.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return new Gson().toJson(movie);
    }

    public static boolean changeMovieEntryInDatabase(String payLoad) {

        MovieModel movie = new Gson().fromJson(payLoad,MovieModel.class);
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL,USER,PASSWORD);

            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM MOVIES WHERE id=?");
            pstmt.setInt(1,movie.getId());

            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                MovieModel movieDetailsInDatabase = new MovieModel(rs.getInt("id"),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getDouble(6));

                movie.print();
                System.out.println("--------------");
                movieDetailsInDatabase.print();


                if (!movie.getTitle().equals(movieDetailsInDatabase.getTitle())
                        || !movie.getDescription().equals(movieDetailsInDatabase.getDescription())
                        || !movie.getReleaseYear().equals(movieDetailsInDatabase.getReleaseYear())
                        || !movie.getGenres().equals(movieDetailsInDatabase.getGenres())
                        || movie.getUserRatings() != movieDetailsInDatabase.getUserRatings()) {


                    PreparedStatement updateStmt = conn.prepareStatement(
                            "UPDATE MOVIES SET _title=?, _description=?, _releaseYear=?, _geners=?, _userRatings=? WHERE id=?");
                    updateStmt.setString(1, movie.getTitle());
                    updateStmt.setString(2, movie.getDescription());
                    updateStmt.setString(3, movie.getReleaseYear());
                    updateStmt.setString(4, movie.getGenres());
                    updateStmt.setDouble(5, movie.getUserRatings());
                    updateStmt.setInt(6, movie.getId());

                    // Execute the update statement
                    updateStmt.executeUpdate();
                }

            }

            return true;
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public static boolean deleteMovieEntryFromDatabase(int movieId) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM MOVIES WHERE id=?");
            pstmt.setInt(1, movieId);

            int affectedRows = pstmt.executeUpdate();

            return affectedRows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    public static boolean authenticateUserFromDatabase(String username, String password){

        try{

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL,USER,PASSWORD);

            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM USER WHERE username=? AND password=?");
            pstmt.setString(1,username);
            pstmt.setString(2,password);

            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                return true;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return false;
    }

}
