/**
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.furnace.container.guice.event;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.jboss.forge.furnace.container.guice.EventListener;

/**
 * 
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public enum TestEventListener implements EventListener
{
   INSTANCE;
   private List<Object> events = new ArrayList<>();

   @Override
   public void handleEvent(Object event, Annotation... qualifiers)
   {
      events.add(event);
   }

   /**
    * @return the events
    */
   public List<Object> getEvents()
   {
      return events;
   }

}
