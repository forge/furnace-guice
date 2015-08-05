/**
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.furnace.container.guice;

import com.google.inject.Module;

/**
 * Classes registered as a Service Provider under this interface are registered through their constructors using the
 * default {@link Module} implementation. They do not require to implement this interface.
 * 
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public interface Service
{

}
