package b.printers;

import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import javax.swing.JPanel;
import javax.swing.border.Border;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import b.printfoot.TestimonianzaPubblica;


public class CreateParameterProgramma{

	public static TestimonianzaPubblica[] getRows(JSONArray pp) {    

	       String titlemese = "";
	       ArrayList<ArrayList<String>> printProg = new ArrayList<ArrayList<String>>();
	       
	       String day = "";
	       String pos ="";
	       String ora ="";
	       String proc = "";
	       String settimana = "";
	      
	       
	       /* counter */
	       int cSett = 0;
	       
	       TestimonianzaPubblica[] lst = new TestimonianzaPubblica[pp.length()];
	       
	       for (int i = 0; i < pp.length(); i++) {
	    	   
	    	   TestimonianzaPubblica tp = new TestimonianzaPubblica();
	    	   
	    	   ArrayList<String> tr = new ArrayList<String>();
	    	   
	    	   JSONObject obj = pp.getJSONObject(i);
	    	   Date data = getDate((String)obj.get("data"));
	    	   String strData = (String)obj.get("data");
	    	   
	    	   String nome = (String)obj.get("nome");
	    	   String cognome = (String)obj.get("cognome");
	    	   Object nomevisuale = obj.get("nomevisuale");

	    	   
	    	   String datadal = (String)obj.get("oradal");
	    	   String dataal = (String)obj.get("oraal");
	    	   String giorno = (String)obj.get("giorno");
	    	   
	    	   String postazione = (String)obj.get("postazione");
	    	   String tipopostazione = (String)obj.get("tipopostazione");
	    	   
	    	   String anno = (String)obj.get("anno");
		       String mese = (String)obj.get("mese");
	    		   titlemese = mese +" "+anno;
	    		   //params.put("title", titlemese);
	    		   tp.setAnno(anno);
	    		   tp.setMese(mese);

	    	   System.out.println(settimana);
	    	   String sett = calcolaSettimana(data);
	    	   if(settimana.equals("") || !sett.equals(settimana)) {
	    		   settimana = sett;
	    		  // params.put("settimana"+cSett, settimana+" "+titlemese.split(" ")[0]);   
	    		   if(cSett==3) {
	    			   cSett = 1;
	    		   }else
	    			   cSett++;
	    	   }
	    	   
	    	   tp.setSettimana(settimana);
	    	   tp.setNumSettimana(cSett+"");
	    	   
	    	   String thisDay = giorno+" "+strData.split("-")[2];
	    	   String thisPos = postazione+" ("+tipopostazione+")";
	    	   String thisOra = datadal.substring(0,5)+" - "+dataal.substring(0,5);
	    	   String thisProc = nome.substring(0, 1)+"."+cognome;
	    	   
	    	  /*if(!day.equals("") && !day.equals(thisDay)) {
		    	   tr.add(day);
		    	   tr.add(pos);
		    	   tr.add(ora);
		    	   tr.add(proc);

		    	   //rows.add(day+"/"+pos+"/"+ora+"/"+proc);
		    	   tp.setGiorno(day);
		    	   tp.setOra(ora);
		    	   tp.setPostazione(pos);
		    	   tp.setProclamatore1(proc);
		    	   
		    	   day 	= thisDay;
		    	   pos 	= thisPos;
		    	   ora 	= thisOra;
		    	   proc = thisProc;
	    	   }else if(!pos.equals("") && !pos.equals(thisPos)) {
	    			   pos 	= thisPos;
			    	   ora 	= thisOra;
			    	   proc = thisProc;
	    	   }else if(!ora.equals("") && !ora.equals(thisOra)) {
	    		    	   ora 	= thisOra;
	    		    	   proc = thisProc;
	    	   }else if(!proc.equals("")){
	    		   proc += " - "+thisProc;  
	    	   }else {
	    		   day 	= thisDay;
		    	   pos 	= thisPos;
		    	   ora 	= thisOra;
		    	   proc = thisProc;
	    	   }

	    	   if(i==(pp.length()-1)) {
	    		   tr.add(day);
		    	   tr.add(pos);
		    	   tr.add(ora);
		    	   tr.add(proc);
		    	   //rows.add(day+"/"+pos+"/"+ora+"/"+proc);
		    	   tp.setGiorno(day);
		    	   tp.setOra(ora);
		    	   tp.setPostazione(pos);
		    	   tp.setProclamatore1(proc);
		    	   
	    	   }*/
	    	   
	    	   tp.setGiorno(thisDay);
	    	   tp.setOra(thisOra);
	    	   tp.setPostazione(thisPos);
	    	   tp.setProclamatore1(thisProc);
	    	   
	    	   lst[i] = tp;

	       }
	       
	       //params.put("rows", rows);
	       
	       try {
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	       
	    
	       
		return lst;
	}
	

	private static String calcolaSettimana(Date data) {
		String settimana = "";
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		
		int d = cal.get(Calendar.DAY_OF_WEEK);
		int m = cal.get(Calendar.DAY_OF_MONTH);
		int y = cal.get(Calendar.DAY_OF_YEAR);

		Date dinizio=null,dfine=null;
		if(d==2) {
			dinizio = data;
			cal.add(Calendar.DAY_OF_MONTH, 6);
			dfine = cal.getTime();
		}else {
			if(d==1)d = 8;
			int minusDays = (d-2)*-1;
			cal.add(Calendar.DAY_OF_MONTH, minusDays);
			dinizio = cal.getTime();
			cal.add(Calendar.DAY_OF_MONTH, 6);
			dfine = cal.getTime();
		}
		
		Calendar c1 = Calendar.getInstance();
		c1.setTime(dinizio);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(dfine);
		
		int d1 = c1.get(Calendar.DAY_OF_MONTH);
		int d2 = c2.get(Calendar.DAY_OF_MONTH);
		settimana = (d1<10?"0"+d1:d1)+" - "+(d2<10?"0"+d2:d2);
		
		return settimana;
	}






	
	private static java.util.Date getDate(String d) {
		String[] a = d.split("-");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		java.util.Date date = null;
		try {
			date = sdf.parse(a[2]+"/"+a[1]+"/"+a[0]);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return date;
	}

	
	
	
}
