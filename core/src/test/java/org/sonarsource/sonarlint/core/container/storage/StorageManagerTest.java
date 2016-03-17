/*
 * SonarLint Core - Implementation
 * Copyright (C) 2009-2016 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonarsource.sonarlint.core.container.storage;

import java.nio.file.Path;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.sonarsource.sonarlint.core.client.api.GlobalConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

public class StorageManagerTest {

  @Rule
  public TemporaryFolder temp = new TemporaryFolder();

  @Test
  public void encodeModuleKeyForFs() throws Exception {

    Path sonarUserHome = temp.newFolder().toPath();
    StorageManager manager = new StorageManager(GlobalConfiguration.builder()
      .setSonarLintUserHome(sonarUserHome)
      .setServerId("server_id")
      .build());

    Path moduleStorageRoot = manager.getModuleStorageRoot(".module.:key/with_branché%\n");
    assertThat(moduleStorageRoot).isEqualTo(sonarUserHome.resolve("storage").resolve("server_id").resolve("modules").resolve("%2emodule.:key%2fwith_branch%e9%25%0a"));
  }

}