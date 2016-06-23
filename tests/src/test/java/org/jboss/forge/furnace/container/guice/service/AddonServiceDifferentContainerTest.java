/**
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.furnace.container.guice.service;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.forge.arquillian.AddonDeployment;
import org.jboss.forge.arquillian.AddonDeployments;
import org.jboss.forge.arquillian.archive.AddonArchive;
import org.jboss.forge.furnace.container.guice.Service;
import org.jboss.forge.furnace.container.guice.mock.services.AbstractBaseConsumer;
import org.jboss.forge.furnace.container.guice.mock.services.ConcreteConsumer;
import org.jboss.forge.furnace.container.guice.mock.services.ConsumerModule;
import org.jboss.forge.furnace.container.guice.mock.services.ExportedService;
import org.jboss.forge.furnace.repositories.AddonDependencyEntry;
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
public class AddonServiceDifferentContainerTest
{
   @Deployment(order = 0)
   @AddonDeployments({
            @AddonDeployment(name = "org.jboss.forge.furnace.container:guice")
   })
   public static AddonArchive getDeployment()
   {
      AddonArchive archive = ShrinkWrap.create(AddonArchive.class)
               .addClass(ConcreteConsumer.class)
               .addClass(ConsumerModule.class)
               .addAsServiceProvider(Module.class, ConsumerModule.class)
               .addAsServiceProvider(Service.class, AddonServiceDifferentContainerTest.class)
               .addAsAddonDependencies(
                        AddonDependencyEntry.create("org.jboss.forge.furnace.container:guice"),
                        AddonDependencyEntry.create("dependency"));

      return archive;
   }

   @Deployment(name = "dependency,2", testable = false, order = 1)
   public static AddonArchive getDependencyDeployment()
   {
      AddonArchive archive = ShrinkWrap.create(AddonArchive.class, "dependency.jar")
               .addClasses(AbstractBaseConsumer.class, ExportedService.class, Inject.class)
               .addAsLocalServices(ExportedService.class);

      return archive;
   }

   @Inject
   private ConcreteConsumer consumer;

   @Test
   public void testInheritedServiceInjectionsResolveSuccessfully() throws Exception
   {
      Assert.assertNotNull(consumer);
      Assert.assertNotNull(consumer.getService());
   }

}
