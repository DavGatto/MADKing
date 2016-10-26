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
	private final static String PEC_ISTRUZIONE = "@pec.istruzione.it";
	private final static boolean DEBUG = false;

	public static int send(String[] args) {

		String targetDir = TARGET_FOLDER_PATH;
		boolean teacherdetailsarg = false;
		String teacherJsonPath = "";
		boolean schoolsarg = false;
		String pecJsonPath = "";
		boolean pecarg = false;
		String schoolsJsonPath = "";
		boolean debug = DEBUG;
		String targetMail = PEC_ISTRUZIONE;
		boolean simulating = false;
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
						System.out.println("MADKing:MADSender: Teacher details found at: " + teacherJsonPath);
					} else {
						System.out.println("MADKing:MADSender: ERROR! Couldn't find Teacher details JSON file at: "
								+ teacherJsonPath + "\nAborting...");
						return -1;
					}
				} else if (s.startsWith("--schools=")) {
					schoolsarg = true;
					schoolsJsonPath = s.substring(10);
					if (Files.exists(Paths.get(schoolsJsonPath))) {
						System.out.println("MADKing:MADSender: School list found at: " + schoolsJsonPath);
					} else {
						System.out.println("MADKing:MADSender: ERROR!! Couldn't find schools JSON file at: "
								+ schoolsJsonPath + "\nAborting...");
						return -1;
					}
				} else if (s.startsWith("--directory=")) {
					targetDir = s.substring(12);
					System.out.println("MADKing:MADSender: MAD files directory set as " + targetDir);
				} else if (s.startsWith("--pecmaildetails=")) {
					pecarg = true;
					pecJsonPath = s.substring(17);
					if (Files.exists(Paths.get(pecJsonPath))) {
						System.out.println("MADKing:MADSender: PEC details found at " + pecJsonPath);
					} else {
						System.out.println("MADKing:MADSender: ERROR!! Couldn't find PEC details JSON file at "
								+ pecJsonPath + "\nAborting...");
						return -1;
					}
				} else if (s.startsWith("--debug")) {
					debug = true;
					System.out.println("MADKing:MADSender: §§§§§§§§§§ Debug mode §§§§§§§§§§");
				} else if (s.startsWith("--as=")) {
					// Do nothing
				} else if (s.startsWith("--simulate=")) {
					simulating = true;
					targetMail = s.substring(11);
					System.out.println("MADKing:MADSender: Simulated run -- actually sending to " + targetMail);
				} else {
					System.out.println("MADKing:MADSender: Invalid argument:" + s + "\nAborting...\n\n");
					printHelp();
					return -1;
				}
			}
			if (!teacherdetailsarg) {
				System.out.println("MADKing:MADSender: ERROR!! Teacher details JSON file not specified!\nAborting...");
				printHelp();
				return -1;
			}
			if (!schoolsarg) {
				System.out.println("MADKing:MADSender: ERROR!! Schools JSON file not specified!\nAborting...");
				printHelp();
				return -1;
			}
			if (!pecarg) {
				System.out.println("MADKing:MADSender: ERROR!! PEC details JSON file not specified!\nAborting...");
				printHelp();
				return -1;
			}
		} else {
			System.out.println("MADKing:MADSender: ERROR!! Sender won't run with no arguments\n\n");
			printHelp();
			return -1;
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

		return 0;

	}

	private static void printHelp() {
		System.out.print("MADKing Sender -- Usage:\n" + "madkingsender [PARAMS] [OPTIONS]\n\n"
				+ "send\t\tSend the PEC mails\n" + "simulate" + "PARAMS:\n"
				+ "--teacherdetails=PATH\t\tPath to the JSON file containing teacher's details\n"
				+ "--pecmaildetails=PATH\t\tPath to the JSON file containing PEC mail details\n"
				+ "--schools=PATH\t\tPath to the JSON file containing school list\n\n"
				+ "--directory=PATH\t\tdirectory of the MADMAker generated pdf files\n" + "OPTIONS:\n"
				+ "--simulate=EXAMPLE@MAILADDRESS.COM\t\tAvoid sending real PEC, send to alternate email instead\n"
				+ "--debug\t\tPrint debug messages\n");
	}
}
