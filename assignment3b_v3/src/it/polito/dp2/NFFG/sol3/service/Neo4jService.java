package it.polito.dp2.NFFG.sol3.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import it.polito.dp2.NFFG.LinkReader;
import it.polito.dp2.NFFG.NffgReader;
import it.polito.dp2.NFFG.NodeReader;
import it.polito.dp2.NFFG.lab2.server.lib.*;

public class Neo4jService {
	
	private final static String uriSysProp = "it.polito.dp2.NFFG.lab3.NEO4JURL";
	private final static String defaultUri = "http://localhost:8080/Neo4JXML/rest";
	private static Logger logger = Logger.getLogger(Neo4jService.class.getName());
	private WebTarget target;
	
	//clean neo4j database at startup
	static{
		WebTarget target;
		String respStr = null;
		try{
			target = ClientBuilder.newClient().target(getBaseURI());
			respStr = target.path("/resource/nodes").request().delete(String.class);
		} catch (ProcessingException e) {
			logger.log(Level.SEVERE, "Could not clean Neo4j remote database.", e);
		} catch (WebApplicationException e) {
			logger.log(Level.SEVERE, "Invalid response from Neo4j server while trying to clean remote database.", e);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Could not find Neo4j service.", e);
		}
		if (respStr == null) {
			logger.log(Level.SEVERE, "Cleaning of Neo4j remote database failed, could not get response from server.");
		}
		
	}
	
	public Neo4jService() throws NullPointerException{
		try{
			target = ClientBuilder.newClient().target(getBaseURI());
		} catch(NullPointerException e){
			logger.log(Level.SEVERE, "Failed read of URI for Neo4J Service, Could not instantiate Client.", e);
			throw new NullPointerException();
		}
	}

