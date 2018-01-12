package b;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import b.printfoot.ComitiveElenco;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class NodeUtils {

	/**********/
	/** Menu sulla griglia al tasto destro del mouse **/
	
	/** GUIDA 
	 * 
	 * Prima si definisce il context menu aggiungendo ogni voce del menu e il relativo metodo di actio
	 * Ovviamente si deve definire anche lo stage nel quale deve essere eventualmente aperta la nuova finestra
	 * dopo di che si può richiamare il metodo della creazione vera e propria
	 * 
	 * */
	
	public static class ActionsMenus{
		public  ArrayList<HashMap<String, String>> actions = new ArrayList<>();
		public String name; 
		public String urlClass;
		public String method;
		
		public  void addAction(String name, String urlClass, String method) {
			HashMap<String, String> el = new HashMap<>();
			el.put("name", name);
			el.put("urlClass", urlClass);
			el.put("method", method);
			actions.add(el);
		}
		
		public  ArrayList<HashMap<String, String>> getActions() {
			return actions;
		}
	}
	
	public static class ContextMenus{
		public  ContextMenu cm = new ContextMenu();
		public  ArrayList<HashMap<String, String>> actions = new ArrayList<>();
		public  Stage stage = null;
		
		public  void setActions(String name, String urlClass, String method) {
			ActionsMenus am = new ActionsMenus();
			am.addAction(name, urlClass, method);
			actions = am.getActions();
		}
		
		public  void setActions(ArrayList<HashMap<String, String>> a){
			actions = a;
		}		
		
		public  void setStage(Stage stg) {
			stage = stg;
		}
		
		public  ContextMenu createContextMenu() {
			if(!actions.isEmpty() && stage!=null) {
				for (int i = 0; i < actions.size(); i++) {
					HashMap<String, String> act = actions.get(i);
					String name = (String)act.get("name");
					MenuItem mi = new MenuItem(name);
					mi.setId(name.toLowerCase());
					
					
					EventHandler<ActionEvent> eh = new EventHandler<ActionEvent>() {					
						@Override
						public void handle(ActionEvent event) {
							String className = act.get("urlClass");
							String method = act.get("method");
							try {
								int find = 0;
								Class<?> c = Class.forName(className);
								Method[] methods = c.getDeclaredMethods();
								for (int k = 0; k < methods.length; k++) {
									Method m = methods[k];
									if(m.getName().equals(method)) {
										m.invoke(c.newInstance(), stage);
										find = 1;
									}
								}
								
								if(find==0)
									System.out.println("NodeUtils.ContextMenus.createContextMenu - Metodo non trovato: "+className+"."+method);
							} catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
								
							
						}
					};
					
					mi.setOnAction(eh);
					
					cm.getItems().add(mi);
				}
			}else {
				cm = null;
				System.out.println("NodeUtils.ContextMenus.createContextMenu: Non è stato definito correttamente il context menu");
			}
			return cm;
		}
		
	}
	
	public static TableView addContextMenuToTableView(TableView tb, ContextMenu cm) {	
		if(cm!=null) {	
					
			tb.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
	
			    @Override
			    public void handle(MouseEvent t) {
			    	cm.hide();
			        if(t.getButton() == MouseButton.SECONDARY && tb.getSelectionModel().getSelectedItem()!=null) {
			            cm.show(tb, t.getScreenX(), t.getScreenY());
			            tb.getSelectionModel().clearSelection();
			        }
			    }
			});
		}
		return tb;
	}
	
	
	public static TableView addContextMenuToTableView(TableView tb, ArrayList<HashMap<String, String>> actions, Stage stage) {	
		
		ContextMenus cms = new ContextMenus();
		
		cms.setActions(actions);
		cms.setStage(stage);
		ContextMenu cm = cms.createContextMenu();
		return addContextMenuToTableView(tb,cm);
		
	}
	
	public static TableView addContextMenuToTableView(TableView tb, ContextMenus cms) {	
		ContextMenu cm = cms.createContextMenu();
		return addContextMenuToTableView(tb,cm);
		
	}
	/**********/
}
