package com.change_vision.astah.extension.plugin.dbreverse.reverser.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.change_vision.astah.extension.plugin.dbreverse.reverser.model.TableInfo;
import com.change_vision.jude.api.inf.editor.ERModelEditor;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.model.IEREntity;
import com.change_vision.jude.api.inf.model.IERSchema;

public class TableConverter {
    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(TableConverter.class);

    private ERModelEditor editor;
    private IERSchema schema;
    
    public TableConverter(ERModelEditor editor, IERSchema schema) {
        this.editor = editor;
        this.schema = schema;
    }

    public IEREntity convert(TableInfo tableInfo) {
        IEREntity entity = null;
//        TransactionManager.beginTransaction();
        try {
            String name = tableInfo.getName();
            entity = editor.createEREntity(schema, name, name);
            entity.setType(tableInfo.getType());
            entity.setDefinition(tableInfo.getDefinition());
//            TransactionManager.endTransaction();
        } catch (InvalidEditingException e) {
            logger.error("invalid editing exception is occured.",e);
//            TransactionManager.abortTransaction();
        }
//        addAttributes(tableInfo, entity);
//        addIndexes(tableInfo, entity);
        return entity;
    }

}
