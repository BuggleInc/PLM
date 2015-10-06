package plm.test.git;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.model.LogHandler;
import plm.core.model.lesson.ExecutionProgress;
import plm.core.model.lesson.ExecutionProgress.outcomeKind;
import plm.core.model.lesson.Exercise;
import plm.core.model.session.SourceFile;
import plm.core.model.tracking.GitSpy;

@RunWith(MockitoJUnitRunner.class)
public class GitSpyTest {
	
	private GitSpy gitSpy;
	private File repoDir;
	private String userUUID;
	private Utils utils;
	
	public GitSpyTest() throws IOException, GitAPIException {
		repoDir = new File(System.getProperty("user.home") + System.getProperty("file.separator") + ".plm-test");

		userUUID = UUID.randomUUID().toString();
		
		Game game = new Game(userUUID, mock(LogHandler.class), new Locale("en"), "Java", false);
		

		gitSpy = new GitSpy(game, repoDir, game.getGitUtils(), userUUID);
		
		utils = new Utils();
		
		System.out.println("repoDir: "+ repoDir.getAbsolutePath());
	}
	
	@Before 
	public void setUp() {

	}
	
	@After
	public void tearDown() {
		utils.deleteRepo(repoDir);
	}
	
	@Test
	public void testCreateFilesWithoutPreviouslyCreatedFiles() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
		String code = utils.generateRandomString(32);
		String correction = utils.generateRandomString(32);
		String error = utils.generateRandomString(32);
		String mission = utils.generateRandomString(32);
		
		ExecutionProgress lastResult = Mockito.mock(ExecutionProgress.class);
		lastResult.language = Game.JAVA;
		lastResult.compilationError = error;
		
		SourceFile sf = Mockito.mock(SourceFile.class);
		Mockito.when(sf.getBody()).thenReturn(code);
		Mockito.when(sf.getCorrection()).thenReturn(correction);
		
		Exercise exo = Mockito.mock(Exercise.class);
		exo.lastResult = lastResult;
		Mockito.when(exo.getId()).thenReturn("exoTest");
		
		Mockito.when(exo.getMission(lastResult.language)).thenReturn(mission);
		Mockito.when(exo.getSourceFile(lastResult.language, 0)).thenReturn(sf);

		ArrayList<String> suffixes = new ArrayList<String>();
		suffixes.add(".code");
		suffixes.add(".correction");
		suffixes.add(".error");
		suffixes.add(".mission");
		
		for(String suffix:suffixes) {
			String fp = utils.getFilePath(repoDir, userUUID, exo, lastResult, suffix);
			if(new File(fp).exists()) {
				fail(fp+" should not exist previously...");
			}
		}
		
		Method method = GitSpy.class.getDeclaredMethod("createFiles", Exercise.class);
		method.setAccessible(true);
		method.invoke(gitSpy, exo);
		
		HashMap<String, String> hm = new HashMap<>();
		for(String suffix:suffixes) {
			hm.put(suffix, utils.getFileContent(repoDir, userUUID, exo, lastResult, suffix));
		}
		
