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
	
	public static ArrayList<String> getAllValuesForField(String field, String jsonFiles) throws IllegalArgumentException, FileNotFoundException{
		log4j.debug(SchoolRetriever.class.getName() + " getAllValuesForField called with arguments: " + field + ", " + jsonFiles);
		ArrayList<School> schools = getAllSchools(jsonFiles);
		ArrayList<String> ans = new ArrayList<String>();
		
			if("codMec".equals(field)){
				for (School s : schools) {
					if(!ans.contains(s.getCodMec())){
						ans.add(s.getCodMec());
					}
				}
			} else if("nome".equals(field)){
				for (School s : schools) {
					if(!ans.contains(s.getNome())){
						ans.add(s.getNome());
					}
				}
			} else if("comune".equals(field)){
				for (School s : schools) {
					if(!ans.contains(s.getComune())){
						ans.add(s.getComune());
					}
				}
			} else if("provincia".equals(field)){
				for (School s : schools) {
					if(!ans.contains(s.getProvincia())){
						ans.add(s.getProvincia());
					}
				}
			} else if("tipo".equals(field)){
				for (School s : schools) {
					if(!ans.contains(s.getTipo())){
						ans.add(s.getTipo());
					}
				}
			} else {
				throw new IllegalArgumentException("School JavaBean does not contain required field: " + field);
			}
		
		return ans;
	}
	
	public static ArrayList<School> getAllSchools(String jsonFiles) throws FileNotFoundException{
		log4j.debug(SchoolRetriever.class.getName() + " getAllSchools called with argument: " + jsonFiles);
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
				log4j.error(App.class.getName() + " Il percorso: " + schoolsFile.getAbsolutePath() + " produce un File[] = null");
				throw new FileNotFoundException();
			}
		} else {
			log4j.error(App.class.getName() + " Il percorso: " + schoolsFile.getAbsolutePath() + " non è un file o una directory valida");
			throw new FileNotFoundException();
		}
		return ans;
	}
	
	private static ArrayList<School> getFromFile(File f) throws FileNotFoundException{
		log4j.debug(SchoolRetriever.class.getName() + " getFromFile called with argument: " + f);
		JsonReader reader = Json.createReader(new FileReader(f));
		JsonArray jsarr = (JsonArray) reader.read();
		ArrayList<School> ans = new ArrayList<School>();
		for (JsonValue jsv : jsarr) {
			ans.add(new School((JsonObject) jsv));
		}
		return ans;
	}

}
