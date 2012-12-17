package com.change_vision.astah.extension.plugin.dbreverse.view;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.change_vision.astah.extension.plugin.dbreverse.Messages;
import com.change_vision.astah.extension.plugin.dbreverse.internal.progress.MessageAreaNotifier;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.DBReader;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.ImportToProject;
import com.change_vision.astah.extension.plugin.dbreverse.util.DBReverseUtil;

public class ImportButton extends JButton implements ActionListener {

    private static final long serialVersionUID = -8333380826691409178L;

    private static final String NAME = "button.import";

    private static ImportButton instance = null;

    private MessageAreaNotifier monitor;

    public ImportButton() {
        setName(NAME);
        setText(Messages.getMessage("button.text.import"));
        setEnabled(false);

        addActionListener(this);
        monitor = new MessageAreaNotifier();
    }

    public static ImportButton getInstance() {
        if (instance == null) {
            instance = new ImportButton();
        }

        return instance;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SchemaComboBox.getInstance().setEnabled(false);
        DBReader reader = DBReader.getInstance();
        if (reader == null) {
            monitor.showMessage(Messages.getMessage("message.database.disconnected"));
            setEnabled(false);
            return;
        }
        String schema = getSchema();
        String currentDBType = getDBType();
        
        ImportToProject importer = new ImportToProject(reader,monitor);
        Cursor currentCursor = getCursor();
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        try {
            boolean imported = importer.doImport(currentDBType,schema);
            if (imported) {
                DBReverseUtil.disconnectDB();
                monitor.showMessage(Messages.getMessage("message.import.successfully"));
                ReverseDialog dialog = ReverseDialog.getInstance();
                DBReverseUtil.showInformationDialog(dialog.getParent(), Messages.getMessage("message.process.finished"));
                setEnabled(false);
            }
        } finally {
            setCursor(currentCursor);
        }

    }

    private String getDBType() {
        Object item = DBComboBox.getInstance().getSelectedItem();
        if(item == null) return null;
        return item.toString();
    }

    private String getSchema() {
        Object item = SchemaComboBox.getInstance().getSelectedItem();
        if (item == null) return null;
        return item.toString();
    }
}