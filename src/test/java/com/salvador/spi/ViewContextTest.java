package com.salvador.spi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Created by IntelliJ IDEA.
 * User: MEarlam
 * Date: 14/05/12
 * Time: 09:34
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(FacesContext.class)
public class ViewContextTest {

    ViewContext viewContext;
    final Map viewMap = new LinkedHashMap();

    @Mock
    FacesContext facesContext;

    @Mock
    UIViewRoot uiViewRoot;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockStatic(FacesContext.class);
        when(FacesContext.getCurrentInstance()).thenReturn(facesContext);
        when(facesContext.getViewRoot()).thenReturn(uiViewRoot);
        when(uiViewRoot.getViewMap(true)).thenReturn(viewMap);

        viewContext = new ViewContext();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetScopeNotInViewCreationalContext() {
        Bean contextual = mock(Bean.class);
        CreationalContext creationalContext = mock(CreationalContext.class);
        when(contextual.create(creationalContext)).thenReturn(contextual);
        final Object obj = viewContext.get(contextual, creationalContext);
        assertThat(obj, notNullValue());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetScopeIsInViewCreationalContext() {
        Bean contextual = mock(Bean.class);
        when(contextual.getName()).thenReturn("bean");
        viewMap.put("bean", contextual);
        CreationalContext creationalContext = mock(CreationalContext.class);
        when(contextual.create(creationalContext)).thenReturn(contextual);
        final Object obj = viewContext.get(contextual, creationalContext);
        assertThat(obj, notNullValue());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetScopeNotInView() {
        Bean contextual = mock(Bean.class);
        final Object obj = viewContext.get(contextual);
        assertThat(obj, nullValue());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetScopeIsInView() {
        Bean contextual = mock(Bean.class);
        when(contextual.getName()).thenReturn("bean");
        viewMap.put("bean", contextual);
        final Object obj = viewContext.get(contextual);
        assertThat(obj, notNullValue());
    }

    @Test
    public void testGetScope() {
        final Class<? extends Annotation> scope = viewContext.getScope();
        assertThat(scope.getClass(), is(ViewScoped.class.getClass()));
    }


    @Test
    public void testIsActive() {
        assertThat(viewContext.isActive(), is(true));
    }

}
