package com.game.b1ingservice.payload.commons;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author natthawut
 */
@Data
@JsonSerialize(using = com.game.b1ingservice.payload.commons.PageResponse.ResponsePageSerializer.class)
public class PageResponse implements Serializable {
    private Boolean status;
    private String message;
    private PageObject page;
    private Object data;
    private String fieldName;

    public static class ResponsePageSerializer extends JsonSerializer<PageResponse> {
        @Override
        public void serialize(PageResponse value, JsonGenerator jsonGenerator, SerializerProvider provider)
                throws IOException {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeBooleanField("status", value.getStatus());
            jsonGenerator.writeStringField("message", value.getMessage());
            if (value.getPage() != null) {
                jsonGenerator.writeObjectField("page", value.getPage());
            }
            jsonGenerator.writeObjectField(value.getFieldName(), value.getData());
            jsonGenerator.writeEndObject();
        }
    }
}
