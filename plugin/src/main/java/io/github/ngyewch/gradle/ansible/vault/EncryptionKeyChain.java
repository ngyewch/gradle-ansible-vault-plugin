package io.github.ngyewch.gradle.ansible.vault;

import de.rtner.security.auth.spi.PBKDF2Engine;
import de.rtner.security.auth.spi.PBKDF2Parameters;
import java.util.Arrays;

public class EncryptionKeyChain {
  private static final String DEFAULT_HASH_CHARSET = "UTF-8";

  private final byte[] cipherKey;
  private final byte[] hmacKey;
  private final byte[] cipherIv;

  private EncryptionKeyChain(byte[] encryptionKey, byte[] hmacKey, byte[] cipherIv) {
    super();

    this.cipherKey = encryptionKey;
    this.hmacKey = hmacKey;
    this.cipherIv = cipherIv;
  }

  public static EncryptionKeyChain create(
      byte[] salt,
      String password,
      int cipherKeyLength,
      int hmacKeyLength,
      int cipherIvLength,
      int iterations,
      String hashAlgorithm) {
    final PBKDF2Parameters params =
        new PBKDF2Parameters(hashAlgorithm, DEFAULT_HASH_CHARSET, salt, iterations);
    final int derivedKeyLength = cipherKeyLength + hmacKeyLength + cipherIvLength;
    final PBKDF2Engine pbkdf2Engine = new PBKDF2Engine(params);
    final byte[] derivedKey = pbkdf2Engine.deriveKey(password, derivedKeyLength);
    return new EncryptionKeyChain(
        Arrays.copyOfRange(derivedKey, 0, cipherKeyLength),
        Arrays.copyOfRange(derivedKey, cipherKeyLength, cipherKeyLength + hmacKeyLength),
        Arrays.copyOfRange(
            derivedKey,
            cipherKeyLength + hmacKeyLength,
            cipherKeyLength + hmacKeyLength + cipherIvLength));
  }

  public byte[] getCipherKey() {
    return cipherKey;
  }

  public byte[] getHmacKey() {
    return hmacKey;
  }

  public byte[] getCipherIv() {
    return cipherIv;
  }
}
