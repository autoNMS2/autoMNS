package automnsCLI;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


public class Menus
{
	//start cli
	public static int MainMenu() throws IOException
	{
		//create scanner to read input
		Scanner input = new Scanner(System.in);
		//prompt menu
		System.out.print("\n<<<<<<AutoMNS Main Menu>>>>>>" +
				"\n|1| Initialise VMs" +
				"\n|2| Initialise Agent Platform" +
				"\n|0| Exit Application" +
				"\nEnter Option Number >> ");
		//take user input
		int i = input.nextInt();
		return MenuChoice(i);
	}
	//process menu options
	public static int MenuChoice(int i) throws IOException {
		switch (i) {
			//Initialise VMs
			case 1:
				return VMFunctions.addVMs();
			//Initialise Agent Platform
			case 2:
				return VMFunctions.initialiseAgents();
			//Exit Application
			case 0:
				return 0;
		}
		return i;
	}
}
