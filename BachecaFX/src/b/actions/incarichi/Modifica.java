package b.actions.incarichi;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

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
import b.printfoot.IncarichiElenco;
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
import javafx.scene.control.DatePicker;
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
		public static IncarichiElenco ie;
	
	public static void view(Stage primaryStage) {
		int c = 0;
		try {
			Scene sc = primaryStage.getScene();
			TableView tb = (TableView)sc.lookup("#elenco");
			
			FXMLLoader loader = new FXMLLoader(Utils.getResourceUrl("template/incarichi_aggiungi.fxml"));
			AnchorPane page = (AnchorPane)loader.load();	
			
			ie = (IncarichiElenco)tb.getSelectionModel().getSelectedItem();
			
			Scene scene = new Scene(page);			
			Stage secondStage = new Stage();
		
			MenuBar mb = (MenuBar)page.lookup("#menuPrincipal");
			//MenuPrincipal mp = new MenuPrincipal(mb, primaryStage);
			
			/****/
			ComboBox sru = (ComboBox) page.lookup("#selRuolo");
			String[] rr = "Assistente,Responsabile,Assegnato".split(",");
			sru.getItems().addAll(rr);		
			sru.getSelectionModel().select(ie.getRuolo());
			/****/
			ComboBox sre = (ComboBox) page.lookup("#selReparto");
			ArrayList<String> reparti = new ArrayList<>();			
			
			String query = " SELECT reparto "+
					" FROM gp_incarichi i order by reparto";
			JSONArray res = GePrato.getSelectResponse(query);
			for (int i = 0; i < res.length(); i++) {
				JSONObject objR = res.getJSONObject(i);
				reparti.add(objR.getString("reparto"));
			}
			sre.getItems().addAll(reparti);
			sre.getSelectionModel().select(ie.getReparto());
			/****/
			/****/
			ComboBox si = (ComboBox) page.lookup("#selIncaricato");
			si.setDisable(true);
			ArrayList<String> incaricati = new ArrayList<>();			
			
			String qI = "SELECT concat(cognome,' ', nome) as proclamatore, sdm, anziano "+
					" FROM gp_proclamatori "+
					" WHERE sesso = 'M' order by cognome";
			JSONArray resQi = GePrato.getSelectResponse(qI);
			for (int i = 0; i < resQi.length(); i++) {
				JSONObject objR = resQi.getJSONObject(i);
				
				String nomina = "";				
				if(objR.getString("sdm").equals("1"))
					nomina = " (SDM)";
				else if (objR.getString("anziano").equals("1"))
					nomina = " (ANZ)";
				incaricati.add(objR.getString("proclamatore")+nomina);
			}
			si.getItems().addAll(incaricati);
			si.getSelectionModel().select(ie.getProclamatore());
			/****/
			DatePicker valda 	= (DatePicker)page.lookup("#validoda");
			DatePicker vala 	= (DatePicker)page.lookup("#validoa");
			
			String valdaValue = ie.getValidoda();
			String valaValue = ie.getValidoa();
			if(valdaValue!=null && !valdaValue.isEmpty())
				valda.setValue(LocalDate.of(Integer.valueOf(valdaValue.split("-")[0]),Integer.valueOf( valdaValue.split("-")[1]),Integer.valueOf( valdaValue.split("-")[2])));
			if(valaValue!=null && !valaValue.isEmpty())
			vala.setValue(LocalDate.of(Integer.valueOf(valaValue.split("-")[0]),Integer.valueOf( valaValue.split("-")[1]),Integer.valueOf( valaValue.split("-")[2])));
			//valda.setValue();
			
			/****/
			Button bt = (Button) page.lookup("#salva");
			EventHandler<ActionEvent> eh = new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent arg0) {
					salvaIncarico();
					secondStage.close();
					Scene scn = primaryStage.getScene();
					AnchorPane ap = (AnchorPane)scn.lookup("#anchor");
					TableView tb = (TableView)ap.lookup("#elenco");
					tb.getColumns().clear();
					tb = Elenco.setElements(tb);
					
				}

				private void salvaIncarico() {
					String ruolo = sru.getSelectionModel().getSelectedItem().toString();
					String reparto = sre.getSelectionModel().getSelectedItem().toString();
					
					String qR = "SELECT id "+
							" FROM gp_incarichi "+
							" WHERE reparto = '"+reparto+"'";
					JSONArray resQr = GePrato.getSelectResponse(qR);
					String idreparto = "";
					if(resQr.length()>0)
						idreparto = resQr.getJSONObject(0).getString("id");
					
					String incaricato = si.getSelectionModel().getSelectedItem().toString();
					
					String nome = incaricato.split(" ")[1];
					String cogn = incaricato.split(" ")[0];
					
					String qI = "SELECT id "+
							" FROM gp_proclamatori "+
							" WHERE sesso = 'M' and nome = '"+nome+"' and cognome = '"+cogn+"'";
					JSONArray resQi = GePrato.getSelectResponse(qI);
					String idproc = "";
					if(resQi.length()>0)
						idproc = resQi.getJSONObject(0).getString("id");
					
					LocalDate valValda = valda.getValue();
					LocalDate valVala = vala.getValue();
					
					String validodaStr = "";
					String validodaValue = "";
					String validoaStr = "";
					String validoaValue = "";
					if(valValda!=null) {
						validodaStr = ", validoda";	
						
						String gg,mm,yyyy;
						if(valValda.getDayOfMonth()<9)
							gg = "0"+valValda.getDayOfMonth();
						else
							gg = valValda.getDayOfMonth()+"";
						
						if(valValda.getMonthValue()<9)
							mm = "0"+valValda.getMonthValue();
						else
							mm = valValda.getDayOfMonth()+"";
						
						yyyy = valValda.getYear()+"";
						validodaValue = ", STR_TO_DATE('"+gg+"/"+mm+"/"+yyyy+"','%d/%m/%Y') ";
					}
					if(valVala!=null) {
						validoaStr = ", validoa";
						
						String gg,mm,yyyy;
						if(valVala.getDayOfMonth()<9)
							gg = "0"+valVala.getDayOfMonth();
						else
							gg = valVala.getDayOfMonth()+"";
						
						if(valVala.getMonthValue()<9)
							mm = "0"+valVala.getMonthValue();
						else
							mm = valVala.getDayOfMonth()+"";
						
						yyyy = valVala.getYear()+"";
						
						validoaValue = ", STR_TO_DATE('"+gg+"/"+mm+"/"+yyyy+"','%d/%m/%Y') ";
					}
						
					
				/*	String qry = "INSERT INTO gp_incarichiassegnati(idincarico, idproc, ruolo"+validodaStr+validoaStr+") "+
							"VALUES ("+idreparto+","+idproc+",'"+ruolo+"'"+ 
							validodaValue+
							validoaValue+")";
					JSONArray r = GePrato.getInsertResponse(qry);*/
					
					
					String qry = "UPDATE gp_incarichiassegnati set "
							+ " ruolo='"+ruolo+"',";
					if(validodaValue!=null && !validodaValue.isEmpty())
						qry+= " validoda="+validodaValue.substring(1,validodaValue.length())+", ";
					if(validoaValue!=null && !validoaValue.isEmpty())
						qry+= " validoa="+validoaValue+", ";
					qry+= " idincarico = "+idreparto+" "
							+ " where id = "+ie.getId();
					GePrato.getUpdateResponse(qry);
					
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
