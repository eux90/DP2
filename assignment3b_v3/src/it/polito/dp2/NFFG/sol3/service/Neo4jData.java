package it.polito.dp2.NFFG.sol3.service;

import java.util.HashMap;
import java.util.Map;

public class Neo4jData {
	private Map<String, String> nodes;
	private String nffgID;
	
	public Neo4jData(){
		setNodes(new HashMap<String, String>());
		setNffgID(null);
	}
	
	public Neo4jData(Map<String, String> nodes, String nffgID){
		this.setNodes(nodes);
		this.setNffgID(nffgID);
	}

	public Map<String, String> getNodes() {
		return nodes;
	}

	public void setNodes(Map<String, String> nodes) {
		this.nodes = nodes;
	}

	public String getNffgID() {
		return nffgID;
	}

	public void setNffgID(String nffgID) {
		this.nffgID = nffgID;
	}

}
