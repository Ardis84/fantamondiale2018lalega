package b;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;

import javafx.application.Application;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
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
