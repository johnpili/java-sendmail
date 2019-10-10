package com.johnpili.libs.sendmail;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

/**
 * @author John Pilli
 * @since 2016-04-20
 */
public class Sendmail implements Runnable
{
	private Properties properties;
	private String from;
	private String to;
	private String subject;
	private String textMessagePart;
	private String htmlMessagePart;
	private String smtpUsername;
	private String smtpPassword;

	private Sendmail(){}

	public static class Config
	{
		private Properties properties;
		private String from;
		private String to;
		private String subject;
		private String textMessagePart;
		private String htmlMessagePart;
		private String smtpUsername;
		private String smtpPassword;

		public Config properties(Properties properties)
		{
			this.properties = properties;
			return this;
		}

		public Config from(String from)
		{
			this.from = from;
			return this;
		}

		public Config to(String to)
		{
			this.to = to;
			return this;
		}

		public Config subject(String subject)
		{
			this.subject = subject;
			return this;
		}

		public Config textMessagePart(String textMessagePart)
		{
			this.textMessagePart = textMessagePart;
			return this;
		}

		public Config htmlMessagePart(String htmlMessagePart)
		{
			this.htmlMessagePart = htmlMessagePart;
			return this;
		}

		public Config smtpUsername(String smtpUsername)
		{
			this.smtpUsername = smtpUsername;
			return this;
		}

		public Config smtpPassword(String smtpPassword)
		{
			this.smtpPassword = smtpPassword;
			return this;
		}

		public Sendmail build()
		{
			Sendmail sendEmail = new Sendmail();
			sendEmail.properties = this.properties;
			sendEmail.from = this.from;
			sendEmail.to = this.to;
			sendEmail.subject = this.subject;
			sendEmail.textMessagePart = this.textMessagePart;
			sendEmail.htmlMessagePart = this.htmlMessagePart;
			sendEmail.smtpUsername = this.smtpUsername;
			sendEmail.smtpPassword = this.smtpPassword;
			return sendEmail;
		}
	}

	@Override
	public void run()
	{
		try
		{
			Session session = Session.getInstance(properties);
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO,
					new InternetAddress(to));
			message.setSubject(subject);

			Multipart multiPart = new MimeMultipart("alternative");

			MimeBodyPart textPart = new MimeBodyPart();
			textPart.setText(this.textMessagePart, "utf-8");

			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(this.htmlMessagePart, "text/html; charset=utf-8");

			multiPart.addBodyPart(textPart);
			multiPart.addBodyPart(htmlPart);
			message.setContent(multiPart);

			message.setContent(multiPart);
			message.saveChanges();

			Boolean authenticationRequired = false;
			if (properties.getProperty("mail.smtp.auth").equalsIgnoreCase("true"))
			{
				authenticationRequired = true;
			}

			if (authenticationRequired)
			{
				Authenticator auth = new SMTPAuthenticator(this.smtpUsername, this.smtpPassword);
				Session mailSession = Session.getDefaultInstance(properties, auth);
				Transport transport = mailSession.getTransport();
				transport.connect();
				transport.sendMessage(message, message.getAllRecipients());
				transport.close();
			}
			else
			{
				Transport tr = session.getTransport("smtp");
				tr.connect();
				tr.sendMessage(message, message.getAllRecipients());
				tr.close();
			}
		}
		catch(Exception exception)
		{
			System.out.println(exception.getMessage());
			exception.printStackTrace();
		}
	}

	private class SMTPAuthenticator extends Authenticator
	{
		private PasswordAuthentication passwordAuthentication;

		public SMTPAuthenticator(String username, String password)
		{
			this.passwordAuthentication = new PasswordAuthentication(username, password);
		}

		public PasswordAuthentication getPasswordAuthentication()
		{
			return this.passwordAuthentication;
		}
	}
}
