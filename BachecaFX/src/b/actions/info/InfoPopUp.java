package b.actions.info;

import b.MenuPrincipal;
import b.Utils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class InfoPopUp {

	
	public static void view(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(Utils.getResourceUrl("template/info.fxml"));
			AnchorPane page = (AnchorPane)loader.load();

			MenuBar mb = (MenuBar)page.lookup("#menuPrincipal");
			MenuPrincipal mp = new MenuPrincipal(mb, primaryStage);
			
			Scene scene2 = new Scene(page);
			Stage newStage = new Stage();
		     newStage.setScene(scene2);
		     //tell stage it is meannt to pop-up (Modal)
		     newStage.initModality(Modality.APPLICATION_MODAL);
		     newStage.setTitle("?");
		     newStage.show();
			
			
		}catch (Exception e) {
			System.out.println(e);
		}
	}
	
}
