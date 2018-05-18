package b.actions.incarichi;

import java.io.IOException;
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
import b.printfoot.AssegnaComitive;
import b.printfoot.Comitive;
import b.printfoot.ComitiveElenco;
import b.printfoot.IncarichiMese;
import b.printfoot.Proclamatori;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.Window;

public class Assegna {
	
	public static Stage thisStage;
	public static  int totIncarichi;
	
	public static void view(Stage primaryStage) {
		thisStage = primaryStage;
		int c = 0;
		try {
			FXMLLoader loader = new FXMLLoader(Utils.getResourceUrl("template/incarichi_assegna.fxml"));
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
					
					//Utils.startLoading(page);			
					Assegna.show(primaryStage);	
					//Utils.stopLoading(page);
				}
				
			};
			bt.setOnAction(eh);
			
			Label stato = (Label)page.lookup("#stato");
			Label labelStato = (Label)page.lookup("#labelStato");
			CheckBox chiudi = (CheckBox)page.lookup("#chiudi");	
			
			stato.setVisible(false);
			labelStato.setVisible(false);
			chiudi.setVisible(false);
			
			primaryStage.show();
		}catch (Exception e) {
			System.out.println("Riga "+c+"  :  "+e);
		}
	}
	
	private static void show(Stage stage) {

		ComboBox selmese = (ComboBox)stage.getScene().lookup("#mese");
		ComboBox selanno = (ComboBox)stage.getScene().lookup("#anno");
		Button sa = (Button)stage.getScene().lookup("#salvaAssegnazioni");
		sa.setDisable(true);
		
		int numMese = selmese.getSelectionModel().getSelectedIndex();
		int anno = Integer.valueOf((String)selanno.getSelectionModel().getSelectedItem());
		
		ArrayList<Date> dates = Utils.getAllDatesByMonthAndYear(numMese+1,anno);
		
		/* Recupero tutte le comitive inserite */	
		
		String qry = "select * from gp_incarichimese im where im.mese='"+numMese+"' and anno = "+anno+" order by data";
		JSONArray res = GePrato.getSelectResponse(qry);
		
		totIncarichi = 0;
		
		TableView<IncarichiMese> tb = (TableView<IncarichiMese>)stage.getScene().lookup("#tabellaIncarichi");
		tb.getColumns().clear();
		
		ArrayList<AssegnaComitive> ar = new ArrayList<>();
		
		/* Ciclo le date del mese e verifico in quali giorni sono presenti le adunanze ed eventuali incarichi assegnati */
		
		int numIncarichiGiaAssegnati = 0;
		int totDateAdunanzeMese = 0;
		
		/* recupero i giorni delle adunanze dal db*/
		String qAdu = "SELECT * " + 
				"FROM gp_dateorari d " + 
				"WHERE d.tipo in ('AI','AP')";
		
		JSONArray rs = GePrato.getSelectResponse(qAdu);
		if(rs.length()>0) {
		
			String ggAI = "";
			String oraAI = "";
			
			String ggAP = "";
			String oraAP = "";
			
			for (int i = 0; i < rs.length(); i++) {
				String tipo = rs.getJSONObject(i).getString("tipo");
				String giorno = rs.getJSONObject(i).getString("giorno");
				String ora = rs.getJSONObject(i).getString("ora");
				if(tipo.equals("AI")) {
					ggAI = giorno;
					oraAI = ora;
				}else {
					ggAP = giorno;
					oraAP = ora;
				}
			}
			
		for (int i = 0; i < dates.size(); i++) {
			Date d = dates.get(i);
			String dayName = Utils.getDayNameByDate(d);
			
			if(dayName.equals(ggAI.toLowerCase()) || dayName.equals(ggAP.toLowerCase())) {
				
				String data = Utils.reverseDateInString(d);
				
				//Controllo se esistono incarichi già assegnati per questa data
				String q = "SELECT * FROM gp_incarichimese where STR_TO_DATE('01/01/2018', '%dd/%m/%Y')";
				JSONArray r = GePrato.getSelectResponse(q);
				if(r.length()>0) {
					
				}
				
				System.out.println(data);
			}
			
		}		
		
		/* STATI
		 * 
		 * DA ASSEGNARE  (quando non c'è nessuna assegnazione)
		 * DA COMPLETARE/MODIFICARE  (quando c'è almeno una assegnazione)
		 * CHIUSO  (quando ci sono tutte le assegnazioni e/o è stato indicata la chiusura)
		 */
		
//		int chiuso = 0;
//		Label stato = (Label)stage.getScene().lookup("#stato");
//		Label labelStato = (Label)stage.getScene().lookup("#labelStato");
//		CheckBox chiudi = (CheckBox)stage.getScene().lookup("#chiudi");		
//		Button stampa = (Button)stage.getScene().lookup("#stampa");
//		stampa.setDisable(true);
//		stampa.setOnAction(getStampa(stage));
//		
//		stato.setVisible(true);
//		labelStato.setVisible(true);
//		chiudi.setVisible(true);
//		
//		if(numConduttoriGiaAssegnati==0) {
//			stato.setText("DA ASSEGNARE");
//			stato.setTextFill(Color.web("#00B413"));
//		}else if(numConduttoriGiaAssegnati>0 && numConduttoriGiaAssegnati<totDateComitiveMese) {
//			stato.setText("DA COMPLETARE");
//			stato.setTextFill(Color.web("#E78500"));
//			stampa.setDisable(false);
//		}else if(numConduttoriGiaAssegnati==totDateComitiveMese || chiuso == 1) {
//			stato.setText("COMPLETO");
//			stato.setTextFill(Color.web("#E70000"));
//			stampa.setDisable(false);
//		}
		
		
		EventHandler<ActionEvent> ehSa = new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
//				for (int i = 0; i < totComitiveMese; i++) {
//					ComboBox cond = (ComboBox)stage.getScene().lookup("#cond_"+i);
//					String sel = (String)cond.getSelectionModel().getSelectedItem();
//					if(sel!=null && !sel.equals("")) {
//						String[] arSelCond = sel.split(" ");
//						String qry1= "select id from gp_proclamatori where cognome = '"+arSelCond[0]+"' and nome = '"+arSelCond[1]+"'";
//						JSONArray r1 = GePrato.getSelectResponse(qry1);					
//						String idcond = null;
//						if(r1.length()>0 && r1.length()<2) {
//							idcond = r1.getJSONObject(0).get("id").toString();
//							
//							TableView<AssegnaComitive> tb1 = (TableView<AssegnaComitive>)stage.getScene().lookup("#tabellaAssegnazione");
//							ObservableList<AssegnaComitive> itms = tb1.getItems();
//							AssegnaComitive row = itms.get(i);
//							
//							/* Controllo s eesiste già un salvataggio perla stessa comitiva */
//							String chk = " select * " + 
//										 " from gp_comitiveassegnate ca " + 
//										 " where ca.data = STR_TO_DATE('"+row.getData()+"','%d/%m/%Y') "+
//										 " and idcomitiva = "+row.getId();
//							JSONArray rC = GePrato.getSelectResponse(chk);
//							if(rC.length()==0) {
//								String ins1 = "insert into gp_comitiveassegnate "
//										  + " (idcomitiva, data, idconduttore) "
//										  + " values ("+row.getId()+", "
//										  + " STR_TO_DATE('"+row.getData()+"','%d/%m/%Y'), "
//										  + " "+idcond+") ";
//								GePrato.getInsertResponse(ins1);
//							}else {
//								String ins1 = "update gp_comitiveassegnate "
//										  + " set idconduttore = "+idcond+" "
//										  + " where data = STR_TO_DATE('"+row.getData()+"','%d/%m/%Y') "
//										  + " and idcomitiva = "+row.getId();
//								GePrato.getUpdateResponse(ins1);
//							}
//							
//							
//						}
//					}
//				}
			
				
			}
		};
		sa.setOnAction(ehSa );
		
		
		ObservableList<IncarichiMese> lst = (ObservableList<IncarichiMese>) ObservableListMaker.createList(ar);
		tb.setItems(lst);
		tb = (TableView<IncarichiMese>) Utils.createTableColumnsListFromString(tb, "data, giorno, luogo, ora, conduttore");
		tb.setEditable(true);
		}else {
			//Aggingere messaggio delle date adunanze mancanti
		}
		
	}

	
	private static EventHandler<ActionEvent> getStampa(Stage stage) {
		EventHandler<ActionEvent> eh = new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				ComboBox selmese = (ComboBox)stage.getScene().lookup("#mese");
				ComboBox selanno = (ComboBox)stage.getScene().lookup("#anno");
				int nummese = (selmese.getSelectionModel().getSelectedIndex())+1;
				Object numanno = selanno.getSelectionModel().getSelectedItem();
				Printers.stampaComitive(nummese+"", numanno.toString());
				System.out.println(numanno+" "+nummese);
			}
		};
		return eh;
	}
	
	
	
	
}
