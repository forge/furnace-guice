/**
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.furnace.container.guice.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.forge.arquillian.AddonDependencies;
import org.jboss.forge.arquillian.archive.AddonArchive;
import org.jboss.forge.furnace.addons.AddonRegistry;
import org.jboss.forge.furnace.container.guice.Service;
import org.jboss.forge.furnace.container.guice.mock.MockInterface;
import org.jboss.forge.furnace.container.guice.mock.MockModule;
import org.jboss.forge.furnace.container.guice.mock.MockService;
import org.jboss.forge.furnace.services.Imported;
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
public class AddonRegistryTest
{
   @Inject
   private AddonRegistry addonRegistry;

   @Deployment
   @AddonDependencies
   public static AddonArchive getDeployment()
   {
      return ShrinkWrap.create(AddonArchive.class)
               .addClasses(MockInterface.class, MockService.class, MockModule.class)
               .addAsServiceProvider(Module.class, MockModule.class)
               .addAsServiceProvider(Service.class, AddonRegistryTest.class);
   }

   @Test
   public void testServiceInjection() throws Exception
   {
      Imported<MockInterface> service = addonRegistry.getServices(MockInterface.class);
      Assert.assertThat(service.isAmbiguous(), is(false));
      Assert.assertThat(service.isUnsatisfied(), is(false));
      Assert.assertThat(service.get(), notNullValue());
   }
}
