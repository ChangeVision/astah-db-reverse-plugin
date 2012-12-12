package com.change_vision.astah.extension.plugin.dbreverse.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.change_vision.astah.extension.plugin.dbreverse.Messages;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.DBProperties;
import com.change_vision.astah.extension.plugin.dbreverse.util.DBReverseUtil;
import com.change_vision.astah.extension.plugin.dbreverse.util.ReversePreferences;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.project.ProjectAccessor;
import com.change_vision.jude.api.inf.project.ProjectAccessorFactory;
import com.change_vision.jude.api.inf.project.ProjectEvent;
import com.change_vision.jude.api.inf.project.ProjectEventListener;

public class ReverseDialog extends JDialog implements ProjectEventListener {

	private static final long serialVersionUID = 5185132606559602059L;

	private static final Logger logger = LoggerFactory.getLogger(ReverseDialog.class);

	private static ReverseDialog instance = null;

	public String temporaryProjectFilePath = null;

	private URLComboBox urlCombo = null;

	private UserTextField userField = null;

	private PasswordField pswField = null;

	private JDBCDriverURLTextField driverField = null;

	private JDBCDriverPathTextField jdbcField = null;

	private MessageTextArea messageArea = null;

	private SelectDriverButton selectDriverBtn = null;

	private ConnectButton connectBtn = null;

	private ImportButton importBtn = null;

	private CloseButton closeBtn = null;

	private ClearButton clearBtn = null;

	private DBComboBox dbCombo = null;

	private SchemaComboBox schemaCombo = null;

	public ReverseDialog(JFrame frame) {
		super(frame, true);

		ReversePreferences.getInstace(this.getClass());
		DBProperties.getInstance();

		Container contentPane = getContentPane();
		setTitle(Messages.getMessage("dialog.title"));

		JPanel connectPanel = createConnectPanel();
		contentPane.add(connectPanel, BorderLayout.NORTH);

		JPanel importPanel = createImportPanel();
		contentPane.add(importPanel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new BorderLayout());
		buttonPanel.add(new ButtonPanelLeft(), BorderLayout.WEST);
		buttonPanel.add(createButtonPanel(), BorderLayout.EAST);
		contentPane.add(buttonPanel, BorderLayout.SOUTH);

		setSize(600, 440);
		setLocation();
		setResizable(false);
		DBReverseUtil.showMessage(Messages.getMessage("message.console.initialize"));
	}

	public void setTemporaryProjectFilePath(String temporaryProjectFilePath) {
		this.temporaryProjectFilePath = temporaryProjectFilePath;
	}

	private void setLocation() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		setLocation((screenSize.width-frameSize.width) / 2, (screenSize.height-frameSize.height) / 2);
	}

	private JPanel createConnectPanel() {
		CommonGridBagLayout layout = new CommonGridBagLayout();
		JPanel connectPanel = new JPanel(layout);

		// DB
		dbCombo = DBComboBox.getInstance();
		addLine(connectPanel, layout, dbCombo, Messages.getMessage("panel.label.connect"));

		// URL
		urlCombo = URLComboBox.getInstance();
		addLine(connectPanel, layout, urlCombo, Messages.getMessage("panel.label.url"));

		// User
		userField = UserTextField.getInstance();
		addLine(connectPanel, layout, userField, Messages.getMessage("panel.label.user"));

		// Password
		pswField = PasswordField.getInstance();
		addLine(connectPanel, layout, pswField, Messages.getMessage("panel.label.password"));

		// Driver
		driverField = JDBCDriverURLTextField.getInstance();
		addLine(connectPanel, layout, driverField, Messages.getMessage("panel.label.jdbcdriver"));

		// Path of Driver (jar)
		jdbcField = JDBCDriverPathTextField.getInstance();
		selectDriverBtn = SelectDriverButton.getInstance();
		addLineWithButton(connectPanel, layout, jdbcField, Messages.getMessage("panel.label.driverpath"), selectDriverBtn);

		// Connect
		schemaCombo = SchemaComboBox.getInstance();
		addLine(connectPanel, layout, schemaCombo, Messages.getMessage("panel.label.schemas"));

		// Clear
		clearBtn = ClearButton.getInstance();
		addLineWithButton(connectPanel, layout, new JLabel(), Messages.getMessage("panel.label.console"), clearBtn);

		return connectPanel;
	}

	private JPanel createImportPanel() {
		JPanel importContent = new JPanel(new BorderLayout());

		// Message Area
		messageArea = MessageTextArea.getInstance();
		JScrollPane scroll = new JScrollPane(messageArea);
		importContent.add(scroll, BorderLayout.CENTER);

		return importContent;
	}

	private JPanel createButtonPanel() {
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		// Connect
		connectBtn = ConnectButton.getInstance();
		buttonPanel.add(connectBtn);

		// Import
		importBtn = ImportButton.getInstance();
		buttonPanel.add(importBtn);

		// Close
		closeBtn = CloseButton.getInstance();
		buttonPanel.add(closeBtn);

		return buttonPanel;
	}

	private void addLine(JPanel parentPanel, CommonGridBagLayout layout,
			JComponent component, String label) {
		JLabel jLabel = new JLabel("");
		layout.setLayout(jLabel, 1, 1, false);
		parentPanel.add(jLabel);

		JLabel url = new JLabel(label);
		layout.setLayout(url, 1, 1, false);
		parentPanel.add(url);

		layout.setLayout(component, 1, 6, true);
		parentPanel.add(component);
	}

	private void addLineWithButton(JPanel parentPanel, CommonGridBagLayout layout,
			JComponent component, String label, JButton button) {
		JLabel jLabel = new JLabel("");
		layout.setLayout(jLabel, 1, 1, false);
		parentPanel.add(jLabel);

		JLabel jdbc = new JLabel(label);
		layout.setLayout(jdbc, 1, 1, false);
		parentPanel.add(jdbc);

		layout.setLayout(component, 1, 30, false);
		parentPanel.add(component);

		layout.setLayout(button, 1, 1, false);
		parentPanel.add(button);

		jLabel = new JLabel("");
		layout.setLayout(jLabel, 1, 1, true);
		parentPanel.add(jLabel);
	}

	public static ReverseDialog getInstance(JFrame frame) {
		if (instance == null) {
			instance = new ReverseDialog(frame);
		}

		return instance;
	}

	public void showView() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	@Override
	public void projectChanged(ProjectEvent paramProjectEvent) {
	}

	@Override
	public void projectClosed(ProjectEvent paramProjectEvent) {
	}

	@Override
	public void projectOpened(ProjectEvent paramProjectEvent) {
		ProjectAccessor prjAccessor = null;
		try {
			prjAccessor = ProjectAccessorFactory.getProjectAccessor();
			if (temporaryProjectFilePath != null) {
				prjAccessor.easyMerge(temporaryProjectFilePath, true);
			}
			temporaryProjectFilePath = null;
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage(), e);
		} catch (ProjectNotFoundException e) {
			logger.error(e.getMessage(), e);
		} catch (InvalidEditingException e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (prjAccessor != null) {
				prjAccessor.removeProjectEventListener(this);
			}
		}

		DBReverseUtil.disconnectDB();
		DBReverseUtil.showInformationDialog(getParent(), Messages.getMessage("message.process.finished"));
	}

	public static ReverseDialog getInstance() {
		return instance;
	}
}