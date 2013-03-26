package jlm.core;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import jlm.core.model.Game;


public class JLMClassLoader {
	
	private static boolean inAJAR = false;
	private static ArrayList<String> usedJARs = new ArrayList<String>();
	
	/**
	 * Adds a JAR to the classloader at runtime, if not already added.
	 * If the system classloader is not an URLClassLoader (either defined by the JVM,
	 * or explicitely defined at the start), the method will fail, throwing an exception. 
	 * 
	 * @param path Path to the JAR file
	 * @throws RuntimeException  
	 * @throws MalformedURLException 
	 */
	public static void addJar(File jar) throws IOException {
		
		if (!jar.exists())
			throw new IOException("JAR file doesn't exist!");
		
		JarFile jarFile = new JarFile(jar);
		
		// Check if the JAR has already been added. If not, load it in the classloader.
		if (!usedJARs.contains(jar.getAbsolutePath())) {	
			URL u = jar.toURI().toURL();
	        URLClassLoader sysloader = (URLClassLoader)ClassLoader.getSystemClassLoader();
	        Class<URLClassLoader> sysclass = URLClassLoader.class;
	        
	        try {
	            Method method = sysclass.getDeclaredMethod("addURL",new Class[]{URL.class});
	            method.setAccessible(true);
	            method.invoke(sysloader,new Object[]{ u });
	        } catch (Throwable t) {
	            t.printStackTrace();
	            try {
					throw new IOException("Error, could not add a JAR to the JLM. (Can't add URL to the system classloader)");
				} catch (IOException e) {
					e.printStackTrace();
				}
	        }//end try catch
	        
	        usedJARs.add(jar.getAbsolutePath());
		}
        
		// Load the JAR manifest file, and the name of the lesson package which should be inside it.
		Manifest manifest = jarFile.getManifest();
		
		String lessonPackage = manifest.getMainAttributes().getValue("LessonPackage");
		if (lessonPackage == null)
			throw new IOException("Could not find the attribute LessonPackage in the JAR manifest.");
		
		inAJAR = true;
		
		Game.getInstance().loadLesson("lessons." + lessonPackage);
    }//end method

	
	
	public static boolean isInAJAR() {
		return inAJAR;
	}

	public static void setInAJAR(boolean inAJAR) {
		JLMClassLoader.inAJAR = inAJAR;
	}
}
