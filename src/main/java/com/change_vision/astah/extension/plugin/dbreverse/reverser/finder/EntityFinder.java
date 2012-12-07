package com.change_vision.astah.extension.plugin.dbreverse.reverser.finder;

import com.change_vision.jude.api.inf.model.IEREntity;
import com.change_vision.jude.api.inf.model.IERSchema;

public class EntityFinder {

    public IEREntity find(IERSchema schema, String name) {
        if (schema == null) throw new IllegalArgumentException("schema is null");
        if (name == null) throw new IllegalArgumentException("name is null");
        for (IEREntity entity : schema.getEntities()) {
            if (entity.getName().equals(name)) {
                return entity;
            }
        }
        return null;
    }

}
