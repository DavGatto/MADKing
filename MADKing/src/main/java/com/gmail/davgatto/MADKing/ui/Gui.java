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

import java.awt.Color;
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
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.gmail.davgatto.MADKing.Retriever.SchoolRetriever;

public class Gui extends JPanel implements ActionListener, ItemListener {

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
	private ArrayList<String> messages = new ArrayList<String>();

	private JTextField textFieldTarget;
	private JTextField textFieldTeacher;
	private JTextField textFieldPec;
	private JTextField textFieldAs;
	private JTextField textFieldSim;
	private JTextField textFieldSchools;
	private JButton btnMake;
	private JButton btnSend;

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

	public ArrayList<String> getMessages() {
		return messages;
	}

	public void setMessages(ArrayList<String> messages) {
		this.messages = messages;
	}

	public static void main(String[] args) {
		log4j.info("Avviato!");

		InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("etc/application.properties");
		props = new Properties();
		try {
			props.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (!Utils.prompt("MADKing - Licenza d'uso e dichiarazione di non responsabilità",
				"Questo programma, MADKing, è rialsciato con licenza GPL versione 3,\n"
						+ "e di conseguenza NON C'È ALCUNA GARANZIA PER IL PROGRAMMA.\n\n"
						+ "L'autore fornisce il programma \"così com'è\" SENZA GARANZIA DI ALCUN TIPO, NÈ ESPRESSA NÈ IMPLICITA, \n"
						+ "INCLUSE, MA NON LIMITATE A, LE GARANZIE DI COMMERCIABILITÀ O DI UTILIZZABILITÀ PER UN PARTICOLARE SCOPO.\n\n"
						+ "L'INTERO RISCHIO CONCERNENTE LA QUALITÀ E LE PRESTAZIONI DEL PROGRAMMA È DELL'UTENTE FINALE.",
				"Ho capito, voglio usare il programma", "Non lanciare il programma", 1, "icons/gplv3-88x31.png")) {
			log4j.info("L'utente ha rifiutato le condizioni di utilizzo. Chiudo MADKing");
			return;
		}

		log4j.info("L'utente ha accettato le condizioni di utilizzo. Lancio MADKing!");
		new Gui();
	}

	private Gui() {
		log4j.debug(Gui.class.getName() + " constructor invoked");

		// Create and set up the window.
		setMainFrame(new JFrame(props.getProperty("label.title")));
		getMainFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setUnwanted(new ArrayList<String>());
		getMessages().add(props.getProperty("message.error.simMail"));
		setTarget(System.getProperty("user.home") + pathSeparator + props.getProperty("default.workDirectory"));

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
		log4j.debug(Gui.class.getName() + ".refresh invoked");
		frame.getContentPane().removeAll();
		addComponentsToPane(frame.getContentPane());

		frame.pack();
		frame.setVisible(true);
	}

	public void addComponentsToPane(Container pane) {
		log4j.debug(Gui.class.getName() + ".addComponentsToPane invoked");
		// TODO Implementa conteggio delle scuole in diretta
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
			textFieldTarget = new JTextField(getTarget());
		}
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = y;
		pane.add(textFieldTarget, c);
		if (textFieldTarget.getActionListeners().length == 0) {
			textFieldTarget.addActionListener(this);
		}

		JButton chooseDir = new JButton("Scegli...");
		chooseDir.setActionCommand("chooseWorkDir");
		c.gridx = 2;
		pane.add(chooseDir, c);
		chooseDir.addActionListener(this);

		label = new JLabel(props.getProperty("label.teachDet"));
		c.fill = GridBagConstraints.EAST;
		c.gridx = 0;
		c.gridy = ++y;
		pane.add(label, c);
		if (textFieldTeacher == null) {
			textFieldTeacher = new JTextField(getTarget() + pathSeparator + props.getProperty("placeholder.teachDet"));
		}
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = y;
		pane.add(textFieldTeacher, c);
		if (textFieldTeacher.getActionListeners().length == 0) {
			textFieldTeacher.addActionListener(this);
		}

		JButton chooseTeach = new JButton("Scegli...");
		chooseTeach.setActionCommand("chooseTeacher");
		c.gridx = 2;
		pane.add(chooseTeach, c);
		chooseTeach.addActionListener(this);

		label = new JLabel(props.getProperty("label.schoolsDet"));
		c.fill = GridBagConstraints.EAST;
		c.gridx = 0;
		c.gridy = ++y;
		pane.add(label, c);
		if (textFieldSchools == null) {
			textFieldSchools = new JTextField(
					getTarget() + pathSeparator + props.getProperty("placeholder.schoolsDet"));
			textFieldSchools.addActionListener(this);
		}
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = y;
		pane.add(textFieldSchools, c);

		JButton chooseSch = new JButton("Scegli...");
		chooseSch.setActionCommand("chooseSchool");
		c.gridx = 2;
		pane.add(chooseSch, c);
		chooseSch.addActionListener(this);

		label = new JLabel(props.getProperty("label.pecDet"));
		c.fill = GridBagConstraints.EAST;
		c.gridx = 0;
		c.gridy = ++y;
		pane.add(label, c);
		if (textFieldPec == null) {
			textFieldPec = new JTextField(getTarget() + pathSeparator + props.getProperty("placeholder.pecDet"));
			textFieldPec.addActionListener(this);
		} else {
			textFieldPec.setText(getPecDet());
		}
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = y;
		pane.add(textFieldPec, c);

		JButton choosePec = new JButton("Scegli...");
		choosePec.setActionCommand("choosePec");
		c.gridx = 2;
		pane.add(choosePec, c);
		choosePec.addActionListener(this);

		label = new JLabel(props.getProperty("label.as"));
		c.fill = GridBagConstraints.EAST;
		c.gridx = 0;
		c.gridy = ++y;
		pane.add(label, c);
		if (textFieldAs == null) {
			textFieldAs = new JTextField(props.getProperty("placeholder.as"));
			textFieldAs.addActionListener(this);
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
			textFieldSim.addActionListener(this);
		}
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = y;
		pane.add(textFieldSim, c);

		if (getBxsTipi() == null) {
			setBxsTipi(new HashMap<String, JCheckBox>());
			try {
				for (String t : SchoolRetriever.getAllValuesForField("tipo",
						getTarget() + pathSeparator + props.getProperty("default.schoolsDetails"))) {
					getBxsTipi().put(t, new JCheckBox(t, true));
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		if (!getBxsTipi().isEmpty()) {
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
			if (ckboxAllTipi.getItemListeners().length == 0) {
				ckboxAllTipi.addItemListener(this);
			}
		}
		boolean shift = false;
		c.anchor = GridBagConstraints.ABOVE_BASELINE_LEADING;
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
			if (getBxsTipi().get(key).getItemListeners().length == 0) {
				getBxsTipi().get(key).addItemListener(this);
			}
		}

		for (String msg : getMessages()) {
			label = new JLabel(msg);
			label.setForeground(Color.RED);
			c.gridwidth = 3;
			c.gridx = 0;
			c.gridy = ++y;
			pane.add(label, c);
		}

		setMakeAction(props.getProperty("button.make"));
		if (btnMake == null) {
			btnMake = new JButton(getMakeAction());
		}
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = ++y;
		pane.add(btnMake, c);
		if (btnMake.getActionListeners().length == 0) {
			btnMake.addActionListener(this);
		}

		setSendAction(props.getProperty("button.send"));
		if (btnSend == null) {
			btnSend = new JButton(getSendAction());
			btnSend.setEnabled(false);
		}
		c.gridx = 1;
		c.gridy = y;
		pane.add(btnSend, c);
		if (btnSend.getActionListeners().length == 0) {
			btnSend.addActionListener(this);
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		log4j.debug("Action: " + e.getActionCommand() + "\n\t" + e.getSource());
		//System.out.println("Action: " + e.getActionCommand() + "\n\t" + e.getSource());

		if (e.getActionCommand().startsWith("choose")) {
			log4j.debug("Choose file button " + e.getActionCommand());
			//System.out.println("Choose file button " + e.getActionCommand());
			if ("chooseWorkDir".equals(e.getActionCommand())) {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = fc.showOpenDialog(mainFrame);
				//System.out.println("returnval: " + returnVal);
				if (returnVal == 0) {
					setTarget(fc.getSelectedFile().getAbsolutePath());
					textFieldTarget.setText(getTarget());
					//System.out.println("pecdet: " + getTarget());
					ActionEvent chosen = new ActionEvent(textFieldTarget, 0, "workDirChosen");
					actionPerformed(chosen);
				} else {
					return;
				}
			} else if ("choosePec".equals(e.getActionCommand())) {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int returnVal = fc.showOpenDialog(mainFrame);
				if (returnVal == 0) {
					//System.out.println("returnval: " + returnVal);
					setPecDet(fc.getSelectedFile().getAbsolutePath());
					textFieldPec.setText(getPecDet());
					//System.out.println("pecdet: " + getPecDet());
					ActionEvent chosen = new ActionEvent(textFieldPec, 0, "pecFileChosen");
					actionPerformed(chosen);
				} else {
					return;
				}
			} else if ("chooseTeacher".equals(e.getActionCommand())) {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int returnVal = fc.showOpenDialog(mainFrame);
				if (returnVal == 0) {
					//System.out.println("returnval: " + returnVal);
					setTeachDet(fc.getSelectedFile().getAbsolutePath());
					textFieldTeacher.setText(getTeachDet());
					//System.out.println("teachdet: " + getTeachDet());
					ActionEvent chosen = new ActionEvent(textFieldTeacher, 0, "teacherFileChosen");
					actionPerformed(chosen);
				} else {
					return;
				}
			} else if ("chooseSchool".equals(e.getActionCommand())) {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				int returnVal = fc.showOpenDialog(mainFrame);
				//System.out.println("returnval: " + returnVal);
				if (returnVal == 0) {
					setSchools(fc.getSelectedFile().getAbsolutePath());
					textFieldSchools.setText(getSchools());
					//System.out.println("schoolsdet: " + getTeachDet());
					ActionEvent chosen = new ActionEvent(textFieldSchools, 0, getSchools());
					actionPerformed(chosen);
				} else {
					return;
				}
			} else {
				log4j.error("Invalid action command from choose button: " + e.getActionCommand());
				return;
			}
		}

		getMessages().clear();
		int valid = validateUserInput();

		if (getMakeAction().equals(e.getActionCommand()) || getSendAction().equals(e.getActionCommand())) {
			if (valid == 2) {
				return;
			}
			if (valid == 1 && getSendAction().equals(e.getActionCommand())) {
				return;
			}
			File schoolsFile = new File(getSchools());
			if (schoolsFile.isFile()) {
				executeWithSingleSchoolsFile(e, getSchools());
				// TODO implementa dialog o messaggio "inviato!"
				return;
			} else if (schoolsFile.isDirectory()) {
				File[] directoryListing = schoolsFile.listFiles();
				if (directoryListing != null) {
					for (File schools : directoryListing) {
						executeWithSingleSchoolsFile(e, schools.getPath());
					}
					return;
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
			//System.out.println("Schools text field set by user: " + e.getActionCommand());
			try {
				getBxsTipi().clear();
				getUnwanted().clear();
				for (String t : SchoolRetriever.getAllValuesForField("tipo", e.getActionCommand())) {
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
			com.gmail.davgatto.MADKing.Sender.App.send(getTeachDet(), getPecDet(), schools, dirName, getSimMail(),
					getUnwanted());
			return;
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		log4j.debug("ItemEvent " + ((JCheckBox) e.getSource()).getText() + " -> " + e.getStateChange());
		int state = e.getStateChange();
		String item = ((JCheckBox) e.getSource()).getText();
		if (props.getProperty("checkbox.allTipi").equals(item)) {
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

	private int validateUserInput() {

		btnMake.setEnabled(true);
		btnSend.setEnabled(true);
		boolean valid = true;
		boolean couldSend = true;
		getMessages().clear();

		/** Check work directory */
		String input = textFieldTarget.getText();
		if ((new File(input)).isDirectory()) {
			// if ((new File(input + pathSeparator +
			// props.getProperty("default.senderLookupDir"))).isDirectory()) {
			// if (Utils.prompt("MADKing",
			// "Trovata cartella di files generati da MADKing pre-esistente.
			// Vuoi cancellarla? Se non lo fai, le vecchie MAD potrebbero essere
			// spedite",
			// "Cancella la cartella", "Non cancellare, sovrascrivi eventuali
			// vecchie MAD", 0, null)) { // TODO esternalizza
			// try {
			// FileUtils.deleteDirectory(new File(input + pathSeparator +
			// props.getProperty("default.senderLookupDir")));
			// log4j.debug("Old FilesGenerati directory deleted");
			// } catch (IOException e) {
			// log4j.error(e.getMessage());
			// }
			// }
			// } //TODO prompt in caso di cartella FilesGenerati già esistente
			setTarget(input);
			if (!getTarget().endsWith(pathSeparator)) {
				setTarget(getTarget() + pathSeparator);
			}
		} else {
			getMessages().add(props.getProperty("message.error.workDir") + " " + input);
			btnMake.setEnabled(false);
			btnSend.setEnabled(false);
			valid = false;
			refresh(mainFrame);
			return 2;
		}

		/** Check teacher details json file */
		input = textFieldTeacher.getText();
		if (Utils.validateTeacherFile(input) == null) {
			setTeachDet(input);
		} else {
			getMessages().add(props.getProperty("message.error.teachDet") + " " + input);
			btnMake.setEnabled(false);
			btnSend.setEnabled(false);
			valid = false;
		}

		/** Check anno scolastico */
		input = textFieldAs.getText();
		if (!input.isEmpty()) { // TODO implementa controllo formato NNNN/NN
			setAnno(input);
		} else {
			getMessages().add(props.getProperty("message.error.as") + " " + input);
			btnMake.setEnabled(false);
			btnSend.setEnabled(false);
			valid = false;
		}

		/** Check schools details file or directory */
		input = textFieldSchools.getText();
		ArrayList<String> invalidSchoolsFiles = Utils.getInvalidSchoolsFiles(input);
		if (invalidSchoolsFiles == null) {
			getMessages().add(props.getProperty("message.error.schoolsDet.missing") + " " + input);
			btnMake.setEnabled(false);
			btnSend.setEnabled(false);
			getBxsTipi().clear();
			valid = false;
		} else if (invalidSchoolsFiles.isEmpty()) {
			setSchools(input);
		} else {
			getMessages().add(props.getProperty("message.error.schoolsDet.invalid"));
			for (String str : invalidSchoolsFiles) {
				getMessages().add(str);
			}
			btnMake.setEnabled(false);
			btnSend.setEnabled(false);
			getBxsTipi().clear();
			valid = false;
		}

		/** Check PEC details json file */
		input = textFieldPec.getText();
		if (Utils.validatePecFile(input) == null) {
			setPecDet(input);
		} else {
			getMessages().add(props.getProperty("message.error.pecDet") + " " + input);
			// btnMake.setEnabled(false);
			btnSend.setEnabled(false);
			couldSend = false;
		}

		/** Check address for simulated run */
		input = textFieldSim.getText();
		if (Utils.isValidEmailAddress(input)) {
			setSimMail(input);
		} else {
			getMessages().add(props.getProperty("message.error.simMail") + " " + input);
			// btnMake.setEnabled(false);
			btnSend.setEnabled(false);
			couldSend = false;
		}

		if (!valid) {
			refresh(mainFrame);
			return 2;
		}

		if (!couldSend) {
			refresh(mainFrame);
			return 1;
		}

		btnMake.setEnabled(true);
		btnSend.setEnabled(true);
		refresh(mainFrame);
		return 0;
	}
}