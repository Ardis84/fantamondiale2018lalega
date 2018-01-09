package b;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Installer extends Application{
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("template/installer.fxml"));
		
		Logs.write(getClass().getResource("template/bacheca1.fxml").getPath());
		
		AnchorPane page = (AnchorPane)loader.load();
		ObservableList<Node> ch = page.getChildren();
	}
	
	

}
