package activemq;

import java.awt.Color;
import java.awt.Font;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Receiver implements Runnable {
	private String url;
	private String user;
	private String password;
	private final String QUEUE;
	private JTextPane textPane;

	public Receiver(String queue, String url, String user, String password,
			JTextPane textPane) {
		this.url = url;
		this.user = user;
		this.password = password;
		this.QUEUE = queue;
		this.textPane = textPane;
	}

	@Override
	public void run() {

		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				user, password, url);
		Session session = null;
		Destination receiveQueue;
		try {
			Connection connection = connectionFactory.createConnection();

			session = connection.createSession(false,
					Session.CLIENT_ACKNOWLEDGE);
			receiveQueue = session.createQueue(QUEUE);
			MessageConsumer consumer = session.createConsumer(receiveQueue);

			connection.start();

			MutableAttributeSet attributeSet = setStyle();

			while (true) {
				Message message = consumer.receive();

				if (message instanceof TextMessage) {
					TextMessage receiveMessage = (TextMessage) message;

					DefaultStyledDocument doc = (DefaultStyledDocument) textPane
							.getDocument();

					String str = receiveMessage.getText() + "\n";

					doc.setParagraphAttributes(doc.getLength(), str.length(),
							attributeSet, false);

					doc.insertString(doc.getLength(), str, attributeSet);

					message.acknowledge();
					// System.out.println("我是" + user + ",收到消息如下: \r\n" +
					// receiveMessage.getText());
				} else {
					// session.commit();
					break;
				}

			}

			connection.close();
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
		StyleConstants.setAlignment(attributeSet, StyleConstants.ALIGN_LEFT);
		return attributeSet;
	}

}
