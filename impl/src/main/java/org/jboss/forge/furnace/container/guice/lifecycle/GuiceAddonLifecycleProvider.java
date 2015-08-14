/**
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.furnace.container.guice.lifecycle;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import org.jboss.forge.furnace.Furnace;
import org.jboss.forge.furnace.addons.Addon;
import org.jboss.forge.furnace.addons.AddonRegistry;
import org.jboss.forge.furnace.container.guice.events.GuiceEventManager;
import org.jboss.forge.furnace.container.guice.impl.GuiceServiceRegistry;
import org.jboss.forge.furnace.container.guice.modules.GuiceContainerModule;
import org.jboss.forge.furnace.event.EventManager;
import org.jboss.forge.furnace.lifecycle.AddonLifecycleProvider;
import org.jboss.forge.furnace.lifecycle.ControlType;
import org.jboss.forge.furnace.spi.ServiceRegistry;
import org.jboss.forge.furnace.util.Lists;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * {@link AddonLifecycleProvider} instance for Guice
 * 
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class GuiceAddonLifecycleProvider implements AddonLifecycleProvider
{
   private GuiceContainerModule module;
   private GuiceEventManager eventManager;
   private GuiceServiceRegistry serviceRegistry;
   private Injector injector;

   @Override
   public void initialize(Furnace furnace, AddonRegistry registry, Addon container) throws Exception
   {
      this.module = new GuiceContainerModule(furnace, registry);
   }

   @Override
   public void start(Addon addon) throws Exception
   {
      module.setCurrentAddon(addon);
      this.eventManager = new GuiceEventManager(addon.getId().getName());
      module.setEventManager(eventManager);

      List<Module> modules = new ArrayList<>();
      modules.add(module);
      modules.addAll(Lists.toList(ServiceLoader.load(Module.class, addon.getClassLoader())));
      this.injector = Guice.createInjector(modules);
      this.serviceRegistry = new GuiceServiceRegistry(addon, injector);
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
   }

   @Override
   public void preShutdown(Addon addon) throws Exception
   {
   }

   @Override
   public ControlType getControlType()
   {
      return ControlType.DEPENDENTS;
   }

}
