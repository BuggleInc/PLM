package plm.universe.lightbot;



public class LightBotInstruction {
	/* instructionNames must match what toString says, and what the string constructor expects, or comboboxes of editor won't work */
	public final static String[] instructionNames = { "noop","forward", "jump", "left", "right", "light", "f1","f2" };

	private enum InstructionKind {
		NOOP,FORWARD,JUMP,LIGHT,LEFT,RIGHT,F1,F2;		
	}

	InstructionKind kind;
	public LightBotInstruction(InstructionKind kind) {
		this.kind = kind;
	}
	public LightBotInstruction(char c) {
		switch (c) {
		case 'F': kind=InstructionKind.FORWARD; break;
		case 'J': kind=InstructionKind.JUMP; break;
		case 'L': kind=InstructionKind.LEFT; break;
		case 'R': kind=InstructionKind.RIGHT; break;
		case 'O': kind=InstructionKind.LIGHT; break;
		case '1': kind=InstructionKind.F1; break;
		case '2': kind=InstructionKind.F2; break;
		case ' ': kind=InstructionKind.NOOP; break;
		default:
			throw new RuntimeException("Char '"+c+"' does not match any known symbol");
		}
	}
	public LightBotInstruction(String str) {
		/* must match instructionNames, or the comboboxes in the editor won't work */
		if (str.equalsIgnoreCase("forward")) {
			kind=InstructionKind.FORWARD;
		} else if (str.equalsIgnoreCase("jump")) {
			kind=InstructionKind.JUMP;
		} else if (str.equalsIgnoreCase("light")) {
			kind=InstructionKind.LIGHT;
		} else if (str.equalsIgnoreCase("left")) {
			kind=InstructionKind.LEFT;
		} else if (str.equalsIgnoreCase("right")) {
			kind=InstructionKind.RIGHT;
		} else if (str.equalsIgnoreCase("f1")) {
			kind=InstructionKind.F1;
		} else if (str.equalsIgnoreCase("f2")) {
			kind=InstructionKind.F2;
		} else {
			kind=InstructionKind.NOOP; 
		}
	}
	public void run(LightBotEntity lb) {
//		getGame().getLogger().log("exec "+toString());
		switch (kind) {
		case FORWARD: lb.forward(); break;
		case JUMP: lb.jump(); break;
		case LEFT: lb.left(); break;
		case RIGHT: lb.right(); break;
		case LIGHT: lb.light(); break;
		case F1: lb.runF1(); break;
		case F2: lb.runF2(); break;
		case NOOP: /* nothing to do for NOOP :) */
			break;
		}
	}
	public char toChar() {
		switch (kind) {
		case FORWARD: return 'F'; 
		case JUMP: return 'J'; 
		case LEFT: return 'L'; 
		case RIGHT: return 'R';
		case LIGHT: return 'O';
		case F1: return '1'; 
		case F2: return '2'; 
		case NOOP: 
		}
		return ' ';
	}
	public static LightBotInstruction noop() {
		return new LightBotInstruction(InstructionKind.NOOP);
	}
	
	public String toString() {
		switch (kind) { /* must match instructionNames, or the comboboxes in the editor won't work */
		case NOOP: return "noop";
		case FORWARD: return "forward"; 
		case JUMP: return "jump"; 
		case LEFT: return "left"; 
		case RIGHT: return "right";
		case LIGHT: return "light";
		case F1: return "f1"; 
		case F2: return "f2"; 
		}
		return "noop";
	}
	public boolean isNoop() {
		return kind == InstructionKind.NOOP;
	}
}
