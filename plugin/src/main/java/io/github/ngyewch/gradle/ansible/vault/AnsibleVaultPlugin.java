package io.github.ngyewch.gradle.ansible.vault;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class AnsibleVaultPlugin implements Plugin<Project> {

  @Override
  public void apply(Project project) {
    project.getExtensions().create("ansibleVault", AnsibleVaultExtension.class);
  }
}
