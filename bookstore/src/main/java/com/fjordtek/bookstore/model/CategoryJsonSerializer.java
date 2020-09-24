package com.fjordtek.bookstore.model;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

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