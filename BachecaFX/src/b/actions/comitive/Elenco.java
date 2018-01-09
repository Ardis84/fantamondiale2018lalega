package b.actions.comitive;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import b.GePrato;
import b.Logs;
import b.ObservableListMaker;
import b.Utils;
import b.actions.proclamatori.Aggiungi;
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

public class Elenco {
		
	
	public static void view(Stage primaryStage) {
		int c = 0;
		try {
			FXMLLoader loader = new FXMLLoader(Utils.getResourceUrl("template/comitive_elenco.fxml"));
			AnchorPane page = (AnchorPane)loader.load();	
			
			ObservableList<Node> ch = page.getChildren();
			TableView<ComitiveElenco> tb = (TableView<ComitiveElenco>)ch.get(1);
			tb.setEditable(true);
			
			String query = "select attivo, giorno, id, luogo, ora from gp_comitive c order by attivo, luogo, giorno, ora";
			JSONArray res = GePrato.getSelectResponse(query);
			
			/* Colonne */
			TableColumn id = new TableColumn("Id");
			TableColumn attivo = new TableColumn("Attivo");
			TableColumn luogo = new TableColumn("Luogo");
			TableColumn giorno = new TableColumn("Giorno");
			TableColumn ora = new TableColumn("Ora");
			
			/* Associo le colonne al printfeet */
			id.setCellValueFactory(new PropertyValueFactory<ComitiveElenco,String>("id"));
			attivo.setCellValueFactory(new PropertyValueFactory<ComitiveElenco,String>("attivo"));
			luogo.setCellValueFactory(new PropertyValueFactory<ComitiveElenco,String>("luogo"));
			giorno.setCellValueFactory(new PropertyValueFactory<ComitiveElenco,String>("giorno"));
			ora.setCellValueFactory(new PropertyValueFactory<ComitiveElenco,String>("ora"));
			
			ArrayList<ComitiveElenco> ar = new ArrayList<>();
			for (int i = 0; i < res.length(); i++) {
				JSONObject obj = res.getJSONObject(i);
				ComitiveElenco ce = new ComitiveElenco();
				
				c = i;
				
				ce.setId(Utils.isNull(obj,"id")?"":(obj.getString("id")));
				ce.setAttivo(Utils.isNull(obj,"attivo")?"":(obj.getString("attivo")));
				ce.setLuogo(Utils.isNull(obj,"luogo")?"":(obj.getString("luogo")));
				ce.setGiorno(Utils.isNull(obj,"giorno")?"":(obj.getString("giorno")));
				ce.setOra(Utils.isNull(obj,"ora")?"":(obj.getString("ora")));
				
				ar.add(ce);
				
			}
			ObservableList<ComitiveElenco> lst = (ObservableList<ComitiveElenco>) ObservableListMaker.createList(ar);
			tb.setItems(lst );
			tb.getColumns().addAll(id,attivo, giorno, luogo, ora);

			/* Bottone aggiungi */
			Button bt = (Button)ch.get(2);
			EventHandler<ActionEvent> eh = new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent arg0) {
					Aggiungi.view(primaryStage);				
				}
			};
			bt.setOnAction(eh);
			
			Scene scene = new Scene(page);
			primaryStage.setTitle("Bacheca");
			primaryStage.setScene(scene);
			primaryStage.show();
		}catch (Exception e) {
			System.out.println("Riga "+c+"  :  "+e);
		}
	}
}
