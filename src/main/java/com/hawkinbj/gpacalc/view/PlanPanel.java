/*******************************************************************************
 * Copyright (c) 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.hawkinbj.gpacalc.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.hawkinbj.gpacalc.controller.SystemController;
import com.hawkinbj.gpacalc.model.Course;
import com.hawkinbj.gpacalc.model.GUIPanel;
import com.hawkinbj.gpacalc.model.Semester;

public class PlanPanel extends GUIPanel implements ActionListener {
	private static final long serialVersionUID = 8135873613951664946L;

	private String schoolName;

	private Map<String, Course> coursesToTake;

	protected JPanel infoPanel;

	protected JPanel coursesTakenPanel;

	protected JPanel coursesToTakePanel;

	protected JPanel navigationPanel;

	private double creditHoursCompleted;

	private Map<String, Course> sortedCourseMap;

	private static final int CREDITHOURSTOGRADUATE = 120;

	public PlanPanel(SystemController controller) {
		super(controller);
		this.schoolName = controller.getActiveSchool().getName();
		this.coursesToTake = controller.getActiveUser().getCoursesToTake();
		this.addComponentsToPane();
	}

	private void addComponentsToPane() {
		infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, 3));

		this.createTitledBorder(infoPanel, schoolName);

		creditHoursCompleted = controller.getActiveTranscript()
				.getCreditHoursCompleted();

		infoPanel.add(new JLabel("Major: "
				+ controller.getActiveUser().getMajor()));
		infoPanel.add(new JLabel("Credit Hours Completed: "
				+ creditHoursCompleted));
		infoPanel.add(new JLabel("Credit Hours Remaining: "
				+ (CREDITHOURSTOGRADUATE - creditHoursCompleted)));

		coursesTakenPanel = new JPanel();
		coursesTakenPanel.setLayout(new BoxLayout(coursesTakenPanel, 3));

		this.createTitledBorder(coursesTakenPanel, "Courses Taken");

		sortedCourseMap = new TreeMap<String, Course>();

		Iterator<Semester> iter = controller.getActiveTranscript()
				.getSemesters().values().iterator();

		while (iter.hasNext()) {
			Semester s = (Semester) iter.next();
			sortedCourseMap.putAll(s.getCourses());
		}

		if (sortedCourseMap.size() == 0) {
			JLabel instructionLbl = new JLabel("Add a course to get started.");
			instructionLbl.setForeground(Color.blue);
			coursesTakenPanel.add(instructionLbl);
		} else {
			for (Course c : sortedCourseMap.values()) {
				coursesTakenPanel.add(createLabel(c.getCourseName(),
						c.getCreditHours() + " - " + c.getCourseName() + " ["
								+ c.getFinalGrade() + "]"));
			}
		}

		this.coursesToTakePanel = new JPanel();
		coursesToTakePanel.setLayout(new BoxLayout(coursesToTakePanel, 3));

		final GUIPanel thisWholePanel = this;

		coursesToTakePanel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Component selectedComponent = coursesToTakePanel
						.getComponentAt(e.getX(), e.getY());

				if (SwingUtilities.isRightMouseButton(e)
						&& selectedComponent != coursesToTakePanel) {
					System.out.println("You clicked the component named: "
							+ selectedComponent.getName());
				}

				int response = JOptionPane.showConfirmDialog(thisWholePanel,
						"Are you sure you wish to remove this course?",
						"Confirm", 0, 3);

				if (response == 0) {
					controller.getActiveUser().removeCourseToTake(
							selectedComponent.getName());

					controller.saveUserList();
					controller.getRootFrame().addPanel(
							new PlanPanel(controller), thisWholePanel);
				}
			}
		});

		createTitledBorder(this.coursesToTakePanel, "Courses To Take");

		if (coursesToTake.size() == 0) {
			JLabel instructionLbl = new JLabel("Add a course to get started.");
			instructionLbl.setForeground(Color.blue);
			coursesToTakePanel.add(instructionLbl);
		} else {
			for (Course c : coursesToTake.values()) {
				coursesToTakePanel.add(createLabel(c.getCourseName(),
						c.getCreditHours() + " - " + c.getCourseName()));
			}
		}

		navigationPanel = new JPanel();
		navigationPanel.setLayout(new BoxLayout(navigationPanel, 3));
		navigationPanel.add(createButton("selectMajor", "Select Major"));
		navigationPanel.add(createButton("addCourse", "Add Course"));
		navigationPanel.add(createButton("back", "Back"));

		this.createTitledBorder(navigationPanel, "Navigation");

		this.setLayout(new BoxLayout(this, 3));
		this.add(infoPanel);
		this.add(coursesTakenPanel);
		this.add(coursesToTakePanel);
		this.add(navigationPanel);
	}

	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();

		if (action.equals("addCourse")) {
			controller.getRootFrame()
					.addPanel(new PlanDialog(controller), this);
		} else if (action.equals("selectMajor")) {
			controller.getRootFrame().addPanel(new MajorDialog(controller),
					this);
		} else if (action.equals("back"))
			controller.getRootFrame().addPanel(new SemesterPanel(controller),
					this);
	}
}