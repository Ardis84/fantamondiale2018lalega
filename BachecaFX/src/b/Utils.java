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

import b.printfoot.ComitiveElenco;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

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

	public static String getDateinString(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		return gc.get(Calendar.DAY_OF_MONTH)+"/"+gc.get(Calendar.MONTH)+"/"+gc.get(Calendar.YEAR) ;
	}
	
	public static int getMonthFromDate(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		return gc.get(Calendar.MONTH)+1;
	}
	
	public static int getYearFromDate(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		return gc.get(Calendar.YEAR);
	}
	
public static ArrayList<Date> getAllDatesByMonthAndYear(int mese, int anno) {
		
		ArrayList<Date> dates = new ArrayList<>();
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, anno);
        cal.set(Calendar.MONTH, mese-1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        for (int i = 0; i < maxDay; i++) {
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
	
	public static Date reverseStringInDate(String d) {
		return reverseStringInDate(d, null);
	}
	
	public static Date reverseStringInDate(String d, String pattern) {
		if(pattern==null || pattern.equals(""))
			pattern = "dd/MM/yyyy";
		Date date = new Date();
		try {
			date = new SimpleDateFormat(pattern).parse(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static Date getLastDateInMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, getYearFromDate(date));
	    cal.set(Calendar.MONTH, getMonthFromDate(date));
	    cal.set(Calendar.DAY_OF_MONTH,  0);
		return cal.getTime();
	}

	public static Date getFirstDateInMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, getYearFromDate(date));
	    cal.set(Calendar.MONTH, getMonthFromDate(date));
	    cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}

	public static Date addDaysToDate(Date date, int numdays) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE,numdays);  
		return c.getTime();
	}

	/**  TableView 
	 * @param tb 
	 * @return **/
	public static TableView<?>  createTableColumnsListFromString(TableView<?> tb, String lst) {
		String[] l = lst.replaceAll(" ","").split(",");
		for (int i = 0; i < l.length; i++) {
			String lbl = l[i];
			TableColumn tc = new TableColumn(lbl);
			tb.getColumns().add(tc);
			tc.setCellValueFactory(new PropertyValueFactory<ComitiveElenco,String>(lbl));
		}
		//tb.getColumns().addAll();	
		return tb;
	}

	public static void disableEnableAllItemsInAnchorPane(AnchorPane page, boolean isEnable) {
		ObservableList<Node> ch = page.getChildren();
		for (int i = 0; i < ch.size(); i++) {
			Node n = ch.get(i);
			n.setDisable(isEnable);
		}	
	}
	
	public static void startLoading(AnchorPane page) {
		disableEnableAllItemsInAnchorPane(page,true);
	/*	ProgressBar pb = new ProgressBar();
		pb.setId("loading");
		pb.setMaxWidth(150);
		pb.setMaxHeight(20);
		pb.setScaleY(page.getHeight()/2);
		pb.setScaleX(page.getWidth()/2);
		page.getChildren().add(pb);	*/	
	}
	
	public static void stopLoading(AnchorPane page) {
		disableEnableAllItemsInAnchorPane(page,false);
	//	page.getChildren().remove(page.lookup("loading"));
	}

	//TODO aggiustare
	public static String getSettimanaFromDate(Date date) {
		String settimana = "";
		
		String datainizio = "";
		String datafine = "";
		
		String dayname = getDayNameByDate(date);
		
		int addBefore = 0;
		int addAfter = 0;
		
		if(dayname.substring(0, 3).toUpperCase().equals("LUN")) {
			addBefore = 0;
			addAfter = 6;
		}else if(dayname.substring(0, 3).toUpperCase().equals("MAR")) {
			addBefore = 1;
			addAfter = 5;
		}else if(dayname.substring(0, 3).toUpperCase().equals("MER")) {
			addBefore = 2;
			addAfter = 4;	
		}else if(dayname.substring(0, 3).toUpperCase().equals("GIO")) {
			addBefore = 3;
			addAfter = 3;
		}else if(dayname.substring(0, 3).toUpperCase().equals("VEN")) {
			addBefore = 4;
			addAfter = 2;
		}else if(dayname.substring(0, 3).toUpperCase().equals("SAB")) {
			addBefore = 5;
			addAfter = 1;
		}else if(dayname.substring(0, 3).toUpperCase().equals("DOM")) {
			addBefore = 6;
			addAfter = 0;
		}
		
		Date dFirst = addDaysToDate(date,-(addBefore));
		Date dLast 	= addDaysToDate(date,addAfter);
		
		datainizio 	= Utils.reverseDateInString(dFirst);
		datafine 	= Utils.reverseDateInString(dLast);
		
		settimana = datainizio.substring(0,2)+" - "+datafine.substring(0,2);
		
		return settimana;
		
	}
	
	public static String getSettimanaFromDateOnlyForMonth(Date date, int mese) {
		String settimana = "";
		
		String datainizio = "";
		String datafine = "";
		
		String dayname = getDayNameByDate(date);
		
		int addBefore = 0;
		int addAfter = 0;
		
		if(dayname.substring(0, 3).toUpperCase().equals("LUN")) {
			addBefore = 0;
			addAfter = 6;
		}else if(dayname.substring(0, 3).toUpperCase().equals("MAR")) {
			addBefore = 1;
			addAfter = 5;
		}else if(dayname.substring(0, 3).toUpperCase().equals("MER")) {
			addBefore = 2;
			addAfter = 4;	
		}else if(dayname.substring(0, 3).toUpperCase().equals("GIO")) {
			addBefore = 3;
			addAfter = 3;
		}else if(dayname.substring(0, 3).toUpperCase().equals("VEN")) {
			addBefore = 4;
			addAfter = 2;
		}else if(dayname.substring(0, 3).toUpperCase().equals("SAB")) {
			addBefore = 5;
			addAfter = 1;
		}else if(dayname.substring(0, 3).toUpperCase().equals("DOM")) {
			addBefore = 6;
			addAfter = 0;
		}
		
		Date dFirst = addDaysToDate(date,-(addBefore));
		Date dLast 	= addDaysToDate(date,addAfter);
		
		if(getMonthFromDate(dFirst)!=mese) {
			dFirst = getFirstDateInMonth(date);
		}
		
		if(getMonthFromDate(dLast)!=mese) {
			dLast = getLastDateInMonth(date);
		}
		
		datainizio 	= Utils.reverseDateInString(dFirst);
		datafine 	= Utils.reverseDateInString(dLast);
		
		settimana = datainizio.substring(0,2)+" - "+datafine.substring(0,2);
		
		return settimana;
	}

	

	
	
	
}
