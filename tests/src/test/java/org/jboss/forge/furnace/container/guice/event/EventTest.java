/**
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.furnace.container.guice.event;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;

import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.forge.arquillian.AddonDependencies;
import org.jboss.forge.arquillian.archive.AddonArchive;
import org.jboss.forge.furnace.container.guice.Service;
import org.jboss.forge.furnace.event.EventManager;
import org.jboss.forge.furnace.event.PostStartup;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Module;

/**
 * 
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
@RunWith(Arquillian.class)
public class EventTest
{
   @Deployment
   @AddonDependencies
   public static AddonArchive getDeployment()
   {
      return ShrinkWrap.create(AddonArchive.class)
               .addClasses(EventModule.class, TestEventListener.class)
               .addAsServiceProvider(Module.class, EventModule.class)
               .addAsServiceProvider(Service.class, EventTest.class);
   }

   @Inject
   EventManager eventManager;

   @Test
   public void testEventManager()
   {
      String originalEvent = "This is an event";
      eventManager.fireEvent(originalEvent);
      List<Object> events = TestEventListener.INSTANCE.getEvents();
      Assert.assertEquals(2, events.size());
      Assert.assertThat(events.get(0), instanceOf(PostStartup.class));
      Assert.assertThat(events.get(1), instanceOf(String.class));
      Assert.assertThat((String) events.get(1), equalTo(originalEvent));
   }

}
