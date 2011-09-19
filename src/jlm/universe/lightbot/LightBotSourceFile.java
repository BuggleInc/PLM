package jlm.universe.lightbot;

import javax.swing.JScrollPane;

import jlm.core.model.lesson.SourceFileRevertable;

public class LightBotSourceFile extends SourceFileRevertable {
	private LightBotInstruction[] main;
	private LightBotInstruction[] func1;
	private LightBotInstruction[] func2;

	public LightBotSourceFile(String name) {
		super(name, "");
		resetBody();
		setCompilable(false);
	}
	@Override
	public String getBody(){
		StringBuffer sb = new StringBuffer();
		for (int i=0;i<getMain().length;i++)
			sb.append(getMain()[i].toChar());
		sb.append('\n');
		for (int i=0;i<getFunc1().length;i++)
			sb.append(getFunc1()[i].toChar());
		sb.append('\n');
		for (int i=0;i<getFunc2().length;i++)
			sb.append(getFunc2()[i].toChar());
		sb.append('\n');
		return sb.toString();
	}

	private void resetBody() {
		setMain(new LightBotInstruction[12]);
		for (int i=0;i<getMain().length;i++)
			getMain()[i]=LightBotInstruction.noop();
		setFunc1(new LightBotInstruction[8]);
		for (int i=0;i<getFunc1().length;i++)
			getFunc1()[i]=LightBotInstruction.noop();
		setFunc2(new LightBotInstruction[8]);
		for (int i=0;i<getFunc2().length;i++)
			getFunc2()[i]=LightBotInstruction.noop();	
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
				getMain()[pos++]=new LightBotInstruction(c);
		pos=0;
		if (lines.length>1)
			for (char c : lines[1].toCharArray()) 
				getFunc1()[pos++]=new LightBotInstruction(c);
		pos=0;
		if (lines.length>2)
			for (char c : lines[2].toCharArray()) 
				getFunc2()[pos++]=new LightBotInstruction(c);
	}

	@Override
	public JScrollPane getEditorPanel(String lang){
		return new LightBotEditorPanel(this);
	}
	public void setMain(LightBotInstruction[] main) {
		this.main = main;
	}
	public LightBotInstruction[] getMain() {
		return main;
	}
	public void setFunc1(LightBotInstruction[] func1) {
		this.func1 = func1;
	}
	public LightBotInstruction[] getFunc1() {
		return func1;
	}
	public void setFunc2(LightBotInstruction[] func2) {
		this.func2 = func2;
	}
	public LightBotInstruction[] getFunc2() {
		return func2;
	}
}
