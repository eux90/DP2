package it.polito.dp2.NFFG.sol3.service;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NffgServiceDB {
	
	//NFFGs database
	private static Map<String, NffgType> nffgDB = new ConcurrentHashMap<>();
	private static Map<String, RchPolicyType> rchPolicyDB = new ConcurrentHashMap<>();
	private static Map<String, TrvPolicyType> trvPolicyDB = new ConcurrentHashMap<>();
	
	// Neo4j support database
	private static Map<String, Neo4jData> neo4jDB = new ConcurrentHashMap<String, Neo4jData>();
	
	
	public static Object getNffgSync(){
		return nffgDB;
	}
	
	public static Object getPolicySync(){
		return rchPolicyDB;
	}
	
	public static Object getRemoveSync(){
		return trvPolicyDB;
	}
	
	public static Map<String, Neo4jData> getNeo4jDB(){
		return neo4jDB;
	}
	
	public static Map<String, NffgType> getNffgDB(){
		return nffgDB;
	}
	
	public static Map<String, RchPolicyType> getRchPolicyDB(){
		return rchPolicyDB;
	}
	
	public static Map<String, TrvPolicyType> getTrvPolicyDB(){
		return trvPolicyDB;
	}
	
}
