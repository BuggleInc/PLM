package plm.test.model;

import java.awt.Color;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import plm.core.utils.ColorMapper;
import plm.core.utils.InvalidColorNameException;

/** The colors are named in the map files either with a name or with their components, and numbered in the protocol. */
public class ColorMapperTest {

  @Test public void namedColorsRoundTrip() throws InvalidColorNameException
  {
    for (String name : new String[] {"white", "darkGray", "yellow"}) {
      Color color = ColorMapper.name2color(name);
      Assertions.assertEquals(name, ColorMapper.color2name(color));
      Assertions.assertEquals(color, ColorMapper.int2color(ColorMapper.color2int(color)));
    }
    Assertions.assertEquals(Color.red, ColorMapper.name2color("RED"));
  }

  @Test public void otherColorsAreNamedByTheirComponents() throws InvalidColorNameException
  {
    Color color = new Color(1, 2, 3);
    Assertions.assertEquals("1/2/3", ColorMapper.color2name(color));
    Assertions.assertEquals(color, ColorMapper.name2color("1/2/3"));
    Assertions.assertEquals(-1, ColorMapper.color2int(color));
    Assertions.assertEquals(color.toString(), ColorMapper.color2translated(color));
  }

  @Test public void equalColorsAreTranslatedLikeTheConstants()
  {
    Assertions.assertEquals(ColorMapper.color2translated(Color.blue), ColorMapper.color2translated(new Color(0, 0, 255)));
  }

  @Test public void invalidNamesAreRefused()
  {
    for (String name : new String[] {"mauve", "256/0/0", "0/256/0", "0/0/256", "99999999999/0/0"})
      Assertions.assertThrows(InvalidColorNameException.class, () -> ColorMapper.name2color(name), name);
    Assertions.assertThrows(InvalidColorNameException.class, () -> ColorMapper.int2color(13));
  }
}
