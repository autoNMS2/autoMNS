package automnsCLI;

import java.io.IOException;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {
	public static void main(String[] args) throws IOException {
		String header = "\n" +
				"██╗    ██╗███████╗██╗      ██████╗ ██████╗ ███╗   ███╗███████╗    \n" +
				"██║    ██║██╔════╝██║     ██╔════╝██╔═══██╗████╗ ████║██╔════╝    \n" +
				"██║ █╗ ██║█████╗  ██║     ██║     ██║   ██║██╔████╔██║█████╗      \n" +
				"██║███╗██║██╔══╝  ██║     ██║     ██║   ██║██║╚██╔╝██║██╔══╝      \n" +
				"╚███╔███╔╝███████╗███████╗╚██████╗╚██████╔╝██║ ╚═╝ ██║███████╗    \n" +
				" ╚══╝╚══╝ ╚══════╝╚══════╝ ╚═════╝ ╚═════╝ ╚═╝     ╚═╝╚══════╝    \n" +
				"                                                                  \n" +
				"                    ████████╗ ██████╗                             \n" +
				"                    ╚══██╔══╝██╔═══██╗                            \n" +
				"                       ██║   ██║   ██║                            \n" +
				"                       ██║   ██║   ██║                            \n" +
				"                       ██║   ╚██████╔╝                            \n" +
				"                       ╚═╝    ╚═════╝                             \n" +
				"                                                                  \n" +
				" █████╗ ██╗   ██╗████████╗ ██████╗ ███╗   ███╗███╗   ██╗███████╗  \n" +
				"██╔══██╗██║   ██║╚══██╔══╝██╔═══██╗████╗ ████║████╗  ██║██╔════╝  \n" +
				"███████║██║   ██║   ██║   ██║   ██║██╔████╔██║██╔██╗ ██║███████╗  \n" +
				"██╔══██║██║   ██║   ██║   ██║   ██║██║╚██╔╝██║██║╚██╗██║╚════██║  \n" +
				"██║  ██║╚██████╔╝   ██║   ╚██████╔╝██║ ╚═╝ ██║██║ ╚████║███████║  \n" +
				"╚═╝  ╚═╝ ╚═════╝    ╚═╝    ╚═════╝ ╚═╝     ╚═╝╚═╝  ╚═══╝╚══════╝  \n" +
				"                                                                  \n" +
				"                    >Press Enter to Start<";
		try
		{
			//sleep for 1 second
			TimeUnit.SECONDS.sleep(1);
			//print the welcome header
			for (int i=0; i<header.length(); i++){
				System.out.print(header.charAt(i));
			}
			//create the scanner to pause
			Scanner enter = new Scanner(System.in);
			System.out.print("");
			//pause until user press enter
			enter.nextLine();
		}
		//catch thrown errors
		catch (InterruptedException e)
		{
		}
		Menus.MainMenu();		
	}	
}