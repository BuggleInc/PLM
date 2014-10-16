package plm.test.git;

import static java.nio.file.FileVisitResult.CONTINUE;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Random;

import org.eclipse.jgit.api.Git;

import plm.core.lang.ProgrammingLanguage;
import plm.core.model.lesson.ExecutionProgress;
import plm.core.model.lesson.Exercise;
import plm.core.model.tracking.GitUtils;

public class Utils {
	
	private final String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private Random rnd = new Random();
	
	public void deleteRepo(File repoDirectory) {
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
	
	public String generateRandomString(int len) {
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<len; i++) {
			sb.append(characters.charAt(rnd.nextInt(characters.length())));
		}
		return sb.toString();
	}
	
	public void generateFile(File repoDir, String name, String content) {
		File file = new File(repoDir.getAbsolutePath() + System.getProperty("file.separator") + name);
		FileWriter fw;
		try {
			fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getFileContent(String path) throws IOException {
		File file = new File(path);
		return new String(Files.readAllBytes(file.toPath()));
	}
	
	public String getFileContent(File repoDir, String userUUID, Exercise exo, ExecutionProgress lastResult, String suffix) throws IOException {
		return getFileContent(getFilePath(repoDir, userUUID, exo, lastResult, suffix));
	}
	
	public String getFilePath(File repoDir, String userUUID, Exercise exo, ProgrammingLanguage pl, String suffix) {
		return repoDir.getPath() + System.getProperty("file.separator") + 
				userUUID + System.getProperty("file.separator") + 
				exo.getId() + "." + pl.getExt() + suffix;
	}
	
	public String getFilePath(File repoDir, String userUUID, Exercise exo, ExecutionProgress lastResult, String suffix) {
		return getFilePath(repoDir, userUUID, exo, lastResult.language, suffix);
	}
	
	public Git getGit(GitUtils gitUtils) {
		Field f = null;
		try {
			f = gitUtils.getClass().getDeclaredField("git");
			f.setAccessible(true);
			return (Git) f.get(gitUtils);
		} catch (NoSuchFieldException | SecurityException e) {
			System.err.println("An error occurred while retrieving gitUtils' git field...");
			e.printStackTrace();
		} catch (IllegalArgumentException | IllegalAccessException e) {
			System.err.println("An error occurred while retrieving gitUtils' git field's value...");
			e.printStackTrace();
		}
		return null;
	}
}