	public Neo4jData loadNffg(NffgReader nffg) throws Neo4jServiceException {
		
		if(nffg == null){
			throw new Neo4jServiceException("No data passed to Neo4j Service.");
		}

		// data to be received
		//String respStr;
		Node respNode;
		Relationship respRel;
		ArrayList<Node> insertedNodes = new ArrayList<>();

		// data to be sent
		Property p = new Property();
		Node n = new Node();
		Relationship rel = new Relationship();

		// get the nodes if NFFG has nodes
		Set<NodeReader> nrs = nffg.getNodes();
		if (nrs == null) {
			return new Neo4jData();
		}

		// post nodes inside database
		//System.out.print("Posting Nodes... ");
		for (NodeReader nr : nrs) {

			// add node name to node properties
			List<Property> lp = n.getProperty();
			p.setName("name");
			p.setValue(nr.getName());
			lp.add(0, p);
			
			// clean previous response
			respNode = null;

			// post the node inside the database
			try {
				respNode = target
						.path("/resource/node")
						.request()
						.post(Entity.entity(n, MediaType.APPLICATION_XML),Node.class);
			} catch (ProcessingException e) {
				throw new Neo4jServiceException("Could not insert Node in Neo4j service remote database.");
			} catch (WebApplicationException e) {
				throw new Neo4jServiceException("Invalid response from Neo4j service while trying to insert a Node in remote database.");
			} catch (Exception e){
				throw new Neo4jServiceException("An error has occurred in Neo4j service while trying to insert a Node.");
			}

			// if response is not null add the node in local list of database
			// nodes
			if (respNode == null) {
				throw new Neo4jServiceException("Insertion of Node in Neo4j service failed, could not get response from server.");
			}
			insertedNodes.add(respNode);
		}
		//System.out.println("OK");

		// post the relationships between nodes
		//System.out.print("Posting Links... ");
		for (Node no : insertedNodes) {

			// get links of this node
			Set<LinkReader> lrs = nffg.getNode(no.getProperty().get(0).getValue()).getLinks();

			/*
			 * for each link belonging to this node get the corresponding node
			 * id of the destination node in Neo4j database and post the
			 * relationship
			 */
			for (LinkReader lr : lrs) {
				for (Node no2 : insertedNodes) {
					if (no2.getProperty().get(0).getValue().equals(lr.getDestinationNode().getName())) {
						rel.setDstNode(no2.getId());
						rel.setType("Link");
						
						// clean previous response
						respRel = null;
						
						try {
							respRel = target
									.path("/resource/node/" + no.getId() + "/relationship")
									.request()
									.post(Entity.entity(rel, MediaType.APPLICATION_XML), Relationship.class);
						} catch (ProcessingException e) {
							throw new Neo4jServiceException("Could not insert Link relation in Neo4j service remote database.");
						} catch (WebApplicationException e) {
							throw new Neo4jServiceException("Invalid response from Neo4j service while trying to insert a Link relation in remote database.");
						} catch (Exception e){
							throw new Neo4jServiceException("An error has occurred in Neo4j service while trying to insert a Link relation.");
						}
						if (respRel == null) {
							throw new Neo4jServiceException("Insertion of Link relation in Neo4j service failed, could not get response from server.");
						}
						break;
					}
				}
			}
		}
		
		// set belonging NFFG
		
		// add a node that represent the NFFG
		Node respNffgNode = null;
		List<Property> lp = n.getProperty();
		p.setName("name");
		p.setValue(nffg.getName());
		lp.add(0, p);
		Labels labels = new Labels();
		labels.getValue().add("NFFG");
		String respStr = null;
		
		// post the node inside the database
		try {
			// insert node
			respNffgNode = target
					.path("/resource/node")
					.request()
					.post(Entity.entity(n, MediaType.APPLICATION_XML),Node.class);
			// insert label
			respStr = target
					.path("/resource/node/" + respNffgNode.getId() + "/label")
					.request()
					.post(Entity.entity(labels, MediaType.APPLICATION_XML), String.class);
			} catch (ProcessingException e) {
				throw new Neo4jServiceException("Could not insert NFFG in Neo4j service remote database.");
			} catch (WebApplicationException e) {
				throw new Neo4jServiceException("Invalid response from Neo4j service while trying to insert a NFFG in remote database.");
			} catch (Exception e){
				throw new Neo4jServiceException("An error has occurred in Neo4j service while trying to insert a NFFG.");
			}
		if(respStr == null){
			throw new Neo4jServiceException("An error has occurred in Neo4j service while trying to insert a NFFG label.");
		}
		
		// post relationships
		for(Node no : insertedNodes){
			rel.setDstNode(no.getId());
			rel.setType("belongs");
			respRel = null;
			try {
				respRel = target
						.path("/resource/node/" + respNffgNode.getId() + "/relationship")
						.request()
						.post(Entity.entity(rel, MediaType.APPLICATION_XML), Relationship.class);
			} catch (ProcessingException e) {
				throw new Neo4jServiceException("Could not insert NFFG belongs relation in Neo4j service remote database.");
			} catch (WebApplicationException e) {
				throw new Neo4jServiceException("Invalid response from Neo4j service while trying to insert a NFFG belongs relation.");
			} catch (Exception e){
				throw new Neo4jServiceException("An error has occurred in Neo4j service while trying to insert a NFFG belongs relation.");
			}
			if (respRel == null) {
				throw new Neo4jServiceException("Insertion of NFFG belongs relation in Neo4j service failed, could not get response from server.");
			}
			
		}
		
		// create a map <id, name> of the inserted nodes
		Map<String,String> map = new HashMap<String,String>();
		for(Node no : insertedNodes){
			map.put(no.getProperty().get(0).getValue(), no.getId());
		}
		
		// create the Neo4jData object
		return new Neo4jData(map, respNffgNode.getId());

	}
	
	
	public boolean testReachability(String srcId, String dstId) throws Neo4jServiceException{
		
		if(srcId == null || dstId == null){
			throw new Neo4jServiceException("No data passed to Neo4j Service.");
		}
		
		// paths of the database
		List<Path> existingPaths;
		try{
	    existingPaths = target
	    		.path("/resource/node/" + srcId + "/paths")
	    		.queryParam("dst", dstId)
	    		.request()
				.accept(MediaType.APPLICATION_XML)
				.get(new GenericType<List<Path>>() {});
		}
		catch(ProcessingException e){
			throw new Neo4jServiceException("Could not retrieve Paths from Neo4j remote database.");
		}
		catch(WebApplicationException e){
			throw new Neo4jServiceException("Invalid Response from Neo4j while trying to retrieve the list of Paths"
					+ " between Source and Destination Node.");
		}
		catch(Exception e){
			throw new Neo4jServiceException("Error from Neo4j while trying to retrieve the list of Paths"
					+ " between Source and Destination Node.");

		}
		
		//if there are no paths return false else return true
	    if(existingPaths == null || existingPaths.isEmpty()){
	    	return false;
	    }
	    else{
	    	return true;
	    }
	}
	
	// get URI of service from system properties
	private static URI getBaseURI() {
		String uri;
		URI Uri;
		uri = System.getProperty(uriSysProp);
		if(uri == null){
			uri = defaultUri;
		}
		Uri = UriBuilder.fromUri(uri).build();
		
		return Uri;
	  }
}
