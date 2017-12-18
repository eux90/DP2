package it.polito.dp2.NFFG.sol2;

import java.net.URI;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriBuilder;

import it.polito.dp2.NFFG.FactoryConfigurationError;
import it.polito.dp2.NFFG.NffgVerifier;
import it.polito.dp2.NFFG.NffgVerifierException;
import it.polito.dp2.NFFG.NffgVerifierFactory;
import it.polito.dp2.NFFG.lab2.ReachabilityTester;
import it.polito.dp2.NFFG.lab2.ReachabilityTesterException;

public class ReachabilityTesterFactory extends it.polito.dp2.NFFG.lab2.ReachabilityTesterFactory{
	
	private WebTarget target;
	private NffgVerifier monitor;
	private final static String uriSysProp = "it.polito.dp2.NFFG.lab2.URL";

	@Override
	public ReachabilityTester newReachabilityTester() throws ReachabilityTesterException {
		
		
		try {
			target = ClientBuilder.newClient().target(getBaseURI());
			monitor = NffgVerifierFactory.newInstance().newNffgVerifier();
			
		} catch (NffgVerifierException | FactoryConfigurationError | NullPointerException e) {
			System.err.println("Could not instantiate NffgVerifier and/or WebTarget");
			throw new ReachabilityTesterException(e.getMessage());
		}
		return new MyReachabilityTester(target, monitor);
	}
	
	private static URI getBaseURI() {
		
		String uri;
		URI Uri;
		
		try{
			uri = System.getProperty(uriSysProp);
			Uri = UriBuilder.fromUri(uri).build();
		}
		catch(SecurityException | NullPointerException | IllegalArgumentException e) {
			System.err.println("Could not get URI from System Properties.");
			return null;
		}
		
		return Uri;
	  }

}
