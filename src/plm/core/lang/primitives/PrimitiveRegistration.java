package plm.core.lang.primitives;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import plm.universe.Entity;
import plm.universe.EntityPrimitivesBase;

/** Finds the primitives of the entities by reflection, indexed by name. */
public class PrimitiveRegistration {
  private static final Map<Class<? extends Entity>, Map<String, PrimitiveMethod>> minimalCache = new ConcurrentHashMap<>();

  /** The classpath scans are slow: they are done once, when first needed. */
  private static class ClasspathScans {
    static final Reflections LESSONS = new Reflections("lessons");
    static final Reflections PLM     = new Reflections("plm");
  }

  private static <T> Set<Class<? extends T>> getSubTypesOf(Class<? extends T> clazz)
  {
    HashSet<Class<? extends T>> set = new HashSet<>();
    set.addAll(ClasspathScans.LESSONS.getSubTypesOf(clazz));
    set.addAll(ClasspathScans.PLM.getSubTypesOf(clazz));

    return set;
  }

  private static Map<String, PrimitiveMethod> getPrimitiveForEntity(List<Class<? extends EntityPrimitivesBase>> allPrimitivesClass)
  {
    List<PrimitiveMethod> primitives = allPrimitivesClass.stream()
                                           .flatMap(clazz
                                                    -> Arrays.stream(clazz.getDeclaredMethods())
                                                           .filter(method -> method.isAnnotationPresent(Primitive.class))
                                                           .map(method -> new PrimitiveMethod(method.getAnnotation(Primitive.class), method)))
                                           .toList();

    // Declarations of the same primitive in several interfaces are equal and collapse in the set. The names are sorted for a readable glue.
    Map<String, Set<PrimitiveMethod>> primitivesPerName =
        primitives.stream().collect(Collectors.groupingBy(PrimitiveMethod::name, TreeMap::new, Collectors.toSet()));

    String duplicates =
        primitivesPerName.entrySet()
            .stream()
            .filter(entry -> entry.getValue().size() > 1)
            .map(entry -> "\t- " + entry.getKey() + ":\n" + entry.getValue().stream().map(p -> "\t\t- " + p.location() + "\n").collect(Collectors.joining()))
            .collect(Collectors.joining());
    if (!duplicates.isEmpty())
      throw new IllegalStateException("Could not collect primitives by name, the following names are assigned to multiple primitives:\n" + duplicates);

    Map<String, PrimitiveMethod> result = new TreeMap<>();
    primitivesPerName.forEach((name, declarations) -> result.put(name, declarations.iterator().next()));
    return result;
  }

  public static Map<String, PrimitiveMethod> getMinimalPrimitiveForEntity(Class<? extends Entity> entity)
  {
    return minimalCache.computeIfAbsent(entity, clazz -> getPrimitiveForEntity(getAllPrimitivesClass(clazz)));
  }

  public static Map<String, PrimitiveMethod> getMaximalPrimitiveForEntity(Class<? extends Entity> entity)
  {
    Set<Class<? extends Entity>> subTypesOf = getSubTypesOf(entity);
    subTypesOf.add(entity); // For SimpleExerciseEntity, the entity itself carries @EntityPrimitives directly, not in a subclass
    List<Class<? extends EntityPrimitivesBase>> allPrimitivesClass = subTypesOf.stream().flatMap(clazz -> getAllPrimitivesClass(clazz).stream()).toList();

    return getPrimitiveForEntity(allPrimitivesClass);
  }

  @SuppressWarnings("unchecked") private static List<Class<? extends EntityPrimitivesBase>> getAllPrimitivesClass(Class<?> clazz)
  {
    List<Class<? extends EntityPrimitivesBase>> list = new ArrayList<>();

    if (EntityPrimitivesBase.class.isAssignableFrom(clazz)) {
      list.add((Class<? extends EntityPrimitivesBase>)clazz);
      if (clazz.getSuperclass() != null)
        list.addAll(getAllPrimitivesClass(clazz.getSuperclass()));
    }
    if (clazz.isAnnotationPresent(EntityPrimitives.class))
      list.add(clazz.getAnnotation(EntityPrimitives.class).value());

    for (Class<?> implemented : clazz.getInterfaces())
      list.addAll(getAllPrimitivesClass(implemented));
    return list;
  }
}
