package com.github.ngyewch.gradle.ansible.vault;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AnsibleVaultDecryptorTests {
  @Test
  public void test1() throws Exception {
    final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    final AnsibleVault ansibleVault;
    try (final InputStream inputStream = classLoader.getResourceAsStream("test.yml")) {
      ansibleVault = AnsibleVault.readFrom(inputStream);
    }

    final String password =
        StringUtils.trimToNull(
            IOUtils.resourceToString(
                "ansible-vault-password", StandardCharsets.UTF_8, classLoader));

    final byte[] decrypted = AnsibleVaultDecryptor.decrypt(ansibleVault, password);
    final byte[] expectedDecrypted = IOUtils.resourceToByteArray("test-original.yml", classLoader);
    System.out.println(new String(decrypted, StandardCharsets.UTF_8));
    Assertions.assertArrayEquals(expectedDecrypted, decrypted);
  }
}
