/**
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.furnace.container.guice.mock.services;

import com.google.inject.AbstractModule;

/**
 * 
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class ProducerModule extends AbstractModule
{
   @Override
   protected void configure()
   {
      bind(ExportedService.class).toInstance(new ExportedService());
   }

}
