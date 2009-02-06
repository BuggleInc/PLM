package jlm.ui;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Hashtable;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class ResourcesCache {
	private static Hashtable<String, ImageIcon> iconsCache = new Hashtable<String, ImageIcon>();

	private static ImageIcon[] busyIcons;

	public static void loadBusyIconAnimation() {
		busyIcons = new ImageIcon[15];
		for (int i=0 ; i<15; i++) {
			//busyIcons[i] = new ImageIcon("resources/busyicons/busy-icon"+i+".png");
			busyIcons[i] = new ImageIcon(ResourcesCache.class.getClassLoader().getResource("resources/busyicons/busy-icon"+i+".png"));
		}
	}
	
	
	public static ImageIcon getIcon(String path) throws FileNotFoundException {
		if (!iconsCache.containsKey(path)) {
			//iconsCache.put(path, new ImageIcon(path));
			URL url = ResourcesCache.class.getClassLoader().getResource(path);
			if (url == null) {
				throw new FileNotFoundException("Cannot find path "+path+": classloader returned null.");
			}
			ImageIcon img = new ImageIcon(url);
			iconsCache.put(path, img);
		}
		return iconsCache.get(path);
	}


	public static int getBusyIconsSize() {
		return ResourcesCache.busyIcons.length;
	}


	public static Icon getBusyIcons(int busyIconIndex) {
		return ResourcesCache.busyIcons[busyIconIndex];
	}
	
}
