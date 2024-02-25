package com.github.ngyewch.gradle.ansible.vault;

import java.io.*;
import java.nio.charset.StandardCharsets;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class AnsibleVault {
  private final AnsibleVaultHeader header;
  private final byte[] salt;
  private final byte[] hmac;
  private final byte[] data;

  private AnsibleVault(InputStream inputStream) throws IOException {
    this(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
  }

  private AnsibleVault(Reader reader) throws IOException {
    super();

    final BufferedReader bufferedReader = new BufferedReader(reader);

    final String headerLine = bufferedReader.readLine();
    header = AnsibleVaultHeader.parse(headerLine);

    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    while (true) {
      final String line = bufferedReader.readLine();
      if (line == null) {
        break;
      }
      try {
        baos.writeBytes(Hex.decodeHex(line));
      } catch (DecoderException e) {
        throw new IOException("invalid armored payload", e);
      }
    }

    final BufferedReader bufferedReader1 =
        new BufferedReader(new InputStreamReader(new ByteArrayInputStream(baos.toByteArray())));
    try {
      salt = Hex.decodeHex(bufferedReader1.readLine());
    } catch (DecoderException e) {
      throw new IOException("invalid salt");
    }
    try {
      hmac = Hex.decodeHex(bufferedReader1.readLine());
    } catch (DecoderException e) {
      throw new IOException("invalid hmac");
    }
    try {
      data = Hex.decodeHex(bufferedReader1.readLine());
    } catch (DecoderException e) {
      throw new IOException("invalid data");
    }
  }

  public static AnsibleVault readFrom(InputStream inputStream) throws IOException {
    return new AnsibleVault(inputStream);
  }

  public static AnsibleVault readFrom(Reader reader) throws IOException {
    return new AnsibleVault(reader);
  }

  public AnsibleVaultHeader getHeader() {
    return header;
  }

  public byte[] getSalt() {
    return salt;
  }

  public byte[] getHmac() {
    return hmac;
  }

  public byte[] getData() {
    return data;
  }
}
