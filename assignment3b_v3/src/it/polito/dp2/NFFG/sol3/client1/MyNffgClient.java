package it.polito.dp2.NFFG.sol3.client1;

import java.util.Set;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import it.polito.dp2.NFFG.NffgReader;
import it.polito.dp2.NFFG.NffgVerifier;
import it.polito.dp2.NFFG.PolicyReader;
import it.polito.dp2.NFFG.lab3.AlreadyLoadedException;
import it.polito.dp2.NFFG.lab3.ServiceException;
import it.polito.dp2.NFFG.lab3.UnknownNameException;

public class MyNffgClient implements it.polito.dp2.NFFG.lab3.NFFGClient{
	
	private NffgVerifier monitor;
	private WebTarget target;
	private MySerializer serializer;
	private final static String nffgsPath = "/resource/nffgs";
	private final static String policiesPath = "/resource/policies";
	private final static String verificationsPath = "/resource/verifications";
	
	
	public MyNffgClient(NffgVerifier monitor, WebTarget target, MySerializer serializer) {
		this.monitor = monitor;
		this.target = target;
		this.serializer = serializer;
	}

	@Override
	public void loadNFFG(String name) throws UnknownNameException, AlreadyLoadedException, ServiceException {
		NffgReader nr = monitor.getNffg(name);
		if(nr == null){
			throw new UnknownNameException("Nffg " + name + " is not known locally.");
		}
		Response response;
		NffgType nt = serializer.buildNffg(nr);
		try{
			 response = target.path(nffgsPath).request().post(Entity.entity(nt, MediaType.APPLICATION_XML));
		}
		catch (Exception e){
			throw new ServiceException("Error while trying to load NFFG. " + e.getLocalizedMessage());
		}
		//NFFG already exist
		if(response != null && response.getStatus() == 403){
			String err;
			err = response.readEntity(String.class);
			throw new AlreadyLoadedException(err);
		}
		//Any other error
		else if(response == null || response.getStatus() != 201){
			String err = "no response";
			if(response != null){
				err = response.readEntity(String.class);
			}
			throw new ServiceException("Error while trying to load NFFG. " + err);
		}
	}

	@Override
	public void loadAll() throws AlreadyLoadedException, ServiceException {
		Set<NffgReader> nrs = monitor.getNffgs();
		Set<PolicyReader> prs = monitor.getPolicies();
		for(NffgReader nr : nrs){
			NffgType nt = serializer.buildNffg(nr);
			Response response;
			try{
				response = target.path(nffgsPath).request().post(Entity.entity(nt, MediaType.APPLICATION_XML));
			}
			catch (Exception e){
				throw new ServiceException("Error while trying to load NFFG. " + e.getLocalizedMessage());
			}
			//NFFG already exist
			if(response != null && response.getStatus() == 403){
				String err;
				err = response.readEntity(String.class);
				throw new AlreadyLoadedException(err);
			}
			//Any other error
			if(response == null || response.getStatus() != 201){
				String err = "no response";
				if(response != null){
					err = response.readEntity(String.class);
				}
				throw new ServiceException("Error while trying to load NFFG. " + err);
			}
		}
		for(PolicyReader pr : prs){
			PolicyType pt = serializer.buildPolicy(pr);
			Response response;
			try{
				response = target.path(policiesPath).request().post(Entity.entity(pt, MediaType.APPLICATION_XML));
			}
			catch (Exception e){
				throw new ServiceException("Error while trying to load a Policy " + e.getLocalizedMessage());
			}
			if(response != null && response.getStatus() == 403){
				//policy may already exist try to overwrite
				System.out.println("Trying to update policy...");
				try{
					response = target.path(policiesPath).request().put(Entity.entity(pt, MediaType.APPLICATION_XML));
				} catch (Exception e){
						throw new ServiceException("Error while trying to update a Policy." + e.getLocalizedMessage());
				}
				//Any error in updating policy
				if(response == null || response.getStatus() != 200){
					String err = "no response";
					if(response != null){
						err = response.readEntity(String.class);
					}
					throw new ServiceException("Error while trying to update a Policy " + err);
				}
				System.out.println("Policy Updated!");
			}
			//Any other error
			else if(response == null || response.getStatus() != 201){
				String err = "no response";
				if(response != null){
					err = response.readEntity(String.class);
				}
				throw new ServiceException("Error while trying to load a Policy " + err);
			}
		}
	}

