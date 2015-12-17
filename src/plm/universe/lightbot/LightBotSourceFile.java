package plm.universe.lightbot;

import plm.core.model.Game;
import plm.core.model.session.SourceFileRevertable;

public class LightBotSourceFile extends SourceFileRevertable {
	private LightBotInstruction[] main;
	private LightBotInstruction[] func1;
	private LightBotInstruction[] func2;

	public LightBotSourceFile(Game game, String name) {
		super(game, name);
		resetBody();
	}
	@Override
	public String getBody(){
		StringBuffer sb = new StringBuffer();
		for (int i=0;i<main.length;i++)
			sb.append(main[i].toChar());
		sb.append('\n');
		for (int i=0;i<func1.length;i++)
			sb.append(func1[i].toChar());
		sb.append('\n');
		for (int i=0;i<func2.length;i++)
			sb.append(func2[i].toChar());
		sb.append('\n');
		return sb.toString();
	}

	private void resetBody() {
		main = new LightBotInstruction[12];
		for (int i=0;i<main.length;i++)
			main[i]=LightBotInstruction.noop();
		func1 = new LightBotInstruction[8];
		for (int i=0;i<func1.length;i++)
			func1[i]=LightBotInstruction.noop();
		func2 = new LightBotInstruction[8];
		for (int i=0;i<func2.length;i++)
			func2[i]=LightBotInstruction.noop();	
	}
	
	@Override
	public void setBody(String newBody){
		/* reset everything to noop */
		resetBody();
		
		/* parse content */
		String[] lines = newBody.split("\n");
		int pos=0;
		if (lines.length>0)
			for (char c : lines[0].toCharArray()) 
				main[pos++]=new LightBotInstruction(c);
		pos=0;
		if (lines.length>1)
			for (char c : lines[1].toCharArray()) 
				func1[pos++]=new LightBotInstruction(c);
		pos=0;
		if (lines.length>2)
			for (char c : lines[2].toCharArray()) 
				func2[pos++]=new LightBotInstruction(c);
	}

	public LightBotInstruction[] getMain() {
		return main;
	}
	public LightBotInstruction[] getFunc1() {
		return func1;
	}
	public LightBotInstruction[] getFunc2() {
		return func2;
	}
}
