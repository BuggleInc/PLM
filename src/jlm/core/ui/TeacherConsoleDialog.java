package jlm.core.ui;

import javax.swing.*;
import java.awt.*;

public class TeacherConsoleDialog extends JDialog{

    public TeacherConsoleDialog() {
        super(MainFrame.getInstance(), "The JLM Teacher Console", false);

        initComponent();
    }

    public void initComponent(){
        setLayout(new BorderLayout());

        pack();
		setMinimumSize(new Dimension(500, 300));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(true);

		setLocationRelativeTo(getParent());

    }
}
