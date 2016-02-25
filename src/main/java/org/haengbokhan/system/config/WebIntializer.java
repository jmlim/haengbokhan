package org.haengbokhan.system.config;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * @author Administrator
 * 
 */
public class WebIntializer implements WebApplicationInitializer {

	/**
	 * @see org.springframework.web.WebApplicationInitializer#onStartup(javax.servlet.ServletContext)
	 */
	public void onStartup(ServletContext servletContext)
			throws ServletException {
		AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
		rootContext.register(SpringAppConfig.class);
		servletContext.addListener(new ContextLoaderListener(rootContext));

		FilterRegistration.Dynamic delegatingFilterProxy = servletContext
				.addFilter("springSecurityFilterChain",
						new DelegatingFilterProxy());
		delegatingFilterProxy.addMappingForUrlPatterns(null, false, "/*");

		FilterRegistration.Dynamic characterEncodingFilter = servletContext
				.addFilter("encodingFilter", new CharacterEncodingFilter());
		characterEncodingFilter.setInitParameter("encoding", "UTF-8");
		DispatcherType[] dispatcherTypes = { DispatcherType.FORWARD,
				DispatcherType.INCLUDE };
		characterEncodingFilter.addMappingForUrlPatterns(
				EnumSet.of(DispatcherType.REQUEST, dispatcherTypes), false,
				"/*");

		FilterRegistration.Dynamic openEntityManagerInViewFilter = servletContext
				.addFilter("openEntityManagerInViewFilter",
						new OpenEntityManagerInViewFilter());
		openEntityManagerInViewFilter.setInitParameter(
				"entityManagerFactoryBeanName", "entityManagerFactory");
		openEntityManagerInViewFilter.addMappingForUrlPatterns(null, false,
				"/*");

		AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();
		dispatcherContext.register(SpringWebConfig.class);
		ServletRegistration.Dynamic dispatcher = servletContext.addServlet(
				"haengbokhanDispatcher", new DispatcherServlet(
						dispatcherContext));
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping("/");
	}

}
