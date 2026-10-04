package plm.test.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Lesson;

/** Entities are only parsed when first needed: make sure that all of them can be, in every language they are written in. */
public class ExoTestEntityParsing extends ExoTest {

  @ParameterizedTest @MethodSource("exercises") public void testEntityParsing(Lesson l, Exercise e)
  {
    for (ProgrammingLanguage lang : e.getProgLanguages())
      Assertions.assertDoesNotThrow(()
                                        -> Assertions.assertFalse(e.getSourceFilesList(lang).isEmpty(), e.getId() + ": no source file in " + lang),
                                    e.getId() + ": cannot load the entity in " + lang);
  }
}
