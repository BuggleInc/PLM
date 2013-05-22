package jlm.core.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

import jlm.core.model.FileUtils;
import jlm.core.model.Game;
import jlm.core.model.HelpAppEngine;
import jlm.core.model.HelpServer;

import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

/**
 * Class that handle clicks on HELP button
 * It sends a request to the JLM server
 */
public class HelpMe extends AbstractGameAction {
	private static final long serialVersionUID = 1L;
	private I18n i18n = I18nFactory.getI18n(getClass(),"org.jlm.i18n.Messages",FileUtils.getLocale(), I18nFactory.FALLBACK);

    private HelpServer helpServer;
    private boolean isRequestingHelp = false;

    public HelpMe(Game game, String text, ImageIcon icon) {
        super(game, text, icon);
        helpServer = new HelpAppEngine();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    	isRequestingHelp = ! isRequestingHelp;
        helpServer.setStatus(isRequestingHelp);
        ((JToggleButton)e.getSource()).setText(isRequestingHelp ? i18n.tr("Cancel call") : i18n.tr("Call for Help"));
    }

}
