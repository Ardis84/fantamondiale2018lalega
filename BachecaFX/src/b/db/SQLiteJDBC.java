package b.db;

import java.sql.*;

public class SQLiteJDBC
{
  public static void main( String args[] )
  {
    getConnection();
  }
  
  
  public static Connection getConnection(){
	  Connection c = null;
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:test.db");
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    System.out.println("Connessione riuscita");
	   return c;
  }
  
  public static Connection getConnection(String dbname){
	  Connection c = null;
	    try {
	      Class.forName("org.sqlite.JDBC");
	      if(!dbname.contains(".db"))
	    	  dbname = dbname+".db";
	      c = DriverManager.getConnection("jdbc:sqlite:"+dbname);
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    System.out.println("Connessione riuscita");
	   return c;
  }
  
  public static void createTables(){
	    Connection c = getConnection();
	    Statement stmt = null;
	    try {
	      stmt = c.createStatement();
	      String sql = "CREATE TABLE COMPANY " +
	                   "(ID INT PRIMARY KEY     NOT NULL," +
	                   " NAME           TEXT    NOT NULL, " + 
	                   " AGE            INT     NOT NULL, " + 
	                   " ADDRESS        CHAR(50), " + 
	                   " SALARY         REAL)"; 
	      stmt.executeUpdate(sql);
	      stmt.close();
	      c.close();
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    System.out.println("Table created successfully");
  }
  
  public static ResultSet executeQuery(Connection conn, String sql) {
	  ResultSet res=null;
		try {
			Statement stm = conn.createStatement();
			res = stm.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return res;
  }
  
  public static ResultSet executeQuery(String dbname, String sql) {
	  Connection conn = getConnection(dbname);
	  return executeQuery(conn, sql);
  }
  
}