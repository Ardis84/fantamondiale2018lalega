package b;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public class PrintJasper {

	static String pathSuper = Utils.getResourceUrl("reports").getPath();
	static URL pathUrl = Utils.getResourceUrl("reports");
	
	public static File getPdfReport(String[] reportNames, Hashtable<String, Object> parameters, JRDataSource beanArrayDataSource) {
		File pdf = null;
		String reportDir = pathSuper+"/";
		String path = pathSuper+"/print/";
		pdf = new File(path);
		pdf.mkdirs();
		
		Logs.write(reportDir);
		
		try{
			String reportTemplate = "", jasper="";
			JasperDesign jasperDesign = null;
			
			if(parameters == null) parameters = new Hashtable<String, Object>();
			
			for(int k=0; k<reportNames.length;k++){
				if(k==0)
					path+=reportNames[k]+".pdf";
				reportTemplate = (reportDir+reportNames[k]+".jrxml");
				jasper = (reportDir+reportNames[k]+".jasper");
				//caricamento file JRXML
				jasperDesign = JRXmlLoader.load(reportTemplate);
				//compilazione del file e generazione del file JASPER
				JasperCompileManager.compileReportToFile(jasperDesign, jasper);
			}
			
			parameters.put("ReportTitle", "Address Report");
			parameters.put("BaseDir", new File(reportTemplate).getParentFile());
			
			
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasper, parameters, beanArrayDataSource);

			byte[] pagina=JasperExportManager.exportReportToPdf(jasperPrint);
			
			pdf = new File(path);
			FileOutputStream fos = new FileOutputStream(pdf);
			fos.write( pagina);
			fos.flush();
			fos.close();
		}catch(Exception e){
			System.out.println("PrintJasper getPdfReport() "+ e);
			Logs.write(e.getMessage());
		}
		
		return pdf;
	}
	
	
	public static void openPdfViewer(String[] reportNames) {
		String path =  pathSuper+"/print/";
		String filename = path+reportNames[0]+".pdf";
		
		Logs.write(path);
		
		 File myFile = new File(filename);
	     try {
			Desktop.getDesktop().open(myFile);
		} catch (IOException e) {
			Logs.write(e.getMessage());
			e.printStackTrace();
		}
    }

}
