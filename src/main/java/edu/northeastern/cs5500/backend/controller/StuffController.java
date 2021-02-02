package edu.northeastern.cs5500.backend.controller;

import edu.northeastern.cs5500.backend.model.Stuff;
import edu.northeastern.cs5500.backend.repository.GenericRepository;
import java.util.Collection;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

@Singleton
@Slf4j
public class StuffController {
    private final GenericRepository<Stuff> stuffRepository;

    @Inject
    StuffController(GenericRepository<Stuff> stuffRepository) {
        this.stuffRepository = stuffRepository;

        log.info("StuffController > construct");

        if (stuffRepository.count() > 0) {
            return;
        }

        log.info("StuffController > construct > adding default stuff");

        final Stuff defaultStuff1 = new Stuff();
        defaultStuff1.setTitle("Hot dog");

        final Stuff defaultStuff2 = new Stuff();
        defaultStuff2.setTitle("A steak");
        defaultStuff2.setDescription("Not a hot dog");

        try {
            addStuff(defaultStuff1);
            addStuff(defaultStuff2);
        } catch (Exception e) {
            log.error("StuffController > construct > adding default stuff > failure?");
            e.printStackTrace();
        }
    }

    @Nullable
    public Stuff getStuff(@Nonnull ObjectId uuid) {
        log.debug("StuffController > getStuff({})", uuid);
        return stuffRepository.get(uuid);
    }

    @Nonnull
    public Collection<Stuff> getStuff() {
        log.debug("StuffController > getStuff()");
        return stuffRepository.getAll();
    }

    @Nonnull
    public Stuff addStuff(@Nonnull Stuff stuff) throws Exception {
        log.debug("StuffController > addStuff(...)");
        if (!stuff.isValid()) {
            // TODO: replace with a real invalid object exception
            // probably not one exception per object type though...
            throw new Exception("InvalidStuffException");
        }

        ObjectId id = stuff.getId();

        if (id != null && stuffRepository.get(id) != null) {
            // TODO: replace with a real duplicate key exception
            throw new Exception("DuplicateKeyException");
        }

        return stuffRepository.add(stuff);
    }

    public void updateStuff(@Nonnull Stuff stuff) throws Exception {
        log.debug("StuffController > updateStuff(...)");
        stuffRepository.update(stuff);
    }

    public void deleteStuff(@Nonnull ObjectId id) throws Exception {
        log.debug("StuffController > deleteStuff(...)");
        stuffRepository.delete(id);
    }
}
