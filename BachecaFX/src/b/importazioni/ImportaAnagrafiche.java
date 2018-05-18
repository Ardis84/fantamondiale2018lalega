package b.importazioni;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.json.JSONArray;

import b.GePrato;

public class ImportaAnagrafiche {

	public static void main(String[] args) {
		// anagrafiche_import.xls
		
		String path = ImportaGruppi.class.getResource("").getPath();
		FileInputStream inputStream;
		try {
			inputStream = new FileInputStream(new File(path+"/files/anagrafiche_import.xls"));
			Workbook workbook = WorkbookFactory.create(inputStream);
			
			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.rowIterator();
			
			 while (rowIterator.hasNext()) {
		        	Row row = rowIterator.next();
		        	
		        	String denom = row.getCell(1).getStringCellValue();
		        	String sesso = row.getCell(2).getStringCellValue();
		        	String pass = row.getCell(3).getStringCellValue();
		        	
		        	String cogn = denom.split(" ")[0];
		        	String nome = denom.split(" ")[1];
		        	
		        	String user = nome.replaceAll(" ", "").toLowerCase()+"."+cogn.replaceAll(" ", "").toLowerCase();
		        	
		        	String q = "INSERT INTO gp_proclamatori(nome, cognome, datanascita, user, password, "
		        			+ "sesso, idlivello) "
		        			+ "VALUES ('"+nome+"','"+cogn+"',STR_TO_DATE('01/01/2018', '%d/%m/%Y'),'"+user+"','"+pass+"',"
		        			+ "'"+sesso+"',2)";
		        	GePrato.getInsertResponse(q);
		        	JSONArray res = GePrato.getSelectResponse("select id from gp_proclamatori where nome = '"+nome+"' and cognome='"+cogn+"'");
		        	String id = res.getJSONObject(0).getString("id");
		        	System.out.println(id+"  "+denom);
		        	
		        	//GePrato.getDeleteResponse("delete from gp_proclamatori where id ="+id);
		        	
			 }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EncryptedDocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
