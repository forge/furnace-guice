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
import org.jboss.forge.furnace.event.EventManager;

import com.google.inject.Binder;
import com.google.inject.Module;

/**
 * Default {@link Module} implementation
 * 
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class GuiceContainerModule implements Module
{
   private final Furnace furnace;
   private final AddonRegistry addonRegistry;
   private final Addon addon;

   public GuiceContainerModule(Furnace furnace, AddonRegistry addonRegistry, Addon addon)
   {
      this.furnace = furnace;
      this.addonRegistry = addonRegistry;
      this.addon = addon;
   }

   @SuppressWarnings("unchecked")
   @Override
   public void configure(Binder binder)
   {
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
