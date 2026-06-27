package plm.core.lang.primitives;

import lessons.welcome.array.basics.Array1Entity;
import plm.core.lang.LangC;
import plm.universe.Entity;
import plm.universe.EntityPrimitivesBase;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class PrimitiveRegistration {

    public static Map<Integer, PrimitiveMethod> getPrimitiveForEntity(Class<? extends Entity> entity) {
        List<Class<? extends EntityPrimitivesBase>> allPrimitivesClass = getAllPrimitivesClass(entity);

        if (allPrimitivesClass.stream().noneMatch(clazz -> clazz.isAnnotationPresent(EntityPrimitives.class))) {
            throw new IllegalArgumentException("Class hierarchy from '" + entity.getSimpleName() + "' do not use @EntityPrimitives");
        }

        List<PrimitiveMethod> primitives = allPrimitivesClass.stream().flatMap(clazz -> Arrays.stream(clazz.getDeclaredMethods()).filter(method -> method.isAnnotationPresent(Primitive.class))
                .map(method -> new PrimitiveMethod(method.getAnnotation(Primitive.class), method))).toList();

        Map<Integer, Set<PrimitiveMethod>> primitivesPerId = new HashMap<>();

        for (PrimitiveMethod primitive : primitives) {
            int id = primitive.id();
            primitivesPerId.computeIfAbsent(id, (k) -> new HashSet<>()).add(primitive);
        }

        List<Map.Entry<Integer, Set<PrimitiveMethod>>> duplicates = primitivesPerId.entrySet().stream().filter(o -> o.getValue().size() > 1).toList();

        if (!duplicates.isEmpty()) {
            StringBuffer buffer = new StringBuffer();
            for (Map.Entry<Integer, Set<PrimitiveMethod>> entry : duplicates) {
                int id = entry.getKey();
                List<String> methodLocations = entry.getValue().stream().map(PrimitiveMethod::location).toList();
                buffer.append("\t- ").append(id).append(":\n");
                methodLocations.forEach(loc -> buffer.append("\t\t- ").append(loc).append("\n"));
            }
            throw new IllegalStateException("Could not collect primitive by id, the following ids are assigned to multiple primitives:\n" + buffer);
        }

        return primitivesPerId.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, o -> o.getValue().iterator().next()));
    }

    @SuppressWarnings("unchecked")
    public static List<Class<? extends EntityPrimitivesBase>> getAllPrimitivesClass(Class<?> clazz) {
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
