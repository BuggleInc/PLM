package plm.core.lang.primitives;

import java.util.*;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import plm.universe.Entity;
import plm.universe.EntityPrimitivesBase;

public class PrimitiveRegistration {

  public static <T> Set<Class<? extends T>> getSubTypesOf(Class<? extends T> clazz)
  {
    HashSet<Class<? extends T>> set = new HashSet<>();
    set.addAll(new Reflections("lessons").getSubTypesOf(clazz));
    set.addAll(new Reflections("plm").getSubTypesOf(clazz));

    return set;
  }

  public static Map<String, PrimitiveMethod> getPrimitiveForEntity(List<Class<? extends EntityPrimitivesBase>> allPrimitivesClass)
  {
    List<PrimitiveMethod> primitives = allPrimitivesClass.stream()
                                           .flatMap(clazz
                                                    -> Arrays.stream(clazz.getDeclaredMethods())
                                                           .filter(method -> method.isAnnotationPresent(Primitive.class))
                                                           .map(method -> new PrimitiveMethod(method.getAnnotation(Primitive.class), method)))
                                           .toList();

    // Declarations of the same primitive in several interfaces are equal and collapse in the set
    Map<String, Set<PrimitiveMethod>> primitivesPerName = primitives.stream().collect(Collectors.groupingBy(PrimitiveMethod::name, Collectors.toSet()));

    String duplicates =
        primitivesPerName.entrySet()
            .stream()
            .filter(entry -> entry.getValue().size() > 1)
            .map(entry -> "\t- " + entry.getKey() + ":\n" + entry.getValue().stream().map(p -> "\t\t- " + p.location() + "\n").collect(Collectors.joining()))
            .collect(Collectors.joining());
    if (!duplicates.isEmpty())
      throw new IllegalStateException("Could not collect primitives by name, the following names are assigned to multiple primitives:\n" + duplicates);

    return primitivesPerName.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().iterator().next()));
  }

  public static Map<String, PrimitiveMethod> getMinimalPrimitiveForEntity(Class<? extends Entity> entity)
  {
    List<Class<? extends EntityPrimitivesBase>> allPrimitivesClass = getAllPrimitivesClass(entity);

    return getPrimitiveForEntity(allPrimitivesClass);
  }

  public static Map<String, PrimitiveMethod> getMaximalPrimitiveForEntity(Class<? extends Entity> entity)
  {
    Set<Class<? extends Entity>> subTypesOf = getSubTypesOf(entity);
    subTypesOf.add(entity); // For SimpleExerciseEntity, the entity itself carries @EntityPrimitives directly, not in a subclass
    List<Class<? extends EntityPrimitivesBase>> allPrimitivesClass = subTypesOf.stream().flatMap(clazz -> getAllPrimitivesClass(clazz).stream()).toList();

    return getPrimitiveForEntity(allPrimitivesClass);
  }

  @SuppressWarnings("unchecked") public static List<Class<? extends EntityPrimitivesBase>> getAllPrimitivesClass(Class<?> clazz)
  {
    if (clazz == EntityPrimitivesBase.class) return List.of((Class<? extends EntityPrimitivesBase>) clazz);

    ArrayList<Class<? extends EntityPrimitivesBase>> list = new ArrayList<>();

    if (EntityPrimitivesBase.class.isAssignableFrom(clazz)) {
            list.add((Class<? extends EntityPrimitivesBase>) clazz);
            if (clazz.getSuperclass() != null)
              list.addAll(getAllPrimitivesClass(clazz.getSuperclass()));
    }
    if (clazz.isAnnotationPresent(EntityPrimitives.class)) {
      list.add(clazz.getAnnotation(EntityPrimitives.class).value());
    }

    list.addAll(Arrays.stream(clazz.getInterfaces()).flatMap(c -> getAllPrimitivesClass(c).stream()).toList());
    return list;
  }
}
