package com.change_vision.astah.extension.plugin.dbreverse.reverser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.change_vision.astah.extension.plugin.dbreverse.Messages;
import com.change_vision.astah.extension.plugin.dbreverse.internal.progress.LogProggressMonitor;
import com.change_vision.astah.extension.plugin.dbreverse.internal.progress.ProgressMonitor;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.converter.AttributeConverter;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.converter.DatatypeConverter;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.converter.DomainConverter;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.converter.TableConverter;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.finder.AttributeFinder;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.finder.DatatypeFinder;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.finder.DomainFinder;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.finder.EntityFinder;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.model.AttributeInfo;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.model.DatatypeInfo;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.model.DomainInfo;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.model.ERRelationshipInfo;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.model.IndexInfo;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.model.TableInfo;
import com.change_vision.jude.api.inf.editor.ERModelEditor;
import com.change_vision.jude.api.inf.editor.TransactionManager;
import com.change_vision.jude.api.inf.exception.BadTransactionException;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.model.IERAttribute;
import com.change_vision.jude.api.inf.model.IERDatatype;
import com.change_vision.jude.api.inf.model.IERDomain;
import com.change_vision.jude.api.inf.model.IEREntity;
import com.change_vision.jude.api.inf.model.IERIndex;
import com.change_vision.jude.api.inf.model.IERModel;
import com.change_vision.jude.api.inf.model.IERRelationship;
import com.change_vision.jude.api.inf.model.IERSchema;
import com.change_vision.jude.api.inf.model.IModel;

public class DBToProject {

    private static final Logger logger = LoggerFactory.getLogger(DBToProject.class);

	private ERModelEditor editor;

	private IERModel erModel;

	private Map<String, TableInfo> tableInfoMap;
	   
    private Map<String, AttributeInfo> fkInfo;

    private Map<IEREntity,List<IndexInfo>> entityIndexMap;
    
    private Map<IndexInfo,List<String>> indexAttributesMap;
    
    private Map<IERIndex,List<String>> erindexAttributesMap;

	private TableConverter tableConverter;

    private AttributeConverter attributeConverter;

    private DomainFinder domainFinder;

    private DomainConverter domainConverter;

    private DatatypeFinder datatypeFinder;

    private DatatypeConverter datatypeConverter;

    private AttributeFinder attributeFinder;

    private EntityFinder entityFinder;
    
    private ProgressMonitor monitor;
    
    DBToProject(ERModelEditor editor){
        this(editor,new LogProggressMonitor());
    }
    
    public DBToProject(ERModelEditor editor, ProgressMonitor monitor){
        this.editor = editor;
        this.monitor = monitor;
    }

    private void createTableInfoMap(List<TableInfo> tableInfos) {
        tableInfoMap = new HashMap<String, TableInfo>();
        for (TableInfo t: tableInfos) {
            tableInfoMap.put(t.getName(), t);
        }
    }
    
	public void importToProject(IModel project, List<TableInfo> tables, List<ERRelationshipInfo> relationships) throws InvalidEditingException {
	    createTableInfoMap(tables);
	    
		fkInfo = new HashMap<String, AttributeInfo>();
		entityIndexMap = new HashMap<IEREntity, List<IndexInfo>>();
		indexAttributesMap = new HashMap<IndexInfo, List<String>>();
		erindexAttributesMap = new HashMap<IERIndex, List<String>>();

		try {
			TransactionManager.beginTransaction();
	        erModel = editor.createERModel(project, "ER Model");
	        IERSchema schema = erModel.getSchemata()[0];
            entityFinder = new EntityFinder(schema);
	        tableConverter = new TableConverter(editor,schema);
            attributeFinder = new AttributeFinder();
            attributeConverter = new AttributeConverter(editor, erModel);
            domainFinder = new DomainFinder(schema);
            domainConverter = new DomainConverter(editor, erModel);
            datatypeFinder = new DatatypeFinder(schema);
            datatypeConverter = new DatatypeConverter(editor, erModel);
			importTable(tables);
			importRelationships(relationships);
            addMissedIndex(tables);
			showTableCount(tables.size());
			TransactionManager.endTransaction();
		} catch (BadTransactionException e) {
		    logger.error("BadTransactionException is occurred. Transaction is aborted.",e);
			TransactionManager.abortTransaction();
		}
	}

