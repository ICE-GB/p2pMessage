package activemq;

import java.awt.Color;
import java.awt.Font;
import java.util.Date;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Sender {
	private String url;
	private String user;
	private String password;
	private final String QUEUE;
	private JTextPane textPane;

	public Sender(String queue, String url, String user, String password,
			JTextPane textPane) {
		this.url = url;
		this.user = user;
		this.password = password;
		this.QUEUE = queue;
		this.textPane = textPane;
	}

	public void sendMessage(String message) {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				user, password, url);
		Session session = null;
		Destination sendQueue;
		Connection connection = null;

		try {
			connection = connectionFactory.createConnection();

			connection.start();

			session = connection.createSession(false,
					Session.CLIENT_ACKNOWLEDGE);

			sendQueue = session.createQueue(QUEUE);
			MessageProducer sender = session.createProducer(sendQueue);
			TextMessage outMessage = session.createTextMessage();
			outMessage.setText(new Date() + "\n" + user + ":" + message);

			sender.send(outMessage);

			sender.close();

			connection.close();

			MutableAttributeSet attributeSet = setStyle();

			DefaultStyledDocument doc = (DefaultStyledDocument) textPane
					.getDocument();

			String str = new Date() + "\n" + message + ":" + user + "\n";

			doc.setParagraphAttributes(doc.getLength(), str.length(),
					attributeSet, false);

			doc.insertString(doc.getLength(), str, attributeSet);

		} catch (JMSException e) {
			e.printStackTrace();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	private MutableAttributeSet setStyle() {
		MutableAttributeSet attributeSet = new SimpleAttributeSet();
		StyleConstants.setForeground(attributeSet, new Color(0, 0, 0));
		// if(bold)
		// StyleConstants.setBold(attributeSet, true);
		StyleConstants.setFontFamily(attributeSet, "Consolas");
		StyleConstants.setFontSize(attributeSet,
				new Font("微软雅黑", Font.LAYOUT_NO_LIMIT_CONTEXT, 12).getSize());
		StyleConstants.setAlignment(attributeSet, StyleConstants.ALIGN_RIGHT);
		return attributeSet;
	}

}
