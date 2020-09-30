//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.model.book;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * This class implements Jackson JSON serializer for Category entity class.
 * <p>
 * When writing a JSON output based on a Category entity object, the processing
 * is handled in the way described in this class.
 * <p>
 * Unless specifically instructed, customization defined in this class
 * does not affect native Spring REST API JSON outputs generated
 * by a controller having either @BasePathAwareController
 * or @RepositoryRestController annotation.
 *
 * @author Pekka Helenius
 */

public class CategoryJsonSerializer extends StdSerializer<Category> {
	private static final long serialVersionUID = 6376700470881235634L;

	public CategoryJsonSerializer() {
		this(null);
	}

	public CategoryJsonSerializer(Class<Category> jd) {
		super(jd);
	}

	@Override
	public void serialize(Category category, JsonGenerator gen, SerializerProvider provider)
			throws IOException {
				gen.writeStartObject();
				// Category class Id has JsonIgnore annotation
				//gen.writeFieldId(category.getId());
				gen.writeStringField("name", category.getName());
				gen.writeEndObject();
	}

}