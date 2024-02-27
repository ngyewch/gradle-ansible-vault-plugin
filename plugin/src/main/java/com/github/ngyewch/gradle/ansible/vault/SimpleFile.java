package com.github.ngyewch.gradle.ansible.vault;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.toml.TomlFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class SimpleFile {
  private final InputSource inputSource;

  private final JSON json = new JSON();
  private final YAML yaml = new YAML();
  private final TOML toml = new TOML();

  public SimpleFile(InputSource inputSource) {
    super();

    this.inputSource = inputSource;
  }

  public byte[] asByteArray() throws IOException {
    return inputSource.getByteArray();
  }

  public String asString() throws IOException {
    return asString("UTF-8");
  }

  public String asString(String charsetName) throws IOException {
    return new String(inputSource.getByteArray(), charsetName);
  }

  public Properties asProperties() throws IOException {
    try (final InputStream inputStream = inputSource.getInputStream()) {
      final Properties properties = new Properties();
      properties.load(inputStream);
      return properties;
    }
  }

  public JSON json() {
    return json;
  }

  public YAML yaml() {
    return yaml;
  }

  public TOML toml() {
    return toml;
  }

  public class JSON {
    public Map asMap() throws IOException {
      return as(Map.class);
    }

    public <T> T as(Class<T> type) throws IOException {
      final ObjectMapper objectMapper = new ObjectMapper();
      try (final InputStream inputStream = inputSource.getInputStream()) {
        return objectMapper.readValue(inputStream, type);
      }
    }
  }

  public class YAML {
    public Map asMap() throws IOException {
      return as(Map.class);
    }

    public <T> T as(Class<T> type) throws IOException {
      final YAMLFactory yamlFactory = new YAMLFactory();
      final ObjectMapper objectMapper = new ObjectMapper(yamlFactory);
      try (final InputStream inputStream = inputSource.getInputStream()) {
        return objectMapper.readValue(inputStream, type);
      }
    }
  }

  public class TOML {
    public Map asMap() throws IOException {
      return as(Map.class);
    }

    public <T> T as(Class<T> type) throws IOException {
      final TomlFactory tomlFactory = new TomlFactory();
      final ObjectMapper objectMapper = new ObjectMapper(tomlFactory);
      try (final InputStream inputStream = inputSource.getInputStream()) {
        return objectMapper.readValue(inputStream, type);
      }
    }
  }
}
