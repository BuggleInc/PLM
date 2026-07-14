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

  public static Map<Integer, PrimitiveMethod> getPrimitiveForEntity(List<Class<? extends EntityPrimitivesBase>> allPrimitivesClass)
  {
    List<PrimitiveMethod> primitives = allPrimitivesClass.stream()
                                           .flatMap(clazz
                                                    -> Arrays.stream(clazz.getDeclaredMethods())
                                                           .filter(method -> method.isAnnotationPresent(Primitive.class))
                                                           .map(method -> new PrimitiveMethod(method.getAnnotation(Primitive.class), method)))
                                           .toList();

    Map<Integer, Set<PrimitiveMethod>> primitivesPerId = new HashMap<>();

    for (PrimitiveMethod primitive : primitives) {
      int id = primitive.id();
      primitivesPerId.computeIfAbsent(id, (k) -> new HashSet<>()).add(primitive);
    }

    List<Map.Entry<Integer, Set<PrimitiveMethod>>> duplicatesById = primitivesPerId.entrySet().stream().filter(o -> o.getValue().size() > 1).toList();

    if (!duplicatesById.isEmpty()) {
      StringBuffer buffer = new StringBuffer();
      for (Map.Entry<Integer, Set<PrimitiveMethod>> entry : duplicatesById) {
        int id                       = entry.getKey();
        List<String> methodLocations = entry.getValue().stream().map(PrimitiveMethod::location).toList();
        buffer.append("\t- ").append(id).append(":\n");
        methodLocations.forEach(loc -> buffer.append("\t\t- ").append(loc).append("\n"));
      }
      throw new IllegalStateException("Could not collect primitive by id, the following ids are assigned to multiple primitives:\n" + buffer);
    }

    Map<String, Set<PrimitiveMethod>> primitivesPerName = new HashMap<>();

    for (PrimitiveMethod primitive : primitives) {
      String name = primitive.name();
      primitivesPerName.computeIfAbsent(name, (k) -> new HashSet<>()).add(primitive);
    }

    List<Map.Entry<String, Set<PrimitiveMethod>>> duplicatesByName = primitivesPerName.entrySet().stream().filter(o -> o.getValue().size() > 1).toList();

    if (!duplicatesByName.isEmpty()) {
      StringBuffer buffer = new StringBuffer();
      for (Map.Entry<String, Set<PrimitiveMethod>> entry : duplicatesByName) {
        String name                  = entry.getKey();
        List<String> methodLocations = entry.getValue().stream().map(PrimitiveMethod::location).toList();
        buffer.append("\t- ").append(name).append(":\n");
        methodLocations.forEach(loc -> buffer.append("\t\t- ").append(loc).append("\n"));
      }
      throw new IllegalStateException("Could not collect primitive by id, the following names are assigned to multiple primitives:\n" + buffer);
    }

    return primitivesPerId.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, o -> o.getValue().iterator().next()));
  }

  public static Map<Integer, PrimitiveMethod> getMinimalPrimitiveForEntity(Class<? extends Entity> entity)
  {
    List<Class<? extends EntityPrimitivesBase>> allPrimitivesClass = getAllPrimitivesClass(entity);

    return getPrimitiveForEntity(allPrimitivesClass);
  }

  public static Map<Integer, PrimitiveMethod> getMaximalPrimitiveForEntity(Class<? extends Entity> entity)
  {
    Set<Class<? extends Entity>> subTypesOf                        = getSubTypesOf(entity);
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
