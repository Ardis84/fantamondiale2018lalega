package b;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Utils {

	static String[] mesiStr = {"gennaio","febbraio","marzo","aprile","maggio","giugno","luglio","agosto","settembre","ottobre","novembre","dicembre"};
	static int[] mesiNum = {1,2,3,4,5,6,7,8,9,10,11,12};
			
	public static URL getResourceUrl(String resourceName) {
		return Utils.class.getResource(resourceName);
	}

	public static String checkNull(String str) {
		if(str==null)
			str = "";
		return str;
	}
	
	public static boolean isNull(String str) {
		if(str==null)
			return true;
		else
			return false;
	}
	
	public static boolean isNull(JSONObject obj, String name) {
		if(obj.isNull(name))
			return true;
		else
			return false;
	}
	
	public static void alert(String msg) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("");
		alert.setHeaderText("Attenzione!");
		alert.setContentText(msg);

		alert.showAndWait();
	}

	public static int getNumMese(String selMese) {
		
		int mese=0;
		
		for (int i = 0; i < mesiStr.length; i++) {
			String m = mesiStr[i];
			if(selMese.toLowerCase().equals(m)) {
				mese = mesiNum[i];
				i = mesiStr.length;
			}
			
		}
		
		return mese;
	}
	
	public static String getNameMese(int num) {		
		return  mesiStr[num-1];
	}
	
	public static Date getCurrentDate() {
		GregorianCalendar gc = new GregorianCalendar();
		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		sdf.setTimeZone(gc.getTimeZone());
		sdf.format(today);
		return today;
	}
	
	public static int getCurrentDay() {
		GregorianCalendar gc = new GregorianCalendar();
		return gc.get(Calendar.DAY_OF_MONTH);
	}
	
	public static int getCurrentMonth() {
		GregorianCalendar gc = new GregorianCalendar();
		return gc.get(Calendar.MONTH);
	}
	
	public static int getCurrentYear() {
		GregorianCalendar gc = new GregorianCalendar();
		return gc.get(Calendar.YEAR);
	}
	
	
	public static String getCurrentStringDate() {
		GregorianCalendar gc = new GregorianCalendar();
		return gc.get(Calendar.DAY_OF_MONTH)+"/"+gc.get(Calendar.MONTH)+"/"+gc.get(Calendar.YEAR) ;
	}
	
	public static String getCurrentStringMonth() {
		int m = getCurrentMonth();
		return getNameMese(m);	
	}

public static ArrayList<Date> getAllDatesByMonthAndYear(int mese, int anno) {
		
		ArrayList<Date> dates = new ArrayList<>();
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, anno);
        cal.set(Calendar.MONTH, mese);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy	");
        for (int i = 1; i < maxDay; i++) {
	        cal.set(Calendar.DAY_OF_MONTH, i + 1);
	        String d = df.format(cal.getTime());
	        dates.add(cal.getTime());
        }
		
		return dates;
	}


	public static ArrayList<String> getAllStringDatesByMonthAndYear(int mese, int anno) {
	
		ArrayList<String> dates = new ArrayList<>();
	
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, anno);
	    cal.set(Calendar.MONTH, mese);
	    cal.set(Calendar.DAY_OF_MONTH, 1);
	    int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	    for (int i = 1; i < maxDay; i++) {
	        cal.set(Calendar.DAY_OF_MONTH, i + 1);
	        String d = df.format(cal.getTime());
	        dates.add(d);
	    }

	    return dates;
	}
	
	public static String getDayNameByDate(Date d) {
		return new SimpleDateFormat("EEEE", Locale.ITALIAN).format(d);
	}
	
	public static String getDayNameByStringDate(String d) {
		Date date = null;
		try {
			date = new SimpleDateFormat("dd/MM/yyyy").parse(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return getDayNameByDate(date);

	}

	public static String reverseDateInString(Date date) {
		String d = "";
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		d =  df.format(cal.getTime());
		return d;
	}

	
}
