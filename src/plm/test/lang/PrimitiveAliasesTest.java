package plm.test.lang;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import plm.core.lang.primitives.CodeCreation;
import plm.core.lang.primitives.PrimitiveAliases;
import plm.core.lang.primitives.PrimitiveMethod;
import plm.core.lang.primitives.PrimitiveRegistration;
import plm.test.simple.SimpleExerciseEntity;

/** The translated names of the primitives can be added to the glue of every universe, and the translation mistakes are caught. */
public class PrimitiveAliasesTest {

  @Test public void translationsOfTheCatalogsAreValid() throws IOException
  {
    Map<String, Map<String, String>> aliases = PrimitiveAliases.load(PrimitiveAliases.PO_DIRECTORY);
    for (CodeCreation.Remote remote : CodeCreation.REMOTES)
      assertDoesNotThrow(() -> PrimitiveAliases.withAliases(remote.name(), PrimitiveRegistration.getMaximalPrimitiveForEntity(remote.entity()), aliases));
  }

  @Test public void aliasCallsTheCanonicalPrimitive()
  {
    List<PrimitiveMethod> glue    = withAliases(Map.of("fr", Map.of("setObjectif", "fixeObjectif"), "es", Map.of("setObjectif", "fixeObjectif")));
    List<PrimitiveMethod> aliases = glue.stream().filter(primitive -> primitive.name().equals("fixeObjectif")).toList();

    assertEquals(1, aliases.size()); // the same alias in two locales
    assertEquals("setObjectif", aliases.get(0).command());
  }

  @Test public void invalidIdentifiersAreRejected()
  {
    assertThrows(IllegalStateException.class, () -> withAliases(Map.of("fr", Map.of("setObjectif", "fixe-objectif"))));
    assertThrows(IllegalStateException.class, () -> withAliases(Map.of("fr", Map.of("setObjectif", "définirObjectif"))));
    assertThrows(IllegalStateException.class, () -> withAliases(Map.of("fr", Map.of("setObjectif", "class"))));
  }

  @Test public void conflictingNamesAreRejected()
  {
    assertThrows(IllegalStateException.class, () -> withAliases(Map.of("fr", Map.of("setObjectif", "getName"))));
    assertThrows(IllegalStateException.class, () -> withAliases(Map.of("fr", Map.of("setObjectif", "nom"), "es", Map.of("getName", "nom"))));
  }

  private static List<PrimitiveMethod> withAliases(Map<String, Map<String, String>> aliases)
  {
    return PrimitiveAliases.withAliases("RemoteSimple", PrimitiveRegistration.getMaximalPrimitiveForEntity(SimpleExerciseEntity.class), aliases);
  }
}
