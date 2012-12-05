package com.change_vision.astah.extension.plugin.dbreverse.reverser.finder;

import java.util.ArrayList;
import java.util.List;

import com.change_vision.jude.api.inf.model.IERDomain;
import com.change_vision.jude.api.inf.model.IERSchema;

public class DomainFinder {

    private IERSchema schema;

    public DomainFinder(IERSchema schema) {
        this.schema = schema;
    }

    public IERDomain find(String name) {
        if(name == null) throw new IllegalArgumentException("name is null.");
        IERDomain defaultDomain = schema.getDomains()[0];
        for (IERDomain domain : getAllChildDomains(defaultDomain)) {
            if (domain.getName().equalsIgnoreCase(name)) {
                return domain;
            }
        }
        return null;
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

}
