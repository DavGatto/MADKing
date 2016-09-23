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

	private int port;
	private String host;
	private String from;
	private boolean auth;
	private String username;
	private String password;
	private Protocol protocol;
	private boolean debug;

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

	private String getFrom() {
		return from;
	}

	private void setFrom(String from) {
		this.from = from;
	}

	private boolean isAuth() {
		return auth;
	}

	private void setAuth(boolean auth) {
		this.auth = auth;
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

	private Protocol getProtocol() {
		return protocol;
	}

	private void setProtocol(Protocol protocol) {
		this.protocol = protocol;
	}

	private boolean isDebug() {
		return debug;
	}

	private void setDebug(boolean debug) {
		this.debug = debug;
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

	public PECSender(JsonObject jsoMail, JsonObject jsoTeach, String folderPath, boolean debug) {

		setUsername(jsoMail.getString("username"));

		setPassword(jsoMail.getString("password"));

		setPort(Integer.parseInt(jsoMail.getString("port")));

		setHost(jsoMail.getString("host"));

		// setAuth(true);

		if ("SMTP".equals(jsoMail.getString("protocol"))) {
			setProtocol(Protocol.SMTP);
		} else if ("TLS".equals(jsoMail.getString("protocol"))) {
			setProtocol(Protocol.TLS);
		} else {
			setProtocol(Protocol.SMTPS);
		}

		setDebug(debug);

		setSubject(jsoMail.getString("subject"));
		setBody(jsoMail.getString("body"));

		setAttachment(folderPath + "/" + jsoTeach.getString("name").replaceAll("\\s|\'", "") + "_");

	}

	public void sendMail(JsonObject jsoSchool, String pecIstruzione)
			throws AddressException, MessagingException, IOException {

		// setTo(jsoSchool.getString("codMec") + pecIstruzione);
		setTo("davgatto@gmail.com");
		setFrom(getUsername());

		setAuth(true);
		setDebug(isDebug());

		String basicAttachmentName = getAttachment();

		setAttachment(basicAttachmentName + jsoSchool.getString("nome").replaceAll("\\s|\"|\'", "") + "-"
				+ jsoSchool.getString("codMec") + "_MAD.pdf");

		String[] attachments = new String[1];
		attachments[0] = getAttachment();

		sendEmailWithAttachments(getHost(), getPort() + "", getUsername(), getPassword(), getTo(),
				getSubject() + "[" + jsoSchool.getString("codMec") + pecIstruzione + "]", getBody(), attachments);

		setAttachment(basicAttachmentName);

		// Properties props = new Properties();
		// props.put("mail.smtp.host", getHost());
		// props.put("mail.smtp.port", getPort());
		// props.put("mail.user", getUsername());
		// props.put("mail.password", getPassword());
		//
		// switch (getProtocol()) {
		// case SMTPS:
		// props.put("mail.smtp.ssl.enable", true);
		// break;
		// case TLS:
		// props.put("mail.smtp.starttls.enable", true);
		// break;
		// default:
		// break;
		// }
		//
		// Authenticator authenticator = null;
		// if (isAuth()) {
		// props.put("mail.smtp.auth", true);
		// authenticator = new Authenticator() {
		// private PasswordAuthentication pa = new
		// PasswordAuthentication(getUsername(), getPassword());
		//
		// @Override
		// public PasswordAuthentication getPasswordAuthentication() {
		// return pa;
		// }
		// };
		// }
		//
		// Session session = Session.getInstance(props, authenticator);
		// session.setDebug(isDebug());
		//
		// // MimeMessage message = new MimeMessage(session);
		// Message message = new MimeMessage(session);
		// message.setFrom(new InternetAddress(getFrom()));
		// // InternetAddress[] address = {new InternetAddress(getTo())};
		// InternetAddress[] address = { new
		// InternetAddress("davgatto@gmail.com") };
		// message.setRecipients(Message.RecipientType.TO, address);
		// message.setSubject(getSubject());
		// message.setSentDate(new Date());
		//
		// // creates message part
		// MimeBodyPart messageBodyPart = new MimeBodyPart();
		// messageBodyPart.setContent(message,
		// getBody() + "\nWOUDASEND: " + getTo() + " \nWOULDAATTACH: " +
		// getAttachment());
		//
		// // creates multi-part
		// Multipart multipart = new MimeMultipart();
		// multipart.addBodyPart(messageBodyPart);
		//
		// // adds attachments
		// if (getAttachment() != null) {
		// MimeBodyPart attachPart = new MimeBodyPart();
		//
		// attachPart.attachFile(getAttachment());
		//
		// multipart.addBodyPart(attachPart);
		//
		// }
		//
		// // code to add attachment...will be revealed later
		// //MimeBodyPart attachPart = new MimeBodyPart();
		// //System.out.println(getAttachment());
		// //attachPart.attachFile(getAttachment());
		//
		// // adds parts to the multipart
		// //multipart.addBodyPart(messageBodyPart);
		// //multipart.addBodyPart(attachPart);
		//
		// // sets the multipart as message's content
		//// message.setContent(multipart);
		//// Transport.send(message);

	}

	private static void sendEmailWithAttachments(String host, String port, final String userName, final String password,
			String toAddress, String subject, String message, String[] attachFiles)
			throws AddressException, MessagingException {
		// sets SMTP server properties
		Properties properties = new Properties();
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", port);
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.user", userName);
		properties.put("mail.password", password);

		// creates a new session with an authenticator
		Authenticator auth = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userName, password);
			}
		};
		Session session = Session.getInstance(properties, auth);

		// creates a new e-mail message
		Message msg = new MimeMessage(session);

		msg.setFrom(new InternetAddress(userName));
		InternetAddress[] toAddresses = { new InternetAddress(toAddress) };
		msg.setRecipients(Message.RecipientType.TO, toAddresses);
		msg.setSubject(subject);
		msg.setSentDate(new Date());

		// creates message part
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(message, "text/html");

		// creates multi-part
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);

		// adds attachments
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

		// sets the multi-part as e-mail's content
		msg.setContent(multipart);

		// sends the e-mail
		Transport.send(msg);

	}

}
