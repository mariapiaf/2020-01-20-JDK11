package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.artsmia.model.Arco;
import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.Artist;
import it.polito.tdp.artsmia.model.Exhibition;

public class ArtsmiaDAO {

	public List<ArtObject> listObjects() {
		
		String sql = "SELECT * from objects";
		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
						res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
						res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
						res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
				
				result.add(artObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Exhibition> listExhibitions() {
		
		String sql = "SELECT * from exhibitions";
		List<Exhibition> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Exhibition exObj = new Exhibition(res.getInt("exhibition_id"), res.getString("exhibition_department"), res.getString("exhibition_title"), 
						res.getInt("begin"), res.getInt("end"));
				
				result.add(exObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void getVertici(String ruolo, Map<Integer, Artist> idMap) {
		
		String sql = "SELECT ar.artist_id AS id, ar.name AS nome "
				+ "FROM authorship a, artists ar "
				+ "WHERE a.role = ? AND a.artist_id = ar.artist_id";

		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, ruolo);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(!idMap.containsKey(res.getInt("id"))) {
					Artist a = new Artist(res.getInt("id"), res.getString("nome"));
					idMap.put(a.getId(), a);
				}
			}
			conn.close();

			
		} catch (SQLException e) {
			e.printStackTrace();

		}
	}

	public List<Arco> getArchi(String ruolo, Map<Integer, Artist> idMap, Graph<Artist, DefaultWeightedEdge> grafo){
		String sql = "SELECT a1.artist_id AS id1, a2.artist_id AS id2, COUNT(DISTINCT eo1.exhibition_id) AS peso "
				+ "FROM artists a1, artists a2, exhibition_objects eo1, exhibition_objects eo2, authorship au1, authorship au2 "
				+ "WHERE eo1.exhibition_id = eo2.exhibition_id "
				+ "	AND au1.artist_id = a1.artist_id AND au2.artist_id = a2.artist_id "
				+ "	AND au1.object_id = eo1.object_id AND au2.object_id = eo2.object_id "
				+ "	AND au1.role = au2.role AND au1.role = ? "
				+ "	AND a1.artist_id > a2.artist_id "
				+ "GROUP BY a1.artist_id, a2.artist_id";
		
		Connection conn = DBConnect.getConnection();
		List<Arco> result = new ArrayList<>();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, ruolo);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Artist a1 = idMap.get(res.getInt("id1"));
				Artist a2 = idMap.get(res.getInt("id2"));
				if(grafo.containsVertex(a1) && grafo.containsVertex(a2)) {
					result.add(new Arco(a1, a2, res.getInt("peso")));
				}
			}
			conn.close();
			return result;

			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;

		}
		
	}
	
	public List<String> getRuoli(){
		String sql = "SELECT DISTINCT role "
				+ "FROM authorship";
		
		Connection conn = DBConnect.getConnection();
		List<String> result = new ArrayList<>();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				result.add(res.getString("role"));
			}
			conn.close();
			return result;

			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;

		}
	}

}
