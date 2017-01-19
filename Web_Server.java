

import java.io.*;
import java.net.*;
import java.util.*;

/**
   This program implements a multithreaded server that listens to port 8189. Client type download or upload
   follow by a filename if the file exist it will either be uploaded or download depending on what you type. 
   */
  
public class Web_Server
{  
   public static void main(String[] args )
   {  
      try
      {  
         int i = 1;
         ServerSocket s = new ServerSocket(8189);

         while (true)
         {  
            Socket incoming = s.accept();
            System.out.println("Spawning " + i);
            Runnable r = new ThreadedEchoHandler(incoming);
            Thread t = new Thread(r);
            t.start();
            i++;
         }
      }
      catch (IOException e)
      {  
         e.printStackTrace();
      }
   }
}

/**
   This class handles the client input for one server socket connection. 
*/
class ThreadedEchoHandler implements Runnable
{ 
   private Socket incoming;
   OutputStream outStream ;
   InputStream inStream;
   private FileInputStream fis;
   private BufferedInputStream bis;
   FileOutputStream fos = null;
   BufferedOutputStream bos = null;
   public final static int FILE_SIZE = Integer.MAX_VALUE;
   /**
      Constructs a handler.
      @param i the incoming socket
   */
   public ThreadedEchoHandler(Socket i)
   { 
      incoming = i; 
   }
   private void disconnect() 
   {
		try 
		{ 
			if(inStream != null) inStream.close();
			if(outStream != null) outStream.close();
			if(incoming != null) incoming.close();
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		} // not much else I can do
		
		
		
			
	}

   public void run()
   {  
      try
      {  
         try
         {
            inStream = incoming.getInputStream();
            outStream = incoming.getOutputStream();
            
            Scanner in = new Scanner(inStream);         
            PrintWriter out = new PrintWriter(outStream, true /* autoFlush */);
            
            out.println( "Hello! Enter BYE to exit." );
            
            // echo client input
            boolean done = false;
            while (!done && in.hasNextLine())
            {  
               String[] commands = in.nextLine().split("\\s+");            
               out.println("Enter Download or Upload then follow by the file you request");            
               if (commands[0].equals("BYE"))
                  done = true;
               
               if(commands[0].equalsIgnoreCase("upload"))
               {
            	   if(commands[1]!=null)
            	   {
            		   out.println(getFile(commands[1]));
            	   }
               }
               if(commands[0].equalsIgnoreCase("download"))
               {
            	   if(commands[1]!=null)
            	   {
            		  out.println(sendFile(commands[1]));
            	   }
               }
               
            }
         }
         finally
         {
            incoming.close();
            bis.close();
            outStream.close();
         }
      }
      catch (IOException e)
      {  
         e.printStackTrace();
      }
   }
   
   public synchronized String getFile(String filename)
   {
	   int bytesRead = 0;
	    int current = 0;
	   // receive file
	      byte [] mybytearray  = new byte [FILE_SIZE];
	     
	      try {
			fos = new FileOutputStream(filename);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      bos = new BufferedOutputStream(fos);
	      try {
			bytesRead = inStream.read(mybytearray,0,mybytearray.length);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      current = bytesRead;

	      do {
	         try {
				bytesRead =
				    inStream.read(mybytearray, current, (mybytearray.length-current));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	         if(bytesRead >= 0) current += bytesRead;
	      } while(bytesRead > -1);

	      try {
			bos.write(mybytearray, 0 , current);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      try {
			bos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return    "File " + filename + " downloaded (" + current + " bytes read)";
   }
   
   
   public synchronized String sendFile(String filename)
   {
	   File myFile = new File(filename);
       byte [] mybytearray  = new byte [(int)myFile.length()];
       try {
		fis = new FileInputStream(myFile);
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
       bis = new BufferedInputStream(fis);
       try {
		bis.read(mybytearray,0,mybytearray.length);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
       String str = "Sending " + myFile.getAbsolutePath().toString()+ "(" + mybytearray.length + " bytes)";
       try {
		outStream.write(mybytearray,0,mybytearray.length);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
       try {
		outStream.flush();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return str;
       
   }

}
