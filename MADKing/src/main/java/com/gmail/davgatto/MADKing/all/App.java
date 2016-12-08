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
package com.gmail.davgatto.MADKing.all;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Vecchia GUI rudimentale, indipendente da Swing
 * @author Davide Gatto
 *
 */
@Deprecated
public class App extends Frame implements ActionListener, WindowListener {

	private static final long serialVersionUID = 1L;

	private static final Logger log4j = LogManager.getLogger(App.class.getName());

	private static String pathSeparator = System.getProperty("file.separator");

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

	private Label lblTeachDet;
	private TextField tfTeachDet;

	private Label lblAnno;
	private TextField tfAnno;

	private Label lblSchools;
	private TextField tfSchools;

	private Label lblPecDet;
	private TextField tfPecDet;

	private Label lblTarget;
	private TextField tfTarget;

	private Label lblSimMail;
	private TextField tfSimMail;

	private TextField tfUnwanted;
	private Label lblUnwanted;

	private Button btnMake;
	private Button btnSend;

	public App() {

		InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("etc/application.properties");
		Properties props = new Properties();
		try {
			props.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String defaultWorkDir = System.getProperty("user.home") + pathSeparator + props.getProperty("default.workDirectory");

		setLayout(new FlowLayout());


		final int fieldWidth = Integer.parseInt(props.getProperty("textfield.length"));

		lblTarget = new Label(props.getProperty("label.workDir"));
		add(lblTarget);

		tfTarget = new TextField(defaultWorkDir, fieldWidth);

		add(tfTarget);

		lblTeachDet = new Label(props.getProperty("label.teachDet"));

		add(lblTeachDet);

		tfTeachDet = new TextField("teacherDetails.json", fieldWidth);

		add(tfTeachDet);

		lblSchools = new Label(props.getProperty("label.schoolsDet"));
		add(lblSchools);
		tfSchools = new TextField(props.getProperty("placeholder.schoolsDet"), fieldWidth);
		
		add(tfSchools);

		lblUnwanted = new Label(props.getProperty("label.unwanted"));
		add(lblUnwanted);
		tfUnwanted = new TextField(getUnwanted(), fieldWidth);
		add(tfUnwanted);
		

		lblPecDet = new Label(props.getProperty("label.pecDet"));
		add(lblPecDet);
		tfPecDet = new TextField("pecDetails.json", fieldWidth);
		add(tfPecDet);

		lblSimMail = new Label(props.getProperty("label.simMail"));
		add(lblSimMail);
		tfSimMail = new TextField(props.getProperty("placeholder.simMail"), fieldWidth);
		add(tfSimMail);

		lblAnno = new Label(props.getProperty("label.as"));
		add(lblAnno);
		tfAnno = new TextField(props.getProperty("placeholder.as"), 8);
		add(tfAnno);

		btnMake = new Button(props.getProperty("button.make"));
		add(btnMake);
		btnMake.addActionListener(this);

		btnSend = new Button(props.getProperty("button.send"));
		add(btnSend);
		btnSend.addActionListener(this);

		addWindowListener(this);

		setTitle(props.getProperty("label.title"));
		setSize(Integer.parseInt(props.getProperty("window.width")), Integer.parseInt(props.getProperty("window.height")));

		setVisible(true);
	}

	public static void main(String[] args) {

		log4j.info("Started!");

		new App();

	}

	/**
	 * Reagisce alle azioni da finestra
	 */
	public void actionPerformed(ActionEvent e) {
		log4j.info("Richiesta creazione senza invio");
		setTarget(tfTarget.getText());

		if (!getTarget().endsWith(pathSeparator)) {
			setTarget(getTarget() + pathSeparator);
		}

		setTeachDet(getTarget() + tfTeachDet.getText());

		setAnno(tfAnno.getText());

		setSchools(getTarget() + tfSchools.getText());

		setUnwanted(tfUnwanted.getText());

		setPecDet(getTarget() + tfPecDet.getText());

		setSimMail(tfSimMail.getText());

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
			log4j.error(App.class.getName() + " Il percorso: " + getSchools() + " non è un file o una directory valida");
			return;
		}

	}

	private void executeWithSingleSchoolsFile(ActionEvent e, String schools) {
		String dirName = "FilesGenerati" + pathSeparator + schools
				.substring(schools.indexOf("_schoolsDetails.json") - 2, schools.indexOf("_schoolsDetails.json"));
		if (e.getSource() == btnMake) {

			String[] args = { "--teacherdetails=" + getTeachDet(), "--as=" + getAnno(), "--schools=" + schools,
					"--pecmaildetails=" + getPecDet(), "--directory=" + getTarget() + dirName,
					"--unwanted=" + getUnwanted() };
			int m = com.gmail.davgatto.MADKing.Maker.App.main(args);
			if (m == 0) {
				log4j.info("MADKing: MADMaker successfully executed");
			} else {
				log4j.debug("MADKing: MADMaker exit status: " + m);
			}
			return;

		}

		if (e.getSource() == btnSend) {
			if (!getSimMail().isEmpty()) {
				String[] args = { "--teacherdetails=" + getTeachDet(), "--as=" + getAnno(), "--schools=" + schools,
						"--pecmaildetails=" + getPecDet(), "--directory=" + getTarget() + dirName,
						"--simulate=" + getSimMail() };

				int m = com.gmail.davgatto.MADKing.Maker.App.main(args);
				if (m == 0) {
					log4j.info("MADKing: MADMaker successfully executed");
				} else {
					log4j.debug("MADKing: MADMaker exit status: " + m);
				}
				int s = com.gmail.davgatto.MADKing.Sender.App.main(args);
				if (s == 0) {
					log4j.info("MADKing: MADSender successfully executed");
				} else {
					log4j.debug("MADKing: MADSender exit status: " + m);
				}
				return;
			} else {
				String[] args = { "--teacherdetails=" + getTeachDet(), "--as=" + getAnno(), "--schools=" + schools,
						"--pecmaildetails=" + getPecDet(), "--directory=" + getTarget() + dirName };

				int m = com.gmail.davgatto.MADKing.Maker.App.main(args);
				if (m == 0) {
					log4j.info("MADKing: MADMaker successfully executed");
				} else {
					log4j.debug("MADKing: MADMaker exit status: " + m);
				}
				int s = com.gmail.davgatto.MADKing.Sender.App.main(args);
				if (s == 0) {
					log4j.info("MADKing: MADSender successfully executed");
				} else {
					log4j.debug("MADKing: MADSender exit status: " + m);
				}
				return;
			}
		}
	}

	/* WindowEvent handlers */

	public void windowClosing(WindowEvent evt) {
		log4j.trace("windowClosing event");
		System.exit(0);
	}

	public void windowOpened(WindowEvent evt) {
		log4j.trace("windowOpened event");
	}

	public void windowClosed(WindowEvent evt) {
		log4j.trace("windowClosed event");
	}

	public void windowIconified(WindowEvent evt) {
		log4j.trace("windowIconified event");
	}

	public void windowDeiconified(WindowEvent evt) {
		log4j.trace("windowDeiconified event");
	}

	public void windowActivated(WindowEvent evt) {
		log4j.trace("windowActivated event");
	}

	public void windowDeactivated(WindowEvent evt) {
		log4j.trace("windowDeactivated event");
	}

}
