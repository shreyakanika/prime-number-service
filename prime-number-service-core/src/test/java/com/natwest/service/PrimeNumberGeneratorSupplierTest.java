package com.natwest.service;

import com.natwest.generator.PrimeNumberGenerator;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.natwest.service.PrimeNumberGeneratorSupplier;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PrimeNumberGeneratorSupplierTest {

    public @Rule ExpectedException expectedException = ExpectedException.none();

    private @Mock BeanFactory beanFactory;
    private @Mock PrimeNumberGenerator generator;

    private PrimeNumberGeneratorSupplier supplier;

    @Before
    public void init() {
        supplier = new PrimeNumberGeneratorSupplier(beanFactory);
    }

    @Test
    public void generatorNotFound() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectCause(instanceOf(NoSuchBeanDefinitionException.class));
        when(beanFactory.getBean("name", PrimeNumberGenerator.class))
                .thenThrow(NoSuchBeanDefinitionException.class);
        supplier.get("name");
    }

    @Test
    public void generatorFound() {
        when(beanFactory.getBean("name", PrimeNumberGenerator.class)).thenReturn(generator);
        assertThat(supplier.get("name")).isSameAs(generator);
    }
}
