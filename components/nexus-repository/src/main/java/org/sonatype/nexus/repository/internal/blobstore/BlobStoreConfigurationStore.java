/*
 * Sonatype Nexus (TM) Open Source Version
 * Copyright (c) 2008-present Sonatype, Inc.
 * All rights reserved. Includes the third-party code listed at http://links.sonatype.com/products/nexus/oss/attributions.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License Version 1.0,
 * which accompanies this distribution and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Sonatype Nexus (TM) Professional Version is available from Sonatype, Inc. "Sonatype" and "Sonatype Nexus" are trademarks
 * of Sonatype, Inc. Apache Maven is a trademark of the Apache Software Foundation. M2eclipse is a trademark of the
 * Eclipse Foundation. All other trademarks are the property of their respective owners.
 */
package org.sonatype.nexus.repository.internal.blobstore;

import java.util.List;

import org.sonatype.goodies.lifecycle.Lifecycle;
import org.sonatype.nexus.blobstore.api.BlobStoreConfiguration;

/**
 * {@link BlobStoreConfiguration} store.
 *
 * since 3.0
 */
public interface BlobStoreConfigurationStore
    extends Lifecycle
{
  /**
   * @return all BlobStoreConfigurations
   */
  List<BlobStoreConfiguration> list();

  /**
   * Persist a new BlobStoreConfiguration.
   */
  void create(BlobStoreConfiguration configuration);

  /**
   * Update an existing BlobStoreConfiguration.
   *
   * @since 3.next
   */
  void update(BlobStoreConfiguration configuration);

  /**
   * Delete an existing BlobStoreConfiguration.
   */
  void delete(BlobStoreConfiguration configuration);
}
