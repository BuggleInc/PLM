package plm.core.model.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import plm.universe.Direction;

public class CustomDirectionSerializer extends JsonSerializer<Direction> {

	@Override
    public void serialize(Direction direction, JsonGenerator gen, SerializerProvider arg2) 
      throws IOException, JsonProcessingException {
		gen.writeNumber(direction.getValue());
    }
}