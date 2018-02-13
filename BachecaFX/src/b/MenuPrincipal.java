package b;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

public class MenuPrincipal {

	
	public MenuPrincipal(MenuBar mb, Stage stage) {
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
							String className = "b.actions."+labelMenu.toLowerCase().replaceAll(" ", "")+"."+labelItm.toUpperCase().replaceAll(" ", "").substring(0, 1)+labelItm.toLowerCase().replaceAll(" ", "").substring(1, labelItm.replaceAll(" ", "").length());
							
							if(labelMenu.equals("?"))
								className = "b.actions.info.InfoPopUp";
							
							Class<?> c = Class.forName(className);
							Method[] methods = c.getDeclaredMethods();
							for (int k = 0; k < methods.length; k++) {
								Method method = methods[k];
								if(method.getName().equals("view"))
									method.invoke(c.newInstance(), stage);
							}							
							
						} catch (ClassNotFoundException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
							e.printStackTrace();
						}
												
					}
				};
				itm.setOnAction(eh );
			}
		}
	}
	
	
}
