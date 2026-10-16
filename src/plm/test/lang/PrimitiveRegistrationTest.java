package plm.test.lang;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import plm.core.lang.primitives.Primitive;
import plm.core.lang.primitives.PrimitiveMethod;
import plm.core.lang.primitives.PrimitiveRegistration;
import plm.test.simple.SimpleExerciseEntity;
import plm.universe.Entity;
import plm.universe.EntityPrimitivesBase;

/** Tests of {@link PrimitiveRegistration}: primitives are found by reflection and identified by their name. */
public class PrimitiveRegistrationTest {

  public interface FooPrimitives extends EntityPrimitivesBase {
    @Primitive void foo(int x);
    @Primitive(name = "renamed") void original();
  }

  /** Declares the same primitive as {@link FooPrimitives}. */
  public interface SameFooPrimitives extends EntityPrimitivesBase {
    @Primitive void foo(int x);
  }

  /** Declares a primitive named like the one of {@link FooPrimitives}, with another signature. */
  public interface ConflictingFooPrimitives extends EntityPrimitivesBase {
    @Primitive void foo(String s);
  }

  public abstract static class SharedDeclarationEntity extends Entity implements FooPrimitives, SameFooPrimitives {}
  public abstract static class ConflictingEntity extends Entity implements FooPrimitives, ConflictingFooPrimitives {}

  @Test public void primitivesAreIndexedByNameAndSorted()
  {
    Map<String, PrimitiveMethod> primitives = PrimitiveRegistration.getMinimalPrimitiveForEntity(SharedDeclarationEntity.class);
    List<String> names                      = new ArrayList<>(primitives.keySet());

    assertTrue(names.containsAll(List.of("foo", "renamed")), names.toString());
    assertEquals(names.stream().sorted().toList(), names);
  }

  @Test public void explicitNameReplacesTheMethodName()
  {
    Map<String, PrimitiveMethod> primitives = PrimitiveRegistration.getMinimalPrimitiveForEntity(SharedDeclarationEntity.class);

    assertEquals("renamed", primitives.get("renamed").name());
    assertFalse(primitives.containsKey("original"));
  }

  @Test public void sameDeclarationInSeveralInterfacesIsOnePrimitive() throws NoSuchMethodException
  {
    Method foo                    = FooPrimitives.class.getMethod("foo", int.class);
    Method sameFoo                = SameFooPrimitives.class.getMethod("foo", int.class);
    PrimitiveMethod primitive     = new PrimitiveMethod(foo.getAnnotation(Primitive.class), foo);
    PrimitiveMethod samePrimitive = new PrimitiveMethod(sameFoo.getAnnotation(Primitive.class), sameFoo);

    assertEquals(primitive, samePrimitive);
    assertEquals(primitive.hashCode(), samePrimitive.hashCode());
    assertTrue(PrimitiveRegistration.getMinimalPrimitiveForEntity(SharedDeclarationEntity.class).containsKey("foo"));
  }

  @Test public void sameNameWithDifferentSignaturesIsRejected()
  {
    IllegalStateException e = assertThrows(IllegalStateException.class, () -> PrimitiveRegistration.getMinimalPrimitiveForEntity(ConflictingEntity.class));

    assertTrue(e.getMessage().contains("foo"), e.getMessage());
  }

  @Test public void inheritedPrimitivesAreFound()
  {
    assertTrue(PrimitiveRegistration.getMinimalPrimitiveForEntity(SimpleExerciseEntity.class).containsKey("setObjectif"));
  }

  @Test public void minimalPrimitivesAreCached()
  {
    assertSame(PrimitiveRegistration.getMinimalPrimitiveForEntity(SimpleExerciseEntity.class),
               PrimitiveRegistration.getMinimalPrimitiveForEntity(SimpleExerciseEntity.class));
  }
}
