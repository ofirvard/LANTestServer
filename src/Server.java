import java.io.DataInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * Created by ofir on 3/28/2018.
 */
public class Server
{
	public static void main(String[] args)
	{
		getIPv4();
		getMsg();
	}

	public static void getIPv4()
	{
		try
		{
			InetAddress address = InetAddress.getLocalHost();
			System.out.println(address.getHostName());
			System.out.println("IPv4: " + address.getHostAddress());
			System.out.println("Hex:  " + parseAddressToHex(address.getHostAddress()));
		}
		catch (UnknownHostException e)
		{
		}
	}

	public static void getMsg()
	{
		try
		{
			String msg_received;

			ServerSocket serverSocket = new ServerSocket(9153);
			Socket clientSocket = serverSocket.accept();       //This is blocking. It will wait.
			DataInputStream DIS = new DataInputStream(clientSocket.getInputStream());
			msg_received = DIS.readUTF();
			System.out.println(msg_received);
			clientSocket.close();
			serverSocket.close();
		}
		catch (Exception e)
		{
		}
	}

	private static String parseAddressToHex(String address)
	{

		int result = 0;
		String[] str = address.split("\\.");
		for (int i = 0; i < str.length; i++)
		{
			int j = Integer.parseInt(str[i]);
			result = result << 8 | (j & 0xFF);
		}
		return Integer.toHexString(result);
	}

}
