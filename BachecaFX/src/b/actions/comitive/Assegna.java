package b.actions.comitive;

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
	public static  int totComitiveMese;
	
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
		
		Button offertemese = (Button)stage.getScene().lookup("#offertemese");
		offertemese.setDisable(false);
		offertemese.setOnAction(getOfferteMeseAction(stage));
		
		int numMese = selmese.getSelectionModel().getSelectedIndex();
		int anno = Integer.valueOf((String)selanno.getSelectionModel().getSelectedItem());
		
		ArrayList<Date> dates = Utils.getAllDatesByMonthAndYear(numMese+1,anno);
		
		/* Recupero tutte le comitive inserite */	
		
		String qry = "select attivo, giorno, id, luogo, ora from gp_comitive c where c.attivo = 1 order by attivo, luogo, giorno, ora";
		JSONArray res = GePrato.getSelectResponse(qry);
		
		totComitiveMese = 0;
		
		TableView<AssegnaComitive> tb = (TableView<AssegnaComitive>)stage.getScene().lookup("#tabellaAssegnazione");
		tb.getColumns().clear();
		
		ArrayList<AssegnaComitive> ar = new ArrayList<>();
		
		/* Ciclo le date del mese e verifico in quali giorni sono presenti comitive */
		
		int numConduttoriGiaAssegnati = 0;
		int totDateComitiveMese = 0;
		
		for (int i = 0; i < dates.size(); i++) {
			Date d = dates.get(i);
			String dayName = Utils.getDayNameByDate(d);
			for (int j = 0; j < res.length(); j++) {
				JSONObject obj = res.getJSONObject(j);
				String gg = obj.getString("giorno");
				String attivo = obj.getString("attivo");
				if(gg.substring(0, 3).toLowerCase().equals(dayName.substring(0, 3).toLowerCase())  && attivo.equals("1")) {
					totDateComitiveMese++;
					Label l = new Label();
					l.setText(Utils.reverseDateInString(d)+" - "+dayName+" - "+obj.getString("ora")+" - "+obj.getString("luogo")+" ");
					
					AssegnaComitive e = new AssegnaComitive();
					String data = Utils.reverseDateInString(d);
					e.setId(obj.getString("id"));
					e.setData(data);
					e.setGiorno(dayName);			
					e.setLuogo(obj.getString("luogo"));
					e.setOra(obj.getString("ora"));
					/*** Conduttori ***/
					
					/* verifico che non sia già stata assegnata */
					String qV = " SELECT * FROM gp_comitiveassegnate ca " + 
							 	" where ca.data = STR_TO_DATE('"+data+"','%d/%m/%Y') "+
								" and idcomitiva = "+obj.getString("id");
					
					JSONArray rV = GePrato.getSelectResponse(qV);
					String  idcondSalvato = null;
					if(rV.length()>0) {
						 idcondSalvato = rV.getJSONObject(0).getString("idconduttore");
						 numConduttoriGiaAssegnati++;
					}
					/* recupero tutti gli anziani e i servitori */
					String q = 	" SELECT cognome, nome, id FROM gp_proclamatori p " + 
								" where (p.anziano = 1 or p.sdm=1) " + 
								" and p.idcongregazione = 1";
					JSONArray r = GePrato.getSelectResponse(q);
					ArrayList<String> cond = new ArrayList<>();
					for (int k = 0; k < r.length(); k++) {
						JSONObject objCond = r.getJSONObject(k);
						String cog = objCond.getString("cognome");
						String nome = objCond.getString("nome");
						cond.add(cog+" "+nome);
						if(idcondSalvato!=null && idcondSalvato.equals(objCond.getString("id")))
							idcondSalvato = k+"";
					}
					ComboBox<String> b = new ComboBox<>();
					b.getItems().addAll(cond);
					if(idcondSalvato!=null)
						b.getSelectionModel().select(Integer.valueOf(idcondSalvato));
					EventHandler<ActionEvent> eh = new EventHandler<ActionEvent>() {
						
						@Override
						public void handle(ActionEvent event) {
							
							if(b.getSelectionModel().getSelectedItem()!=null)
								sa.setDisable(false);
							
						}
					};
					b.setId("cond_"+totComitiveMese);
					b.setOnAction(eh);
					e.setConduttore(b);
					ar.add(e);
					totComitiveMese++;
					//VBox vb = (VBox)stage.getScene().lookup("#vbox");
					//vb.getChildren().add(l);
				}				
			}			
		}		
		
		/* STATI
		 * 
		 * DA ASSEGNARE  (quando non c'è nessuna assegnazione)
		 * DA COMPLETARE/MODIFICARE  (quando c'è almeno una assegnazione)
		 * CHIUSO  (quando ci sono tutte le assegnazioni e/o è stato indicata la chiusura)
		 */
		
		int chiuso = 0;
		Label stato = (Label)stage.getScene().lookup("#stato");
		Label labelStato = (Label)stage.getScene().lookup("#labelStato");
		CheckBox chiudi = (CheckBox)stage.getScene().lookup("#chiudi");		
		Button stampa = (Button)stage.getScene().lookup("#stampa");
		stampa.setDisable(true);
		stampa.setOnAction(getStampa(stage));
		
		stato.setVisible(true);
		labelStato.setVisible(true);
		chiudi.setVisible(true);
		
		if(numConduttoriGiaAssegnati==0) {
			stato.setText("DA ASSEGNARE");
			stato.setTextFill(Color.web("#00B413"));
		}else if(numConduttoriGiaAssegnati>0 && numConduttoriGiaAssegnati<totDateComitiveMese) {
			stato.setText("DA COMPLETARE");
			stato.setTextFill(Color.web("#E78500"));
			stampa.setDisable(false);
		}else if(numConduttoriGiaAssegnati==totDateComitiveMese || chiuso == 1) {
			stato.setText("COMPLETO");
			stato.setTextFill(Color.web("#E70000"));
			stampa.setDisable(false);
		}
		
		
		EventHandler<ActionEvent> ehSa = new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				for (int i = 0; i < totComitiveMese; i++) {
					ComboBox cond = (ComboBox)stage.getScene().lookup("#cond_"+i);
					String sel = (String)cond.getSelectionModel().getSelectedItem();
					if(sel!=null && !sel.equals("")) {
						String[] arSelCond = sel.split(" ");
						String qry1= "select id from gp_proclamatori where cognome = '"+arSelCond[0]+"' and nome = '"+arSelCond[1]+"'";
						JSONArray r1 = GePrato.getSelectResponse(qry1);					
						String idcond = null;
						if(r1.length()>0 && r1.length()<2) {
							idcond = r1.getJSONObject(0).get("id").toString();
							
							TableView<AssegnaComitive> tb1 = (TableView<AssegnaComitive>)stage.getScene().lookup("#tabellaAssegnazione");
							ObservableList<AssegnaComitive> itms = tb1.getItems();
							AssegnaComitive row = itms.get(i);
							
							/* Controllo s eesiste già un salvataggio perla stessa comitiva */
							String chk = " select * " + 
										 " from gp_comitiveassegnate ca " + 
										 " where ca.data = STR_TO_DATE('"+row.getData()+"','%d/%m/%Y') "+
										 " and idcomitiva = "+row.getId();
							JSONArray rC = GePrato.getSelectResponse(chk);
							if(rC.length()==0) {
								String ins1 = "insert into gp_comitiveassegnate "
										  + " (idcomitiva, data, idconduttore) "
										  + " values ("+row.getId()+", "
										  + " STR_TO_DATE('"+row.getData()+"','%d/%m/%Y'), "
										  + " "+idcond+") ";
								GePrato.getInsertResponse(ins1);
							}else {
								String ins1 = "update gp_comitiveassegnate "
										  + " set idconduttore = "+idcond+" "
										  + " where data = STR_TO_DATE('"+row.getData()+"','%d/%m/%Y') "
										  + " and idcomitiva = "+row.getId();
								GePrato.getUpdateResponse(ins1);
							}
							
							
						}
					}
				}
			
				
			}
		};
		sa.setOnAction(ehSa );
		
		
		ObservableList<AssegnaComitive> lst = (ObservableList<AssegnaComitive>) ObservableListMaker.createList(ar);
		tb.setItems(lst);
		tb = (TableView<AssegnaComitive>) Utils.createTableColumnsListFromString(tb, "data, giorno, luogo, ora, conduttore");
		tb.setEditable(true);
		
	}

	private static EventHandler<ActionEvent> getOfferteMeseAction(Stage stage) {
		EventHandler<ActionEvent> eh = new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				ComboBox selmese = (ComboBox)stage.getScene().lookup("#mese");
				ComboBox selanno = (ComboBox)stage.getScene().lookup("#anno");
				int nummese = (selmese.getSelectionModel().getSelectedIndex())+1;
				Object numanno = selanno.getSelectionModel().getSelectedItem();
				
				
				FXMLLoader loader1 = new FXMLLoader(Utils.getResourceUrl("template/offertemese.fxml"));
				
				try {
					AnchorPane page1 = (AnchorPane)loader1.load();
					Scene scene2 = new Scene(page1);
					Stage newStage = new Stage();
					
					Button salva = (Button)page1.lookup("#salva");
					salva.setOnAction(salvaOfferteMese(newStage,page1, nummese, numanno));
					
					/* Se esistono offerte per questo mese le visualizzo */
					TextArea of1 = (TextArea)page1.lookup("#offerta1");
					TextArea of2 = (TextArea)page1.lookup("#offerta2");
					TextArea of3 = (TextArea)page1.lookup("#offerta3");
					
					String off = " select * " + 
							 " from gp_offertemese om " + 
							 " where om.anno = "+numanno+" "+
							 " and om.mese = "+nummese;
					JSONArray rOff = GePrato.getSelectResponse(off);
					for (int i = 0; i < rOff.length(); i++) {
						JSONObject obj = rOff.getJSONObject(i);
						String numero = obj.getString("numero");
						if(numero.equals("1"))
							of1.setText(obj.getString("descrizione"));
						else if(numero.equals("2"))
							of2.setText(obj.getString("descrizione"));
						else if(numero.equals("3"))
							of3.setText(obj.getString("descrizione"));
						
					}
					
					
					
				     newStage.setScene(scene2);
				     //tell stage it is meannt to pop-up (Modal)
				     newStage.initModality(Modality.APPLICATION_MODAL);
				     newStage.setTitle("Offerte Mese ");
				     newStage.show();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}						
			}

			

		};
		return eh;
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
	
	private static EventHandler<ActionEvent> salvaOfferteMese(Stage newStage, AnchorPane ap, int nummese, Object numanno) {
		EventHandler<ActionEvent> eh = new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				TextArea of1 = (TextArea)ap.lookup("#offerta1");
				TextArea of2 = (TextArea)ap.lookup("#offerta2");
				TextArea of3 = (TextArea)ap.lookup("#offerta3");
				
				String txtOf1 = of1.getText();
				String txtOf2 = of2.getText();
				String txtOf3 = of3.getText();
				
				/* Controllo se esistono già offerte per questo mese e anno */
				String chk = " select * " + 
							 " from gp_offertemese om " + 
							 " where om.anno = "+numanno+" "+
							 " and om.mese = "+nummese;
				JSONArray rOff = GePrato.getSelectResponse(chk);
				if(rOff.length()==0) {
					String insBase = "INSERT INTO gp_offertemese(mese, anno, descrizione, numero)  "+
							  " values ("+nummese+", "+numanno+", ";
					
					if(txtOf1!=null && !txtOf1.isEmpty()) {
						String ins1 = insBase+" '"+txtOf1+"',1)";
						GePrato.getInsertResponse(ins1);
					}
					if(txtOf2!=null && !txtOf2.isEmpty()) {
						String ins2 = insBase+" '"+txtOf2+"',2)";
						GePrato.getInsertResponse(ins2);
					}
					if(txtOf3!=null && !txtOf3.isEmpty()) {
						String ins3 = insBase+" '"+txtOf3+"',3)";
						GePrato.getInsertResponse(ins3);
					}
					
					
				}else {
					String upBase = "update gp_offertemese "
							  + " set descrizione = ";
					String whereBase = " where anno = "+numanno+" "
							  + " and mese = "+nummese+" "
							  + " and numero = ";
					
					for (int i = 0; i < rOff.length(); i++) {
						JSONObject obj = rOff.getJSONObject(i);
						String numero = obj.getString("numero");
						
						if(numero.equals("1") && txtOf1!=null && !txtOf1.isEmpty()) {
							String up1 = upBase+"'"+txtOf1+"' "+whereBase+"1 ";
							GePrato.getUpdateResponse(up1);
						}
						if(numero.equals("2") &&txtOf2!=null && !txtOf2.isEmpty()) {
							String up2 = upBase+"'"+txtOf2+"' "+whereBase+"2 ";
							GePrato.getUpdateResponse(up2);
						}
						if(numero.equals("3") &&txtOf3!=null && !txtOf3.isEmpty()) {
							String up3 = upBase+"'"+txtOf3+"' "+whereBase+"3 ";
							GePrato.getUpdateResponse(up3);
						}		
						
					}
					
											
				}
				
				newStage.close();
			}
		};
		return eh;
	}
	
	
	
	
}
