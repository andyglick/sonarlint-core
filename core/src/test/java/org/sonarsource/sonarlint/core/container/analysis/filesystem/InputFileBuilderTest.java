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
package org.sonarsource.sonarlint.core.container.analysis.filesystem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.sonar.api.batch.fs.InputFile;
import org.sonarsource.sonarlint.core.TestClientInputFile;
import org.sonarsource.sonarlint.core.client.api.common.analysis.ClientInputFile;

public class InputFileBuilderTest {
  @Rule
  public TemporaryFolder temp = new TemporaryFolder();

  private LanguageDetection langDetection = mock(LanguageDetection.class);
  private FileMetadata metadata = new FileMetadata();

  @Test
  public void test() throws IOException {
    when(langDetection.language(any(InputFile.class))).thenReturn("java");

    Path path = temp.getRoot().toPath().resolve("file");
    Files.write(path, "test".getBytes(StandardCharsets.ISO_8859_1));
    ClientInputFile file = new TestClientInputFile(path, true, StandardCharsets.ISO_8859_1);

    InputFileBuilder builder = new InputFileBuilder(langDetection, metadata);
    SonarLintInputFile inputFile = builder.create(file);

    assertThat(inputFile.type()).isEqualTo(InputFile.Type.TEST);
    assertThat(inputFile.file()).isEqualTo(path.toFile());
    assertThat(inputFile.absolutePath()).isEqualTo(path.toAbsolutePath().toString());
    assertThat(inputFile.language()).isEqualTo("java");
    assertThat(inputFile.key()).isEqualTo(path.toAbsolutePath().toString());
    assertThat(inputFile.lines()).isEqualTo(1);
  }
}