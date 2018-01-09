package b.db;

import java.sql.Connection;
import java.sql.Statement;

public class TerritoriTables {

	
	 public static void main( String args[] )
	  {
		 createTables();
	  }
	
	  public static void createTables(){
		    Connection c = SQLiteJDBC.getConnection("territori");
		    Statement stmt = null;
		    try {
		      stmt = c.createStatement();
		      
		      /* TABELLE */
		      String sql = "CREATE TABLE TERRITORI " +
		                   "(ID INT PRIMARY KEY     NOT NULL," +
		                   " NUMERO         INT    NOT NULL, " + 
		                   " DESCRIZIONE    VARCHAR(200)     NOT NULL, " + 
		                   " NOTE        	VARCHAR(2000) " + 
		                   ")"; 
		      stmt.executeUpdate(sql);
		      
		      sql = "CREATE TABLE CIVICI " +
	                   "(ID INT PRIMARY KEY     NOT NULL," +
	                   " NUMERO         INT     NOT NULL, " + 
	                   " IDTERRITORIO   INT     NOT NULL, "+
	                   " VIA			VARCHAR(1000) NOT NULL, " +
	                   " INTERNI 		INT	,"+
	                   " NOTE        	VARCHAR(2000) " + 
	                   ")"; 
		      stmt.executeUpdate(sql);
		      
		      sql = "CREATE TABLE ASSEGNAZIONI " +
	                   "(ID INT PRIMARY KEY     NOT NULL," +
	                   " IDPROCLAMATORE INT     NOT NULL, " + 
	                   " IDTERRITORIO   INT     NOT NULL, "+
	                   " DATAASSEGNAZIONE	DATE NOT NULL, " +
	                   " DATASCADENZA 		DATE	,"+
	                   " RESTITUITO     CHAR(1) NOT NULL,"+
	                   " NOTE        	VARCHAR(2000) " + 
	                   ")"; 
		      stmt.executeUpdate(sql);
		      
		      sql = "CREATE TABLE PROCLAMATORI " +
	                   "(ID INT PRIMARY KEY     NOT NULL," +
	                   " NOME		VARCHAR(300) NOT NULL, " +
	                   " COGNOME  	VARCHAR(300)	NOT NULL	,"+
	                   " DATANASCITA     DATE,"+
	                   " NOTE        	VARCHAR(2000) " + 
	                   ")"; 
		      stmt.executeUpdate(sql);
		      
		      /* RELAZIONI */
		     /* 	sql = "ALTER TABLE CIVICI " + 
		      			"ADD CONSTRAINT FK_CIVICI_TO_TERRITORI FOREIGN KEY (IDTERRITORIO) " + 
		      			"    REFERENCES TERRITORI (ID) ";
		      	stmt.executeUpdate(sql);
		      	
		      	sql = "ALTER TABLE ASSEGNAZIONI " + 
		      			"ADD CONSTRAINT FK_ASSEGNAZIONI_TO_TERRITORI FOREIGN KEY (IDTERRITORIO) " + 
		      			"    REFERENCES TERRITORI (ID) ";
		      	stmt.executeUpdate(sql);
		      	
		      	sql = "ALTER TABLE ASSEGNAZIONI " + 
		      			"ADD CONSTRAINT FK_ASSEGNAZIONI_TO_PROCLAMATORI FOREIGN KEY (IDPROCLAMATORE) " + 
		      			"    REFERENCES PROCLAMATORI (ID) ";
		      	stmt.executeUpdate(sql);*/
		      			      	
		      stmt.close();
		      
		      c.close();
		    } catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
		    }
		    System.out.println("Table created successfully");
	  }
	
}
