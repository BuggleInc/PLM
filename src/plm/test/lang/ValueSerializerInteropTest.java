package plm.test.lang;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import plm.core.ValueSerializer;
import plm.universe.Point;

/**
 * The three implementations of the textual protocol stay in sync: the Python and C ones read the values serialized by the Java one
 * (plm.core.ValueSerializer), and serialize them back into texts that Java reads as the same values. The texts are compared once
 * normalized by Java, as each language writes the doubles its own way (1e+20 or 1.0E20). Also runs the unit tests of the C one.
 */
public class ValueSerializerInteropTest {
  private static final Path PYTHON = Path.of("lib/resources/langages/python");
  private static final Path C      = Path.of("lib/resources/langages/c");

  /** Values of every type and tag of the protocol, nested arrays and escaped strings included */
  private static final List<Object> VALUES = Arrays.asList(
      null, 0, 1, -1, Integer.MAX_VALUE, Integer.MIN_VALUE, 0.5, -3.25, 1e20, 1e-7, 100.0, true, false, "", "hello", "with \"quotes\" and \\backslashes\\",
      "accents: àéïõ", "line\nbreaks\r\n and \\n", "colon:bracket]", Color.RED, new Color(10, 20, 30, 40), new int[] {1, -2, 3}, new int[][] {{1, 2}, {3}},
      new int[][][] {{{1}, {2, 3}}}, new double[] {0.5, -1.25}, new boolean[] {true, false}, new String[] {"a", "b:c"}, new Color[] {Color.BLUE}, new String[0],
      new byte[0], new byte[] {0, '\n', '"', ':', ']', (byte)0xff}, new byte[][] {{1}, {}});

  /** Python has no char type, and its empty lists carry no element type */
  @Test public void testPython() throws Exception
  {
    List<Object> values = new ArrayList<>(VALUES);
    values.addAll(List.of(new Point(1, -2.5), new Point[] {new Point(0, 0)}));
    String script = "import sys\nfrom ValueSerializer import serialize, deserialize\n"
                    + "for line in sys.stdin.read().splitlines():\n  print(serialize(deserialize(line)))\n";
    ProcessBuilder python = new ProcessBuilder("python3", "-c", script);
    python.environment().put("PYTHONPATH", PYTHON.toString());
    python.environment().put("PYTHONIOENCODING", "utf-8");
    python.environment().put("PYTHONDONTWRITEBYTECODE", "1"); // no __pycache__ in the sources
    assertRoundTrips(values, python);
  }

  /** The generic parser of the C glue has no point: the generated glue parses them itself */
  @Test public void testC(@TempDir Path dir) throws Exception
  {
    List<Object> values = new ArrayList<>(VALUES);
    values.addAll(List.of('x', new char[] {'a', 'b'}, new int[0]));
    Path roundtrip = dir.resolve("roundtrip");
    run(new ProcessBuilder("gcc", "-Wall", "-Wextra", "-o", roundtrip.toString(), C.resolve("value_serializer_roundtrip.c").toString(),
                           C.resolve("value_serializer.c").toString()),
        "");
    assertRoundTrips(values, new ProcessBuilder(roundtrip.toString()));
  }

  @Test public void testCUnitTests(@TempDir Path dir) throws Exception
  {
    Path tests = dir.resolve("tests");
    run(new ProcessBuilder("gcc", "-Wall", "-Wextra", "-o", tests.toString(), C.resolve("value_serializer.c").toString(),
                           C.resolve("value_serializer_test.c").toString()),
        "");
    run(new ProcessBuilder(tests.toString()), "");
  }

  /** Sends one serialized value per line to the process, and checks that it writes each of them back */
  private static void assertRoundTrips(List<Object> values, ProcessBuilder process) throws Exception
  {
    List<String> sent = values.stream().map(ValueSerializer::serialize).toList();
    List<String> back = run(process, String.join("\n", sent) + "\n").lines().toList();
    assertEquals(sent.size(), back.size(), "one line per value expected, got:\n" + String.join("\n", back));
    for (int i = 0; i < sent.size(); i++)
      assertEquals(sent.get(i), back.get(i).equals("ERROR") ? "ERROR" : ValueSerializer.serialize(ValueSerializer.deserialize(back.get(i))),
                   "sent " + sent.get(i) + ", got back " + back.get(i));
  }

  /** Runs the process with the given input, and returns its output; fails if it does not exit with 0 */
  private static String run(ProcessBuilder process, String input) throws IOException, InterruptedException
  {
    Process p = process.redirectErrorStream(true).start();
    try (OutputStream in = p.getOutputStream()) {
      in.write(input.getBytes(StandardCharsets.UTF_8));
    }
    String output = new String(p.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    assertEquals(0, p.waitFor(), String.join(" ", process.command()) + " failed:\n" + output);
    return output;
  }
}
