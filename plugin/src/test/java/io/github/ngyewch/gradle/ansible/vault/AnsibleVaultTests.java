package io.github.ngyewch.gradle.ansible.vault;

import java.io.InputStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AnsibleVaultTests {
  @Test
  public void test1() throws Exception {
    final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    final AnsibleVault ansibleVault;
    try (final InputStream inputStream = classLoader.getResourceAsStream("test.yml")) {
      ansibleVault = AnsibleVault.readFrom(inputStream);
    }

    Assertions.assertNotNull(ansibleVault.getHeader());
    Assertions.assertEquals("1.1", ansibleVault.getHeader().getVersion());
    Assertions.assertEquals("AES256", ansibleVault.getHeader().getCipher());
    Assertions.assertNull(ansibleVault.getHeader().getVaultIdLabel());
  }
}
