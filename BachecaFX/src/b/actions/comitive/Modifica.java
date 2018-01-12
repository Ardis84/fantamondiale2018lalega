package b.actions.comitive;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import b.GePrato;
import b.Logs;
import b.MenuPrincipal;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.Window;

public class Modifica {
		
	
	public static void view(Stage primaryStage) {
		int c = 0;
		try {
			
			Scene sc = primaryStage.getScene();
			TableView tb = (TableView)sc.lookup("#elenco");
			ComitiveElenco ce = (ComitiveElenco)tb.getSelectionModel().getSelectedItem();
			
			FXMLLoader loader = new FXMLLoader(Utils.getResourceUrl("template/comitive_aggiungi.fxml"));
			AnchorPane page = (AnchorPane)loader.load();	
			
			MenuBar mb = (MenuBar)page.lookup("#menuPrincipal");
			MenuPrincipal mp = new MenuPrincipal(mb, primaryStage);
			
			Scene scene = new Scene(page);			
			Stage secondStage = new Stage();
			
			TextField luogo = (TextField) page.lookup("#luogo");
			luogo.setText(ce.getLuogo());
			
			/****/
			ComboBox sg = (ComboBox) page.lookup("#selGiorno");
			String[] gg = "Lunedì,Martedì,Mercoledì,Giovedì,Venerdì,Sabato,Domenica".split(",");
			String ggSel = ce.getGiorno();
			int intGgSel = 0;
			for (int i = 0; i < gg.length; i++) {
				String g = gg[i];
				if(g.equals(ggSel)) {
					intGgSel = i;
					i =  gg.length;
				}
			}
			sg.getItems().addAll(gg);		
			sg.getSelectionModel().select(intGgSel);
			/****/
			ComboBox so = (ComboBox) page.lookup("#selOra");
			ArrayList<String> ore = new ArrayList<>();			
			String[] min = {"00","15","30","45"}; 
			String selOra = ce.getOra();
			int intSelOra = 0;
			int cc = 0;
			for (int j = 0; j < 24; j++) {
				String h = j+"";
				if(j<10) {
					h = "0"+h;
				}
				
				for (int k = 0; k < min.length; k++) {
					String elH = h+":"+min[k];
					ore.add(elH);
					if(elH.equals(selOra)) {
						intSelOra = cc;
					}
					cc++;
				}
			}
			so.getItems().addAll(ore);
			so.getSelectionModel().select(intSelOra);
			/****/
			Button bt = (Button) page.lookup("#salva");
			EventHandler<ActionEvent> eh = new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent arg0) {
					salvaComitiva();
					secondStage.close();
					Scene scn = primaryStage.getScene();
					AnchorPane ap = (AnchorPane)scn.lookup("#anchor");
					TableView tb = (TableView)ap.lookup("#elenco");
					tb.getColumns().clear();
					tb = Elenco.setElements(tb);
					
				}

				private void salvaComitiva() {
					TextField luogo = (TextField)scene.lookup("#luogo");
					String giorno = sg.getSelectionModel().getSelectedItem().toString();
					String ora = so.getSelectionModel().getSelectedItem().toString();
					
					String qry = "UPDATE gp_comitive set "
							+ " luogo='"+luogo.getText()+"',"
							+ " ora='"+ora+"', "
							+ " giorno='"+giorno+"', "
							+ " attivo=1 "
							+ " where id = "+ce.getId();
					JSONArray r = GePrato.getUpdateResponse(qry);
					
				}
			};
			bt.setOnAction(eh);		
			
			secondStage.setTitle("Aggiungi Comitive");
			secondStage.initModality(Modality.APPLICATION_MODAL);
			secondStage.setScene(scene);
			secondStage.show();
		}catch (Exception e) {
			System.out.println("Riga "+c+"  :  "+e);
		}
	}
}