    private void addMissedIndex(List<TableInfo> tables) throws InvalidEditingException {
        for (TableInfo t : tables) {
            addMissedIndex(entityFinder.find(t.getName()));
        }
    }
    
	private void showTableCount(int count) {
	    String message = Messages.getMessage("message.import.table.count",count);
        monitor.showMessage(message);
	}

	private void importTable(List<TableInfo> tables) throws InvalidEditingException {
		for (TableInfo tInfo : tables) {
		    IEREntity entity = tableConverter.convert(tInfo);
	        addAttributes(tInfo, entity);
	        addIndexes(tInfo, entity);
			showImportingTable(tInfo.getName());
		}
	}

	public void showImportingTable(String name) {
        String message = Messages.getMessage("message.import.table.name",name);
        monitor.showMessage(message);
	}

	private void addIndexes(TableInfo tInfo, IEREntity entity)
			throws InvalidEditingException {
		List<IndexInfo> missedInfo = new ArrayList<IndexInfo>();
		for (IndexInfo indexInfo : tInfo.getIndexes()) {
			List<String> attributes = indexInfo.getAttributes();
            IERAttribute[] erAttributes =  getAttributes(entity, attributes);
			List<String> missedAttributes = getMissedAttributes(entity, attributes);
            if (erAttributes.length != 0) {
				IERIndex newIndex = editor.createERIndex(indexInfo.getName(), entity, indexInfo.isUnique(), true, erAttributes);
				erindexAttributesMap.put(newIndex, missedAttributes);
			} else {
			    indexAttributesMap.put(indexInfo, missedAttributes);
				missedInfo.add(indexInfo);
			}
		}
		if (!missedInfo.isEmpty()) {
			entityIndexMap.put(entity, missedInfo);
		}
	}

	private IERAttribute[] getAttributes(IEREntity entity, List<String> attrNames) {
		List<IERAttribute> attrs = new ArrayList<IERAttribute>();
		for (String attrName : attrNames) {
			IERAttribute erAttr = attributeFinder.find(entity, attrName);
			if(erAttr != null){
			    attrs.add(erAttr);
			}
		}

		return (IERAttribute[]) attrs.toArray(new IERAttribute[0]);
	}

	private List<String> getMissedAttributes(IEREntity entity, List<String> attrNames) {
        List<String> missed = new ArrayList<String>();
        for (String attrName : attrNames) {
            IERAttribute erAttr = attributeFinder.find(entity, attrName);
            if (erAttr == null) {
                missed.add(attrName);
            }
        }
        return missed;
    }

	
	private void addAttributes(TableInfo tInfo, IEREntity entity)
			throws InvalidEditingException {
		for (AttributeInfo aInfo : tInfo.getAttributes()) {
			if (aInfo.isFK()) {
				fkInfo.put(tInfo.getName(), aInfo);
				continue;
			}
			attributeConverter.convert(entity,aInfo);
		}
	}
	
	// Use to compare objects simply.
    // ERRelationshipInfo.equals() can't use for Collections.conains().
	private boolean containsERRelationshipInfo(ERRelationshipInfo rInfo, List<ERRelationshipInfo> relationships) {
	    for (ERRelationshipInfo e : relationships) {
	        if (e == rInfo) {
	            return true;
	        }
	    }
	    return false;
	}
	
	// Reorder to avoid that an exception occures while a relationship is created
	private List<ERRelationshipInfo> orderRelationships(List<ERRelationshipInfo> relationships) {
        List<ERRelationshipInfo> newRelationships = new ArrayList<ERRelationshipInfo>();
        List<ERRelationshipInfo> laterRelationships = new ArrayList<ERRelationshipInfo>();
        for (ERRelationshipInfo rInfo : relationships) {
            logger.debug("[ordering start]" + rInfo.getName());
            Map<String, String> keyMap = rInfo.getKeys();
            for (String key : keyMap.keySet()) {
                logger.debug("key - val: " + key + " - " + keyMap.get(key));
                if (key.equals(keyMap.get(key))) {
                    newRelationships.add(rInfo);
                    logger.debug("add to newlist : " + rInfo.getName());
                    break;
                }
            }
            if (!containsERRelationshipInfo(rInfo, newRelationships)) {
                laterRelationships.add(rInfo);
                logger.debug("add to laterlist : " + rInfo.getName());
            }
        }

        newRelationships.addAll(laterRelationships);
        return newRelationships;
	}
	
