package b.printfoot;

import org.json.JSONArray;

import b.GePrato;
import b.printers.CreateParameterProgramma;


public class TestimonianzaPubblica {
	
	String settimana;
	String anno;
	String mese;
	String giorno;
	String postazione;
	String ora;
	String proclamatore1;
	String proclamatore2;
	String proclamatore3;
	String proclamatore4;
	String numSettimana;
	
	
	
	public String getSettimana() {
		return settimana;
	}
	public void setSettimana(String settimana) {
		this.settimana = settimana;
	}
	public String getAnno() {
		return anno;
	}
	public void setAnno(String anno) {
		this.anno = anno;
	}
	public String getMese() {
		return mese;
	}
	public void setMese(String mese) {
		this.mese = mese;
	}
	public String getGiorno() {
		return giorno;
	}
	public void setGiorno(String giorno) {
		this.giorno = giorno;
	}
	public String getPostazione() {
		return postazione;
	}
	public void setPostazione(String postazione) {
		this.postazione = postazione;
	}
	public String getOra() {
		return ora;
	}
	public void setOra(String ora) {
		this.ora = ora;
	}
	public String getProclamatore1() {
		return proclamatore1;
	}
	public void setProclamatore1(String proclamatore1) {
		this.proclamatore1 = proclamatore1;
	}
	public String getProclamatore2() {
		return proclamatore2;
	}
	public void setProclamatore2(String proclamatore2) {
		this.proclamatore2 = proclamatore2;
	}
	public String getProclamatore3() {
		return proclamatore3;
	}
	public void setProclamatore3(String proclamatore3) {
		this.proclamatore3 = proclamatore3;
	}
	public String getProclamatore4() {
		return proclamatore4;
	}
	public void setProclamatore4(String proclamatore4) {
		this.proclamatore4 = proclamatore4;
	}
	
	public String getNumSettimana() {
		return numSettimana;
	}
	public void setNumSettimana(String numSettimana) {
		this.numSettimana = numSettimana;
	}
	public static TestimonianzaPubblica[] createBeanArray() {
		String qry = "SELECT p.mese, p.anno,d.data, pr.nome, pr.cognome, pr.nomevisuale, "+
				"t.oradal, t.oraal, t.giorno, pos.descrizione as postazione, pos.tipo as tipopostazione "+
				"FROM gp_disponibilita d, "+
				"gp_programmi p , "+
				"gp_proclamatori pr, "+
				"gp_turni t, "+
				"gp_postazioni pos "+
				"where idprogramma = 16 "+
				"and p.id= d.idprogramma "+
				"and pr.id = d.idproc "+
				"and t.id= d.idturno "+
				"and pos.id = t.idpostazione "+
				"order by data, postazione ";
		JSONArray pp = GePrato.getSelectResponse(qry);
		
		
		
		String[] reportNames = {"testimonianza_pubblica"};
		TestimonianzaPubblica[] javabeanarray = CreateParameterProgramma.getRows(pp);

	    
	    return javabeanarray;
	}

	
	
}
