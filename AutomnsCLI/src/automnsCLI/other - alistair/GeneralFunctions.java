package automnsCLI;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class GeneralFunctions {

    public static String GetLocalIP() throws IOException
    {
        for (
                final Enumeration<NetworkInterface> interfaces =
                NetworkInterface.getNetworkInterfaces( );
                interfaces.hasMoreElements( );
        )
        {
            final NetworkInterface cur = interfaces.nextElement( );

            if ( cur.isLoopback( ) ) {
                continue;
            }
            //	System.out.println( "interface " + cur.getName( ) );

            for ( final InterfaceAddress addr : cur.getInterfaceAddresses( ) )
            {
                final InetAddress inet_addr = addr.getAddress( );

                if ( !( inet_addr instanceof Inet4Address) )
                {
                    continue;
                }

                if (inet_addr.getHostAddress().substring(0, 4).contains("192")) {	//	get local ip for some reason
                    System.out.println("IP: " + inet_addr.getHostAddress());
                    return inet_addr.getHostAddress();
                }
            }
        }
        System.out.println("IP Not Found");
        return null;
    }
}
