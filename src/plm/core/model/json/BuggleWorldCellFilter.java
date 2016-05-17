package plm.core.model.json;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;

import plm.universe.bugglequest.BuggleWorldCell;

public class BuggleWorldCellFilter extends SimpleBeanPropertyFilter {

	private List<String> optionalFields = Arrays.asList("color", "leftWall", "topWall", "msgColor", "content", "hasBaggle");

	@Override
	public void serializeAsField
	(Object pojo, JsonGenerator jgen, SerializerProvider provider, PropertyWriter writer)
			throws Exception {
		if (include(writer)) {
			String fieldName = writer.getName();
			if (!optionalFields.contains(fieldName)) {
				writer.serializeAsField(pojo, jgen, provider);
				return;
			}
			BuggleWorldCell cell = (BuggleWorldCell) pojo;
			switch(fieldName) {
			case "color":
				serializeCellColor(pojo, jgen, provider, writer, cell.getColor());
				break;
			case "msgColor":
				serializeMsgColor(pojo, jgen, provider, writer, cell.getMsgColor());
			case "content":
				serializeContent(pojo, jgen, provider, writer, cell.getContent());
			case "leftWall":
				serializeLeftWall(pojo, jgen, provider, writer, cell.hasLeftWall());
				break;
			case "topWall":
				serializeTopWall(pojo, jgen, provider, writer, cell.hasTopWall());
				break;
			case "hasBaggle":
				serializeHasBaggle(pojo, jgen, provider, writer, cell.hasBaggle());
				break;
			default:
				writer.serializeAsField(pojo, jgen, provider);
				break;
			}
		} else if (!jgen.canOmitFields()) { // since 2.3
			writer.serializeAsOmittedField(pojo, jgen, provider);
		}
	}

	public void serializeColor(Object pojo, JsonGenerator jgen, SerializerProvider provider, PropertyWriter writer, Color color, Color defaultColor) throws Exception {
		if(!color.equals(defaultColor)) {
			writer.serializeAsField(pojo, jgen, provider);
		}
	}

	public void serializeCellColor(Object pojo, JsonGenerator jgen, SerializerProvider provider, PropertyWriter writer, Color color) throws Exception {
		serializeColor(pojo, jgen, provider, writer, color, BuggleWorldCell.DEFAULT_COLOR);
	}

	public void serializeMsgColor(Object pojo, JsonGenerator jgen, SerializerProvider provider, PropertyWriter writer, Color msgColor) throws Exception {
		serializeColor(pojo, jgen, provider, writer, msgColor, BuggleWorldCell.DEFAULT_MSG_COLOR);
	}

	public void serializeContent(Object pojo, JsonGenerator jgen, SerializerProvider provider, PropertyWriter writer, String content) throws Exception {
		if(!content.isEmpty()) {
			writer.serializeAsField(pojo, jgen, provider);
		}
	}
	
	public void serializeBoolean(Object pojo, JsonGenerator jgen, SerializerProvider provider, PropertyWriter writer, boolean bool, boolean defaultBool) throws Exception {
		if(bool != defaultBool) {
			writer.serializeAsField(pojo, jgen, provider);
		}
	}

	public void serializeLeftWall(Object pojo, JsonGenerator jgen, SerializerProvider provider, PropertyWriter writer, boolean leftWall) throws Exception {
		serializeBoolean(pojo, jgen, provider, writer, leftWall, false);
	}

	public void serializeTopWall(Object pojo, JsonGenerator jgen, SerializerProvider provider, PropertyWriter writer, boolean topWall) throws Exception {
		serializeBoolean(pojo, jgen, provider, writer, topWall, false);
	}

	public void serializeHasBaggle(Object pojo, JsonGenerator jgen, SerializerProvider provider, PropertyWriter writer, boolean hasBaggle) throws Exception {
		serializeBoolean(pojo, jgen, provider, writer, hasBaggle, false);
	}

	@Override
	protected boolean include(BeanPropertyWriter writer) {
		return true;
	}
	@Override
	protected boolean include(PropertyWriter writer) {
		return true;
	}
};