package com.change_vision.astah.extension.plugin.dbreverse.reverser.finder;

import com.change_vision.jude.api.inf.model.IERDatatype;
import com.change_vision.jude.api.inf.model.IERSchema;

public class DatatypeFinder {

    private IERSchema schema;

    public DatatypeFinder(IERSchema schema) {
        this.schema = schema;
    }

    public IERDatatype find(String name) {
        if(name == null) throw new IllegalArgumentException("name is null");
        IERDatatype[] datatypes = schema.getDatatypes();
        for (IERDatatype datatype : datatypes) {
            if (datatype.getName().equalsIgnoreCase(name)) {
                return datatype;
            }
        }
        return null;
    }

}
