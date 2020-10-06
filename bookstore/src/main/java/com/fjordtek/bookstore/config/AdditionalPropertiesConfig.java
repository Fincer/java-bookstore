//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
*
* This class implements a custom EnvironmentPostProcessor interface.
* <p>
* Main purpose of this implementation is to load additional properties files into Spring
* environment as early as possible during Spring Boot initialization process.
* <p>
* Loading of this class must be enabled in /resources/META-INF/spring.factories file.
*
* @author Pekka Helenius
*/

@Configuration
//@Order(Ordered.LOWEST_PRECEDENCE)
public class AdditionalPropertiesConfig implements EnvironmentPostProcessor {

	private List<Resource> getAdditionalResources() {
		List<Resource> resources = new ArrayList<Resource>();

		// Add your additional (classpath, filesystem, etc.) properties files here
		resources.add(new ClassPathResource("website.properties"));
		resources.add(new ClassPathResource("authentication.properties"));
		resources.add(new ClassPathResource("categories.properties"));

		return resources;
	}

	private void showSpringConfiguration(MutablePropertySources properties) {

		System.out.print("** Used Spring property sources **\n\n");
		for (PropertySource<?> source : properties) {
			System.out.println(source.toString());
		}
	}

	@Override
	public void postProcessEnvironment(
			ConfigurableEnvironment environment,
			SpringApplication application
			) {

		MutablePropertySources propertySources = environment.getPropertySources();

		for (Resource res : getAdditionalResources()) {
			try {
				propertySources.addLast(
						new PropertiesPropertySource(
								res.getFilename(),
								PropertiesLoaderUtils.loadProperties(res)
								)
						);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		showSpringConfiguration(propertySources);
	}

}

