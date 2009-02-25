package lessons.lightbot;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import jlm.ui.WorldView;
import jlm.universe.World;

public class LightBotWorldView extends WorldView {
	
	private static final long serialVersionUID = -3538528173123507782L;

	private LightBotWorldViewIsometric isometricView;
	
	public LightBotWorldView(World w) {
		super(w);
		initComponents();
	}
	
	public void initComponents() {
		JButton rotateLeft = new JButton("left");
		rotateLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				getWorld().rotateLeft();
			}
		});
	
		JButton rotateRight = new JButton("right");
		rotateRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				getWorld().rotateRight();
			}
		});
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridLayout(1,2));
		buttonsPanel.add(rotateLeft);
		buttonsPanel.add(rotateRight);
		
		isometricView = new LightBotWorldViewIsometric(this.world);

		setLayout(new BorderLayout());
		add(BorderLayout.SOUTH, buttonsPanel);
		add(BorderLayout.CENTER, isometricView);
	}

	private LightBotWorld getWorld() {
		return (LightBotWorld) this.world;
	}

	
	@Override
	public void worldHasMoved() {
		this.isometricView.setWorld(this.world);
		super.worldHasMoved();
	}
	
	@Override
	public boolean isWorldCompatible(World world) {
		return world instanceof LightBotWorld;
	}
}
