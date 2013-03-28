package jlm.core.ui.action;

import jlm.core.model.Game;
import jlm.core.model.HelpAppEngine;
import jlm.core.model.HelpServer;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Class that handle clicks on HELP button
 * It sends a request to the JLM server
 */
public class HelpMe extends AbstractGameAction {
	private static final long serialVersionUID = 1L;

    private HelpServer helpServer;

    public HelpMe(Game game, String text, ImageIcon icon) {
        super(game, text, icon);
        helpServer = new HelpAppEngine();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        helpServer.switchStatus((JToggleButton)e.getSource());
        helpServer.requestHelp();
    }

}
