package b;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import org.json.JSONArray;

public class GePrato {

	public static  JSONArray getSelectResponse(String qry) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("qselect", trimQry(qry) );
		return getResponse(map);
	}
	
	public static  JSONArray getUpdateResponse(String qry) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("qupdate", trimQry(qry) );
		return getResponse(map);
	}
	
	public static  JSONArray getInsertResponse(String qry) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("qinsert", trimQry(qry) );
		return getResponse(map);
	}
	
	public static JSONArray getDeleteResponse(String qry) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("qdelete", trimQry(qry) );
		return getResponse(map);
	}
	
	public static JSONArray getResponse() {
		return getResponse(new HashMap<String, String>());
	}
	
	public static JSONArray getResponse(HashMap<String, String> params) {
		String url = "http://geprato.altervista.org/_bacheca/consulting.php?panel=request";
		if(params!=null && !params.isEmpty()) {
			Object[] keys = params.keySet().toArray();
			for (int i = 0; i < params.size(); i++) {
				Object k = keys[i];
				String v = params.get(k);
				url += "&"+k+"="+v;
			}
		}
		String res = resolveUrl(url);	
		JSONArray jsonObj = new JSONArray(res);
		return jsonObj;
	}
	
	 private static String resolveUrl(String site){
	        StringBuilder sb=null;
	        BufferedReader reader=null;
	        String serverResponse=null;
	        try{
	             /* Passo i parametri di get all'url */
	            HttpClient httpClient = new DefaultHttpClient();
	            HttpContext localContext = new BasicHttpContext();
	            HttpGet httpGet = new HttpGet(site);
	            HttpResponse response = httpClient.execute(httpGet, localContext);
	            HttpEntity et = response.getEntity();
	            InputStream content = et.getContent();

	            sb = new StringBuilder();
	            reader = new BufferedReader(new InputStreamReader(content));
	            String line;
	            while ((line = reader.readLine()) != null) {
	                sb.append(line + "\n");
	            }

	            if (sb!=null)
	                serverResponse=sb.toString();
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            if (reader != null) {
	                try {
	                    reader.close();
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	        return serverResponse;
	    }
	 
		private static String trimQry(String qry) {
			return qry.replaceAll(" ", "%20");
		}
		
		private static String[] getFileOrderInSelect(String query, Object[] keys) {			
			String from = query.split("from")[1];
			
			query = query.replace("select", "");
			query = query.split("from")[0];
			
			String[] order = null;
			if(query.contains(" as ")) {
				int cOrder = 0;
				String newOrder = "";
				String[] arFields = query.split(",");
				for (int i = 0; i < arFields.length; i++) {
					String f = arFields[i];
					if(f.split(" as ").length>1)
						f = f.split(" as ")[1];
					else
						f = f.split(" as ")[0];
					
					if(!f.contains("(") || !f.contains(")") || !f.contains("concat") || !f.contains("CONCAT") || !f.trim().isEmpty()) {
						for (int j = 0; j < keys.length; j++) {
							if(f.contains(keys[j]+"") && !newOrder.contains(keys[j]+",")) {
								newOrder += keys[j]+"";
								if(cOrder<keys.length)
									newOrder += ",";
								j = keys.length;
							}
						}
					}
				}
				
				order = newOrder.split(",");
			}else {
				order = query.replaceAll(" ", "").trim().split(",");
			}
						
			return order;
		}
		
		public static String[] getAllFieldInTable(String tableName) {
			String qry = 	"select COLUMN_NAME, DATA_TYPE "+
							"from information_schema.columns "+
							"where table_schema = 'my_geprato' ";
			
			if(tableName!=null && !tableName.isEmpty())
				qry 	+=	"and table_name='"+tableName.replaceAll(" ","").toLowerCase()+"' ";
			
			qry 		+=	"order by table_name,ordinal_position";
			JSONArray res = getSelectResponse(qry);
			String[] lst = new String[res.length()];
			for (int i = 0; i < res.length(); i++) {
				JSONObject obj = res.getJSONObject(i);
				String name = obj.getString("COLUMN_NAME");
				lst[i] = name;
			}
			return lst;
		}

	/*	public static JScrollPane createGrid(String query) {
			JSONArray res = getSelectResponse(query);	
			Object[] keys = res.getJSONObject(0).keySet().toArray();
			String[] order = getFileOrderInSelect(query, keys);
			return createGrid(res, order, null);
		}
		
		public static JScrollPane createGrid(String query,  ModelPopMenuItem[] lstItems) {
			JSONArray res = getSelectResponse(query);	
			Object[] keys = res.getJSONObject(0).keySet().toArray();
			String[] order = getFileOrderInSelect(query, keys);
			return createGrid(res, order, lstItems);
		}

		private static JScrollPane createGrid(JSONArray res, String[] order, ModelPopMenuItem[] lstItems ) {		
			Object[] keys = order;
			Object[][] datas = new Object[res.length()][];
			
			for (int i = 0; i < res.length(); i++) {
				JSONObject obj = res.getJSONObject(i);
				if(keys == null)
					keys = obj.keySet().toArray();
				Object[] s = obj.keySet().toArray();
				for (int j = 0; j < keys.length; j++) {
					Object k = (keys[j]);
					s[j] = obj.get(k.toString())!=null?obj.get(k.toString()):"";
				}
				datas[i] = s;
			}
			
//			if(datas.length>0 && keys.length>0) {
//				JTable t = new JTable(datas, keys);
//				JScrollPane sp = new JScrollPane(t, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//				return sp;
//			}else {
	        
	        
			
				final JTable t = new JTable(datas, keys);
				
				Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
				t.setCursor(cursor );
		        
				/* PopUp Menu 
				
				final JPopupMenu popupMenu = new JPopupMenu();
				
				if(lstItems!=null) {
					for (int i = 0; i < lstItems.length; i++) {
						ModelPopMenuItem itm = lstItems[i];
						popupMenu.add( createMenuOption(t, itm.getNome(), itm.getClasse(), itm.getMetodo()));
					}
				}
				
				/* ** 
				
				t.setComponentPopupMenu(popupMenu);
				
				//Cambio colore al passaggio del mouse
				t.addMouseMotionListener( new MouseMotionAdapter()
				{
				   public void mouseMoved(MouseEvent e)
				   {
				      int row = t.rowAtPoint(e.getPoint());
				      if (row > -1)
				      {
				         // easiest way:
				         t.clearSelection();
				         t.setRowSelectionInterval(row, row);
				      }
				      else
				      {
				         t.setSelectionBackground(Color.blue);
				      }
				   }
				});
				
				
				JScrollPane sp = new JScrollPane(t, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				return sp;
//			}
		}
		*/
		private static JMenuItem createMenuOption(final JTable t, String name, final String classe, final String metodo) {
			JMenuItem item = new JMenuItem(name);
	        item.addActionListener(new ActionListener() {

	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	
	            	int c = t.getSelectedColumn();
	            	int r = t.getSelectedRow();
	            	
	            	
	            	try {
						Class<?> cls = Class.forName(classe);
						Object inst = cls.newInstance();
						Method m = cls.getMethod(metodo);
						
						
						
						m.invoke(inst);
					} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
						e1.printStackTrace();
					}
	            	
	            /*	int c = t.getSelectedColumn();
	            	int r = t.getSelectedRow();
	            	//Nome
	            	Object nome = t.getModel().getValueAt(r,0);
	            	//Cognome
	            	Object cognome = t.getModel().getValueAt(r,1);
	            	//Data Nascita
	            	Object datanascita = t.getModel().getValueAt(r,2);
	            	*/
	            	/*  */
	            	
	                //JOptionPane.showMessageDialog(frame, "Right-click performed on table and choose DELETE");
	            }
	        });

			return item;
		}

		public static JComboBox<String> createSelect(String query,String label, String nome) {
			JSONArray res = getSelectResponse(query);	
			
			JComboBox<String> jcombo = new JComboBox<String>();
			
			for (int i = 0; i < res.length(); i++) {
				JSONObject obj = res.getJSONObject(i);
				String id="";
				String name = "";
				if(label.split(",").length>1) {
					
					String[] lbs = label.split(",");
					for (int j = 0; j < lbs.length; j++) {
						if(!lbs[j].toLowerCase().equals("id"))
							name += obj.get(lbs[j])+" ";
						else
							id ="  (id="+ obj.get(lbs[j])+") ";
					}
				}else {
					name += obj.get(label)+" ";
				}
				jcombo.addItem(name+id);
			}			
			
			jcombo.setName(nome);
			jcombo.setBackground(Color.white);
			jcombo.setCursor(Cursor.getDefaultCursor());
			
			return jcombo;
		}

		
		
}

