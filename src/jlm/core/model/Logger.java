package jlm.core.model;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


/**
 * The log console used within JLM. It is setup by Game as the default System.out, 
 * and points into the console pane at the bottom of the interface.
 */
public class Logger extends PrintStream {
	private LogWriter writer;
	private static boolean ENABLED = true;

	public static void log(String method, String msg) {
		if (Logger.ENABLED)
			System.out.println("[" + System.currentTimeMillis() + "] " + method + " " + msg);
	}
	
	public Logger(LogWriter w){
		super(new ByteArrayOutputStream());
		writer = w;
	}

	@Override
	public void print(boolean b) {
		writer.log(""+b);
	}

	@Override
	public void print(char c) {
		writer.log(""+c);
	}

	@Override
	public void print(char[] s) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(s);
		writer.log(stringBuilder.toString());
	}

	@Override
	public void print(double d) {
		writer.log(""+d);
	}

	@Override
	public void print(float f) {
		writer.log(""+f);
	}

	@Override
	public void print(int i) {
		writer.log(""+i);
	}

	@Override
	public void print(long l) {
		writer.log(""+l);
	}

	@Override
	public void print(Object obj) {
		writer.log(obj.toString());
	}

	@Override
	public void print(String s) {
		writer.log(s);
	}

	@Override
	public void println() {
		writer.log("\n");
	}

	@Override
	public void println(boolean x) {
		writer.log(""+x+"\n");
	}

	@Override
	public void println(char x) {
		writer.log(""+x+"\n");
	}

	@Override
	public void println(char[] x) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(x);
		stringBuilder.append("\n");
		writer.log(stringBuilder.toString());
	}

	@Override
	public void println(double x) {
		writer.log(""+x+"\n");
	}

	@Override
	public void println(float x) {
		writer.log(""+x+"\n");
	}

	@Override
	public void println(int x) {
		writer.log(""+x+"\n");
	}

	@Override
	public void println(long x) {
		writer.log(""+x+"\n");
	}

	@Override
	public void println(Object x) {
		writer.log(x.toString()+"\n");
	}

	@Override
	public void println(String x) {
		writer.log(x+"\n");
	}
}
