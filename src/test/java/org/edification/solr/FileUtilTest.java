package org.edification.solr;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FileUtilTest {

	@Test
	public void listDirectoryFiles() {
		String directoryPath="C:\\work\\wenglish\\scripts";
		try (Stream<Path> fileStream = Files.walk(Paths.get(directoryPath), Integer.MAX_VALUE)) {
			fileStream.forEach(pathName-> {
				System.out.println(pathName.getParent().getFileName().toString()+" -- "+pathName);
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
