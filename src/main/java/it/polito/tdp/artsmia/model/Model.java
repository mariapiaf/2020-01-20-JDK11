package it.polito.tdp.artsmia.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {

	private ArtsmiaDAO dao;
	private Graph<Artist, DefaultWeightedEdge> grafo;
	private Map<Integer, Artist> idMap;
	private List<Artist> soluzioneMigliore;
	private int numEspMigliori;
	
	public Model() {
		dao = new ArtsmiaDAO();
		idMap = new HashMap<>();
	}
	
	public void creaGrafo(String ruolo) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		dao.getVertici(ruolo, idMap);
		
		// aggiungo vertici
		Graphs.addAllVertices(this.grafo, idMap.values());
		
		// aggiungo archi
		for(Arco a: dao.getArchi(ruolo, idMap, grafo)) {
			Graphs.addEdge(this.grafo, a.getA1(), a.getA2(), a.getPeso());
		}
	}
	
	public List<String> getRuoli(){
		return dao.getRuoli();
	}
	
	public int nVertici() {
		return grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return grafo.edgeSet().size();
	}
	
	public List<Arco> getConnessi(String ruolo) {
		List<Arco> result = new ArrayList<>(dao.getArchi(ruolo, idMap, grafo)); 
		Collections.sort(result);
		return result;
	}
	
	public List<Artist> camminoPiuLungo(Integer id) {
		soluzioneMigliore = new ArrayList<>();
		numEspMigliori = 0;
		List<Artist> parziale = new ArrayList<>();
		Artist a = idMap.get(id);
		if(a == null)
			return null;
		parziale.add(a);
		cerca(parziale, -1);
		return soluzioneMigliore;
	}
	
	public void cerca(List<Artist> parziale, int peso) {
		
		if(parziale.size() == grafo.vertexSet().size()) {
			int numeroEsposizioni = parziale.size();
			if(numeroEsposizioni > numEspMigliori) {
				numEspMigliori = numeroEsposizioni;
				soluzioneMigliore = new ArrayList<>(parziale);
			}
			return;
		}

		Artist ultimo = parziale.get(parziale.size()-1);
		List<Artist> vicini = new ArrayList<>(Graphs.neighborListOf(this.grafo, parziale.get(0)));
		
		for(Artist vicino: vicini) {
			if(!parziale.contains(vicino) && parziale.size() == 1) {
				parziale.add(vicino);
				cerca(parziale, (int) this.grafo.getEdgeWeight(this.grafo.getEdge(ultimo, vicino)));
				parziale.remove(vicino);
			}
			else if(!parziale.contains(vicino)) {
				if((int) this.grafo.getEdgeWeight(this.grafo.getEdge(ultimo, vicino)) == peso) {
					parziale.add(vicino);
					cerca(parziale, peso);
					parziale.remove(vicino);
				}
			}
		}
		
	}
	
	public int getNumEsp() {
		return numEspMigliori;
	}
	
	
}
