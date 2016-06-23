/**
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.furnace.container.guice.modules;

import java.lang.reflect.Field;

import org.jboss.forge.furnace.addons.AddonRegistry;
import org.jboss.forge.furnace.services.Imported;

import com.google.inject.Inject;
import com.google.inject.MembersInjector;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

/**
 * Furnace service {@link TypeListener}
 *
 * @author <a href="mailto:bsideup@gmail.com">Sergei Egorov</a>
 */
public class ServiceTypeListener implements TypeListener
{
   private final AddonRegistry addonRegistry;

   public ServiceTypeListener(AddonRegistry addonRegistry)
   {
      this.addonRegistry = addonRegistry;
   }

   @Override
   public <I> void hear(TypeLiteral<I> typeLiteral, TypeEncounter<I> typeEncounter)
   {
      Class<?> clazz = typeLiteral.getRawType();
      while (clazz != null)
      {
         for (Field field : clazz.getDeclaredFields())
         {
            boolean hasInjectAnnotation = field.isAnnotationPresent(Inject.class) ||
                     field.isAnnotationPresent(javax.inject.Inject.class);

            if (!hasInjectAnnotation) {
               continue;
            }

            Class<?> fieldType = field.getType();

            Imported services = addonRegistry.getServices(fieldType);
            if (services.isAmbiguous() || services.isUnsatisfied())
            {
               continue;
            }

            try
            {
               Object serviceInstance = services.get();
               typeEncounter.register(new MembersInjector<I>()
               {
                  @Override
                  public void injectMembers(I i)
                  {
                     try
                     {
                        field.setAccessible(true);
                        field.set(i, serviceInstance);
                     }
                     catch (IllegalAccessException e)
                     {
                        throw new RuntimeException(e);
                     }
                  }
               });
            }
            catch (Exception e)
            {
               // Well... we tried.
            }
         }
         clazz = clazz.getSuperclass();
      }
   }
}