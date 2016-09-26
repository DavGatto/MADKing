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

	public final static String BLANK_MAD_MODEL_PATH = "./configFiles/ModelloMAD.pdf";
	public final static String TARGET_FOLDER_PATH = "MADKing-ModuliCompilati";
	public final static String TEACHER_DETAILS_JSON_PATH = "./JSONFiles/teacherDetails.json";
	public final static String SCHOOLS_DETAILS_JSON_PATH = "./JSONFiles/schoolsDetails.json";

	public static void main(String[] args) {

		String targetDir = TARGET_FOLDER_PATH;
		boolean teacherdetailsarg = false;
		String teacherJsonPath = TEACHER_DETAILS_JSON_PATH;
		boolean schoolsarg = false;
		String schoolsJsonPath = SCHOOLS_DETAILS_JSON_PATH;
		if (args.length > 0) {
			for (String s : args) {
				if ("--help".equals(s.substring(0, 6))) {
					printHelp();
					return;
				}
				if (s.startsWith("--teacherdetails=")) {
					teacherdetailsarg = true;
					if (Files.exists(Paths.get(s.substring(17)))) {
						teacherJsonPath = s.substring(17);
						System.out.println("MADking:MADMaker: Teacher details found at: " + teacherJsonPath);
					} else {
						System.out.println(
								"MADking:MADMaker: WARNING!! Teacher details JSON file does not exist!\nUsing default teacher details (are you debugging me?)");
					}
				} else if (s.startsWith("--schools=")) {
					schoolsarg = true;
					if (Files.exists(Paths.get(s.substring(10)))) {
						schoolsJsonPath = s.substring(10);
						System.out.println("MADking:MADMaker: School list found at: " + schoolsJsonPath);
					} else {
						System.out.println(
								"MADking:MADMaker: WARNING!! Schools JSON file does not exist!\nUsing default schools (are you debugging me?)");
					}
				} else if (s.startsWith("--directory=")) {
					targetDir = s.substring(12);
					System.out.println("MADking:MADMaker: Output directory set as " + targetDir);
				} else {
					System.out.println("MADking:MADMaker: Invalid argument: " + s + "\nAborting...\n\n");
					printHelp();
					return;
				}
				
			}
			if (!teacherdetailsarg) {
				System.out.println(
						"MADking:MADMaker: WARNING!! Teacher details JSON file not specified!\nUsing default teacher details (are you debugging me?)");
			}
			if (!schoolsarg) {
				System.out.println(
						"MADking:MADMaker: WARNING!! Schools JSON file not specified!\nUsing default schools (are you debugging me?)");

			}
		} else {
			System.out.println(
					"MADking:MADMaker: WARNING!! With no argumnts, the program runs with default params.\nThis makes sense only for debug purposes");
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
			teacherSpecificMADPath += teacherDetails.getName().replaceAll("\\s|\'", "") + "_MAD.pdf";
		} catch (IOException e) {
			System.out.println("MADking:MADMaker: teacherDetails.json not found");
			e.printStackTrace();
		} catch (BadElementException e) {
			System.out.println("MADking:MADMaker: teacherDetails.json could not be read properly");
			e.printStackTrace();
		}

		try {
			TeacherSpecificMADMaker.generateTeacherSpecificMADFile(teacherDetails, teacherSpecificMADPath,
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

	}

	private static void printHelp() {
		System.out.print("MADKing Maker -- Usage:\n" + "madkingmaker [PARAMS] [OPTIONS]\n\n" + "PARAMS:\n"
				+ "--teacherdetails\t\tPath to the JSON file containing teacher's details\n"
				+ "--schools\t\tPath to the JSON file containing school list\n\n" + "OPTIONS:\n"
				+ "--directory\t\tAlternative directory for the generated pdf files");
	}
}
