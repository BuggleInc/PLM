package plm.core.model.lesson.tip;

import java.util.Locale;

public class DefaultTipFactory extends AbstractTipFactory {

	@Override
	public String getDefaultTipReplacement(Locale humanLanguage) {
		return "<a href=\"#$1\">" + getDefaultLabel(humanLanguage) + "</a>";
	}

	@Override
	public String getTipWithLabelReplacement() {
		return "<a href=\"#$1\">$2</a>";
	}

}
