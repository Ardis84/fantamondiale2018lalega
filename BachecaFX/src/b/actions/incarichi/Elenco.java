package b.actions.incarichi;

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
import b.printfoot.IncarichiElenco;
import b.printfoot.Proclamatori;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
			FXMLLoader loader = new FXMLLoader(Utils.getResourceUrl("template/incarichi_elenco.fxml"));
			AnchorPane page = (AnchorPane)loader.load();	
			
			MenuBar mb = (MenuBar)page.lookup("#menuPrincipal");
			MenuPrincipal mp = new MenuPrincipal(mb, primaryStage);
			
			ObservableList<Node> ch = page.getChildren();
			TableView<IncarichiElenco> tb = (TableView<IncarichiElenco>)ch.get(1);
			tb.setEditable(true);
			
			tb = setElements(tb,c);

			tb.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {		
			    @Override
			    public void handle(MouseEvent t) {
			    	Button mod = (Button)primaryStage.getScene().lookup("#mod");
		    		Button canc = (Button)primaryStage.getScene().lookup("#canc");
		    		
		    		EventHandler<ActionEvent> eh1 = new EventHandler<ActionEvent>() {
						
						@Override
						public void handle(ActionEvent arg0) {
							ModificaRow(primaryStage);					
						}
					};
					mod.setOnAction(eh1);
					
					EventHandler<ActionEvent> eh2 = new EventHandler<ActionEvent>() {
						
						@Override
						public void handle(ActionEvent arg0) {
							CancellaRow(primaryStage);				
						}
					};
					canc.setOnAction(eh2);
		    		
			    	TableView<IncarichiElenco> tb1 = (TableView<IncarichiElenco>)primaryStage.getScene().lookup("#elenco");
			    	if(tb1.getSelectionModel().getSelectedItem()!=null) {	    		
			    		 mod.setDisable(false);
			    		 canc.setDisable(false);
			    	}else {
			    		mod.setDisable(false);
			    		canc.setDisable(false);
			    	}
			    }
			});
			
			/* Bottone aggiungi */
			Button bt = (Button)page.lookup("#aggiungi");
			EventHandler<ActionEvent> eh = new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent arg0) {
					Aggiungi.view(primaryStage);				
				}
			};
			bt.setOnAction(eh);
			
			/* Bottone stampariepilogo */
			Button stampariepilogo = (Button)page.lookup("#stampariepilogo");
			EventHandler<ActionEvent> ehSR = new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent arg0) {
					stampariepilogo(primaryStage);				
				}

				
			};
			stampariepilogo.setOnAction(ehSR);
			
			Scene scene = new Scene(page);
			primaryStage.setTitle("Bacheca");
			primaryStage.setScene(scene);
			primaryStage.show();
		}catch (Exception e) {
			System.out.println("Riga "+c+"  :  "+e);
		}
	}

	public static TableView<IncarichiElenco> setElements(TableView<IncarichiElenco> tb) {
		return setElements(tb,0);
	}
	
	private static TableView<IncarichiElenco> setElements(TableView<IncarichiElenco> tb, int c) {
		String query = " SELECT ia.id, concat(p.cognome,' ',p.nome) as proclamatore, ia.idproc, "+
				" i.reparto, ia.ruolo, ia.validoda, ia.validoa "+
				" FROM gp_incarichiassegnati ia, gp_proclamatori p, gp_incarichi i "+
				" where p.id = ia.idproc "+
				" and i.id = ia.idincarico";
		JSONArray res = GePrato.getSelectResponse(query);
		
		/* Colonne */
		TableColumn id = new TableColumn("Id");
		TableColumn proc = new TableColumn("Proclamatore");
		TableColumn reparto = new TableColumn("Reparto");
		TableColumn ruolo = new TableColumn("Ruolo");
		TableColumn valda = new TableColumn("Valido Da");
		TableColumn vala = new TableColumn("Valido Fino A");
		
		/* Associo le colonne al printfeet */
		id.setCellValueFactory(new PropertyValueFactory<IncarichiElenco,String>("id"));
		proc.setCellValueFactory(new PropertyValueFactory<IncarichiElenco,String>("proclamatore"));
		reparto.setCellValueFactory(new PropertyValueFactory<IncarichiElenco,String>("reparto"));
		ruolo.setCellValueFactory(new PropertyValueFactory<IncarichiElenco,String>("ruolo"));
		valda.setCellValueFactory(new PropertyValueFactory<IncarichiElenco,String>("validoda"));
		vala.setCellValueFactory(new PropertyValueFactory<IncarichiElenco,String>("validoa"));
		
		ArrayList<IncarichiElenco> ar = new ArrayList<>();
		for (int i = 0; i < res.length(); i++) {
			JSONObject obj = res.getJSONObject(i);
			IncarichiElenco ie = new IncarichiElenco();
			
			c = i;
			
			ie.setId(Utils.isNull(obj,"id")?"":(obj.getString("id")));
			ie.setProclamatore(Utils.isNull(obj,"proclamatore")?"":obj.getString("proclamatore"));
			ie.setReparto(Utils.isNull(obj,"reparto")?"":(obj.getString("reparto")));
			ie.setRuolo(Utils.isNull(obj,"ruolo")?"":(obj.getString("ruolo")));
			ie.setValidoda(Utils.isNull(obj,"validoda")?"":(obj.getString("validoda")));
			ie.setValidoa(Utils.isNull(obj,"validoa")?"":(obj.getString("validoa")));
			
			ar.add(ie);
			
		}
		ObservableList<IncarichiElenco> lst = (ObservableList<IncarichiElenco>) ObservableListMaker.createList(ar);
		tb.setItems(lst );
		tb.getColumns().addAll(id,proc, reparto, ruolo, valda, vala);	
		tb.setEditable(true);		

		/*ActionsMenus am = new ActionsMenus();
		am.addAction("Modifica", "b.actions.incarichi.Elenco", "ModificaRow");
		am.addAction("Cancella", "b.actions.incarichi.Elenco", "CancellaRow");
		ArrayList<HashMap<String, String>> actions = am.getActions();
		final TableView newtb = NodeUtils.addContextMenuToTableView(tb, actions, thisStage);*/
		
		
		
		return tb;
	}
	
	public static void ModificaRow(Stage stage) {
		Modifica.view(stage);
	}
	
	public static void CancellaRow(Stage stage) {
		Scene sc = stage.getScene();
		TableView tb = (TableView)sc.lookup("#elenco");
		IncarichiElenco ie = (IncarichiElenco)tb.getSelectionModel().getSelectedItem();
		
		String qry = "delete from gp_incarichiassegnati where id = "+ie.getId();
		JSONArray r = GePrato.getDeleteResponse(qry );
		
		tb.getColumns().clear();
		tb = Elenco.setElements(tb);
	}
	
	
	private static void stampariepilogo(Stage stage) {
		TableView tb = (TableView)stage.getScene().lookup("#elenco");
		
		Printers.stampaRiepilogoIncarichi();
	}
	
}


