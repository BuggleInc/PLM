package plm.core.utils;

import plm.core.model.lesson.ExerciseTemplated;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

/**
 * This class is in charge of loading the resources from disk into memory
 * 
 * It solves 2 main difficulties. The first one is to find the files in any case, be
 * them in the distributed jar file, or on the disk (as it happens when we develop PLM:
 * we don't build a jar for each run, we directly from our source tree). It also deals
 * with the windows/unix incompatibilities about directory separators (/ or \).
 * 
 * The second problem it deals with is about translations. When looking for a help file,
 * it first search for a suitable translated version. If not found, it fallbacks to the
 * English version. This is done through the locale static variable. Yeah, that's not
 * clean but it just works.
 */
public class FileUtils {
	public static BufferedReader newFileReader(String file, Locale locale, String extension, boolean translatable)
			throws FileNotFoundException, UnsupportedEncodingException {
		if (translatable && locale == null) {
				throw new RuntimeException("locale is null: you cannot request for translated material (yet)");
		}
		/* Build the list of filenames we will iterate (translated if any, and raw) */
		String[] fileNames;
		if (translatable && ! locale.getLanguage().equals("en")) {
			String lang = locale.getLanguage();
			if (lang.equals("pt_br"))
				lang = "pt_BR"; // Damn stupid java. Could you please stick to ISO norms and put the country uppercase?
			
			fileNames = new String[] {
					file.replace('.', '/') + "." + lang,
					file.replace('.', '/')
			};
		} else { // not translatable, or currently in English: only search for non-translated form
				fileNames = new String[] {
						file.replace('.', '/')
				};
		}
		/* Do that iteration */
		for (String fileName : fileNames) {
			/* change class name to directories */
			fileName = fileName + (extension != null ? "." + extension : "");

			// external HTML file of this exercise not found on file system. Try as resource, in case we are in a jar file
			String resourceName =  "/" + fileName;
        	resourceName = resourceName.replace('\\', '/'); /* just in case we were passed a windows path */

        	InputStream s = ExerciseTemplated.class.getResourceAsStream(resourceName);
        	if (s == null) 
        		continue; // test next name in the list

        	try {
        		return new BufferedReader(new InputStreamReader(s, "UTF-8"));
        	} catch (UnsupportedEncodingException e) {
        		e.printStackTrace();
        		System.err.println("File encoding of " + fileName + " is not supported on this platform (please report this bug)");
        		throw e;
        	}
        }
		// file not found, give up. No logs here, as it is ok that some entities do not exist in some languages
		throw new FileNotFoundException(
		    (extension == null)
				? file + " (without extension) could not be found."
				: file + " with extension " + extension + " could not be found.");
	}	
	
	public static StringBuffer readContentAsText(String file, Locale locale, String extension, boolean translatable)
			throws FileNotFoundException, UnsupportedEncodingException {
		BufferedReader br = FileUtils.newFileReader(file, locale, extension, translatable);
		String newLine = System.getProperty("line.separator");
		
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
