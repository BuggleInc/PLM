package jlm.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import jlm.lesson.ExerciseTemplated;

public class Reader {
	private static String locale;
	
	public static void setLocale(String lang) {
		locale=lang;
	}
	public static BufferedReader fileReader(String file,String extension,boolean translatable) {
		if (translatable) {
			if (locale==null) 
				throw new RuntimeException("locale is null");
				
			BufferedReader br =  fileReader(file.replace('.','/')+"."+locale+(extension!=null?"."+extension:""));
			if (br != null)
				return br;
		}
		BufferedReader br = fileReader(file.replace('.','/')+(extension!=null?"."+extension:""));
		return br;
	}	
	static protected BufferedReader fileReader(String filename) {
		BufferedReader br = null;
		/* try to find the file */
		try {
			br = new BufferedReader(new FileReader(new File(filename)));
		} catch (FileNotFoundException e) {
			// external HTML file of this exercise not found on file system. Give as resource, in case we are in a jar file
			String resourceName = "/"+filename;
			resourceName = resourceName.replace('\\', '/'); /* just in case we're passed a windows path */

			InputStream s = ExerciseTemplated.class.getResourceAsStream(resourceName);
			if (s == null) {
				return null;	/* file not found, give up */
			}

			try {
				br = new BufferedReader(new InputStreamReader(s,"UTF-8"));
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
				System.err.println("File encoding of "+filename+
						" is not supported on this platform (please report this bug)");
				return null;
			}
		}
		return br;
	}	
	public static StringBuffer fileToStringBuffer(String file, String extension, boolean translatable) {
		BufferedReader br = fileReader(file,extension,translatable);
		String newLine = System.getProperty("line.separator");
		if (br==null) 
			return null;
		
		StringBuffer sb = new StringBuffer();
		try {
			String s;
			s = br.readLine();
			while (s != null) {
				sb.append(s);
				sb.append(newLine);
				s = br.readLine();
			}
			
		} catch (IOException e) {
			e.printStackTrace();			
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb;
	}
}
