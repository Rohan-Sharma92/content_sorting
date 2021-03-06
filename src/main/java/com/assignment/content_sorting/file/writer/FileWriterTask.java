package com.assignment.content_sorting.file.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Set;

/**
 * The Class FileWriterTask.
 * @author Rohan
 */
public class FileWriterTask{

	  /**
  	 * Move files from source path to destine path.
  	 *
  	 * @param source the source
  	 * @param destine the destine
  	 * @throws IOException Signals that an I/O exception has occurred.
  	 */
    public void move(File source, File destine) throws IOException {
        try {
            Files.copy(source.toPath(), destine.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * Delete the files.
     *
     * @param files list of files to be deleted
     */
    public void delete(Set<File> files) {
        for (File file : files) {
            file.delete();
        }
    }
    
	/**
	 * Write lines.
	 *
	 * @param lines the lines
	 * @param file the file
	 * @param append the append
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void writeLines(List<String> lines, File file, Boolean append) throws IOException{
        try (BufferedWriter bw = new BufferedWriter(new java.io.FileWriter(file, append))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
            bw.flush();
        } catch (IOException e) {
            throw e;
        }
    }
}
