package b;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;

import org.json.JSONArray;

import javafx.application.Application;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application {

	boolean reinstall = false;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		/* Controllo se esiste già una configurazione per questo pc procedo all'apertura se no avvio installazione */
		boolean folderSetAndExists = false;
		JSONArray res = GePrato.getSelectResponse("select * from gp_installazioni where pcname='"+Utils.getPcName()+"'");
		if(res.length()>0) {
			String pathInst = res.getJSONObject(0).getString("path");
			File folderInst = new File(pathInst);
			if(folderInst.isDirectory())
				folderSetAndExists = true;
			else
				reinstall = true;
		}
		
		if(folderSetAndExists) {
		
			FXMLLoader loader = new FXMLLoader(getClass().getResource("template/bacheca1.fxml"));
			
			Logs.write(getClass().getResource("template/bacheca1.fxml").getPath());
		
			
			AnchorPane page = (AnchorPane)loader.load();
			ObservableList<Node> ch = page.getChildren();
			
			MenuBar mb = (MenuBar)ch.get(0);
			MenuPrincipal mp = new MenuPrincipal(mb, primaryStage);
		
			Scene scene = new Scene(page);
			primaryStage.setTitle("Bacheca");
			primaryStage.setScene(scene);
			primaryStage.show();
			
		}else {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("template/installazione.fxml"));
			AnchorPane page = (AnchorPane)loader.load();
			
			Button cartella = (Button)page.lookup("#cartella");
			Button avanti 	= (Button)page.lookup("#avanti");
		
			EventHandler<ActionEvent> ehC = new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent arg0) {
					String folder = Utils.openFolder();		
					TextField url = (TextField)page.lookup("#url");
					url.setText(folder);
				}

				
			}; ;
			cartella.setOnAction(ehC);
			
			EventHandler<ActionEvent> ehA = new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent arg0) {
					FXMLLoader l1 = new FXMLLoader(getClass().getResource("template/installazione2.fxml"));
					try {
						AnchorPane p1 = (AnchorPane)l1.load();

						TextField url = (TextField)page.lookup("#url");
						String folder = url.getText();
						folder = folder.replace("\\", "\\\\")+"/BachecaFX";
						if(!reinstall) {
							String ins = 	"insert into gp_installazioni(pcname, path) "+
											"values('"+Utils.getPcName()+"','"+folder+"')";
							GePrato.getInsertResponse(ins );
						}else {
							String mod = 	"update gp_installazioni "+
									"set path= '"+folder+"' "+
									"where pcname = '"+Utils.getPcName()+"'";
							GePrato.getUpdateResponse(mod);
						}
						installa(folder);
						
//						Scene scene = new Scene(page);
//						primaryStage.setTitle("Installazione BachecaFX");
//						primaryStage.setScene(scene);
//						primaryStage.show();
//						
						start(primaryStage);
						
					} catch ( Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}				
				}

				
			};;
			avanti.setOnAction(ehA);
			
			Scene scene = new Scene(page);
			primaryStage.setTitle("Installazione BachecaFX");
			primaryStage.setScene(scene);
			primaryStage.show();
			//Utils.openFile();
		}
		
	}
	
	
	protected void installa(String folder) {
		boolean success = (new File(folder)).mkdirs();
		if (!success) {
		    // Directory creation failed
		}else {
			getClass().getResourceAsStream("reports");
		}
		
	}

	@FXML
	private void viewProclamatori(ActionEvent event) {
			
		}
	@FXML
	private void viewGruppi(ActionEvent event) {
		
	}
	@FXML
	private void viewTestPubbl(ActionEvent event) {
		
	}
	@FXML
	private void viewComitive(ActionEvent event) {
		
	}
	@FXML
	private void viewReparti(ActionEvent event) {
		
	}

}
