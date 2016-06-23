/**
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.furnace.container.guice.lifecycle;

import java.util.LinkedHashSet;
import java.util.ServiceLoader;
import java.util.Set;

import org.jboss.forge.furnace.Furnace;
import org.jboss.forge.furnace.addons.Addon;
import org.jboss.forge.furnace.addons.AddonRegistry;
import org.jboss.forge.furnace.container.guice.events.GuiceEventManager;
import org.jboss.forge.furnace.container.guice.impl.GuiceServiceRegistry;
import org.jboss.forge.furnace.container.guice.modules.GuiceContainerModule;
import org.jboss.forge.furnace.container.guice.modules.ServiceTypeListener;
import org.jboss.forge.furnace.event.EventManager;
import org.jboss.forge.furnace.event.PostStartup;
import org.jboss.forge.furnace.event.PreShutdown;
import org.jboss.forge.furnace.lifecycle.AddonLifecycleProvider;
import org.jboss.forge.furnace.lifecycle.ControlType;
import org.jboss.forge.furnace.spi.ServiceRegistry;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.matcher.Matchers;
import com.google.inject.Module;
import com.google.inject.Stage;

/**
 * {@link AddonLifecycleProvider} instance for Guice
 * 
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class GuiceAddonLifecycleProvider implements AddonLifecycleProvider
{
   private Furnace furnace;
   private AddonRegistry addonRegistry;

   private GuiceEventManager eventManager;
   private GuiceServiceRegistry serviceRegistry;
   private Injector injector;

   @Override
   public void initialize(Furnace furnace, AddonRegistry registry, Addon container) throws Exception
   {
      this.furnace = furnace;
      this.addonRegistry = registry;
   }

   @Override
   public void start(Addon addon) throws Exception
   {
      Set<Module> modules = new LinkedHashSet<>();
      modules.add(new GuiceContainerModule(furnace, addonRegistry, addon));
      modules.add(binder -> binder.bindListener(Matchers.any(), new ServiceTypeListener(addonRegistry)));

      for (Module addonModule : ServiceLoader.load(Module.class, addon.getClassLoader()))
      {
         modules.add(addonModule);
      }

      this.injector = Guice.createInjector(Stage.PRODUCTION, modules);
      this.serviceRegistry = new GuiceServiceRegistry(addon, injector);
      this.eventManager = new GuiceEventManager(serviceRegistry);
   }

   @Override
   public void stop(Addon addon) throws Exception
   {
   }

   @Override
   public ServiceRegistry getServiceRegistry(Addon addon) throws Exception
   {
      return serviceRegistry;
   }

   @Override
   public EventManager getEventManager(Addon addon) throws Exception
   {
      return eventManager;
   }

   @Override
   public void postStartup(Addon addon) throws Exception
   {
      eventManager.fireEvent(new PostStartup(addon));
   }

   @Override
   public void preShutdown(Addon addon) throws Exception
   {
      eventManager.fireEvent(new PreShutdown(addon));
   }

   @Override
   public ControlType getControlType()
   {
      return ControlType.DEPENDENTS;
   }

}
