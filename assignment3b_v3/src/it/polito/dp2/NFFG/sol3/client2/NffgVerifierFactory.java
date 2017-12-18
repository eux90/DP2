package it.polito.dp2.NFFG.sol3.client2;

import java.net.URI;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriBuilder;

import it.polito.dp2.NFFG.NffgVerifier;
import it.polito.dp2.NFFG.NffgVerifierException;

public class NffgVerifierFactory extends it.polito.dp2.NFFG.NffgVerifierFactory{
	
	private final static String nffgUriSysProp = "it.polito.dp2.NFFG.lab3.URL";
	private final static String defaultNffgUri = "http://localhost:8080/NffgService/rest";
	private WebTarget target;

	@Override
	public NffgVerifier newNffgVerifier() throws NffgVerifierException {
		
		try{
			target = ClientBuilder.newClient().target(getBaseURI());
			return new MyNffgVerifier(target);
		} catch (Exception e){
			throw new NffgVerifierException("Could not instantiate NffgVerifier.");
		}
	}
	
	// get URI of service from system properties
	private static URI getBaseURI() {
		String uri = null;
		URI Uri;
		uri = System.getProperty(nffgUriSysProp);
		if(uri == null){
			uri = defaultNffgUri;
		}
		Uri = UriBuilder.fromUri(uri).build();
		
		return Uri;
	  }

}
