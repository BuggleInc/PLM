package jlm.core.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import jlm.core.model.lesson.ExerciseTemplated;

/** This class is in charge of loading the resources from disk into memory
 * 
 * 	It solves 2 main difficulties. The first one is to find the files in any case, be 
 *  them in the distributed jar file, or on the disk (as it happens when we develop JLM: 
 *  we don't build a jar for each run, we directly from our source tree). It also deals 
 *  with the windows/unix incompatibilities about directory separators (/ or \).
 * 
 *  The second problem it deals with is about translations. When looking for a help file, 
 *  it first seach for a suitable translated version. If not found, it fallbacks to the 
 *  english version. This is done through the locale static variable. Yeah, that's not 
 *  clean but it just works.
 */
public class Reader {
	private static String locale;
	
	/** Specifies the locale that we have to use when looking for translated files */
	public static void setLocale(String lang) {
		locale=lang;
	}
	public static String getLocale() {
		return locale;
	}
	public static BufferedReader fileReader(String file,String extension,boolean translatable) {
		if (translatable) {
			if (locale==null) 
				throw new RuntimeException("locale is null: you cannot request for translated material (yet)");
				
			BufferedReader br =  fileReader(file.replace('.','/')+"."+locale+(extension!=null?"."+extension:""));
			if (br != null)
				return br;
			br =  fileReader("lib/"+file.replace('.','/')+"."+locale+(extension!=null?"."+extension:""));
			if (br != null)
				return br;
			br =  fileReader("src/"+file.replace('.','/')+"."+locale+(extension!=null?"."+extension:""));
			if (br != null)
				return br;
			
		}
		BufferedReader br = fileReader(file.replace('.','/')+(extension!=null?"."+extension:""));
		if (br != null)
			return br;
		br = fileReader("lib/"+file.replace('.','/')+(extension!=null?"."+extension:""));
		if (br != null)
			return br;
		br = fileReader("src/"+file.replace('.','/')+(extension!=null?"."+extension:""));
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
