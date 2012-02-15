package jlm.core.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;


public class CourseChangeDialog extends JDialog {

	private static final long serialVersionUID = 2234402839093122248L;
	
	protected JList courseList;

	public CourseChangeDialog() {
        super(MainFrame.getInstance(), "JLM Course", false);

        initComponent();
    }

    public void initComponent() {
        setLayout(new BorderLayout());
        
        this.add(new JLabel("Pick up your course:"), BorderLayout.NORTH);
        this.add(new JList(), BorderLayout.CENTER);
        
        // OK/Cancel button
	        JButton OKButton = new JButton("OK");
	        OKButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					System.out.println(courseList.getSelectedValue());
					setVisible(false);
				}
			});
	        this.add(OKButton);
	        
	        JButton cancelButton = new JButton("Cancel");
	        cancelButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					setVisible(false);
				}
			});
	        this.add(cancelButton);
	        
	        JPanel bottomButtons = new JPanel();
	        bottomButtons.setLayout(new FlowLayout());
	        bottomButtons.add(cancelButton);
	        bottomButtons.add(OKButton);
	    
	    this.add(bottomButtons, BorderLayout.SOUTH);
	    
        
        pack();
		setMinimumSize(new Dimension(300, 400));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(true);

		setLocationRelativeTo(getParent());

    }
}
