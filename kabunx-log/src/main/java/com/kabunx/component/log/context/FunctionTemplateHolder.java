package com.kabunx.component.log.context;

import com.kabunx.component.log.FunctionTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;

import java.util.Map;
import java.util.Objects;

public class FunctionTemplateHolder implements ApplicationContextAware, InitializingBean {

    private ApplicationContext applicationContext;
    private Map<String, FunctionTemplate> allFunctionMap;

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, FunctionTemplate> map = applicationContext.getBeansOfType(FunctionTemplate.class);
        map.forEach((key, parser) -> allFunctionMap.put(parser.functionName(), parser));
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public FunctionTemplate getFunctionTemplate(String functionName) {
        return allFunctionMap.get(functionName);
    }

    public boolean isBeforeFunction(String functionName) {
        return Objects.nonNull(allFunctionMap.get(functionName)) && allFunctionMap.get(functionName).executeBefore();
    }
}
