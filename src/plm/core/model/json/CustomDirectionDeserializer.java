package plm.core.model.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import plm.universe.Direction;

public class CustomDirectionDeserializer extends JsonDeserializer<Direction> {
	
	public CustomDirectionDeserializer() {}
	
    @Override
    public Direction deserialize(JsonParser jp, DeserializationContext ctxt) 
      throws IOException, JsonProcessingException {
    	JsonNode node = jp.getCodec().readTree(jp);
        return new Direction(node.asInt());
    }
}