package jlm.core.ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Hashtable;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIManager;

import jlm.core.model.Logger;

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
			if (url==null) { // give a try to another path, used in Debian packages
				url = ResourcesCache.class.getClassLoader().getResource("lib/"+path);
			}
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

	public static ImageIcon getCombinedIcon(String path1, String path2) {
		String path = path1+path2;
		if (!iconsCache.containsKey(path)) {

			ImageIcon img1 = getIcon(path1);			
			ImageIcon img2 = getIcon(path2);
			
			int w = Math.max(img1.getIconWidth(), img2.getIconWidth());
			int h = Math.max(img1.getIconHeight(), img2.getIconHeight());
			BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			Graphics g = combined.getGraphics();
			g.drawImage(img1.getImage(), 0, 0, null);
			g.drawImage(img2.getImage(), 0, 0, null);
			
			iconsCache.put(path, new ImageIcon(combined));
		}
		return iconsCache.get(path);
	}
	
	

	public static int getBusyIconsSize() {
		return ResourcesCache.busyIcons.length;
	}


	public static Icon getBusyIcons(int busyIconIndex) {
		return ResourcesCache.busyIcons[busyIconIndex];
	}

	public static ImageIcon getStarIcon(ImageIcon icon, String name) {
		String path = name+"-star";
		if (!iconsCache.containsKey(path)) {

			ImageIcon img1 = icon;			
			ImageIcon img2 = getIcon("resources/star.png");
			
			int w = Math.max(img1.getIconWidth(), img2.getIconWidth());
			int h = Math.max(img1.getIconHeight(), img2.getIconHeight());
			BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			Graphics g = combined.getGraphics();
			g.drawImage(img1.getImage(), 0, 0, null);
			g.drawImage(img2.getImage(), 0, 0, null);
			
			iconsCache.put(path, new ImageIcon(combined));
		}
		return iconsCache.get(path);
	}
	
}
