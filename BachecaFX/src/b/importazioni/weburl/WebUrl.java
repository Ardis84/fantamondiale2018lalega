package b.importazioni.weburl;

import java.util.Date;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import b.Utils;
import b.printfoot.VitaMinisteroDett;

public class WebUrl {

	static String urlBase = "https://wol.jw.org/";
	
	public static void main(String[] args) {
		 VitaMinisteroDett vmd = readGuida(6,2018,14);

	}
	
	public static VitaMinisteroDett readGuida(String data,  VitaMinisteroDett vmd) {
		int mese 	= Integer.valueOf(data.split("/")[1]);
		int anno 	= Integer.valueOf(data.split("/")[2]);
		int gg 		= Integer.valueOf(data.split("/")[0]);
		
		return readGuida(mese, anno, gg, vmd);	
	}
	
	public static VitaMinisteroDett readGuida(String data) {
		int mese 	= Integer.valueOf(data.split("/")[1]);
		int anno 	= Integer.valueOf(data.split("/")[2]);
		int gg 		= Integer.valueOf(data.split("/")[0]);
		
		return readGuida(mese, anno, gg, null);	
	}
	
	public static VitaMinisteroDett readGuida(int mese, int anno, int gg) {
		return readGuida(mese, anno, gg, null);	
	}
	
	public static VitaMinisteroDett readGuida(int mese, int anno, int gg, VitaMinisteroDett vmd) {
		if(vmd==null)
			vmd = new VitaMinisteroDett();
		
		String strMonthSelect = Utils.getNameMese(mese);
		
		String urlPrincipal = "https://wol.jw.org/it/wol/lv/r6/lp-i/0/55470";
		 org.jsoup.select.Elements elm = Utils.getElementFromUrl(urlPrincipal,".directory");
		 org.jsoup.select.Elements liS = elm.select("li");
		 /* --------- */
		 for (int i = 0; i < liS.size(); i++) {
			Element li = liS.get(i);
			Elements title = li.select(".title");
			String val = title.get(0).text();
			
			if(val.contains(anno+"")) {
				System.out.println(val);
				String link = li.select("a").attr("href");
				Elements guidePage = Utils.getElementFromUrl(urlBase+link, ".directory");
				org.jsoup.select.Elements liG = guidePage.select("li");
				/* --------- */
				for (int j = 0; j < liG.size(); j++) {
					Element monthGuide = liG.get(j);
					Elements issueTitle = monthGuide.select(".issueTitle");
					String[] its = issueTitle.text().split(",");
					if(its.length>1) {
						String strMonth = its[1].trim().split(" ")[0];
						
						if(strMonthSelect.equals(strMonth)) {
							String linkMonth = monthGuide.select("a").attr("href");
							Elements monthPage = Utils.getElementFromUrl(urlBase+linkMonth, ".directory");
							Elements liM = monthPage.select("li");
							/* --------- */
							for (int k = 0; k < liM.size(); k++) {
								String titleWeek = liM.get(k).select(".title").text();
								
								if(titleWeek.contains(strMonthSelect) && !titleWeek.contains(anno+"")) {
									titleWeek = titleWeek.replaceAll(strMonthSelect, "");
									System.out.println(titleWeek);
									String firstWeekDay = titleWeek.trim().split("-")[0];
									boolean isThisWeek = checkIfSelectedDayIsInSameWeek(gg,firstWeekDay,mese, anno);
									System.out.println(isThisWeek);
									if(isThisWeek) {
										String linkWeek = liM.get(k).select("a").attr("href");
										Elements article = Utils.getElementFromUrl(urlBase+linkWeek, "#article");
										Elements lettura = article.select("header").select("#p2");
										System.out.println(lettura.text());
										
										vmd.setCapitolilettura(lettura.text());									
										vmd.setData(getSelectedDate(gg, mese, anno));
										
										/* Prima sezione cantico1 */
										Elements section1 = article.select("#section1");
										String cantico1 = section1.select("a").text();
										vmd.setCantico1(cantico1);
										
										/* Seconda sezione lettura bibblica (tesori3) */
										Elements section2 = article.select("#section2");
										Element liTesori3 = section2.select("li").get(2);
										String tesori3 = liTesori3.select("a").text();
										vmd.setTesori3capitoli(tesori3);
										
										/* Quarta sezione vita */
										Elements section4 = article.select("#section4");
										Elements liVita = section4.select("li");
										
										Element liCantico2 = liVita.get(0);
										String cantico2 = liCantico2.select("a").text();
										vmd.setCantico2(cantico2);
										
										Element liCantico3 = liVita.get(liVita.size()-1);
										String cantico3 = liCantico3.select("a").text();
										vmd.setCantico3(cantico3);
										
										for (int l = 1; l < liVita.size()-2; l++) {
											Element lv = liVita.get(i);
											Element tlt = lv.select("a").get(0);
											if(l==1)
												vmd.setTemaVita1(tlt.text());
											if(l==2 && l!=liVita.size()-3)
												vmd.setTemaVita2(tlt.text());
											if(l==3 || (l==2 && l==liVita.size()-3))
												vmd.setCapitoliStudioLibro(tlt.text());
				
										}
									}
								}
							}
							
							
						}
					}
				}
			}
			
			
		 }
		 
		 return vmd;
	}

	private static boolean checkIfSelectedDayIsInSameWeek(int gg, String firstWeekDay, int mese, int anno) {		
		Date date = Utils.reverseStringInDate(getSelectedDate(gg, mese, anno));
		String week = Utils.getSettimanaFromDate(date);
		
		String weekFirstDay = week.split(" - ")[0];
		
		if(weekFirstDay.equals(firstWeekDay))
			return true;
		else		
			return false;
	}
	
	private static String getSelectedDate(int gg, int mese, int anno) {
		String strGG = gg+"";
		if(gg<10) {
			strGG = "0"+gg;
		}
		
		String strMese = mese+"";
		if(mese<10) {
			strMese = "0"+mese;
		}
		
		return strGG+"/"+strMese+"/"+anno;
	}

}
