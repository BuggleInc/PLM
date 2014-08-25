/** In memory compiler of scala code. This is highly inspired of https://github.com/twitter/util/blob/master/util-eval/src/main/scala/com/twitter/util/Eval.scala */

package plm.core.lang;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import plm.core.PLMCompilerException;
import plm.core.model.Game;
import scala.Option;
import scala.collection.JavaConverters;
import scala.reflect.internal.util.BatchSourceFile;
import scala.reflect.internal.util.Position;
import scala.reflect.internal.util.SourceFile;
import scala.reflect.io.VirtualDirectory;
import scala.reflect.io.VirtualFile;
import scala.tools.nsc.Global;
import scala.tools.nsc.Global.Run;
import scala.tools.nsc.Settings;
import scala.tools.nsc.interpreter.AbstractFileClassLoader;
import scala.tools.nsc.reporters.AbstractReporter;

public class CompilerScala {
	
	static private CompilerScala instance;
	public static CompilerScala getInstance() {
		if (instance == null)
			instance = new CompilerScala();
		return instance;
	}
	
	private PLMReporter reporter;
	private Settings settings;
	private Map<String, Class<?>> cache = new HashMap<String, Class<?>>();
	private Global global;
	private VirtualDirectory target;
	private ClassLoader classLoader = new AbstractFileClassLoader(target, this.getClass().getClassLoader());
	
	private CompilerScala() {
		super();
		settings = new Settings();
		settings.nowarnings().tryToSetFromPropertyValue("true"); // warnings seem to be exceptions, and we don't want them to mess with us

		Option<VirtualDirectory> noAncestor = scala.Option$.MODULE$.apply(null);
		target = new VirtualDirectory("(memory)", noAncestor);
		settings.outputDirs().setSingleOutput(target);
		
		settings.usejavacp().tryToSetFromPropertyValue("true");
		//settings.usemanifestcp().tryToSetFromPropertyValue("true");
		reporter = new PLMReporter(settings);
		global = new Global(settings,reporter);
	}

	public void reset() {
		reporter.reset();
		reporter.setOffset(0);
		target.clear();
		cache = new HashMap<String, Class<?>>();
		classLoader = new AbstractFileClassLoader(target, this.getClass().getClassLoader());
	}

	public void compile(String name,String content,int offset) throws PLMCompilerException {
		
		Run compiler = global.new Run();
		List<SourceFile> sources = new LinkedList<SourceFile>();
		
		sources.add(new BatchSourceFile(new VirtualFile(name) , content.toCharArray()));
		reporter.setOffset(offset);
		
		compiler.compileSources(JavaConverters.asScalaBufferConverter(sources).asScala().toList());
		
		if (Game.getInstance().isDebugEnabled() && reporter.hasErrors())
			System.out.println("Here is the scala source code of "+name+" (offset:"+offset+"): "+content);
		reporter.throwExceptionOnNeed();
	}
	public Class<?> findClass(String className) {
		synchronized (this) {
			if (!cache.containsKey(className)) {
				Class<?> res;
				try {
					res = classLoader.loadClass(className);
				} catch (ClassNotFoundException e) {
					res = null;
				}
				cache.put(className, res);
			}

			return cache.get(className);			
		}
	}
	
	class PLMReporter extends AbstractReporter {
		final static int INFO = 0;
		final static int WARNING = 1;
		final static int ERROR = 2;
		int offset=0;
		Vector<String> messages = new Vector<String>();
		Settings settings;

		public PLMReporter(Settings s) {
			settings = s;
		}
		public void setOffset(int _offset) {
			this.offset = _offset;
		}
		@Override
		public Settings settings() {
			return settings;
		}
		@Override
		public void displayPrompt() { 
			/* Don't do that, pal. */ 
		}
		@Override
		public void display(Position pos, String message, Severity _severity) {
			String severityName = _severity.toString(); 
			String label = "";
			int severity = -1;
			if (severityName.equals("INFO") || severityName.equals("scala.tools.nsc.reporters.Reporter$Severity@0"))
				severity = INFO;
			if (severityName.equals("WARNING") || severityName.equals("scala.tools.nsc.reporters.Reporter$Severity@1")) {
				severity = WARNING;
				label= "warning: ";
			}
			if (severityName.equals("ERROR") || severityName.equals("scala.tools.nsc.reporters.Reporter$Severity@2")) {
				severity = ERROR;
				label = "error: ";
			}
			if (severity == -1)
				throw new RuntimeException("Got an unknown severity: "+severityName+". Please adapt the PLM to this new version of scala (or whatever).");
			if (severity == INFO && !Game.getInstance().isDebugEnabled()) 
				return;

			int lineNum = -1;
			try {
				lineNum = pos.line() - offset;
			} catch (Throwable t) {
				// That's fine if the line number is not defined.
			}

			String name = pos.source().path();
			int lastDot = name.lastIndexOf('.');
			if (lastDot != -1)
				name = name.substring(lastDot+1);
			String msg = name+(lineNum == -1? "": ":"+lineNum) +": "+label+message;

			// Append the line content and a position marker, if possible
			if (pos != null && pos.isDefined()) {
				msg += "\n"+pos.inUltimateSource(pos.source()).lineContent()+"\n";
				for (int i=0;i<pos.column()-1;i++)
					msg += " ";
				msg += "^";
			}

			messages.add(msg);
		}
		public void throwExceptionOnNeed() throws PLMCompilerException {
			if (hasErrors()) {
				StringBuffer sb = new StringBuffer();
				for (String s : messages)
					sb.append(s);
				throw new PLMCompilerException(sb.toString(), null, null);
			}
		}
		@Override
		public void reset() {
			super.reset();
			messages.removeAllElements();
		}
	}
}