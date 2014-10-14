package plm.test;

import static java.nio.file.FileVisitResult.CONTINUE;
import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.xnap.commons.i18n.I18nFactory;

import plm.core.model.Game;
import plm.core.model.User;
import plm.core.model.Users;
import plm.core.model.lesson.ExecutionProgress;
import plm.core.model.lesson.ExecutionProgress.outcomeKind;
import plm.core.model.lesson.Exercise;
import plm.core.model.session.SourceFile;
import plm.core.model.tracking.GitSpy;
import plm.core.utils.FileUtils;

@RunWith(MockitoJUnitRunner.class)
public class GitSpyTest {
	
	private GitSpy gitSpy;
	private final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private Random rnd = new Random();
	private File repoDirectory;
	private String userUUID;
	
	public GitSpyTest() throws IOException, GitAPIException {
		repoDirectory = new File(System.getProperty("user.home") + System.getProperty("file.separator") + ".plm-test");
		Users users = Mockito.mock(Users.class);
		User user = Mockito.mock(User.class);

		Game.i18n = I18nFactory.getI18n(getClass(),"org.plm.i18n.Messages",FileUtils.getLocale(), I18nFactory.FALLBACK);
		
		userUUID = UUID.randomUUID().toString();
		
		Mockito.when(user.getUserUUIDasString()).thenReturn(userUUID);
		Mockito.when(users.getCurrentUser()).thenReturn(user);
		gitSpy = new GitSpy(repoDirectory, users);
		System.out.println("repoDirectory: "+ repoDirectory.getAbsolutePath());
	}
	
	@Before 
	public void setUp() {

	}
	
	@After
	public void tearDown() {
		deleteRepo(repoDirectory);
	}
	
	@Test
	public void testCreateFilesWithoutPreviouslyCreatedFiles() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
		String code = generateRandomString(32);
		String correction = generateRandomString(32);
		String error = generateRandomString(32);
		String mission = generateRandomString(32);
		
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
			if(new File(getFilePath(exo, lastResult, suffix)).exists()) {
				fail(getFilePath(exo, lastResult, suffix)+" should not exist previously...");
			}
		}
		
		Method method = GitSpy.class.getDeclaredMethod("createFiles", Exercise.class);
		method.setAccessible(true);
		method.invoke(gitSpy, exo);
		
		HashMap<String, String> hm = new HashMap<>();
		for(String suffix:suffixes) {
			hm.put(suffix, getFileContent(exo, lastResult, suffix));
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
		String code = generateRandomString(32);
		String correction = generateRandomString(32);
		String error = generateRandomString(32);
		String mission = generateRandomString(32);
		
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
			File file = new File(getFilePath(exo, lastResult, suffix));
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(generateRandomString(32));
			bw.close();
		}
		
		for(String suffix:suffixes) {
			if(! (new File(getFilePath(exo, lastResult, suffix)).exists()) ) {
				fail(getFilePath(exo, lastResult, suffix)+" should have been created...");
			}
		}
		
		Method method = GitSpy.class.getDeclaredMethod("createFiles", Exercise.class);
		method.setAccessible(true);
		method.invoke(gitSpy, exo);
		
		HashMap<String, String> hm = new HashMap<>();
		for(String suffix:suffixes) {
			hm.put(suffix, getFileContent(exo, lastResult, suffix));
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
		
		if(new File(getFilePath(exo, lastResult, ".DONE")).exists()) {
			fail(getFilePath(exo, lastResult, ".DONE")+" should not exist previously...");
		}
		
		Method method = GitSpy.class.getDeclaredMethod("checkSuccess", Exercise.class);
		method.setAccessible(true);
		method.invoke(gitSpy, exo);
		
		if(! (new File(getFilePath(exo, lastResult, ".DONE")).exists()) ) {
			fail(getFilePath(exo, lastResult, ".DONE")+" should exist now...");
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
		
		File file = new File(getFilePath(exo, lastResult, ".DONE"));
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write("");
		bw.close();
		
		if(! (new File(getFilePath(exo, lastResult, ".DONE")).exists()) ) {
			fail(getFilePath(exo, lastResult, ".DONE")+" should exist previously...");
		}
		
		Method method = GitSpy.class.getDeclaredMethod("checkSuccess", Exercise.class);
		method.setAccessible(true);
		method.invoke(gitSpy, exo);
		
		if( !(new File(getFilePath(exo, lastResult, ".DONE")).exists()) ) {
			fail(getFilePath(exo, lastResult, ".DONE")+" should still exist now...");
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
		
		if(new File(getFilePath(exo, lastResult, ".DONE")).exists()) {
			fail(getFilePath(exo, lastResult, ".DONE")+" should not exist previously...");
		}
		
		Method method = GitSpy.class.getDeclaredMethod("checkSuccess", Exercise.class);
		method.setAccessible(true);
		method.invoke(gitSpy, exo);
		
		if(new File(getFilePath(exo, lastResult, ".DONE")).exists()) {
			fail(getFilePath(exo, lastResult, ".DONE")+" should not have been created...");
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
		
		File file = new File(getFilePath(exo, lastResult, ".DONE"));
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write("");
		bw.close();
		
		if(! (new File(getFilePath(exo, lastResult, ".DONE")).exists()) ) {
			fail(getFilePath(exo, lastResult, ".DONE")+" should exist previously...");
		}
		
		Method method = GitSpy.class.getDeclaredMethod("checkSuccess", Exercise.class);
		method.setAccessible(true);
		method.invoke(gitSpy, exo);
		
		if(new File(getFilePath(exo, lastResult, ".DONE")).exists()) {
			fail(getFilePath(exo, lastResult, ".DONE")+" should have been deleted...");
		}
	}
	
	private void deleteRepo(File repoDirectory) {
		try {
			Files.walkFileTree(repoDirectory.toPath(), new SimpleFileVisitor<Path>() {
				 
			      @Override
			      public FileVisitResult visitFile(Path file,
			              BasicFileAttributes attrs) throws IOException {
			 
			          Files.delete(file);
			          return CONTINUE;
			      }
			 
			      @Override
			      public FileVisitResult postVisitDirectory(Path dir,
			              IOException exc) throws IOException {
			 
			          if (exc == null) {
			              Files.delete(dir);
			              return CONTINUE;
			          } else {
			              throw exc;
			          }
			      }
			 
			  });
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String generateRandomString(int len) {
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<len; i++) {
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		}
		return sb.toString();
	}
	
	private String getFileContent(Exercise exo, ExecutionProgress lastResult, String suffix) throws IOException {
		File file = new File(getFilePath(exo, lastResult, suffix));
		System.out.println(file.getAbsolutePath().toString());
		return new String(Files.readAllBytes(file.toPath()));
	}
	
	private String getFilePath(Exercise exo, ExecutionProgress lastResult, String suffix) {
		return repoDirectory.getPath() + System.getProperty("file.separator") + 
				userUUID + System.getProperty("file.separator") + 
				exo.getId() + "." + lastResult.language.getExt() + suffix;
	}
}
