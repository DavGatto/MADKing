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
package com.gmail.davgatto.MADKing.Maker;

import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;

public class TeacherDetails {

	private String sex;
	private String name;
	private String birthTown;
	private String birthArea;
	private String birthDate;
	private String residTown;
	private String residAddress;
	private String militare;
	private String degreeSubject;
	private String degreeExams;
	private String degreeDate;
	private String degreeInstitute;
	private String subjects;
	private String recapitoName;
	private String recapitoAddress;
	private String recapitoCAP;
	private String recapitoTown;
	private String recapitoTel;
	private String recapitoCell;
	private String recapitoEMail;
	private Image signature;

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBirthTown() {
		return birthTown;
	}

	public void setBirthTown(String birthTown) {
		this.birthTown = birthTown;
	}

	public String getBirthArea() {
		return birthArea;
	}

	public void setBirthArea(String birthArea) {
		this.birthArea = birthArea;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getResidTown() {
		return residTown;
	}

	public void setResidTown(String residTown) {
		this.residTown = residTown;
	}

	public String getResidAddress() {
		return residAddress;
	}

	public void setResidAddress(String residAddress) {
		this.residAddress = residAddress;
	}

	public String getMilitare() {
		return militare;
	}

	public void setMilitare(String militare) {
		this.militare = militare;
	}

	public String getDegreeSubject() {
		return degreeSubject;
	}

	public void setDegreeSubject(String degreeSubject) {
		this.degreeSubject = degreeSubject;
	}

	public String getDegreeDate() {
		return degreeDate;
	}

	public void setDegreeDate(String degreeDate) {
		this.degreeDate = degreeDate;
	}

	public String getDegreeInstitute() {
		return degreeInstitute;
	}

	public void setDegreeInstitute(String degreeInstitute) {
		this.degreeInstitute = degreeInstitute;
	}

	public String getDegreeExams() {
		return degreeExams;
	}

	public void setDegreeExams(String degreeExams) {
		this.degreeExams = degreeExams;
	}

	public String getSubjects() {
		return subjects;
	}

	public void setSubjects(String subjects) {
		this.subjects = subjects;
	}

	public String getRecapitoName() {
		return recapitoName;
	}

	public void setRecapitoName(String recapitoName) {
		this.recapitoName = recapitoName;
	}

	public String getRecapitoAddress() {
		return recapitoAddress;
	}

	public void setRecapitoAddress(String recapitoAddress) {
		this.recapitoAddress = recapitoAddress;
	}

	public String getRecapitoCAP() {
		return recapitoCAP;
	}

	public void setRecapitoCAP(String recapitoCAP) {
		this.recapitoCAP = recapitoCAP;
	}

	public String getRecapitoTown() {
		return recapitoTown;
	}

	public void setRecapitoTown(String recapitoTown) {
		this.recapitoTown = recapitoTown;
	}

	public String getRecapitoTel() {
		return recapitoTel;
	}

	public void setRecapitoTel(String recapitoTel) {
		this.recapitoTel = recapitoTel;
	}

	public String getRecapitoCell() {
		return recapitoCell;
	}

	public void setRecapitoCell(String recapitoCell) {
		this.recapitoCell = recapitoCell;
	}

	public String getRecapitoEMail() {
		return recapitoEMail;
	}

	public void setRecapitoEMail(String recapitoEMail) {
		this.recapitoEMail = recapitoEMail;
	}

	public Image getSignature() {
		return signature;
	}

	public void setSignature(Image signature) {
		this.signature = signature;
	}

	@Override
	public String toString() {
		return "TeacherDetails [sex=" + sex + ", name=" + name + ", birthTown=" + birthTown + ", birthArea=" + birthArea
				+ ", birthDate=" + birthDate + ", residTown=" + residTown + ", residAddress=" + residAddress
				+ ", militare=" + militare + ", degreeSubject=" + degreeSubject + ", degreeDate=" + degreeDate
				+ ", degreeInstitute=" + degreeInstitute + ", subjects=" + subjects + ", recapitoName=" + recapitoName
				+ ", recapitoAddress=" + recapitoAddress + ", recapitoCAP=" + recapitoCAP + ", recapitoTown="
				+ recapitoTown + ", recapitoTel=" + recapitoTel + ", recapitoCell=" + recapitoCell + ", recapitoEMail="
				+ recapitoEMail + "]";
	}

	public void setFromJsonObj(String pathToJsonFile) throws BadElementException, MalformedURLException, IOException {
		JsonReader reader = Json.createReader(new FileReader(pathToJsonFile));
		JsonObject jso = (JsonObject) reader.read();

		setSex(jso.getString("sex"));
		setName(jso.getString("name"));
		setBirthTown(jso.getString("birthTown"));
		setBirthArea(jso.getString("birthArea"));
		setBirthDate(jso.getString("birthDate"));
		setResidTown(jso.getString("residTown"));
		setResidAddress(jso.getString("residAddress"));
		setMilitare(jso.getString("militare"));
		setDegreeSubject(jso.getString("degreeSubject"));
		setDegreeExams(jso.getString("degreeExams"));
		setDegreeDate(jso.getString("degreeDate"));
		setDegreeInstitute(jso.getString("degreeInstitute"));
		setSubjects(jso.getString("subjects"));
		setRecapitoName(jso.getString("recapitoName"));
		setRecapitoAddress(jso.getString("recapitoAddress"));
		setRecapitoCAP(jso.getString("recapitoCAP"));
		setRecapitoTown(jso.getString("recapitoTown"));
		setRecapitoTel(jso.getString("recapitoTel"));
		setRecapitoCell(jso.getString("recapitoCell"));
		setRecapitoEMail(jso.getString("recapitoEMail"));

		Image signatureImg = Image.getInstance(jso.getString("pathToSignFile"));
		setSignature(signatureImg);

	}

	public void setFromConfigFile(Path path, String signaturePath) throws IOException, BadElementException {

		ArrayList<String> teacherDetailsRaw = new ArrayList<String>(Files.readAllLines(path));
		boolean newLine = false;

		int i = 0;
		for (String rawLine : teacherDetailsRaw) {
			String line = rawLine.trim();
			if ("".equals(line)) {
				i++;
				newLine = true;
			} else if (!"#".equals(line.substring(0, 1))) {
				i++;
				newLine = true;
			}
			if (i == 0 || !newLine) {
				// do nothing!
			} else if (i == 1 && newLine) {
				if ("M".equals(line.toUpperCase()) || "F".equals(line.toUpperCase())) {
					setSex(line.toUpperCase());
					newLine = false;
				} else {
					newLine = false;
					throw new IOException("Invalid 'Sex' identifier [" + line + "] in Teacher Details file!");
				}
			} else if (i == 2 && newLine) {
				setName(line);
				newLine = false;
			} else if (i == 3 && newLine) {
				setBirthTown(line);
				newLine = false;
			} else if (i == 4 && newLine) {
				setBirthArea(line);
				newLine = false;
			} else if (i == 5 && newLine) {
				setBirthDate(line);
				newLine = false;
			} else if (i == 6 && newLine) {
				setResidTown(line);
				newLine = false;
			} else if (i == 7 && newLine) {
				setResidAddress(line);
				newLine = false;
			} else if (i == 8 && newLine) {
				setMilitare(line);
				newLine = false;
			} else if (i == 9 && newLine) {
				setDegreeSubject(line);
				newLine = false;
			} else if (i == 10 && newLine) {
				setDegreeDate(line);
				newLine = false;
			} else if (i == 11 && newLine) {
				setDegreeInstitute(line);
				newLine = false;
			} else if (i == 12 && newLine) {
				setDegreeExams(line);
				newLine = false;
			} else if (i == 13 && newLine) {
				setSubjects(line);
				newLine = false;
			} else if (i == 14 && newLine) {
				setRecapitoName(line);
				newLine = false;
			} else if (i == 15 && newLine) {
				setRecapitoAddress(line);
				newLine = false;
			} else if (i == 16 && newLine) {
				setRecapitoCAP(line);
				newLine = false;
			} else if (i == 17 && newLine) {
				setRecapitoTown(line);
				newLine = false;
			} else if (i == 18 && newLine) {
				setRecapitoTel(line);
				newLine = false;
			} else if (i == 19 && newLine) {
				setRecapitoCell(line);
				newLine = false;
			} else if (i == 20 && newLine) {
				setRecapitoEMail(line);
				newLine = false;
			} else {
				break;
			}
		}

		Image signatureImg = Image.getInstance(signaturePath);
		setSignature(signatureImg);

	}
}
