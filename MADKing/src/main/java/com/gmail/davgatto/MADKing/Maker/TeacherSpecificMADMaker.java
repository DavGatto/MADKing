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

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import com.itextpdf.text.Image;

public final class TeacherSpecificMADMaker {

	public static void generateTeacherSpecificMADFile(TeacherDetails td, String anno_scolastico, String targetPath, String modelloMADPath)
			throws IOException, DocumentException {

		boolean female = false;

		if ("F".equals(td.getSex())) {
			female = true;
		}

		PdfReader reader = new PdfReader(modelloMADPath);

		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(targetPath)); // output

		BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED); // set-font

		PdfContentByte over = stamper.getOverContent(1);

		// write text
		over.beginText();
		over.setFontAndSize(bf, 10); // set font and size

		over.setTextMatrix(450, 645);
		over.showText(anno_scolastico);
		
		over.setTextMatrix(152, 615);
		over.showText(td.getName());

		if (female) {
			over.setTextMatrix(70, 615);
			over.showText("a");

			over.setTextMatrix(137, 615);
			over.showText("a");

			over.setTextMatrix(72, 602);
			over.showText("a");
		} else {
			over.setTextMatrix(56, 615);
			over.showText("I");

			over.setTextMatrix(137, 615);
			over.showText("o");

			over.setTextMatrix(72, 602);
			over.showText("o");
		}

		over.setTextMatrix(103, 602); // set x,y position (0,0 is at the bottom
										// left)
		over.showText(td.getBirthTown()); // set text

		over.setTextMatrix(345, 602); // set x,y position (0,0 is at the bottom
										// left)
		over.showText(td.getBirthArea()); // set text

		over.setTextMatrix(400, 602);
		over.showText(td.getBirthDate());

		over.setTextMatrix(73, 591);
		over.showText(td.getResidTown());

		over.setTextMatrix(307, 591);
		over.showText(td.getResidAddress());

		over.setTextMatrix(58, 467);
		over.showText(td.getMilitare());

		if (td.getDegreeSubject().length() <= 53) {
			over.setTextMatrix(280, 444);
			over.showText(td.getDegreeSubject());
		} else {
			over.setTextMatrix(280, 444);
			over.showText(td.getDegreeSubject().substring(0,53));
			over.setTextMatrix(58, 431);
			over.showText(td.getDegreeSubject().substring(53));
		}
		
		over.setTextMatrix(400, 431);
		over.showText(td.getDegreeDate());
		
		over.setTextMatrix(98, 420);
		over.showText(td.getDegreeInstitute());
		
		if (td.getDegreeExams().length() <= 100) {
			over.setTextMatrix(58, 396);
			over.showText(td.getDegreeExams());
		} else {
			over.setTextMatrix(419, 408);
			over.showText(td.getDegreeExams().substring(0,20));
			over.setTextMatrix(56, 396);
			over.showText(td.getDegreeExams().substring(20,110));
			over.setTextMatrix(56, 385);
			over.showText(td.getDegreeExams().substring(110));
		}
		
		if (td.getSubjects().length() <= 85) {
			over.setTextMatrix(58, 276);
			over.showText(td.getSubjects());
		} else {
			over.setTextMatrix(58, 276);
			over.showText(td.getSubjects().substring(0,85));
			over.setTextMatrix(56, 266);
			over.showText(td.getSubjects().substring(85));
		}
		
		over.setTextMatrix(58, 163);
		over.showText(td.getRecapitoName());
		
		over.setTextMatrix(75, 143);
		over.showText(td.getRecapitoAddress());
		
		over.setTextMatrix(83, 125);
		over.showText(td.getRecapitoCAP());
		
		over.setTextMatrix(190, 125);
		over.showText(td.getRecapitoTown());
		
		over.setTextMatrix(80, 107);
		over.showText(td.getRecapitoTel());
		
		over.setTextMatrix(215, 107);
		over.showText(td.getRecapitoCell());

		over.setTextMatrix(100, 90);
		over.showText(td.getRecapitoEMail());

		bf = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
		over.setFontAndSize(bf, 10);
		
		over.setTextMatrix(58, 509);
		over.showText("X");

		over.setTextMatrix(58, 496);
		over.showText("X");

		over.setTextMatrix(58, 481);
		over.showText("X");

		over.setTextMatrix(58, 446);
		over.showText("X");

		over.setTextMatrix(58, 362);
		over.showText("X");

		over.setTextMatrix(58, 325);
		over.showText("X");

		over.setTextMatrix(58, 301);
		over.showText("X");
		
		over.endText();
		
		Image signature = td.getSignature();
		signature.scaleToFit(250, 80);
        signature.setAbsolutePosition(320,190);
        over.addImage(signature);

		stamper.close();

	}

}
