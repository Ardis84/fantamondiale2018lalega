package b.actions.comitive;

import java.util.ArrayList;
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
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.Window;

public class Elenco {
	
	public static Stage thisStage;
	
	public static void view(Stage primaryStage) {
		thisStage = primaryStage;
		int c = 0;
		try {
			FXMLLoader loader = new FXMLLoader(Utils.getResourceUrl("template/comitive_elenco.fxml"));
			AnchorPane page = (AnchorPane)loader.load();	
			
			MenuBar mb = (MenuBar)page.lookup("#menuPrincipal");
			MenuPrincipal mp = new MenuPrincipal(mb, primaryStage);
			
			ObservableList<Node> ch = page.getChildren();
			TableView<ComitiveElenco> tb = (TableView<ComitiveElenco>)ch.get(1);
			tb.setEditable(true);
			
			tb = setElements(tb,c);

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

	public static TableView<ComitiveElenco> setElements(TableView<ComitiveElenco> tb) {
		return setElements(tb,0);
	}
	
	private static TableView<ComitiveElenco> setElements(TableView<ComitiveElenco> tb, int c) {
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
			ce.setAttivo(Utils.isNull(obj,"attivo")?"":(obj.getString("attivo").equals("1")?"SI":"NO"));
			ce.setLuogo(Utils.isNull(obj,"luogo")?"":(obj.getString("luogo")));
			ce.setGiorno(Utils.isNull(obj,"giorno")?"":(obj.getString("giorno")));
			ce.setOra(Utils.isNull(obj,"ora")?"":(obj.getString("ora")));
			
			ar.add(ce);
			
		}
		ObservableList<ComitiveElenco> lst = (ObservableList<ComitiveElenco>) ObservableListMaker.createList(ar);
		tb.setItems(lst );
		tb.getColumns().addAll(id,attivo, giorno, luogo, ora);	
		tb.setEditable(true);		

		ActionsMenus am = new ActionsMenus();
		am.addAction("Modifica", "b.actions.comitive.Elenco", "ModificaRow");
		am.addAction("Cancella", "b.actions.comitive.Elenco", "CancellaRow");
		ArrayList<HashMap<String, String>> actions = am.getActions();
		final TableView newtb = NodeUtils.addContextMenuToTableView(tb, actions, thisStage);
		
		return newtb;
	}
	
	public static void ModificaRow(Stage stage) {
		Modifica.view(stage);
		System.out.println("ModificaRow");
	}
	
	public static void CancellaRow(Stage stage) {
		Scene sc = stage.getScene();
		TableView tb = (TableView)sc.lookup("#elenco");
		ComitiveElenco ce = (ComitiveElenco)tb.getSelectionModel().getSelectedItem();
		
		String qry = "delete from gp_comitive where id = "+ce.getId();
		JSONArray r = GePrato.getDeleteResponse(qry );
		
		tb.getColumns().clear();
		tb = Elenco.setElements(tb);
		
		System.out.println("CancellaRow");
	}
}
