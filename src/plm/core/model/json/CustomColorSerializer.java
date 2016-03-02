package plm.core.model.json;

import java.awt.Color;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CustomColorSerializer extends JsonSerializer<Color> {

	@Override
    public void serialize(Color color, JsonGenerator gen, SerializerProvider arg2) 
      throws IOException, JsonProcessingException {
		gen.writeStartArray();
		gen.writeNumber(color.getRed());
		gen.writeNumber(color.getGreen());
		gen.writeNumber(color.getBlue());
		gen.writeNumber(color.getAlpha());
        gen.writeEndArray();
    }
}