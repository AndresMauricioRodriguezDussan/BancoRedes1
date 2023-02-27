package model;

public class Cuenta {

	private String nombreCliente;
	private double saldo;
	
	public Cuenta(String nombreCliente, double saldo) {
		this.nombreCliente = nombreCliente;
		this.saldo = saldo;
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}


	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}
	
	public void depositar(double deposito) {
		this.saldo+=deposito;
	}
	
	public void retirar(double retiro) {
		this.saldo-=retiro;
	}
	
	public String consultar() {
		return " tiene de saldo: "+this.saldo;
	}
	
}
