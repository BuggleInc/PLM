package plm.test.git;

import static java.nio.file.FileVisitResult.CONTINUE;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Random;

import plm.core.lang.ProgrammingLanguage;
import plm.core.model.lesson.ExecutionProgress;
import plm.core.model.lesson.Exercise;

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
	
	public String getFileContent(File repoDir, String userUUID, Exercise exo, ExecutionProgress lastResult, String suffix) throws IOException {
		File file = new File(getFilePath(repoDir, userUUID, exo, lastResult, suffix));
		System.out.println(file.getAbsolutePath().toString());
		return new String(Files.readAllBytes(file.toPath()));
	}
	
	public String getFilePath(File repoDir, String userUUID, Exercise exo, ProgrammingLanguage pl, String suffix) {
		return repoDir.getPath() + System.getProperty("file.separator") + 
				userUUID + System.getProperty("file.separator") + 
				exo.getId() + "." + pl.getExt() + suffix;
	}
	
	public String getFilePath(File repoDir, String userUUID, Exercise exo, ExecutionProgress lastResult, String suffix) {
		return getFilePath(repoDir, userUUID, exo, lastResult.language, suffix);
	}
}
