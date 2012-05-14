package calculator;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class SchoolDialog extends GUIPanel {

	private static final long serialVersionUID = -873283817151884443L;
	private JPanel navigationPanel, newSchoolPanel;
	private JTextField newSchoolField;
	private JRadioButton plusMinusRadio, normalRadio;

	public SchoolDialog(SystemController controller) {
		super(controller);
		addComponentsToPane();
	}

	private void addComponentsToPane() {

		// New school panel.
		newSchoolPanel = new JPanel(new GridLayout(2, 2));
		newSchoolPanel.add(new JLabel("Name of new school: "));
		newSchoolField = new JTextField(5);
		newSchoolPanel.add(newSchoolField);
		// Radio buttons.
		ButtonGroup addSchoolBtns = new ButtonGroup();
		plusMinusRadio = new JRadioButton("Plus/Minus", true);
		normalRadio = new JRadioButton("Normal");
		addSchoolBtns.add(plusMinusRadio);
		addSchoolBtns.add(normalRadio);
		newSchoolPanel.add(plusMinusRadio);
		newSchoolPanel.add(normalRadio);

		// Navigation panel.
		navigationPanel = new JPanel(new GridLayout(3, 1));
		navigationPanel.add(createButton("add", "Add"));
		navigationPanel.add(createButton("cancel", "Cancel"));

		// Layout.
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		// add(instructionPanel);
		add(newSchoolPanel);
		add(navigationPanel);
	}

	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		if (action.equals("cancel")) {
			controller.rootFrame.showPanel("SchoolPanel", this);
		} else if (action.equals("add")) {
			String newSchoolName = newSchoolField.getText();
			if (newSchoolName == null
					|| !controller.addSchool(newSchoolName, new GradingScale(
							plusMinusRadio.isSelected()))) {
				JOptionPane.showMessageDialog(this,
						"That school already exists or no name was entered.",
						"Error", JOptionPane.ERROR_MESSAGE);
			} else {
				controller.rootFrame
						.addPanel(new SchoolPanel(controller), this);
			}
		}
	}
}