package b.actions.comitive;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import b.GePrato;
import b.Logs;
import b.MenuPrincipal;
import b.NodeUtils;
import b.NodeUtils.ActionsMenus;
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
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.Window;

public class Assegna {
	
	public static Stage thisStage;
	
	public static void view(Stage primaryStage) {
		thisStage = primaryStage;
		int c = 0;
		try {
			FXMLLoader loader = new FXMLLoader(Utils.getResourceUrl("template/comitive_assegna.fxml"));
			AnchorPane page = (AnchorPane)loader.load();	
			
			//ObservableList<Node> ch = page.getChildren();
			
			MenuBar mb = (MenuBar)page.lookup("#menuPrincipal");
			MenuPrincipal mp = new MenuPrincipal(mb, primaryStage);
			
			ComboBox<String> mesi = (ComboBox<String>)page.lookup("#mese");
			ComboBox<String> anni = (ComboBox<String>)page.lookup("#anno");
			
			int numMese = Utils.getCurrentMonth();
			mesi.getItems().addAll("Gennaio","Febbraio","Marzo","Aprile","Maggio","Giugno","Luglio","Agosto","Settembre","Ottobre","Novembre","Dicembre");
			mesi.getSelectionModel().select(numMese);
			
			ArrayList<String> elAnni = new ArrayList<>();
			int numAnno = Utils.getCurrentYear();
			for (int i = -2; i < 9; i++) {
				int a = numAnno + i;
				elAnni.add( a+"");
			}
			anni.getItems().addAll(elAnni);
			anni.getSelectionModel().select(2);
					
			Scene scene = new Scene(page);
			primaryStage.setTitle("Bacheca");
			primaryStage.setScene(scene);
			
			/* Bottone aggiungi */
			Button bt = (Button)page.lookup("#visualizza");
			EventHandler<ActionEvent> eh = new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent arg0) {
					Assegna.show(primaryStage);				
				}
				
			};
			bt.setOnAction(eh);
			
			
			primaryStage.show();
		}catch (Exception e) {
			System.out.println("Riga "+c+"  :  "+e);
		}
	}
	
	private static void show(Stage stage) {

		ComboBox selmese = (ComboBox)stage.getScene().lookup("#mese");
		ComboBox selanno = (ComboBox)stage.getScene().lookup("#anno");
		
		int numMese = selmese.getSelectionModel().getSelectedIndex();
		int anno = Integer.valueOf((String)selanno.getSelectionModel().getSelectedItem());
		
		ArrayList<Date> dates = Utils.getAllDatesByMonthAndYear(numMese,anno);
		
		/* Recupero tutte le comitive inserite */
		String qry = "select attivo, giorno, id, luogo, ora from gp_comitive c order by attivo, luogo, giorno, ora";
		JSONArray res = GePrato.getSelectResponse(qry);
		
		/* Ciclo le date del mese e verifico in quali giorni sono presenti comitive */
		for (int i = 0; i < dates.size(); i++) {
			Date d = dates.get(i);
			String dayName = Utils.getDayNameByDate(d);
			for (int j = 0; j < res.length(); j++) {
				JSONObject obj = res.getJSONObject(j);
				String gg = obj.getString("giorno");
				String attivo = obj.getString("attivo");
				if(gg.substring(0, 3).toLowerCase().equals(dayName.substring(0, 3).toLowerCase())  && attivo.equals("1")) {
					Label l = new Label();
					l.setText(Utils.reverseDateInString(d)+" - "+dayName+" - "+obj.getString("ora")+" - "+obj.getString("luogo")+" ");
					
					Label dateLabel = new Label(Utils.reverseDateInString(d));
					dateLabel.setMinHeight(20);
					
					
					VBox vb = (VBox)stage.getScene().lookup("#vbox");
					vb.getChildren().add(l);
				}				
			}
			
		}		
		
	}
	
}
