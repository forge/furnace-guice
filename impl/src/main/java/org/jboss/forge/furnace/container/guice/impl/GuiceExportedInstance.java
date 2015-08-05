/**
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.furnace.container.guice.impl;

import org.jboss.forge.furnace.addons.Addon;
import org.jboss.forge.furnace.spi.ExportedInstance;

import com.google.inject.Binding;

/**
 * 
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class GuiceExportedInstance<T> implements ExportedInstance<T>
{
   private final Addon sourceAddon;
   private final Binding<T> binding;

   public GuiceExportedInstance(Addon sourceAddon, Binding<T> binding)
   {
      super();
      this.sourceAddon = sourceAddon;
      this.binding = binding;
   }

   @Override
   public T get()
   {
      return binding.getProvider().get();
   }

   @Override
   public void release(T instance)
   {
   }

   @SuppressWarnings("unchecked")
   @Override
   public Class<? extends T> getActualType()
   {
      return (Class<? extends T>) binding.getKey().getTypeLiteral().getRawType();
   }

   @Override
   public Addon getSourceAddon()
   {
      return sourceAddon;
   }

}