	@Override
	public void loadReachabilityPolicy(String name, String nffgName, boolean isPositive, String srcNodeName,
			String dstNodeName) throws UnknownNameException, ServiceException {
		// TODO Auto-generated method stub
		PolicyType pt = serializer.buildRchPolicy(name, nffgName, isPositive, srcNodeName, dstNodeName);
		Response response;
		try{
			response = target.path(policiesPath).request().post(Entity.entity(pt, MediaType.APPLICATION_XML));
		}
		catch (Exception e){
			throw new ServiceException("Error while trying to load a Policy." + e.getLocalizedMessage());
		}
		if(response != null && response.getStatus() == 403){
			//policy may already exist try to overwrite
			System.out.println("Trying to update policy... ");
			try{
				response = target.path(policiesPath).request().put(Entity.entity(pt, MediaType.APPLICATION_XML));
			} catch (Exception e){
					throw new ServiceException("Error while trying to update a Policy." + e.getLocalizedMessage());
			}
			//FORBIDDEN policy do no respect some database constraint
			if(response != null && response.getStatus() == 403){
				String err;
				err = response.readEntity(String.class);
				throw new UnknownNameException(err);
			}
			//any other error
			else if(response == null || response.getStatus() != 200){
				String err = "no response";
				if(response != null){
					err = response.readEntity(String.class);
				}
				throw new ServiceException("Error while trying to update a Policy " + err);
			}
			System.out.println("Policy Updated!");
		}
		//any other error
		else if(response == null || response.getStatus() != 201){
			String err = "no response";
			if(response != null){
				err = response.readEntity(String.class);
			}
			throw new ServiceException("Error while trying to load a Policy " + err);
		}
		
	}

	@Override
	public void unloadReachabilityPolicy(String name) throws UnknownNameException, ServiceException {
		Response response;
		try{
			response = target.path(policiesPath).path(name).request().delete();
		}
		catch (Exception e){
			throw new ServiceException("Error while trying to delete a Policy " + e.getLocalizedMessage());
		}
		if(response != null && response.getStatus() == 404){
			String err;
			err = response.readEntity(String.class);
			throw new UnknownNameException(err);
		}
		else if(response == null || response.getStatus() != 200){
			String err = "no response";
			if(response != null){
				err = response.readEntity(String.class);
			}
			throw new ServiceException("Error while trying to unload a Policy " + err);
		}
		
	}

	@Override
	public boolean testReachabilityPolicy(String name) throws UnknownNameException, ServiceException {
		// TODO Auto-generated method stub
		Response response;
		try{
			response = target.path(verificationsPath).path(name).request().get();
		}
		catch (Exception e){
			throw new ServiceException("Error while trying to get a verification result." + e.getLocalizedMessage());
		}
		if(response != null && response.getStatus() == 404){
			String err;
			err = response.readEntity(String.class);
			System.err.println(err);
			throw new UnknownNameException(err);
		}
		else if(response == null || response.getStatus() != 200){
			String err = "no response";
			if(response != null){
				err = response.readEntity(String.class);
			}
			throw new ServiceException("Error while trying to get a verification result " + err);
		}
		
		PolicyType pst;
		RchPolicyType rpt;
		TrvPolicyType tpt;
		
		try{
			pst = (PolicyType)response.readEntity(PolicyType.class);
		} catch(ProcessingException e){
			throw new ServiceException("Error while trying to read the verification result " + e.getLocalizedMessage());
		} catch(IllegalStateException e){
			throw new ServiceException("Error while trying to read the verification result " + e.getLocalizedMessage());
		}
		
		rpt = pst.getRchPolicy();
		tpt = pst.getTrvPolicy();
		
		if(rpt != null){
			return rpt.getVerification().isResult();
		} else{
			return tpt.getVerification().isResult();
		}
	}

}
