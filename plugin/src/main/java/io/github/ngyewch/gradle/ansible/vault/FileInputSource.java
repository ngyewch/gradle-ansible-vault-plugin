package io.github.ngyewch.gradle.ansible.vault;

import java.io.*;
import org.apache.commons.io.FileUtils;

public class FileInputSource implements InputSource {
  private final File file;

  public FileInputSource(File file) {
    super();

    this.file = file;
  }

  public File getFile() {
    return file;
  }

  @Override
  public byte[] getByteArray() throws IOException {
    return FileUtils.readFileToByteArray(file);
  }

  @Override
  public InputStream getInputStream() throws IOException {
    return new FileInputStream(file);
  }
}
