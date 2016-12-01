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
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.DocumentException;

public class App {

	private static final Logger log4j = LogManager.getLogger(App.class.getName());

	//public final static String BLANK_MAD_MODEL_PATH = "${'model.blankMADpdf'}"; //"/ModelloMAD.pdf";
	//public final static String TARGET_FOLDER_PATH = "MADKing-ModuliCompilati";
	public final static String FILE_SEPARATOR = System.getProperty("file.separator");



	public static int makeMad(String[] args) {
		
		log4j.info("MADking:MADMaker: makeMad invoked");
		
		InputStream is = Thread.currentThread().getContextClassLoader().
			    getResourceAsStream("etc/application.properties");
		Properties props = new Properties();
		try {
			props.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String blankMadModelPath = props.get("model.blankMADpdf").toString();
		
		String targetDir = props.get("default.targetDirectory").toString();
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
						log4j.info("MADking:MADMaker: Teacher details found at: " + teacherJsonPath);
//						if(Utils.IS_NOT_UTF8){
//							log4j.info("Teacher JSON file encoding appears to be not UTF-8. Rewriting it as such...");
//							try {
//								Utils.encodeFile(teacherJsonPath);
//							} catch (IOException e) {
//								log4j.error("ERROR while encoding file: "+ teacherJsonPath);
//								e.printStackTrace();
//							}
//						}
					} else {
						log4j.error("MADKing:MADMaker: ERROR! Couldn't find Teacher details JSON file at: "
								+ teacherJsonPath + "\nAborting...");
						return -1;
					}
				} else if (s.startsWith("--as=")) {
					annoScolastico = s.substring(5);
				} else if (s.startsWith("--schools=")) {
					schoolsarg = true;
					schoolsJsonPath = s.substring(10);
					if (Files.exists(Paths.get(schoolsJsonPath))) {
						log4j.info("MADking:MADMaker: School list found at: " + schoolsJsonPath);
//						if(Utils.IS_NOT_UTF8){
//							log4j.info("Schools JSON file encoding appears to be not UTF-8. Rewriting it as such...");
//							try {
//								Utils.encodeFile(schoolsJsonPath);
//							} catch (IOException e) {
//								log4j.error("ERROR while encoding file: "+ schoolsJsonPath);
//								e.printStackTrace();
//							}
//						}
					} else {
						log4j.error("MADKing:MADMaker: ERROR!! Couldn't find schools JSON file at: "
								+ schoolsJsonPath + "\nAborting...");
						return -1;
						}
				} else if (s.startsWith("--directory=")) {
					targetDir = s.substring(12);
					log4j.info("MADking:MADMaker: Output directory set as " + targetDir);
				} else if (s.startsWith("--pecmaildetails=") || s.startsWith("--simulate=")) {
					// Do nothing
				} else {
					log4j.error("MADking:MADMaker: Invalid argument: " + s + "\nAborting...\n\n");
					printHelp();
					return -1;
				}
				
			}
			if (!teacherdetailsarg) {
				log4j.error("MADKing:MADMaker: ERROR!! Teacher details JSON file not specified!\nAborting...");
				printHelp();
				return -1;
			}
			if (!schoolsarg) {
				log4j.error("MADKing:MADMaker: ERROR!! Schools JSON file not specified!\nAborting...");
				printHelp();
				return -1;
			}
		} else {
			log4j.error(
					"MADking:MADMaker: WARNING!! Maker won't run with no arguments");
			printHelp();
			return -1;
		}

		try {
			Files.createDirectories(Paths.get(targetDir));
		} catch (IOException e1) {
			log4j.error("MADking:MADMaker: could not create directory " + targetDir);
			e1.printStackTrace();
		}

		TeacherDetails teacherDetails = new TeacherDetails();
		String teacherSpecificMADPath = targetDir + FILE_SEPARATOR;

		try {
			teacherDetails.setFromJsonObj(teacherJsonPath);
			teacherSpecificMADPath += teacherDetails.getRecapitoName().replaceAll("\\s|\'", "") + "_MAD.pdf";
		} catch (IOException e) {
			log4j.error("MADking:MADMaker: teacherDetails.json not found");
			e.printStackTrace();
		} catch (BadElementException e) {
			log4j.error("MADking:MADMaker: teacherDetails.json could not be read properly");
			e.printStackTrace();
		}

		try {
			TeacherSpecificMADMaker.generateTeacherSpecificMADFile(teacherDetails, annoScolastico, teacherSpecificMADPath,
					blankMadModelPath);
			log4j.trace("MADking:MADMaker: Teacher-specific MAD file created!");
		} catch (IOException e) {
			log4j.error("MADking:MADMaker: IOExceprion occurred while generating teacher-specific MAD pdf file");
			e.printStackTrace();
		} catch (DocumentException e) {
			log4j.error("MADking:MADMaker: DocumentException occurred while generating teacher-specific MAD pdf file");
			e.printStackTrace();
		}

		try {
			SchoolSpecificMADMaker.generateSchoolSpecificMADFiles(schoolsJsonPath, teacherSpecificMADPath);
			log4j.trace("MADking:MADMaker: School-specific MAD files created!");
		} catch (DocumentException e) {
			log4j.error("MADking:MADMaker: DocumentException occurred while generating school-specific MAD pdf file");
			e.printStackTrace();
		} catch (IOException e) {
			log4j.error("MADking:MADMaker: IOExceprion occurred while generating school-specific MAD pdf file");
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
