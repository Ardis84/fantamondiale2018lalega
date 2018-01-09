package b;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

public class Logs {

	
	public static void write(String str) {
		try {
			File f = new File("logBacheca.txt");
			if(!f.exists())
				f.createNewFile();
			
			Writer writer = new BufferedWriter(new OutputStreamWriter(
			        new FileOutputStream(f, true), "UTF-8"));
			
			writer.append("-- "+str);
			writer.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
