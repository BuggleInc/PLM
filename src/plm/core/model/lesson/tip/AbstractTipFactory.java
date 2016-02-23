package plm.core.model.lesson.tip;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import plm.core.model.I18nManager;
import plm.core.model.lesson.Exercise;

public abstract class AbstractTipFactory {

	public String getDefaultLabel(Locale humanLanguage) {
		return I18nManager.getI18n(humanLanguage).tr("Show Tip");
	}

	public String getDefaultTipTemplate() {
		return "<div class=\"tip\" id=\"(tip-\\d+?)\">(.*?)</div>";
	}

	public String getTipWithLabelTemplate() {
		return "<div class=\"tip\" id=\"(tip-\\d+?)\" alt=\"([^\"]+?)\">(.*?)</div>";
	}

	public abstract String getDefaultTipReplacement(Locale humanLanguage);
	public abstract String getTipWithLabelReplacement();

	public String computeTips(Exercise exo, Locale humanLanguage, String mission) {
		/* prepare the tips, if any */
		String res = mission;
		Pattern p3 =  Pattern.compile(getTipWithLabelTemplate(), Pattern.MULTILINE | Pattern.DOTALL);
		Matcher m3 = p3.matcher(res);
		while (m3.find()) {
			String id = m3.group(1);
			String content = m3.group(3);
			exo.addTips(humanLanguage.getLanguage(), id, content);
		}
		res = m3.replaceAll(getTipWithLabelReplacement());

		Pattern p4 =  Pattern.compile(getDefaultTipTemplate(), Pattern.MULTILINE | Pattern.DOTALL);
		Matcher m4 = p4.matcher(res);
		while (m4.find()) {
			String id = m4.group(1);
			String content = m4.group(2);
			exo.addTips(humanLanguage.getLanguage(), id, content);
		}
		res = m4.replaceAll(getDefaultTipReplacement(humanLanguage));
		return res;
	}
}
