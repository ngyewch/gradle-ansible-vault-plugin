package com.github.ngyewch.gradle.ansible.vault;

import java.io.IOException;
import java.io.InputStream;

public interface InputSource {
  byte[] getByteArray() throws IOException;

  InputStream getInputStream() throws IOException;
}
