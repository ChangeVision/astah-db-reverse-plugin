package com.change_vision.astah.extension.plugin.dbreverse.reverser.finder;

import com.change_vision.jude.api.inf.model.IERAttribute;
import com.change_vision.jude.api.inf.model.IEREntity;

public class AttributeFinder {

    public IERAttribute find(IEREntity entity, String name) {
        if(entity == null) throw new IllegalArgumentException("entity is null");
        if(name == null) throw new IllegalArgumentException("name is null");
        
        IERAttribute[] primaryKeys = entity.getPrimaryKeys();
        for (IERAttribute pk : primaryKeys) {
            if(pk.getName().equals(name)) return pk;
        }
        IERAttribute[] nonPrimaryKeys = entity.getNonPrimaryKeys();
        for (IERAttribute nonPK : nonPrimaryKeys) {
            if(nonPK.getName().equals(name)) return nonPK;
        }
        return null;
    }

}
