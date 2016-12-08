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

import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.gmail.davgatto.MADKing.Retriever.SchoolRetriever;

public class Gui extends JFrame implements ActionListener, ItemListener {

	private static final long serialVersionUID = 1L;

	private static final Logger log4j = LogManager.getLogger(Gui.class.getName());

	final private static String pathSeparator = System.getProperty("file.separator");

	private static Properties props;

	private JFrame mainFrame;

	private HashMap<String, JCheckBox> bxsTipi;

	private String makeAction = "";
	private String sendAction = "";

	private String teachDet;
	private String schools;
	private String pecDet;
	private String target;
	private String simMail = "";
	private String anno;
	private ArrayList<String> unwanted;

	private JTextField textFieldTarget;
	private JTextField textFieldTeacher;
	private JTextField textFieldPec;
	private JTextField textFieldAs;
	private JTextField textFieldSim;
	private JTextField textFieldSchools;

	private String defaultWorkDir = ""; // TODO Metti listener su
										// textFieldTarget e
	// caccia sto cunno di defaultWorkDir,
	// soprattutto dai metodi responsive

	private JFrame getMainFrame() {
		return mainFrame;
	}

	private void setMainFrame(JFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

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

	private ArrayList<String> getUnwanted() {
		return unwanted;
	}

	private void setUnwanted(ArrayList<String> unwanted) {
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

	private String getDefaultWorkDir() {
		return defaultWorkDir;
	}

	private void setDefaultWorkDir(String defaultWorkDir) {
		this.defaultWorkDir = defaultWorkDir;
	}

	public static void main(String[] args) {
		log4j.info("Started!");

		InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("etc/application.properties");
		props = new Properties();
		try {
			props.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}

		new Gui();
	}

	private Gui() {
		log4j.debug(Gui.class.getName() + " constructor invoked");

		// Create and set up the window.
		setMainFrame(new JFrame(props.getProperty("label.title")));
		getMainFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setUnwanted(new ArrayList<String>());

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		// Set up the content pane.
		addComponentsToPane(getMainFrame().getContentPane());

		// Display the window.
		getMainFrame().pack();
		getMainFrame().setVisible(true);
	}

	private void refresh(JFrame frame) {
		frame.getContentPane().removeAll();
		addComponentsToPane(frame.getContentPane());

		frame.pack();
		frame.setVisible(true);
	}

	public void addComponentsToPane(Container pane) {
		log4j.debug(Gui.class.getName() + ".addComponentsToPane invoked");

		setDefaultWorkDir(System.getProperty("user.home") + pathSeparator + props.getProperty("default.workDirectory"));

		pane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.weightx = 1;
		c.weighty = 1;

		int y = -1;

		JLabel label = new JLabel(props.getProperty("label.workDir"));
		// label.setFont(font);
		c.anchor = GridBagConstraints.BASELINE_TRAILING;
		c.gridx = 0;
		c.gridy = ++y;
		pane.add(label, c);
		if (textFieldTarget == null) {
			textFieldTarget = new JTextField(getDefaultWorkDir());
		}
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = y;
		pane.add(textFieldTarget, c);

		label = new JLabel(props.getProperty("label.teachDet"));
		c.fill = GridBagConstraints.EAST;
		c.gridx = 0;
		c.gridy = ++y;
		pane.add(label, c);
		if (textFieldTeacher == null) {
			textFieldTeacher = new JTextField(props.getProperty("placeholder.teachDet"));
		}
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = y;
		pane.add(textFieldTeacher, c);

		label = new JLabel(props.getProperty("label.schoolsDet"));
		c.fill = GridBagConstraints.EAST;
		c.gridx = 0;
		c.gridy = ++y;
		pane.add(label, c);
		if (textFieldSchools == null) {
			textFieldSchools = new JTextField(props.getProperty("placeholder.schoolsDet"));
		}
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = y;
		pane.add(textFieldSchools, c);
		textFieldSchools.addActionListener(this);

		label = new JLabel(props.getProperty("label.pecDet"));
		c.fill = GridBagConstraints.EAST;
		c.gridx = 0;
		c.gridy = ++y;
		pane.add(label, c);
		if (textFieldPec == null) {
			textFieldPec = new JTextField(props.getProperty("placeholder.pecDet"));
		}
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = y;
		pane.add(textFieldPec, c);

		label = new JLabel(props.getProperty("label.as"));
		c.fill = GridBagConstraints.EAST;
		c.gridx = 0;
		c.gridy = ++y;
		pane.add(label, c);
		if (textFieldAs == null) {
			textFieldAs = new JTextField(props.getProperty("placeholder.as"));
		}
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = y;
		pane.add(textFieldAs, c);

		label = new JLabel(props.getProperty("label.simMail"));
		c.fill = GridBagConstraints.EAST;
		c.gridx = 0;
		c.gridy = ++y;
		pane.add(label, c);
		if (textFieldSim == null) {
			textFieldSim = new JTextField(props.getProperty("placeholder.simMail"));
		}
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = y;
		pane.add(textFieldSim, c);

		label = new JLabel(props.getProperty("label.tipi"));
		c.fill = GridBagConstraints.EAST;
		c.anchor = GridBagConstraints.CENTER;
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
		c.anchor = GridBagConstraints.ABOVE_BASELINE_LEADING;
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
		for (String key : getBxsTipi().keySet()) {
			if (shift) {
				c.gridx = 1;
				c.gridy = y;
			} else {
				c.gridx = 0;
				c.gridy = ++y;
			}
			shift = !shift;
			pane.add(getBxsTipi().get(key), c);
			getBxsTipi().get(key).addItemListener(this);
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
		btnSend.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		log4j.debug("Action: " + e.getActionCommand() + "\n\t" + e.getSource());

		if (getMakeAction().equals(e.getActionCommand()) || getSendAction().equals(e.getActionCommand())) {
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
					log4j.error(Gui.class.getName() + " Il percorso: " + getSchools() + " non è una directory valida");
					return;
				}
			} else {
				log4j.error(Gui.class.getName() + " Il percorso: " + getSchools()
						+ " non è un file o una directory valida");
				return;
			}
		} else if (e.getSource().equals(textFieldSchools)) {
			log4j.debug("Schools text field set by user: " + e.getActionCommand());
			try {
				getBxsTipi().clear();
				getUnwanted().clear();
				for (String t : SchoolRetriever.getAllValuesForField("tipo",
						defaultWorkDir + pathSeparator + e.getActionCommand())) {
					JCheckBox checkbox = new JCheckBox(t, true);
					getBxsTipi().put(t, checkbox);
				}
				refresh(mainFrame);
			} catch (IllegalArgumentException ex) {
				getBxsTipi().clear();
				getUnwanted().clear();
				refresh(mainFrame);
				ex.printStackTrace();
				return;
			} catch (FileNotFoundException ex) {
				getBxsTipi().clear();
				getUnwanted().clear();
				refresh(mainFrame);
				ex.printStackTrace();
				return;
			}
		}

	}

	private void executeWithSingleSchoolsFile(ActionEvent e, String schools) {
		log4j.debug(Gui.class.getName() + ".executeWithSingleSchoolsFile invoked");
		String dirName = getTarget() + props.getProperty("default.senderLookupDir") + pathSeparator + schools
				.substring(schools.indexOf("_schoolsDetails.json") - 2, schools.indexOf("_schoolsDetails.json"));

		if (getMakeAction().equals(e.getActionCommand())) {
			com.gmail.davgatto.MADKing.Maker.App.makeMad(getTeachDet(), schools, dirName, getAnno(), getUnwanted(),
					props.getProperty("model.blankMADpdf"));
			return;
		}

		if (getSendAction().equals(e.getActionCommand())) {
			com.gmail.davgatto.MADKing.Maker.App.makeMad(getTeachDet(), schools, dirName, getAnno(), getUnwanted(),
					props.getProperty("model.blankMADpdf"));
			com.gmail.davgatto.MADKing.Sender.App.send(getTeachDet(), getPecDet(), schools, dirName, getSimMail());
			return;
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
			if (getUnwanted().contains(item)) {
				getUnwanted().remove(item);
			}
		} else if (state == 2) {
			getUnwanted().add(item);
		}
		log4j.debug("ArrayList<String> unwanted = " + getUnwanted().toString());
	}
}