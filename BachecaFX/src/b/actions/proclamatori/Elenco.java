package b.actions.proclamatori;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import b.GePrato;
import b.MenuPrincipal;
import b.ObservableListMaker;
import b.Utils;
import b.printfoot.Proclamatori;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Elenco {
		
	
	public static void view(Stage primaryStage) {
		int c = 0;
		try {
			FXMLLoader loader = new FXMLLoader(Utils.getResourceUrl("template/proclamatori_elenco.fxml"));
			AnchorPane page = (AnchorPane)loader.load();	
			
			MenuBar mb = (MenuBar)page.lookup("#menuPrincipal");
			MenuPrincipal mp = new MenuPrincipal(mb, primaryStage);
			
			ObservableList<Node> ch = page.getChildren();
			TableView<Proclamatori> tb = (TableView<Proclamatori>)ch.get(1);
			tb.setEditable(true);
			
			String query = "select nome, cognome, datanascita, email, anziano, sdm, pregolare from gp_proclamatori p";
			JSONArray res = GePrato.getSelectResponse(query);
			
			/* Colonne */
			TableColumn nome = new TableColumn("Nome");
			TableColumn cognome = new TableColumn("Cognome");
			TableColumn datanascita = new TableColumn("Data di Nascita");
			TableColumn email = new TableColumn("Email");
			TableColumn anziano = new TableColumn("Anziano");
			TableColumn sdm = new TableColumn("Servitore");
			TableColumn pregolare = new TableColumn("P. Regolare");
			
			/* Associo le colonne al printfeet */
			nome.setCellValueFactory(new PropertyValueFactory<Proclamatori,String>("nome"));
			cognome.setCellValueFactory(new PropertyValueFactory<Proclamatori,String>("cognome"));
			datanascita.setCellValueFactory(new PropertyValueFactory<Proclamatori,String>("datanascita"));
			email.setCellValueFactory(new PropertyValueFactory<Proclamatori,String>("email"));
			anziano.setCellValueFactory(new PropertyValueFactory<Proclamatori,String>("anziano"));
			sdm.setCellValueFactory(new PropertyValueFactory<Proclamatori,String>("sdm"));
			pregolare.setCellValueFactory(new PropertyValueFactory<Proclamatori,String>("pregolare"));
			
			ArrayList<Proclamatori> ar = new ArrayList<>();
			for (int i = 0; i < res.length(); i++) {
				JSONObject obj = res.getJSONObject(i);
				Proclamatori p = new Proclamatori();
				
				c = i;
				
				p.setNome(Utils.isNull(obj,"nome")?"":(obj.getString("nome")));
				p.setCognome(Utils.isNull(obj,"cognome")?"":(obj.getString("cognome")));
				p.setDatanascita(Utils.isNull(obj,"datanascita")?"":(obj.getString("datanascita")));
				p.setEmail(Utils.isNull(obj,"email")?"":(obj.getString("email")));
				p.setAnziano(Utils.isNull(obj,"anziano")?"":(obj.getString("anziano")));
				p.setSdm(Utils.isNull(obj,"sdm")?"":(obj.getString("sdm")));			
				p.setPregolare(Utils.isNull(obj,"pregolare")?"":(obj.getString("pregolare")));
				
				ar.add(p);
				
			}
			ObservableList<Proclamatori> lst = (ObservableList<Proclamatori>) ObservableListMaker.createList(ar);
			tb.setItems(lst );
			tb.getColumns().addAll(nome, cognome, datanascita, email, anziano, sdm, pregolare);

			Scene scene = new Scene(page);
			primaryStage.setTitle("Bacheca");
			primaryStage.setScene(scene);
			primaryStage.show();
		}catch (Exception e) {
			System.out.println("Riga "+c+"  :  "+e);
		}
	}
}
