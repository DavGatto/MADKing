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

import java.io.IOException;

import java.util.Date;
import java.util.Properties;

import javax.json.JsonObject;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class PECSender {

	String pathSeparator = System.getProperty("file.separator");

	private int port;
	private String host;
	private String username;
	private String password;

	private String to;
	private String subject;
	private String body;
	private String attachment;

	private int getPort() {
		return port;
	}

	private void setPort(int port) {
		this.port = port;
	}

	private String getHost() {
		return host;
	}

	private void setHost(String host) {
		this.host = host;
	}

	private String getUsername() {
		return username;
	}

	private void setUsername(String username) {
		this.username = username;
	}

	private String getPassword() {
		return password;
	}

	private void setPassword(String password) {
		this.password = password;
	}

	private String getTo() {
		return to;
	}

	private void setTo(String to) {
		this.to = to;
	}

	private String getSubject() {
		return subject;
	}

	private void setSubject(String subject) {
		this.subject = subject;
	}

	private String getBody() {
		return body;
	}

	private void setBody(String body) {
		this.body = body;
	}

	private String getAttachment() {
		return attachment;
	}

	private void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public PECSender(JsonObject jsoMail, JsonObject jsoTeach, String folderPath) {

		setUsername(jsoMail.getString("username"));

		setPassword(jsoMail.getString("password"));

		setPort(Integer.parseInt(jsoMail.getString("port")));

		setHost(jsoMail.getString("host"));

		setSubject(jsoMail.getString("subject"));
		setBody(jsoMail.getString("body"));

		setAttachment(folderPath + pathSeparator + jsoTeach.getString("recapitoName").replaceAll("\\s|\'", "") + "_");

	}

	public void sendMail(JsonObject jsoSchool, String pecIstruzione, boolean simulating)
			throws AddressException, MessagingException, IOException {

		String body = getBody();
		if (!simulating) {
			setTo(jsoSchool.getString("codMec") + pecIstruzione);
		} else {
			setTo(pecIstruzione);
			body += " <br> <br> <b> DEBUG </b>: Questa mail sarebbe stata inviata alla scuola: " + jsoSchool.getString("codMec")
					+ " - " + jsoSchool.getString("nome") + " - " + jsoSchool.getString("tipo") + " - "
					+ jsoSchool.getString("comune") + " - " + jsoSchool.getString("provincia");
		}

		String basicAttachmentName = getAttachment();
		//TODO implementa invio CV come secondo allegato
		setAttachment(basicAttachmentName + jsoSchool.getString("codMec") + "_MAD.pdf");

		String[] attachments = new String[1];
		attachments[0] = getAttachment();

		sendEmailWithAttachments(getHost(), getPort() + "", getUsername(), getPassword(), getTo(), getSubject(), body,
				attachments);

		setAttachment(basicAttachmentName);

	}

	private static void sendEmailWithAttachments(String host, String port, final String userName, final String password,
			String toAddress, String subject, String message, String[] attachFiles)
			throws AddressException, MessagingException {

		Properties properties = new Properties();
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", port);
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.user", userName);
		properties.put("mail.password", password);

		Authenticator auth = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userName, password);
			}
		};
		Session session = Session.getInstance(properties, auth);

		Message msg = new MimeMessage(session);

		msg.setFrom(new InternetAddress(userName));
		InternetAddress[] toAddresses = { new InternetAddress(toAddress) };
		msg.setRecipients(Message.RecipientType.TO, toAddresses);
		msg.setSubject(subject);
		msg.setSentDate(new Date());

		MimeBodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(message, "text/html");

		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);

		if (attachFiles != null && attachFiles.length > 0) {
			for (String filePath : attachFiles) {
				MimeBodyPart attachPart = new MimeBodyPart();

				try {
					attachPart.attachFile(filePath);
				} catch (IOException ex) {
					ex.printStackTrace();
				}

				multipart.addBodyPart(attachPart);
			}
		}

		msg.setContent(multipart);

		Transport.send(msg);

	}

}
