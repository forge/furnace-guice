/**
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.furnace.container.guice.events;

import java.lang.annotation.Annotation;

import org.jboss.forge.furnace.container.guice.EventListener;
import org.jboss.forge.furnace.event.EventException;
import org.jboss.forge.furnace.event.EventManager;
import org.jboss.forge.furnace.spi.ExportedInstance;
import org.jboss.forge.furnace.spi.ServiceRegistry;

/**
 * {@link EventManager} implementation for Guice
 * 
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class GuiceEventManager implements EventManager
{
   private final ServiceRegistry serviceRegistry;

   public GuiceEventManager(ServiceRegistry serviceRegistry)
   {
      this.serviceRegistry = serviceRegistry;
   }

   @Override
   public void fireEvent(final Object event, Annotation... qualifiers) throws EventException
   {
      for (ExportedInstance<EventListener> instance : serviceRegistry.getExportedInstances(EventListener.class))
      {
         EventListener eventListener = instance.get();
         eventListener.handleEvent(event, qualifiers);
      }
   }
}