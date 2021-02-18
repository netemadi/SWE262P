import java.io.File;
import java.io.IOException;
import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TwentySix{

  public static void main(String[] args)
  {
    check_arguments(args);

    Connection connection = null;
    try{
      connection = create_db_Schema(connection);
      load_file_into_db(args[0], connection);

      //run query
      connection = DriverManager.getConnection("jdbc:sqlite:twentySix.db");
      Statement statement = connection.createStatement();
      ResultSet rs = statement.executeQuery("SELECT value, COUNT(*) as C FROM words GROUP BY value ORDER BY C DESC");
      int count = 0;
      while(rs.next()){
          if(count < 25){
              System.out.println(rs.getString(1) + " - " + rs.getInt(2));
              count++;
          }
          else{break;}
      }
    }
    catch(SQLException e)
    {
      // if the error message is "out of memory",
      // it probably means no database file is found
      System.err.println(e);
      e.printStackTrace();
    }
    finally
    {
      try
      {
        if(connection != null)
          connection.close();
      }
      catch(SQLException e)
      {
        // connection close failed.
        System.err.println(e);
        e.printStackTrace();
        
      }
    }   
  }

  //citation: https://github.com/xerial/sqlite-jdbc
  private static Connection create_db_Schema(Connection connection) throws SQLException{
    // create a database connection
          connection = DriverManager.getConnection("jdbc:sqlite:twentySix.db");
          Statement statement = connection.createStatement();
          statement.setQueryTimeout(30);  // set timeout to 30 sec.

          statement.executeUpdate("drop table if exists documents");
          statement.executeUpdate("create table documents (id INTEGER PRIMARY KEY AUTOINCREMENT, name)");
           statement.executeUpdate("drop table if exists words");
          statement.executeUpdate("create table words (id, doc_id, value)");
           statement.executeUpdate("drop table if exists characters");
          statement.executeUpdate("create table characters (id, word_id, value)");
          return connection;
  }

  private static void load_file_into_db(String path, Connection connection) throws SQLException{
    LinkedList<String> words = extract_words(path);
    Statement statement = connection.createStatement();
    String s = "'"+path+"'";
    statement.executeUpdate("INSERT INTO documents (name) VALUES (" + s + ")") ;
    ResultSet rs = statement.executeQuery("SELECT id from documents WHERE name=" + s);
    int doc_id = -1;
    if(rs.next()){
      doc_id = rs.getInt("id");
    }
    int word_id = -1;
    try{
      rs = statement.executeQuery("SELECT MAX(id) FROM words");
      word_id = rs.getInt(1);
    }
    catch(SQLException e){
      word_id = 0;
    }

    connection.setAutoCommit(false);
    for(String word: words){
      //c.execute("INSERT INTO words VALUES (?, ?, ?)", (word_id, doc_id, w))
      s = "INSERT INTO words VALUES ('" + 
            word_id + "' , '" + doc_id + "', '" + word + "')";
      statement.executeUpdate(s);
      int char_id = 0;
      for(int i=0; i< word.length(); i++){
        Character c = word.charAt(i);
        //c.execute("INSERT INTO characters VALUES (?, ?, ?)", (char_id, word_id, char))
        s = "INSERT INTO characters VALUES ('" + 
            char_id + "' , '" + word_id + "', '" + c + "')";
        statement.executeUpdate(s);
        char_id += 1;
      }
      word_id += 1;
    }

    connection.commit();
    connection.close();
  }

  private static LinkedList<String> extract_words(String path){
    LinkedList<String> words = new LinkedList<String>();
    LinkedList<String> stopWords = new LinkedList<>();
    try{
      File file = new File("../stop_words.txt"); 
        Scanner sc = new Scanner(file); 
        while(sc.hasNextLine()){
            String line = sc.nextLine();
            String[] splitted = line.split(",");
            for (int i = 0; i<splitted.length; i++){
                stopWords.add(splitted[i]);
            }
        }
        sc.close();

        file = new File(path);
        sc = new Scanner(file);
        while (sc.hasNextLine()) {
            String line = sc.nextLine().toLowerCase();
            line = line.replaceAll("[^a-z0-9]", " ");
            String[] result = line.split(" ");
            for(int i =0 ; i < result.length;i++){
              String word = result[i];
              if (!(word.length() < 2) && !stopWords.contains(word)){
                words.add(word);
              }
            }
        }
        sc.close();
    }
    catch(Exception e){
      System.out.println("Couldn't extract words: " + e.getMessage());
    }

    return words;
  }

  private static void check_arguments(String[] args){
    if(!(args.length > 0)){
        System.out.println("Error: file name must be passed as argument");
        System.exit(0);
    }
  } 
}