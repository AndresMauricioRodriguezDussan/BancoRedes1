package network;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class BancoTCPClient {

	/**
	 * Un `Scanner` que se utiliza para leer la entrada de la consola.
	 */
	private static final Scanner SCANNER = new Scanner(System.in);

	private static final String RUTA_ARCHIVO_MENSAJES = "C:\\Users\\57312\\Repositorio-Git\\BancoRedes\\operaciones.txt";

	/**
	 * La dirección IP o nombre de dominio del servidor al que se conectará el
	 * cliente.
	 */
	public static final String SERVER = "localhost";

	/**
	 * El número de puerto en el que el servidor está escuchando.
	 */
	public static final int PORT = 3400;

	/**
	 * Un `PrintWriter` que se utiliza para escribir en el flujo de salida del
	 * socket.
	 */
	private PrintWriter toNetwork;

	/**
	 * Un `BufferedReader` que se utiliza para leer del flujo de entrada del socket.
	 */
	private BufferedReader fromNetwork;

	/**
	 * El `Socket` que representa la conexión entre el cliente y el servidor.
	 */
	private Socket clientSideSocket;

	/**
	 * El constructor inicializa la impresión en la consola de un mensaje indicando
	 * que el cliente se está ejecutando.
	 */
	public void init() throws Exception {

		clientSideSocket = new Socket(SERVER, PORT); // Se crea una nueva conexión con el servidor

		createStreams(clientSideSocket); // Se crean los flujos de entrada/salida con el servidor

		protocol(clientSideSocket); // Se ejecuta el protocolo de comunicación

		clientSideSocket.close(); // Se cierra la conexión con el servidor

	}

	public void protocol(Socket socket) throws Exception {

		while (true) {

			String fromUser = menu();

			if (fromUser.equals("LEER")) {

				try (BufferedReader br = new BufferedReader(new FileReader(RUTA_ARCHIVO_MENSAJES))) {

					while ((fromUser = br.readLine()) != null) {

						boolean ultimaIteracion = !br.ready();

						toNetwork.println(fromUser);

						String fromServer = fromNetwork.readLine();
						System.out.println("[Client] From server: " + fromServer);
						if (ultimaIteracion) {
							// 5toNetwork.println("FIN");
							System.out.println("[Client] El programa ha terminado.");
						}

						if (ultimaIteracion) {
							break;
						}

					}

				}

			} else {

				toNetwork.println(fromUser); // Se envia el mensaje al servidor

				if (fromUser.isEmpty()) {
					// Si el mensaje está vacío, se sale del ciclo y se cierra la conexión
					break;
				}

				String fromServer = fromNetwork.readLine(); // Se lee el mensaje enviado por el servidor

				System.out.println("[Client] From server: " + fromServer); // Se imprime el mensaje recibido desde el
																			// servidor

			}

		}

	}

	private void createStreams(Socket socket) throws Exception {

		toNetwork = new PrintWriter(socket.getOutputStream(), true); // Se crea un flujo de salida con el servidor

		fromNetwork = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Se crea un flujo de entrada
																							// con el servidor

	}

	public String menu() {

		System.out.println("Digite la opcion que desee.");
		System.out.println("1. Para crear una cuenta");
		System.out.println("2. Para consultar saldo en cuenta");
		System.out.println("3. Para depositar dinero a una cuenta");
		System.out.println("4. Para retirar dinero de una cuenta");
		System.out.println("5. Para ingresar archivo de texto");
		String mensaje = SCANNER.nextLine(); // Se lee el mensaje ingresado por el usuario
		String mensajeMenu = "";

		switch (mensaje) {

		case "1":

			// Este caso es para crear una cuenta
			System.out.println(
					"Para crear una cuenta digite los siguientes datos de esta manera:Nombre,identificacion,deposito inicial");
			mensajeMenu = SCANNER.nextLine(); // Se lee el mensaje ingresado por el usuario
			mensajeMenu = "CREATE," + mensajeMenu;

			break;

		case "2":

			// Este caso es para consultar un valor en cuenta
			System.out.println("Para consultar su saldo digite los siguientes datos de esta manera:Nro de cuenta");
			mensajeMenu = SCANNER.nextLine(); // Se lee el mensaje ingresado por el usuario
			mensajeMenu = "CONSULT," + mensajeMenu;

			break;
		case "3":

			// Este caso es para depositar dinero en cuenta
			System.out.println(
					"Para depositar digite los siguientes datos de esta manera:Nro de cuenta,valor a depositar");
			mensajeMenu = SCANNER.nextLine(); // Se lee el mensaje ingresado por el usuario
			mensajeMenu = "DEPOSIT," + mensajeMenu;

			break;
		case "4":

			// Este caso es para retirar dinero en cuenta
			System.out.println("Para retirar digite los siguientes datos de esta manera:Nro de cuenta,valor a retirar");
			mensajeMenu = SCANNER.nextLine(); // Se lee el mensaje ingresado por el usuario
			mensajeMenu = "RETIRE," + mensajeMenu;

			break;
		case "5":

			// Este caso es para leer un txt
			System.out.println("A continuacion  se leera y enviaran las ordenes en el archivo de texto");
			mensajeMenu = "LEER";

			break;
		default:

			break;

		}
		return mensajeMenu;

	}

	public static void main(String args[]) throws Exception {

		BancoTCPClient ec = new BancoTCPClient(); // Se crea una nueva instancia del cliente

		ec.init(); // Se inicializa el cliente

	}
}