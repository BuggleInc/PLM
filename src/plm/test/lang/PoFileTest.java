package plm.test.lang;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import plm.core.utils.PoFile;
import plm.core.utils.PoFile.Entry;

/** PoFile reads the entries as gettext writes them, with their comments for the translators, without the obsolete ones and the plural forms. */
public class PoFileTest {

  @Test public void testEntries(@TempDir Path dir) throws IOException
  {
    Path po = dir.resolve("fr.po");
    Files.writeString(po, """
msgid ""
msgstr ""
"Content-Type: text/plain; charset=UTF-8\\n"

#. A comment for the translators
#. on two lines
#: src/Foo.java:12
msgid ""
"A long text written "
"over several lines"
msgstr ""
"Un long texte écrit "
"sur plusieurs lignes"

#, fuzzy, java-format
msgid "Hello {0}"
msgstr "Bonjour {0}"

#, fuzzy
#~ msgid "Obsolete"
#~ msgstr "Obsolète"

msgid "One file"
msgid_plural "{0} files"
msgstr[0] "Un fichier"
msgstr[1] "{0} fichiers"

msgctxt "primitive"
msgid "left"
msgstr "gauche\"""");

    assertEquals(List.of(new Entry(null, "", "Content-Type: text/plain; charset=UTF-8\\n", false, ""),
                         new Entry(null, "A long text written over several lines", "Un long texte écrit sur plusieurs lignes", false,
                                   "A comment for the translators\non two lines"),
                         new Entry(null, "Hello {0}", "Bonjour {0}", true, ""), new Entry(null, "One file", "", false, ""),
                         new Entry("primitive", "left", "gauche", false, "")),
                 PoFile.read(po));
  }
}
