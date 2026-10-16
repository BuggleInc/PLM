package plm.test.lang;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import plm.core.lang.primitives.PrimitiveParameter;
import plm.core.lang.primitives.PrimitiveRegistration;
import plm.test.simple.SimpleExerciseEntity;
import plm.universe.Entity;
import plm.universe.EntityPrimitivesBase;
import plm.universe.bugglequest.AbstractBuggle;

/** Tests of {@link PrimitiveRegistration}: primitives are found by reflection and identified by their name. */
public class PrimitiveRegistrationTest {

  public interface FooPrimitives extends EntityPrimitivesBase {
    @Primitive void foo(int x);
    @Primitive void bar();
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

    assertTrue(names.containsAll(List.of("foo", "bar")), names.toString());
    assertEquals(names.stream().sorted().toList(), names);
  }

  @Test public void sameDeclarationInSeveralInterfacesIsOnePrimitive() throws NoSuchMethodException
  {
    Method foo                    = FooPrimitives.class.getMethod("foo", int.class);
    Method sameFoo                = SameFooPrimitives.class.getMethod("foo", int.class);
    PrimitiveMethod primitive     = new PrimitiveMethod(foo);
    PrimitiveMethod samePrimitive = new PrimitiveMethod(sameFoo);

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

  /**
   * The parameter names reach the student's glue, which needs the -parameters flag of javac
   *
   *  If this test ever breaks, this is because you removed the -parameters flag in pom.xml
   */
  @Test public void parameterNamesAreTheDeclaredOnes()
  {
    PrimitiveMethod forward = PrimitiveRegistration.getMinimalPrimitiveForEntity(AbstractBuggle.class).get("forward");

    assertEquals(List.of("steps"), forward.parameters().stream().map(PrimitiveParameter::name).toList());
  }
}
