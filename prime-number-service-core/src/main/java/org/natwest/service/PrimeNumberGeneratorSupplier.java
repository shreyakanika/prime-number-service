package org.natwest.service;

import com.natwest.generator.PrimeNumberGenerator;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/*
Component class to return the requested bean of the prime number generator based on the incoming name.
 */
@Component
public class PrimeNumberGeneratorSupplier {

    private final BeanFactory factory;

    @Autowired
    public PrimeNumberGeneratorSupplier(BeanFactory factory) {
        this.factory = factory;
    }

    //Method to return the requested the bean.
    // If the requested bean does not exist, then it will throw the `NoSuchBeanDefinitionException` exception.
    public PrimeNumberGenerator get(final String name) {
        try {
            return factory.getBean(name, PrimeNumberGenerator.class);
        } catch (NoSuchBeanDefinitionException e) {
            throw new IllegalArgumentException("Prime number generator '" + name + "' algorithm not found", e);
        }
    }
}
