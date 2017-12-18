package it.polito.dp2.NFFG.sol3.client1;

import java.util.Calendar;
import java.util.List;
import java.util.Set;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import it.polito.dp2.NFFG.FunctionalType;
import it.polito.dp2.NFFG.LinkReader;
import it.polito.dp2.NFFG.NffgReader;
import it.polito.dp2.NFFG.NodeReader;
import it.polito.dp2.NFFG.PolicyReader;
import it.polito.dp2.NFFG.ReachabilityPolicyReader;
import it.polito.dp2.NFFG.TraversalPolicyReader;


public class MySerializer {
	
	public PolicyType buildRchPolicy(String name, String nffgName, boolean isPositive, String srcNodeName, String dstNodeName){
		PolicyType pt = new PolicyType();
		RchPolicyType rpt = new RchPolicyType();
		rpt.setName(name);
		rpt.setNffg(nffgName);
		rpt.setIsPositive(isPositive);
		rpt.setSrcNode(srcNodeName);
		rpt.setDstNode(dstNodeName);
		
		pt.setRchPolicy(rpt);
		return pt;
	}
	
	
	public PolicyType buildPolicy(PolicyReader pr){
		
		//RchPolicyType rpt = new RchPolicyType();
		//RchPolicyType rpt = new RchPolicyType();
		
			GeneralPolicyType myPolicy;
			PolicyType pt = new PolicyType();
					
			if (pr instanceof ReachabilityPolicyReader && !(pr instanceof TraversalPolicyReader)) {
				
				myPolicy = new RchPolicyType();
				((RchPolicyType) myPolicy).setSrcNode(((ReachabilityPolicyReader) pr).getSourceNode().getName());
				((RchPolicyType) myPolicy).setDstNode(((ReachabilityPolicyReader) pr).getDestinationNode().getName());
				// append policy to RchPolicyList
				//myRchPolicyList.add((RchPolicyType) myPolicy);
				
			}
			else if (pr instanceof TraversalPolicyReader) {
				
				myPolicy = new TrvPolicyType();
				((TrvPolicyType) myPolicy).setSrcNode(((ReachabilityPolicyReader) pr).getSourceNode().getName());
				((TrvPolicyType) myPolicy).setDstNode(((ReachabilityPolicyReader) pr).getDestinationNode().getName());
				// add the Traversed Functional Types
				List<FuncType> myFunctionList = ((TrvPolicyType) myPolicy).getFunctionList();
				for (FunctionalType f : ((TraversalPolicyReader) pr).getTraversedFuctionalTypes()) {
					myFunctionList.add(FuncType.fromValue(f.name()));
				}
				// append policy to TrvPolicyList
				//myTrvPolicyList.add((TrvPolicyType) myPolicy);
			}
			else{
				return null;
			}
			myPolicy.setName(pr.getName());
			myPolicy.setNffg(pr.getNffg().getName());
			// if Policy has been verified add Verification
			if (pr.getResult() != null) {
				VerificationType myVt = new VerificationType();
				myVt.setResult(pr.getResult().getVerificationResult().booleanValue());
				myVt.setMessage(pr.getResult().getVerificationResultMsg());
				myVt.setDate(calendarToXMLGregorianCalendar(pr.getResult().getVerificationTime()));
				myPolicy.setVerification(myVt);
			}
			myPolicy.setIsPositive(pr.isPositive().booleanValue());
			
			if (pr instanceof ReachabilityPolicyReader && !(pr instanceof TraversalPolicyReader)) {
				pt.setRchPolicy((RchPolicyType)myPolicy);
			}
			else{
				pt.setTrvPolicy((TrvPolicyType)myPolicy);
			}
			
			
			return pt;

	}
	
	public NffgType buildNffg(NffgReader r){
		
		NffgType myNffg = new NffgType();
		// assign properties to NFFG
		myNffg.setName(r.getName());
		myNffg.setDate(calendarToXMLGregorianCalendar(r.getUpdateTime()));
		// retrieve the NodeSet
		myNffg.setNodeSet(getNffgNodeSet(r.getNodes()));
		// retrieve the LinkSet
		myNffg.setLinkSet(getNffgLinkSet(r.getNodes()));
		
		return myNffg;
		
		
	}
	
	// function to get all the Nodes belonging to an NFFG
		private NodeSetType getNffgNodeSet(Set<NodeReader> nrs) {
			// create a new NodeSet
			NodeSetType myNodeSet = new NodeSetType();
			// the list of Nodes belonging to this NodeSet
			List<NodeType> myNodeList = myNodeSet.getNode();

			for (NodeReader nr : nrs) {
				// crate a new Node
				NodeType myNode = new NodeType();
				// set Node properties
				myNode.setName(nr.getName());
				myNode.setType(FuncType.fromValue(nr.getFuncType().toString()));
				// append Node to the NodeList relative to the NodeSet
				myNodeList.add(myNode);
			}
			return myNodeSet;
		}

		// function to get all the Links belonging to an NFFG
		private LinkSetType getNffgLinkSet(Set<NodeReader> nrs) {
			// create a new LinkSet
			LinkSetType myLinkSet = new LinkSetType();
			// the list of Links belonging to this LinkSet
			List<LinkType> myLinkList = myLinkSet.getLink();

			for (NodeReader nr : nrs) {
				// get the Set of Links relative to a Node
				Set<LinkReader> lrs = nr.getLinks();

				// add each link to the LinkSet
				for (LinkReader lr : lrs) {
					// create a new Link
					LinkType myLink = new LinkType();
					// set the Link properties
					myLink.setName(lr.getName());
					myLink.setSrcNode(lr.getSourceNode().getName());
					myLink.setDstNode(lr.getDestinationNode().getName());
					// append Link to the LinkList relative to the LinkSet
					myLinkList.add(myLink);
				}
			}
			return myLinkSet;
		}
	
		/**
		 * 
		 * Converts Calendar object into XMLGregorianCalendar
		 *
		 * @param calendar
		 *            Object to be converted
		 * @return XMLGregorianCalendar
		 */
		private static XMLGregorianCalendar calendarToXMLGregorianCalendar(Calendar calendar) {
			try {
				DatatypeFactory dtf = DatatypeFactory.newInstance();
				XMLGregorianCalendar xgc = dtf.newXMLGregorianCalendar();
				xgc.setYear(calendar.get(Calendar.YEAR));
				xgc.setMonth(calendar.get(Calendar.MONTH) + 1);
				xgc.setDay(calendar.get(Calendar.DAY_OF_MONTH));
				xgc.setHour(calendar.get(Calendar.HOUR_OF_DAY));
				xgc.setMinute(calendar.get(Calendar.MINUTE));
				xgc.setSecond(calendar.get(Calendar.SECOND));
				xgc.setMillisecond(calendar.get(Calendar.MILLISECOND));

				// Calendar ZONE_OFFSET and DST_OFFSET fields are in milliseconds.
				int offsetInMinutes = (calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET))
						/ (60 * 1000);
				xgc.setTimezone(offsetInMinutes);
				return xgc;
			} catch (DatatypeConfigurationException e) {
				System.err.println("Some error occurred in date conversion to XMLCalendar type.");
				System.err.println(e.getMessage());
				return null;
			}

		}
	

}
