package b.importazioni;

import java.util.HashMap;

import org.apache.commons.collections.map.HashedMap;
import org.json.JSONArray;
import org.json.JSONObject;

import b.GePrato;

public class ImportIncarichi {

	public static HashMap<String,String> ruoliId = new HashMap<String,String>();
	
	public static void main(String[] args) {
	
		addIncarichi();
		
		String[] uscieri = {
				"BUCCINNA' M.",
				"CORRADINI D.",
				"D'ONOFRIO S.",
				"FANELLI S.",
				"GOLISANO G.",
				"GRILLO D.",
				"GRILLO G.",
				"NEGRO C.",
				"PROVVIDENTI E.",
				"RAGUSO Y.",
				"ROBERTI O.",
				"ZANGRILLI A.",
				"ZAPPALA' B.",
				"ZUPPARDO D.",
				"ZUPPARDO M.",
				"ZUPPARDO V."
		};
		
		importaIncarico(uscieri,"uscieri");
		
		String[] audioV = {"BORGO W.",
				"CORRADINI D.",
				"D'ONOFRIO S.",
				"GALLUCCIO M.",
				"GRILLO D.",
				"ZANGRILLI A.",
				"ZIGLIOLI M."};
		
		importaIncarico(audioV,"audio/video");
		
		String[] micro = {"BUCCINNA' M.",
				"CORRADINI D.",
				"D'ONOFRIO S.",
				"FANELLI S.",
				"GOLISANO A.",
				"GOLISANO G.",
				"GRILLO D.",
				"GRILLO G.",
				"PROVVIDENTI E.",
				"RAGUSO Y.",
				"ROBERTI O.",
				"ZANGRILLI A.",
				"ZAPPALA' B.",
				"ZARRA G.",
				"ZUPPARDO D.",
				"ZUPPARDO M.",
				"ZUPPARDO V."};
		
		importaIncarico(micro,"microfonisti");
		
		String[] asta = {"BORGO W.",
				"CORRADINI D.",
				"D'ONOFRIO S.",
				"GALLUCCIO M.",
				"GOLISANO G.",
				"GRILLO D.",
				"GRILLO G.",
				"RAGUSO Y.",
				"ROBERTI O.",
				"ZANGRILLI A.",
				"ZIGLIOLI M.",
				"ZUPPARDO D."};
		
		importaIncarico(asta,"asta podio");
		
		String[] lettori = {"BORGO W.",
				"BUCCINNA' M.",
				"CATTOI A.",
				"CATTOI C.",
				"CORRADINI D.",
				"D'ONOFRIO S.",
				"GALLUCCIO M.",
				"GOLISANO G.",
				"GRILLO D.",
				"MALACRIDA M.",
				"MAZZOLA C.",
				"MAZZOLO I.",
				"PLUCHINO G.",
				"PROVVIDENTI E.",
				"RAGUSO Y.",
				"TEDESCO B.",
				"TROMBETTA C.",
				"ZANGRILLI A.",
				"ZAPPALA' B.",
				"ZIGLIOLI M.",
				"ZUPPARDO D.",
				"URBONI G."};
		
		importaIncarico(lettori,"lettori");
		
		String[] presDom = {"BORGO W.",
				"BUCCINNA' M.",
				"CATTOI C.",
				"CORRADINI D.",
				"CORRADINI I.",
				"GOLISANO G.",
				"MALACRIDA M.",
				"MANCA R.",
				"MAZZOLO I.",
				"RISOLI R.",
				"TEDESCO B.",
				"TEDESCO F.",
				"TROMBETTA C.",
				"URBONI G."};
		
		importaIncarico(presDom,"presidenti domenica");
		
		String[] preInGio = {"D'ONOFRIO S.",
				"FANELLI S.",
				"GALLUCCIO M.",
				"GRILLO D.",
				"PLUCHINO G.",
				"RAGUSO Y.",
				"ROBERTI O.",
				"TRUFFELLI M.",
				"ZAPPALA' B.",
				"ZIGLIOLI M.",
				"ZUPPARDO D.",
				"ZUPPARDO V."};
		
		importaIncarico(preInGio,"preg.iniz.gio.");
		
		String[] pregFinGio = {"BORGO W.",
				"BUCCINNA' M.",
				"CARDONE C.",
				"CATTOI C.",
				"CORRADINI D.",
				"CORRADINI I.",
				"DE PINTO G.",
				"GOLISANO G.",
				"MALACRIDA M.",
				"MANCA R.",
				"MAZZOLA C.",
				"MAZZOLO I.",
				"NEGRO C.",
				"RISOLI R.",
				"TEDESCO B.",
				"TEDESCO F.",
				"TROMBETTA C.",
				"TROMBETTA L.",
				"URBONI G."};
		
		importaIncarico(pregFinGio,"preg.fin.gio.");
		
		String[] pregFinDom = {"BORGO W.",
				"CATTOI C.",
				"CORRADINI I.",
				"MALACRIDA M.",
				"MANCA R.",
				"MAZZOLA C.",
				"MAZZOLO I.",
				"RISOLI R.",
				"TEDESCO B.",
				"TEDESCO F.",
				"TROMBETTA C.",
				"TROMBETTA L.",
				"URBONI G."};
		
		importaIncarico(pregFinDom,"preg.fin.dom.");
		
	}

