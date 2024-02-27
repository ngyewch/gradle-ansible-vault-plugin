package com.github.ngyewch.gradle.ansible.vault;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import javax.annotation.Nullable;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.gradle.api.provider.Property;

public abstract class AnsibleVaultExtension {

  public abstract Property<File> getPasswordFile();

  @Nullable
  private String getPassword() throws IOException {
    if (getPasswordFile().isPresent()) {
      return StringUtils.trimToNull(
          FileUtils.readFileToString(getPasswordFile().get(), StandardCharsets.UTF_8));
    } else {
      return null;
    }
  }

  public SimpleFile decrypt(File f) throws IOException, GeneralSecurityException {
    final String password = getPassword();
    if (password == null) {
      throw new RuntimeException("password not specified");
    }
    final AnsibleVault ansibleVault = AnsibleVault.readFrom(new FileInputStream(f));
    return new SimpleFile(new ByteArrayInputSource(AnsibleVaultDecryptor.decrypt(ansibleVault, password)));
  }

  public SimpleFile file(File f) {
    return new SimpleFile(new FileInputSource(f));
  }
}
