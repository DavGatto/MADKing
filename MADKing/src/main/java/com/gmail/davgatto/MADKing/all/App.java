/**
 * 	MADMaker - Genera e invia tramite PEC moduli di MAD per supplenze nelle scuole itailane
 *  (No chance you need this program and do not speak Italian!)
 *  
 *   Copyright (C) 2016  Davide Gatto
 *   
 *   @author Davide Gatto
 *   @mail davgatto@gmail.com
 *   
 *   This file is part of MADMaker
 *   MADMaker is free software: you can redistribute it and/or modify
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
import java.awt.Checkbox;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class App extends Frame implements ActionListener, WindowListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static String pathSeparator = "/";

	private String teachDet;
	private String schools;
	private String pecDet;
	private String target;
	private String simMail;
	private String anno;
	private boolean debug;

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

	private boolean isDebug() {
		return debug;
	}

	private void setDebug(boolean debug) {
		this.debug = debug;
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

	private Checkbox cbDebug;

	private Button btnMake;
	private Button btnSend;

	public App() {

		setLayout(new FlowLayout());

		final int fieldWidth = 50;

		lblTarget = new Label("Cartella di lavoro");
		add(lblTarget);

		tfTarget = new TextField("<Path>", fieldWidth);

		add(tfTarget);

		lblTeachDet = new Label("File dettagli insegnante");

		add(lblTeachDet);

		tfTeachDet = new TextField("teacherDetails.json", fieldWidth);

		add(tfTeachDet);

		lblSchools = new Label("File dettagli scuole");
		add(lblSchools);
		tfSchools = new TextField("schoolsDetails.json", fieldWidth);
		add(tfSchools);

		lblPecDet = new Label("File dettagli PEC");
		add(lblPecDet);
		tfPecDet = new TextField("pecDetails.json", fieldWidth);
		add(tfPecDet);

		lblSimMail = new Label("Indirizzo per invio simulato");
		add(lblSimMail);
		tfSimMail = new TextField("<Lascia vuoto per invio reale tramite PEC>", fieldWidth);
		add(tfSimMail);

		lblAnno = new Label("Anno Scolastico");
		add(lblAnno);
		tfAnno = new TextField("2016/17", 8);
		add(tfAnno);

		cbDebug = new Checkbox("Debug", false);
		add(cbDebug);

		btnMake = new Button("Crea (senza inviare)");
		add(btnMake);
		btnMake.addActionListener(this);

		btnSend = new Button("Invia");
		add(btnSend);
		btnSend.addActionListener(this);

		addWindowListener(this);

		setTitle("MADKing");
		setSize(650, 255);

		setVisible(true);
	}

	public static void main(String[] args) {

		new App();

	}

	public void actionPerformed(ActionEvent e) {
		setTarget(tfTarget.getText());

		if (!getTarget().endsWith(pathSeparator)) {
			setTarget(getTarget() + pathSeparator);
		}

		setTeachDet(getTarget() + tfTeachDet.getText());
		setAnno(tfAnno.getText());
		setSchools(getTarget() + tfSchools.getText());
		setPecDet(getTarget() + tfPecDet.getText());

		setSimMail(tfSimMail.getText());
		setDebug(cbDebug.getState());

		if (e.getSource() == btnMake) {
			if (isDebug()) {
				String[] args = { "--teacherdetails=" + getTeachDet(), "--as=" + getAnno(), "--schools=" + getSchools(),
						"--pecmaildetails=" + getPecDet(), "--directory=" + getTarget() + "FilesGenerati", "--debug" };
				System.out.println("### DEBUG: Passed args[]:");
				for (String s : args) {
					System.out.println(s);
				}

				int m = com.gmail.davgatto.MADKing.Maker.App.makeMad(args);
				if (m == 0) {
					System.out.println("MADKing: MADMaker successfully executed");
				} else {
					System.out.println("MADKing: MADMaker exit status: " + m);
				}
				return;
			} else if (!isDebug()) {
				String[] args = { "--teacherdetails=" + getTeachDet(), "--as=" + getAnno(), "--schools=" + getSchools(),
						"--pecmaildetails=" + getPecDet(), "--directory=" + getTarget() + "FilesGenerati" };
				int m = com.gmail.davgatto.MADKing.Maker.App.makeMad(args);
				if (m == 0) {
					System.out.println("MADKing: MADMaker successfully executed");
				} else {
					System.out.println("MADKing: MADMaker exit status: " + m);
				}
				return;
			}
		}

		if (e.getSource() == btnSend) {
			if (isDebug() && !getSimMail().isEmpty()) {
				String[] args = { "--teacherdetails=" + getTeachDet(), "--as=" + getAnno(), "--schools=" + getSchools(),
						"--pecmaildetails=" + getPecDet(), "--directory=" + getTarget() + "FilesGenerati",
						"--simulate=" + getSimMail(), "--debug" };

				System.out.println("### DEBUG: Passed args[]:");
				for (String s : args) {
					System.out.println(s);
				}

				int m = com.gmail.davgatto.MADKing.Maker.App.makeMad(args);
				if (m == 0) {
					System.out.println("MADKing: MADMaker successfully executed");
				} else {
					System.out.println("MADKing: MADMaker exit status: " + m);
				}
				int s = com.gmail.davgatto.MADKing.Sender.App.send(args);
				if (s == 0) {
					System.out.println("MADKing: MADSender successfully executed");
				} else {
					System.out.println("MADKing: MADSender exit status: " + m);
				}
				return;
			} else if (isDebug() && getSimMail().isEmpty()) {
				String[] args = { "--teacherdetails=" + getTeachDet(), "--as=" + getAnno(), "--schools=" + getSchools(),
						"--pecmaildetails=" + getPecDet(), "--directory=" + getTarget() + "FilesGenerati", "--debug" };

				System.out.println("### DEBUG: Passed args[]:");
				for (String s : args) {
					System.out.println(s);
				}

				int m = com.gmail.davgatto.MADKing.Maker.App.makeMad(args);
				if (m == 0) {
					System.out.println("MADKing: MADMaker successfully executed");
				} else {
					System.out.println("MADKing: MADMaker exit status: " + m);
				}
				int s = com.gmail.davgatto.MADKing.Sender.App.send(args);
				if (s == 0) {
					System.out.println("MADKing: MADSender successfully executed");
				} else {
					System.out.println("MADKing: MADSender exit status: " + m);
				}
				return;
			} else if (!isDebug() && !getSimMail().isEmpty()) {
				String[] args = { "--teacherdetails=" + getTeachDet(), "--as=" + getAnno(), "--schools=" + getSchools(),
						"--pecmaildetails=" + getPecDet(), "--directory=" + getTarget() + "FilesGenerati" };
				int m = com.gmail.davgatto.MADKing.Maker.App.makeMad(args);
				if (m == 0) {
					System.out.println("MADKing: MADMaker successfully executed");
				} else {
					System.out.println("MADKing: MADMaker exit status: " + m);
				}
				int s = com.gmail.davgatto.MADKing.Sender.App.send(args);
				if (s == 0) {
					System.out.println("MADKing: MADSender successfully executed");
				} else {
					System.out.println("MADKing: MADSender exit status: " + m);
				}
				return;
			} else if (!isDebug() && getSimMail().isEmpty()) {
				String[] args = { "--teacherdetails=" + getTeachDet(), "--as=" + getAnno(), "--schools=" + getSchools(),
						"--pecmaildetails=" + getPecDet(), "--directory=" + getTarget() + "FilesGenerati" };
				int m = com.gmail.davgatto.MADKing.Maker.App.makeMad(args);
				if (m == 0) {
					System.out.println("MADKing: MADMaker successfully executed");
				} else {
					System.out.println("MADKing: MADMaker exit status: " + m);
				}
				int s = com.gmail.davgatto.MADKing.Sender.App.send(args);
				if (s == 0) {
					System.out.println("MADKing: MADSender successfully executed");
				} else {
					System.out.println("MADKing: MADSender exit status: " + m);
				}
				return;
			}
		}

	}

	/* WindowEvent handlers */

	public void windowClosing(WindowEvent evt) {
		System.exit(0);
	}

	public void windowOpened(WindowEvent evt) {
	}

	public void windowClosed(WindowEvent evt) {
	}

	public void windowIconified(WindowEvent evt) {
	}

	public void windowDeiconified(WindowEvent evt) {
	}

	public void windowActivated(WindowEvent evt) {
	}

	public void windowDeactivated(WindowEvent evt) {
	}
}
