package sample.apollo;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Created by za-daixiaojun on 2018/1/24.
 */
@Component
public class PrintBeansPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        String[] beanNames = beanFactory.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            BeanDefinition def = beanFactory.getBeanDefinition(beanName);
            System.out.println("***** " + def.getBeanClassName() + " - " + def.getScope());
        }
    }
}
