package jlm.ui;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class TipsDialog extends AbstractAboutDialog {

	public TipsDialog(JFrame parent) {
		super(parent);
		currentExerciseHasChanged();
	}

	@Override
	public void currentExerciseHasChanged() {
		setTitle("Tips");
		this.area.setText("no tips");
		this.area.setCaretPosition(0);
	}
	
	public void setText(String txt) {
		this.area.setText(txt);
		this.area.setCaretPosition(0);
	}

}