	private void importRelationships(List<ERRelationshipInfo> relationships) throws InvalidEditingException {
	    relationships = orderRelationships(relationships);
        logger.debug("[importRelationships start]");
        for (ERRelationshipInfo rInfo : relationships) {
            logger.debug(rInfo.getName());
        }
        logger.debug("[importRelationships end]");

		for (ERRelationshipInfo rInfo : relationships) {
			IEREntity parent = entityFinder.find(rInfo.getParentTable());
			IEREntity child = entityFinder.find(rInfo.getChildTable());
			if (parent == null){
			    logger.warn("can't convert relationship '{}'. The parent table '{}' is not found.",rInfo.getName(),rInfo.getParentTable());
                continue;
			}
			if (child == null) {
                logger.warn("can't convert relationship '{}'. The child table '{}' is not found.",rInfo.getName(),rInfo.getChildTable());
				continue;
			}
			createERRelationship(parent, child, rInfo);
		}
	}

	private void createERRelationship(IEREntity parent,
			IEREntity child, ERRelationshipInfo errInfo) throws InvalidEditingException {
		String relationshipName = errInfo.getName();
		IERRelationship relationship = null;
        debugCreateERRelationship(errInfo, parent, child);
		if (errInfo.isIdentifying()) {
			if (parent.equals(child)) {
			    String warnMessage = Messages.getMessage("message.import.relationship.error.message",relationshipName);
                monitor.showMessage(warnMessage);
                logger.warn(warnMessage);
                String helpMessage = Messages.getMessage("message.import.relationship.error.help_message",relationshipName);
                monitor.showMessage(helpMessage);
				logger.debug(helpMessage);
				return;
			} else {
				relationship = editor.createIdentifyingRelationship(parent, child, relationshipName, relationshipName);
                checkRelationship(parent, child, errInfo, relationship);
                renameForeignKey(relationship, parent, child, errInfo);
                //[56_16_improve_fk2]
				doSpecialForChangeForeignKeyName(child, relationship);
			}
		} else if (errInfo.isNonIdentifying()) {
			relationship = editor.createNonIdentifyingRelationship(parent, child, relationshipName, relationshipName);
            checkRelationship(parent, child, errInfo, relationship);
            renameForeignKey(relationship, parent, child, errInfo);
			//[56_16_improve_fk2]
			doSpecialForChangeForeignKeyName(child, relationship);
		} else if (errInfo.isMultiToMulti()) {
			relationship = editor.createMultiToMultiRelationship(parent, child, relationshipName, relationshipName);
		}

        if (relationship == null) {
            logger.debug("relationship is null.");
            return;
        }

        relationship.setERIndexToPrimarykey();

		if (!(errInfo.isIdentifying() && parent.equals(child))) {
			relationship.setVerbPhraseParent(errInfo.getVerbPhraseParent());
			relationship.setVerbPhraseChild(errInfo.getVerbPhraseChild());
			relationship.setParentRequired(errInfo.isParentRequired());
			relationship.setDefinition(errInfo.getDefinition());
		}
	}

	private void debugCreateERRelationship(ERRelationshipInfo errInfo,IEREntity parent, IEREntity child) {
	    logger.debug("name : " + errInfo.getName());
        logger.debug("isIdentifying : " + errInfo.isIdentifying());
		logger.debug("isNonIdentifying : " + errInfo.isNonIdentifying());

		// parent
		logger.debug("[P]Entity       :=" + parent.getName());
		IERAttribute[] primaryKeys = parent.getPrimaryKeys();
		if (primaryKeys.length > 0) {
			logger.debug("[P]Entity     PK:=" + primaryKeys[0].getName() + " : " + primaryKeys[0].getDatatype());
		}
		IERAttribute[] nonPrimaryKeys = parent.getNonPrimaryKeys();
		if (nonPrimaryKeys.length > 0) {
			logger.debug("[P]Entity Non-PK:=" + nonPrimaryKeys[0].getName() + " : " + nonPrimaryKeys[0].getDatatype());
		}

		// child
		logger.debug("[C]Entity       :=" + child.getName());
		primaryKeys = child.getPrimaryKeys();
		if (primaryKeys.length > 0) {
			logger.debug("[C]Entity     PK:=" + primaryKeys[0].getName() + " : " + primaryKeys[0].getDatatype());
		}
		nonPrimaryKeys = child.getNonPrimaryKeys();
		if (nonPrimaryKeys.length > 0) {
			logger.debug("[C]Entity Non-PK:=" + nonPrimaryKeys[0].getName() + " : " + nonPrimaryKeys[0].getDatatype());
		}
	}
	
