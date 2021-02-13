package edu.northeastern.cs5500.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class User implements Model {
    private ObjectId id;
    private String username;
    private String emailAddress;

    /** @return true if this Stuff is valid */
    @JsonIgnore
    public boolean isValid() {
        return username != null && !username.isEmpty();
    }
}
