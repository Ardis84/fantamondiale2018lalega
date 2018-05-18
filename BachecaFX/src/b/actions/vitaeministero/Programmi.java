package b.actions.vitaeministero;

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
import b.printfoot.VMRigheElementi;
import b.printfoot.VitaMinisteroDett;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.Window;

public class Programmi {
	
	public static Stage thisStage;
	public static  int totComitiveMese;
	
	public static void view(Stage primaryStage) {
		thisStage = primaryStage;
		int c = 0;
		try {
			FXMLLoader loader = new FXMLLoader(Utils.getResourceUrl("template/vitaministero_programmi.fxml"));
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
					Programmi.show(primaryStage);	
					//Utils.stopLoading(page);
				}
				
			};
			bt.setOnAction(eh);
			
			primaryStage.show();
		}catch (Exception e) {
			System.out.println("Riga "+c+"  :  "+e);
		}
	}
	
	private static void show(Stage stage) {

		ComboBox selmese = (ComboBox)stage.getScene().lookup("#mese");
		ComboBox selanno = (ComboBox)stage.getScene().lookup("#anno");

		int numMese = selmese.getSelectionModel().getSelectedIndex();
		int anno = Integer.valueOf((String)selanno.getSelectionModel().getSelectedItem());
		
		ArrayList<Date> dates = Utils.getAllDatesByMonthAndYear(numMese+1,anno);
		
		/* Recupero tutte le comitive inserite */	
		
		String qry = 	"select giorno, ora " + 
						"from gp_dateorari " + 
						"where tipo = 'AI'";
		JSONArray res = GePrato.getSelectResponse(qry);
		
		totComitiveMese = 0;
		
		ArrayList<VitaMinisteroDett> ar = new ArrayList<>();
		
		/* Ciclo le date del mese e verifico i giorni delle adunanze infrasettimanali */
		
		BackgroundFill f1 = new BackgroundFill(Color.valueOf("acdcf7") , CornerRadii.EMPTY, Insets.EMPTY);
		Background bg1 = new Background(f1);
		BackgroundFill f2 = new BackgroundFill(Color.valueOf("fbd89d") , CornerRadii.EMPTY, Insets.EMPTY);
		Background bg2 = new Background(f2);
		BackgroundFill f3 = new BackgroundFill(Color.valueOf("ff9d83") , CornerRadii.EMPTY, Insets.EMPTY);
		Background bg3 = new Background(f3);
		
		ScrollPane spane = (ScrollPane)stage.getScene().lookup("#scroll");
		
		double w = spane.getWidth()-30;
		
		VBox vb = new VBox();
		vb.setMinWidth(w);
		vb.setMaxWidth(w);
		
		int bgNumber = 1;
		for (int i = 0; i < dates.size(); i++) {
			Date d = dates.get(i);
			String dayName = Utils.getDayNameByDate(d);
			for (int j = 0; j < res.length(); j++) {
				JSONObject obj = res.getJSONObject(j);
				String gg = obj.getString("giorno");
				String ora = obj.getString("ora");
				if(gg.substring(0, 3).toLowerCase().equals(dayName.substring(0, 3).toLowerCase())) {					
					String data = Utils.reverseDateInString(d);
					
					VitaMinisteroDett vmd = getSavedVMDett(gg,data);
																	
					BackgroundFill fills = new BackgroundFill(Color.valueOf("ebf4fb") , CornerRadii.EMPTY, Insets.EMPTY);
					Background bgL = new Background(fills);
					BorderWidths bw = new BorderWidths(0, 0, 2, 0);
					CornerRadii cr = new CornerRadii(3);
					Border border = new Border(new BorderStroke(Color.valueOf("888888"), 
				            BorderStrokeStyle.SOLID, cr, bw));
					
					Insets pad = new Insets(3);
					
					String nomeMese = Utils.getNameMese(Utils.getMonthFromDate(d));
					nomeMese = nomeMese.substring(0, 1).toUpperCase()+""+nomeMese.substring(1,nomeMese.length()).toLowerCase();
					int numGG = Utils.getDayNumberFromDate(d);
			
					VBox vbAdunanza = new VBox();
					//vbAdunanza.setBackground(bgL);
					//vbAdunanza.setBorder(border);
					vbAdunanza.setPadding(pad);
					vbAdunanza.setMaxWidth(w);
					
					//vbAdunanza.getChildren().add(l);

					/*Controllo se esistono già dei dati inseriti per questa adunanza*/
					//vbAdunanza = addAllSavedDetails(vbAdunanza, vmd, anno, numMese);
					
					int h = 50;
					
					/* Riga 0 */
					
					CheckBox cb = new CheckBox();
					cb.setText("Assemblea/Congresso");
					cb.setId("chkAssembleaCongresso");
					
					StackPane sp = new StackPane(cb );
					sp.setAlignment( cb, Pos.CENTER_LEFT );
					sp.setBorder(border);
					vbAdunanza.getChildren().add(sp);					
					
					Label emptyNode = new Label();
					
					/* Riga 1 */
					
					Label l = new Label();
					String formatLabel = dayName.toUpperCase()+" "+numGG+" "+nomeMese+" "+anno;
					l.setText(formatLabel); 
					l.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
					
					/* cantico1 */
					Button addCantico1 = new Button();
					addCantico1.setText("Cantico");
					addCantico1.setId("cantico1");
					addCantico1.setOnAction(openCantico(gg,data,1));
							
					Label pc1 = createResponseLabel("label_cantico1","");
					
					StackPane spc1 = createStackPane(addCantico1,pc1,w/8);
					
					/* Preghiera iniziale */
					Label pi = createResponseLabel("label_pregIniziale", "");
					
					Button addPI = new Button();
					addPI.setText("Preghiera Iniziale");
					addPI.setId("pregIniziale");
					
					StackPane sppi =  createStackPane(addPI,pi,w/4);
					/* ***/
					
					sp = new StackPane(l, spc1, sppi );
					sp.setAlignment( spc1, Pos.CENTER);
					sp.setAlignment( l, Pos.CENTER_LEFT );
					sp.setAlignment( sppi, Pos.CENTER_RIGHT );
					sp.setBorder(border);
					vbAdunanza.getChildren().add(sp);
					
					/* Riga 2 */
					Label cimin = new Label();
					cimin.setText("Min 03");
					cimin.setFont(Font.font("Verdana", FontWeight.BOLD, 10));
					
					Label ci = new Label();
					ci.setText("Commenti Introduttivi ");
					ci.setFont(Font.font("Verdana", FontWeight.BOLD, 10));
					
					Label lPres = createResponseLabel("label_presidente","");
					
					Button addPres = new Button();
					addPres.setText("Presidente");
					addPres.setId("presidente");				
					
					StackPane sppr = createStackPane(addPres,lPres,w/3);
					
					ci.setAlignment(Pos.CENTER_LEFT);
					
					StackPane colum = new StackPane(cimin, ci);
					colum.setAlignment( cimin, Pos.CENTER_LEFT );
					colum.setAlignment( ci, Pos.CENTER_RIGHT );
					colum.setMaxWidth(w/2);
					
					sp = new StackPane(colum, sppr );
					sp.setAlignment( colum, Pos.CENTER_LEFT );
					sp.setAlignment( sppr, Pos.CENTER_RIGHT );
					sp.setBackground(bgL);
					sp.setBorder(border);
					vbAdunanza.getChildren().add(sp);
					
					/* Riga 3 */
					Label titleTesori = new Label();
					titleTesori.setText("TESORI DELLA PAROLA DI DIO");
					titleTesori.setFont(Font.font("Verdana", FontWeight.BOLD, 11));
					
					/* lettura */
					Button addLetturaSettimana = new Button();
					addLetturaSettimana.setText("Capitoli in programma");
					addLetturaSettimana.setId("capitoliLettura");
					
					Label lCapProg = createResponseLabel("label_capitoliLettura","");			
					
					StackPane spcp = createStackPane(addLetturaSettimana,lCapProg,w/4);
									
					sp = new StackPane(titleTesori, spcp );
					sp.setAlignment( titleTesori, Pos.CENTER_LEFT );
					sp.setAlignment( spcp, Pos.CENTER );
					sp.setBackground(bgL);
					sp.setBorder(border);				
					vbAdunanza.getChildren().add(sp);
					
					/* Riga 4 */
//					/* minuti parte */
//					Button addMinParte = new Button();
//					addMinParte.setText("Minuti");
					
					/* Parte */
					Button addParte = new Button();
					addParte.setText("Discorso");
					addParte.setId("discorsoTesori");
					
					Label ldiscorsoTesori = createResponseLabel("label_discorsoTesori","");			
					
					StackPane spdt = createStackPane(addParte,ldiscorsoTesori,w);
					
//					/* Oratore */
//					Button addOratore = new Button();
//					addOratore.setText("Oratore");
//									
					sp = new StackPane(spdt);
					sp.setAlignment( spdt, Pos.CENTER_LEFT );
					sp.setBackground(bgL);
					sp.setBorder(border);				
					vbAdunanza.getChildren().add(sp);
					
					/* Riga 5 */				
					/* Parte */
					Button addScaviamo = new Button();
					addScaviamo.setText("Scaviamo per trovare gemme spirituali");
					addScaviamo.setId("scaviamo");
					
					Label lscaviamo = createResponseLabel("label_scaviamo","");			
					
					StackPane spsca = createStackPane(addScaviamo,lscaviamo,w);
					
					sp = new StackPane(spsca);
					sp.setAlignment( spsca, Pos.CENTER_LEFT );
					sp.setBackground(bgL);
					sp.setBorder(border);				
					vbAdunanza.getChildren().add(sp);

					/* Riga 6 */				
					/* Parte */
					Button lettura = new Button();
					lettura.setText("Lettura Biblica");
					lettura.setId("lettura");
					
					Label llettura = createResponseLabel("label_lettura","");			
					
					StackPane splet = createStackPane(lettura,llettura,w);
				
					sp = new StackPane(splet);
					sp.setAlignment( splet, Pos.CENTER_LEFT );
					sp.setBackground(bgL);
					sp.setBorder(border);				
					vbAdunanza.getChildren().add(sp);
					
					/* Riga 7 */
					Label efficaci = new Label();
					efficaci.setText("EFFICACI NEL MINISTERO");
					efficaci.setFont(Font.font("Verdana", FontWeight.BOLD, 11));
					
					/* sala1 */
					Label sala1 = new Label();
					sala1.setText("Sala Principale");
					sala1.setFont(Font.font("Verdana", FontWeight.BOLD, 11));
					
					/* sala2 */
					Label sala2 = new Label();
					sala2.setText("Classe suplementare");
					sala2.setFont(Font.font("Verdana", FontWeight.BOLD, 11));
									
					sp = new StackPane(efficaci, sala1, sala2);
					sp.setAlignment( efficaci, Pos.CENTER_LEFT );
					sp.setAlignment( sala1, Pos.CENTER );
					sp.setAlignment( sala2, Pos.CENTER_RIGHT );
					sp.setBackground(bgL);
					sp.setBorder(border);				
					vbAdunanza.getChildren().add(sp);				
					
					/* Riga 8 */
					Button disc = new Button();
					disc.setText("Esercitazione1");
					disc.setId("eserc1");
					
					/* sala 1*/
					Label leserc1_sala1 = createResponseLabel("label_eserc1_sala1","");	
					/* sala 2*/
					Label leserc1_sala2 = createResponseLabel("label_eserc1_sala2","");	
					
					sp = new StackPane(disc,leserc1_sala1,leserc1_sala2);
					sp.setAlignment( disc, Pos.CENTER_LEFT );
					sp.setAlignment( leserc1_sala1, Pos.CENTER );
					sp.setAlignment( leserc1_sala2, Pos.CENTER_RIGHT );
					sp.setBackground(bgL);
					sp.setBorder(border);				
					vbAdunanza.getChildren().add(sp);
					
					/* Riga 9 */
					disc = new Button();
					disc.setText("Esercitazione2");
					disc.setId("eserc2");
				
					/* sala 1*/
					Label leserc2_sala1 = createResponseLabel("label_eserc2_sala1","");	
					/* sala 2*/
					Label leserc2_sala2 = createResponseLabel("label_eserc2_sala2","");	
					
					sp = new StackPane(disc,leserc2_sala1,leserc2_sala2);
					sp.setAlignment( disc, Pos.CENTER_LEFT );
					sp.setAlignment( leserc2_sala1, Pos.CENTER );
					sp.setAlignment( leserc2_sala2, Pos.CENTER_RIGHT );
					sp.setBackground(bgL);
					sp.setBorder(border);				
					vbAdunanza.getChildren().add(sp);
					
					/* Riga 10 */
					disc = new Button();
					disc.setText("Esercitazione3");
					disc.setId("eserc3");
				
					/* sala 1*/
					Label leserc3_sala1 = createResponseLabel("label_eserc3_sala1","");	
					/* sala 2*/
					Label leserc3_sala2 = createResponseLabel("label_eserc3_sala2","");	
					
					sp = new StackPane(disc,leserc3_sala1,leserc3_sala2);
					sp.setAlignment( disc, Pos.CENTER_LEFT );
					sp.setAlignment( leserc3_sala1, Pos.CENTER );
					sp.setAlignment( leserc3_sala2, Pos.CENTER_RIGHT );
					sp.setBackground(bgL);
					sp.setBorder(border);				
					vbAdunanza.getChildren().add(sp);
					
					/* Riga 11 */
					Label vitac = new Label();
					vitac.setText("VITA CRISTIANA");
					vitac.setFont(Font.font("Verdana", FontWeight.BOLD, 11));
					
					/* lettura */
					Button addCantico2 = new Button();
					addCantico2.setText("Cantico");
					addCantico2.setId("cantico2");
					addCantico2.setOnAction(openCantico(gg,data,2));
					
					Label pc2 = createResponseLabel("label_cantico2","");
					
					StackPane spc2 = createStackPane(addCantico2,pc2,w/4);
									
					sp = new StackPane(vitac, spc2 );
					sp.setAlignment( vitac, Pos.CENTER_LEFT );
					sp.setAlignment( addCantico2, Pos.CENTER );
					sp.setBackground(bgL);
					sp.setBorder(border);				
					vbAdunanza.getChildren().add(sp);
					
					/* Riga 12 */
					Button addParti = new Button();
					addParti.setText("Parti");
					addParti.setId("partiVitaC");
				
					sp = new StackPane(addParti);
					sp.setAlignment( addParti, Pos.CENTER_LEFT );
					sp.setBackground(bgL);
					sp.setBorder(border);				
					vbAdunanza.getChildren().add(sp);
					

					/* Riga 12 bis */
					
					TextArea taParti = new TextArea();
					taParti.setId("txt_parti");
					taParti.setMaxWidth(w);
					taParti.setMinHeight(15);
					taParti.setEditable(false);
					taParti.setText("VUOTO");
					
					sp = new StackPane(taParti);
					sp.setAlignment( taParti, Pos.CENTER_LEFT );
					sp.setBackground(bgL);
					sp.setBorder(border);				
					vbAdunanza.getChildren().add(sp);
					
					/* Riga 13 */
					Button addCantico3 = new Button();
					addCantico3.setText("Cantico");
					addCantico3.setId("cantico3");
					addCantico3.setOnAction(openCantico(gg,data,3));
					
					Label pc3 = createResponseLabel("label_cantico3","");
					
					StackPane spc3 = createStackPane(addCantico3,pc3,w/4);
				
					Label pc = createResponseLabel("label_pregConclusiva", "");
					
					Button addPC = new Button();
					addPC.setText("Preghiera Conclusiva");
					addPC.setId("pregConclusiva");
					
					StackPane sppc = new StackPane(addPC ,pc);
					sppc.setAlignment( addPC, Pos.CENTER_LEFT);
					sppc.setMaxWidth(w/3);
					
					emptyNode.setMinWidth(w/3);
					
					sp = new StackPane(spc3, sppc, emptyNode);
					sp.setAlignment( addCantico3, Pos.CENTER );
					sp.setAlignment( sppc, Pos.CENTER_RIGHT );
					sp.setAlignment( emptyNode, Pos.CENTER_LEFT );
					sp.setBackground(bgL);
					sp.setBorder(border);				
					vbAdunanza.getChildren().add(sp);
					
					
					if(bgNumber==1) {
						bgNumber++;
						vbAdunanza.setBackground(bg1);
					}else if(bgNumber == 2) {
						bgNumber++;
						vbAdunanza.setBackground(bg2);
					}else {
						bgNumber = 1;
						vbAdunanza.setBackground(bg3);
					}
										
					vb.getChildren().add(vbAdunanza);
					vb.setMargin(vbAdunanza, pad);

				}				
			}	
			
			
		}		

		spane.setContent(vb);
		
		
		
	}


	private static Label createResponseLabel(String id, String text) {
		
		BackgroundFill fills = new BackgroundFill(Color.valueOf("E8ECEC") , CornerRadii.EMPTY, Insets.EMPTY);
		Background bgL = new Background(fills);
		
		if(text==null || text.isEmpty())
			text = "Vuoto";
		
		Label l = new Label();
		l.setText(text);
		l.setId(id);
		l.setFont(Font.font("Verdana", FontWeight.EXTRA_LIGHT, 13));
		l.setTextFill(Color.RED);
		//l.setBackground(bgL);
		l.setMinWidth(20);
		return l;
	}

	private static StackPane createStackPane(Node node1, Node node2, double w) {
		StackPane sp = new StackPane(node1, node2 );
		sp.setAlignment( node1, Pos.CENTER_LEFT);
		sp.setAlignment( node2, Pos.CENTER_RIGHT );
		sp.setMaxWidth(w);
		return sp;
		
	}

	private static EventHandler<ActionEvent> openCantico(String gg, String data, int numero) {
		EventHandler<ActionEvent> eh = new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				try {
					Stage st = new Stage();
					FXMLLoader loader = new FXMLLoader(Utils.getResourceUrl("template/add_cantico.fxml"));
					AnchorPane page = (AnchorPane)loader.load();
					
					Scene scene = new Scene(page);
					st.setTitle("Cantico "+numero+" del "+data);
					st.setScene(scene);
					
					loader.getResources();
					
					Label titolo = (Label)page.lookup("#titolo");
					TextField cantico = (TextField)page.lookup("#cantico");
					Button salva = (Button) page.lookup("#salva");
					salva.setOnAction(saveCantico(gg,data,numero,st));
					
					titolo.setText("Numero");
					
					st.show();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}

			
		};		
		
		return eh;
	}

	private static EventHandler<ActionEvent> saveCantico(String gg, String data, int numero, Stage st) {
		EventHandler<ActionEvent> eh = new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				try {
					TextField cantico = (TextField)st.getScene().lookup("#cantico");
					String c = cantico.getText();
					if(!c.isEmpty()) {
						String existsVMD = checkIfExistsVMDett(gg,data);
						if(existsVMD!=null) {
							String q = "update gp_vitaministerodett "
									+ " set cantico"+numero+"='"+c+"' "
									+ " where id = "+existsVMD;
							GePrato.getUpdateResponse(q);
						}else {
							String existsVM = checkIfExistsVM(gg,data);
							if(existsVM==null) {
								int mese = Utils.getMonthFromDate(Utils.reverseStringInDate(data, "dd/mm/yyyy"));
								int anno = Utils.getYearFromDate(Utils.reverseStringInDate(data, "dd/mm/yyyy"));
								String strMese = Utils.getNameMese(mese);
								String q1 = "insert into gp_vitaministero "
										+ "(mese, anno) "
										+ " values ('"+strMese+"',"+anno+")";
								GePrato.getInsertResponse(q1);
								existsVM = checkIfExistsVM(gg,data);
							}
							String q2 = "insert into gp_vitaministerodett "
									+ " (data, giorno, cantico"+numero+", idvm) "
									+ " values (STR_TO_DATE('"+data+"', '%d/%m/%Y'),'"+gg+"','"+c+"',"+existsVM+")";
							GePrato.getInsertResponse(q2);
						}
						
						st.close();
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
			
			}
	
		
		};		
	
		return eh;
	}
	
	private static String checkIfExistsVMDett(String gg, String data) {
		String id = null;
		String q1 = "select vd.id from gp_vitaministerodett vd "
				+ "where vd.data=STR_TO_DATE('"+data+"', '%d/%m/%Y') and vd.giorno = '"+gg+"'";
		JSONArray rs1 = GePrato.getSelectResponse(q1);
		if(rs1.length()>0) {
			id = rs1.getJSONObject(0).getString("id");
		}
		return id;
	}
	
	private static String checkIfExistsVM(String gg, String data) {
		int mese = Utils.getMonthFromDate(Utils.reverseStringInDate(data, "dd/mm/yyyy"));
		int anno = Utils.getYearFromDate(Utils.reverseStringInDate(data, "dd/mm/yyyy"));
		String strMese = Utils.getNameMese(mese);
		String id = null;
		String q1 = "select vm.id from gp_vitaministero vm where vm.mese='"+strMese+"' and anno = "+anno+"";
		JSONArray rs1 = GePrato.getSelectResponse(q1);
		if(rs1.length()>0) {
			id = rs1.getJSONObject(0).getString("id");
		}
		return id;
	}
	
	private static VitaMinisteroDett getSavedVMDett(String gg, String data) {
		VitaMinisteroDett vmd = null;
		Date d = Utils.reverseStringInDate(data, "dd/mm/yyyy");
		int mese = Utils.getMonthFromDate(d);
		int anno = Utils.getYearFromDate(d);
		String strMese = Utils.getNameMese(mese);
		//Innanzitutto controllo che esista nella tabella vitaministero
		String q1 = "select * from gp_vitaministero vm where vm.mese='"+mese+"' and anno = "+anno+"";
		JSONArray rs1 = GePrato.getSelectResponse(q1);
		if(rs1.length()>0) {
			JSONObject obj = rs1.getJSONObject(0);
			int idvm = obj.getInt("id");
			String q2 = "select * from gp_vitaministerodett vmd vmd.idvm = "+idvm+" and giorno = '"+gg+"'"
					+ " and data =  STR_TO_DATE('"+data+"', '%d/%m/%Y')";
			JSONArray rs2 = GePrato.getSelectResponse(q1);
			if(rs2.length()>0){
				JSONObject obj1 = rs2.getJSONObject(0);
				int idvmd = obj1.getInt("id");
				vmd = new VitaMinisteroDett();
				vmd.setCantico1(obj1.getString("cantico1"));
				vmd.setCantico2(obj1.getString("cantico2"));
				vmd.setCantico3(obj1.getString("cantico3"));
				//vmd.set(obj1.getString("cantico1"));
				/** TODO */
			}
		}
		return vmd;
	}

	private static VBox addAllSavedDetails(VBox vbAdunanza, VitaMinisteroDett vmd, int anno, int numMese) {
		
		String mese = Utils.getNameMese(numMese);
		
		String q = "select * from gp_vitaministero vm where vm.mese='"+mese+"' and anno = "+anno+"";
		JSONArray rs = GePrato.getSelectResponse(q);
		if(rs.length()>0) {
			JSONObject obj = rs.getJSONObject(0);
			int idvm = obj.getInt("id");
			String q1 = "select * from gp_vitaministerodett vmd vmd.idvm = "+idvm;
			JSONArray rs1 = GePrato.getSelectResponse(q1);
			for (int i = 0; i < rs1.length(); i++) {
				JSONObject obj1 = rs1.getJSONObject(i);
				/** TODO **/
			}
			
		}else {
			VBox v = new VBox();
			
			BackgroundFill fills = new BackgroundFill(Color.valueOf("ffffff") , CornerRadii.EMPTY, Insets.EMPTY);
			Background bgL = new Background(fills);
			
			
			
			v.setBackground(bgL);
			
			vbAdunanza.getChildren().add(v);
		}
		
		return vbAdunanza;
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
	
	
	
	
	
	
}
