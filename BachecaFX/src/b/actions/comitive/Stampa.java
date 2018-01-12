package b.actions.comitive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import b.Logs;
import b.MenuPrincipal;
import b.Utils;
import b.printers.Printers;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Stampa {

	public static void view(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(Utils.getResourceUrl("template/testimonianza_pubblica_stampa.fxml"));
			AnchorPane page = (AnchorPane)loader.load();
			ObservableList<Node> ch = page.getChildren();
			
			MenuBar mb = (MenuBar)page.lookup("#menuPrincipal");
			MenuPrincipal mp = new MenuPrincipal(mb, primaryStage);
			
			Scene scene = new Scene(page);
			
			ComboBox<String> mese,anno,tipo;
					
			for (int i = 0; i < ch.size(); i++) {
				String id = ch.get(i).getId();
				if(id!=null && id.equals("tipo")) {
					tipo = (ComboBox<String>) ch.get(i);
					tipo.getItems().addAll("Carrelli","Stand","Unificato");
				}else if(id!=null && id.equals("mese")) {
					mese = (ComboBox<String>) ch.get(i);
					mese.getItems().addAll("Gennaio","Febbraio","Marzo","Aprile","Maggio","Giugno","Luglio","Agosto","Settembre","Ottobre","Novembre","Dicembre");
				}else if(id!=null && id.equals("anno")) {
					anno = (ComboBox<String>) ch.get(i);
					
					/* calcolo l'anno attuale */
					Date t = new Date();
					GregorianCalendar gc = new GregorianCalendar();
					gc.setTime(t);
					int annoCorrente = gc.get(1);
					
					ArrayList<String> anni = new ArrayList<>();
					anni.add((annoCorrente-1)+"");
					anni.add((annoCorrente)+"");
					for (int j = 1; j < 9; j++) {
						int a = annoCorrente+j;
						anni.add(a+"");
					}
					
					anno.getItems().addAll(anni);
				}else if(id!=null && id.equals("stampa")) {
					Button itm = (Button) ch.get(i);
					EventHandler<ActionEvent> eh = new EventHandler<ActionEvent>() {
						
						@Override
						public void handle(ActionEvent arg0) {
							
							ComboBox<String> tipo = (ComboBox<String>) scene.lookup("#tipo");
							ComboBox<String> mese = (ComboBox<String>) scene.lookup("#mese");
							ComboBox<String> anno = (ComboBox<String>) scene.lookup("#anno");
							
							String selTipo = tipo.getSelectionModel().getSelectedItem().toString();
							String selMese = mese.getSelectionModel().getSelectedItem().toString();
							String selAnno = anno.getSelectionModel().getSelectedItem().toString();							
							
							if(!selTipo.equals("Unificato")) {
								
								Utils.alert("Non è al momento abilitata la stampa separata");
								
							}else {			
								Logs.write("stampo");
								Printers.stampaProgramma(selMese, selAnno);
							}
						}
					};
					itm.setOnAction(eh);
				}
			}
			
			
			primaryStage.setTitle("Bacheca");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		
	}
	
}
