package it.polito.dp2.NFFG.sol1;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.MarshalException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import it.polito.dp2.NFFG.*;
import it.polito.dp2.NFFG.sol1.jaxb.*;

public class NffgInfoSerializer {
	
	private final static String jaxbPackage = "it.polito.dp2.NFFG.sol1.jaxb";
	private final static String schemaPath = "xsd/nffgInfo.xsd";
	private NffgVerifier monitor;

	/**
	 * Default constructor
	 * @throws FactoryConfigurationError 
	 * 
	 * @throws NffgVerifierException
	 */
	public NffgInfoSerializer() throws NffgVerifierException, FactoryConfigurationError {
		monitor = NffgVerifierFactory.newInstance().newNffgVerifier();
	}

	public NffgInfoSerializer(NffgVerifier monitor) {
		this.monitor = monitor;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		if(args.length != 1){
			System.err.println("Usage: file.xml");
			System.exit(1);
		}
		
		if(!args[0].endsWith(".xml")){
			System.err.println("File extension should be .xml");
			System.exit(1);
		}

		NffgInfoSerializer nis;
		try {
			nis = new NffgInfoSerializer();
			nis.printNffgInfo(args[0]);

		} catch (NffgVerifierException e) {
			System.err.println("Could not generate data: " + e.getLocalizedMessage());
			System.exit(1);
		} catch (FactoryConfigurationError e){
			System.err.println("Could not instantiate factory: " + e.getLocalizedMessage());
			System.exit(1);
		} 

	}

	public void printNffgInfo(String fileName) {
		NffgInfoType nit = new NffgInfoType();
		nit.setNffgSet(getNffgSet());
		nit.setPolicySet(getPolicySet());

		try {

			JAXBContext jc = JAXBContext.newInstance(jaxbPackage);

			// create an element for marshalling
			JAXBElement<NffgInfoType> nElement = (new ObjectFactory()).createNffgInfo(nit);

			Marshaller m = jc.createMarshaller();
			try{
				m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://NFFG.dp2.polito.it nffgInfo.xsd");
				SchemaFactory sf = SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
				Schema schema = sf.newSchema(new File(schemaPath));
				m.setSchema(schema);
			}
			catch (PropertyException pe){
				System.err.println("Error in processing a marshaller property.");
				System.err.println(pe.getCause().getLocalizedMessage());
				System.exit(1);
			} catch (SAXException e) {
				System.err.println("Error in parsing the provided schema");
				System.err.println(e.getLocalizedMessage());
				System.exit(1);
			} catch (NullPointerException e){
				System.err.println("Provided schema file not found.");
				System.exit(1);
			}

			// create a Marshaller and marshal to file
			m.marshal(nElement, new FileOutputStream(fileName));
			System.out.println("File " + fileName + " has been written.");

		}catch (ClassCastException cce){
			System.err.println("Root element is not cast to correct type.");
			System.err.println(cce.getLocalizedMessage());
			System.exit(1);
		}
		catch (MarshalException me){
			System.err.println("Exception in marshalling occurred.");
			System.err.println(me.getLocalizedMessage());
			System.exit(1);
		}
		catch (JAXBException je) {
			System.err.println("Exception in JAXB occurred.");
			System.err.println(je.getLocalizedMessage());
			System.exit(1);
		}
		catch (FileNotFoundException fnfe){
			System.err.println("Could not find the output file.");
			System.err.println(fnfe.getLocalizedMessage());
			System.exit(1);
		}catch (SecurityException se){
			System.err.println("Access to xml output file denied");
			System.err.println(se.getLocalizedMessage());
			System.exit(1);
		}
	}

	// function to get all the NFFGs
	private NffgSetType getNffgSet() {
		// get the set of NFFGs
		Set<NffgReader> nffgSet = monitor.getNffgs();
		// create the root for all NFFG
		NffgSetType myNffgSet = new NffgSetType();
		// instantiate the NffgList
		List<NffgType> myNffgList = myNffgSet.getNffg();

		for (NffgReader r : nffgSet) {
			// create an NFFG
			NffgType myNffg = new NffgType();
			// assign properties to NFFG
			myNffg.setName(r.getName());
			myNffg.setDate(calendarToXMLGregorianCalendar(r.getUpdateTime()));
			// retrieve the NodeSet
			myNffg.setNodeSet(getNffgNodeSet(r.getNodes()));
			// retrieve the LinkSet
			myNffg.setLinkSet(getNffgLinkSet(r.getNodes()));
			// add the NFFG to the NffgList
			myNffgList.add(myNffg);
		
		}
		return myNffgSet;
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

	// function to get all the Policies
	private PolicySetType getPolicySet() {
		// get the list of Policies
		Set<PolicyReader> policySet = monitor.getPolicies();
		// create the root for all Policies
		PolicySetType myPolicySet = new PolicySetType();
		// create the root for ReachabilityPolicies
		RchPolicySetType myRchPolicySet = new RchPolicySetType();
		// create the root for TraversalPolicies
		TrvPolicySetType myTrvPolicySet = new TrvPolicySetType();

		// List of ReachabilityPolicies and TraversalPolicies
		List<RchPolicyType> myRchPolicyList = myRchPolicySet.getRchPolicy();
		List<TrvPolicyType> myTrvPolicyList = myTrvPolicySet.getTrvPolicy();

		for (PolicyReader pr : policySet) {
			// set Policy properties
			GeneralPolicyType myPolicy;
					
			if (pr instanceof ReachabilityPolicyReader && !(pr instanceof TraversalPolicyReader)) {
				
				myPolicy = new RchPolicyType();
				((RchPolicyType) myPolicy).setSrcNode(((ReachabilityPolicyReader) pr).getSourceNode().getName());
				((RchPolicyType) myPolicy).setDstNode(((ReachabilityPolicyReader) pr).getDestinationNode().getName());
				// append policy to RchPolicyList
				myRchPolicyList.add((RchPolicyType) myPolicy);
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
				myTrvPolicyList.add((TrvPolicyType) myPolicy);
			}
			else{
				myPolicy = new GeneralPolicyType();
			}
			myPolicy.setName(pr.getName());
			myPolicy.setNffg(pr.getNffg().getName());
			// if Policy has been verified add Verification
			if (pr.getResult() != null) {
				VerificationType myVt = new VerificationType();
				myVt.setResult(pr.getResult().getVerificationResult().booleanValue());
				myVt.setMessage(pr.getResult().getVerificationResultMsg());
				XMLGregorianCalendar xgc = calendarToXMLGregorianCalendar(pr.getResult().getVerificationTime());
				myVt.setDate(xgc);
				myPolicy.setVerification(myVt);
			}
			myPolicy.setIsPositive(pr.isPositive().booleanValue());

		}

		// add to policy set the TraversalPolicies and ReachabilityPolicies
		myPolicySet.setRchPolicySet(myRchPolicySet);
		myPolicySet.setTrvPolicySet(myTrvPolicySet);

		return myPolicySet;
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
		if(calendar == null)
			return null;
		
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
