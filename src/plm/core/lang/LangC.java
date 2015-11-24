package plm.core.lang;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

import org.xnap.commons.i18n.I18n;

import plm.core.PLMCompilerException;
import plm.core.model.Game;
import plm.core.model.LogHandler;
import plm.core.model.lesson.ExecutionProgress;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Exercise.StudentOrCorrection;
import plm.core.model.session.SourceFile;
import plm.core.utils.ValgrindParser;
import plm.universe.Entity;

public class LangC extends ProgrammingLanguage {

	public LangC(boolean isDebugEnabled) {
		super("C", "c", isDebugEnabled);
	}

	@Override
	public void compileExo(Exercise exo, LogHandler logger, StudentOrCorrection whatToCompile, I18n i18n) 
			throws PLMCompilerException {
		
		List<SourceFile> sfs = exo.getSourceFilesList(Game.C);
		if (sfs == null || sfs.isEmpty()) {
			String msg = exo.getName()+": No source to compile";
			System.err.println(msg);
			exo.lastResult = ExecutionProgress.newCompilationError(msg, this);	
			throw new PLMCompilerException(msg, null, null);
		}

		for (SourceFile sf : sfs){
			String code = sf.getCompilableContent(runtimePatterns,whatToCompile);
			compile(code,exo.getId(),exo.lastResult, i18n);
			
		}
	}


	private void compile(String code, String executable, ExecutionProgress lastResult, I18n i18n) throws PLMCompilerException{
		
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
				lastResult = ExecutionProgress.newCompilationError(e.getMessage(), this);				
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
				System.err.println(i18n.tr("Compilation error:"));
				System.err.println(e.getMessage());
				lastResult = ExecutionProgress.newCompilationError(e.getMessage(), this);

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
			StudentOrCorrection whatToMutate, I18n i18n, int nbError) {
		
		return old; /* Nothing to do, actually */
	}

	@Override
	public void runEntity(final Entity ent, final ExecutionProgress progress, I18n i18n) {
		Runtime runtime = Runtime.getRuntime();
		final StringBuffer resCompilationErr = new StringBuffer();

		try {

			String tempdir = System.getProperty("java.io.tmpdir")+"/plmTmp";
			File saveDir = new File(tempdir+"/bin");

			int nb=(int)(Math.random()*1000);
			final File randomFile = new File(tempdir+"/tmp_"+nb+".txt");
			if(!randomFile.createNewFile()){
				System.out.println("Error creating a temporary file, make sure "+saveDir.getAbsolutePath()+" is writable");
				return;
			}

			final File valgrindFile=new File(tempdir+"/valgrind_"+nb+".xml");
			String extension="";
			String arg1[] = {};
			String os = System.getProperty("os.name").toLowerCase();
			final StringBuffer valgrind=new StringBuffer("");
			String executable = "";
			// FIXME: 
			// How to find executable?
			/*
			if(ent.getScript(Game.C)!=null){
				executable=ent.getScript(Game.C);
			}else{
				executable= getGame().getCurrentLesson().getCurrentExercise().getId();
			}
			if (os.indexOf("win") >= 0) {
				extension=".exe";
				arg1 = new String[3];
				arg1[0]="cmd.exe";
				arg1[1]="/c";
				arg1[2]=saveDir.getAbsolutePath()+"/"+executable+""+extension+" "+randomFile.getAbsolutePath();
			} else {
				//test if valgrind exist
				Runtime r = Runtime.getRuntime();
				try {
					r.exec("valgrind --version");
					if(valgrindFile.createNewFile()){
						valgrind.append("valgrind --xml=yes --xml-file=\""+valgrindFile.getAbsolutePath()+"\"");
					}
				} catch (IOException e) {
					System.err.println("Valgrind does not seem to be installed");
				}
				arg1 = new String[3];
				arg1[0]="/bin/sh";
				arg1[1]="-c";
				arg1[2]=valgrind+" "+saveDir.getAbsolutePath()+"/"+executable+""+extension+" "+randomFile.getAbsolutePath();
			}
			*/
			File exec = new File(saveDir.getAbsolutePath()+"/"+executable+""+extension);
			if(!exec.exists()){
				System.err.println(i18n.tr("Error, please recompile the exercise"));
				randomFile.delete();
				if(valgrindFile.exists()){
					valgrindFile.delete();
				}
				return;
			}else if(!exec.canExecute() || !exec.isFile()){
				System.err.println(i18n.tr("Error, please recompile the exercise"));
				randomFile.delete();
				if(valgrindFile.exists()){
					valgrindFile.delete();
				}
				return;
			}

			final Process process = runtime.exec(arg1);

			final BufferedWriter bwriter = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
			Thread reader = new Thread() {
				public void run() {
					try {
						BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
						try {
							int truc;
							String str = "";
							while((truc=reader.read())!=-1){
								if(truc!=10){
									str+=(char)truc;
								}else{
									ent.command(str, bwriter);
									str="";
								}
							}

						} finally {
							reader.close();
						}
					} catch(IOException ioe) {
						ioe.printStackTrace();
					}
				}
			};


			Thread error = new Thread() {
				public void run() {
					try {
						InputStreamReader isr = new InputStreamReader(process.getErrorStream());
						BufferedReader err = new BufferedReader(isr);

						if(valgrind.length()>0){
							try {
								process.waitFor();
								StringBuffer errmsg = ValgrindParser.parse(valgrindFile);
								resCompilationErr.append(errmsg);
								System.err.println(errmsg);
								progress.executionError += errmsg;
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}else{
							String line = "";
							while((line = err.readLine()) != null) {
								if(line.contains("<")){
									resCompilationErr.append(line+"\n");
								}
								System.err.println("error: "+line);
							}
						}

					} catch(IOException ioe) {
						ioe.printStackTrace();	
					}

				}
			};

			final StringBuffer continu = new StringBuffer("");
			Thread print = new Thread() {
				public void run() {
					try {
						InputStream ips=new FileInputStream(randomFile.getAbsolutePath()); 
						InputStreamReader ipsr=new InputStreamReader(ips);
						BufferedReader br=new BufferedReader(ipsr);
						try {
							int truc;
							String str = "";
							while(continu.length()==0){
								truc=br.read();
								if(truc!=-1){
									if(((char)truc)!='\n'){
										str+=(char)truc;
									}else{
										System.out.println(str);
										str="";
									}
								}
							}
							while((truc=br.read())!=-1){
								if(truc!=10){
									str+=(char)truc;
								}else{
									System.out.println(str);
									str="";
								}
							}
						} finally {
							br.close();
						}
					} catch(IOException ioe) {
						ioe.printStackTrace();
					}
				}
			};

			reader.start();
			error.start();
			print.start();
			process.waitFor();
			reader.join();
			error.join();
			continu.append("fin");
			print.join();

			bwriter.close();


			randomFile.delete();

			if(valgrindFile.exists()){
				valgrindFile.delete();
			}
			
			if(resCompilationErr.length()>0){
				System.err.println(resCompilationErr.toString());
				progress.setCompilationError(resCompilationErr.toString());
			}


		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void compileExo(SourceFile sourceFile,
			StudentOrCorrection whatToCompile, LogHandler logger, I18n i18n)
			throws PLMCompilerException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Entity> mutateEntities(String newClassName, SourceFile sourceFile,
			StudentOrCorrection whatToCompile, List<Entity> old)
			throws PLMCompilerException {
		// TODO Auto-generated method stub
		return null;
	}

}
