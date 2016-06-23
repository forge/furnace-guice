/**
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.furnace.container.guice.service;

import static org.hamcrest.CoreMatchers.notNullValue;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.forge.arquillian.AddonDependencies;
import org.jboss.forge.arquillian.archive.AddonArchive;
import org.jboss.forge.furnace.container.guice.Service;
import org.jboss.forge.furnace.container.guice.mock.MockInterface;
import org.jboss.forge.furnace.container.guice.mock.MockModule;
import org.jboss.forge.furnace.container.guice.mock.MockService;
import org.jboss.forge.furnace.container.guice.mock.services.ExplicitBindingsModule;
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
public class ServiceInjectionTest
{
   @Inject
   private MockInterface service;

   @Deployment
   @AddonDependencies
   public static AddonArchive getDeployment()
   {
      return ShrinkWrap.create(AddonArchive.class)
               .addClasses(MockInterface.class, MockService.class)
               .addAsServiceProviderAndClasses(Module.class, MockModule.class, ExplicitBindingsModule.class)
               .addAsServiceProvider(Service.class, ServiceInjectionTest.class);
   }

   @Test
   public void testServiceInjection() throws Exception
   {
      Assert.assertThat(service, notNullValue());
   }
}
