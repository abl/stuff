package edu.northeastern.cs5500.backend.model;

import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class Session implements Model {
    private ObjectId id;
    private ObjectId userId;
}
