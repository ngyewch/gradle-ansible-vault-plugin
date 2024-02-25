package com.github.ngyewch.gradle.ansible.vault;

import java.security.GeneralSecurityException;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AnsibleVaultDecryptor {
  public static final int CIPHER_KEY_LENGTH = 32;
  public static final int HMAC_KEY_LENGTH = 32;
  public static final int CIPHER_IV_LENGTH = 16;
  public static final int ITERATIONS = 10000;
  public static final String KEY_GENERATION_ALGORITHM = "HmacSHA256";
  public static final String CYPHER_KEY_ALGO = "AES";
  public static final String CYPHER_ALGO = "AES/CTR/NoPadding";

  public static byte[] decrypt(AnsibleVault ansibleVault, String password)
      throws GeneralSecurityException {
    final EncryptionKeyChain encryptionKeyChain =
        EncryptionKeyChain.create(
            ansibleVault.getSalt(),
            password,
            CIPHER_KEY_LENGTH,
            HMAC_KEY_LENGTH,
            CIPHER_IV_LENGTH,
            ITERATIONS,
            KEY_GENERATION_ALGORITHM);

    final SecretKeySpec hmacKey =
        new SecretKeySpec(encryptionKeyChain.getHmacKey(), KEY_GENERATION_ALGORITHM);
    final Mac mac = Mac.getInstance(KEY_GENERATION_ALGORITHM);
    mac.init(hmacKey);
    final byte[] computedMac = mac.doFinal(ansibleVault.getData());

    if (!Arrays.equals(ansibleVault.getHmac(), computedMac)) {
      throw new IllegalArgumentException("HMAC mismatch");
    }

    final SecretKeySpec keySpec =
        new SecretKeySpec(encryptionKeyChain.getCipherKey(), CYPHER_KEY_ALGO);
    final IvParameterSpec ivSpec = new IvParameterSpec(encryptionKeyChain.getCipherIv());
    final Cipher cipher = Cipher.getInstance(CYPHER_ALGO);
    cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
    final byte[] paddedDecrypted = cipher.doFinal(ansibleVault.getData());

    final int paddingLength =
        (paddedDecrypted.length > 0) ? paddedDecrypted[paddedDecrypted.length - 1] : 0;
    return Arrays.copyOfRange(paddedDecrypted, 0, paddedDecrypted.length - paddingLength);
  }
}
