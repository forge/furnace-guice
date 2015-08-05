package org.jboss.forge.furnace.container.guice;

import static org.hamcrest.CoreMatchers.notNullValue;

import javax.inject.Inject;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.forge.furnace.Furnace;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
@RunWith(Arquillian.class)
public class AddonArchiveDefaultSmokeTest
{
   @Inject
   private Furnace furnace;

   @Test
   public void test()
   {
      Assert.assertThat(furnace, notNullValue());
   }

}
