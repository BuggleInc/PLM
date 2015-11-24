package plm.core.lang;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import java.util.ArrayList;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

import org.xnap.commons.i18n.I18n;

import plm.core.PLMCompilerException;
import plm.core.model.LogHandler;
import plm.core.model.lesson.ExecutionProgress;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Exercise.StudentOrCorrection;
import plm.universe.Entity;
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

public class LangScala extends JVMCompiledLang {

	ScalaCompiler compiler;

	public LangScala(boolean isDebugEnabled) {
		super("Scala", "scala", isDebugEnabled);
		compiler = new ScalaCompiler(isDebugEnabled);
	}

	@Override
	public void compileExo(plm.core.model.session.SourceFile sourceFile, StudentOrCorrection whatToCompile, LogHandler logger, I18n i18n) throws PLMCompilerException {
		/* Make sure each run generate a new package to avoid that the loader cache prevent the reloading of the newly generated class */
		packageNameSuffix++;
		runtimePatterns.put("\\$package", "package "+packageName()+";import java.awt.Color;");

		try {
			compiler.reset();
			compiler.compile(className(sourceFile.getName()), sourceFile.getCompilableContent(runtimePatterns,whatToCompile), sourceFile.getOffset());
		} catch (PLMCompilerException e) {
			System.err.println(i18n.tr("Compilation error:"));
			System.err.println(e.getMessage());
			throw e;
		}
	}

	@Override
	public void compileExo(Exercise exo, LogHandler logger, StudentOrCorrection whatToCompile, I18n i18n) 
			throws PLMCompilerException {
		/* Make sure each run generate a new package to avoid that the loader cache prevent the reloading of the newly generated class */
		packageNameSuffix++;
		runtimePatterns.put("\\$package", 
				"package "+packageName()+";import java.awt.Color;");

		List<plm.core.model.session.SourceFile> sfs = exo.getSourceFilesList(this);
		if (sfs == null || sfs.isEmpty()) {
			String msg = exo.getName()+": No source to compile";
			System.err.println(msg);
			PLMCompilerException e = new PLMCompilerException(msg, null, null);
			exo.lastResult = ExecutionProgress.newCompilationError(e.getMessage(), this);				
			throw e;
		}

		try {
			compiler.reset();
			for (plm.core.model.session.SourceFile sf : sfs) {
				compiler.compile(className(sf.getName()), sf.getCompilableContent(runtimePatterns,whatToCompile), sf.getOffset());
			}
		} catch (PLMCompilerException e) {
			System.err.println(i18n.tr("Compilation error:"));
			System.err.println(e.getMessage());
			exo.lastResult = ExecutionProgress.newCompilationError(e.getMessage(), this);

			throw e;
		}
		
	}
	
	/** Converts {@code "foo.bar.baz"} to {@code "foo.bar.Scalabaz"}. */
	@Override
	public String nameOfCorrectionEntity(Exercise exo){
		String path = super.nameOfCorrectionEntity(exo);
		
		String[] components = path.split("\\.");
		StringBuilder result = new StringBuilder();
		int last = components.length - 1;
		for (int i = 0; i < last; i++) {
			result.append(components[i] + ".");
		}
		result.append("Scala" + components[last]);
		return result.toString();
	}

	@Override
	protected Entity mutateEntity(String newClassName)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		return (Entity) compiler.findClass(className(newClassName)).newInstance();
	}
}

/** In memory compiler of scala code. 
 *  This is highly inspired of https://github.com/twitter/util/blob/master/util-eval/src/main/scala/com/twitter/util/Eval.scala */
class ScalaCompiler {
	
	private PLMReporter reporter;
	private Settings settings;
	private Map<String, Class<?>> cache = new HashMap<String, Class<?>>();
	private Global global;
	private VirtualDirectory target;
	private ClassLoader classLoader = new AbstractFileClassLoader(target, this.getClass().getClassLoader());
	private boolean isDebugEnabled;
	
	public ScalaCompiler(boolean isDebugEnabled) {
		super();
		this.isDebugEnabled = isDebugEnabled;
		settings = new Settings();
		settings.nowarnings().tryToSetFromPropertyValue("true"); // warnings seem to be exceptions, and we don't want them to mess with us

		Option<VirtualDirectory> noAncestor = scala.Option$.MODULE$.apply(null);
		target = new VirtualDirectory("(memory)", noAncestor);
		settings.outputDirs().setSingleOutput(target);
		
		settings.usejavacp().tryToSetFromPropertyValue("true");
		//settings.usemanifestcp().tryToSetFromPropertyValue("true");
		reporter = new PLMReporter(settings, isDebugEnabled);
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
		if (isDebugEnabled) 
			System.out.println("Compiline souce "+name+" to scala (offset:"+offset+"):\n"+content);
		
		Run compiler = global.new Run();
		List<SourceFile> sources = new LinkedList<SourceFile>();
		
		sources.add(new BatchSourceFile(new VirtualFile(name) , content.toCharArray()));
		reporter.setOffset(offset);
		
		compiler.compileSources(JavaConverters.asScalaBufferConverter(sources).asScala().toList());
		
		if (isDebugEnabled && reporter.hasErrors())
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
		final int[] counts = new int[] {0, 0, 0};
		int offset=0;
		boolean isDebugEnabled;
		Vector<String> messages = new Vector<String>();
		Settings settings;

		public PLMReporter(Settings s, boolean isDebugEnabled) {
			settings = s;
			this.isDebugEnabled = isDebugEnabled;
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
		private int severityRank(Severity s) {
			String severityName = s.toString(); 
			int severity = -1;
			if (severityName.equals("INFO") || severityName.equals("scala.tools.nsc.reporters.Reporter$Severity@0"))
				severity = INFO;
			if (severityName.equals("WARNING") || severityName.equals("scala.tools.nsc.reporters.Reporter$Severity@1"))
				severity = WARNING;
			if (severityName.equals("ERROR") || severityName.equals("scala.tools.nsc.reporters.Reporter$Severity@2")) 
				severity = ERROR;
			if (severity == -1)
				throw new RuntimeException("Got an unknown severity: "+severityName+". Please adapt the PLM to this new version of scala (or whatever).");
			return severity;
		}
		
		@Override
		public void display(Position pos, String message, Severity _severity) {
			//System.err.println("Display pos:"+pos+"; msg:"+message+"; severity:"+_severity);

			String label = "";
			int severity = severityRank(_severity);
			if (severity == INFO && isDebugEnabled) 
				return;
			if (severity == WARNING)
				label = "warning: ";
			if (severity == ERROR)
				label = "error: "; 

			counts[severity]++;
			
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
			System.err.println(msg);

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
		
		@Override
		public int count(Object o) {
			return counts[severityRank((Severity) o)];
		}
		@Override
		public void resetCount(Object o) {
			counts[severityRank((Severity) o)] = 0;
		}
		@Override
		public void info0(Position pos, String msg, Object o, boolean force) {
			Severity s = (Severity) o;
			display(pos, msg, s);
		}
	}
}