package com.change_vision.astah.extension.plugin.dbreverse.reverser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.change_vision.astah.extension.plugin.dbreverse.reverser.converter.AttributeConverter;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.converter.TableConverter;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.model.AttributeInfo;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.model.DatatypeInfo;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.model.DomainInfo;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.model.ERRelationshipInfo;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.model.IndexInfo;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.model.RelationshipInfo;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.model.SubtypeRelationshipInfo;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.model.TableInfo;
import com.change_vision.jude.api.inf.editor.ERModelEditor;
import com.change_vision.jude.api.inf.editor.ModelEditorFactory;
import com.change_vision.jude.api.inf.editor.TransactionManager;
import com.change_vision.jude.api.inf.exception.BadTransactionException;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.exception.LicenseNotFoundException;
import com.change_vision.jude.api.inf.exception.ProjectLockedException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IERAttribute;
import com.change_vision.jude.api.inf.model.IERDatatype;
import com.change_vision.jude.api.inf.model.IERDomain;
import com.change_vision.jude.api.inf.model.IEREntity;
import com.change_vision.jude.api.inf.model.IERIndex;
import com.change_vision.jude.api.inf.model.IERModel;
import com.change_vision.jude.api.inf.model.IERRelationship;
import com.change_vision.jude.api.inf.model.IERSchema;
import com.change_vision.jude.api.inf.model.IERSubtypeRelationship;
import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.project.ProjectAccessor;
import com.change_vision.jude.api.inf.project.ProjectAccessorFactory;

public class DBToProject {

	private static final Logger logger = LoggerFactory.getLogger(DBToProject.class);

	private ERModelEditor editor;

	private IERModel erModel;

	private Map<String, AttributeInfo> fkInfo;

	private Map indexMap;
	
	private TableConverter tableConverter;

    private AttributeConverter attributeConverter;

	public void importToProject(String projectFilePath, List<TableInfo> tables, List<ERRelationshipInfo> relationships) throws LicenseNotFoundException,
			ProjectLockedException, InvalidEditingException, ClassNotFoundException, IOException, ProjectNotFoundException {
		ProjectAccessor prjAccessor = ProjectAccessorFactory.getProjectAccessor();
		prjAccessor.create(projectFilePath);
		IModel project = prjAccessor.getProject();
		if (project == null) {
			return;
		}
		
		editor = ModelEditorFactory.getERModelEditor();
		fkInfo = new HashMap<String, AttributeInfo>();
		indexMap = new HashMap();

		try {
			TransactionManager.beginTransaction();
	        erModel = editor.createERModel(project, "ER Model");
	        tableConverter = new TableConverter(editor,erModel.getSchemata()[0]);
            attributeConverter = new AttributeConverter(editor, erModel);
			importTable(tables);
			importRelationships(relationships);
			showTableCount(tables.size());
			TransactionManager.endTransaction();
		} catch (BadTransactionException e) {
			TransactionManager.abortTransaction();
		}
		prjAccessor.save();
		prjAccessor.close();
	}

	public void showTableCount(int count) {
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
	}

	private void addIndexes(TableInfo tInfo, IEREntity entity)
			throws InvalidEditingException {
		List<IndexInfo> missedInfo = new ArrayList<IndexInfo>();
		for (IndexInfo indexInfo : tInfo.getIndexes()) {
			IERAttribute[] erAttributes =  getAttributes(entity, indexInfo.getAttributes());
			if (erAttributes.length != 0) {
				IERIndex newIndex = editor.createERIndex(indexInfo.getName(), entity, indexInfo.isUnique(), true, erAttributes);
				indexMap.put(newIndex, indexMap.get(indexInfo.getAttributes()));
			} else {
				indexMap.put(indexInfo, indexMap.get(indexInfo.getAttributes()));
				missedInfo.add(indexInfo);
			}
			indexMap.remove(indexInfo.getAttributes());
		}
		if (!missedInfo.isEmpty()) {
			indexMap.put(entity, missedInfo);
		}
	}

