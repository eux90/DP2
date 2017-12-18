package it.polito.dp2.NFFG.sol3.service;

import static javax.xml.datatype.DatatypeConstants.FIELD_UNDEFINED;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.datatype.XMLGregorianCalendar;

public class NffgDataVerifier {
	
	private Map<String, NffgType> nffgDB;
	private Map<String, RchPolicyType> rchPolicyDB;
	private Map<String, TrvPolicyType> trvPolicyDB;
	
	public NffgDataVerifier(){
		nffgDB = NffgServiceDB.getNffgDB();
		rchPolicyDB = NffgServiceDB.getRchPolicyDB();
		trvPolicyDB = NffgServiceDB.getTrvPolicyDB();
	}

	// check a new single NFFG
	public NffgType verifyNewNffg(NffgType nffg)
			throws MyForbiddenException {

		// check if a NFFG with this name is already loaded
		if (nffgDB.containsKey(nffg.getName()))
			throw new MyForbiddenException("NFFG: " + nffg.getName() + " already exist in NffgService database.");

		return nffg;
	}

	// check a new policy
	public PolicyType verifyNewPolicy(PolicyType policy) throws MyForbiddenException {

		String policyName;
		GeneralPolicyType gpt;

		// get the policy (we do not know if is TRV or RCH)
		gpt = policy.getRchPolicy();
		if (gpt == null) {
			gpt = policy.getTrvPolicy();
		}

		policyName = gpt.getName();

		// check policy name not duplicated in all the DB of policies
		// (both for RCH and TRV policies)
		if (rchPolicyDB.containsKey(policyName) || trvPolicyDB.containsKey(policyName))
			throw new MyForbiddenException("Policy: " + policyName + " already exist in NffgService database.");

		// check other policy/DB constraints
		checkPolicyConstraints(gpt);

		return policy;
	}

	// check a single policy to update
	public PolicyType verifyUpdatedPolicy(PolicyType policy) throws MyForbiddenException {

		// if XML is valid only one can be not null
		RchPolicyType rpt = policy.getRchPolicy();
		RchPolicyType tpt = policy.getTrvPolicy();

		if (rpt != null) {
			checkPolicyConstraints(rpt);
		}
		else {
			checkPolicyConstraints(tpt);
		}

		return policy;
	}

	// verify that a list of submitted NFFG names is valid (are all in DB)
	public Set<String> verifyNffgIDs(List<String> names) throws MyNotFoundException {

		Set<String> idSet = new HashSet<>();
		idSet.addAll(names);
		for (String id : idSet) {
			if (!nffgDB.containsKey(id))
				throw new MyNotFoundException("Nffg " + id + " is not in NffgService database.");
		}

		return idSet;
	}

	// verify that a list of submitted POLICIES names (mixed TRV RCH) is valid
	// (are all in DB)
	public Set<String> verifyPolicyIDs(List<String> names) throws MyNotFoundException {

		Set<String> idSet = new HashSet<>();
		idSet.addAll(names);
		for (String id : idSet) {
			if (!rchPolicyDB.containsKey(id) && !trvPolicyDB.containsKey(id))
				throw new MyNotFoundException("Policy " + id + " is not NffgService database.");
		}

		return idSet;
	}

	/************* utilities ************/

	// check if a policy respect some DB integrity constraint
	private void checkPolicyConstraints(GeneralPolicyType gpt)
			throws MyForbiddenException {

		// check Date format
		checkDateFormat(gpt);

		// check if policy reference an existing NFFG
		if (!nffgDB.containsKey(gpt.getNffg()))
			throw new MyForbiddenException("Policy " + gpt.getName() + " reference a not existing NFFG.");

		// check if SRC and DST nodes are inside the referenced NFFG
		if (!checkPolicySrcDst(gpt, nffgDB.get(gpt.getNffg()).getNodeSet()))
			throw new MyForbiddenException(
					"Policy " + gpt.getName() + " Src and/or Dst Nodes are not in the referenced NFFG.");

	}

	// check if SRC and DST nodes of a POLICY are inside the referenced NFFG (given the nodeset)
	private boolean checkPolicySrcDst(GeneralPolicyType p, NodeSetType nst) {

		if (!(p instanceof RchPolicyType))
			return false;

		String srcNode = ((RchPolicyType) p).getSrcNode();
		String dstNode = ((RchPolicyType) p).getDstNode();
		int cnt = 0;

		for (NodeType n : nst.getNode()) {
			if (n.getName().equals(srcNode) || n.getName().equals(dstNode)) {
				cnt++;
				if (cnt == 2) {
					return true;
				}
			}
		}
		return false;
	}

	// check date format of a policy verification result (if present)
	private boolean checkDateFormat(GeneralPolicyType gpt) {
		VerificationType vt = gpt.getVerification();
		if (vt != null) {
			XMLGregorianCalendar xgc = vt.getDate();
			if (FIELD_UNDEFINED == xgc.getTimezone()) {
				return false;
			}
		}
		return true;
	}

}
