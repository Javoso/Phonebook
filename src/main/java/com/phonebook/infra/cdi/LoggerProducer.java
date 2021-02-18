package com.phonebook.infra.cdi;

import java.util.logging.Logger;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;

public class LoggerProducer {

	@Produces
	public Logger getLogger(InjectionPoint p) {
		Bean<?> bean = p.getBean();
		return Logger.getLogger(bean.getBeanClass().getCanonicalName());
	}
	
	public static Logger getLogger(Class<?> clazz) {
		return Logger.getLogger(clazz.getName());
	}

}
