package plm.universe;

import org.xnap.commons.i18n.I18n;
import plm.core.model.Game;

public abstract class Operation {
	private String name;
	private String msg;
	
	public Operation(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public String getMsg() {
		return msg;
	}
	
	public static I18n getI18n() {
		return Game.i18n;
	}
}
