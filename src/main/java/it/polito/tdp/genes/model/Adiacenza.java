package it.polito.tdp.genes.model;

public class Adiacenza {
	
	private int cromosoma1;
	private int cromosoma2;
	private double correlazione;
	
	public Adiacenza(int cromosoma1, int cromosoma2, double correlazione) {
		super();
		this.cromosoma1 = cromosoma1;
		this.cromosoma2 = cromosoma2;
		this.correlazione = correlazione;
	}

	public int getCromosoma1() {
		return cromosoma1;
	}

	public void setCromosoma1(int cromosoma1) {
		this.cromosoma1 = cromosoma1;
	}

	public int getCromosoma2() {
		return cromosoma2;
	}

	public void setCromosoma2(int cromosoma2) {
		this.cromosoma2 = cromosoma2;
	}

	public double getCorrelazione() {
		return correlazione;
	}

	public void setCorrelazione(double correlazione) {
		this.correlazione = correlazione;
	}
	
	

}
