package it.polito.dp2.NFFG.sol3.client1;

import java.net.URI;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriBuilder;
import it.polito.dp2.NFFG.FactoryConfigurationError;
import it.polito.dp2.NFFG.NffgVerifier;
import it.polito.dp2.NFFG.NffgVerifierException;
import it.polito.dp2.NFFG.NffgVerifierFactory;
import it.polito.dp2.NFFG.lab3.NFFGClient;
import it.polito.dp2.NFFG.lab3.NFFGClientException;

public class NFFGClientFactory extends it.polito.dp2.NFFG.lab3.NFFGClientFactory{
	
	private final static String nffgUriSysProp = "it.polito.dp2.NFFG.lab3.URL";
	private final static String defaultNffgUri = "http://localhost:8080/NffgService/rest";
	private NffgVerifier monitor;
	private WebTarget target;
	private MySerializer serializer;

	@Override
	public NFFGClient newNFFGClient() throws NFFGClientException {
			try {
				monitor = NffgVerifierFactory.newInstance().newNffgVerifier();
				target = ClientBuilder.newClient().target(getBaseURI());
				serializer = new MySerializer();
				return new MyNffgClient(monitor, target, serializer);
			} catch (NffgVerifierException e) {
				throw new NFFGClientException("Could not instantiate NffgVerifier");
			} catch (FactoryConfigurationError e) {
				throw new NFFGClientException("Could not instantiate NffgVerifier");
			} catch(Exception e){
				throw new NFFGClientException("Could not instantiate Web Target");
			}
	}
	
	
	// get URI of service from system properties
			private static URI getBaseURI() {
				String uri=null;
				URI Uri;
				uri = System.getProperty(nffgUriSysProp);
				if(uri == null){
					uri = defaultNffgUri;
				}
				Uri = UriBuilder.fromUri(uri).build();
				
				return Uri;
			  }

}
