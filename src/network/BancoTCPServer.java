package network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import model.Cuenta;



public class BancoTCPServer {

	public static final int PORT = 3400;
	
	private ServerSocket listener;
	private Socket serverSideSocket;
	
	private PrintWriter toNetwork;
	private BufferedReader fromNetwork;
	
	private HashMap<String,Cuenta> cuentas = new HashMap<String, Cuenta>();
	
	public BancoTCPServer() {
		System.out.println("Banco TCP server is running on port: " + PORT);
	}
	
	public void init() throws Exception{
		listener = new ServerSocket(PORT);
		
		while(true) {
			serverSideSocket = listener.accept();
			
			createStreams(serverSideSocket);
			protocol(serverSideSocket);
		}
	}
	
	public void protocol(Socket socket) throws Exception{
		String message = fromNetwork.readLine();
		String answer="";
		String[] control=message.split(",");
		
		System.out.println("[Server] From Client: " + message);
		switch(control[0]) {
		
		case "CREATE":
			System.out.println("entro a create");
			Cuenta cuenta = new Cuenta(control[1],control[2],Double.parseDouble(control[3]));
			String nCuenta=generarNCuenta();
			cuentas.put(nCuenta,cuenta);
			answer="Su cuenta ha sido creada con exito, su numero de cuenta es: "+nCuenta;
			break;
			
		case "CONSULT":
			System.out.println("entro a consult");
			for(String llave:cuentas.keySet()) {
				System.out.println("si entro al for");
				if(llave.equals(control[1])) {
					answer="la cuenta"+llave+cuentas.get(llave).consultar();
				}else {
					answer="la cuenta ingresada no se encuentra registrada";
				}
			}
			break;
		case "DEPOSIT":
			System.out.println("entro a deposit");
			for(String llave:cuentas.keySet()) {
				if(llave.equals(control[1])) {
					cuentas.get(llave).depositar(Double.parseDouble(control[2]));
					answer="la cuenta: "+llave+" tiene un nuevo saldo de: "+cuentas.get(llave).getSaldo();
				}else {
					answer="la cuenta ingresada no se encuentra registrada";
				}
			}
			break;
		case "RETIRE":
			System.out.println("entro a retire");
			for(String llave:cuentas.keySet()) {
				if(llave.equals(control[1])) {
					if(cuentas.get(llave).getSaldo()>Double.parseDouble(control[2])) {
						cuentas.get(llave).retirar(Double.parseDouble(control[2]));
						answer="la cuenta: "+llave+" tiene un nuevo saldo de: "+cuentas.get(llave).getSaldo();
					}else {
						answer="saldo insuficiente para realizar el retiro";
					}
				}else {
					answer="la cuenta ingresada no se encuentra registrada";
				}
			}
			break;
		default:
			answer="opcion invalida";
		}
		System.out.println(answer);
		toNetwork.println(answer);
	}
	
	private void createStreams(Socket socket) throws Exception{
		toNetwork = new PrintWriter(socket.getOutputStream(),true);
		fromNetwork = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
	
	public static void main(String args[]) throws Exception{
		BancoTCPServer es = new BancoTCPServer();
		es.init();
	}
	
	public String generarNCuenta() {
		Long numero;
		String salida;
		do {
			numero = (long) (Math.random() * 9_000_000_000_000_000_0L) + 1_000_000_000_000_000_0L;
			salida=Long.toString(numero);
		}
		while(!isDisponible(salida));
		return salida;
	}
	
	public boolean isDisponible(String nCuenta) {
		for(String llave:cuentas.keySet()) {
			if(llave.equals(nCuenta)) {
				return false;
			}
		}
		return true;
	}
}

