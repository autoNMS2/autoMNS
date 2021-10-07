package automnsCLI.multi;

import java.io.IOException;
import java.io.InputStreamReader;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.*;
import java.io.BufferedReader;
import java.io.FileWriter;

public class db_agent2 extends Agent
{
    protected void setup()
    {
	    Runtime r = Runtime.getRuntime();
    	String cmd = "docker ps";
	try {
							Process proc = r.exec(cmd);
							BufferedReader stdInput = new BufferedReader(new 
								     InputStreamReader(proc.getInputStream()));

								BufferedReader stdError = new BufferedReader(new 
								     InputStreamReader(proc.getErrorStream()));

								// Read the output from the command
								System.out.println("Here is the standard output of the command:\n");
								String s = null;
								while ((s = stdInput.readLine()) != null) {
								    System.out.println(s);
								    FileWriter log=new FileWriter("log.txt");
								    log.write(s);
								    System.out.println("Writing successful");
							        //close the file 
							        log.close();
								}
								
								// Read any errors from the attempted command
								System.out.println("Here is the standard error of the command (if any):\n");
								while ((s = stdError.readLine()) != null) {
								    System.out.println(s);
								}
	    } catch (IOException e)
        {
		    e.printStackTrace();
	    }
    }
}
