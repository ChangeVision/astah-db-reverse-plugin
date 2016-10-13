package com.change_vision.astah.extension.plugin.dbreverse.reverser.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.change_vision.astah.extension.plugin.dbreverse.reverser.finder.DatatypeFinder;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.finder.DomainFinder;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.model.AttributeInfo;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.model.DatatypeInfo;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.model.DomainInfo;
import com.change_vision.jude.api.inf.editor.ERModelEditor;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.model.IERAttribute;
import com.change_vision.jude.api.inf.model.IERDatatype;
import com.change_vision.jude.api.inf.model.IERDomain;
import com.change_vision.jude.api.inf.model.IEREntity;
import com.change_vision.jude.api.inf.model.IERModel;
import com.change_vision.jude.api.inf.model.IERSchema;

public class AttributeConverter {

    private static final Logger logger = LoggerFactory.getLogger(AttributeConverter.class);
    private ERModelEditor editor;
    private IERModel erModel;
    private DatatypeFinder datatypeFinder;
    private DatatypeConverter datatypeConverter;
    private DomainFinder domainFinder;

    public AttributeConverter(ERModelEditor editor,IERModel erModel) {
        this.editor = editor;
        this.erModel = erModel;
        IERSchema schema = erModel.getSchemata()[0];
        this.datatypeFinder = new DatatypeFinder(schema);
        this.datatypeConverter = new DatatypeConverter(editor, erModel);
        this.domainFinder = new DomainFinder(schema);
    }

    public IERAttribute convert(IEREntity owner, AttributeInfo attributeInfo) throws InvalidEditingException {
        if(attributeInfo == null) throw new IllegalArgumentException("argument is null");
        if(owner == null) throw new IllegalArgumentException("owner is null");
        IERAttribute converted = doConvert(owner, attributeInfo);
        if(converted == null) return null;
        setAttributeInfo(converted,attributeInfo);
        return converted;
    }

    private void setAttributeInfo(IERAttribute converted, AttributeInfo attributeInfo) throws InvalidEditingException {
        converted.setPrimaryKey(attributeInfo.isPK());
        converted.setNotNull(attributeInfo.isPK() ? true : attributeInfo.isNotNull());
        converted.setDefaultValue(attributeInfo.getDefaultValue());
        converted.setDefinition(attributeInfo.getDefinition());
        convertLengthPrecision(converted, attributeInfo.getLengthPrecision());
    }

    private void convertLengthPrecision(IERAttribute converted, String lengthPrecision) {
        try {
            converted.setLengthPrecision(lengthPrecision);
        } catch (InvalidEditingException e) {
            if (InvalidEditingException.PARAMETER_ERROR_KEY.equals(e.getKey())) {
                logger.warn("Ignored invalid length/precision:=" + converted.getName());
            }
        }
    }

    private IERAttribute doConvert(IEREntity owner, AttributeInfo attributeInfo)
            throws InvalidEditingException {
        IERAttribute converted = convertByDomain(owner, attributeInfo);
        if(converted != null) return converted;
        return convertByDataType(owner, attributeInfo);
    }

    private IERAttribute convertByDataType(IEREntity owner, AttributeInfo attributeInfo)
            throws InvalidEditingException {
        DatatypeInfo dataTypeInfo = attributeInfo.getDataType();
        if (isDatatypeAttribute(dataTypeInfo) == false) return null;
        IERDatatype datatype = createERDatatype(dataTypeInfo);
        return editor.createERAttribute(owner, attributeInfo.getName(), attributeInfo.getName(), datatype);
    }

    private IERAttribute convertByDomain(IEREntity owner, AttributeInfo attributeInfo)
            throws InvalidEditingException {
        DomainInfo domainInfo = attributeInfo.getDomain();
        if (isDomainAttribute(domainInfo) == false) return null;
        String domainName = domainInfo.getName();
        IERDomain iDomain = domainFinder.find(domainName);
        if(iDomain == null) iDomain = createDomain(domainInfo);
        String name = attributeInfo.getName();
        return editor.createERAttribute(owner, name, name, iDomain);
    }

    private boolean isDatatypeAttribute(DatatypeInfo dataTypeInfo) {
        return !"".equals(dataTypeInfo.getName());
    }

    private boolean isDomainAttribute(DomainInfo domainInfo) {
        return !"".equals(domainInfo.getName());
    }
    
    private IERDomain createDomain(DomainInfo dmInfo) throws InvalidEditingException {
        String domainName = dmInfo.getName();
        String datatypeName = dmInfo.getDatatypeName();
        IERDatatype datatype = datatypeFinder.find(datatypeName);
        if (datatype == null) {
            DatatypeInfo datatypeInfo = new DatatypeInfo();
            datatypeInfo.setName(datatypeName);
            datatype = datatypeConverter.convert(datatypeInfo);
        }
        return  editor.createERDomain(erModel, null, domainName, domainName, datatype);
    }
    
    private IERDatatype createERDatatype(DatatypeInfo datatypeInfo) throws InvalidEditingException {
        String datatypeName = datatypeInfo.getName();
        IERDatatype datatype = datatypeFinder.find(datatypeName);
        if (datatype == null) {
            datatype = datatypeConverter.convert(datatypeInfo);
        }
        String dLen = datatypeInfo.getDefaultLength();
        String dPre = datatypeInfo.getDefaultPrecision();
        StringBuilder lengthPrecision = new StringBuilder();
        lengthPrecision.append(dLen);
        lengthPrecision.append(("".equals(dLen) || "".equals(dPre)) ? "" : ",");
        lengthPrecision.append(dPre);
        if (lengthPrecision.length() > 0) {
            datatype.setDefaultLengthPrecision(lengthPrecision.toString());
        }
        datatype.setDescription(datatypeInfo.getDescription());

        return datatype;
    }

}
