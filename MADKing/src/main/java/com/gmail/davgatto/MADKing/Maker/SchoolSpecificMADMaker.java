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
