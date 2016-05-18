package plm.universe.turtles;

import org.xnap.commons.i18n.I18n;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use = Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type")
public interface Shape {
	public Shape copy();
	public String diffTo(Shape o, I18n i18n);
}
