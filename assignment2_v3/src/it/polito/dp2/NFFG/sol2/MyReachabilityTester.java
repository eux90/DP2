package it.polito.dp2.NFFG.sol2;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import it.polito.dp2.NFFG.LinkReader;
import it.polito.dp2.NFFG.NffgReader;
import it.polito.dp2.NFFG.NffgVerifier;
import it.polito.dp2.NFFG.NodeReader;
import it.polito.dp2.NFFG.lab2.NoGraphException;
import it.polito.dp2.NFFG.lab2.ServiceException;
import it.polito.dp2.NFFG.lab2.UnknownNameException;

public class MyReachabilityTester implements it.polito.dp2.NFFG.lab2.ReachabilityTester{
	
	private WebTarget target;
	private NffgVerifier monitor;
	private String graphName;
	
	public MyReachabilityTester(WebTarget target, NffgVerifier monitor){
		this.target = target;
		this.monitor = monitor;
	}

	@Override
	public void loadNFFG(String name) throws UnknownNameException, ServiceException {
		
		// get NFFG by name if it exist
		NffgReader nfr = monitor.getNffg(name);
		if(nfr == null){
			throw new UnknownNameException("Cannot load NFFG, the name provided does not belong to the current NFFG set.");
		}
		
		// data to be received
		String respStr;
		Node respNode;
		Relationship respRel;
		ArrayList<Node> insertedNodes = new ArrayList<>();
		
		// data to be sent
		Property p = new Property();
		Node n = new Node();
		Relationship rel = new Relationship();
		
		
		// delete all nodes of previous NFFG (if any)
		System.out.print("Cleaning database... ");
		try{
			respStr = target
					.path("resource/nodes")
					.request()
					.delete(String.class);
		}
		catch(ProcessingException e){
			System.err.println("Could not clean Neo4j database.");
			throw new ServiceException(e);
		}
		catch(WebApplicationException e){
			System.err.println("Invalid Response from Server while trying to clean database.");
			throw new ServiceException(e);
		}
		if(respStr == null){
			System.err.println("Cleaning of database failed could not get response from Server.");
			throw new ServiceException();
		}
		System.out.println(respStr);
		
		// delete graph name
		graphName = null;
		
		// get the nodes if NFFG has nodes
		Set<NodeReader> nrs = nfr.getNodes();
		if(nrs == null){
			System.out.println("Warning: No Nodes in this NFFG.");
			// even if it has no nodes the NFFG has a name
			graphName = name;
			return;
		}
		
		
		// post nodes inside database
		System.out.print("Posting Nodes... ");
		for(NodeReader nr : nrs){
			
			// add node name to node properties
			List<Property> lp = n.getProperty();
			p.setName("name");
			p.setValue(nr.getName());
			lp.add(0, p);
			// clean previous response
			respNode = null;
			
			// post the node inside the database
			try{
			respNode = target
					.path("resource/node/")
					.request()
					.post(Entity.entity(n, MediaType.APPLICATION_XML), Node.class);
			}
			catch(ProcessingException e){
				System.err.println("Could not insert Node in Neo4j database.");
				throw new ServiceException(e);
			}
			catch(WebApplicationException e){
				System.err.println("Invalid Response from Server while trying to insert a Node.");
				throw new ServiceException(e);
			}
			
			// if response is not null add the node in local list of database nodes
			if(respNode == null){
				System.err.println("Post of Node failed could not get response from Server.");
				throw new ServiceException();
			}
			insertedNodes.add(respNode);
		}
		System.out.println("OK");
		
		// set the graph name to the current NFFG
		graphName = name;
		
		// post the relationships between nodes
		System.out.print("Posting Links... ");
		for(Node no : insertedNodes){
			
			// get links of this node
			Set<LinkReader> lrs = nfr.getNode(no.getProperty().get(0).getValue()).getLinks();
			
			/* for each link belonging to this node
			 * get the corresponding node id of the destination node
			 * in Neo4j database and post the relationship */
			for(LinkReader lr : lrs){
				for(Node no2 : insertedNodes){
					if(no2.getProperty().get(0).getValue().equals(lr.getDestinationNode().getName())){
						rel.setDstNode(no2.getId());
						rel.setType("Link");
						// clean previous response
						respRel = null;
						try{
						respRel = target
								.path("resource/node/" + no.getId() + "/relationship")
								.request()
								.post(Entity.entity(rel, MediaType.APPLICATION_XML), Relationship.class);
						}
						catch(ProcessingException e){
							System.err.println("Could not insert links in Neo4j database.");
							throw new ServiceException(e);
						}
						catch(WebApplicationException e){
							System.err.println("Invalid Response from Server while trying to insert a Link.");
							throw new ServiceException(e);
						}
						if(respRel == null){
							System.err.println("Post of link failed could not get response from Server.");
							throw new ServiceException();
						}
						break;
					}
				}
			}
		}
		System.out.println("OK");
	}

	@Override
	public boolean testReachability(String srcName, String destName)
			throws UnknownNameException, ServiceException, NoGraphException {
		
		// test if Source and Destination are valid string
		if(srcName == null || destName == null || srcName.isEmpty() || destName.isEmpty()){
			throw new UnknownNameException("Could not test for Reachability, invalid or missing source or/and destination name");
		}
		
		String srcId = new String();
		String destId = new String();
		
		// nodes and paths of the database
		List<Node> loadedNodes;
		List<Path> existingPaths;
		
		// get the nodes from Neo4j database
		try{
		loadedNodes = target
				.path("resource/nodes")
				.request()
				.accept(MediaType.APPLICATION_XML)
				.get(new GenericType<List<Node>>() {});
		}
		catch(ProcessingException e){
			System.err.println("Could not retrieve Nodes from Neo4j database.");
			throw new ServiceException(e);
		}
		catch(WebApplicationException e){
			System.err.println("Invalid Response from Server while trying to retrieve the Node list.");
			throw new ServiceException(e);
		}
		
		// check if there are nodes
		if(loadedNodes == null || loadedNodes.isEmpty()){
			throw new NoGraphException("Neo4j database is empty.");
		}
		
		// get the id's inside Neo4j database of Source and Destination nodes
		for(Node n : loadedNodes){
			if(srcName.equals(n.getProperty().get(0).getValue())){
				srcId = n.getId();
			}
			if(destName.equals(n.getProperty().get(0).getValue())){
				destId = n.getId(); 
			}
		}
		
		// check if those nodes are actually in the database
		if(srcId.isEmpty() || destId.isEmpty()){
			throw new UnknownNameException("Source and/or Destination node not present in Neo4j database.");
		}
		
		//System.out.println("Source id: " + srcId);
		//System.out.println("Destination id: " + destId);
		
		// get the paths between the nodes
		try{
	    existingPaths = target
	    		.path("resource/node/" + srcId + "/paths")
	    		.queryParam("dst", destId)
	    		.request()
				.accept(MediaType.APPLICATION_XML)
				.get(new GenericType<List<Path>>() {});
		}
		catch(ProcessingException e){
			System.err.println("Could not retrieve Paths from Neo4j database.");
			throw new ServiceException(e);
		}
		catch(WebApplicationException e){
			System.err.println("Invalid Response from Server while trying to retrieve the list of Paths"
					+ " between Source and Destination Node.");
			throw new ServiceException(e);
		}
		
		//if there are no paths return false else return true
	    if(existingPaths == null || existingPaths.isEmpty()){
	    	return false;
	    }
	    else{
	    	return true;
	    }
	}

	@Override
	public String getCurrentGraphName() {
		return graphName;
	}

}
