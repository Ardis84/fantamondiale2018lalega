package b.printers;

import org.json.JSONArray;

import b.printfoot.Comitive;

public class CreateParameterPulizie {

	
	public static Comitive[] getRows(JSONArray pp) {    
		int i = 0;
		Comitive[] c = new Comitive[17];

		String sett1 = "01 al 05";
		String sett2 = "06 al 12";
		String sett3 = "13 al 19";
		String sett4 = "20 al 26";
		String sett5 = "27 al 30";
		
		Comitive c1 = new Comitive(); 
		
		c1.setOffertamese1("La Torre di Guardia");
		c1.setOffertamese2("Insegniamo la verit�: Salmo 83:18");
		c1.setOffertamese3("Op. \"La vostra famiglia pu� essere felice?\"");
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
