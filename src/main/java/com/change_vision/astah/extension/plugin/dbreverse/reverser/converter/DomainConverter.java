package com.change_vision.astah.extension.plugin.dbreverse.reverser.converter;

import com.change_vision.astah.extension.plugin.dbreverse.reverser.finder.DatatypeFinder;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.model.DatatypeInfo;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.model.DomainInfo;
import com.change_vision.jude.api.inf.editor.ERModelEditor;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.model.IERDatatype;
import com.change_vision.jude.api.inf.model.IERDomain;
import com.change_vision.jude.api.inf.model.IERModel;
import com.change_vision.jude.api.inf.model.IERSchema;

public class DomainConverter {

    private ERModelEditor editor;
    private IERModel model;
    private DatatypeFinder datatypeFinder;
    private DatatypeConverter datatypeConverter;

    public DomainConverter(ERModelEditor editor, IERModel model) {
        this.editor = editor;
        this.model = model;
        IERSchema defaultSchema = model.getSchemata()[0];
        this.datatypeFinder = new DatatypeFinder(defaultSchema);
        this.datatypeConverter = new DatatypeConverter(editor, model);
    }

    public IERDomain convert(DomainInfo domainInfo) throws InvalidEditingException {
        if (domainInfo == null) throw new IllegalArgumentException("domainInfo is null.");
        String name = domainInfo.getName();
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("name is empty");
        String datatypeName = domainInfo.getDatatypeName();
        IERDatatype datatype = datatypeFinder.find(datatypeName );
        if (datatype == null) {
            DatatypeInfo datatypeInfo = new DatatypeInfo();
            datatypeInfo.setName(datatypeName);
            datatype = datatypeConverter.convert(datatypeInfo );
        }
        return editor.createERDomain(model, null, name, name, datatype);
    }

    void setDatatypeFinder(DatatypeFinder datatypeFinder) {
        this.datatypeFinder = datatypeFinder;
    }

    public void setDatatypeConverter(DatatypeConverter datatypeConverter) {
        this.datatypeConverter = datatypeConverter;
    }

}
