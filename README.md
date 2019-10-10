# java-sendmail

A simple java send email library


## Usage example

```java

Sendmail sendmail = new Sendmail.Config()
    .properties(properties)
    .from(from)
		.to(targetRecipient)
		.subject(subject)
		.textMessagePart(plainTextMessage)
		.htmlMessagePart(htmlMessage)
		.smtpUsername(appConfig.getSmtpUsername())
		.smtpPassword(appConfig.getSmtpPassword())
		.build();

new Thread(sendmail).start();
```

```java

## Code example in a service method

public void sendEmailNotification(String targetRecipient, String subject, String plainTextMessage, String htmlMessage)
	{
		try
		{
			String from = "johnpili.com <no-reply@johnpili.com>";
			String host = appConfig.getSmtpHost();
			String port = appConfig.getSmtpPort();

			Properties properties = System.getProperties();
			properties.put("mail.transport.protocol", "smtp");
			properties.put("mail.smtp.socketFactory.port", port);
			properties.put("mail.smtp.socketFactory.class",
					"javax.net.ssl.SSLSocketFactory");
			properties.put("mail.smtp.host", host);
			if (appConfig.isSmtpRequireAuthentication())
			{
				properties.put("mail.smtp.auth", "true");
			}
			properties.put("mail.smtp.port", port);

			Sendmail sendmail = new Sendmail.Config()
					.properties(properties)
					.from(from)
					.to(targetRecipient)
					.subject(subject)
					.textMessagePart(plainTextMessage)
					.htmlMessagePart(htmlMessage)
					.smtpUsername(appConfig.getSmtpUsername())
					.smtpPassword(appConfig.getSmtpPassword())
					.build();

			new Thread(sendmail).start();
		}
		catch (Exception exception)
		{
			logger.error("sendEmailNotification Error: " + exception.getMessage());
		}
	}
  
  
  ```
