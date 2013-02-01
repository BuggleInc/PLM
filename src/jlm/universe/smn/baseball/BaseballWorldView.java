package jlm.universe.smn.baseball;

import javax.swing.ImageIcon;

import jlm.core.ui.ResourcesCache;
import jlm.core.ui.WorldView;
import jlm.universe.World;

public class BaseballWorldView extends WorldView
{
	public BaseballWorldView(World w)
	{
		super(w);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public ImageIcon getIcon()
	{
		return ResourcesCache.getIcon("resources/IconWorld/baseball.png");
	}

}
