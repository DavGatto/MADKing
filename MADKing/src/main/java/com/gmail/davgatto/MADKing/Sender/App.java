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
package com.gmail.davgatto.MADKing.Sender;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.gmail.davgatto.MADKing.Maker.Utils;

public class App {

	private static final Logger log4j = LogManager.getLogger(App.class.getName());

	public static int send(String teacherJsonPath, String pecJsonPath, String schoolsJsonPath, String targetDir,
			String simMail, ArrayList<String> unwanted) {

		log4j.debug("MADKing:MADSender: send invoked with arguments: " + teacherJsonPath + ", " + pecJsonPath + ", "
				+ schoolsJsonPath + ", " + targetDir + ", " + simMail);
		InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("etc/application.properties");
		Properties props = new Properties();
		try {
			props.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}

		String targetMail = "";
		boolean simulate = true;
		/**
		 * If simulated-run mail address is empty, targetMail must store the
		 * value of suffix.pecIstruzione, otherwise it must store the
		 * simMailAdderss itself
		 */
		if (simMail.isEmpty()) {
			targetMail = props.get("suffix.pecIstruzione").toString();
			simulate = false;
		} else {
			targetMail = simMail;
		}

		try {
			JsonReader reader = Json.createReader(new FileReader(teacherJsonPath));
			JsonObject jsoTeach = (JsonObject) reader.read();
			reader = Json.createReader(new FileReader(pecJsonPath));
			JsonObject jsoMail = (JsonObject) reader.read();

			reader = Json.createReader(new FileReader(schoolsJsonPath));
			JsonArray jsarr = (JsonArray) reader.read();

			PECSender sender = new PECSender(jsoMail, jsoTeach, targetDir);

			for (JsonValue jsoSchool : jsarr) {
				if (!unwanted.contains(((JsonObject) jsoSchool).getString("tipo"))) {
					log4j.info("Tentativo di invio a " + ((JsonObject) jsoSchool).getString("nome"));
					sender.sendMail((JsonObject) jsoSchool, targetMail, simulate);
					log4j.info("Inviata!");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;

	}

	public static int main(String[] args) {

		log4j.debug("MADKing:MADSender: send invoked with arguments:");
		for (String string : args) {
			log4j.debug(string);
		}

		String targetMail = "";
		String targetDir = "";
		boolean teacherdetailsarg = false;
		String teacherJsonPath = "";
		boolean schoolsarg = false;
		String pecJsonPath = "";
		boolean pecarg = false;
		String schoolsJsonPath = "";
		ArrayList<String> unwanted = new ArrayList<String>();
		// boolean debug = DEBUG;
		if (args.length > 0) {
			for (String s : args) {
				// if ("--help".equals(s.substring(0, 6))) {
				// printHelp();
				// return 0;
				// }
				if (s.startsWith("--teacherdetails=")) {
					teacherdetailsarg = true;
					teacherJsonPath = s.substring(17);
					if (Files.exists(Paths.get(teacherJsonPath))) {
						log4j.info("MADKing:MADSender: Teacher details found at: " + teacherJsonPath);
					} else {
						log4j.error("MADKing:MADSender: ERROR! Couldn't find Teacher details JSON file at: "
								+ teacherJsonPath + "\nAborting...");
						return -1;
					}
				} else if (s.startsWith("--schools=")) {
					schoolsarg = true;
					schoolsJsonPath = s.substring(10);
					if (Files.exists(Paths.get(schoolsJsonPath))) {
						log4j.info("MADKing:MADSender: School list found at: " + schoolsJsonPath);
					} else {
						log4j.error("MADKing:MADSender: ERROR!! Couldn't find schools JSON file at: " + schoolsJsonPath
								+ "\nAborting...");
						return -1;
					}
				} else if (s.startsWith("--unwanted=")) {
					String unwantedString = s.substring(11);
					log4j.debug("unwantedString set to:\n" + unwantedString);
					unwanted = new ArrayList<String>(Arrays.asList(unwantedString.split("\\|")));
					log4j.debug("unwanted set to:\n" + unwanted.toString());
				} else if (s.startsWith("--directory=")) {
					targetDir = s.substring(12);
					log4j.info("MADKing:MADSender: MAD files directory set as " + targetDir);
				} else if (s.startsWith("--pecmaildetails=")) {
					pecarg = true;
					pecJsonPath = s.substring(17);
					if (Files.exists(Paths.get(pecJsonPath))) {
						log4j.info("MADKing:MADSender: PEC details found at " + pecJsonPath);
						if (Utils.IS_NOT_UTF8) {
							log4j.info("PEC JSON file encoding appears to be not UTF-8. Rewriting it as such...");
							try {
								Utils.encodeFile(pecJsonPath);
							} catch (IOException e) {
								log4j.error("ERROR while encoding file: " + pecJsonPath);
								e.printStackTrace();
							}
						}
					} else {
						log4j.error("MADKing:MADSender: ERROR!! Couldn't find PEC details JSON file at " + pecJsonPath
								+ "\nAborting...");
						return -1;
					}
				} else if (s.startsWith("--as=")) {
					// Do nothing
				} else if (s.startsWith("--simulate=")) {
					targetMail = s.substring(11);
					log4j.info("MADKing:MADSender: Simulated run -- actually sending to " + targetMail);
				} else {
					log4j.error("MADKing:MADSender: Invalid argument:" + s + "\nAborting...\n\n");
					// printHelp();
					return -1;
				}
			}
			if (!teacherdetailsarg) {
				log4j.error("MADKing:MADSender: ERROR!! Teacher details JSON file not specified!\nAborting...");
				// printHelp();
				return -1;
			}
			if (!schoolsarg) {
				log4j.error("MADKing:MADSender: ERROR!! Schools JSON file not specified!\nAborting...");
				// printHelp();
				return -1;
			}
			if (!pecarg) {
				log4j.error("MADKing:MADSender: ERROR!! PEC details JSON file not specified!\nAborting...");
				// printHelp();
				return -1;
			}
		}else

	{
		log4j.error("MADKing:MADSender: ERROR!! Sender won't run with no arguments\n\n");
		// printHelp();
		return -1;
	}

	send(teacherJsonPath, pecJsonPath, schoolsJsonPath, targetDir, targetMail, unwanted);

		// try {
		// JsonReader reader = Json.createReader(new
		// FileReader(teacherJsonPath));
		// JsonObject jsoTeach = (JsonObject) reader.read();
		// reader = Json.createReader(new FileReader(pecJsonPath));
		// JsonObject jsoMail = (JsonObject) reader.read();
		//
		// reader = Json.createReader(new FileReader(schoolsJsonPath));
		// JsonArray jsarr = (JsonArray) reader.read();
		//
		// PECSender sender = new PECSender(jsoMail, jsoTeach, targetDir);
		//
		// for (JsonValue jsoSchool : jsarr) {
		// log4j.info("MADKing:MADSender: Attempting to send to " +
		// ((JsonObject) jsoSchool).getString("nome"));
		// sender.sendMail((JsonObject) jsoSchool, targetMail, simulating);
		// log4j.info("MADKing:MADSender: Sent!");
		// }
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		return 0;

	}

	// private static void printHelp() {
	// System.out.print("MADKing Sender -- Usage:\n" + "madkingsender [PARAMS]
	// [OPTIONS]\n\n"
	// + "send\t\tSend the PEC mails\n" + "simulate" + "PARAMS:\n"
	// + "--teacherdetails=PATH\t\tPath to the JSON file containing teacher's
	// details\n"
	// + "--pecmaildetails=PATH\t\tPath to the JSON file containing PEC mail
	// details\n"
	// + "--schools=PATH\t\tPath to the JSON file containing school list\n\n"
	// + "--directory=PATH\t\tdirectory of the MADMAker generated pdf files\n" +
	// "OPTIONS:\n"
	// + "--simulate=EXAMPLE@MAILADDRESS.COM\t\tAvoid sending real PEC, send to
	// alternate email instead\n"
	// );
	// }
}
