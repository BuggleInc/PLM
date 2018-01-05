package plm.utils;

import plm.core.lang.ProgrammingLanguage;

public class HtmlUtils {

	/**
	 * Filters out the part that should not be displayed in the current programming language
	 * <p>
	 * If current language is java, then we will:
	 * - keep parts such as [!java] ... [/!]  (that is, replace the whole group by what's within the tag)
	 * - remove parts such as [!python] ... [/!]
	 * - remove parts such as [!scala] ... [/!]
	 * <p>
	 * - keep parts such as [!java|python] ... [/!]
	 * - keep parts such as [!python|java] ... [/!]
	 * - keep parts such as [!java|scala] ... [/!]
	 * - keep parts such as [!scala|java] ... [/!]
	 * - remove parts such as [!scala|python] ... [/!]
	 * - remove parts such as [!python|scala] ... [/!]
	 * <p>
	 * Other automagic conversions:
	 * [!thelang] displays the name of the current programming language.
	 */

	public static String filter(String in, ProgrammingLanguage targetLang) {
		String res = in.replaceAll("\\[!thelang/?\\]", "[!java]Java[/!][!python]python[/!][!scala]Scala[/!][!c]C[/!]");

		String[] existingProgLang = new String[] {"java", "scala", "python", "c"};
		String targetLangStr = targetLang.getLang().toLowerCase();

		String strtemp = "";

		while (!strtemp.equals(res)) {
			strtemp = res.toString();

			// Process any block with one language first so that they can be nested in blocks with more than one language.
			for (String l : existingProgLang) {
				if (l.equals(targetLangStr))
					res = res.replaceAll("(?s)\\[!" + l + "\\](.*?)\\[/!\\]", "$1"); // Keep it
				else
					res = res.replaceAll("(?s)\\[!" + l + "\\](.*?)\\[/!\\]", ""); // Not for us
			}

			for (String l : existingProgLang) {
				for (String l2 : existingProgLang) {
					if (!l2.equals(l)) {
						if (l.equals(targetLangStr) || l2.equals(targetLangStr))
							res = res.replaceAll("(?s)\\[!" + l + "\\|" + l2 + "\\](.*?)\\[/!\\]", "$1"); // Keep it
						else
							res = res.replaceAll("(?s)\\[!" + l + "\\|" + l2 + "\\](.*?)\\[/!\\]", ""); // Not for us
					}
				}
			}

			for (String l : existingProgLang) {
				for (String l2 : existingProgLang) {
					if (!l2.equals(l)) {
						for (String l3 : existingProgLang) {
							if (!l3.equals(l) && !l3.equals(l2)) {
								if (l.equals(targetLangStr) || l2.equals(targetLangStr) || l3.equals(targetLangStr))
									res = res.replaceAll("(?s)\\[!" + l + "\\|" + l2 + "\\|" + l3 + "\\](.*?)\\[/!\\]", "$1");
								else
									res = res.replaceAll("(?s)\\[!" + l + "\\|" + l2 + "\\|" + l3 + "\\](.*?)\\[/!\\]", "");
							}
						}
					}
				}
			}

			for (String l : existingProgLang) {
				for (String l2 : existingProgLang) {
					if (!l2.equals(l)) {
						for (String l3 : existingProgLang) {
							if (!l3.equals(l) && !l3.equals(l2)) {
								for (String l4 : existingProgLang) {
									if (!l4.equals(l) && !l4.equals(l2) && !l4.equals(l3)) {
										if (l.equals(targetLangStr) || l2.equals(targetLangStr) || l3.equals(targetLangStr) || l4.equals(targetLangStr))
											res = res.replaceAll("(?s)\\[!" + l + "\\|" + l2 + "\\|" + l3 + "\\|" + l4 + "\\](.*?)\\[/!\\]", "$1");
										else
											res = res.replaceAll("(?s)\\[!" + l + "\\|" + l2 + "\\|" + l3 + "\\|" + l4 + "\\](.*?)\\[/!\\]", "");
									}
								}
							}
						}
					}
				}
			}
		}

		return res;
	}
}
