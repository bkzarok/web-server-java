import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.Callable;

/**
 * 
 */

/**
 * @author kuir
 *
 */
public class ThreadedServer implements Callable<HashMap<String, String>>
{
	Socket socket;
	BufferedReader in;
	PrintWriter out;
	String rootdir;
	HashMap<String,String> data;

	/**
	 * 
	 */
	public ThreadedServer(Socket s, String location) 
	{
		try
		{
			socket = s;
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			out = new PrintWriter(s.getOutputStream());
			data = new HashMap<String,String>();
			this.rootdir=location;
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	/**
	 *
	 *@return HashMap<String,String>
	 */
	@Override
	public HashMap<String,String> call() 
	{

		String response ="", request= "";	
		
	
		try
		{
			while(in.ready()|| request.length()==0)
				request += (char) in.read();
			
			String line[] = request.split(" ");
			File file = new File(rootdir+line[1]);
			FileInputStream fin = new FileInputStream(file);
			//HTTP/1.0 2--- Document follows 
			response = "HTTP/1.1 200\r\n" 
					+ "Server: Out Java Server/1.0 \r\n" 
					+ "Content-Type: text/html \r\n"// 
					+ "Connection:close \r\n" 
					+ "Content-Length: "+file.length()+"\r\n"; 
				
			int t = 0;
			while((t=fin.read())!=-1)
				response += (char)t;
			//
			fin.close();
			out.write(response.toCharArray());
			out.close();
			in.close();
			socket.close();
			
			
		}catch(FileNotFoundException e)
		{
			//e.printStackTrace();//file not found 
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		long pid = Thread.currentThread().getId();
		data.put("request", request);
		data.put("pid", Integer.toString((int) pid));
		return data;
	}
	
	

}
