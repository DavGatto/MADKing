package com.gmail.davgatto.MADKing.Sender;

import java.io.FileReader;
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
	// private final static String PEC_ISTRUZIONE = "@pec.istruzione.it";
	private final static String PEC_ISTRUZIONE = "@fakefake.it";
	private final static boolean DEBUG = true;


	public static void main(String[] args) {
		
 
		
		try{
		JsonReader reader = Json.createReader(new FileReader(TEACHER_DETAILS_JSON_PATH));
		JsonObject jsoTeach = (JsonObject) reader.read();
		reader = Json.createReader(new FileReader(PEC_MAIL_PARAMS_JSON_PATH));
		JsonObject jsoMail = (JsonObject) reader.read();
		
	

		reader = Json.createReader(new FileReader(SCHOOLS_DETAILS_JSON_PATH));
		JsonArray jsarr = (JsonArray) reader.read();
		
		
		PECSender sender = new PECSender(jsoMail, jsoTeach, TARGET_FOLDER_PATH, DEBUG);
		
		for (JsonValue jsoSchool : jsarr) {
			System.out.println("Attempting to send to " + ((JsonObject) jsoSchool).getString("nome"));
			sender.sendMail((JsonObject) jsoSchool, PEC_ISTRUZIONE);
			System.out.println("Sent!");
		}
		
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		

	}
}
