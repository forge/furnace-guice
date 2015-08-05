/**
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.furnace.container.guice.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.forge.furnace.addons.Addon;
import org.jboss.forge.furnace.spi.ExportedInstance;
import org.jboss.forge.furnace.spi.ServiceRegistry;

import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;

/**
 * 
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class GuiceServiceRegistry implements ServiceRegistry
{
   private final Addon sourceAddon;
   private final Injector injector;

   public GuiceServiceRegistry(Addon sourceAddon, Injector injector)
   {
      super();
      this.sourceAddon = sourceAddon;
      this.injector = injector;
   }

   @Override
   public <T> Set<ExportedInstance<T>> getExportedInstances(Class<T> clazz)
   {
      TypeLiteral<T> typeLiteral = Key.get(clazz).getTypeLiteral();
      List<Binding<T>> bindings = injector.findBindingsByType(typeLiteral);
      Set<ExportedInstance<T>> instances = new HashSet<>();
      for (Binding<T> binding : bindings)
      {
         instances.add(new GuiceExportedInstance<>(sourceAddon, binding));
      }
      return instances;
   }

   @SuppressWarnings("unchecked")
   @Override
   public <T> Set<ExportedInstance<T>> getExportedInstances(String type)
   {
      try
      {
         Class<T> clazz = (Class<T>) sourceAddon.getClassLoader().loadClass(type);
         return getExportedInstances(clazz);
      }
      catch (ClassNotFoundException e)
      {
      }
      return null;
   }

   @Override
   public <T> ExportedInstance<T> getExportedInstance(Class<T> type)
   {
      Binding<T> binding = injector.getBinding(type);
      return new GuiceExportedInstance<>(sourceAddon, binding);
   }

   @SuppressWarnings("unchecked")
   @Override
   public <T> ExportedInstance<T> getExportedInstance(String type)
   {
      try
      {
         Class<T> clazz = (Class<T>) sourceAddon.getClassLoader().loadClass(type);
         return getExportedInstance(clazz);
      }
      catch (ClassNotFoundException e)
      {
      }
      return null;
   }

   @Override
   public Set<Class<?>> getExportedTypes()
   {
      Map<Key<?>, Binding<?>> allBindings = injector.getAllBindings();
      Set<Class<?>> types = new HashSet<>();
      for (Key<?> key : allBindings.keySet())
      {
         types.add(key.getTypeLiteral().getRawType());
      }
      return types;
   }

   @SuppressWarnings("unchecked")
   @Override
   public <T> Set<Class<T>> getExportedTypes(Class<T> type)
   {
      Map<Key<?>, Binding<?>> allBindings = injector.getAllBindings();
      Set<Class<T>> types = new HashSet<>();
      for (Key<?> key : allBindings.keySet())
      {
         Class<?> rawType = key.getTypeLiteral().getRawType();
         if (rawType.isAssignableFrom(type))
         {
            types.add((Class<T>) rawType);
         }
      }
      return types;
   }

   @Override
   public boolean hasService(Class<?> clazz)
   {
      Key<?> key = Key.get(clazz);
      return injector.getExistingBinding(key) != null;
   }

   @Override
   public boolean hasService(String clazz)
   {
      try
      {
         Class<?> type = sourceAddon.getClassLoader().loadClass(clazz);
         return hasService(type);
      }
      catch (ClassNotFoundException e)
      {
      }
      return false;
   }

}