/**
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.furnace.container.guice.event;

import javax.inject.Inject;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.forge.furnace.event.EventManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.eventbus.Subscribe;

/**
 * 
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
@RunWith(Arquillian.class)
public class EventTest
{
   @Inject
   EventManager eventManager;

   private String event;
   private int count;

   @Before
   public void setUp()
   {
      this.event = null;
   }

   @Test
   public void testEventManager()
   {
      String originalEvent = "This is an event";
      Assert.assertNull(event);
      eventManager.fireEvent(originalEvent);
      Assert.assertEquals(originalEvent, event);
      Assert.assertEquals(1, count);
   }

   @Subscribe
   public void observe(String event)
   {
      this.event = event;
      count++;
   }

}
