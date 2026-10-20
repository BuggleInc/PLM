package plm.core.lang.primitives;

import java.awt.Color;
import java.util.Map;
import plm.universe.Direction;
import plm.universe.Point;

/** The kinds of values that a primitive can return to the student's process, each read by its own function of the glue. */
public enum Answer {
  STRING,
  DOUBLE,
  CHAR,
  COLOR,
  INT,
  BOOLEAN,
  POINT,
  POINT_ARRAY,
  BYTES;

  private static final Map<Class<?>, Answer> BY_TYPE =
      Map.ofEntries(Map.entry(String.class, STRING), Map.entry(double.class, DOUBLE), Map.entry(Double.class, DOUBLE), Map.entry(char.class, CHAR),
                    Map.entry(Character.class, CHAR), Map.entry(Color.class, COLOR), Map.entry(Direction.class, INT), Map.entry(int.class, INT),
                    Map.entry(Integer.class, INT), Map.entry(boolean.class, BOOLEAN), Map.entry(Boolean.class, BOOLEAN), Map.entry(Point.class, POINT),
                    Map.entry(Point[].class, POINT_ARRAY), Map.entry(byte[].class, BYTES));

  /** The kind of the values of that return type, which cannot be void */
  public static Answer of(Class<?> type)
  {
    Answer answer = BY_TYPE.get(type);
    if (answer == null)
      throw new IllegalStateException("Unknown return type: " + type);
    return answer;
  }
}
