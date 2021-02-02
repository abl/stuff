package edu.northeastern.cs5500.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class Stuff implements Model {
    private ObjectId id;
    private String title;
    private String description;

    /** @return true if this Stuff is valid */
    @JsonIgnore
    public boolean isValid() {
        return title != null && !title.isEmpty();
    }
}
