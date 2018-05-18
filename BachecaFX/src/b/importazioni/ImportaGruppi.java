package b.importazioni;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.common.collect.Iterators;

import b.GePrato;
import b.printfoot.Gruppi;
import b.printfoot.GruppiProclamatori;

public class ImportaGruppi {

	public static void main(String[] args) {
		try {
			String path = ImportaGruppi.class.getResource("").getPath();
			FileInputStream inputStream = new FileInputStream(new File(path+"/files/gruppi.xls"));
			Workbook workbook = WorkbookFactory.create(inputStream);
			
			Sheet sheet = workbook.getSheetAt(0);
			
			 Iterator<Row> rowIterator = sheet.rowIterator();
	            String lastGruppo = "";
	            
	            ArrayList<Gruppi> arGruppi = new ArrayList<Gruppi>();
	            ArrayList<GruppiProclamatori> arProclamatori = new ArrayList<GruppiProclamatori>();
	            
	            Gruppi g = new Gruppi();
	            
	            int totRows = Iterators.size(sheet.rowIterator());
	            
	            int cR = 0;
		        while (rowIterator.hasNext()) {
		        	Row row = rowIterator.next();
		        	if(cR > 0) {	
			            GruppiProclamatori gpr = new GruppiProclamatori();
			            
			            /* Gruppo */
			            Cell celGruppo = row.getCell(0);
			            String cellValue = celGruppo.getStringCellValue();
			            if(!lastGruppo.equals(cellValue)) {
	                		g = new Gruppi();
	                		g.setDescrizione(cellValue);
	                		
	                		arGruppi.add(g);
	                		
	                		lastGruppo = cellValue;
	                		 
	                	}
			            
			            /* Proc */
			            Cell celProclamatore = row.getCell(1);
			            cellValue = celProclamatore.getStringCellValue();
			            gpr.setDenominazione(cellValue);
			            gpr.setNomegruppo(lastGruppo);
			            
			            /* Tipo */
			            Cell celTipo = row.getCell(2);
			            if(celTipo!=null) {
				            cellValue = celTipo.getStringCellValue();
				            if(cellValue.equals("R")) {
		                		gpr.setTipo("R");
		                		g.setResponsabile(gpr.getDenominazione());
		                	}else if(cellValue.equals("A")) {
		                		gpr.setTipo("A");
		                		g.setAssistente(gpr.getDenominazione());
		                	}
			            }
			            
			            if(cR==(totRows-1)) {
			            	arGruppi.remove(arGruppi.size()-1);
			            	arGruppi.add(g);
			            }
			            
			            arProclamatori.add(gpr);
		        	}
		            cR++;
		            
		           /* int c = 0;
		            String idprocgruppo = "0";
		            while (cellIterator.hasNext()) {
		                Cell cell = cellIterator.next();
		                String cellValue = cell.getStringCellValue();
		                String colName = "";
		                
		                
		                
		                if(cell.getColumnIndex()==0) {
		                	colName = "Gruppo";
		                	
		                	if(!lastGruppo.equals(cellValue) || c==(numTotCell-1)) {
		                		g = new Gruppi();
		                		g.setDescrizione(cellValue);
		                		if(!lastGruppo.equals("")) {
		                			insertGruppo = true;
		                		}
		                	}

		                }else if(cell.getColumnIndex()==1) {
		                	colName = "Proclamatore";
		                	idprocgruppo = getIdProclamatoreGruppo(cellValue);
		                }else if(cell.getColumnIndex()==2) {
		                	colName="Tipo";
		                	if(cellValue.equals("R")) {
		                		
		                	}else if(cellValue.equals("A")) {
		                		
		                	}
		                }
		                
		                if(insertGruppo) {
		                	
		                	insertGruppo=false;
		                }
		               
		               /* if(row.getRowNum()!=0)
		                	System.out.print(colName +"  --> "+cellValue + "\t");
		                
		                c++;
		            }*/
		            
		        }
		        HashMap<String, String> groupsMap = new HashMap<String, String>();
		        for (int i = 0; i < arGruppi.size(); i++) {
		        	Gruppi gr = arGruppi.get(i);
		        	//Cerco l'id del responsabile
		        	String idresp = getIdProclamatore(gr.getResponsabile());
		        	//Cerco l'id dell'assistente
		        	String idass = getIdProclamatore(gr.getAssistente());
		        	
		        	if(!idresp.equals("0") && !idass.equals("0")) {
		        		String qins = "insert into gp_gruppiservizio(id,idresponsabile, idassistente, descrizione) "
		        				+ " values("+(i+1)+","+idresp+","+idass+",'"+gr.getDescrizione()+"') ";
		        		gr.setId(""+(i+1));
		        		groupsMap.put(gr.getDescrizione(), (i+1)+"");
		        		
		        		System.out.println(qins);
		        		GePrato.getInsertResponse(qins);
		        	}
		        	
		        	
				}
		        
		        for (int i = 0; i < arProclamatori.size(); i++) {
		        	
		        	GruppiProclamatori gp = arProclamatori.get(i);
		        	String idgruppo = groupsMap.get(gp.getNomegruppo());
		        	insertProclamatoreGruppo(gp.getDenominazione(), idgruppo, gp.getTipo());
				}
		        
		       // System.out.println(arGruppi);
		        
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EncryptedDocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private static String getIdProclamatore(String denominazione) {
		String id = "0";
		String proclamatore = denominazione.toUpperCase().replaceAll("JR.", "");
		proclamatore = proclamatore.replaceAll("À", "A'");
		
		String q = " select * from ( "+
				" SELECT id, upper(concat(cognome,' ',nome)) as denominazione "+
				" FROM `gp_proclamatori` )a "+
				" where denominazione = '"+proclamatore+"'";
		
    	JSONArray res = GePrato.getSelectResponse(q);
    	if(res.length()>0) {
    		id = res.getJSONObject(0).getString("id");
    	}else {
    		System.out.print(proclamatore);
    	}
    	
    	
		return id;
	}

	private static void insertProclamatoreGruppo(String denominazione, String idgruppo, String tipo) {
		
		String q = "select * from gp_gruppiproclamatori where idgruppo = "+idgruppo+" "+
					" and denominazione='"+denominazione+"'";
		JSONArray res = GePrato.getSelectResponse(q);
		if(res.length()<1) {	
			String id = getIdProclamatore(denominazione);
	    	
			if(tipo==null)
				tipo = "";
			
	    	String ins = "insert into gp_gruppiproclamatori(idproc,idgruppo, denominazione, tipo) "
	    			+ " values("+id+","+idgruppo+",'"+denominazione+"','"+tipo+"')";
	    	GePrato.getInsertResponse(ins);
		}else {
			System.out.println("Non inserito:  "+  denominazione);
		}
	}

}
