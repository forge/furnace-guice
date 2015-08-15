/**
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.furnace.container.guice.events;

import java.lang.annotation.Annotation;
import java.util.concurrent.Callable;

import org.jboss.forge.furnace.addons.Addon;
import org.jboss.forge.furnace.event.EventException;
import org.jboss.forge.furnace.event.EventManager;
import org.jboss.forge.furnace.util.ClassLoaders;

import com.google.common.eventbus.EventBus;

/**
 * {@link EventManager} implementation for Guice
 * 
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class GuiceEventManager implements EventManager
{
   private final EventBus eventBus;
   private final Addon addon;

   public GuiceEventManager(Addon addon)
   {
      this.addon = addon;
      this.eventBus = new EventBus(addon.getId().getName());
   }

   @Override
   public void fireEvent(final Object event, Annotation... qualifiers) throws EventException
   {
      try
      {
         ClassLoaders.executeIn(addon.getClassLoader(), new Callable<Void>()
         {
            @Override
            public Void call() throws Exception
            {
               eventBus.post(event);
               return null;
            }
         });
      }
      catch (Exception e)
      {
         throw new EventException("Could not propagate event to addon [" + addon + "]", e);
      }
   }

   public void register(Object listener)
   {
      eventBus.register(listener);
   }

   public void unregister(Object listener)
   {
      eventBus.unregister(listener);
   }
}