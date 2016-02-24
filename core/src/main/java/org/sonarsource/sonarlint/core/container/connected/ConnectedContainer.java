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
package org.sonarsource.sonarlint.core.container.connected;

import org.sonarsource.sonarlint.core.client.api.GlobalConfiguration;
import org.sonarsource.sonarlint.core.client.api.connected.ServerConfiguration;
import org.sonarsource.sonarlint.core.client.api.connected.ValidationResult;
import org.sonarsource.sonarlint.core.container.ComponentContainer;
import org.sonarsource.sonarlint.core.container.storage.StorageManager;
import org.sonarsource.sonarlint.core.plugin.cache.PluginCacheProvider;
import org.sonarsource.sonarlint.core.plugin.cache.PluginHashes;

public class ConnectedContainer extends ComponentContainer {

  private final ServerConfiguration serverConfiguration;
  private final GlobalConfiguration globalConfig;

  public ConnectedContainer(GlobalConfiguration globalConfig, ServerConfiguration serverConfiguration) {
    this.globalConfig = globalConfig;
    this.serverConfiguration = serverConfiguration;
  }

  @Override
  protected void doBeforeStart() {
    add(
      globalConfig,
      serverConfiguration,
      AuthenticationChecker.class,
      SonarLintWsClient.class,
      GlobalSync.class,
      new PluginCacheProvider(),
      PluginHashes.class,
      StorageManager.class);
  }

  public ValidationResult validateCredentials() {
    return getComponentByType(AuthenticationChecker.class).validateCredentials();
  }

  public void sync() {
    getComponentByType(GlobalSync.class).sync();
  }

}
