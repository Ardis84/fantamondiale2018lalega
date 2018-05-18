package b.importazioni;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Color;
import org.apache.poi.ss.usermodel.PatternFormatting;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.json.JSONArray;
import org.json.JSONObject;

import b.GePrato;
import b.Utils;
import b.importazioni.weburl.WebUrl;
import b.printfoot.VitaMinisteroDett;

public class ImportaVM {

	public static void main(String[] args) {
		String path = ImportaGruppi.class.getResource("").getPath();
		importVMFromXls(path+"/files/original_vm_giugno.xls");
	}

	private static void importVMFromXls(String filepath) {
		FileInputStream inputStream;
		try {
			inputStream = new FileInputStream(new File(filepath));
			BufferedInputStream bis = new BufferedInputStream(inputStream);
			//inputStream = new FileInputStream(new File(path+"/files/vm_giugno.xls"));
			Workbook workbook = WorkbookFactory.create(bis);
			
			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.rowIterator();
			
			ArrayList<ModelVM> models = new ArrayList<ModelVM>();
			
			int cR = 0;
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				Iterator<Cell> celIterator = row.cellIterator();
				if(cR>4) {
					ModelVM mvm = new ModelVM();
					while (celIterator.hasNext()) {
						Cell cl = celIterator.next();
						CellType tp = cl.getCellTypeEnum();
						int idx = cl.getColumnIndex();
						
						Object valore = null;
						
						if(CellType.STRING == tp) {
							String val = cl.getStringCellValue();
							valore = val;
						}else if(CellType.NUMERIC==tp) {
							double val = cl.getNumericCellValue();
							valore = val;
						}
						
					 CellStyle cellStyle = cl.getCellStyle();
					 
					 Color cc = cellStyle.getFillBackgroundColorColor();
					String hsColor = ((HSSFColor)cc).getHexString();

					//C6C6:D9D9:F1F1 = Assemblea      FFFF:9999:0 = Visita Sorvegliante     9999:3333:0 = Classe supplementare sospesa
						if(valore!=null && !hsColor.equals("C6C6:D9D9:F1F1") && !hsColor.equals("FFFF:9999:0") && !hsColor.equals("9999:3333:0")) {
							String v = valore.toString();
							/* DATA */
							if(idx==0) {
								String d = Utils.reverseDateInString(cl.getDateCellValue());
								mvm.setDATA(d);
							}
							
							/* PRES 1 */
							if(idx == 2) {
								mvm.setPRESIDENTE1(v);
								//System.out.println(hsColor+" - "+v);
							}
							
							/* Tesori 1 */
							if(idx == 4) {
								mvm.setTESORI1(v);
								
							}
							
							/* Tesori 2 */
							if(idx == 5) {
								mvm.setTESORI2(v);
								
							}
							
							/* Tesori 3 A */
							if(idx == 7) {
								mvm.setTESORI3_A(v);
								
							}
							
							/* 1 Contatto A */
							if(idx == 10) {
								mvm.setEFFICACI_A_PC(v);
								
							}else if(idx == 12) {
								//Ass
								mvm.setEFFICACI_A_PC_ASS(v);
								
							}
							
							/* 1 Vis Ult A */
							if(idx == 14) {
								mvm.setEFFICACI_A_PVU(v);
								
							}else if(idx == 16) {
								//Ass
								mvm.setEFFICACI_A_PVU_ASS(v);
								
							}
							
							/* 2 Vis Ult A */
							if(idx == 18) {
								mvm.setEFFICACI_A_SVU(v);
								
							}else if(idx == 20) {
								//Ass
								mvm.setEFFICACI_A_SVU_ASS(v);
								
							}
							
							/* 3 Vis Ult A */
							if(idx == 22) {
								mvm.setEFFICACI_A_TVU(v);
								
							}else if(idx == 24) {
								//Ass
								mvm.setEFFICACI_A_TVU_ASS(v);
								
							}
							
							/* Studio Bibblico A */
							if(idx == 26) {
								mvm.setEFFICACI_A_SB(v);
								
							}else if(idx == 28) {
								//Ass
								mvm.setEFFICACI_A_SB_ASS(v);
								
							}
							
							/* Discorso A */
							if(idx == 30) {
								mvm.setEFFICACI_A_D(v);
								
							}
							
							/**********/
							
							/* PRES 2 */
							if(idx == 33) {
								mvm.setPRESIDENTE2(v);
								
							}
							
							/* Tesori 3 B */
							if(idx == 35) {
								mvm.setTESORI3_B(v);
								
							}
							
							/* 1 Contatto B */
							if(idx == 38) {
								mvm.setEFFICACI_B_PC(v);
								
							}else if(idx == 40) {
								//Ass
								mvm.setEFFICACI_B_PC_ASS(v);
								
							}
														
							/* 1 Vis Ult B */
							if(idx == 42) {
								mvm.setEFFICACI_B_PVU(v);
								
							}else if(idx == 44) {
								//Ass
								mvm.setEFFICACI_B_PVU_ASS(v);
								
							}
							
							/* 2 Vis Ult B */
							if(idx == 46) {
								mvm.setEFFICACI_B_SVU(v);
								
							}else if(idx == 48) {
								//Ass
								mvm.setEFFICACI_B_SVU_ASS(v);
								
							}
							
							/* 3 Vis Ult B */
							if(idx == 50) {
								mvm.setEFFICACI_B_TVU(v);
								
							}else if(idx == 52) {
								//Ass
								mvm.setEFFICACI_B_TVU_ASS(v);
								
							}
							
							/* Studio Bibblico B */
							if(idx == 54) {
								mvm.setEFFICACI_B_SB(v);
								
							}else if(idx == 56) {
								//Ass
								mvm.setEFFICACI_B_SB_ASS(v);
								
							}
							
							/* Discorso B */
							if(idx == 58) {
								mvm.setEFFICACI_B_D(v);
								
							}
							
							/* Vita 1 */
							if(idx == 61) {
								mvm.setVITA1(v);
								
							}
							
							/* Vita 2 */
							if(idx == 62) {
								mvm.setVITA2(v);
								
							}
							
							/* Vita SB */
							if(idx == 63) {
								mvm.setVITA_SB(v);
								
							}
							
							/* Da Rivedere queste ultime righe dopo prossima versione (luglio 2018) */
							
							/* Preg Iniz */
							if(idx == 67) {
								mvm.setPREGINIZ(v);
								
							}
							/* Preg Fin */
							if(idx == 68) {
								mvm.setPREGFIN(v);
								
							}
							/* Lettore */
							if(idx == 69) {
								mvm.setLETTORE(v);
								
							}
						}

						if(hsColor.equals("C6C6:D9D9:F1F1")) {
							mvm.setAssemblea(true);
						}else if(hsColor.equals("FFFF:9999:0")) {
							mvm.setVisitasorvegliante(true);
						}else if(hsColor.equals("9999:3333:0")) {
							mvm.setClassesupplsosp(true);
						}
						
						
					}
					
					models.add(mvm);
				}
				cR++;
			}
			
			System.out.println(models.size());
			
			for (int i = 0; i < models.size(); i++) {
				ModelVM mvm = models.get(i);
				
				VitaMinisteroDett vmd = new VitaMinisteroDett();
				
				if(mvm.getDATA()!=null) {
					if(mvm.assemblea) {
						System.out.println(mvm.getDATA()+" Assemblea");
					}else {
						String data 	= mvm.getDATA();
						vmd.setData(data);
						/** Recupero i titoli delle parti dal web **/
						
						String pres1 	= mvm.getPRESIDENTE1();
						String tes1 	= mvm.getTESORI1();
						String tes2 	= mvm.getTESORI2();
						String tes3a 	= mvm.getTESORI3_A();
						
						vmd.setIdpresidente1(getUserId(pres1));
						vmd.setIdtesori1(getUserId(tes1));
						vmd.setIdtesori2(getUserId(tes2));
						vmd.setIdtesori3A(getUserId(tes3a));
						
						/** CASISTICHE EFFICACI **/
						String eff1A = null,eff2A = null,eff3A = null;
						String labelEff1 = null,labelEff2 = null,labelEff3 = null;
											
						/* 1 contatto */
						String pcA 		= mvm.getEFFICACI_A_PC();
						String pcassA 	= mvm.getEFFICACI_A_PC_ASS();
						
						/* 1 vis ult */
						String pvuA 	= mvm.getEFFICACI_A_PVU();
						String pvuassA 	= mvm.getEFFICACI_A_PVU_ASS();
						
						/* 2 vis ult */
						String svuA 	= mvm.getEFFICACI_A_SVU();
						String svuassA 	= mvm.getEFFICACI_A_SVU_ASS();
						
						/* 3 vis ult */
						String tvuA 	= mvm.getEFFICACI_A_TVU();
						String tvuassA 	= mvm.getEFFICACI_A_TVU_ASS();
						
						/* studio */
						String sbA 		= mvm.getEFFICACI_A_SB();
						String sbassA 	= mvm.getEFFICACI_A_SB_ASS();
						
						/* discorso */
						String dA 	= mvm.getEFFICACI_A_D();
						
						/*****/
						if(pcA!=null) {
							eff1A = pcA+" - "+pcassA;
							labelEff1 = "1° Contatto";
						}else if(pvuA!=null) {
							eff1A = pvuA+" - "+pvuassA;
							labelEff1 = "1° Vis. Ulteriore";
						}else if(svuA!=null) {
							eff1A = svuA+" - "+svuassA;
							labelEff1 = "2° Vis. Ulteriore";
						}else{
							eff1A = tvuA+" - "+tvuassA;
							labelEff1 = "3° Vis. Ulteriore";
						}

						if(eff1A!=null) {
							if(pvuA!=null && !eff1A.equals(pvuA+" - "+pvuassA)) {
								eff2A = pvuA+" - "+pvuassA;
								labelEff2 = "1° Vis. Ulteriore";
							}else if(svuA!=null  && !eff1A.equals(svuA+" - "+svuassA)) {
								eff2A = svuA+" - "+svuassA;
								labelEff2 = "2° Vis. Ulteriore";
							}else{
								eff2A = tvuA+" - "+tvuassA;
								labelEff2 = "3° Vis. Ulteriore";
							}
						}
						
						if(sbA!=null) {
							eff3A = sbA+" - "+sbassA;
							labelEff3 = "Studio Bibblico";
						}else {
							eff3A = dA;
							labelEff3 = "Discorso";
						}
						
						if(eff1A!=null && eff1A.contains("VIDEO")) {
							labelEff1 += " (Video)";
							eff1A = "(Presidente)";
						}
						
						if(eff2A!=null && eff2A.contains("VIDEO")) {
							labelEff2 += " (Video)";
							eff2A = "(Presidente)";
						}
						
						if(eff3A!=null && eff3A.contains("VIDEO")) {
							labelEff3 += " (Video)";
							eff3A = "(Presidente)";
						}
						
						/***/
						
						System.out.println("");
						System.out.println(" --> "+data);
						System.out.println("Sala A: "+pres1+" / "+tes1+" / "+tes2+" / "+tes3a+" / "
								+ labelEff1+": "+eff1A+" / "+labelEff2+": "+ eff2A+" / "+labelEff3+": "+eff3A);
						
						vmd.setIdtipoEfficaci1(getIdTipoEfficaci(labelEff1));
						vmd.setIdtipoEfficaci2(getIdTipoEfficaci(labelEff2));
						vmd.setIdtipoEfficaci3(getIdTipoEfficaci(labelEff3));
						
						if(eff1A!=null) {
							String[] eff1Ar = eff1A.split(" - ");
							if(!eff1A.contains("Presidente") && !eff1Ar[0].equals("null")) {
								vmd.setIdproceff1A(getUserId(eff1Ar[0]));
								vmd.setIdproceff1A_ass(getUserId(eff1Ar[1]));
							}
						}
						
						if(eff2A!=null) {
							String[] eff2Ar = eff2A.split(" - ");
							if(!eff2A.contains("Presidente")  && !eff2Ar[0].equals("null")) {
								vmd.setIdproceff2A(getUserId(eff2Ar[0]));
								vmd.setIdproceff2A_ass(getUserId(eff2Ar[1]));
							}
						}
						
						if(eff3A!=null) {
							String[] eff3Ar = eff3A.split(" - ");
							if(!eff3A.contains("Presidente")  && !eff3Ar[0].equals("null")) {
								vmd.setIdproceff3A(getUserId(eff3Ar[0]));
								if(eff3Ar.length>1) {
									vmd.setIdproceff3A_ass(getUserId(eff3Ar[1]));
									System.out.println(vmd.getIdproceff3A_ass());
								}
							}
						}
						
						/* Se c'è la seconda sala salvo anche i suoi dati */
						if(!mvm.classesupplsosp) {
							
							String eff1B = null,eff2B = null,eff3B = null;
							
							String pres2 = mvm.getPRESIDENTE2();
							String tes3b 	=mvm.getTESORI3_B();
							
							vmd.setIdpresidente2(getUserId(pres2));
							vmd.setIdtesori3B(getUserId(tes3b));
							
							/** 1 **/
							if(labelEff1.contains("1° Contatto")) {
								eff1B = mvm.getEFFICACI_B_PC()+" - "+mvm.getEFFICACI_B_PC_ASS();
							}else if(labelEff1.equals("1° Vis. Ulteriore")) {
								eff1B = mvm.getEFFICACI_B_PVU()+" - "+mvm.getEFFICACI_B_PVU_ASS();							
							}else if(labelEff1.equals("2° Vis. Ulteriore")) {
								eff1B = mvm.getEFFICACI_B_SVU()+" - "+mvm.getEFFICACI_B_SVU_ASS();								
							}else if(labelEff1.equals("3° Vis. Ulteriore")) {
								eff1B = mvm.getEFFICACI_B_TVU()+" - "+mvm.getEFFICACI_B_TVU_ASS();							
							}
							
							/** 2 **/
							if(labelEff2.contains("1° Vis. Ulteriore")) {
								eff2B = mvm.getEFFICACI_B_PVU()+" - "+mvm.getEFFICACI_B_PVU_ASS();							
							}else if(labelEff2.equals("2° Vis. Ulteriore")) {
								eff2B = mvm.getEFFICACI_B_SVU()+" - "+mvm.getEFFICACI_B_SVU_ASS();								
							}else if(labelEff2.equals("3° Vis. Ulteriore")) {
								eff2B = mvm.getEFFICACI_B_TVU()+" - "+mvm.getEFFICACI_B_TVU_ASS();								
							}
							
							/** 3 **/
							if(labelEff3.contains("Studio Bibblico")) {
								eff3B = mvm.getEFFICACI_B_SB()+" - "+mvm.getEFFICACI_B_SB_ASS();								
							}else {
								eff3B = mvm.getEFFICACI_B_D();						
							}
							
							if(labelEff1!=null && labelEff1.contains("Video")) {
								eff1B = "(Presidente)";
							}
							
							if(labelEff2!=null && labelEff2.contains("Video")) {
								eff2B = "(Presidente)";
							}
							
							if(labelEff3!=null && labelEff3.contains("Video")) {
								eff3B = "(Presidente)";
							}
							
							System.out.println("Sala B: "+pres2+" / "+tes3b+" / "
									+ labelEff1+": "+eff1B+" / "+labelEff2+": "+ eff2B+" / "+labelEff3+": "+eff3B);
							
							if(eff1B!=null) {
								String[] eff1Br = eff1B.split(" - ");
								if(!eff1B.contains("Presidente") && !eff1Br[0].equals("null")) {
									vmd.setIdproceff1B(getUserId(eff1Br[0]));
									vmd.setIdproceff1B_ass(getUserId(eff1Br[1]));
								}
							}
							
							if(eff2B!=null) {
								String[] eff2Br = eff2B.split(" - ");
								if(!eff2B.contains("Presidente")  && !eff2Br[0].equals("null")) {
									vmd.setIdproceff2B(getUserId(eff2Br[0]));
									vmd.setIdproceff2B_ass(getUserId(eff2Br[1]));
								}
							}
							
							if(eff3B!=null) {
								String[] eff3Br = eff3B.split(" - ");
								if(!eff3B.contains("Presidente")  && !eff3Br[0].equals("null")) {
									vmd.setIdproceff3B(getUserId(eff3Br[0]));
									if(eff3Br.length>1) {
										vmd.setIdproceff3B_ass(getUserId(eff3Br[1]));
										System.out.println(vmd.getIdproceff3B_ass());
									}
								}
							}
							
						}
						
						String vita1 	= mvm.getVITA1();
						String vita2 	= mvm.getVITA2();
						String vitaSB 	= mvm.getVITA_SB();
						
						String preginiz = mvm.getPREGINIZ();
						String pregfin 	= mvm.getPREGFIN();
						String lettore 	= mvm.getLETTORE();
						
						if(vita1!=null)
							System.out.println("Vita1: "+vita1);
						if(vita2!=null)
							System.out.println("Vita2: "+vita2);
						if(vitaSB!=null)
							System.out.println("VitaSB: "+vitaSB);
						if(preginiz!=null)
							System.out.println("Preg. Iniz.: "+preginiz);
						if(pregfin!=null)
							System.out.println("Preg. Fin: "+pregfin);
						if(lettore!=null)
							System.out.println("Lettore: "+lettore);
						
						vmd.setIdvita1(getUserId(vita1));
						vmd.setIdvita2(getUserId(vita2));
						vmd.setIdvitaStudio(getUserId(vitaSB));
						
						vmd.setIdpreghiera1(getUserId(preginiz));
						vmd.setIdpreghiera2(getUserId(pregfin));
						vmd.setIdproclettore(getUserId(lettore));
						
						vmd = WebUrl.readGuida(data, vmd);
						
						importIntoDB(vmd);
						
						System.out.println("");
					}
					
					/*else if(mvm.classesupplsosp) {
						
					}else if(mvm.visitasorvegliante) {
						System.out.println(mvm.getDATA()+" Visita Sorvegliante");
					}*/
					
					
				}
				
			}
			 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	private static void importIntoDB(VitaMinisteroDett vmd) {
		//Controllo se esiste già un vm per questo mese
		String data = vmd.getData();
		Date date = Utils.reverseStringInDate(data);
		int mm = Utils.getMonthFromDate(date);
		String strMM = Utils.getNameMese(mm);
		int aa = Utils.getYearFromDate(date);
		String qVm = "SELECT * FROM gp_vitaministero where mese = '"+strMM+"' and anno="+aa+"";
		JSONArray resVm = GePrato.getSelectResponse(qVm);
		
		int idVM = 0;
		if(resVm.length()<1) {
			String insVm = "insert into gp_vitaministero(mese,anno) values('"+strMM+"',"+aa+")";
			GePrato.getInsertResponse(insVm);
			resVm = GePrato.getSelectResponse(qVm);
		}
		
		idVM = resVm.getJSONObject(0).getInt("id");
		System.out.println(idVM);
		
	}

	private static int getIdTipoEfficaci(String label) {
		int id=0;
		String code = "";
		
		if(label.contains("1° Contatto"))
			code = "PC";
		else if(label.contains("1° Vis. Ulteriore"))
			code = "PVU";
		else if(label.contains("2° Vis. Ulteriore"))
			code = "SVU";
		else if(label.contains("3° Vis. Ulteriore"))
			code = "TVU";
		else if(label.contains("2° Vis. Ulteriore"))
			code = "SVU";
		else if(label.contains("Studio Bibblico"))
			code = "SB";
		else
			code = "D";
		
		JSONArray res = GePrato.getSelectResponse("select * from gp_efficacitipi where cod='"+code+"'");
		if(res.length()>0) {
			id = res.getJSONObject(0).getInt("id");
		}
		
		return id;
	}

	private static int getUserId(String procl) {		
		int id = 0;
		String cogn ="", iniznome = "";
		
		if(procl!=null && !procl.isEmpty() && !procl.contains("Presidente")) {
			String[] elProc = procl.split(" ");
			for (int i = 0; i < elProc.length-1; i++) {
				cogn += elProc[i]+" ";
			}
			
			cogn = cogn.trim();
			
			iniznome = elProc[elProc.length-1].replaceAll("\\.", "").trim();
			
			cogn = cogn.replace("'", "");
					
			JSONArray res = GePrato.getSelectResponse("select id, cognome, nome from gp_proclamatori p where lower(cognome) like '%"+cogn+"%'");
			for (int i = 0; i < res.length(); i++) {
				JSONObject obj = res.getJSONObject(i);
				String nome = obj.getString("nome");
			
				if(nome.substring(0, iniznome.length()).equals(iniznome)) {
					id = obj.getInt("id");
					System.out.println(id);
					i = res.length();
				}
			}
		}
		return id;
	}

	
}

class ModelVM {
	  String  DATA;
	  String  PRESIDENTE1;
	  String  TESORI1;
	  String  TESORI2;
	  String  TESORI3_A;
	  String  TESORI3_B;
	  String  EFFICACI_A_PC;
	  String  EFFICACI_A_PC_ASS;
	  String  EFFICACI_A_PVU;
	  String  EFFICACI_A_PVU_ASS;
	  String  EFFICACI_A_SVU;
	  String  EFFICACI_A_SVU_ASS;
	  String  EFFICACI_A_TVU;
	  String  EFFICACI_A_TVU_ASS;
	  String  EFFICACI_A_SB;
	  String  EFFICACI_A_SB_ASS;
	  String  EFFICACI_A_D;
	  String  PRESIDENTE2;
	  String  EFFICACI_B_PC;
	  String  EFFICACI_B_PC_ASS;
	  String  EFFICACI_B_PVU;
	  String  EFFICACI_B_PVU_ASS;
	  String  EFFICACI_B_SVU;
	  String  EFFICACI_B_SVU_ASS;
	  String  EFFICACI_B_TVU;
	  String  EFFICACI_B_TVU_ASS;
	  String  EFFICACI_B_SB;
	  String  EFFICACI_B_SB_ASS;
	  String  EFFICACI_B_D;
	  String  VITA1;
	  String  VITA2;
	  String  VITA_SB;
	  String  PREGINIZ;
	  String  PREGFIN;
	  String  LETTORE;
	  boolean assemblea;
	  boolean visitasorvegliante;
	  boolean classesupplsosp;
	  
	  
	public String getDATA() {
		return DATA;
	}
	public void setDATA(String dATA) {
		DATA = dATA;
	}
	public String getPRESIDENTE1() {
		return PRESIDENTE1;
	}
	public void setPRESIDENTE1(String pRESIDENTE1) {
		PRESIDENTE1 = pRESIDENTE1;
	}
	public String getTESORI1() {
		return TESORI1;
	}
	public void setTESORI1(String tESORI1) {
		TESORI1 = tESORI1;
	}
	public String getTESORI2() {
		return TESORI2;
	}
	public void setTESORI2(String tESORI2) {
		TESORI2 = tESORI2;
	}
	public String getTESORI3_A() {
		return TESORI3_A;
	}
	public void setTESORI3_A(String tESORI3_A) {
		TESORI3_A = tESORI3_A;
	}
	public String getTESORI3_B() {
		return TESORI3_B;
	}
	public void setTESORI3_B(String tESORI3_B) {
		TESORI3_B = tESORI3_B;
	}
	public String getEFFICACI_A_PC() {
		return EFFICACI_A_PC;
	}
	public void setEFFICACI_A_PC(String eFFICACI_A_PC) {
		EFFICACI_A_PC = eFFICACI_A_PC;
	}
	public String getEFFICACI_A_PC_ASS() {
		return EFFICACI_A_PC_ASS;
	}
	public void setEFFICACI_A_PC_ASS(String eFFICACI_A_PC_ASS) {
		EFFICACI_A_PC_ASS = eFFICACI_A_PC_ASS;
	}
	public String getEFFICACI_A_PVU() {
		return EFFICACI_A_PVU;
	}
	public void setEFFICACI_A_PVU(String eFFICACI_A_PVU) {
		EFFICACI_A_PVU = eFFICACI_A_PVU;
	}
	public String getEFFICACI_A_PVU_ASS() {
		return EFFICACI_A_PVU_ASS;
	}
	public void setEFFICACI_A_PVU_ASS(String eFFICACI_A_PVU_ASS) {
		EFFICACI_A_PVU_ASS = eFFICACI_A_PVU_ASS;
	}
	public String getEFFICACI_A_SVU() {
		return EFFICACI_A_SVU;
	}
	public void setEFFICACI_A_SVU(String eFFICACI_A_SVU) {
		EFFICACI_A_SVU = eFFICACI_A_SVU;
	}
	public String getEFFICACI_A_SVU_ASS() {
		return EFFICACI_A_SVU_ASS;
	}
	public void setEFFICACI_A_SVU_ASS(String eFFICACI_A_SVU_ASS) {
		EFFICACI_A_SVU_ASS = eFFICACI_A_SVU_ASS;
	}
	public String getEFFICACI_A_TVU() {
		return EFFICACI_A_TVU;
	}
	public void setEFFICACI_A_TVU(String eFFICACI_A_TVU) {
		EFFICACI_A_TVU = eFFICACI_A_TVU;
	}
	public String getEFFICACI_A_TVU_ASS() {
		return EFFICACI_A_TVU_ASS;
	}
	public void setEFFICACI_A_TVU_ASS(String eFFICACI_A_TVU_ASS) {
		EFFICACI_A_TVU_ASS = eFFICACI_A_TVU_ASS;
	}
	public String getEFFICACI_A_SB() {
		return EFFICACI_A_SB;
	}
	public void setEFFICACI_A_SB(String eFFICACI_A_SB) {
		EFFICACI_A_SB = eFFICACI_A_SB;
	}
	public String getEFFICACI_A_SB_ASS() {
		return EFFICACI_A_SB_ASS;
	}
	public void setEFFICACI_A_SB_ASS(String eFFICACI_A_SB_ASS) {
		EFFICACI_A_SB_ASS = eFFICACI_A_SB_ASS;
	}
	public String getEFFICACI_A_D() {
		return EFFICACI_A_D;
	}
	public void setEFFICACI_A_D(String eFFICACI_A_D) {
		EFFICACI_A_D = eFFICACI_A_D;
	}
	public String getPRESIDENTE2() {
		return PRESIDENTE2;
	}
	public void setPRESIDENTE2(String pRESIDENTE2) {
		PRESIDENTE2 = pRESIDENTE2;
	}
	public String getEFFICACI_B_PC() {
		return EFFICACI_B_PC;
	}
	public void setEFFICACI_B_PC(String eFFICACI_B_PC) {
		EFFICACI_B_PC = eFFICACI_B_PC;
	}
	public String getEFFICACI_B_PC_ASS() {
		return EFFICACI_B_PC_ASS;
	}
	public void setEFFICACI_B_PC_ASS(String eFFICACI_B_PC_ASS) {
		EFFICACI_B_PC_ASS = eFFICACI_B_PC_ASS;
	}
	public String getEFFICACI_B_PVU() {
		return EFFICACI_B_PVU;
	}
	public void setEFFICACI_B_PVU(String eFFICACI_B_PVU) {
		EFFICACI_B_PVU = eFFICACI_B_PVU;
	}
	public String getEFFICACI_B_PVU_ASS() {
		return EFFICACI_B_PVU_ASS;
	}
	public void setEFFICACI_B_PVU_ASS(String eFFICACI_B_PVU_ASS) {
		EFFICACI_B_PVU_ASS = eFFICACI_B_PVU_ASS;
	}
	public String getEFFICACI_B_SVU() {
		return EFFICACI_B_SVU;
	}
	public void setEFFICACI_B_SVU(String eFFICACI_B_SVU) {
		EFFICACI_B_SVU = eFFICACI_B_SVU;
	}
	public String getEFFICACI_B_SVU_ASS() {
		return EFFICACI_B_SVU_ASS;
	}
	public void setEFFICACI_B_SVU_ASS(String eFFICACI_B_SVU_ASS) {
		EFFICACI_B_SVU_ASS = eFFICACI_B_SVU_ASS;
	}
	public String getEFFICACI_B_TVU() {
		return EFFICACI_B_TVU;
	}
	public void setEFFICACI_B_TVU(String eFFICACI_B_TVU) {
		EFFICACI_B_TVU = eFFICACI_B_TVU;
	}
	public String getEFFICACI_B_TVU_ASS() {
		return EFFICACI_B_TVU_ASS;
	}
	public void setEFFICACI_B_TVU_ASS(String eFFICACI_B_TVU_ASS) {
		EFFICACI_B_TVU_ASS = eFFICACI_B_TVU_ASS;
	}
	public String getEFFICACI_B_SB() {
		return EFFICACI_B_SB;
	}
	public void setEFFICACI_B_SB(String eFFICACI_B_SB) {
		EFFICACI_B_SB = eFFICACI_B_SB;
	}
	public String getEFFICACI_B_SB_ASS() {
		return EFFICACI_B_SB_ASS;
	}
	public void setEFFICACI_B_SB_ASS(String eFFICACI_B_SB_ASS) {
		EFFICACI_B_SB_ASS = eFFICACI_B_SB_ASS;
	}
	public String getEFFICACI_B_D() {
		return EFFICACI_B_D;
	}
	public void setEFFICACI_B_D(String eFFICACI_B_D) {
		EFFICACI_B_D = eFFICACI_B_D;
	}
	public String getVITA1() {
		return VITA1;
	}
	public void setVITA1(String vITA1) {
		VITA1 = vITA1;
	}
	public String getVITA2() {
		return VITA2;
	}
	public void setVITA2(String vITA2) {
		VITA2 = vITA2;
	}
	public String getVITA_SB() {
		return VITA_SB;
	}
	public void setVITA_SB(String vITA_SB) {
		VITA_SB = vITA_SB;
	}
	public String getPREGINIZ() {
		return PREGINIZ;
	}
	public void setPREGINIZ(String pREGINIZ) {
		PREGINIZ = pREGINIZ;
	}
	public String getPREGFIN() {
		return PREGFIN;
	}
	public void setPREGFIN(String pREGFIN) {
		PREGFIN = pREGFIN;
	}
	public String getLETTORE() {
		return LETTORE;
	}
	public void setLETTORE(String lETTORE) {
		LETTORE = lETTORE;
	}
	public boolean isAssemblea() {
		return assemblea;
	}
	public void setAssemblea(boolean assemblea) {
		this.assemblea = assemblea;
	}
	public boolean isVisitasorvegliante() {
		return visitasorvegliante;
	}
	public void setVisitasorvegliante(boolean visitasorvegliante) {
		this.visitasorvegliante = visitasorvegliante;
	}
	public boolean isClassesupplsosp() {
		return classesupplsosp;
	}
	public void setClassesupplsosp(boolean classesupplsosp) {
		this.classesupplsosp = classesupplsosp;
	}
	  
	  
	
	
}
