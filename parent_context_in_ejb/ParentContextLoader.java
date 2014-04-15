package com.drq.lm.helpers;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.access.BeanFactoryLocator;
import org.springframework.beans.factory.access.BeanFactoryReference;
import org.springframework.context.ApplicationContext;
import org.springframework.context.access.ContextSingletonBeanFactoryLocator;

import javax.servlet.ServletContext;

/**

 */
public class ParentContextLoader implements InitializingBean, DisposableBean{

    private static final Logger logger = Logger.getLogger(ParentContextLoader.class);

    public String locatorFactorySelector;

    public String parentContextKey;

    private BeanFactoryReference parentContextRef;

    protected void loadParentContext() throws BeansException {

        if (parentContextKey != null) {
            // locatorFactorySelector may be null, indicating the default "classpath*:beanRefContext.xml"
            BeanFactoryLocator locator = ContextSingletonBeanFactoryLocator.getInstance(locatorFactorySelector);
            if (logger.isDebugEnabled()) {
                logger.debug("Getting parent context definition: using parent context key of '" +
                        parentContextKey + "' with BeanFactoryLocator");
            }
            this.parentContextRef = locator.useBeanFactory(parentContextKey);
        }
    }

    public ApplicationContext getParentContext() {
        if (this.parentContextRef != null) {
             return (ApplicationContext) this.parentContextRef.getFactory();
        }
        return null;
    }

    public void afterPropertiesSet() throws Exception {
        loadParentContext();
    }

    public void destroy() throws Exception {
        if (this.parentContextRef != null) {
            this.parentContextRef.release();
            this.parentContextRef = null;
        }
    }

    public String getParentContextKey() {
        return parentContextKey;
    }

    public void setParentContextKey(String parentContextKey) {
        this.parentContextKey = parentContextKey;
    }

    public String getLocatorFactorySelector() {
        return locatorFactorySelector;
    }

    public void setLocatorFactorySelector(String locatorFactorySelector) {
        this.locatorFactorySelector = locatorFactorySelector;
    }
}
