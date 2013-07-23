package jlm.core.ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Hashtable;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIManager;

import jlm.core.model.Game;
import jlm.core.model.Logger;
import jlm.core.model.ProgrammingLanguage;
import jlm.core.model.lesson.Exercise;

public class ResourcesCache {
	private static Hashtable<String, ImageIcon> iconsCache = new Hashtable<String, ImageIcon>();

	private static ImageIcon[] busyIcons;

	public static void loadBusyIconAnimation() {
		busyIcons = new ImageIcon[30];
		for (int i=0 ; i<30; i++) {
			URL url = ResourcesCache.class.getClassLoader().getResource("img/busyicon/anim-"+(i+1)+".png");
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
		return getIcon(path, false);
	}
	/**
	 * Lazy loading of ImageIcon resources.
	 * @param path of the image resource to be loaded.
	 * @param okNull : whether it's ok to return null (useful to search for several paths) 
	 * @return the ImageIcon or a blank ImageIcon when resource is not found.
	 */
	public static ImageIcon getIcon(String path, boolean okNull) {
		if (!iconsCache.containsKey(path)) {
			URL url = ResourcesCache.class.getClassLoader().getResource(path);
			if (url == null) {
				if (okNull) 
					return null;
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

	public static ImageIcon getIcon(Object basePath, String path) {
		String name = basePath.getClass().getPackage().getName().replaceAll("\\.", "/") 
		        +"/"+path;
		return getIcon(name);
	}

	public static int getBusyIconsSize() {
		return ResourcesCache.busyIcons.length;
	}


	public static Icon getBusyIcons(int busyIconIndex) {
		return ResourcesCache.busyIcons[busyIconIndex];
	}

	public static ImageIcon getStarredIcon(ImageIcon icon, Exercise exo) {
		String path = exo.getWorld(0).getView().getClass().getCanonicalName();
		for (ProgrammingLanguage lang : exo.getProgLanguages()) {
			if (Game.getInstance().studentWork.getPassed(exo, lang))
				path += "_"+lang.getLang()+"ok";
			else 
				path += "_"+lang.getLang()+"nok";
		}
		if (!iconsCache.containsKey(path)) {
			BufferedImage combined;
			if (exo.getProgLanguages().contains(Game.LIGHTBOT)) {
				combined = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
				Graphics g = combined.getGraphics();
				g.drawImage(icon.getImage(), 0, 0, null);
				
				ImageIcon star = getIcon("resources/star.png");
				ImageIcon starNo = getIcon("resources/star_white.png");
				if (Game.getInstance().studentWork.getPassed(exo, Game.LIGHTBOT))  
					g.drawImage(star.getImage(), 0, 0, null);
				else 
					g.drawImage(starNo.getImage(), 0, 0, null);
				
			} else {
				combined = new BufferedImage(icon.getIconWidth()+10, icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
				Graphics g = combined.getGraphics();
				g.drawImage(icon.getImage(), 0, 0, null);
				
				if (Game.getInstance().studentWork.getPassed(exo, Game.JAVA)) { 
					g.drawImage(Game.JAVA.getIcon().getImage(), 26, 0, null);
				} else {
					//g.drawImage(getIcon("img/lang_java_no.png").getImage(), 26,0,null);
				}
				if (Game.getInstance().studentWork.getPassed(exo, Game.PYTHON)) { 
					g.drawImage(Game.PYTHON.getIcon().getImage(), 26, 16, null);
				} else {
					//	g.drawImage(getIcon("img/lang_python_no.png").getImage(), 26,16,null);
				}
				
			}
			
			iconsCache.put(path, new ImageIcon(combined));
		}
		return iconsCache.get(path);
	}
	
}
