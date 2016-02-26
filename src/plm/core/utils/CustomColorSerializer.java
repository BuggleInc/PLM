package plm.core.utils;

import java.awt.Color;
import java.io.IOException;
import java.util.Arrays;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CustomColorSerializer extends JsonSerializer<Color> {

	@Override
    public void serialize(Color value, JsonGenerator gen, SerializerProvider arg2) 
      throws IOException, JsonProcessingException {
		int[] color = new int[4];
		color[0] = value.getRed();
		color[0] = value.getBlue();
		color[0] = value.getGreen();
		color[0] = value.getAlpha();
        gen.writeString(Arrays.toString(color));
    }
}