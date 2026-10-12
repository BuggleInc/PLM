package lessons.lightbot.universe;

import java.util.Arrays;

/**
 * The program of a LightBot student: the instructions of main, func1 and func2. It is stored as a text of one line per function, with
 * one character per instruction (see {@link LightBotInstruction#toChar}).
 */
public class LightBotProgram {
  private final LightBotInstruction[] main  = noops(12);
  private final LightBotInstruction[] func1 = noops(8);
  private final LightBotInstruction[] func2 = noops(8);

  /** The instructions missing from the text are noops, and the ones beyond the size of a function are ignored */
  public static LightBotProgram parse(String text)
  {
    LightBotProgram program           = new LightBotProgram();
    LightBotInstruction[][] functions = {program.main, program.func1, program.func2};
    String[] lines                    = text.split("\n");
    for (int f = 0; f < functions.length && f < lines.length; f++)
      for (int pos = 0; pos < functions[f].length && pos < lines[f].length(); pos++)
        functions[f][pos] = new LightBotInstruction(lines[f].charAt(pos));
    return program;
  }

  /** The text of the program: one line per function, even when it is empty */
  @Override public String toString()
  {
    StringBuilder sb = new StringBuilder();
    for (LightBotInstruction[] function : new LightBotInstruction[][] {main, func1, func2}) {
      for (LightBotInstruction instruction : function)
        sb.append(instruction.toChar());
      sb.append('\n');
    }
    return sb.toString();
  }

  private static LightBotInstruction[] noops(int size)
  {
    LightBotInstruction[] function = new LightBotInstruction[size];
    Arrays.setAll(function, i -> LightBotInstruction.noop());
    return function;
  }

  public LightBotInstruction[] getMain() { return main; }
  public LightBotInstruction[] getFunc1() { return func1; }
  public LightBotInstruction[] getFunc2() { return func2; }
}
