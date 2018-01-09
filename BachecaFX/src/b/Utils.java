package b;

import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Utils {

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

		String[] mesiStr = {"gennaio","febbraio","marzo","aprile","maggio","giugno","luglio","agosto","settembre","ottobre","novembre","dicembre"};
		int[] mesiNum = {1,2,3,4,5,6,7,8,9,10,11,12};
		
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
	
}
