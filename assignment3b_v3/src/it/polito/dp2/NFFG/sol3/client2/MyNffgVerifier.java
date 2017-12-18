package it.polito.dp2.NFFG.sol3.client2;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import it.polito.dp2.NFFG.NffgReader;
import it.polito.dp2.NFFG.NffgVerifierException;
import it.polito.dp2.NFFG.PolicyReader;

public class MyNffgVerifier implements it.polito.dp2.NFFG.NffgVerifier{
	

	private WebTarget target;
	private final static String nffgsPath = "/resource/nffgs";
	private final static String databasePath = "/resource/database";
	
	public MyNffgVerifier(WebTarget target) throws NffgVerifierException{
		this.target = target;
	}

	// return a single NffgReader from NFFG name given in input or null if not present
	@Override
	public NffgReader getNffg(String name) {
		Response res;
		
		if(name == null){
			return null;
		}
		
		try{
			res = target.path(nffgsPath).path(name).request().get();
		} catch(Exception e){
			return null;
		}
		if(res == null || res.getStatus() != 200){
			return null;
		}
		
		NffgType nt = (NffgType) res.readEntity(NffgType.class);
		return new MyNffgReader(nt);
	}

	// return a set of NFFG reader representing all NFFGs in the Server
	@Override
	public Set<NffgReader> getNffgs() {
		Response res;

		Set<NffgReader> nrs = new HashSet<NffgReader>();
		
		try{
			res = target.path(nffgsPath).queryParam("all", true).request().get();
		} catch(Exception e){
			return nrs;
		}
		if(res == null || res.getStatus() != 200){
			return nrs;
		}
		
		NffgSetType nst = (NffgSetType) res.readEntity(NffgSetType.class);
		for(NffgType nt : nst.getNffgType()){
			nrs.add(new MyNffgReader(nt));
		}
		return nrs;
	}

	// return a set of Policy Readers representing all policies available in the server
	@Override
	public Set<PolicyReader> getPolicies() {
		Response res;
		
		// the set of policy readers to be returned
		Set<PolicyReader> prs = new HashSet<PolicyReader>();
		
		// get whole database
		try{
			res = target.path(databasePath).request().get();
		} catch(Exception e){
			return prs;
		}
		if(res == null || res.getStatus() != 200){
			return prs;
		}
		
		// all DB
		NffgInfoType nit = (NffgInfoType) res.readEntity(NffgInfoType.class);
		
		// all Policies
		PolicySetType pst = nit.getPolicySetType();
		
		// all NFFGs
		NffgSetType nst = nit.getNffgSetType();
		
		// create a peculiar policy reader RCH or TRV for each available policy
		for(RchPolicyType rpt : pst.getRchPolicySet().getRchPolicy()){
			prs.add(new MyReachabilityPolicyReader(rpt, nst.getNffgType()));
		}
		for(TrvPolicyType tpt : pst.getTrvPolicySet().getTrvPolicy()){
			prs.add(new MyTraversalPolicyReader(tpt, nst.getNffgType()));
		}
		
		return prs;
	}

	// return a set of Policies filtered by their belonging NFFGs or null if no Policy correspond
	@Override
	public Set<PolicyReader> getPolicies(String nffg) {
		
		if(nffg == null){
			return null;
		}

		Response res;
		
		try{
			res = target.path(databasePath).request().get();
		} catch(Exception e){
			return null;
		}
		if(res == null || res.getStatus() != 200){
			return null;
		}
		
		// all DB
		NffgInfoType nit = (NffgInfoType) res.readEntity(NffgInfoType.class);
		
		// all Policies
		PolicySetType pst = nit.getPolicySetType();
		
		// all NFFGs
		NffgSetType nst = nit.getNffgSetType();
		
		// Set of Policies to be returned if present
		Set<PolicyReader> prs = new HashSet<>();
		
		// for each Policy depending on the type RCH or TRV if their NFFG correspond load them to the set. 
		for(RchPolicyType rp : pst.getRchPolicySet().getRchPolicy()){
			if(rp.getNffg().equals(nffg))
				prs.add(new MyReachabilityPolicyReader(rp, nst.getNffgType()));
		}
		for(TrvPolicyType tp : pst.getTrvPolicySet().getTrvPolicy()){
			if(tp.getNffg().equals(nffg))
				prs.add(new MyTraversalPolicyReader(tp, nst.getNffgType()));
		}
		
		if(prs.isEmpty())
			return null;
		
		return prs;
	}

	// A set of PolicyReader for policies verified after a given date or null
	@Override
	public Set<PolicyReader> getPolicies(Calendar date) {
		
		if(date == null){
			return null;
		}
		
		Response res;
		
		try{
			res = target.path(databasePath).request().get();
		} catch(Exception e){
			System.err.println("Error while trying to get Policies from NffgService " + e.getLocalizedMessage());
			return null;
		}
		if(res == null || res.getStatus() != 200){
			System.err.println("Could not retrieve Policies from Server, empty Response.");
			return null;
		}
		
		// all DB
		NffgInfoType nit = (NffgInfoType) res.readEntity(NffgInfoType.class);
		
		// all Policies
		PolicySetType pst = nit.getPolicySetType();
				
		// all NFFGs
		NffgSetType nst = nit.getNffgSetType();
				
		// Set of Policies to be returned if present
		Set<PolicyReader> prs = new HashSet<>();
		
		// create a new Calendar
		Calendar c = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
		
		// get the PolicyReader for policies verified after a given date
		for(RchPolicyType rp : pst.getRchPolicySet().getRchPolicy()){
			VerificationType vt = rp.getVerification();
			// if a verification exist do the test
			if(vt != null){
				c.clear();
				c = vt.getDate().toGregorianCalendar();
				if(c.after(date))
					prs.add(new MyReachabilityPolicyReader(rp, nst.getNffgType()));
			}
		}
		for(TrvPolicyType tp : pst.getTrvPolicySet().getTrvPolicy()){
			VerificationType vt = tp.getVerification();
			// if a verification exist do the test
			if(vt != null){
				c.clear();
				c = vt.getDate().toGregorianCalendar();
				if(c.after(date))
					prs.add(new MyTraversalPolicyReader(tp, nst.getNffgType()));
			}
		}
		
		if(prs.isEmpty())
			return null;
		
		return prs;
	}

}
