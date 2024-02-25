package com.github.ngyewch.gradle.ansible.vault;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Map;
import javax.annotation.Nullable;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.gradle.api.provider.Property;

public abstract class AnsibleVaultExtension {

  public abstract Property<File> getPasswordFile();

  @Nullable private String getPassword() throws IOException {
    if (getPasswordFile().isPresent()) {
      return StringUtils.trimToNull(
          FileUtils.readFileToString(getPasswordFile().get(), StandardCharsets.UTF_8));
    } else {
      return null;
    }
  }

  public byte[] toBytes(File f) throws IOException, GeneralSecurityException {
    final String password = getPassword();
    if (password == null) {
      throw new RuntimeException("password not specified");
    }
    final AnsibleVault ansibleVault = AnsibleVault.readFrom(new FileInputStream(f));
    return AnsibleVaultDecryptor.decrypt(ansibleVault, password);
  }

  public Map unmarshalJSON(File f) throws IOException, GeneralSecurityException {
    return unmarshalJSON(f, Map.class);
  }

  public <T> T unmarshalJSON(File f, Class<T> type) throws IOException, GeneralSecurityException {
    final byte[] data = toBytes(f);
    final ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readValue(data, type);
  }

  public Map unmarshalYAML(File f) throws IOException, GeneralSecurityException {
    return unmarshalYAML(f, Map.class);
  }

  public <T> T unmarshalYAML(File f, Class<T> type) throws IOException, GeneralSecurityException {
    final byte[] data = toBytes(f);
    final YAMLFactory yamlFactory = new YAMLFactory();
    final ObjectMapper objectMapper = new ObjectMapper(yamlFactory);
    return objectMapper.readValue(data, type);
  }
}
