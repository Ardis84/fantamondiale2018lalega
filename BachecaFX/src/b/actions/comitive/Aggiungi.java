package b.actions.comitive;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import b.GePrato;
import b.Logs;
import b.ObservableListMaker;
import b.Utils;
import b.printers.Printers;
import b.printfoot.Comitive;
import b.printfoot.ComitiveElenco;
import b.printfoot.Proclamatori;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.Window;

public class Aggiungi {
		
	
	public static void view(Stage primaryStage) {
		int c = 0;
		try {
			FXMLLoader loader = new FXMLLoader(Utils.getResourceUrl("template/comitive_aggiungi.fxml"));
			AnchorPane page = (AnchorPane)loader.load();	
			
			ObservableList<Node> ch = page.getChildren();
			
			
			Scene scene = new Scene(page);
			primaryStage.setTitle("Bacheca");
			primaryStage.setScene(scene);
			primaryStage.show();
		}catch (Exception e) {
			System.out.println("Riga "+c+"  :  "+e);
		}
	}
}
