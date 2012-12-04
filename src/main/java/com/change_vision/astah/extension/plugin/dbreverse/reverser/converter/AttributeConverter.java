package com.change_vision.astah.extension.plugin.dbreverse.reverser.converter;

import java.util.ArrayList;
import java.util.List;

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

public class AttributeConverter {

    private ERModelEditor editor;
    private IERModel erModel;

    public AttributeConverter(ERModelEditor editor,IERModel erModel) {
        this.editor = editor;
        this.erModel = erModel;
    }

    public IERAttribute convert(IEREntity owner, AttributeInfo attributeInfo) throws InvalidEditingException {
        if(attributeInfo == null) throw new IllegalArgumentException("argument is null");
        if(owner == null) throw new IllegalArgumentException("owner is null");
        IERAttribute converted = convertByDomain(owner, attributeInfo);
        if(converted != null) return converted;
        return convertByDataType(owner, attributeInfo);
    }

    private IERAttribute convertByDataType(IEREntity owner, AttributeInfo attributeInfo)
            throws InvalidEditingException {
        DatatypeInfo dataTypeInfo = attributeInfo.getDataType();
        if (isDatatypeAttribute(dataTypeInfo)) {
            IERDatatype datatype = createERDatatype(dataTypeInfo);
            return editor.createERAttribute(owner, attributeInfo.getName(), attributeInfo.getName(), datatype);
        }
        return null;
    }

    private IERAttribute convertByDomain(IEREntity owner, AttributeInfo attributeInfo)
            throws InvalidEditingException {
        DomainInfo domainInfo = attributeInfo.getDomain();
        if (isDomainAttribute(domainInfo)) {
            IERDomain iDomain = createDomain(domainInfo);
            return editor.createERAttribute(owner, attributeInfo.getName(), attributeInfo.getName(), iDomain);
        }
        return null;
    }

    private boolean isDatatypeAttribute(DatatypeInfo dataTypeInfo) {
        return !"".equals(dataTypeInfo.getName());
    }

    private boolean isDomainAttribute(DomainInfo domainInfo) {
        return !"".equals(domainInfo.getName());
    }
    
    private IERDomain createDomain(DomainInfo dmInfo) throws InvalidEditingException {
        IERDomain iDomain = getDomain(dmInfo.getName());
        if (iDomain == null) {
            IERDatatype datatype = getDatatype(dmInfo.getDatatypeName());
            if (datatype == null) {
                datatype = editor.createERDatatype(erModel, dmInfo.getDatatypeName());
            }
            iDomain = editor.createERDomain(erModel, null, dmInfo.getName(), dmInfo.getName(), datatype);
        }
        // More api should be added in IERDomain for setting domain info
        return iDomain;
    }
    
    private IERDatatype createERDatatype(DatatypeInfo dtInfo) throws InvalidEditingException {
        IERDatatype datatype = getDatatype(dtInfo.getName());
        if (datatype == null) {
            datatype = editor.createERDatatype(erModel, dtInfo.getName());
            datatype.setLengthConstraint(dtInfo.getLengthConstraint());
            datatype.setPrecisionConstraint(dtInfo.getPrecisionConstraint());
        }
        String dLen = dtInfo.getDefaultLength();
        String dPre = dtInfo.getDefaultPrecision();
        StringBuilder lengthPrecision = new StringBuilder();
        lengthPrecision.append(dLen);
        lengthPrecision.append(("".equals(dLen) || "".equals(dPre)) ? "" : ",");
        lengthPrecision.append(dPre);
        if (lengthPrecision.length() > 0) {
            datatype.setDefaultLengthPrecision(lengthPrecision.toString());
        }
        datatype.setDescription(dtInfo.getDescription());

        return datatype;
    }

    private List<IERDomain> getAllChildDomains(IERDomain domain) {
        List<IERDomain> domains = new ArrayList<IERDomain>();
        domains.add(domain);
        IERDomain[] children = domain.getChildren();
        for (IERDomain child : children) {
            domains.addAll(getAllChildDomains(child));
        }
        return domains;
    }

    private IERDatatype getDatatype(String name) {
        IERDatatype[] datatypes = erModel.getSchemata()[0].getDatatypes();
        for (IERDatatype datatype : datatypes) {
            if (datatype.getName().equalsIgnoreCase(name)) {
                return datatype;
            }
        }
        return null;
    }
    
    private IERDomain getDomain(String name) {
        IERDomain defaultDomain = erModel.getSchemata()[0].getDomains()[0];
        for (IERDomain domain : getAllChildDomains(defaultDomain)) {
            if (domain.getName().equalsIgnoreCase(name)) {
                return domain;
            }
        }
        return null;
    }

}
