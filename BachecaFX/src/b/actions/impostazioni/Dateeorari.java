package b.actions.impostazioni;

import java.time.LocalDate;
import java.util.ArrayList;

import org.json.JSONArray;

import b.GePrato;
import b.MenuPrincipal;
import b.Utils;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Dateeorari {

	public static Stage thisStage;
	
	public static boolean existAI = false;
	public static boolean existAP = false;
	public static boolean existAS1 = false;
	public static boolean existAS2 = false;
	public static boolean existCDA = false;
	public static boolean existCA = false;
	
	public static void view(Stage primaryStage) {
		thisStage = primaryStage;
		int c = 0;
		try {
			FXMLLoader loader = new FXMLLoader(Utils.getResourceUrl("template/dateeorari.fxml"));
			AnchorPane page = (AnchorPane)loader.load();	
			
			MenuBar mb = (MenuBar)page.lookup("#menuPrincipal");
			MenuPrincipal mp = new MenuPrincipal(mb, primaryStage);

			ComboBox selAduInfra 	= (ComboBox) page.lookup("#selAduInfraGG");
			ComboBox selPubTdg 		= (ComboBox) page.lookup("#selPubTdgGG");
			
			String[] ggs = Utils.getNomiGiorniSettimana();
			
			ArrayList<String> infra = new ArrayList<>();
			ArrayList<String> pubbtdg = new ArrayList<>();
			
			for (int i = 0; i < ggs.length-2; i++) {
				infra.add(ggs[i]);
			}
			
			for (int i = ggs.length-2; i < ggs.length; i++) {
				pubbtdg.add(ggs[i]);
			}
			
			selAduInfra.getItems().addAll(infra);
			selPubTdg.getItems().addAll(pubbtdg);
						
			ArrayList<String> ore = new ArrayList<>();			
			String[] min = {"00","15","30","45"}; 
			
			for (int j = 0; j < 24; j++) {
				String h = j+"";
				if(j<10) {
					h = "0"+h;
				}
				
				for (int k = 0; k < min.length; k++) {
					ore.add(h+":"+min[k]);
				}
			}
			
			ComboBox sinfra = (ComboBox) page.lookup("#selAduInfraOra");
			sinfra.getItems().addAll(ore);
			
			ComboBox spubtdg = (ComboBox) page.lookup("#selPubTdgOra");
			spubtdg.getItems().addAll(ore);
			
			DatePicker dpAss1 = (DatePicker)page.lookup("#assemblea1");
			DatePicker dpAss2 = (DatePicker)page.lookup("#assemblea2");
			DatePicker dpCongrDa= (DatePicker)page.lookup("#congressoDa");
			DatePicker dpCongrA= (DatePicker)page.lookup("#congressoA");
			
			/* Verifico se ci sono dati a db */
			JSONArray res = GePrato.getSelectResponse("select giorno, ora from gp_dateorari where tipo='AI'");
			if(res.length()>0) {
				selAduInfra.getSelectionModel().select(res.getJSONObject(0).getString("giorno"));
				sinfra.getSelectionModel().select(res.getJSONObject(0).getString("ora"));
				existAI = true;
			}
			
			res = GePrato.getSelectResponse("select giorno, ora from gp_dateorari where tipo='AP'");
			if(res.length()>0) {
				selPubTdg.getSelectionModel().select(res.getJSONObject(0).getString("giorno"));
				spubtdg.getSelectionModel().select(res.getJSONObject(0).getString("ora"));
				existAP = true;
			}
			
			res = GePrato.getSelectResponse("select data from gp_dateorari where tipo='AS1'");
			if(res.length()>0) {
				String d = res.getJSONObject(0).getString("data");
				dpAss1.setValue(LocalDate.of(Integer.valueOf(d.split("-")[0]),Integer.valueOf(d.split("-")[1]),Integer.valueOf(d.split("-")[2])));
				existAS1 = true;
			}
			
			res = GePrato.getSelectResponse("select data from gp_dateorari where tipo='AS2'");
			if(res.length()>0) {
				String d = res.getJSONObject(0).getString("data");
				dpAss2.setValue(LocalDate.of(Integer.valueOf(d.split("-")[0]),Integer.valueOf(d.split("-")[1]),Integer.valueOf(d.split("-")[2])));
				existAS2 = true;
			}
								
			res = GePrato.getSelectResponse("select data from gp_dateorari where tipo='CDA'");
			if(res.length()>0) {
				String d = res.getJSONObject(0).getString("data");
				dpCongrDa.setValue(LocalDate.of(Integer.valueOf(d.split("-")[0]),Integer.valueOf(d.split("-")[1]),Integer.valueOf(d.split("-")[2])));
				existCDA = true;
			}
			
			res = GePrato.getSelectResponse("select data from gp_dateorari where tipo='CA'");
			if(res.length()>0) {
				String d = res.getJSONObject(0).getString("data");
				dpCongrA.setValue(LocalDate.of(Integer.valueOf(d.split("-")[0]),Integer.valueOf(d.split("-")[1]),Integer.valueOf(d.split("-")[2])));
				existCA = true;
			}
			
			Button bt = (Button)page.lookup("#salva");
			EventHandler<ActionEvent> eh = new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent arg0) {
					String ggInfra 		= (String)selAduInfra.getSelectionModel().getSelectedItem();
					String ggPubTdg 	= (String)selPubTdg.getSelectionModel().getSelectedItem();
					
					String oraInfra 	= (String)sinfra.getSelectionModel().getSelectedItem();
					String oraPubTdg 	= (String)spubtdg.getSelectionModel().getSelectedItem();
					
					LocalDate ass1 = dpAss1.getValue();
					LocalDate ass2 = dpAss2.getValue();
					LocalDate congrDa= dpCongrDa.getValue();
					LocalDate congrA= dpCongrA.getValue();
					
					if((ggInfra!=null && oraInfra !=null && !ggInfra.isEmpty() && !oraInfra.isEmpty()) || existAI ) {
						if(!existAI) {
							String insInfra = "INSERT INTO gp_dateorari(tipo, giorno, ora) VALUES";
							insInfra += "('AI','"+ggInfra+"','"+oraInfra+"')";
							GePrato.getInsertResponse(insInfra);
						}else {
							if(ggInfra==null)ggInfra="";
							if(oraInfra==null)oraInfra="";
							String upInfra = 	"update gp_dateorari "+
												" set giorno = '"+ggInfra+"', "+
												" ora = '"+oraInfra+"' "+
												" where tipo = 'AI'";
							GePrato.getUpdateResponse(upInfra);
						}
					}
					if((ggPubTdg!=null && ggPubTdg !=null && !ggPubTdg.isEmpty() && !ggPubTdg.isEmpty()) || existAP ) {
						if(!existAP) {
							String insPubTdg= "INSERT INTO gp_dateorari(tipo, giorno, ora) VALUES";
							insPubTdg += "('AP','"+ggPubTdg+"','"+oraPubTdg+"')";
							GePrato.getInsertResponse(insPubTdg);
						}else {
							if(ggPubTdg==null)ggPubTdg="";
							if(oraPubTdg==null)oraPubTdg="";
							String upPubTdg = 	"update gp_dateorari "+
									" set giorno = '"+ggPubTdg+"', "+
									" ora = '"+oraPubTdg+"' "+
									" where tipo = 'AP'";
							GePrato.getUpdateResponse(upPubTdg);
						}
					}
					if(ass1!=null || existAS1) {
						if(!existAS1) {
							String insAss1 = "INSERT INTO gp_dateorari(tipo, data) VALUES";
							insAss1 += "('AS1','"+ass1+"')";
							GePrato.getInsertResponse(insAss1);
						}else {
							String upAss1 ="";
							if(ass1==null) {
								upAss1 = 	"delete from  gp_dateorari "+
										" where tipo = 'AS1'";
								GePrato.getDeleteResponse(upAss1);
							}else {
								upAss1 = 	"update gp_dateorari "+
										" set data = '"+ass1+"' "+
										" where tipo = 'AS1'";
								GePrato.getUpdateResponse(upAss1);
							}
							
							
						}
					}
					if(ass2!=null || existAS2) {
						if(!existAS2) {
							String insAss2 = "INSERT INTO gp_dateorari(tipo, data) VALUES";
							insAss2 += "('AS2','"+ass2+"')";
							GePrato.getInsertResponse(insAss2);
						}else {
							String upAss2 ="";
							if(ass2==null) {
								upAss2 = 	"delete from gp_dateorari "+
										" where tipo = 'AS2'";
								GePrato.getDeleteResponse(upAss2);
							}else {
								 upAss2 = 	"update gp_dateorari "+
										" set data = '"+ass2+"' "+
										" where tipo = 'AS2'";
								 GePrato.getUpdateResponse(upAss2);
							}
							
						}
					}
					if(congrDa!=null || existCDA) {
						if(!existCDA) {
							String insCong = "INSERT INTO gp_dateorari(tipo, data) VALUES";
							insCong += "('CDA','"+congrDa+"')";
							GePrato.getInsertResponse(insCong);
						}else {
							String upCong ="";
							if(congrDa==null) {
								upCong = 	"delete from gp_dateorari "+
										" where tipo = 'CDA'";
								GePrato.getDeleteResponse(upCong);
							}else {
								upCong = 	"update gp_dateorari "+
										" set data = '"+congrDa+"' "+
										" where tipo = 'CDA'";
								GePrato.getUpdateResponse(upCong);
							}
							
						}
					}
					
					if(congrA!=null || existCA) {
						if(!existCA) {
							String insCong = "INSERT INTO gp_dateorari(tipo, data) VALUES";
							insCong += "('CA','"+congrA+"')";
							GePrato.getInsertResponse(insCong);
						}else {
							String upCong ="";
							if(congrA==null) {
								upCong = 	"delete from gp_dateorari "+
										" where tipo = 'CA'";
								GePrato.getDeleteResponse(upCong);
							}else {
								upCong = 	"update gp_dateorari "+
										" set data = '"+congrA+"' "+
										" where tipo = 'CA'";
								GePrato.getUpdateResponse(upCong);
							}
							
						}
					}
						
					
					Utils.success("Salvataggio avvenuto");
						
						
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
