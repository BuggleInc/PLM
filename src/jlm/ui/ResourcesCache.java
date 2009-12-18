package jlm.ui;

import java.net.URL;
import java.util.Hashtable;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIManager;

import jlm.core.Logger;

public class ResourcesCache {
	private static Hashtable<String, ImageIcon> iconsCache = new Hashtable<String, ImageIcon>();

	private static ImageIcon[] busyIcons;

	public static void loadBusyIconAnimation() {
		busyIcons = new ImageIcon[15];
		for (int i=0 ; i<15; i++) {
			URL url = ResourcesCache.class.getClassLoader().getResource("resources/busyicons/busy-icon"+i+".png");
			if (url == null) {
				busyIcons[i] =  new ImageIcon();
			} else {
				busyIcons[i] = new ImageIcon(url);
			}
		}
	}
	
	private static Boolean warnedAboutBrokenPath = false;
	/**
	 * Lazy loading of ImageIcon resources.
	 * @param path of the image resource to be loaded. 
	 * @return the ImageIcon or a blank ImageIcon when resource is not found.
	 */
	public static ImageIcon getIcon(String path) {
		if (!iconsCache.containsKey(path)) {
			URL url = ResourcesCache.class.getClassLoader().getResource(path);
			if (url == null) {
				if (!warnedAboutBrokenPath) {
					Logger.log("jlm.ui.ResourcesCache.getIcon()", "Cannot find path "+path+": classloader returned null.");
					warnedAboutBrokenPath = true;
				}
				ImageIcon c = (ImageIcon) UIManager.getLookAndFeelDefaults().get("html.missingImage");
				iconsCache.put(path, c);
			} else {
				ImageIcon img = new ImageIcon(url);
				iconsCache.put(path, img);
			}
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