	private static void addIncarichi() {
		String[] ruoli = {"AUDIO/VIDEO",
				"ASTA PODIO",
				"LETTORI",
				"PRESIDENTI DOMENICA",
				"PREG.INIZ.GIO.",
				"PREG.FIN.GIO.",
				"PREG.FIN.DOM.",
				"USCIERI",
				"MICROFONISTI"}; 
		
		for (int i = 0; i < ruoli.length; i++) {
			String r = ruoli[i];
			String qry = "select * from gp_incarichi where reparto='"+r.toLowerCase()+"'";
			JSONArray res = GePrato.getSelectResponse(qry);
			if(res.length()<1) {
				String ins = "insert into gp_incarichi(reparto, idlivello) values('"+r.toLowerCase()+"',10)";
				GePrato.getInsertResponse(ins);
				
				qry = "select * from gp_incarichi where reparto='"+r.toLowerCase()+"'";
				res = GePrato.getSelectResponse(qry);			
			}
			
			String id = res.getJSONObject(0).getString("id");
			ruoliId.put(r.toLowerCase(), id);
		}
		
	}

	private static void importaIncarico(String[] ar, String nomeincarico) {
		System.out.println("*****************************************");
		int tot = 0;
		
		String[] mancanti = ar;
		
		String qry = "select * from gp_proclamatori where sesso='M'";
		JSONArray res = GePrato.getSelectResponse(qry);
		for (int i = 0; i < res.length(); i++) {
			JSONObject obj = res.getJSONObject(i);
			String cogn = obj.getString("cognome");
			String nome = obj.getString("nome");
			String id = obj.getString("id");
			
			for (int j = 0; j < ar.length; j++) {
				String u = ar[j];
				if(u.contains(cogn.toUpperCase())) {
					String inN = u.substring(u.length()-2, u.length()-1);
					if(inN.equals(nome.substring(0, 1).toUpperCase())) {
						System.out.println(cogn+" "+nome.substring(0,3)+".");
						
						String idincarico = ruoliId.get(nomeincarico);
						
						String qChk = "select * from gp_incarichiassegnati where idincarico="+idincarico+" and idproc="+id;
						JSONArray rchk = GePrato.getSelectResponse(qChk);
						if(rchk.length()<1) {
							String insIncarico = "INSERT INTO gp_incarichiassegnati(idincarico, idproc, validoda, validoa, ruolo) "+
												 "VALUES ("+idincarico+","+id+",STR_TO_DATE('1-05-2018', '%d-%m-%Y'),null,'Incaricato')";
							GePrato.getInsertResponse(insIncarico);
						}
						
						tot++;
						mancanti[j] = "";
					}
				}
			}
		}
		
		System.out.println(nomeincarico+" da importare: "+ar.length+"  -  "+nomeincarico+" importati: "+tot);

		System.out.println(nomeincarico.toUpperCase()+" MANCANTI: ");
		for (int i = 0; i < mancanti.length; i++) {
			if(!mancanti[i].equals("")) {
				System.out.println(mancanti[i]);
			}
		}
		System.out.println("*****************************************");
		
	}

}
