package com.salvador.spi;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeanManager;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by IntelliJ IDEA.
 * User: MEarlam
 * Date: 14/05/12
 * Time: 09:52
 */
public class ViewContextExtensionTest {

    ViewContextExtension viewContextExtension;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        viewContextExtension = new ViewContextExtension();
    }

    @Test
    public void testAfterBeanDiscovery() {
        AfterBeanDiscovery afterBeanDiscovery = mock(AfterBeanDiscovery.class);
        BeanManager beanManager = mock(BeanManager.class);
        viewContextExtension.afterBeanDiscovery(afterBeanDiscovery, beanManager);
        verify(afterBeanDiscovery).addContext(any(ViewContext.class));
    }
}
