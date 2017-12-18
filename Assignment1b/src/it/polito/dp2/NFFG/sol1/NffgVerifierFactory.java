package it.polito.dp2.NFFG.sol1;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;
import static javax.xml.datatype.DatatypeConstants.FIELD_UNDEFINED;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import it.polito.dp2.NFFG.NffgVerifier;
import it.polito.dp2.NFFG.NffgVerifierException;
import it.polito.dp2.NFFG.sol1.jaxb.GeneralPolicyType;
import it.polito.dp2.NFFG.sol1.jaxb.NffgInfoType;
import it.polito.dp2.NFFG.sol1.jaxb.NffgType;
import it.polito.dp2.NFFG.sol1.jaxb.NodeType;
import it.polito.dp2.NFFG.sol1.jaxb.RchPolicyType;
import it.polito.dp2.NFFG.sol1.jaxb.VerificationType;

public class NffgVerifierFactory extends it.polito.dp2.NFFG.NffgVerifierFactory {
	
	private final static String fileSysProp = "it.polito.dp2.NFFG.sol1.NffgInfo.file";
	private final static String jaxbPackage = "it.polito.dp2.NFFG.sol1.jaxb";
	private final static String schemaPath = "xsd/nffgInfo.xsd";

	@Override
	public NffgVerifier newNffgVerifier() throws NffgVerifierException {

		try {
			String inputFile = System.getProperty(fileSysProp);

			if (!inputFile.endsWith(".xml")) {
				throw new NffgVerifierException("File extension should be .xml");
			}

			JAXBContext jc = JAXBContext.newInstance(jaxbPackage);

			// create an Unmarshaller
			Unmarshaller u = jc.createUnmarshaller();
			try {
				SchemaFactory sf = SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
				Schema schema = sf.newSchema(new File(schemaPath));
				u.setSchema(schema);
			} catch (SAXException se) {
				System.err.println("Unable to validate invalid schema.");
				throw new NffgVerifierException(se);
			} catch (NullPointerException npe) {
				System.err.println("No schema found.");
				throw new NffgVerifierException(npe);
			} catch (UnsupportedOperationException uoe) {
				System.err.println("JAXB 1.0 mapped classes, please use JAXB 2.0.");
				throw new NffgVerifierException(uoe);
			}

			// class cast exception is catch
			@SuppressWarnings("unchecked")
			JAXBElement<NffgInfoType> poe = (JAXBElement<NffgInfoType>) u.unmarshal(new File(inputFile));

			// further checks on data not possible in Schema.
			if (!furtherChecks(poe.getValue())) {
				throw new NffgVerifierException("Some custom validity check has failed.");
			}

			return new MyNffgVerifier(poe.getValue());

		} catch (NullPointerException ne) {
			System.err.println("Specify the input file in the system property it.polito.dp2.NFFG.sol1.NffgInfo.file");
			throw new NffgVerifierException(ne);
		} catch (IllegalArgumentException iae) {
			System.err.println("Unmarshal input file is null.");
			throw new NffgVerifierException(iae);
		} catch (SecurityException se) {
			System.err.println("Could not access the system property.");
			throw new NffgVerifierException(se);
		} catch (ClassCastException cce) {
			System.err.println("Error in casting the root element to the right type.");
			throw new NffgVerifierException(cce);
		} catch (UnmarshalException ue) {
			System.err.println("Error in unmarshalling the xml.");
			System.err.println(ue.getCause().getLocalizedMessage());
			throw new NffgVerifierException(ue);
		} catch (JAXBException je) {
			System.err.println("Error in JAXB");
			System.err.println(je.getCause().getLocalizedMessage());
			throw new NffgVerifierException(je);
		}
	}

	// check if srcNode and dstNode of a policy are in the corresponding NFFG
	private boolean checkPolicySrcDst(RchPolicyType p, NffgInfoType nfi) {
		int cnt = 0;
		for (NffgType nf : nfi.getNffgSet().getNffg()) { // iterate on all NFFG
			if (nf.getName().equals(p.getNffg())) { // do the match test for
													// srcNode and dstNode
				for (NodeType n : nf.getNodeSet().getNode()) {
					if ((n.getName().equals(p.getSrcNode())) || (n.getName().equals(p.getDstNode()))) {
						cnt++;
					}
				}
				break; // a policy cannot be in 2 different NFFG, can break main
						// loop
			}
		}
		if (cnt == 2)
			return true; // both srcNode and dstNode are present in the NFFG
		else
			return false;
	}

	private boolean furtherChecks(NffgInfoType nfi) {
		List<NffgType> nfl = nfi.getNffgSet().getNffg();
		// check NFFG dates
		for (NffgType nf : nfl) {
			XMLGregorianCalendar xgc = nf.getDate();
			if (FIELD_UNDEFINED == xgc.getTimezone()) {
				System.err.println("Invalid date format in NFFG " + nf.getName() + ", no Time Zone specified.");
				return false;
			}
		}

		// make a unique list to check all policies together
		List<GeneralPolicyType> lgp = new ArrayList<>();

		lgp.addAll(nfi.getPolicySet().getRchPolicySet().getRchPolicy());
		lgp.addAll(nfi.getPolicySet().getTrvPolicySet().getTrvPolicy());

		// check Policies
		for (GeneralPolicyType gp : lgp) {
			// check srcNode and dstNode
			if (gp instanceof RchPolicyType) {
				if (!checkPolicySrcDst((RchPolicyType) gp, nfi)) {
					System.err.println("Source or Destination node in Policy " + gp.getName()
							+ " are not in the corresponding NFFG.");
					return false;
				}
				// check verification date (but only if it has been already
				// verified)
				VerificationType vt = gp.getVerification();
				if (vt != null) {
					XMLGregorianCalendar xgc = vt.getDate();
					if (FIELD_UNDEFINED == xgc.getTimezone()) {
						System.err
								.println("Invalid date format in Policy " + gp.getName() + ", no Time Zone specified.");
						return false;
					}
				}
				/*
				if (gp instanceof TrvPolicyType) {
					// check that list of traversed functionalities is not empty
					List<FuncType> lf = ((TrvPolicyType) gp).getFunctionList();
					if (lf.isEmpty()) {
						System.err.println("Functionalities List of Policy " + gp.getName() + " is empty.");
						return false;
					}
				}
				*/
			}
		}

		return true;
	}

}
