package com.change_vision.astah.extension.plugin.dbreverse.reverser.finder;

import com.change_vision.jude.api.inf.model.IEREntity;
import com.change_vision.jude.api.inf.model.IERSchema;

public class EntityFinder {

    private IERSchema schema;

    public EntityFinder(IERSchema schema) {
        this.schema = schema;
    }

    public IEREntity find(String name) {
        if (name == null) throw new IllegalArgumentException("name is null");
        for (IEREntity entity : schema.getEntities()) {
            if (entity.getName().equals(name)) {
                return entity;
            }
        }
        return null;
    }

}
