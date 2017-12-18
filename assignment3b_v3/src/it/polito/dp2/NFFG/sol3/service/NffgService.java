package it.polito.dp2.NFFG.sol3.service;

import java.net.URI;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.UriBuilder;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class NffgService {

	private Map<String, NffgType> nffgDB;
	private Map<String, RchPolicyType> rchPolicyDB;
	private Map<String, TrvPolicyType> trvPolicyDB;
	private Map<String, Neo4jData> neo4jDB;
	private Neo4jService neo4j;
	private DatatypeFactory df;
	private static NffgDataVerifier verifier = new NffgDataVerifier();
	private static String basePath;
	private static Logger logger = Logger.getLogger(NffgService.class.getName());
	private static final String databasePath = "/resource/database";
	private static final String nffgsPath = "/resource/nffgs";
	private static final String policiesPath = "/resource/policies";
	private static final String verificationsPath = "/resource/verifications";
	private static final String sysPropURL = "it.polito.dp2.NFFG.lab3.URL";
	private static final String defaultPath = "http://localhost:8080/NffgService/rest";
	

	public NffgService() throws DatatypeConfigurationException, NullPointerException {
		nffgDB = NffgServiceDB.getNffgDB();
		rchPolicyDB = NffgServiceDB.getRchPolicyDB();
		trvPolicyDB = NffgServiceDB.getTrvPolicyDB();
		neo4jDB = NffgServiceDB.getNeo4jDB();
		basePath = getBaseURI().toString();
		try{
			neo4j = new Neo4jService();
			df = DatatypeFactory.newInstance();
		} catch(DatatypeConfigurationException e){
			logger.log(Level.SEVERE, "Failed instantiating Datatype Factory", e);
			throw new DatatypeConfigurationException(e);
		} catch(NullPointerException e){
			logger.log(Level.SEVERE, "Failed instantiating Neo4jService", e);
			throw new NullPointerException();
		}
	}

	// add and return a NFFG
	public NffgType loadNffg(NffgType nffg) 
			throws MyBadRequestException, MyForbiddenException, MyServerErrorException, Neo4jServiceException {

		if (nffg == null)
			throw new MyBadRequestException("No data.");
		
		// update the date to current date
		GregorianCalendar gc = new GregorianCalendar();
		XMLGregorianCalendar c = df.newXMLGregorianCalendar(gc);
		nffg.setDate(c);
		
		// add self link
		LinksType links = new LinksType();
		LinkRefType nffgLink = new LinkRefType();
		nffgLink.setRel("self");
		nffgLink.setHref(basePath + nffgsPath + "/" + nffg.getName());
		links.getLink().add(nffgLink);
		nffg.setLinks(links);

		// verified data
		NffgType verifiedNffg;
		
		// from here should be executed by one thread at a time
		// to avoid double entries on Neo4jDB
		// and replacement of element with same id in local DB
		synchronized(NffgServiceDB.getNffgSync()){
			
			// check if already exist
			verifiedNffg = verifier.verifyNewNffg(nffg);
	
			if (verifiedNffg == null) {
				throw new MyServerErrorException("Fatal error: data verifier returned null data.");
			}
			
			// load to Neo4j remote service
			Neo4jData neo4jData = neo4j.loadNffg(new MyNffgReader(verifiedNffg));
			if (neo4jData == null)
				throw new Neo4jServiceException("Exception occurred in Neo4j Service.");
	
			// add values to local database
			neo4jDB.put(verifiedNffg.getName(), neo4jData);
			nffgDB.put(verifiedNffg.getName(), verifiedNffg);
		}
		return verifiedNffg;
		
	}
	
	// add and return a policy
		public PolicyType loadPolicy(PolicyType policy) 
				throws MyForbiddenException, MyBadRequestException, MyServerErrorException {

			if (policy == null)
				throw new MyBadRequestException("No data.");
			
			// verify submitted data
			PolicyType verifiedData;
			
			// add links
			if(policy.getRchPolicy() != null){
				policy.getRchPolicy().setLinks(policyLinks(policy.getRchPolicy()));
			}
			else{
				policy.getTrvPolicy().setLinks(policyLinks(policy.getTrvPolicy()));
			}
			
			/* from here one thread at a time should run
			 * policies can be deleted at the same time without problems
			 * To avoid inconsistencies policies
			 * should not change name between verifier and put
			 * and NFFGs should not be deleted (both conditions cannot happen) */
			synchronized(NffgServiceDB.getPolicySync()){
				
				// check policy constraints, already exist etc..
				verifiedData = verifier.verifyNewPolicy(policy);
	
				// this should never happen
				if (verifiedData == null) {
					throw new MyServerErrorException("Fatal error: data verifier returned null data.");
				}
				
				// add policy to database and return submitted data
				RchPolicyType rchPolicy = verifiedData.getRchPolicy();
				TrvPolicyType trvPolicy = verifiedData.getTrvPolicy();
				if (rchPolicy == null) {
					trvPolicyDB.put(trvPolicy.getName(), trvPolicy);
				} else {
					rchPolicyDB.put(rchPolicy.getName(), rchPolicy);
				}
			}
			return verifiedData;
		}
		
		// modify and return a single policy
		public PolicyType modifyPolicy(PolicyType policy) 
				throws MyBadRequestException, MyForbiddenException, MyServerErrorException, MyNotFoundException {

			if (policy == null)
				throw new MyBadRequestException("No data.");
			
			// verify submitted data
			PolicyType verifiedData;
			
			/*
			 * To avoid inconsistencies NFFGs should not be removed between
			 * verifier and replace (that cannot happen).
			 * replace would not execute if the 
			 * selected policy has been deleted in the meantime
			 */
			verifiedData = verifier.verifyUpdatedPolicy(policy);

			// this should never happen
			if (verifiedData == null) {
				throw new MyServerErrorException("Fatal error: data verifier returned null data.");
			}
			
			
			RchPolicyType rch = verifiedData.getRchPolicy();
			TrvPolicyType trv = verifiedData.getTrvPolicy();
			String name = "";

			// if data is valid one of them is null (verifier check that)
			GeneralPolicyType test = null;
			if (rch != null){
				name = rch.getName();
				// add links
				rch.setLinks(policyLinks(rch));
				// replace policies in the local database only if policies with that
				// name were already present (Thread Safe)
				test = rchPolicyDB.replace(rch.getName(), rch);
			}
			else{
				name = trv.getName();
				// add links
				trv.setLinks(policyLinks(trv));
				// replace policies in the local database only if policies with that
				// name were already present (Thread Safe)
				test = trvPolicyDB.replace(trv.getName(), trv);
			}

			if(test == null){
				throw new MyNotFoundException("Policy: " + name + " is not in NffgService database.");
			}
			
			return verifiedData;
		}
		
		//return whole database
		public NffgInfoType getDatabase(){
			
			// create a data structure to be returned
			NffgInfoType nit = new NffgInfoType();
			NffgSetType nst = new NffgSetType();
			PolicySetType pst = new PolicySetType();
			RchPolicySetType rchPst = new RchPolicySetType();
			TrvPolicySetType trvPst = new TrvPolicySetType();
			pst.setRchPolicySet(rchPst);
			pst.setTrvPolicySet(trvPst);
			nit.setNffgSetType(nst);
			nit.setPolicySetType(pst);
			
			/* since NFFG cannot be removed no need to synchronize 
			 * if a policy is removed database cannot be inconsistent.
			 */
			nst.getNffgType().addAll(nffgDB.values());
			rchPst.getRchPolicy().addAll(rchPolicyDB.values());
			trvPst.getTrvPolicy().addAll(trvPolicyDB.values());
			
			//generate links
			LinksType links = new LinksType();
			LinkRefType databaseLink = new LinkRefType();
			LinkRefType nffgsLink = new LinkRefType();
			LinkRefType policiesLink = new LinkRefType();
			LinkRefType verificationsLink = new LinkRefType();
			
			databaseLink.setRel("self");
			databaseLink.setHref(basePath + databasePath);
			nffgsLink.setRel("nffgs");
			nffgsLink.setHref(basePath + nffgsPath + "/?all=true");
			policiesLink.setRel("policies");
			policiesLink.setHref(basePath + policiesPath + "/?all=true");
			verificationsLink.setRel("verifications");
			verificationsLink.setHref(basePath + verificationsPath + "/?all=true");
			
			links.getLink().add(databaseLink);
			links.getLink().add(nffgsLink);
			links.getLink().add(policiesLink);
			links.getLink().add(verificationsLink);
			
			nit.setLinks(links);
			
			return nit;
		}
		
		/* return the set of NFFGs in database
		 * since NFFGs cannot be removed is Thread safe
		 */
		public NffgSetType getNffgs(boolean all, List<String> names) 
				throws MyBadRequestException, MyNotFoundException, MyServerErrorException {
			
			// if no parameter are set BAD REQUEST
			if (all == false && names.isEmpty()) {
				throw new MyBadRequestException("Invalid query parameters, use: "
						+ "<a href=\"" + basePath + nffgsPath + "/?all=true\"> all=true </a>"
						+ "for the complete list of NFFGs or specify a list of NFFGs: id=name1&id=name2...");
			} 
			
			// if all = true return all the list of NFFGs
			else if (all == true) {
				// create data structure to be returned
				NffgSetType nst = new NffgSetType();
				
				// get all NFFGs database and return it
				nst.getNffgType().addAll(nffgDB.values());
				
				//generate links
				LinksType links = new LinksType();
				LinkRefType nffgsLink = new LinkRefType();
				LinkRefType policiesLink = new LinkRefType();
				LinkRefType verificationsLink = new LinkRefType();
				LinkRefType databaseLink = new LinkRefType();
				
				nffgsLink.setRel("self");
				nffgsLink.setHref(basePath + nffgsPath + "/?all=true");
				policiesLink.setRel("policies");
				policiesLink.setHref(basePath + policiesPath + "/?all=true");
				verificationsLink.setRel("verifications");
				verificationsLink.setHref(basePath + verificationsPath + "/?all=true");
				databaseLink.setRel("database");
				databaseLink.setHref(basePath + databasePath);
				
				links.getLink().add(nffgsLink);
				links.getLink().add(policiesLink);
				links.getLink().add(verificationsLink);
				links.getLink().add(databaseLink);
				
				nst.setLinks(links);
				
				return nst;
			} 
			
			// return specified list of NFFGs
			// this method is thread safe since NFFG removal is not implemented.
			else {
				
				// verify submitted names refer to existing NFFGs
				Set<String> verifiedNames;
				
				// checks that submitted IDs are in database
				verifiedNames = verifier.verifyNffgIDs(names);
				
				if (verifiedNames == null) {
					throw new MyServerErrorException("Fatal error: data verifier returned null data.");
				}
				
				// create data structure to be returned
				NffgSetType nst = new NffgSetType();

				// populate data structure and return it
				for (String n : verifiedNames) {
					nst.getNffgType().add(nffgDB.get(n));
				}
				
				//generate links
				LinksType links = new LinksType();
				LinkRefType policiesLink = new LinkRefType();
				LinkRefType verificationsLink = new LinkRefType();
				LinkRefType databaseLink = new LinkRefType();
				
				policiesLink.setRel("policies");
				policiesLink.setHref(basePath + policiesPath + "/?all=true");
				verificationsLink.setRel("verifications");
				verificationsLink.setHref(basePath + verificationsPath + "/?all=true");
				databaseLink.setRel("database");
				databaseLink.setHref(basePath + databasePath);
				
				links.getLink().add(policiesLink);
				links.getLink().add(verificationsLink);
				links.getLink().add(databaseLink);
				
				nst.setLinks(links);
				
				return nst;
			}
		}
		
		// return the list of Policies in database
		public PolicySetType getPolicies(boolean all, List<String> names) 
				throws MyBadRequestException, MyNotFoundException, MyServerErrorException {

			// if no parameter are set BAD REQUEST
			if (all == false && names.isEmpty()) {
				throw new MyBadRequestException("Invalid query parameters, use: "
						+ "<a href=\"" + basePath + policiesPath + "/?all=true\"> all=true </a>"
						+ "for the complete list of Policiess or specify a list of Policies: ?id=name1&id=name2...");
			} 
			// return all the list of Policies
			else if (all == true) {
				
				// create data structure to be returned
				PolicySetType pst = new PolicySetType();
				RchPolicySetType rchPst = new RchPolicySetType();
				TrvPolicySetType trvPst = new TrvPolicySetType();
				
				// get all database of policies
				rchPst.getRchPolicy().addAll(rchPolicyDB.values());
				trvPst.getTrvPolicy().addAll(trvPolicyDB.values());

				pst.setRchPolicySet(rchPst);
				pst.setTrvPolicySet(trvPst);
				
				//generate links
				LinksType links = new LinksType();
				LinkRefType policiesLink = new LinkRefType();
				LinkRefType nffgsLink = new LinkRefType();
				LinkRefType verificationsLink = new LinkRefType();
				LinkRefType databaseLink = new LinkRefType();
				
				policiesLink.setRel("self");
				policiesLink.setHref(basePath + policiesPath + "/?all=true");
				nffgsLink.setRel("nffgs");
				nffgsLink.setHref(basePath + nffgsPath + "/?all=true");
				verificationsLink.setRel("verifications");
				verificationsLink.setHref(basePath + verificationsPath + "/?all=true");
				databaseLink.setRel("database");
				databaseLink.setHref(basePath + databasePath);
				
				links.getLink().add(policiesLink);
				links.getLink().add(nffgsLink);
				links.getLink().add(verificationsLink);
				links.getLink().add(databaseLink);
				
				pst.setLinks(links);
				
				return pst;
			} 
			
			// return specified list of policies
			else {
				Set<String> verifiedNames;
				
				// create data structure to be returned
				PolicySetType pst = new PolicySetType();
				RchPolicySetType rchPst = new RchPolicySetType();
				TrvPolicySetType trvPst = new TrvPolicySetType();
				
				/* from "verifier" to "db.get", Policies should not be deleted 
				 * because by my implementation this method should
				 * raise an exception if one of the declared policies is not present in database */
				synchronized(NffgServiceDB.getRemoveSync()){
					
					// verify submitted names refer to existing policies
					verifiedNames = verifier.verifyPolicyIDs(names);
		
	
					// this should never happen
					if (verifiedNames == null) {
						throw new MyServerErrorException("Fatal error: data verifier returned null data.");
					}
					
	
					// populate data structure and return the policies
					for (String s : verifiedNames) {
						if (rchPolicyDB.containsKey(s))
							rchPst.getRchPolicy().add(rchPolicyDB.get(s));
						if (trvPolicyDB.containsKey(s))
							trvPst.getTrvPolicy().add(trvPolicyDB.get(s));
					}
				}
				
				//generate links
				LinksType links = new LinksType();
				LinkRefType nffgsLink = new LinkRefType();
				LinkRefType verificationsLink = new LinkRefType();
				LinkRefType databaseLink = new LinkRefType();
				
				nffgsLink.setRel("nffgs");
				nffgsLink.setHref(basePath + nffgsPath + "/?all=true");
				verificationsLink.setRel("verifications");
				verificationsLink.setHref(basePath + verificationsPath + "/?all=true");
				databaseLink.setRel("database");
				databaseLink.setHref(basePath + databasePath);
				
				links.getLink().add(nffgsLink);
				links.getLink().add(verificationsLink);
				links.getLink().add(databaseLink);
				
				pst.setLinks(links);
				pst.setRchPolicySet(rchPst);
				pst.setTrvPolicySet(trvPst);
				
				return pst;
			}

		}

	// return the list of verified policies in database
	public PolicySetType getVerifications(boolean all, List<String> names) 
			throws MyBadRequestException, Neo4jServiceException, MyNotFoundException, MyServerErrorException {

		// if no parameter are set BAD REQUEST
		if (all == false && names.isEmpty()) {
			throw new MyBadRequestException("Invalid query parameters, use: "
					+ "<a href=\"" + basePath + verificationsPath + "/?all=true\"> all=true </a>"
					+ "for executing and retrieve the complete list of Verified Policies or specify a list of"
					+ " Policies to be Verified: ?id=name1&id=name2...");
		}
		// if all = true return all the list of Verified RCH Policies
		else if (all == true) {
			
			// create data structure to be returned
			PolicySetType pst = new PolicySetType();
			RchPolicySetType rchPst = new RchPolicySetType();
			TrvPolicySetType trvPst = new TrvPolicySetType();
			
			for (RchPolicyType rpt : rchPolicyDB.values()) {
				// testReachability is thread safe but can return null if policy is deleted while it is executed
				GeneralPolicyType newGpt = testReachability(rpt);
				if(newGpt != null){
					rchPst.getRchPolicy().add((RchPolicyType)newGpt);
				}
			}
			for (TrvPolicyType tpt : trvPolicyDB.values()) {
				// testReachability is thread safe but can return null if policy is deleted while it is executed
				GeneralPolicyType newGpt = testReachability(tpt);
				if(newGpt != null){
					trvPst.getTrvPolicy().add((TrvPolicyType)newGpt);
				}
			}
			
			//generate links
			LinksType links = new LinksType();
			LinkRefType verificationsLink = new LinkRefType();
			LinkRefType policiesLink = new LinkRefType();
			LinkRefType nffgsLink = new LinkRefType();
			LinkRefType databaseLink = new LinkRefType();
			
			verificationsLink.setRel("self");
			verificationsLink.setHref(basePath + verificationsPath + "/?all=true");
			nffgsLink.setRel("nffgs");
			nffgsLink.setHref(basePath + nffgsPath + "/?all=true");
			policiesLink.setRel("policies");
			policiesLink.setHref(basePath + policiesPath + "/?all=true");
			databaseLink.setRel("database");
			databaseLink.setHref(basePath + databasePath);

			links.getLink().add(verificationsLink);
			links.getLink().add(nffgsLink);
			links.getLink().add(policiesLink);
			links.getLink().add(databaseLink);
			
			pst.setLinks(links);
			pst.setRchPolicySet(rchPst);
			pst.setTrvPolicySet(trvPst);
			return pst;
		}

		// return specified list of Verified RCH policies
		else {

			Set<String> verifiedNames;
			
			// create data structure to be returned
			PolicySetType pst = new PolicySetType();
			RchPolicySetType rchPst = new RchPolicySetType();
			TrvPolicySetType trvPst = new TrvPolicySetType();
			
			
			// from here Policies should not be deleted 
			// because by my implementation this method should
			// raise a Not Found exception if one of the declared policies is not present in database
			synchronized(NffgServiceDB.getRemoveSync()){
				// verify submitted names refer to existing policies
				verifiedNames = verifier.verifyPolicyIDs(names);
	
				// this should never happen
				if (verifiedNames == null) {
					throw new MyServerErrorException("Fatal error: data verifier returned null data.");
				}
				
				// test the policies in neo4j Service
				for (String s : verifiedNames) {
					if(rchPolicyDB.containsKey(s)){
						GeneralPolicyType newGpt = testReachability(rchPolicyDB.get(s));
						// this should never happen because sync blocks removal
						if(newGpt == null)
							throw new MyServerErrorException("Fatal error: problem with a concurent operation.");
						rchPst.getRchPolicy().add((RchPolicyType)newGpt);
					}
					if(trvPolicyDB.containsKey(s)){
						GeneralPolicyType newGpt = testReachability(trvPolicyDB.get(s));
						// this should never happen because sync blocks removal
						if(newGpt == null)
							throw new MyServerErrorException("Fatal error: problem with a concurent operation.");
						trvPst.getTrvPolicy().add((TrvPolicyType)newGpt);
					}
				}
			}
			
			//generate links
			LinksType links = new LinksType();
			LinkRefType policiesLink = new LinkRefType();
			LinkRefType nffgsLink = new LinkRefType();
			LinkRefType databaseLink = new LinkRefType();
			
			nffgsLink.setRel("nffgs");
			nffgsLink.setHref(basePath + nffgsPath + "/?all=true");
			policiesLink.setRel("policies");
			policiesLink.setHref(basePath + policiesPath + "/?all=true");
			databaseLink.setRel("database");
			databaseLink.setHref(basePath + databasePath);

			links.getLink().add(nffgsLink);
			links.getLink().add(policiesLink);
			links.getLink().add(databaseLink);
			
			pst.setLinks(links);
			pst.setRchPolicySet(rchPst);
			pst.setTrvPolicySet(trvPst);
			return pst;
		}

	}
	
	public PolicyType getVerification(String id) 
			throws MyNotFoundException, Neo4jServiceException {
		
		RchPolicyType rpt = rchPolicyDB.get(id);
		TrvPolicyType tpt = trvPolicyDB.get(id);
				
		// if policy exist one should be not null
		if (rpt == null && tpt == null) {
			throw new MyNotFoundException("Policy " + id + " is not in NffgService Database.");
		}
		
		if(rpt != null){
			RchPolicyType newRpt = (RchPolicyType) testReachability(rpt);
			if(newRpt == null){
				throw new MyNotFoundException("Policy: " + id + " is not in NffgService Database.");
			}
			
			// create data structure to be returned
			PolicyType pt = new PolicyType();
			pt.setLinks(verificationLinks(newRpt));
			pt.setRchPolicy(newRpt);
			return pt;
		} else {
			TrvPolicyType newTpt = (TrvPolicyType) testReachability(tpt);
			if(newTpt == null){
				throw new MyNotFoundException("Policy " + id + " is not in NffgService Database.");
			}
			// create data structure to be returned
			PolicyType pt = new PolicyType();
			pt.setLinks(verificationLinks(newTpt));
			pt.setTrvPolicy(newTpt);
			return pt;
		}
		
	}

	

	// return a single NFFG
	public NffgType getNffg(String id) throws MyNotFoundException {
		NffgType nt = nffgDB.get(id);
		// name NOT FOUND
		if (nt == null) {
			throw new MyNotFoundException("Nffg " + id + " is not in NffgService Database.");
		}
		//
		NffgType newNt = new NffgType();
		newNt.setName(nt.getName());
		newNt.setDate(nt.getDate());
		newNt.setLinkSet(nt.getLinkSet());
		newNt.setNodeSet(nt.getNodeSet());
		newNt.setLinks(addNffgLinks(nt));
		return newNt;
	}

	// return a single policy
	public PolicyType getPolicy(String id) throws MyNotFoundException {
		
		RchPolicyType rpt = rchPolicyDB.get(id);
		TrvPolicyType tpt = trvPolicyDB.get(id);
		
		// if policy exist one should be not null
		if (rpt != null) {
			
			// create data structure to be returned
			PolicyType pt = new PolicyType();
			pt.setLinks(policyWrapperLinks(rpt));
			pt.setRchPolicy(rpt);
			return pt;
		} 
		else if (tpt != null){	
			// create data structure to be returned
			PolicyType pt = new PolicyType();
			pt.setLinks(policyWrapperLinks(tpt));
			pt.setTrvPolicy(tpt);
			return pt;
		}
		else {
			throw new MyNotFoundException("Policy " + id + " is not in NffgService Database.");
		}
	}

	// remove and return a single policy
	public PolicyType removePolicy(String id) throws MyNotFoundException {
		
		// remove policy if present
		RchPolicyType rpt;
		TrvPolicyType tpt;
		synchronized(NffgServiceDB.getRemoveSync()){
			rpt = rchPolicyDB.remove(id);
			tpt = trvPolicyDB.remove(id);
		}
		if (rpt != null || tpt != null) {
			// create data structure to be returned
			PolicyType pt = new PolicyType();
			pt.setRchPolicy(rpt);
			pt.setTrvPolicy(tpt);
			return pt;
		} else {
			throw new MyNotFoundException("Policy " + id + " is not in NffgService Database.");
		}
	}

	// test Reachability on a policy and return the updated policy if it is still in DB, otherwise return null (is Thread safe)
	private GeneralPolicyType testReachability(GeneralPolicyType gpt) throws Neo4jServiceException {
		
		if(gpt == null){
			return null;
		}

		String nffg = gpt.getNffg();
		String srcNode = ((RchPolicyType)gpt).getSrcNode();
		String dstNode = ((RchPolicyType)gpt).getDstNode();
		String srcNodeId = neo4jDB.get(nffg).getNodes().get(srcNode);
		String dstNodeId = neo4jDB.get(nffg).getNodes().get(dstNode);
		
		// test Reachability
		boolean result = neo4j.testReachability(srcNodeId, dstNodeId);
		
		// create new verification result and update policy
		VerificationType vt = new ObjectFactory().createVerificationType();
		
		// get current date and set it to the verification time
		GregorianCalendar gc = new GregorianCalendar();
		XMLGregorianCalendar c = df.newXMLGregorianCalendar(gc);
		vt.setDate(c);
		
		if (result) {
			vt.setMessage("Path found between nodes in date: " + gc.getTime().toString());
		} else {
			vt.setMessage("No Path found between nodes in date: " + gc.getTime().toString());
		}
		if (gpt.isIsPositive()) {
			vt.setResult(result);
		} else {
			vt.setResult(!result);
		}
		gpt.setVerification(vt);
		
		// replace tested policy with new one, but only if it still exist
		GeneralPolicyType newGpt = null;
		if(gpt instanceof RchPolicyType && !(gpt instanceof TrvPolicyType))
			newGpt = rchPolicyDB.replace(gpt.getName(), (RchPolicyType)gpt);
		if(gpt instanceof TrvPolicyType)
			newGpt = trvPolicyDB.replace(gpt.getName(), (TrvPolicyType)gpt);
		
		return newGpt;
	}
	
	private LinksType addNffgLinks(NffgType nt){
		
		LinksType links = new LinksType();
		LinkRefType nffgLink = new LinkRefType();
		LinkRefType nffgsLink = new LinkRefType();
		LinkRefType policiesLink = new LinkRefType();
		LinkRefType verificationsLink = new LinkRefType();
		LinkRefType databaseLink = new LinkRefType();
		
		nffgLink.setRel("self");
		nffgLink.setHref(basePath + nffgsPath + "/" + nt.getName());		
		nffgsLink.setRel("nffgs");
		nffgsLink.setHref(basePath + nffgsPath + "/?all=true");		
		policiesLink.setRel("policies");
		policiesLink.setHref(basePath + policiesPath + "/?all=true");		
		verificationsLink.setRel("verifications");
		verificationsLink.setHref(basePath + verificationsPath + "/?all=true");		
		databaseLink.setRel("database");
		databaseLink.setHref(basePath + databasePath);		
		links.getLink().add(nffgLink);
		links.getLink().add(nffgsLink);
		links.getLink().add(policiesLink);
		links.getLink().add(verificationsLink);
		links.getLink().add(databaseLink);
		
		return links;
		
	}
	
	private LinksType policyLinks(GeneralPolicyType gpt){
		
		LinksType links = new LinksType();
		LinkRefType policyLink = new LinkRefType();
		LinkRefType nffgLink = new LinkRefType();
		LinkRefType verificationLink = new LinkRefType();
		
		policyLink.setRel("self");
		policyLink.setHref(basePath + policiesPath + "/" + gpt.getName());			
		nffgLink.setRel("referenced-nffg");
		nffgLink.setHref(basePath + nffgsPath + "/" + gpt.getNffg());
		verificationLink.setRel("verification");
		verificationLink.setHref(basePath + verificationsPath + "/" + gpt.getName());
		
		links.getLink().add(policyLink);
		links.getLink().add(nffgLink);
		links.getLink().add(verificationLink);
		
		return links;
		
	}
	
	
	private LinksType policyWrapperLinks(GeneralPolicyType gpt){
		
		LinksType links = new LinksType();
		LinkRefType nffgsLink = new LinkRefType();
		LinkRefType policiesLink = new LinkRefType();
		LinkRefType verificationsLink = new LinkRefType();
		LinkRefType databaseLink = new LinkRefType();
		
		
		nffgsLink.setRel("nffgs");
		nffgsLink.setHref(basePath + nffgsPath + "/?all=true");		
		policiesLink.setRel("policies");
		policiesLink.setHref(basePath + policiesPath + "/?all=true");		
		verificationsLink.setRel("verifications");
		verificationsLink.setHref(basePath + verificationsPath + "/?all=true");		
		databaseLink.setRel("database");
		databaseLink.setHref(basePath + databasePath);		
		links.getLink().add(nffgsLink);
		links.getLink().add(policiesLink);
		links.getLink().add(verificationsLink);
		links.getLink().add(databaseLink);
		
		return links;
	}
	
	private LinksType verificationLinks(GeneralPolicyType gpt){
		
		LinksType links = new LinksType();
		LinkRefType verificationLink = new LinkRefType();
		LinkRefType nffgsLink = new LinkRefType();
		LinkRefType policiesLink = new LinkRefType();
		LinkRefType verificationsLink = new LinkRefType();
		LinkRefType databaseLink = new LinkRefType();
		
		verificationLink.setRel("self");
		verificationLink.setHref(basePath + verificationsPath + "/" + gpt.getName());			
		nffgsLink.setRel("nffgs");
		nffgsLink.setHref(basePath + nffgsPath + "/?all=true");		
		policiesLink.setRel("policies");
		policiesLink.setHref(basePath + policiesPath + "/?all=true");		
		verificationsLink.setRel("verifications");
		verificationsLink.setHref(basePath + verificationsPath + "/?all=true");		
		databaseLink.setRel("database");
		databaseLink.setHref(basePath + databasePath);		
		links.getLink().add(verificationLink);
		links.getLink().add(nffgsLink);
		links.getLink().add(policiesLink);
		links.getLink().add(verificationsLink);
		links.getLink().add(databaseLink);
		
		return links;
		
	}
	
	// get URI of service from system properties
		private static URI getBaseURI() {
			String uri=null;
			URI Uri;
			uri = System.getProperty(sysPropURL);
			if(uri == null){
				uri = defaultPath;
			}
			Uri = UriBuilder.fromUri(uri).build();
						
			return Uri;
		}

}
