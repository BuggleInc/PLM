package jlm.core.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import jlm.core.model.Course;
import jlm.core.model.Game;


public class CourseChangeDialog extends JDialog {

	private static final long serialVersionUID = 2234402839093122248L;
	
	protected ArrayList<String> courseListIDs;
	protected JList jListID = null;
	
	protected JButton OKButton;
	
	

	public CourseChangeDialog() {
        super(MainFrame.getInstance(), "JLM Course", false);

        initComponent(this.getContentPane());

		setMinimumSize(new Dimension(300, 400));
        pack();

		setLocationRelativeTo(getParent());
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
    }

    public void initComponent(Container c) {
        c.setLayout(new BorderLayout());
        c.add(new JLabel("Please select your course:"), BorderLayout.NORTH);
        
        
        
        // OK/Cancel button
	        OKButton = new JButton("OK");
	        OKButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					System.out.println(jListID.getSelectedValue());
					MainFrame.getInstance().appendToTitle("[ " + jListID.getSelectedValue() + " ]");
					setVisible(false);
				}
			});
	        
	        JButton cancelButton = new JButton("Cancel");
	        cancelButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					setVisible(false);
				}
			});
	        
	        JPanel bottomButtons = new JPanel();
	        bottomButtons.setLayout(new FlowLayout());
	        bottomButtons.add(cancelButton);
	        bottomButtons.add(OKButton);
	    
	    c.add(bottomButtons, BorderLayout.SOUTH);
	    
	    
	    
        // Load the list of availables "courses", or a message to say nope.
	    
        Game.getInstance().getCurrentCourse();
        Course course = new Course();
        
        try {
			courseListIDs = course.getAllCoursesId();

	        if (courseListIDs.size() == 0) {
	        	c.add(new JLabel("No course currently opened, sorry.", JLabel.CENTER), BorderLayout.CENTER);
	        	OKButton.setEnabled(false);
	        }
	        else {
	        	jListID = new JList(courseListIDs.toArray());
	        	c.add(jListID, BorderLayout.CENTER);
	        }
			
		} catch (IOException e) {
			c.add(new JLabel("Course server currently unavailable.", JLabel.CENTER), BorderLayout.CENTER);
			OKButton.setEnabled(false);
		}
	    
    }
}
