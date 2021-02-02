package edu.northeastern.cs5500.backend.repository;

import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.northeastern.cs5500.backend.model.Model;
import edu.northeastern.cs5500.backend.service.MongoDBService;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.Nullable;
import javax.inject.Inject;
import org.bson.types.ObjectId;

public class MongoDBRepository<T extends Model> implements GenericRepository<T> {

    MongoCollection<T> collection;

    @Inject
    public MongoDBRepository(Class<T> clazz, MongoDBService mongoDBService) {
        MongoDatabase mongoDatabase = mongoDBService.getMongoDatabase();
        collection = mongoDatabase.getCollection(clazz.getName(), clazz);
    }

    @Nullable
    public T get(ObjectId id) {
        return collection.find(eq("id", id)).first();
    }

    @Override
    public T add(T item) {
        if (item.getId() == null) {
            item.setId(new ObjectId());
        }
        collection.insertOne(item);
        return item;
    }

    @Override
    public T update(T item) {
        return collection.findOneAndReplace(eq("id", item.getId()), item);
    }

    @Override
    public void delete(ObjectId id) {
        collection.deleteOne(eq("id", id));
    }

    @Override
    public Collection<T> getAll() {
        return collection.find().into(new ArrayList<>());
    }

    @Override
    public long count() {
        return collection.countDocuments();
    }
}
