package htmldriver;
//MYSQL CONNECTOR JAR 
//USE TO DO A TEST CASE FOR EXAMPLE TO VALIDATE A USER REGISTERED FROM THE UI OR THROUGH THE API IF THE RECORD IS IN THE DATABASE OR A PURCHASE ORDER AND ALWAYS DELETE IT THE SAME VARIABLE AS THE ORDER OR USERNAME
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;


//Email libraries
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
//import reporter.JyperionListener;


public class InitConnection {
	
	//ArrayList to be send as email body
	private static ArrayList<String> sqlResultsList = new ArrayList<String>();

	public static void main(String[] args) {
		MysqlConnect mysqlConnect = new MysqlConnect();
		String sql = "SELECT * FROM `actor`";
		try {
		    PreparedStatement statement = mysqlConnect.connect().prepareStatement(sql);
		    //I ADDED THIS BY MYSELF
		    ResultSet result = statement.executeQuery();
		    System.out.println(result.toString());
		    //It always starts from the first row - the column names so go to the second row before working on it
		    result.next();
		    do{
		    	System.out.println(result.getRow());
		    	//I need to know the datatype of the columns that I need easiest way to see the im MySQLWorkbench or phpMyAdmin and from there generate my queries just in case or test them 
		    	System.out.println(result.getInt("actor_id") + "|" + result.getString("first_name") + "|" + result.getString("last_name") + "|" + result.getTimestamp("last_update"));
		    	//add the row to ArrayList to be send as email
		    	sqlResultsList.add(result.getInt("actor_id") + "|" + result.getString("first_name") + "|" + result.getString("last_name") + "|" + result.getTimestamp("last_update"));
		    	result.next();
		    }
		    while(!result.isLast());
		    System.out.println(result.getRow());
	    	//I need to know the datatype of the columns that I need easiest way to see the im MySQLWorkbench or phpMyAdmin and from there generate my queries just in case or test them 
	    	System.out.println(result.getInt("actor_id") + "|" + result.getString("first_name") + "|" + result.getString("last_name") + "|" + result.getTimestamp("last_update"));
	    	//add the row to ArrayList to be send as email
	    	sqlResultsList.add(result.getInt("actor_id") + "|" + result.getString("first_name") + "|" + result.getString("last_name") + "|" + result.getTimestamp("last_update"));
	    	
		    
		    //Create order from WebDriver or SoaupUI API
		    //Validate that the order is in the database
	    	//Assert.assertTrue(expected order, order in the database);
		    //Remove the record
		    //repeat nightly for regression
		    
		} catch (SQLException e) {
		    e.printStackTrace();
		} finally {
		    mysqlConnect.disconnect();
		}
		
		
		//Send Email with the results from the query
		//Google sends email to the recovery email listed in the account settings for a old program trying to access the email
		//To allow it I have to allow it from there first before this code can start working
		//Google Account Settings Allow access to less secure apps
		//This version works simple setBody
		sendPDFReportByGMail("cceekkoo@gmail.com", "cceekkoo", "cccceeekko@yahoo.com", "sql query results", sqlResultsList.toString());

	}	
	
	
	/**
	 * Send email using java
	 * mail.jar itext-2.1.7.jar
	 * 
	 * 
	 * @param from
	 * @param pass
	 * @param to
	 * @param subject
	 * @param body
	 */
	private static void sendPDFReportByGMail(String from, String pass, String to, String subject, String body) {
        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
        	//Set from address
            message.setFrom(new InternetAddress(from));
             message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
           //Set subject
            message.setSubject(subject);
            message.setText(body);
          
            //BodyPart objMessageBodyPart = new MimeBodyPart();
            
            //objMessageBodyPart.setText("Please Find The Attached Report File!");
            
            //Multipart multipart = new MimeMultipart();

            //multipart.addBodyPart(objMessageBodyPart);

            //objMessageBodyPart = new MimeBodyPart();

            //Set path to the pdf report file
            //String filename = System.getProperty("user.dir")+"\\Default test.pdf"; 
            //Create data source to attach the file in mail
            //DataSource source = new FileDataSource(filename);
            
            //objMessageBodyPart.setDataHandler(new DataHandler(source));

            //objMessageBodyPart.setFileName(filename);

            //multipart.addBodyPart(objMessageBodyPart);

            //message.setContent(multipart);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }
        catch (AddressException ae) {
            ae.printStackTrace();
        }
        catch (MessagingException me) {
            me.printStackTrace();
        }
    }
}