	AttributeInfo getAttributeInfo(String tableName, String attributeName) {
	    TableInfo table = tableInfoMap.get(tableName);
	    if (table == null) {
	        return null;
	    }
	    return table.getAttributeInfo(attributeName);
	}

	//[56_16_improve_fk2]
	private void doSpecialForChangeForeignKeyName(IEREntity child,
			IERRelationship relationship) throws InvalidEditingException {
		IERAttribute[] foreignKeys = relationship.getForeignKeys();
		if (foreignKeys.length > 0) {
			logger.debug("Created Relationship:=" + relationship.getName());
		}
		for (int i = 0; i < foreignKeys.length; i++) {
			IERAttribute attribute = foreignKeys[i];
			logger.debug("Created attribute:=" + attribute.getName() + " ; " + attribute.getDatatype().getName());
			AttributeInfo attributeInfo = getAttributeInfo(child.getName(), attribute.getName());
			if (attributeInfo == null) {
				logger.warn("Ignored unexpected table:=" + child.getName());
				continue;
			}
			DomainInfo domainInfo = attributeInfo.getDomain();
			if(domainInfo != null) {
			    IERDomain domain = findOrConvertDomain(domainInfo);
                if(domain != null){
                    attribute.setDomain(domain);
                    logger.debug("Chaged attribute:=" + attribute.getName() + " ; " + attribute.getDomain().getName());
                    continue;
                }
            }
			DatatypeInfo datatypeInfo = attributeInfo.getDataType();
			if(datatypeInfo != null){
			    IERDatatype datatype = findOrConvertDatatype(datatypeInfo);
			    if(datatype != null){
			        attribute.setDatatype(datatype);
			        String defaultValue = attributeInfo.getDefaultValue();
			        attribute.setDefaultValue(defaultValue);
			        String lengthPrecision = attributeInfo.getLengthPrecision();
			        logger.debug("Set length: " + attribute.getName() + " ; " + lengthPrecision);
			        setLengthPrecision(attribute, lengthPrecision);
			    }
			}
		}
	}

    private void setLengthPrecision(IERAttribute attribute,
            String lengthPrecision) {
        try {
            attribute.setLengthPrecision(lengthPrecision);
        } catch (InvalidEditingException e) {
            if (InvalidEditingException.PARAMETER_ERROR_KEY.equals(e.getKey())) {
                logger.warn("Ignored invalid length/precision]:=" + attribute.getName());
            }
        }

    }

    private IERDatatype findOrConvertDatatype(DatatypeInfo datatypeInfo)
            throws InvalidEditingException {
        IERDatatype datatype = null;
        String datatypeName = datatypeInfo.getName();
        if(!"".equals(datatypeName)){
            datatype = datatypeFinder.find(datatypeName);
            if(datatype == null) {
                datatype = datatypeConverter.convert(datatypeInfo);
            }
        }
        return datatype;
    }

    private IERDomain findOrConvertDomain(DomainInfo domainInfo) throws InvalidEditingException {
        IERDomain domain = null;
        String domainName = domainInfo.getName();
        if (!"".equals(domainName)){
            domain = domainFinder.find(domainName);
            if(domain == null){
                domain = domainConverter.convert(domainInfo);
            }
        }
        return domain;
    }

    private void checkRelationship(IEREntity parent, IEREntity child,
            ERRelationshipInfo errInfo, IERRelationship relationship)
            throws InvalidEditingException {
        IERIndex index = getKindOfRelationship(parent, errInfo);
        if (index != null) {
            addMissedAttributeForIndex(parent);
            relationship.setERIndex(index);
        }
        addMissedAttributeForIndex(child);
    }

	private void addMissedAttributeForIndex(IEREntity entity) throws InvalidEditingException {
		addMissedAttributes(entity);
		addMissedIndex(entity);
	}

