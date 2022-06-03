package it.polito.tdp.genes.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.genes.db.GenesDao;

public class Model {
	
	private GenesDao dao;
	private List<Integer> chromosomes;
	private Graph<Integer, DefaultWeightedEdge> grafo;
	private double pesoMinimo;
	private double pesoMassimo;
	private double soglia;
	
	private List<Integer> best;
	private double bestPeso;
	
	public Model() {
		this.dao = new GenesDao();
		this.pesoMassimo=0;
		this.pesoMinimo=0;
		this.soglia = 0;
	}
	
	public String creaGrafo() {
		
		this.grafo = new SimpleDirectedWeightedGraph<Integer, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//aggiungo i vertici
		this.chromosomes = this.dao.getChromosomes();
		Graphs.addAllVertices(this.grafo, this.chromosomes);
//		System.out.println("vertici: "+this.grafo.vertexSet().size());
		
		//aggiungo gli archi
		
		for(Adiacenza a : this.dao.getArchi()) {
			Graphs.addEdgeWithVertices(this.grafo, a.getCromosoma1(), a.getCromosoma2(), a.getCorrelazione());
		}
		
//		System.out.println("archi: "+this.grafo.edgeSet().size());
		
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;
		for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
			
			double peso = this.grafo.getEdgeWeight(e);
			if(peso<min)
				min = peso;
			if(peso>max)
				max = peso;
			
		}
		
//		System.out.println("min = "+min);
//		System.out.println("max = "+max);
		
		this.pesoMinimo = min;
		this.pesoMassimo = max;
		
		String result = "Grafo creato: "+this.chromosomes.size()+" vertici e "+this.grafo.edgeSet().size()+" archi\n";
		result += "Peso minimo= "+min+", peso massimo= "+max;
		return result;
		
	}
	
	public String stampaConSoglia(double soglia) {
		int countMinori=0;
		int countMaggiori=0;
		
		for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
			double peso = this.grafo.getEdgeWeight(e);
			if(peso<soglia)
				countMinori++;
			else if(peso>soglia)
				countMaggiori++;
		}
		
		this.soglia = soglia;
		
//		System.out.println("soglia: "+soglia+" , maggiori= "+countMaggiori+" e minori= "+countMinori);
		String result = "\nSoglia: "+soglia+" --> Maggiori "+countMaggiori+", minori "+countMinori;
		return result;
		
	}

	public double getPesoMinimo() {
		return pesoMinimo;
	}

	public double getPesoMassimo() {
		return pesoMassimo;
	}
	
	public void preparaRicorsione() {
		
		this.best = new ArrayList<Integer>();
		
		for(Integer cromosoma : this.chromosomes) {
			List<Integer> parziale = new ArrayList<Integer>();
			parziale.add(cromosoma);
			cerca(parziale);
		}
		
		System.out.println(best);
		System.out.println("bestPeso= "+bestPeso);
		
	}

	private void cerca(List<Integer> parziale) {
		
		double pesoSoluzioneParziale = calcolaPesoSoluzione(parziale);
		
		if(pesoSoluzioneParziale>bestPeso) {
			best = new ArrayList<Integer>(parziale);
			this.bestPeso = pesoSoluzioneParziale;
		}
		
		for(DefaultWeightedEdge e : this.grafo.edgesOf(parziale.get(parziale.size()-1))) {
			
			if(this.grafo.getEdgeSource(e)==parziale.get(parziale.size()-1) && this.grafo.getEdgeWeight(e)>soglia 
					&& !parziale.contains(Graphs.getOppositeVertex(this.grafo, e, parziale.get(parziale.size()-1)))) {
				
				parziale.add(Graphs.getOppositeVertex(this.grafo, e, parziale.get(parziale.size()-1)));
				cerca(parziale);
				parziale.remove(parziale.size()-1);
				
			}
			
		}
		
	}
	
	private double calcolaPesoSoluzione(List<Integer> lista) {
		
		if(lista==null) return 0;
		
		double pesoSoluzione = 0;
		
		for(int i=0;i<lista.size()-1;i++) {
			
			pesoSoluzione+=this.grafo.getEdgeWeight(this.grafo.getEdge(lista.get(i), lista.get(i+1)));
		}
		
		return pesoSoluzione;
		
	}
	
	public void mostraPesoMigliore() {
		
		double pesoSoluzione = 0;
		
		for(int i=0;i<best.size()-1;i++) {
			System.out.println(this.grafo.getEdgeWeight(this.grafo.getEdge(best.get(i), best.get(i+1))));
			pesoSoluzione+=this.grafo.getEdgeWeight(this.grafo.getEdge(best.get(i), best.get(i+1)));
		}
		
		System.out.println("Totale: "+pesoSoluzione);
	}

}