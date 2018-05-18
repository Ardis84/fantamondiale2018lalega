package b.printfoot;

import org.json.JSONArray;

import javafx.scene.control.ComboBox;


public class Comitive {
	
	String settimana;
	String anno;
	String mese;
	String data;
	String giorno;
	String ora;
	String luogo;
	String conduttore;
	String offertamese1;
	String offertamese2;
	String offertamese3;
	String offertamese4;
	String tiposettimana;

	ComboBox<String> boxConduttore;
	
	
	
	public ComboBox<String> getBoxConduttore() {
		return boxConduttore;
	}

	public void setBoxConduttore(ComboBox<String> boxConduttore) {
		this.boxConduttore = boxConduttore;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

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





	public String getOra() {
		return ora;
	}





	public void setOra(String ora) {
		this.ora = ora;
	}





	public String getLuogo() {
		return luogo;
	}





	public void setLuogo(String luogo) {
		this.luogo = luogo;
	}





	public String getConduttore() {
		return conduttore;
	}





	public void setConduttore(String conduttore) {
		this.conduttore = conduttore;
	}






	public String getOffertamese1() {
		return offertamese1;
	}

	public void setOffertamese1(String offertamese1) {
		this.offertamese1 = offertamese1;
	}

	public String getOffertamese2() {
		return offertamese2;
	}

	public void setOffertamese2(String offertamese2) {
		this.offertamese2 = offertamese2;
	}

	public String getOffertamese3() {
		return offertamese3;
	}

	public void setOffertamese3(String offertamese3) {
		this.offertamese3 = offertamese3;
	}

	public String getOffertamese4() {
		return offertamese4;
	}

	public void setOffertamese4(String offertamese4) {
		this.offertamese4 = offertamese4;
	}

	public static Comitive[] createBeanArray() {
		Comitive[] javabeanarray = new Comitive[3];

	    
	    return javabeanarray;
	}

	public String getTiposettimana() {
		return tiposettimana;
	}

	public void setTiposettimana(String tiposettimana) {
		this.tiposettimana = tiposettimana;
	}

	
	
}
