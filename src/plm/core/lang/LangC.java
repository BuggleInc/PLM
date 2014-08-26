package plm.core.lang;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

import plm.core.PLMCompilerException;
import plm.core.model.Game;
import plm.core.model.LogWriter;
import plm.core.model.lesson.ExecutionProgress;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Exercise.StudentOrCorrection;
import plm.core.model.session.SourceFile;
import plm.core.ui.ResourcesCache;
import plm.universe.Entity;

public class LangC extends ProgrammingLanguage {

	public LangC() {
		super("C","c",ResourcesCache.getIcon("img/lang_c.png"));
	}

	@Override
	public void compileExo(Exercise exo, LogWriter out, StudentOrCorrection whatToCompile) 
			throws PLMCompilerException {
		
		List<SourceFile> sfs = exo.getSourceFilesList(Game.C);
		if (sfs == null || sfs.isEmpty()) {
			String msg = exo.getName()+": No source to compile";
			System.err.println(msg);
			exo.lastResult = ExecutionProgress.newCompilationError(msg);				
			throw new PLMCompilerException(msg, null, null);
		}

		for (SourceFile sf : sfs){
			String code = sf.getCompilableContent(runtimePatterns,whatToCompile);
			compile(code,exo.getId(),exo.lastResult);
			
		}
	}


	private void compile(String code, String executable, ExecutionProgress lastResult) throws PLMCompilerException{
		
		Runtime runtime = Runtime.getRuntime();

		final StringBuffer resCompilationErr=new StringBuffer();
		try {
			String tempdir = System.getProperty("java.io.tmpdir");
			
			File plmDirTmp = new File(tempdir+"/plmTmp");
			if(!plmDirTmp.exists()){
				plmDirTmp.mkdir();
			}
			
			File saveDirBin = new File(plmDirTmp.getAbsolutePath()+"/bin");
			if(!saveDirBin.exists()){
				saveDirBin.mkdir();
			}
			String saveDirPathBin = saveDirBin.getAbsolutePath();
			
			String extension="";
			String os = System.getProperty("os.name").toLowerCase();
			if (os.indexOf("win") >= 0) {
				extension=".exe";
			}
			
			File exec = new File(saveDirPathBin+"/"+executable+extension);
			if(exec.exists()){
				exec.delete();
			}

			String remote="";
			
			if(code.contains("RemoteBuggle")){
				remote = "RemoteBuggle";
			}else if(code.contains("RemoteTurtle")){
				remote = "RemoteTurtle";
			}else if(code.contains("RemoteSort")){
				remote = "RemoteSort";
			}else if(code.contains("RemoteFlag")){
				remote = "RemoteFlag";
			}else if(code.contains("RemoteBaseball")){
				remote = "RemoteBaseball";
			}else if(code.contains("RemotePancake")){
				remote = "RemotePancake";
			}else if(code.contains("RemoteHanoi")){
				remote = "RemoteHanoi";
			}else{
				PLMCompilerException e = new PLMCompilerException("This world is not implemented", null, null);
				lastResult = ExecutionProgress.newCompilationError(e.getMessage());				
				throw e;
			}
			
			BufferedReader cRemote = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("resources/langages/c/src/Remote.c")));
			BufferedReader hRemote = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("resources/langages/c/include/Remote.h")));
			BufferedReader cRemoteWorld = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("resources/langages/c/src/"+remote+".c")));
			BufferedReader hRemoteWorld = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("resources/langages/c/include/"+remote+".h")));
			String line;
			StringBuffer preCode = new StringBuffer();
			
			while((line=hRemote.readLine())!=null){
				preCode.append(line+"\n");
			}
			line="";
			while((line=cRemote.readLine())!=null){
				preCode.append(line+"\n");
			}
			line="";
			while((line=hRemoteWorld.readLine())!=null){
				preCode.append(line+"\n");
			}
			line="";
			while((line=cRemoteWorld.readLine())!=null){
				preCode.append(line+"\n");
			}
			cRemoteWorld.close();
			hRemoteWorld.close();
			cRemoteWorld.close();
			hRemote.close();
			
			String[] arg1;
			if (os.indexOf("win") >= 0) {
				arg1 = new String[3];
				arg1[0]="cmd.exe";
				arg1[1]="/c";
				arg1[2]="gcc -g -Wall -lm -o \""+exec+"\" - ";
			} else {
				arg1 = new String[3];
				arg1[0]="/bin/sh";
				arg1[1]="-c";
				arg1[2]="gcc -g -x c -Wall -lm -o \""+exec+"\" - ";
			}

			final Process process = runtime.exec(arg1);
			final BufferedWriter bwriter = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
			bwriter.write(preCode.toString()+"\n"+code);
			bwriter.close();
			Thread reader = new Thread() {
				public void run() {
					try {
						BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
						String line = "";
						try {
							while((line = reader.readLine()) != null ) {
								resCompilationErr.append(line+"\n");
							}
						} finally {
							reader.close();
						}
					} catch(IOException ioe) {
						ioe.printStackTrace();
					}
				}
			};
			reader.run();

			Thread error = new Thread() {
				public void run() {
					try {
						BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
						String line = "";
						try {
							while((line = reader.readLine()) != null ) {
								resCompilationErr.append(line+"\n");
							}
						} finally {
							reader.close();
						}
					} catch(IOException ioe) {
						ioe.printStackTrace();
					}
				}
			};
			error.run();

			process.waitFor();

			if(resCompilationErr.length()>0){
				PLMCompilerException e = new PLMCompilerException(resCompilationErr.toString(), null, null);
				System.err.println(Game.i18n.tr("Compilation error:"));
				System.err.println(e.getMessage());
				lastResult = ExecutionProgress.newCompilationError(e.getMessage());

				throw e;
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch(InterruptedException e){
			e.printStackTrace();
		}
	}

	@Override
	public List<Entity> mutateEntities(Exercise exercise, List<Entity> old,
			StudentOrCorrection whatToMutate) {
		
		return old; /* Nothing to do, actually */
	}

}
