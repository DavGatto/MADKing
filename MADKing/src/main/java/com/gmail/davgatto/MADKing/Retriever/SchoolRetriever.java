package com.gmail.davgatto.MADKing.Retriever;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.gmail.davgatto.MADKing.Maker.School;
import com.gmail.davgatto.MADKing.Sender.App;


public class SchoolRetriever {
	
	private static final Logger log4j = LogManager.getLogger(App.class.getName());
	
	public ArrayList<School> getAllSchools(String jsonFiles) throws Exception{
		log4j.debug("getAllSchools called with argument: " + jsonFiles);
		ArrayList<School> ans = new ArrayList<School>();
		File schoolsFile = new File(jsonFiles);
		if(schoolsFile.isFile()){
			ans = getFromFile(schoolsFile);
		} else if(schoolsFile.isDirectory()){
			File[] directoryListing = schoolsFile.listFiles();
			if (directoryListing != null) {
				for (File schools : directoryListing) {
					ArrayList<School> scarr = getFromFile(schools);
					for (School sc : scarr) {
						ans.add(sc);
					}
				}
			} else {
				//log4j.error(App.class.getName() + " Il percorso: " + getSchools() + " non è una directory valida");
				throw new Exception();
			}
		} else {
			throw new Exception();
		}
		return ans;
	}
	
	private ArrayList<School> getFromFile(File f) throws FileNotFoundException{
		JsonReader reader = Json.createReader(new FileReader(f));
		JsonArray jsarr = (JsonArray) reader.read();
		ArrayList<School> ans = new ArrayList<School>();
		for (JsonValue jsv : jsarr) {
			ans.add(new School((JsonObject) jsv));
		}
		return ans;
	}

}
