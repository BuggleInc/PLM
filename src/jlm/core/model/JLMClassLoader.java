package jlm.core.model;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

public class JLMClassLoader extends URLClassLoader {

	private static URLClassLoader instance;
	
	private JLMClassLoader(URL[] url) {
		super(url);
	}
	
	/**
	 * Gives an instance of the URLClassLoader allowing to dynamically 
	 * load from external JARs.
	 * The URLClassLoader is initialized with the content of the current classpath.
	 * @return URLClassloader to use for loading lessons from JARs
	 */
	public static URLClassLoader getInstance() {
		if (instance == null) {
			
			URL[] url = null;
			
			try {
				String classpath = System.getProperty("java.class.path");
				for(String path : Arrays.asList(classpath.split(":"))) {
					url = new URL[]{ new URL("file:"+path) };
					System.err.println(path);
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			} 
			
			instance = new JLMClassLoader(url);
		}
		
		return instance;
	}
	
	/**
	 * Adds a JAR to the classloader at runtime, if not already added.
	 * @param path Path to the JAR file
	 */
	public void addJar(String path) {
		File file = new File(path);
		try {
			URL url = file.toURI().toURL();
			if (!Arrays.asList(super.getURLs()).contains(url))
				super.addURL(url);
		} 
		catch (MalformedURLException e) {
			System.err.println("Adresse du JAR incorrecte : " + path);
			e.printStackTrace();
		}

	}
	
	
	/* http://tech.puredanger.com/2006/11/09/classloader/ */
	public Class<?> loadClass(String name) throws ClassNotFoundException {
        // First check whether it's already been loaded, if so use it
        Class loadedClass = findLoadedClass(name);

        // Not loaded, try to load it
        if (loadedClass == null) {
            try {
                // Ignore parent delegation and just try to load locally
                loadedClass = findClass(name);
            } catch (ClassNotFoundException e) {
                // Swallow exception - does not exist locally
            }

            // If not found locally, use normal parent delegation in URLClassloader
            if (loadedClass == null) {
                // throws ClassNotFoundException if not found in delegation hierarchy at all
                loadedClass = super.loadClass(name);
            }
        }
        // will never return null (ClassNotFoundException will be thrown)
        return loadedClass;
    }

	
}
