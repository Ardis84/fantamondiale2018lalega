package b.printers;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import b.Utils;
import b.printfoot.Comitive;



public class CreateParameterComitive {

	public static Comitive[] getRows(JSONArray pp) {
		Comitive[] c = new Comitive[pp.length()];
		
//		int nummese = pp.getJSONObject(0).getInt("nummese") ;	
//		int anno = pp.getJSONObject(0).getInt("anno") ;
		
		//offerta1, offerta2, offerta3, ca.data, p.nome, p.cognome, c.luogo, c.giorno
		String sett = "";
		int tiposett = 0;
		for (int i = 0; i < pp.length(); i++) {
			Comitive cv = new Comitive();
			JSONObject obj = pp.getJSONObject(i);
			
			System.out.println(obj.get("offerta1").toString());
			
			if(obj.get("offerta1")!=null && !obj.get("offerta1").toString().equals("null"))
				cv.setOffertamese1(obj.getString("offerta1"));
			if(obj.get("offerta2")!=null && !obj.get("offerta2").toString().equals("null"))
				cv.setOffertamese2(obj.getString("offerta2"));
			if(obj.get("offerta3")!=null && !obj.get("offerta3").toString().equals("null"))
				cv.setOffertamese3(obj.getString("offerta3"));
			
			cv.setData(obj.getString("data"));
			cv.setConduttore(obj.getString("cognome")+" "+obj.getString("nome").substring(0,3)+". ");
			cv.setLuogo(obj.getString("luogo"));
			
			//2018-01-01
			String dataCom = obj.getString("giorno").substring(0, 3)+" "+obj.getString("data").substring(8, 10);
			
			cv.setGiorno(dataCom);
			cv.setOra(obj.getString("ora"));
			
			cv.setAnno(obj.getString("anno"));
			String mese = obj.getString("mese").substring(0, 1).toUpperCase()+""+obj.getString("mese").substring(1, obj.getString("mese").length());
			cv.setMese(mese);
			
			Date date = Utils.reverseStringInDate(obj.getString("data"), "yyyy-MM-dd");
			String settimana = Utils.getSettimanaFromDateOnlyForMonth(date, obj.getInt("nummese"));
			cv.setSettimana(settimana);
			
			if(!sett.equals(settimana)) {
				sett = settimana;
				if(tiposett<3)
					tiposett++;
				else
					tiposett = 1;
			}
			
			cv.setTiposettimana(tiposett+"");
			
			c[i] = cv;
		}
		
		return c;   
		
	}
	
