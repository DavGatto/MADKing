package com.gmail.davgatto.MADKing.ui;


import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.mail.internet.InternetAddress;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.commons.lang3.StringUtils;

import com.gmail.davgatto.MADKing.Maker.TeacherDetails;

public class Utils {

	public static String validateTeacherFile(String f) {
		try {
			(new TeacherDetails()).setFromJsonObj(f);
		} catch (Exception e) {
			return f;
		}
		return null;
	}

	public static String validatePecFile(String f) {
		// System.out.println("validatePecFile on " + f);
		JsonReader reader;
		try {
			reader = Json.createReader(new FileReader(f));

			JsonObject jso = (JsonObject) ((JsonValue) reader.read());

			if (StringUtils.isEmpty(jso.getString("username"))) {
				return f;
			}
			if (StringUtils.isEmpty(jso.getString("password"))) {
				return f;
			}
			if (StringUtils.isEmpty(jso.getString("port"))) {
				return f;
			}
			if (StringUtils.isEmpty(jso.getString("host"))) {
				return f;
			}
			if (StringUtils.isEmpty(jso.getString("subject"))) {
				return f;
			}
			if (StringUtils.isEmpty(jso.getString("body"))) {
				return f;
			}
		} catch (Exception e) {
			return f;
		}
		// System.out.println("OK");
		return null;
	}

	public static ArrayList<String> getInvalidSchoolsFiles(String p) {
		File f = new File(p);
		ArrayList<String> ans = new ArrayList<String>();
		if (f.isFile()) {
			if (!validateSchoolsFile(p)) {
				ans.add(p);
			}
			return ans;
		} else if (f.isDirectory()) {
			File[] directoryListing = f.listFiles();
			if (directoryListing != null && directoryListing.length > 0) {
				for (File schools : directoryListing) {
					if (!validateSchoolsFile(schools.getAbsolutePath())) {
						ans.add(schools.getAbsolutePath());
					}
				}
				return ans;
			} else {
				return null;
			}
		}
		return null;
	}

	// TODO cambia in modo da CONTARE le scuole
	public static boolean validateSchoolsFile(String f) {
		// System.out.println("validateSchoolsFile on " + f);
		JsonReader reader;
		try {
			reader = Json.createReader(new FileReader(f));
			JsonArray jsa = (JsonArray) reader.read();
			for (JsonValue jsV : jsa) {
				JsonObject jso = (JsonObject) jsV;
				if (StringUtils.isEmpty(jso.getString("codMec"))) {
					// System.out.println("no codMec");
					return false;
				}
				if (StringUtils.isEmpty(jso.getString("nome"))) {
					// System.out.println("no nome");
					return false;
				}
				if (StringUtils.isEmpty(jso.getString("tipo"))) {
					// System.out.println("no tipo");
					return false;
				}
			}
		} catch (Exception e) {
			// System.out.println("Exception: " + e.getMessage());
			return false;
		}
		// System.out.println("OK");
		return true;
	}

	public static boolean isValidEmailAddress(String email) {
		if (email.isEmpty()) {
			return true;
		}
		boolean result = true;
		try {
			InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();
		} catch (Exception ex) {
			result = false;
		}
		return result;
	}

	// TODO Funziona ma non trova l'icona
	public static boolean prompt(String title, String question, String yes, String no, int preselected,
			String iconPath) {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		URL url = Thread.currentThread().getContextClassLoader().getResource(iconPath);
		//System.out.println(iconPath);
		Object[] options = { yes, no };
		ImageIcon icon = null;
		if (iconPath != null) {
			icon = new ImageIcon(url);
			//System.out.println(icon);
		}
		int n = JOptionPane.showOptionDialog(new JFrame(), question, title, JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, icon, options, options[preselected]);
		return n == 0;
	}
}
