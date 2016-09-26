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
				if ("--teacherdetails=".equals(s.substring(0, 17))) {
					teacherdetailsarg = true;
					if (Files.exists(Paths.get(s.substring(17)))) {
						teacherJsonPath = s.substring(17);
					} else {
						System.out.println(
								"WARNING!! Teacher details JSON file does not exist!\nUsing default teacher details (are you debugging me?)");
					}
				} else if ("--schools=".equals(s.substring(0, 10))) {
					schoolsarg = true;
					if (Files.exists(Paths.get(s.substring(10)))) {
						teacherJsonPath = s.substring(10);
					} else {
						System.out.println(
								"WARNING!! Schools JSON file does not exist!\nUsing default schools (are you debugging me?)");
					}
				} else if ("--directory=".equals(s.substring(0, 12))) {
					targetDir = s.substring(12);
				} else if ("--pecmaildetails=".equals(s.substring(0, 17))) {
					pecarg = true;
					if (Files.exists(Paths.get(s.substring(17)))) {
						pecJsonPath = s.substring(17);
					} else {
						System.out.println(
								"WARNING!! PEC details JSON file does not exist!\nUsing default PEC details (are you debugging me?)");
					}
				} else if ("--debug".equals(s.substring(0, 7))) {
					debug = true;
				} else if ("--simulate=".equals(s.substring(0, 11))) {
					simulating = true;
					targetMail = s.substring(11);
				} else {
					System.out.println("Invalid argument: " + s + "\nAborting...\n\n");
					printHelp();
					return;
				}
				if (!teacherdetailsarg) {
					System.out.println(
							"WARNING!! Teacher details JSON file not specified!\nUsing default teacher details (are you debugging me?)");
				}
				if (!schoolsarg) {
					System.out.println(
							"WARNING!! Schools JSON file not specified!\nUsing default schools (are you debugging me?)");
				}
				if (!pecarg) {
					System.out.println(
							"WARNING!! PEC details JSON file not specified!\nUsing default PEC details (are you debugging me?)");
				}
			}
		} else

		{
			System.out.println(
					"WARNING!! With no argumnts, the program runs with default params.\nThis makes sense only for debug purposes");
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
				System.out.println("Attempting to send to " + ((JsonObject) jsoSchool).getString("nome"));
				sender.sendMail((JsonObject) jsoSchool, targetMail, simulating);
				System.out.println("Sent!");
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
