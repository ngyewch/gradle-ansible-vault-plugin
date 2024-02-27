package io.github.ngyewch.gradle.ansible.vault;

import org.apache.commons.lang3.StringUtils;

public class AnsibleVaultHeader {
  private String version;
  private String cipher;
  private String vaultIdLabel;

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getCipher() {
    return cipher;
  }

  public void setCipher(String cipher) {
    this.cipher = cipher;
  }

  public String getVaultIdLabel() {
    return vaultIdLabel;
  }

  public void setVaultIdLabel(String vaultIdLabel) {
    this.vaultIdLabel = vaultIdLabel;
  }

  public static AnsibleVaultHeader parse(String headerLine) {
    final String[] headerParts = StringUtils.split(headerLine, ";");
    if ((headerParts == null)
        || (headerParts.length < 3)
        || !StringUtils.equals(headerParts[0], "$ANSIBLE_VAULT")) {
      throw new IllegalArgumentException("invalid header");
    }
    final AnsibleVaultHeader ansibleVaultHeader = new AnsibleVaultHeader();
    ansibleVaultHeader.setVersion(headerParts[1]);
    ansibleVaultHeader.setCipher(headerParts[2]);
    if (headerParts.length > 3) {
      ansibleVaultHeader.setVaultIdLabel(headerParts[3]);
    }
    return ansibleVaultHeader;
  }
}
