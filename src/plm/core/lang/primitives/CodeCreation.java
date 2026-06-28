package plm.core.lang.primitives;

import com.google.common.io.Files;
import lessons.recursion.hanoi.universe.HanoiEntity;
import lessons.sort.baseball.universe.BaseballEntity;
import lessons.sort.dutchflag.universe.DutchFlagEntity;
import lessons.sort.pancake.universe.PancakeEntity;
import org.reflections.Reflections;
import plm.core.lang.LangC;
import plm.universe.Entity;
import plm.universe.bugglequest.AbstractBuggle;
import plm.universe.sort.SortingEntity;
import plm.universe.turtles.Turtle;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class CodeCreation {
    public static void main(String[] args) throws IOException {
        File folder = new File("target/classes/resources/langages/c");

        LangC.LangCExternalPrimitiveGenerator generator = new LangC.LangCExternalPrimitiveGenerator();

        Map<String, Class<? extends Entity>> remoteMap = Map.of(
                "RemoteBuggle", AbstractBuggle.class,
                "RemoteSort", SortingEntity.class,
                "RemoteTurtle", Turtle.class,
                "RemotePancake", PancakeEntity.class,
                "RemoteHanoi", HanoiEntity.class,
                "RemoteBaseball", BaseballEntity.class,
                "RemoteFlag", DutchFlagEntity.class
        );

        for (Map.Entry<String, Class<? extends Entity>> entry : remoteMap.entrySet()) {
            String name = entry.getKey();
            Class<? extends Entity> clazz = entry.getValue();

            Map<Integer, PrimitiveMethod> list = null;
            try {
                list = PrimitiveRegistration.getMaximalPrimitiveForEntity(clazz);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            generator.generate(folder, name, list.values().stream().toList());
            Files.move(new File(folder, name+".h"), new File(folder,"include/"+name+".h"));
            Files.move(new File(folder, name+".c"), new File(folder,"src/"+name+".c"));
        }
    }
}
