import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.ScrollPaneConstants;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.swing.JList;


public class WebServerUI extends JFrame {

	private JPanel contentPane;
	private JTextArea textArea;
	private JLabel lblPort;
	private JButton btnStart;
	private JLabel lblModule;
	private HashMap<String, String> data;
	private Thread t;
	private boolean isStop = false;
	private ServerSocket serverSocket;
	private ExecutorService executor;
	private static final int portNumber = 8080;
	private static final String rootdir ="C:/root";
	private JTextArea textArea_Pids;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				try 
				{
					WebServerUI frame = new WebServerUI();
					frame.setVisible(true);
				} catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public WebServerUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 373, 415);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textArea = new JTextArea();
		DefaultCaret caret = (DefaultCaret)textArea.getCaret();
		 caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		JScrollPane scroll = new JScrollPane(textArea);
	    scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setBounds(10, 154, 339, 211);		
		contentPane.add(scroll);
		
		lblPort = new JLabel(Integer.toString(portNumber));
		lblPort.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblPort.setBounds(169, 44, 46, 14);
		contentPane.add(lblPort);
		
		lblModule = new JLabel(rootdir);
		lblModule.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblModule.setBounds(10, 44, 46, 14);
		contentPane.add(lblModule);
		
		JLabel lblAction = new JLabel("Action");
		lblAction.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblAction.setBounds(253, 19, 46, 14);
		contentPane.add(lblAction);
		
		JLabel lblport = new JLabel("Port");
		lblport.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblport.setBounds(169, 19, 46, 14);
		contentPane.add(lblport);
		
		JLabel lblPids_1 = new JLabel("PIDs");
		lblPids_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblPids_1.setBounds(90, 19, 46, 14);
		contentPane.add(lblPids_1);
		
		JLabel lblModule_1 = new JLabel("Directory");
		lblModule_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblModule_1.setBounds(10, 19, 70, 14);
		contentPane.add(lblModule_1);
		btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() 
		{

			public void actionPerformed(ActionEvent event) 
			{
				if(isStop!=true)
				{
					t = new Thread()
					{
						public void run()
						{
							startServer();
						}
					
					};
					t.start();
					isStop = true;
					btnStart.setText("Stop");
					lblModule.setForeground(Color.GREEN);
				}
				else
				{
					
					isStop = false;
					btnStart.setText("Start");
					lblModule.setForeground(null);
					disconnect();
				}
				
			}
		});
		btnStart.setBounds(246, 40, 65, 23);
		contentPane.add(btnStart);
		
		textArea_Pids = new JTextArea();
		textArea_Pids.setEditable(false);
		textArea_Pids.setBounds(90, 40, 37, 103);
		textArea_Pids.setBackground(null);
		
		contentPane.add(textArea_Pids);
	}
	

	public void disconnect()
	{
		try 
		{
			
			if(executor!=null|| serverSocket!=null)
			{
				textArea.append("Attempting to stop\n");
				executor.shutdown();
				executor.awaitTermination(1, TimeUnit.SECONDS);
				serverSocket.close();
				textArea.append("disconnected");
			}
			else
				textArea.append("all objects are null");
			
			
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		
		
	}
	public void startServer()
	{
		try
		{
			textArea.append("starting server now\n");
			serverSocket = new  ServerSocket(portNumber);//port nu

			while(true)
			{
				Socket s = serverSocket.accept();
				executor = Executors.newSingleThreadExecutor();
		        Future<HashMap<String, String>> future = executor.submit(new ThreadedServer(s ,rootdir));
		        data=future.get();
		        textArea.append(data.get("request"));
		        textArea_Pids.append(data.get("pid")+"\n");
		        System.out.println(data);
		        executor.shutdown(); 
			}
		}catch(IOException e)
		{
			
		}
		catch(Exception e)
		{
			
		}
			
	}
}
