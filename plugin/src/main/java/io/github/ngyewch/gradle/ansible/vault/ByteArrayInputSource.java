package io.github.ngyewch.gradle.ansible.vault;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ByteArrayInputSource implements InputSource {
  private final byte[] data;

  public ByteArrayInputSource(byte[] data) {
    super();

    this.data = data;
  }

  public byte[] getData() {
    return data;
  }

  @Override
  public byte[] getByteArray() throws IOException {
    return data;
  }

  @Override
  public InputStream getInputStream() throws IOException {
    return new ByteArrayInputStream(data);
  }
}
