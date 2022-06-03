package it.polito.tdp.genes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.genes.model.Adiacenza;
import it.polito.tdp.genes.model.Genes;
import it.polito.tdp.genes.model.Interactions;


public class GenesDao {
	
	public List<Genes> getAllGenes(){
		String sql = "SELECT DISTINCT GeneID, Essential, Chromosome FROM Genes";
		List<Genes> result = new ArrayList<Genes>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Genes genes = new Genes(res.getString("GeneID"), 
						res.getString("Essential"), 
						res.getInt("Chromosome"));
				result.add(genes);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			throw new RuntimeException("Database error", e) ;
		}
	}
	
	public List<Integer> getChromosomes() {
		String sql="SELECT DISTINCT Chromosome "
				+ "FROM genes "
				+ "WHERE Chromosome!=0";
		
		List<Integer> result = new ArrayList<Integer>();
		Connection conn = DBConnect.getConnection();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			
			while(res.next()) {
				result.add(res.getInt("Chromosome"));
			}
			
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Database error", e) ;
		}
	}
	
	public List<Adiacenza> getArchi() {
		String sql="SELECT T1.Chromosome, T2.Chromosome, SUM(DISTINCT T1.Expression_Corr) "
				+ "FROM ( "
				+ "SELECT * FROM interactions i1, genes g1 WHERE i1.GeneID1 = g1.GeneID ) AS T1, "
				+ "(SELECT * FROM interactions i2, genes g2 WHERE i2.GeneID2 = g2.GeneID ) AS T2 "
				+ "WHERE T1.GeneID1 = T2.GeneId1 AND T1.GeneID2 = T2.GeneId2 "
				+ "AND T1.Chromosome!=T2.Chromosome AND T1.Chromosome!=0 AND T2.Chromosome!=0 "
				+ "GROUP BY T1.Chromosome, T2.Chromosome";
		List<Adiacenza> result = new ArrayList<Adiacenza>();
		Connection conn = DBConnect.getConnection();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			
			while(res.next()) {
				result.add(new Adiacenza(res.getInt("T1.Chromosome"),
						res.getInt("T2.Chromosome"),
						res.getDouble("SUM(DISTINCT T1.Expression_Corr)")));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	


	
}
