//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
* Additionally, this class provides processing methods for Spring profile
* properties configuration.
* <p>
* Loading of this class must be enabled in /resources/META-INF/spring.factories file.
*
* @author Pekka Helenius
*/

@Configuration
//@Order(Ordered.LOWEST_PRECEDENCE)
public class AdditionalPropertiesConfig implements EnvironmentPostProcessor {

	// Add global properties
	private List<Resource> getAdditionalResources() {
		List<Resource> classPathResources = new ArrayList<Resource>();

		/*
		 *  Add your additional global properties files here
		 */
		classPathResources.add(new ClassPathResource("website.properties"));
		classPathResources.add(new ClassPathResource("authentication.properties"));
		classPathResources.add(new ClassPathResource("categories.properties"));

		for (Resource classPathResource : classPathResources) {
			try {
				classPathResource.getInputStream();
			} catch (IOException e) {
				System.err.println(
						"Resource " + classPathResource.getFilename() + " not found!"
						);
			}
		}

		return classPathResources;
	}

	// Add predefined profile properties
	private List<Resource> getAdditionalProfileResources(String[] profiles) {
		List<Resource> classPathResources = new ArrayList<Resource>();

		/*
		 * Add your additional profile property file prefixes here
		 * For instance, if profile is 'dev':
		 * "database" ->> "database-dev.properties"
		 */
		String[] resourceFilePrefixes = {
				"database"
		};

		for (String profile : profiles) {

			for (String prefix : resourceFilePrefixes) {
				try {
					ClassPathResource classPathResource = new ClassPathResource(prefix + "-" + profile + ".properties");

					/*
					 * Do not use getFile() method.
					 * Instead, use getInputStream(), required by Tomcat/WAR deployment
					 */

					classPathResource.getInputStream();

					classPathResources.add(classPathResource);
					System.out.printf(
							"Profile properties file found: %s\n",
							classPathResource.getFilename()
							);
				} catch (IOException e) {

					if ( prefix.equals("database") && profile.equals("prod") ) {

						System.err.println(String.join("\n",
								"Production mode was enabled. However the following issue occured.",
								"",
								"Missing file: database-prod.properties",
								"",
								"File is not found from application class path.",
								"",
								"Please provide the missing file with following entries:",
								"",
								"spring.datasource.driver-class-name                = com.mysql.cj.jdbc.Driver",
								"spring.datasource.url                              = jdbc:mysql://<host_ip>:<TCP port>/<DB_DATABASE_NAME> ...",
								"spring.datasource.username                         = <DB_USERNAME>",
								"spring.datasource.password                         = <DB_PASSWORD>",
								"spring.datasource.initialization-mode              = always",
								"",
								"spring.jpa.hibernate.use-new-id-generator-mappings = false",
								"spring.jpa.hibernate.naming.physical-strategy      = org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl",
								"",
								"Unable to run application. Exiting."
								)
								);
						System.exit(1);

					} else {
						System.err.println(
							"Resource " +
							prefix + "-" + profile + ".properties" +
							" not found!"
							);
					}
				}
				continue;
			}
		}
		return classPathResources;
	}

	private void checkPropertiesExist(
			ConfigurableEnvironment environment,
			String[] propEntries
			) {

		List<String> missingProperties = new ArrayList<String>();

		for (String prop : propEntries) {
			try {
				environment.getRequiredProperty(prop);
			} catch (IllegalStateException e) {
				missingProperties.add(prop);
			}
		}

		if (missingProperties.size() != 0) {
			System.err.printf(
					"\nFollowing, required application property entries are missing:\n\n"
					);
			int i = 0;
			while (i < missingProperties.size()) {
				System.err.printf(
						"%s\n",
						missingProperties.get(i)
						);
				i++;
			}
			System.err.printf(
					"\nCannot continue. Exiting. (Active profiles: %s)\n",
					Arrays.stream(environment.getActiveProfiles())
						.toArray()
					);
			System.exit(1);
		}

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

		List<List<Resource>> additionalResources = new ArrayList<List<Resource>>();

		additionalResources.add(getAdditionalResources());
		additionalResources.add(getAdditionalProfileResources(environment.getActiveProfiles()));

		for (List<Resource> resList : additionalResources) {
			for (Resource res : resList) {
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
		}

		if ( Arrays.stream(environment.getActiveProfiles()).anyMatch(p -> p.contains("prod")) ) {
			checkPropertiesExist(
					environment,
					new String[] {
							"spring.datasource.driver-class-name",
							"spring.datasource.url",
							"spring.datasource.username",
							"spring.datasource.password",
							"spring.datasource.initialization-mode",
							"spring.jpa.hibernate.use-new-id-generator-mappings",
							"spring.jpa.hibernate.naming.physical-strategy"
					}
					);
		}

		showSpringConfiguration(propertySources);
	}

}

