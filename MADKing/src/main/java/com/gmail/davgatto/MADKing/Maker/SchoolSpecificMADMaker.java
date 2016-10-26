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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class SchoolSpecificMADMaker {

	private static ArrayList<School> getSchoolsFromJsonFile(String pathToJsonFile) throws FileNotFoundException {

		ArrayList<School> schools = new ArrayList<School>();

		JsonReader reader = Json.createReader(new FileReader(pathToJsonFile));
		JsonArray jsarr = (JsonArray) reader.read();
		for (JsonValue jsV : jsarr) {
			schools.add(new School((JsonObject) jsV));
		}

		return schools;

	}

	public static void generateSchoolSpecificMADFiles(String jsonSchoolsFilePath, String teacherSpecificMADPath)
			throws DocumentException, IOException {

		ArrayList<School> schools = getSchoolsFromJsonFile(jsonSchoolsFilePath);

		for (School school : schools) {

			PdfReader reader = new PdfReader(teacherSpecificMADPath);

			String targetFilePath = teacherSpecificMADPath.replaceAll("_MAD.pdf",
					"_" + school.getNome().replaceAll("\\s|\"|\'", "") + "-" + school.getCodMec() + "_MAD.pdf");

			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(targetFilePath)); // output

			BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED); // set-font

			PdfContentByte over = stamper.getOverContent(1);

			// write text
			over.beginText();
			over.setFontAndSize(bf, 10); // set font and size

			over.setTextMatrix(305, 704);
			over.showText(school.getNome());

			over.setTextMatrix(305, 686);
			over.showText(school.getComune() + ", " + school.getProvincia());

			LocalDate date = LocalDate.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			String dateStamp = date.format(formatter);

			over.setTextMatrix(55, 215);
			over.showText(dateStamp);
			over.endText();

			stamper.close();
		}
	}

}
