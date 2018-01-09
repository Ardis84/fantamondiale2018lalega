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
		ObservableList<Menu> menus = mb.getMenus();
		for (int i = 0; i < menus.size(); i++) {
			Menu mi = menus.get(i);
			String labelMenu = mi.getText();
			ObservableList<MenuItem> itms = mi.getItems();
			for (int j = 0; j < itms.size(); j++) {
				MenuItem itm = itms.get(j);
				String labelItm = itm.getText();
				EventHandler<ActionEvent> eh = new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent event) {
						
						try {
							String className = "b.actions."+labelMenu.toLowerCase().replaceAll(" ", "")+"."+labelItm.toUpperCase().replaceAll(" ", "").substring(0, 1)+labelItm.toLowerCase().replaceAll(" ", "").substring(1, labelItm.length());
							Class<?> c = Class.forName(className);
							Method[] methods = c.getDeclaredMethods();
							for (int k = 0; k < methods.length; k++) {
								Method method = methods[k];
								if(method.getName().equals("view"))
									method.invoke(c.newInstance(), primaryStage);
							}
							
							
						} catch (ClassNotFoundException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
					}
				};
				itm.setOnAction(eh );
			}
		}
		
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
