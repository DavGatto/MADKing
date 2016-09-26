package com.gmail.davgatto.MADKing.Sender;

import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

public class App {

	public final static String TARGET_FOLDER_PATH = "/home/dave/Desktop/MADKingMade";
	public final static String TEACHER_DETAILS_JSON_PATH = "./JSONFiles/teacherDetails.json";
	public final static String SCHOOLS_DETAILS_JSON_PATH = "./JSONFiles/schoolsDetails.json";
	public final static String PEC_MAIL_PARAMS_JSON_PATH = "./JSONFiles/pecMailParams.json";
	private final static String PEC_ISTRUZIONE = "@pec.istruzione.it";
	private final static boolean DEBUG = false;

	public static void main(String[] args) {

		String targetDir = TARGET_FOLDER_PATH;
		boolean teacherdetailsarg = false;
		String teacherJsonPath = TEACHER_DETAILS_JSON_PATH;
		boolean schoolsarg = false;
		String pecJsonPath = PEC_MAIL_PARAMS_JSON_PATH;
		boolean pecarg = false;
		String schoolsJsonPath = SCHOOLS_DETAILS_JSON_PATH;
		boolean debug = DEBUG;
		String targetMail = PEC_ISTRUZIONE;
		boolean simulating = false;
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
						System.out.println("MADKing:MADSender: Teacher details found at: " + teacherJsonPath);
					} else {
						System.out.println(
								"MADKing:MADSender: WARNING!! Teacher details JSON file does not exist!\nUsing default teacher details (are you debugging me?)");
					}
				} else if (s.startsWith("--schools=")) {
					schoolsarg = true;
					if (Files.exists(Paths.get(s.substring(10)))) {
						schoolsJsonPath = s.substring(10);
						System.out.println("MADKing:MADSender: School list found at: " + schoolsJsonPath);
					} else {
						System.out.println(
								"MADKing:MADSender: WARNING!! Schools JSON file does not exist!\nUsing default schools (are you debugging me?)");
					}
				} else if (s.startsWith("--directory=")) {
					targetDir = s.substring(12);
					System.out.println("MADKing:MADSender: MAD files directory set as " + targetDir);
				} else if (s.startsWith("--pecmaildetails=")) {
					pecarg = true;
					if (Files.exists(Paths.get(s.substring(17)))) {
						pecJsonPath = s.substring(17);
						System.out.println("MADKing:MADSender: PEC details found at " + pecJsonPath);
					} else {
						System.out.println(
								"MADKing:MADSender: WARNING!! PEC details JSON file does not exist!\nUsing default PEC details (are you debugging me?)");
					}
				} else if (s.startsWith("--debug")) {
					debug = true;
					System.out.println("MADKing:MADSender: §§§§§§§§§§ Debug mode §§§§§§§§§§");
				} else if (s.startsWith("--simulate=")) {
					simulating = true;
					targetMail = s.substring(11);
					System.out.println("MADKing:MADSender: Simulated run -- actually sending to " + targetMail);
				} else {
					System.out.println("MADKing:MADSender: Invalid argument: " + s + "\nAborting...\n\n");
					printHelp();
					return;
				}
			}
			if (!teacherdetailsarg) {
				System.out.println(
						"MADKing:MADSender: WARNING!! Teacher details JSON file not specified!\nUsing default teacher details (are you debugging me?)");
			}
			if (!schoolsarg) {
				System.out.println(
						"MADKing:MADSender: WARNING!! Schools JSON file not specified!\nUsing default schools (are you debugging me?)");
			}
			if (!pecarg) {
				System.out.println(
						"MADKing:MADSender: WARNING!! PEC details JSON file not specified!\nUsing default PEC details (are you debugging me?)");
			}
		} else
		{
			System.out.println(
					"MADKing:MADSender: WARNING!! With no argumnts, the program runs with default params.\nThis makes sense only for debug purposes");
		}

		try {
			JsonReader reader = Json.createReader(new FileReader(teacherJsonPath));
			JsonObject jsoTeach = (JsonObject) reader.read();
			reader = Json.createReader(new FileReader(pecJsonPath));
			JsonObject jsoMail = (JsonObject) reader.read();

			reader = Json.createReader(new FileReader(schoolsJsonPath));
			JsonArray jsarr = (JsonArray) reader.read();

			PECSender sender = new PECSender(jsoMail, jsoTeach, targetDir, debug);

			for (JsonValue jsoSchool : jsarr) {
				System.out.println(
						"MADKing:MADSender: Attempting to send to " + ((JsonObject) jsoSchool).getString("nome"));
				sender.sendMail((JsonObject) jsoSchool, targetMail, simulating);
				System.out.println("MADKing:MADSender: Sent!");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void printHelp() {
		System.out.print("MADKing Sender -- Usage:\n" + "madkingsender [PARAMS] [OPTIONS]\n\n"
				+ "send\t\tSend the PEC mails\n" + "simulate" + "PARAMS:\n"
				+ "--teacherdetails=PATH\t\tPath to the JSON file containing teacher's details\n"
				+ "--pecmaildetails=PATH\t\tPath to the JSON file containing PEC mail details\n"
				+ "--schools=PATH\t\tPath to the JSON file containing school list\n\n" + "OPTIONS:\n"
				+ "--directory=PATH\t\tAlternative directory for the generated pdf files\n"
				+ "--simulate=EXAMPLE@MAILADDRESS.COM\t\tAvoid sending real PEC, send to alternate email instead\n"
				+ "--debug\t\tPrint debug messages\n");
	}
}
