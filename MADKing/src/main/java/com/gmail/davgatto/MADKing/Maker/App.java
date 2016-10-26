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
package com.gmail.davgatto.MADKing.Maker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.DocumentException;

/**
 * Questo programma genera moduli personalizzati di MAD (messa a disposizione
 * per le supplenze) Released under GPL licence
 * 
 * @author Davide Gatto -davgatto@gmail.com
 */
public class App {

	public final static String BLANK_MAD_MODEL_PATH = "/ModelloMAD.pdf";
	public final static String TARGET_FOLDER_PATH = "MADKing-ModuliCompilati";


	public static int makeMad(String[] args) {

		String targetDir = TARGET_FOLDER_PATH;
		boolean teacherdetailsarg = false;
		String teacherJsonPath = "";
		boolean schoolsarg = false;
		String schoolsJsonPath = "";
		String annoScolastico = "";

		if (args.length > 0) {
			for (String s : args) {
				if ("--help".equals(s.substring(0, 6))) {
					printHelp();
					return 0;
				}
				if (s.startsWith("--teacherdetails=")) {
					teacherdetailsarg = true;
					teacherJsonPath = s.substring(17);
					if (Files.exists(Paths.get(teacherJsonPath))) {
						System.out.println("MADking:MADMaker: Teacher details found at: " + teacherJsonPath);
					} else {
						System.out.println("MADKing:MADMaker: ERROR! Couldn't find Teacher details JSON file at: "
								+ teacherJsonPath + "\nAborting...");
						return -1;
					}
				} else if (s.startsWith("--as=")) {
					annoScolastico = s.substring(5);
				} else if (s.startsWith("--schools=")) {
					schoolsarg = true;
					schoolsJsonPath = s.substring(10);
					if (Files.exists(Paths.get(schoolsJsonPath))) {
						System.out.println("MADking:MADMaker: School list found at: " + schoolsJsonPath);
					} else {
						System.out.println("MADKing:MADMaker: ERROR!! Couldn't find schools JSON file at: "
								+ schoolsJsonPath + "\nAborting...");
						return -1;
						}
				} else if (s.startsWith("--directory=")) {
					targetDir = s.substring(12);
					System.out.println("MADking:MADMaker: Output directory set as " + targetDir);
				} else if (s.startsWith("--pecmaildetails=") || s.startsWith("--debug")) {
					// Do nothing
				} else {
					System.out.println("MADking:MADMaker: Invalid argument: " + s + "\nAborting...\n\n");
					printHelp();
					return -1;
				}
				
			}
			if (!teacherdetailsarg) {
				System.out.println("MADKing:MADMaker: ERROR!! Teacher details JSON file not specified!\nAborting...");
				printHelp();
				return -1;
			}
			if (!schoolsarg) {
				System.out.println("MADKing:MADMaker: ERROR!! Schools JSON file not specified!\nAborting...");
				printHelp();
				return -1;
			}
		} else {
			System.out.println(
					"MADking:MADMaker: WARNING!! Maker won't run with no arguments");
			printHelp();
			return -1;
		}

		try {
			Files.createDirectories(Paths.get(targetDir));
		} catch (IOException e1) {
			System.out.println("MADking:MADMaker: could not create directory " + targetDir);
			e1.printStackTrace();
		}

		TeacherDetails teacherDetails = new TeacherDetails();
		String teacherSpecificMADPath = targetDir + "/";

		try {
			teacherDetails.setFromJsonObj(teacherJsonPath);
			teacherSpecificMADPath += teacherDetails.getRecapitoName().replaceAll("\\s|\'", "") + "_MAD.pdf";
		} catch (IOException e) {
			System.out.println("MADking:MADMaker: teacherDetails.json not found");
			e.printStackTrace();
		} catch (BadElementException e) {
			System.out.println("MADking:MADMaker: teacherDetails.json could not be read properly");
			e.printStackTrace();
		}

		try {
			TeacherSpecificMADMaker.generateTeacherSpecificMADFile(teacherDetails, annoScolastico, teacherSpecificMADPath,
					BLANK_MAD_MODEL_PATH);
			System.out.println("MADking:MADMaker: Teacher-specific MAD file created!");
		} catch (IOException e) {
			System.out.println("MADking:MADMaker: IOExceprion occurred while generating teacher-specific MAD pdf file");
			e.printStackTrace();
		} catch (DocumentException e) {
			System.out.println("MADking:MADMaker: DocumentException occurred while generating teacher-specific MAD pdf file");
			e.printStackTrace();
		}

		try {
			SchoolSpecificMADMaker.generateSchoolSpecificMADFiles(schoolsJsonPath, teacherSpecificMADPath);
			System.out.println("MADking:MADMaker: School-specific MAD files created!");
		} catch (DocumentException e) {
			System.out.println("MADking:MADMaker: DocumentException occurred while generating school-specific MAD pdf file");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("MADking:MADMaker: IOExceprion occurred while generating school-specific MAD pdf file");
			e.printStackTrace();
		}

		
		return 0;
	}

	private static void printHelp() {
		System.out.print("MADKing Maker -- Usage:\n" + "madkingmaker [PARAMS] [OPTIONS]\n\n" + "PARAMS:\n"
				+ "--teacherdetails\t\tPath to the JSON file containing teacher's details\n"
				+ "--schools\t\tPath to the JSON file containing school list\n\n" + "OPTIONS:\n"
				+ "--as\t\tScholastic Year\n"
				+ "--directory\t\tAlternative directory for the generated pdf files");
	}
	
}
