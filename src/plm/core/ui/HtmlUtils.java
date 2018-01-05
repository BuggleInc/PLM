package plm.core.ui;

import java.util.HashMap;
import java.util.Map;

import plm.core.lang.ProgrammingLanguage;


public class HtmlUtils {

    private static Map<String, String> langColors = null;

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

    public static String filterHTML(String in, boolean showAll, ProgrammingLanguage language) {
        return filterHTML(in, showAll, false, null, language);
    }

    private static String filterHTML(String in, boolean showAll, boolean showMulti, ProgrammingLanguage[] currLang, ProgrammingLanguage targetLang) {
        if (langColors == null) {
            langColors = new HashMap<String, String>();
            langColors.put("java", "FF0000");
            langColors.put("python", "008000");
            langColors.put("scala", "0000FF");
            langColors.put("c", "00FF00");

            langColors.put("java|python", "FF8000");
            langColors.put("java|scala", "FF00FF");
            langColors.put("java|c", "FFFF00");
            langColors.put("python|java", "FF8000");
            langColors.put("python|scala", "0080FF");
            langColors.put("python|c", "00AF00");
            langColors.put("scala|java", "FF00FF");
            langColors.put("scala|python", "0080FF");
            langColors.put("scala|c", "00FFFF");

            // scala python c
            langColors.put("scala|python|c", "00AFFF");
            langColors.put("scala|c|python", "00AFFF");
            langColors.put("python|c|scala", "00AFFF");
            langColors.put("python|scala|c", "00AFFF");
            langColors.put("c|python|scala", "00AFFF");
            langColors.put("c|scala|python", "00AFFF");

            //scala python java
            langColors.put("scala|python|java", "FF80FF");
            langColors.put("scala|java|python", "FF80FF");
            langColors.put("python|java|scala", "FF80FF");
            langColors.put("python|scala|java", "FF80FF");
            langColors.put("java|python|scala", "FF80FF");
            langColors.put("java|scala|python", "FF80FF");

            //scala c java
            langColors.put("scala|c|java", "EEEEEE");
            langColors.put("scala|java|c", "EEEEEE");
            langColors.put("c|java|scala", "EEEEEE");
            langColors.put("c|scala|java", "EEEEEE");
            langColors.put("java|c|scala", "EEEEEE");
            langColors.put("java|scala|c", "EEEEEE");

            //python java c
            langColors.put("python|c|java", "333333");
            langColors.put("python|java|c", "333333");
            langColors.put("c|java|python", "333333");
            langColors.put("c|python|java", "333333");
            langColors.put("java|python|c", "333333");
            langColors.put("java|c|python", "333333");
        }
        String res = in.replaceAll("\\[!thelang/?\\]", "[!java]Java[/!][!python]python[/!][!scala]Scala[/!][!c]C[/!]");

        String[] existingProgLang = new String[] {"java", "scala", "python", "c"};
        String targetLangStr = targetLang.getLang().toLowerCase();
        
		/* Display everything when in debug mode, with shiny colors */
        if (showAll) {
            // Process any block with one language first so that they can be nested in blocks with more than one language.
            for (String l : existingProgLang) {
                res = res.replaceAll("(?s)\\[!" + l + "\\](.*?)\\[/!\\]",
                        "<font color=\"" + langColors.get(l) + "\">$1</font>");
            }
            for (String l : existingProgLang) {
                for (String l2 : existingProgLang) {
                    if (!l2.equals(l)) {
                        res = res.replaceAll("(?s)\\[!" + l + "\\|" + l2 + "\\](.*?)\\[/!\\]", "<font color=\"" + langColors.get(l + "|" + l2) + "\">$1</font>");
                    }
                }
            }

            for (String l : existingProgLang) {
                for (String l2 : existingProgLang) {
                    if (!l2.equals(l)) {
                        for (String l3 : existingProgLang) {
                            if (!l3.equals(l) && !l3.equals(l2)) {
                                res = res.replaceAll("(?s)\\[!" + l + "\\|" + l2 + "\\|" + l3 + "\\](.*?)\\[/!\\]", "<font color=\"" + langColors.get(l + "|" + l2 + "|" + l3) + "\">$1</font>");
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
                                    	res = res.replaceAll("(?s)\\[!" + l + "\\|" + l2 + "\\|" + l3 + "\\|" + l4 + "\\](.*?)\\[/!\\]", "<font color=\"" + langColors.get(l + "|" + l2 + "|" + l3 + "|" + l4) + "\">$1</font>");
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return res;
        }

		/* if showMulti, all of selected language will be appear on editor, but in the same color */
        //FIXME add colors
        if (showMulti) {
            String replace = "<font color=\"FF0000\">$7</font>";

            String strtemp = "";
            String cls[] = new String[currLang.length];
            for (int i = 0; i < currLang.length; i++) {
                cls[i] = currLang[i].getLang().toLowerCase();
            }
            while (!strtemp.equals(res)) {
                strtemp = res.toString();
                for (String cl : cls) {
                    res = res.replaceAll("(?s)\\[!" + cl + "\\](.*?)\\[/!\\]", "\\[!#\\]<font color=\"FF0000\">$1</font>\\[/!\\]");
                    res = res.replaceAll("(?s)\\[!((([a-z]*\\|)?)?)" + cl + "(((\\|[a-z]*)?)?)\\](.*?)\\[/!\\]", "\\[!#\\]" + replace + "\\[/!\\]");
                    res = res.replaceAll("(?s)\\[!((([a-z]*\\|)*)?)" + cl + "(((\\|[a-z]*)*)?)\\](.*?)\\[/!\\]", "\\[!#\\]" + replace + "\\[/!\\]");
                }

                res = res.replaceAll("(?s)\\[![a-z]*\\](.*?)\\[/!\\]", "");
                res = res.replaceAll("(?s)\\[!((([a-z]*\\|)?)?)" + "[a-z]*" + "(((\\|[a-z]*)?)?)\\](.*?)\\[/!\\]", "");
                res = res.replaceAll("(?s)\\[!((([a-z]*\\|)*)?)" + "[a-z]*" + "(((\\|[a-z]*)*)?)\\](.*?)\\[/!\\]", "");
                res = res.replaceAll("(?s)\\[![a-z]*\\](.*?)\\[/!\\]", "");
                res = res.replaceAll("(?s)\\[!#\\](.*?)\\[/!\\]", "$1");

            }
        } else {


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
        }

        return res;
    }
}
