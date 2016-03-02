package plm.core.model.json;

import java.awt.Color;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class CustomColorDeserializer extends JsonDeserializer<Color> {
	
	public CustomColorDeserializer() {}
	
    @Override
    public Color deserialize(JsonParser jp, DeserializationContext ctxt) 
      throws IOException, JsonProcessingException {
    	JsonNode node = jp.getCodec().readTree(jp);
        return new Color(node.get(0).asInt(), node.get(1).asInt(), node.get(2).asInt(), node.get(3).asInt());
    }
}
