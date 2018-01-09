package b.printers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.JComboBox;

import org.json.JSONArray;

import b.GePrato;
import b.PrintJasper;
import b.printfoot.TestimonianzaPubblica;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;


public class Printers {

	
	public static void stampaProgramma(String selMese, String selAnno) {				
				/* Controllo che esista un programma per quel mese e quell'anno */				
				String qry = "SELECT p.mese, p.anno,d.data, pr.nome, pr.cognome, pr.nomevisuale, "+
						"t.oradal, t.oraal, t.giorno, pos.descrizione as postazione, pos.tipo as tipopostazione "+
						"FROM gp_disponibilita d, "+
						"gp_programmi p , "+
						"gp_proclamatori pr, "+
						"gp_turni t, "+
						"gp_postazioni pos "+
						/*"where idprogramma = "+id+" "+*/
						"where p.mese = '"+selMese+"' "+
						"and p.anno ="+selAnno+" "+
						"and p.id= d.idprogramma "+
						"and pr.id = d.idproc "+
						"and t.id= d.idturno "+
						"and pos.id = t.idpostazione "+
						"order by data, postazione, oradal, oraal ";
				JSONArray pp = GePrato.getSelectResponse(qry);
								
				String[] reportNames = {"testimonianza_pubblica"};
				TestimonianzaPubblica[] javabeanarray = CreateParameterProgramma.getRows(pp);
				
//				 try {
//					 parameters.put("html", Jsoup.connect("http://geprato.altervista.org/stampe/promoStampaUnificata.php?id="+id).get().html());
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				} 
				
//				Connection conn = SQLiteJDBC.getConnection();
				Hashtable<String, Object> parameters = new Hashtable<String, Object>();
				JRDataSource beanArrayDataSource = new JRBeanArrayDataSource(javabeanarray);
				PrintJasper.getPdfReport(reportNames, parameters , beanArrayDataSource);
				
				PrintJasper.openPdfViewer(reportNames);
				
				//PrintProgramma.generateAndPrint(pp, name);

	}
	
}