	private void addMissedIndex(IEREntity entity)
			throws InvalidEditingException {
		List<IndexInfo> indexInfoes = entityIndexMap.get(entity);
		if (indexInfoes != null) {
			List<IndexInfo> missedInfo = new ArrayList<IndexInfo>();
			for (IndexInfo indexInfo : indexInfoes) {
				List<String> attributes = indexAttributesMap.get(indexInfo);
				IERAttribute[] erAttrs = getAttributes(entity, attributes);
				List<String> missedAttributes = getMissedAttributes(entity, attributes);
				if (erAttrs.length != 0) {
					IERIndex newIndex = editor.createERIndex(indexInfo.getName(), entity, indexInfo.isUnique(), true, erAttrs);
					indexAttributesMap.remove(indexInfo);
					if (erAttrs.length != attributes.size()) {
					    erindexAttributesMap.put(newIndex, missedAttributes);
					}
				} else {
					indexAttributesMap.put(indexInfo, missedAttributes);
					missedInfo.add(indexInfo);
				}
			}
			entityIndexMap.remove(entity);
			if (!missedInfo.isEmpty()) {
				entityIndexMap.put(entity, missedInfo);
			}
		}
	}

	private void addMissedAttributes(IEREntity entity)
			throws InvalidEditingException {
		IERIndex[] erIndices = entity.getERIndices();
        for (IERIndex index : erIndices) {
			List<String> attributes = erindexAttributesMap.get(index);
			if (attributes == null || attributes.isEmpty()) {
				continue;
			}
			IERAttribute[] erAttrs = getAttributes(entity, attributes);
			for (int j = 0; j < erAttrs.length; j++) {
				index.addERAttribute(erAttrs[j]);
			}
			if (erAttrs.length != attributes.size()) {
			    erindexAttributesMap.put(index, indexAttributesMap.get(attributes));
			    indexAttributesMap.remove(attributes);
			} else {
			    erindexAttributesMap.remove(index);
			}
		}
	}

	private IERIndex getKindOfRelationship(IEREntity parent, ERRelationshipInfo errInfo) {
		for (IERIndex index : parent.getERIndices()) {
			List<String> missedAttrs = erindexAttributesMap.get(index);
			List<IERAttribute> pks = new ArrayList<IERAttribute>();
			for (String pkName : errInfo.getKeys().keySet()) {
				if (missedAttrs != null && missedAttrs.contains(pkName)) {
					continue;
				}
				IERAttribute pk = attributeFinder.find(parent, pkName);
				pks.add(pk);
			}
			if (index.isUnique() && Arrays.asList(index.getERAttributes()).containsAll(pks)) {
				return index;
			}
		}
		return null;
	}

	private void renameForeignKey(IERRelationship relationship,
			IEREntity parent, IEREntity child,
			ERRelationshipInfo errInfo) throws InvalidEditingException {
		Map<String, String> keys = errInfo.getKeys();
        Set<String> keySet = keys.keySet();
        for (String parentKeyName : keySet) {
			IERAttribute parentKey = attributeFinder.find(parent, parentKeyName);
			if (parentKey == null){
			    continue;
			}
			
			IERAttribute fk = getReferenceFK(relationship, parentKey);
			if (fk == null) {
				continue;
			}
			
			AttributeInfo fkAttributeInfo = getFKAttributeInfo(parent.getName());
			boolean isNotNull = false;
			if(fkAttributeInfo != null){
			    isNotNull = fkAttributeInfo.isNotNull();
			}
			fk.setNotNull(isNotNull);

            String childKeyName = keys.get(parentKeyName);
			IERAttribute childKey = attributeFinder.find(child, childKeyName);
			if (childKey != null && fk != childKey) {
				relationship.setForeignKey(parentKey, childKey);
				continue;
			}
			if (!fk.getName().equals(childKeyName)) {
				fk.setLogicalName(childKeyName);
				fk.setPhysicalName(childKeyName);
			}
		}
	}

	private IERAttribute getReferenceFK(IERRelationship relationship, IERAttribute pk) {
		for (IERAttribute fk : pk.getReferencedForeignKeys()) {
			if (fk.getReferencedRelationship() == relationship) {
				return fk;
			}
		}
		return null;
	}

	private AttributeInfo getFKAttributeInfo(String tableName) {
		if (fkInfo.keySet().contains(tableName)) {
			return fkInfo.get(tableName);
		}
		return null;
	}

}