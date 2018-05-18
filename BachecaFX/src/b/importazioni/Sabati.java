package b.importazioni;

import java.util.ArrayList;
import java.util.Date;

import b.Utils;

public class Sabati {

	public static void main(String[] args) {
		
		int mese = 6;
		
		for (int i = 0; i < 5; i++) {
			ArrayList<Date> dates = Utils.getAllDatesByMonthAndYear(mese, 2018);
			String nomeMese = Utils.getNameMese(mese);
			System.out.println(nomeMese);
			System.out.println("********************************");
			
			for (int j = 0; j < dates.size(); j++) {
				Date d = dates.get(j);
				String day = Utils.getDayNameByDate(d);
				
				if(day.toLowerCase().equals("sabato")) {
					int numDay = Utils.getDayNumberFromDate(d);
					System.out.println("Sab "+numDay);
				}
				
			}
			System.out.println("********************************");
			
			mese++;
		}
		
		
		String[] reparti = {"BIBLIOTECA"};
		
		for (int i = 0; i < reparti.length; i++) {
			System.out.println(reparti[i].substring(0, 1)+""+reparti[i].toLowerCase().substring(1,reparti[i].length()));
		}
		

	}

}
