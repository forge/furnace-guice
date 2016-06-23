package org.jboss.forge.furnace.container.guice.mock.services;

import com.google.inject.Binder;
import com.google.inject.Module;

public class ExplicitBindingsModule implements Module
{
   @Override
   public void configure(Binder binder)
   {
      binder.requireExplicitBindings();
   }
}
