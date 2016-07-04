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
package org.sonatype.nexus.audit.internal;

import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.audit.AuditData;
import org.sonatype.nexus.audit.AuditDataRecordedEvent;
import org.sonatype.nexus.audit.AuditRecorder;
import org.sonatype.nexus.audit.AuditStore;
import org.sonatype.nexus.audit.InitiatorProvider;
import org.sonatype.nexus.common.event.EventBus;
import org.sonatype.nexus.common.node.NodeAccess;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Default {@link AuditRecorder} implementation.
 *
 * @since 3.1
 */
@Named
@Singleton
public class AuditRecorderImpl
    extends ComponentSupport
    implements AuditRecorder
{
  private final EventBus eventBus;

  private final NodeAccess nodeAccess;

  private final AuditStore auditStore;

  private final InitiatorProvider initiatorProvider;

  private volatile boolean enabled = false;

  @Inject
  public AuditRecorderImpl(final EventBus eventBus,
                           final NodeAccess nodeAccess,
                           final AuditStore auditStore,
                           final InitiatorProvider initiatorProvider)
  {
    this.eventBus = eventBus;
    this.nodeAccess = nodeAccess;
    this.auditStore = auditStore;
    this.initiatorProvider = initiatorProvider;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(final boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public void record(final AuditData data) {
    checkNotNull(data);

    if (enabled) {
      // fill in timestamp, node-id and initiator if missing
      if (data.getTimestamp() == null) {
        data.setTimestamp(new Date());
      }
      if (data.getNodeId() == null) {
        data.setNodeId(nodeAccess.getId());
      }
      if (data.getInitiator() == null) {
        data.setInitiator(initiatorProvider.get());
      }

      log.debug("Record: {}", data);
      try {
        auditStore.add(data);
        eventBus.post(new AuditDataRecordedEvent(data.copy()));
      }
      catch (Exception e) {
        log.warn("Failed to record audit data", e);
      }
    }
  }
}
