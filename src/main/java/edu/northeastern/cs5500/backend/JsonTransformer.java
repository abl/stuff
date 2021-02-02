package edu.northeastern.cs5500.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.io.IOException;
import java.io.StringWriter;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import spark.ResponseTransformer;

/** Serializes ObjectIds as simple strings instead of maps. */
class CustomObjectMapper extends ObjectMapper {
    private static final long serialVersionUID = -4133993268721110576L;

    public CustomObjectMapper() {
        SimpleModule module = new SimpleModule("ObjectIdmodule");
        module.addSerializer(ObjectId.class, new ToStringSerializer());
        this.registerModule(module);
    }
}

@Slf4j
public class JsonTransformer implements ResponseTransformer {

    @Inject
    JsonTransformer() {
        super();
    }

    @Override
    public String render(Object model) {
        try {
            CustomObjectMapper mapper = new CustomObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            StringWriter sw = new StringWriter();
            mapper.writeValue(sw, model);
            return sw.toString();
        } catch (IOException e) {
            log.error("JsonTransformer > render > IOException?");
            e.printStackTrace();
        }
        return "{\"status\": \"ERROR\"}";
    }
}