	public static Comitive[] getRowsOld(JSONArray pp) {    
		int i = 0;
		Comitive[] c = new Comitive[17];

		String sett1 = "01 al 05";
		String sett2 = "06 al 12";
		String sett3 = "13 al 19";
		String sett4 = "20 al 26";
		String sett5 = "27 al 30";
		
		Comitive c1 = new Comitive(); 
		
		c1.setOffertamese1("La Torre di Guardia");
		c1.setOffertamese2("Insegniamo la verità: Salmo 83:18");
		c1.setOffertamese3("Op. \"La vostra famiglia può essere felice?\"");
		c1.setAnno("2017");
		c1.setMese("Novembre");
		c1.setGiorno("01 Mer");
		c1.setOra("09.15");
		c1.setLuogo("Casa Brunettini");
		c1.setConduttore("Mazzola C.");
		c1.setSettimana(sett1);
					
		c[i++] = c1;
		c1 = new Comitive();
		c1.setAnno("2017");
		c1.setMese("Novembre");
		c1.setGiorno("03 Ven");
			c1.setOra("15.30");
			c1.setLuogo("Sala del Regno");
			c1.setConduttore("Borgo W.");
			c1.setSettimana(sett1);			
		c[i++] = c1;
		c1 = new Comitive();
		c1.setAnno("2017");
		c1.setMese("Novembre");
		c1.setGiorno("04 Sab");
			c1.setOra("09.15");
			c1.setLuogo("Sala del Regno");
			c1.setConduttore("Risoli R.");
			c1.setSettimana(sett1);			
		c[i++] = c1;
		c1 = new Comitive();
		c1.setAnno("2017");
		c1.setMese("Novembre");
		c1.setGiorno("05 Dom");
			c1.setOra("09.30");
			c1.setLuogo("Sala del Regno");
			c1.setConduttore("Cattoi C.");			
			c1.setSettimana(sett1);			
		c[i++] = c1;
		c1 = new Comitive();
		c1.setAnno("2017");
		c1.setMese("Novembre");
		c1.setGiorno("08 Mer");
			c1.setOra("09.15");
			c1.setLuogo("Casa Brunettini");
			c1.setConduttore("Manca R.");
			c1.setSettimana(sett2);			
		c[i++] = c1;
		c1 = new Comitive();
		c1.setAnno("2017");
		c1.setMese("Novembre");
		c1.setGiorno("10 Ven");
			c1.setOra("15.30");
			c1.setLuogo("Sala del Regno");
			c1.setConduttore("Golisano G.");
			c1.setSettimana(sett2);			
		c[i++] = c1;
		c1 = new Comitive();
		c1.setAnno("2017");
		c1.setMese("Novembre");
		c1.setGiorno("11 Sab");
			c1.setOra("09.15");
			c1.setLuogo("Sala del Regno");
			c1.setConduttore("Trombetta C.");
			c1.setSettimana(sett2);		
		c[i++] = c1;
		c1 = new Comitive();
		
		c1.setAnno("2017");
		c1.setMese("Novembre");
		c1.setGiorno("12 Dom");
			c1.setOra("09.30");
			c1.setLuogo("Sala del Regno");
			c1.setConduttore("Corradini I.");
			c1.setSettimana(sett2);	
		c[i++] = c1;
		c1 = new Comitive();
		c1.setAnno("2017");
		c1.setMese("Novembre");
		c1.setGiorno("15 Mer");
			c1.setOra("09.15");
			c1.setLuogo("Casa Brunettini");
			c1.setConduttore("Cardone C.");
			c1.setSettimana(sett3);		
		c[i++] = c1;
		c1 = new Comitive();
		c1.setAnno("2017");
		c1.setMese("Novembre");
		c1.setGiorno("17 Ven");
			c1.setOra("15.30");
			c1.setLuogo("Sala del Regno");
			c1.setConduttore("Mazzolo I.");
			c1.setSettimana(sett3);				
		c[i++] = c1;
		c1 = new Comitive();
		c1.setAnno("2017");
		c1.setMese("Novembre");
		c1.setGiorno("18 Sab");
			c1.setOra("09.15");
			c1.setLuogo("Sala del Regno");
			c1.setConduttore("Tedesco B.");
			c1.setSettimana(sett3);			
		c[i++] = c1;
		c1 = new Comitive();
		c1.setAnno("2017");
		c1.setMese("Novembre");
		c1.setGiorno("19 Dom");
			c1.setOra("09.30");
			c1.setLuogo("Sala del Regno");
			c1.setConduttore("Tedesco F.");
			c1.setSettimana(sett3);		
		c[i++] = c1;
		c1 = new Comitive();
		c1.setAnno("2017");
		c1.setMese("Novembre");
		c1.setGiorno("22 Mer");
			c1.setOra("09.15");
			c1.setLuogo("Casa Brunettini");
			c1.setConduttore("Malacrida M.");
			c1.setSettimana(sett4);			
		c[i++] = c1;
		c1 = new Comitive();
		c1.setAnno("2017");
		c1.setMese("Novembre");
		c1.setGiorno("24 Ven");
			c1.setOra("15.30");
			c1.setLuogo("Sala del Regno");
			c1.setConduttore("Borgo W.");
			c1.setSettimana(sett4);		
		c[i++] = c1;
		c1 = new Comitive();
		c1.setAnno("2017");
		c1.setMese("Novembre");
		c1.setGiorno("25 Sab");
			c1.setOra("09.15");
			c1.setLuogo("Sala del Regno");
			c1.setConduttore("Trombetta L.");
			c1.setSettimana(sett4);		
		c[i++] = c1;
		c1 = new Comitive();
		c1.setAnno("2017");
		c1.setMese("Novembre");
		c1.setGiorno("26 Dom");
			c1.setOra("09.30");
			c1.setLuogo("Sala del Regno");
			c1.setConduttore("Risoli R.");
			c1.setSettimana(sett4);					
					
		c[i++] = c1;
		c1 = new Comitive();
		c1.setAnno("2017");
		c1.setMese("Novembre");
		c1.setGiorno("29 Mer");
			c1.setOra("09.15");
			c1.setLuogo("Casa Brunettini");
			c1.setConduttore("Mazzola C.");
			c1.setSettimana(sett5);		
				c[i++] = c1;
		
		return c;
	}
}
