/**
 * 	MADKing - Genera e invia tramite PEC moduli di MAD per supplenze nelle scuole itailane
 *  (No chance you need this program and do not speak Italian!)
 *  
 *   Copyright (C) 2016  Davide Gatto
 *   
 *   @author Davide Gatto
 *   @mail davgatto@gmail.com
 *   
 *   This file is part of MADKing
 *   MADKing is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.gmail.davgatto.MADKing.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.gmail.davgatto.MADKing.Retriever.SchoolRetriever;
import com.gmail.davgatto.MADKing.all.App;

public class Gui extends JFrame implements ActionListener, ItemListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger log4j = LogManager.getLogger(Gui.class.getName());

	final private static String pathSeparator = System.getProperty("file.separator");

	private HashMap<String, JCheckBox> bxsTipi;
	private String makeAction = "";
	private String sendAction = "";

	private String teachDet;
	private String schools;
	private String pecDet;
	private String target;
	private String simMail;
	private String anno;
	private String unwanted = "";

	private String getTeachDet() {
		return teachDet;
	}

	private void setTeachDet(String teachDet) {
		this.teachDet = teachDet;
	}

	private String getSchools() {
		return schools;
	}

	private void setSchools(String schools) {
		this.schools = schools;
	}

	private String getPecDet() {
		return pecDet;
	}

	private void setPecDet(String pecDet) {
		this.pecDet = pecDet;
	}

	private String getTarget() {
		return target;
	}

	private void setTarget(String target) {
		this.target = target;
	}

	private String getSimMail() {
		return simMail;
	}

	private void setSimMail(String simMail) {
		this.simMail = simMail;
	}

	public String getAnno() {
		return anno;
	}

	public void setAnno(String anno) {
		this.anno = anno;
	}

	private String getUnwanted() {
		return unwanted;
	}

	private void setUnwanted(String unwanted) {
		this.unwanted = unwanted;
	}

	private HashMap<String, JCheckBox> getBxsTipi() {
		return bxsTipi;
	}

	private void setBxsTipi(HashMap<String, JCheckBox> bxsTipi) {
		this.bxsTipi = bxsTipi;
	}

	private String getMakeAction() {
		return makeAction;
	}

	private void setMakeAction(String makeAction) {
		this.makeAction = makeAction;
	}

	private String getSendAction() {
		return sendAction;
	}

	private void setSendAction(String sendAction) {
		this.sendAction = sendAction;
	}

	private JTextField textFieldTarget;
	private JTextField textFieldTeacher;
	private JTextField textFieldPec;
	private JTextField textFieldAs;
	private JTextField textFieldSim;
	private JTextField textFieldSchools;

	public void addComponentsToPane(Container pane) {
		log4j.debug(Gui.class.getName() + ".addComponentsToPane invoked");

		InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("etc/application.properties");
		Properties props = new Properties();
		try {
			props.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String defaultWorkDir = System.getProperty("user.home") + pathSeparator
				+ props.getProperty("default.workDirectory");

		pane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		int y = -1;

		JLabel label = new JLabel(props.getProperty("label.workDir"));
		c.fill = GridBagConstraints.EAST;
		c.gridx = 0;
		c.gridy = ++y;
		pane.add(label, c);
		textFieldTarget = new JTextField(defaultWorkDir);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = y;
		pane.add(textFieldTarget, c);

		label = new JLabel(props.getProperty("label.teachDet"));
		c.fill = GridBagConstraints.EAST;
		c.gridx = 0;
		c.gridy = ++y;
		pane.add(label, c);
		textFieldTeacher = new JTextField(props.getProperty("placeholder.teachDet"));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = y;
		pane.add(textFieldTeacher, c);

		label = new JLabel(props.getProperty("label.schoolsDet"));
		c.fill = GridBagConstraints.EAST;
		c.gridx = 0;
		c.gridy = ++y;
		pane.add(label, c);
		textFieldSchools = new JTextField(props.getProperty("placeholder.schoolsDet"));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = y;
		pane.add(textFieldSchools, c);

		label = new JLabel(props.getProperty("label.pecDet"));
		c.fill = GridBagConstraints.EAST;
		c.gridx = 0;
		c.gridy = ++y;
		pane.add(label, c);
		textFieldPec = new JTextField(props.getProperty("placeholder.pecDet"));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = y;
		pane.add(textFieldPec, c);

		label = new JLabel(props.getProperty("label.as"));
		c.fill = GridBagConstraints.EAST;
		c.gridx = 0;
		c.gridy = ++y;
		pane.add(label, c);
		textFieldAs = new JTextField(props.getProperty("placeholder.as"));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = y;
		pane.add(textFieldAs, c);

		label = new JLabel(props.getProperty("label.simMail"));
		c.fill = GridBagConstraints.EAST;
		c.gridx = 0;
		c.gridy = ++y;
		pane.add(label, c);
		textFieldSim = new JTextField(props.getProperty("placeholder.simMail"));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = y;
		pane.add(textFieldSim, c);

		label = new JLabel(props.getProperty("label.tipi"));
		c.fill = GridBagConstraints.EAST;
		c.gridx = 0;
		c.gridy = ++y;
		pane.add(label, c);
		JCheckBox ckboxAllTipi = new JCheckBox(props.getProperty("checkbox.allTipi"), true);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = y;
		pane.add(ckboxAllTipi, c);
		ckboxAllTipi.addItemListener(this);

		boolean shift = false;
		if (getBxsTipi() == null) {
			setBxsTipi(new HashMap<String, JCheckBox>());
			try {
				for (String t : SchoolRetriever.getAllValuesForField("tipo",
						defaultWorkDir + pathSeparator + props.getProperty("default.schoolsDetails"))) {
					JCheckBox checkbox = new JCheckBox(t, true);
					if (shift) {
						c.gridx = 1;
						c.gridy = y;
					} else {
						c.gridx = 0;
						c.gridy = ++y;
					}
					shift = !shift;
					getBxsTipi().put(t, checkbox);
					pane.add(bxsTipi.get(t), c);
					getBxsTipi().get(t).addItemListener(this);
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		setMakeAction(props.getProperty("button.make"));
		JButton btnMake = new JButton(getMakeAction());
		c.gridx = 0;
		c.gridy = ++y;
		pane.add(btnMake, c);
		btnMake.addActionListener(this);

		setSendAction(props.getProperty("button.send"));
		JButton btnSend = new JButton(getSendAction());
		c.gridx = 1;
		c.gridy = y;
		pane.add(btnSend, c);
	}

	private Gui() {
		log4j.debug(Gui.class.getName() + " constructor invoked");

		InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("etc/application.properties");
		Properties props = new Properties();
		try {
			props.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Create and set up the window.
		JFrame frame = new JFrame(props.getProperty("label.title"));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Set up the content pane.
		addComponentsToPane(frame.getContentPane());

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {

		log4j.info("Started!");

		new Gui();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		log4j.debug("Action: " + e.getActionCommand() + "\n\t" + e.getSource());
		setTarget(textFieldTarget.getText());

		if (!getTarget().endsWith(pathSeparator)) {
			setTarget(getTarget() + pathSeparator);
		}

		setTeachDet(getTarget() + textFieldTeacher.getText());

		setAnno(textFieldAs.getText());

		setSchools(getTarget() + textFieldSchools.getText());

		setPecDet(getTarget() + textFieldPec.getText());

		setSimMail(textFieldSim.getText());

		File schoolsFile = new File(getSchools());

		if (schoolsFile.isFile()) {
			executeWithSingleSchoolsFile(e, getSchools());
		} else if (schoolsFile.isDirectory()) {
			File[] directoryListing = schoolsFile.listFiles();
			if (directoryListing != null) {
				for (File schools : directoryListing) {
					executeWithSingleSchoolsFile(e, schools.getPath());
				}
			} else {
				log4j.error(App.class.getName() + " Il percorso: " + getSchools() + " non è una directory valida");
				return;
			}
		} else {
			log4j.error(
					App.class.getName() + " Il percorso: " + getSchools() + " non è un file o una directory valida");
			return;
		}

	}

	private void executeWithSingleSchoolsFile(ActionEvent e, String schools) {
		log4j.debug(Gui.class.getName() + ".executeWithSingleSchoolsFile invoked");
		String dirName = "FilesGenerati" + pathSeparator + schools
				.substring(schools.indexOf("_schoolsDetails.json") - 2, schools.indexOf("_schoolsDetails.json"));
		if (getMakeAction().equals(e.getActionCommand())) {

			String[] args = { "--teacherdetails=" + getTeachDet(), "--as=" + getAnno(), "--schools=" + schools,
					"--pecmaildetails=" + getPecDet(), "--directory=" + getTarget() + dirName,
					"--unwanted=" + getUnwanted() };
			int m = com.gmail.davgatto.MADKing.Maker.App.makeMad(args);
			if (m == 0) {
				log4j.info("MADKing: MADMaker successfully executed");
			} else {
				log4j.debug("MADKing: MADMaker exit status: " + m);
			}
			return;
		}

		if (getSendAction().equals(e.getActionCommand())) {
			if (!getSimMail().isEmpty()) {
				String[] args = { "--teacherdetails=" + getTeachDet(), "--as=" + getAnno(), "--schools=" + schools,
						"--pecmaildetails=" + getPecDet(), "--directory=" + getTarget() + dirName,
						"--simulate=" + getSimMail() };

				int m = com.gmail.davgatto.MADKing.Maker.App.makeMad(args);
				if (m == 0) {
					log4j.info("MADKing: MADMaker successfully executed");
				} else {
					log4j.debug("MADKing: MADMaker exit status: " + m);
				}
				int s = com.gmail.davgatto.MADKing.Sender.App.send(args);
				if (s == 0) {
					log4j.info("MADKing: MADSender successfully executed");
				} else {
					log4j.debug("MADKing: MADSender exit status: " + m);
				}
				return;
			} else {
				String[] args = { "--teacherdetails=" + getTeachDet(), "--as=" + getAnno(), "--schools=" + schools,
						"--pecmaildetails=" + getPecDet(), "--directory=" + getTarget() + dirName };

				int m = com.gmail.davgatto.MADKing.Maker.App.makeMad(args);
				if (m == 0) {
					log4j.info("MADKing: MADMaker successfully executed");
				} else {
					log4j.debug("MADKing: MADMaker exit status: " + m);
				}
				int s = com.gmail.davgatto.MADKing.Sender.App.send(args);
				if (s == 0) {
					log4j.info("MADKing: MADSender successfully executed");
				} else {
					log4j.debug("MADKing: MADSender exit status: " + m);
				}
				return;
			}
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		log4j.debug("ItemEvent " + ((JCheckBox) e.getSource()).getText() + " -> " + e.getStateChange());
		int state = e.getStateChange();
		String item = ((JCheckBox) e.getSource()).getText();
		if ("(Seleziona/Deseleziona tutti)".equals(item)) {
			if (state == 1) {
				for (String key : getBxsTipi().keySet()) {
					if (!getBxsTipi().get(key).isSelected()) {
						getBxsTipi().get(key).doClick();
					}
				}
				log4j.debug("String unwanted = " + getUnwanted());
				return;
			} else {
				for (String key : getBxsTipi().keySet()) {
					if (getBxsTipi().get(key).isSelected()) {
						getBxsTipi().get(key).doClick();
					}
				}
				log4j.debug("String unwanted = " + getUnwanted());
				return;
			}
		}
		if (state == 1) {
			if (unwanted.contains("|" + item + "|")) {
				setUnwanted(getUnwanted().replace("|" + item + "|", ""));
			}
		} else if (state == 2) {
			setUnwanted(unwanted + "|" + item + "|");
		}
		log4j.debug("String unwanted = " + getUnwanted());
	}
}