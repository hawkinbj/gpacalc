package com.hawkinbj.gpacalc.view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.hawkinbj.gpacalc.controller.SystemController;
import com.hawkinbj.gpacalc.model.GUIPanel;

public class FinalGradePanel extends GUIPanel {
	private static final long serialVersionUID = -438674586864467004L;

	private JComboBox<String> letterGradeComboBox;

	private String[] letterGrades;

	private JPanel setGradePanel;

	private JPanel navPanel;

	public FinalGradePanel(SystemController controller) {
		super(controller);
		this.addComponentsToPane();
	}

	private void addComponentsToPane() {
		setGradePanel = new JPanel(new GridLayout(2, 2));
		setGradePanel.add(new JLabel("Set final letter grade: "));

		Set<String> gradeMapKeys = controller.getActiveSchool()
				.getGradingScale().getGradingScaleMap().keySet();

		letterGrades = gradeMapKeys.toArray(new String[gradeMapKeys.size()]);

		Arrays.sort(letterGrades);

		letterGradeComboBox = new JComboBox<String>(letterGrades);
		letterGradeComboBox.setSelectedItem(controller.getActiveCourse()
				.getFinalGrade());

		setGradePanel.add(this.letterGradeComboBox);
		setGradePanel.add(new JLabel("Clear final grade: "));
		setGradePanel.add(createButton("clearFinalGrade", "Clear"));

		this.createTitledBorder(setGradePanel, controller.getActiveCourse()
				.getCourseName());

		navPanel = new JPanel(new GridLayout(2, 1));
		navPanel.add(createButton("done", "Done"));
		navPanel.add(createButton("cancel", "Cancel"));

		this.createTitledBorder(navPanel, "Navigation");

		this.setLayout(new BoxLayout(this, 3));
		this.add(setGradePanel);
		this.add(navPanel);
	}

	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();

		if (action.equals("clearFinalGrade")) {
			this.letterGradeComboBox.setSelectedIndex(-1);
		} else if (action.equals("done")) {
			controller.getActiveCourse().setFinalGrade(
					(String) this.letterGradeComboBox.getSelectedItem());
			controller.saveUserList();
			controller.getRootFrame().addPanel(new CourseInfoPanel(controller),
					this);
		} else if (action.equals("cancel")) {
			controller.getRootFrame().showPanel("CourseInfoPanel", this);
		}
	}
}