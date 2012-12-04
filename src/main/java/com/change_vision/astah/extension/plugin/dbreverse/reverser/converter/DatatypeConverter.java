package com.change_vision.astah.extension.plugin.dbreverse.reverser.converter;

import com.change_vision.astah.extension.plugin.dbreverse.reverser.model.DatatypeInfo;
import com.change_vision.jude.api.inf.editor.ERModelEditor;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.model.IERDatatype;
import com.change_vision.jude.api.inf.model.IERModel;

public class DatatypeConverter {

    private ERModelEditor editor;
    private IERModel erModel;

    public DatatypeConverter(ERModelEditor editor, IERModel erModel) {
        this.editor = editor;
        this.erModel = erModel;
    }

    public IERDatatype convert(DatatypeInfo datatypeInfo) throws InvalidEditingException {
        if(datatypeInfo == null) throw new IllegalArgumentException("datatype is null.");
        String name = datatypeInfo.getName();
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("name is empty.");
        IERDatatype converted = editor.createERDatatype(erModel, name);
        converted.setLengthConstraint(datatypeInfo.getLengthConstraint());
        converted.setPrecisionConstraint(datatypeInfo.getPrecisionConstraint());
        return converted;
    }

}
