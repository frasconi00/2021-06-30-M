package it.polito.tdp.genes.model;

public class TestModel {

	public static void main(String[] args) {
		Model m = new Model();
		System.out.println(m.creaGrafo());
		m.stampaConSoglia(4);
		
		m.preparaRicorsione();
		m.mostraPesoMigliore();
	}

}