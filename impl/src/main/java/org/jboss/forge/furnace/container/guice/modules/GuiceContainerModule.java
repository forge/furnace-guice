/**
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.furnace.container.guice.modules;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Scanner;

import org.jboss.forge.furnace.Furnace;
import org.jboss.forge.furnace.addons.Addon;
import org.jboss.forge.furnace.addons.AddonRegistry;
import org.jboss.forge.furnace.container.guice.Service;
import org.jboss.forge.furnace.container.guice.events.GuiceEventManager;
import org.jboss.forge.furnace.event.EventManager;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

/**
 * Default {@link Module} implementation
 * 
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class GuiceContainerModule implements Module
{
   private final Furnace furnace;
   private final AddonRegistry addonRegistry;

   private Addon addon;
   private GuiceEventManager eventManager;

   public GuiceContainerModule(Furnace furnace, AddonRegistry addonRegistry)
   {
      this.furnace = furnace;
      this.addonRegistry = addonRegistry;
   }

   public void setCurrentAddon(Addon addon)
   {
      this.addon = addon;
   }

   public void setEventManager(GuiceEventManager eventManager)
   {
      this.eventManager = eventManager;
   }

   @SuppressWarnings("unchecked")
   @Override
   public void configure(Binder binder)
   {
      // Registering EventBus using AOP.
      // http://www.lexicalscope.com/blog/2012/02/13/guava-eventbus-experiences/
      binder.bindListener(Matchers.any(), new TypeListener()
      {
         @Override
         public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter)
         {
            encounter.register(new InjectionListener()
            {
               @Override
               public void afterInjection(Object injectee)
               {
                  eventManager.register(injectee);
               }
            });
         }
      });
      binder.bind(Furnace.class).toInstance(furnace);
      binder.bind(AddonRegistry.class).toInstance(addonRegistry);
      binder.bind(EventManager.class).toInstance(addonRegistry.getEventManager());
      binder.bind(Addon.class).toInstance(addon);
      // Register each service
      ClassLoader classLoader = addon.getClassLoader();
      Enumeration<URL> resources;
      try
      {
         resources = classLoader.getResources("/META-INF/services/" + Service.class.getName());
      }
      catch (IOException e1)
      {
         return;
      }
      while (resources.hasMoreElements())
      {
         URL resource = resources.nextElement();
         if (resource != null)
         {
            try (InputStream is = resource.openStream(); Scanner sc = new Scanner(is))
            {
               while (sc.hasNextLine())
               {
                  String line = sc.nextLine();
                  Class serviceType = classLoader.loadClass(line);
                  binder.bind(serviceType).toConstructor(serviceType.getConstructor());
               }
            }
            catch (Exception e)
            {
               e.printStackTrace();
            }
         }
      }

   }
}