		if(!hm.get(".code").equals(code)) {
			fail("Code file's content is different from code:\nexpected: "+code+"\nresult: "+hm.get(".code"));
		}
		if(!hm.get(".correction").equals(correction)) {
			fail("Correction file's content is different from correction:\nexpected: "+correction+"\nresult: "+hm.get(".correction"));
		}
		if(!hm.get(".error").equals(error)) {
			fail("Error file's content is different from error:\nexpected: "+error+"\nresult: "+hm.get(".error"));
		}
		if(!hm.get(".mission").equals(mission)) {
			fail("Mission file's content is different from mission:\nexpected: "+mission+"\nresult: "+hm.get(".mission"));
		}
	}

	
	@Test
	public void testCreateFilesWithPreviouslyCreatedFiles() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
		String code = utils.generateRandomString(32);
		String correction = utils.generateRandomString(32);
		String error = utils.generateRandomString(32);
		String mission = utils.generateRandomString(32);
		
		ExecutionProgress lastResult = Mockito.mock(ExecutionProgress.class);
		lastResult.language = Game.JAVA;
		lastResult.compilationError = null;
		lastResult.executionError = error;
		
		SourceFile sf = Mockito.mock(SourceFile.class);
		Mockito.when(sf.getBody()).thenReturn(code);
		Mockito.when(sf.getCorrection()).thenReturn(correction);
		
		Exercise exo = Mockito.mock(Exercise.class);
		exo.lastResult = lastResult;
		Mockito.when(exo.getId()).thenReturn("exoTest");
		
		Mockito.when(exo.getMission(lastResult.language)).thenReturn(mission);
		Mockito.when(exo.getSourceFile(lastResult.language, 0)).thenReturn(sf);

		ArrayList<String> suffixes = new ArrayList<String>();
		suffixes.add(".code");
		suffixes.add(".correction");
		suffixes.add(".error");
		suffixes.add(".mission");
		
		for(String suffix:suffixes) {
			String fp = utils.getFilePath(repoDir, userUUID, exo, lastResult, suffix);
			File file = new File(fp);
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(utils.generateRandomString(32));
			bw.close();
		}
		
		for(String suffix:suffixes) {
			String fp = utils.getFilePath(repoDir, userUUID, exo, lastResult, suffix);
			if(! (new File(fp).exists()) ) {
				fail(fp+" should have been created...");
			}
		}
		
		Method method = GitSpy.class.getDeclaredMethod("createFiles", Exercise.class);
		method.setAccessible(true);
		method.invoke(gitSpy, exo);
		
		HashMap<String, String> hm = new HashMap<>();
		for(String suffix:suffixes) {
			hm.put(suffix, utils.getFileContent(repoDir, userUUID, exo, lastResult, suffix));
		}
		
		if(!hm.get(".code").equals(code)) {
			fail("Code file's content is different from code:\nexpected: "+code+"\nresult: "+hm.get(".code"));
		}
		if(!hm.get(".correction").equals(correction)) {
			fail("Correction file's content is different from correction:\nexpected: "+correction+"\nresult: "+hm.get(".correction"));
		}
		if(!hm.get(".error").equals(error)) {
			fail("Error file's content is different from error:\nexpected: "+error+"\nresult: "+hm.get(".error"));
		}
		if(!hm.get(".mission").equals(mission)) {
			fail("Mission file's content is different from mission:\nexpected: "+mission+"\nresult: "+hm.get(".mission"));
		}
	}
	
	@Test
	public void testSuccessfulCheckSuccessWithoutPreviousSuccessShouldCreateDoneFile() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		ExecutionProgress lastResult = Mockito.mock(ExecutionProgress.class);
		lastResult.language = Game.JAVA;
		lastResult.outcome = outcomeKind.PASS;
		
		Exercise exo = Mockito.mock(Exercise.class);
		exo.lastResult = lastResult;
		Mockito.when(exo.getId()).thenReturn("exoTest");
		
		String fp = utils.getFilePath(repoDir, userUUID, exo, lastResult, ".DONE");
		if(new File(fp).exists()) {
			fail(fp+" should not exist previously...");
		}
		
		Method method = GitSpy.class.getDeclaredMethod("checkSuccess", Exercise.class);
		method.setAccessible(true);
		method.invoke(gitSpy, exo);
		
		if(! (new File(fp).exists()) ) {
			fail(fp+" should exist now...");
		}
	}
	
	@Test
	public void testSuccessfulCheckSuccessWithPreviousSuccessShouldKeepDoneFile() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
		ExecutionProgress lastResult = Mockito.mock(ExecutionProgress.class);
		lastResult.language = Game.JAVA;
		lastResult.outcome = outcomeKind.PASS;
		
		Exercise exo = Mockito.mock(Exercise.class);
		exo.lastResult = lastResult;
		Mockito.when(exo.getId()).thenReturn("exoTest");
		
		String fp = utils.getFilePath(repoDir, userUUID, exo, lastResult, ".DONE");
		File file = new File(fp);
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write("");
		bw.close();
		
		if(! (new File(fp).exists()) ) {
			fail(fp+" should exist previously...");
		}
		
		Method method = GitSpy.class.getDeclaredMethod("checkSuccess", Exercise.class);
		method.setAccessible(true);
		method.invoke(gitSpy, exo);
		
		if( !(new File(fp).exists()) ) {
			fail(fp+" should still exist now...");
		}
	}
	
	@Test
	public void testFailedCheckSuccessWithoutPreviousSuccessShouldDoNothing() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
		ExecutionProgress lastResult = Mockito.mock(ExecutionProgress.class);
		lastResult.language = Game.JAVA;
		lastResult.outcome = outcomeKind.FAIL;
		
		Exercise exo = Mockito.mock(Exercise.class);
		exo.lastResult = lastResult;
		Mockito.when(exo.getId()).thenReturn("exoTest");
		
		String fp = utils.getFilePath(repoDir, userUUID, exo, lastResult, ".DONE");
		if(new File(fp).exists()) {
			fail(fp+" should not exist previously...");
		}
		
		Method method = GitSpy.class.getDeclaredMethod("checkSuccess", Exercise.class);
		method.setAccessible(true);
		method.invoke(gitSpy, exo);
		
		if(new File(fp).exists()) {
			fail(fp+" should not have been created...");
		}
	}
	
	@Test
	public void testFailedCheckSuccessWithPreviousSuccessShouldDeleteDoneFile() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
		ExecutionProgress lastResult = Mockito.mock(ExecutionProgress.class);
		lastResult.language = Game.JAVA;
		lastResult.outcome = outcomeKind.FAIL;
		
		Exercise exo = Mockito.mock(Exercise.class);
		exo.lastResult = lastResult;
		Mockito.when(exo.getId()).thenReturn("exoTest");
		
		String fp = utils.getFilePath(repoDir, userUUID, exo, lastResult, ".DONE");
		File file = new File(fp);
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write("");
		bw.close();
		
		if(! (new File(fp).exists()) ) {
			fail(fp+" should exist previously...");
		}
		
		Method method = GitSpy.class.getDeclaredMethod("checkSuccess", Exercise.class);
		method.setAccessible(true);
		method.invoke(gitSpy, exo);
		
		if(new File(fp).exists()) {
			fail(fp+" should have been deleted...");
		}
	}
	
	@Test
	public void testDeleteFilesWithoutExistingFiles() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Exercise exo = Mockito.mock(Exercise.class);
		Mockito.when(exo.getId()).thenReturn("exoTest");
		
		List<String> suffixes = new ArrayList<String>();
		suffixes.add(".code");
		suffixes.add(".error");
		suffixes.add(".correction");
		suffixes.add(".mission");
		suffixes.add(".DONE");
		
		for(ProgrammingLanguage pl : Game.programmingLanguages) {
			String ext = "." + pl.getExt();	
			for(String suffix:suffixes) {
				File file = new File(repoDir, exo.getId() + ext + suffix);
				if(file.exists()) {
					fail(file.getAbsolutePath() + " should not exist...");
				}
			}
		}
		
		Method method = GitSpy.class.getDeclaredMethod("deleteFiles", Exercise.class);
		method.setAccessible(true);
		method.invoke(gitSpy, exo);
		
		for(ProgrammingLanguage pl : Game.programmingLanguages) {
			String ext = "." + pl.getExt();	
			for(String suffix:suffixes) {
				File file = new File(repoDir, exo.getId() + ext + suffix);
				if(file.exists()) {
					fail(file.getAbsolutePath() + " should still not exist...");
				}
			}
		}
	}
	
	@Test
	public void testDeleteFilesWithExistingFiles() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
		Exercise exo = Mockito.mock(Exercise.class);
		Mockito.when(exo.getId()).thenReturn("exoTest");
		
		List<String> suffixes = new ArrayList<String>();
		suffixes.add(".code");
		suffixes.add(".error");
		suffixes.add(".correction");
		suffixes.add(".mission");
		suffixes.add(".DONE");
		
		for(ProgrammingLanguage pl : Game.programmingLanguages) {
			for(String suffix:suffixes) {
				String fp = utils.getFilePath(repoDir, userUUID, exo, pl, suffix);
				File file = new File(fp);
				if(file.exists()) {
					fail(file.getAbsolutePath() + " should not yet exist...");
				}
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write("");
				bw.close();
			}
		}
		
		Method method = GitSpy.class.getDeclaredMethod("deleteFiles", Exercise.class);
		method.setAccessible(true);
		method.invoke(gitSpy, exo);
		
		for(ProgrammingLanguage pl : Game.programmingLanguages) {
			String ext = "." + pl.getExt();	
			for(String suffix:suffixes) {
				File file = new File(repoDir, exo.getId() + ext + suffix);
				if(file.exists()) {
					fail(file.getAbsolutePath() + " should have been deleted...");
				}
			}
		}
	}
}
