package swing;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import activemq.Receiver;
import activemq.Sender;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTextPane;

public class Telegram extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextPane textPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;

	private String url = "tcp://localhost:61616";
	private String user = null;
	private String password = null;
	private String sendQueue = null;
	private String receiveQueue = null;

	private String senderName = null;
	private String receiverName = null;

	private Sender sender;
	private Receiver receiver;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					Telegram frame = new Telegram();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Telegram() {
		String title = "Telegram";
		this.setTitle(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.NORTH);

		JLabel label = new JLabel("Sender:");
		panel_1.add(label);

		textField_1 = new JTextField();
		panel_1.add(textField_1);
		textField_1.setColumns(10);

		JLabel label_1 = new JLabel("Receiver:");
		panel_1.add(label_1);

		textField_2 = new JTextField();
		panel_1.add(textField_2);
		textField_2.setColumns(10);

		JButton button_1 = new JButton("确定");
		button_1.addActionListener(e -> {
			if (!textField_1.getText().equals("")
					&& !textField_1.getText().equals("")) {
				user = textField_1.getText();
				senderName = user;
				receiverName = textField_2.getText();
				receiveQueue = receiverName + "TO" + senderName;
				receiver = new Receiver(receiveQueue, url, user, password,
						textPane);
				new Thread(receiver, "Name-Receiver").start();
				button_1.setEnabled(false);
				textField_1.setEnabled(false);
				textField_2.setEnabled(false);
			}
		});
		panel_1.add(button_1);

		JPanel panel_2 = new JPanel();
		panel.add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		panel_2.add(scrollPane, BorderLayout.CENTER);

		textPane = new JTextPane();
		scrollPane.setViewportView(textPane);
		textPane.setEditable(false);

		JPanel panel_3 = new JPanel();
		panel.add(panel_3, BorderLayout.SOUTH);
		panel_3.setLayout(new BorderLayout(0, 0));

		textField = new JTextField();
		textField.setText("");
		panel_3.add(textField);
		textField.setColumns(10);

		JButton button = new JButton("发送");
		button.addActionListener(e -> {
			sendQueue = senderName + "TO" + receiverName;
			sender = new Sender(sendQueue, url, user, password, textPane);
			sender.sendMessage(textField.getText());
			textField.setText("");
		});
		panel_3.add(button, BorderLayout.EAST);
	}

}
