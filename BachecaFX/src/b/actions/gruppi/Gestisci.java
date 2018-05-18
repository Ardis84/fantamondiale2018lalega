package b.actions.gruppi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import org.json.JSONArray;
import org.json.JSONObject;

import b.GePrato;
import b.Logs;
import b.MenuPrincipal;
import b.ObservableListMaker;
import b.Utils;
import b.actions.incarichi.Aggiungi;
import b.printers.Printers;
import b.printfoot.Gruppi;
import b.printfoot.IncarichiElenco;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Gestisci {

	public static void view(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(Utils.getResourceUrl("template/gruppi_gestisci.fxml"));
			AnchorPane page = (AnchorPane)loader.load();
			ObservableList<Node> ch = page.getChildren();
			
			MenuBar mb = (MenuBar)page.lookup("#menuPrincipal");
			MenuPrincipal mp = new MenuPrincipal(mb, primaryStage);
			
			TableView<Gruppi> tb = (TableView<Gruppi>)ch.get(1);
			tb.setEditable(true);
			
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
			Button bt = (Button)page.lookup("#add");
			EventHandler<ActionEvent> eh = new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent arg0) {
					Aggiungi.view(primaryStage);				
				}
			};
			bt.setOnAction(eh);
			
			tb = setElements(tb);
						
			Scene scene = new Scene(page);
			primaryStage.setTitle("Bacheca");
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		
	}
	
	
	public static TableView<Gruppi> setElements(TableView<Gruppi> tb) {
		String query = " SELECT gs.id as idgruppo, concat(r.nome,' ',r.cognome) as responsabile, "+
				" concat(ass.nome,' ',ass.cognome) as assistente, "+
				" gs.descrizione as nomegruppo, gs.note, idgemellaggi, ggcomitiva, oracomitiva, luogocomitiva "+
				" FROM gp_gruppiservizio gs , gp_proclamatori r, gp_proclamatori ass "+
				" WHERE gs.idresponsabile = r.id "+
				" and gs.idassistente = ass.id ";
		JSONArray res = GePrato.getSelectResponse(query);
			
		/* Colonne */
		TableColumn id = new TableColumn("Id");
		TableColumn nome = new TableColumn("Nome Gruppo");
		TableColumn resp = new TableColumn("Responsabile");
		TableColumn ass = new TableColumn("Assistente");
		TableColumn gem = new TableColumn("Associato con");
		TableColumn com = new TableColumn("Comitiva");
		TableColumn note = new TableColumn("Note");
		
		/* Associo le colonne al printfeet */
		id.setCellValueFactory(new PropertyValueFactory<Gruppi,String>("id"));
		nome.setCellValueFactory(new PropertyValueFactory<Gruppi,String>("descrizione"));
		resp.setCellValueFactory(new PropertyValueFactory<Gruppi,String>("responsabile"));
		ass.setCellValueFactory(new PropertyValueFactory<Gruppi,String>("assistente"));
		gem.setCellValueFactory(new PropertyValueFactory<Gruppi,String>("gemellaggi"));
		com.setCellValueFactory(new PropertyValueFactory<Gruppi,String>("comitiva"));
		note.setCellValueFactory(new PropertyValueFactory<Gruppi,String>("note"));

		ArrayList<Gruppi> ar = new ArrayList<>();
		for (int i = 0; i < res.length(); i++) {
			JSONObject obj = res.getJSONObject(i);
			Gruppi g = new Gruppi();
			g.setId(Utils.isNull(obj,"idgruppo")?"":(obj.getString("idgruppo")));
			g.setDescrizione(Utils.isNull(obj,"nomegruppo")?"":(obj.getString("nomegruppo")));
			g.setResponsabile(Utils.isNull(obj,"responsabile")?"":(obj.getString("responsabile")));
			g.setAssistente(Utils.isNull(obj,"assistente")?"":(obj.getString("assistente")));
			g.setGemellaggi(Utils.isNull(obj,"idgemellaggi")?"":(obj.getString("idgemellaggi")));
			g.setComitiva(Utils.isNull(obj,"ggcomitiva")?"":(obj.getString("ggcomitiva")));		
			ar.add(g);			
		}
		
		ObservableList<Gruppi> lst = (ObservableList<Gruppi>) ObservableListMaker.createList(ar);
		tb.setItems(lst );
		tb.getColumns().addAll(id,nome, resp, ass, gem, com, note);	
		tb.setEditable(true);	
		
		return tb;
	}
	

	public static void ModificaRow(Stage primaryStage) {
		// TODO Auto-generated method stub
		
	}
	
	public static void CancellaRow(Stage primaryStage) {
		// TODO Auto-generated method stub
		
	}
}
