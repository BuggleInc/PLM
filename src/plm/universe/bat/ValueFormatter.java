package plm.universe.bat;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import lessons.recursion.cons.universe.RecList;
import org.python.core.PyInstance;
import plm.core.lang.ProgrammingLanguage;

public class ValueFormatter {
  // Collapses List, RecList, Integer[], Vector into one canonical internal type
  static Object normalize(Object value)
  {
    if (value == null)
      return null;
    if (value instanceof List) {
      List<?> l = (List<?>)value;
      int[] res = new int[l.size()];
      for (int i = 0; i < l.size(); i++)
        res[i] = (Integer)l.get(i);

      return res;
    }
    if (value instanceof RecList)
      value = RecList.toArray((RecList)value);
    if (value instanceof scala.collection.immutable.List) {
      scala.collection.immutable.List<?> sl = (scala.collection.immutable.List<?>)value;
      int[] res                             = new int[sl.size()];
      scala.collection.Iterator<?> it       = sl.iterator();
      int i                                 = 0;
      while (it.hasNext())
        res[i++] = (Integer)it.next();
      return res;
    }
    if (value.getClass().isArray() && value.getClass().getComponentType().equals(Integer.class)) {
      Integer[] orig = (Integer[])value;
      if (orig.length == 0 || (orig.length == 1 && orig[0] == null))
        return new int[] {};
      int[] res = new int[orig.length];
      for (int i = 0; i < res.length; i++)
        res[i] = orig[i];
      return res;
    }
    return value;
  }

  static boolean equals(Object o1, Object o2)
  {
    o1 = ValueFormatter.normalize(o1);
    o2 = ValueFormatter.normalize(o2);

    if (o1 == null && o2 == null)
      return true;
    if (o1 == null)
      return isEmptyArray(o2);
    if (o2 == null)
      return isEmptyArray(o1);

    if (o1.getClass().isArray() && o2.getClass().isArray()) {
      if (!o1.getClass().getComponentType().equals(o2.getClass().getComponentType()))
        return false; // not same type in both arrays

      if (o1.getClass().getComponentType().equals(Integer.TYPE)) {
        return Arrays.equals((int[])o1, (int[])o2);
      } else if (o1.getClass().getComponentType().equals(String.class)) {
        return Arrays.equals((String[])o1, (String[])o2);
      } else if (o1.getClass().getComponentType().equals(Object.class)) {
        return Arrays.equals((Object[])o1, (Object[])o2);
      } else
        throw new UnsupportedOperationException("ValueFormatter.equals: unsupported array component type: " +
                                                o1.getClass().getComponentType().getName());
    }
    if (o1.getClass().isArray() || o2.getClass().isArray())
      return false; // The other cannot be an array because of previous test
    return o1.equals(o2);
  }
  private static boolean isEmptyArray(Object o)
  {
    return o.getClass().isArray() && java.lang.reflect.Array.getLength(o) == 0;
  }

  /* --- Formatting logic --- */
  static private void openArray(StringBuilder sb, ProgrammingLanguage pl)
  {
    if (pl.isJava()) {
      sb.append("{");
    } else if (pl.isScala()) {
      sb.append("Array(");
    } else if (pl.isPython()) {
      sb.append("[");
    } else {
      throw new RuntimeException("Please port me to " + pl.getLang());
    }
  }
  static private void closeArray(StringBuilder sb, ProgrammingLanguage pl)
  {
    if (pl.isJava())
      sb.append("}");
    else if (pl.isScala())
      sb.append(")");
    else if (pl.isPython())
      sb.append("]");
    else
      throw new RuntimeException("Please port me to " + pl.getLang());
  }

  static String format(Object o, ProgrammingLanguage pl)
  {
    StringBuilder sb = new StringBuilder();
    if (o == null) {
      if (pl.isScala())
        return "Nil";
      else if (pl.isPython())
        return "None";
      else if (pl.isJava())
        return "null";
      else
        throw new RuntimeException("Please port me to " + pl.getLang());
    }

    if (o instanceof String[]) {
      openArray(sb, pl);

      String[] a = (String[])o;
      for (String i : a)
        sb.append(i + ",");
      if (a.length > 0) // Don't kill the last comma if there is none
        sb.deleteCharAt(sb.length() - 1);

      closeArray(sb, pl);

    } else if (o.getClass().equals(Vector.class) || o.getClass().isArray()) {
      if (o.getClass().equals(Vector.class))
        o = ValueFormatter.normalize(o);

      openArray(sb, pl);
      if (o.getClass().getComponentType().equals(Integer.TYPE)) {
        int[] a = (int[])o;
        for (int i : a)
          sb.append(i + ",");

        if (a.length > 0) // Don't kill the last comma if there is none
          sb.deleteCharAt(sb.length() - 1);
      } else if (o.getClass().getComponentType().equals(Integer.class)) {
        Integer[] a = (Integer[])o;
        for (Integer i : a)
          sb.append(i + ",");

        if (a.length > 0) // Don't kill the last comma if there is none
          sb.deleteCharAt(sb.length() - 1);

      } else {
        throw new RuntimeException("Unhandled internal type (only Array<int> and Array<Integer> are handled so far)");
      }
      closeArray(sb, pl);
    } else if (o instanceof Boolean) {
      Boolean b = (Boolean)o;
      if (pl.isJava() || pl.isScala()) {
        sb.append(b ? "true" : "false");
      } else if (pl.isPython()) {
        sb.append(b ? "True" : "False");
      } else {
        throw new RuntimeException("Please port me to " + pl.getLang());
      }
    } else if (o instanceof String) {
      sb.append("\"" + o + "\"");
    } else if (o instanceof PyInstance) {
      sb.append(((PyInstance)o).__str__());
    } else {
      sb.append(o.toString());
    }
    return sb.toString();
  }
}