import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server2
{

	public static void main(String[] args)
	{
		getIPv4();
		new Server2().startServer();
	}

	public static void getIPv4()
	{
		try
		{
			URL whatismyip = new URL("http://checkip.amazonaws.com");
			BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
			String ip = in.readLine(); //you get the IP as a String
			System.out.println("Public IP: "+ip);

			InetAddress address = InetAddress.getLocalHost();
			System.out.println(address.getHostName());
			System.out.println("IPv4: " + address.getHostAddress());
			System.out.println("Hex:  " + parseAddressToHex(address.getHostAddress()));
		} catch (Exception e)
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

	public void startServer()
	{
		final ExecutorService clientProcessingPool = Executors.newFixedThreadPool(10);

		Runnable serverTask = new Runnable()
		{
			@Override public void run()
			{
				try
				{
					ServerSocket serverSocket = new ServerSocket(9153);
					System.out.println("Waiting for clients to connect...");
					while (true)
					{
						Socket clientSocket = serverSocket.accept();
						clientProcessingPool.submit(new ClientTask(clientSocket));
					}
				} catch (IOException e)
				{
					System.err.println("Unable to process client request");
					e.printStackTrace();
				}
			}
		};
		Thread serverThread = new Thread(serverTask);
		serverThread.start();

	}

	private class ClientTask implements Runnable
	{
		private final Socket clientSocket;

		private ClientTask(Socket clientSocket)
		{
			this.clientSocket = clientSocket;
		}

		@Override public void run()
		{
			System.out.println("Got a client !");

			// Do whatever required to process the client's request

			try
			{
				clientSocket.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

}