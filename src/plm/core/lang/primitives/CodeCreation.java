package plm.core.lang.primitives;

import com.google.common.io.Files;
import plm.core.lang.LangC;
import plm.universe.bugglequest.AbstractBuggle;
import plm.universe.sort.SortingEntity;
import plm.universe.turtles.Turtle;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class CodeCreation {
    public static void main(String[] args) throws IOException {
        File folder = new File("target/classes/resources/langages/c");

        System.out.println(folder.exists());


        LangC.LangCExternalPrimitiveGenerator generator = new LangC.LangCExternalPrimitiveGenerator();

        {
            Map<Integer, PrimitiveMethod> list = PrimitiveRegistration.getPrimitiveForEntity(AbstractBuggle.class);
            String name = "RemoteBuggle";
            generator.generate(folder, name, list.values().stream().toList());
            Files.move(new File(folder, name+".h"), new File(folder,"include/"+name+".h"));
            Files.move(new File(folder, name+".c"), new File(folder,"src/"+name+".c"));
        }

        {
            Map<Integer, PrimitiveMethod> list = PrimitiveRegistration.getPrimitiveForEntity(SortingEntity.class);
            String name = "RemoteSort";
            generator.generate(folder, name, list.values().stream().toList());
            Files.move(new File(folder, name+".h"), new File(folder,"include/"+name+".h"));
            Files.move(new File(folder, name+".c"), new File(folder,"src/"+name+".c"));
        }

        {
            Map<Integer, PrimitiveMethod> list = PrimitiveRegistration.getPrimitiveForEntity(Turtle.class);
            String name = "RemoteTurtle";
            generator.generate(folder, name, list.values().stream().toList());
            Files.move(new File(folder, name+".h"), new File(folder,"include/"+name+".h"));
            Files.move(new File(folder, name+".c"), new File(folder,"src/"+name+".c"));
        }
    }
}
