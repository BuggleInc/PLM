package plm.core.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import plm.core.model.Game;
import plm.core.model.HelpAppEngine;
import plm.core.model.HelpServer;
import plm.core.ui.ResourcesCache;
import plm.core.utils.FileUtils;

/**
 * Class that handle clicks on HELP button
 * It sends a request to the PLM server
 */
public class HelpMe extends AbstractGameAction {
	private static final long serialVersionUID = 1L;
	private I18n i18n = I18nFactory.getI18n(getClass(),"org.plm.i18n.Messages",FileUtils.getLocale(), I18nFactory.FALLBACK);

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
        ((JToggleButton)e.getSource()).setIcon(ResourcesCache.getIcon("img/btn-alert-"+(isRequestingHelp?"on":"off")+".png"));
    }

}