	private IERAttribute[] getAttributes(IEREntity entity, List<String> attrNames) {
		List<IERAttribute> attrs = new ArrayList<IERAttribute>();
		List<String> missed = new ArrayList<String>();
		for (String attrName : attrNames) {
			IERAttribute erAttr = getAttribute(entity, attrName);
			if (erAttr == null) {
				missed.add(attrName);
			} else {
				attrs.add(erAttr);
			}
		}
		if (!missed.isEmpty()) {
			indexMap.put(attrNames, missed);
		}

		return (IERAttribute[]) attrs.toArray(new IERAttribute[0]);
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

	private IERDomain getDomain(String name) {
		IERDomain defaultDomain = erModel.getSchemata()[0].getDomains()[0];
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
		int size = domain.getChildren().length;
		for (int i = 0; i < size; i++) {
			domains.addAll(getAllChildDomains(domain.getChildren()[i]));
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

	private void importRelationships(List<ERRelationshipInfo> relationships) throws InvalidEditingException {
		RelationshipInfo rInfo = null;
		for (Iterator<ERRelationshipInfo> it = relationships.iterator(); it.hasNext();) {
			rInfo = it.next();
			IEREntity parent = getEntity(rInfo.getParentTable());
			IEREntity child = getEntity(rInfo.getChildTable());
			if (parent == null || child == null) {
				continue;
			}
			if (rInfo instanceof ERRelationshipInfo) {
				createERRelationship(parent, child, (ERRelationshipInfo) rInfo);
			} else if (rInfo instanceof SubtypeRelationshipInfo) {
				createSubtypeRelationship(parent, child, (SubtypeRelationshipInfo) rInfo);
			}
		}
	}

	private void createSubtypeRelationship(IEREntity parent,
			IEREntity child, SubtypeRelationshipInfo subInfo) throws InvalidEditingException {
		String name = subInfo.getName();
		IERSubtypeRelationship subRelationship =
			editor.createSubtypeRelationship(parent, child, name, name);
		subRelationship.setConclusive(subInfo.isConclusive());
		subRelationship.setDiscriminatorAttribute(
				getAttribute(parent, subInfo.getDiscriminatorAttribute()));
		subRelationship.setDefinition(subInfo.getDefinition());
	}

	private void createERRelationship(IEREntity parent,
			IEREntity child, ERRelationshipInfo errInfo) throws InvalidEditingException {
		String relationshipName = errInfo.getName();
		IERRelationship relationship = null;
		if (errInfo.isIdentifying()) {
			debugCreateERRelationship(errInfo, parent, child);
			if (parent.equals(child)) {
				logger.warn(relationshipName.concat(" のリレーションシップは追加しませんでした。"));
				logger.debug("----- astah*では、リレーションシップの親子が同一で一意制約のあるカラムはリレーションシップを追加できません。処理を省きます。 ----");
			} else {
				relationship = editor.createIdentifyingRelationship(parent, child, relationshipName, relationshipName);
				checkRelationship(parent, child, errInfo, relationship);
				renameForeignKey(relationship, parent, child, errInfo);
				//[56_16_improve_fk2]
				doSpecialForChangeForeignKeyName(child, relationship);
			}
		} else if (errInfo.isNonIdentifying()) {
			debugCreateERRelationship(errInfo, parent, child);
			relationship = editor.createNonIdentifyingRelationship(parent, child, relationshipName, relationshipName);
			checkRelationship(parent, child, errInfo, relationship);
			renameForeignKey(relationship, parent, child, errInfo);
			//[56_16_improve_fk2]
			doSpecialForChangeForeignKeyName(child, relationship);
		} else if (errInfo.isMultiToMulti()) {
			relationship = editor.createMultiToMultiRelationship(parent, child, relationshipName, relationshipName);
		}

		if (!(errInfo.isIdentifying() && parent.equals(child))) {
			relationship.setVerbPhraseParent(errInfo.getVerbPhraseParent());
			relationship.setVerbPhraseChild(errInfo.getVerbPhraseChild());
			relationship.setParentRequired(errInfo.isParentRequired());
			relationship.setDefinition(errInfo.getDefinition());
		}
	}

	private void debugCreateERRelationship(ERRelationshipInfo errInfo,IEREntity parent, IEREntity child) {
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
			DomainInfo dmInfo = getFKDomainInfo(child.getName());
			DatatypeInfo dtInfo = getFKDatatypeInfo(child.getName());
			AttributeInfo aInfo = getFKAttributeInfo(child.getName());
			if (dmInfo != null && !"".equals(dmInfo.getName())) {
				IERDomain iDomain = createDomain(dmInfo);
				attribute.setDomain(iDomain);
			} else if (dtInfo != null && !"".equals(dtInfo.getName())) {
				IERDatatype datatype = createERDatatype(dtInfo);
				attribute.setDatatype(datatype);
				attribute.setDefaultValue(aInfo.getDefaultValue());
				String aLen = aInfo.getLength();
				String aPre = aInfo.getPrecision();
				StringBuilder lengthPrecision = new StringBuilder();
				lengthPrecision.append(aLen);
				lengthPrecision.append(("".equals(aLen) || "".equals(aPre)) ? "" : ",");
				lengthPrecision.append(aPre);
				attribute.setLengthPrecision(lengthPrecision.toString());
				logger.debug("Chaged attribute:=" + attribute.getName() + " ; " + attribute.getDatatype().getName());
			}
		}
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
		List indexInfoes = (List)indexMap.get(entity);
		if (indexInfoes != null) {
			List missedInfo = new ArrayList();
			for (Iterator it = indexInfoes.iterator(); it.hasNext();) {
				IndexInfo indexInfo = (IndexInfo) it.next();
				List attributes = (List)indexMap.get(indexInfo);
				IERAttribute[] erAttrs = getAttributes(entity, attributes);
				if (erAttrs.length != 0) {
					IERIndex newIndex = editor.createERIndex(indexInfo.getName(), entity, indexInfo.isUnique(), true, erAttrs);
					indexMap.remove(indexInfo);
					if (erAttrs.length != attributes.size()) {
						indexMap.put(newIndex, indexMap.get(attributes));
					}
				} else {
					indexMap.put(indexInfo, indexMap.get(attributes));
					missedInfo.add(indexInfo);
				}
				indexMap.remove(attributes);
			}
			indexMap.remove(entity);
			if (!missedInfo.isEmpty()) {
				indexMap.put(entity, missedInfo);
			}
		}
	}

	private void addMissedAttributes(IEREntity entity)
			throws InvalidEditingException {
		for (int i = 0; i < entity.getERIndices().length; i++) {
			IERIndex index = entity.getERIndices()[i];
			List attributes = (List)indexMap.get(index);
			if (attributes == null || attributes.isEmpty()) {
				continue;
			}
			IERAttribute[] erAttrs = getAttributes(entity, attributes);
			for (int j = 0; j < erAttrs.length; j++) {
				index.addERAttribute(erAttrs[j]);
			}
			if (erAttrs.length != attributes.size()) {
				indexMap.put(index, indexMap.get(attributes));
				indexMap.remove(attributes);
			} else {
				indexMap.remove(index);
			}
		}
	}

	private IERIndex getKindOfRelationship(IEREntity parent, ERRelationshipInfo errInfo) {
		for (IERIndex index : parent.getERIndices()) {
			List missedAttrs = (List)indexMap.get(index);
			List<IERAttribute> pks = new ArrayList<IERAttribute>();
			for (String pkName : errInfo.getKeys().keySet()) {
				if (missedAttrs != null && missedAttrs.contains(pkName)) {
					continue;
				}
				IERAttribute pk = getAttribute(parent, pkName);
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
		for (String parentKeyName : errInfo.getKeys().keySet()) {
			String childKeyName = errInfo.getKeys().get(parentKeyName);
			IERAttribute parentKey = getAttribute(parent, parentKeyName);
			IERAttribute childKey = getAttribute(child, childKeyName);
			if (parentKey != null) {
				IERAttribute fk = getReferenceFK(relationship, parentKey);
				if (fk == null) {
					continue;
				}
				boolean isNotNull = isFKNotNull(parent.getName(), childKeyName);
				fk.setNotNull(isNotNull);
				if (childKey != null && fk != childKey) {
					relationship.setForeignKey(parentKey, childKey);
				} else {
					if (!fk.getName().equals(childKeyName)) {
						fk.setLogicalName(childKeyName);
						fk.setPhysicalName(childKeyName);
					}
				}
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

	private IERAttribute getAttribute(IEREntity entity, String name) {
		List<IERAttribute> attributes = new ArrayList<IERAttribute>();
		attributes.addAll(Arrays.asList(entity.getPrimaryKeys()));
		attributes.addAll(Arrays.asList(entity.getNonPrimaryKeys()));
		for (IERAttribute erAttribute : attributes) {
			if (erAttribute.getName().equals(name)) {
				return erAttribute;
			}
		}
		return null;
	}

	private IEREntity getEntity(String name) {
		IERSchema defaultSchema = erModel.getSchemata()[0];
		for (IEREntity erEntity : defaultSchema.getEntities()) {
			if (erEntity.getName().equals(name)) {
				return erEntity;
			}
		}
		return null;
	}

	private boolean isFKNotNull(String tableName, String fkName) {
		if (fkInfo.keySet().contains(tableName)) {
			return fkInfo.get(tableName).isNotNull();
		}
		return false;
	}

	private DomainInfo getFKDomainInfo(String tableName) {
		if (fkInfo.keySet().contains(tableName)) {
			return fkInfo.get(tableName).getDomain();
		}
		return null;
	}

	private DatatypeInfo getFKDatatypeInfo(String tableName) {
		if (fkInfo.keySet().contains(tableName)) {
			return fkInfo.get(tableName).getDataType();
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