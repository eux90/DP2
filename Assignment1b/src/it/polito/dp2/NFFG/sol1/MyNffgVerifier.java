package it.polito.dp2.NFFG.sol1;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

import it.polito.dp2.NFFG.NffgReader;
import it.polito.dp2.NFFG.NffgVerifier;
import it.polito.dp2.NFFG.PolicyReader;
import it.polito.dp2.NFFG.sol1.jaxb.NffgInfoType;
import it.polito.dp2.NFFG.sol1.jaxb.NffgType;
import it.polito.dp2.NFFG.sol1.jaxb.RchPolicyType;
import it.polito.dp2.NFFG.sol1.jaxb.TrvPolicyType;

public class MyNffgVerifier implements NffgVerifier{
	
	private NffgInfoType nfi;
	
	public MyNffgVerifier(NffgInfoType nfi){
		this.nfi = nfi;
	}

	@Override
	public NffgReader getNffg(String name) {
		
		if(name == null)
			return null;
		
		List<NffgType> nfl = nfi.getNffgSet().getNffg();
		for(NffgType nf : nfl){
			if(nf.getName().equals(name))
				return new MyNffgReader(nf);
		}
		return null;
	}

	@Override
	public Set<NffgReader> getNffgs() {
		
		Set<NffgReader> nfrs = new HashSet<>();
		List<NffgType> nfl = nfi.getNffgSet().getNffg();
		for(NffgType nf : nfl){
			nfrs.add(new MyNffgReader(nf));
		}
		return nfrs;
	}

	@Override
	public Set<PolicyReader> getPolicies() {
		
		Set<PolicyReader> prs = new HashSet<>();
		List<RchPolicyType> rpl = nfi.getPolicySet().getRchPolicySet().getRchPolicy();
		List<TrvPolicyType> tpl = nfi.getPolicySet().getTrvPolicySet().getTrvPolicy();
		for(RchPolicyType rp : rpl){
			prs.add(new MyReachabilityPolicyReader(rp, nfi.getNffgSet().getNffg()));
		}
		for(TrvPolicyType tp : tpl){
			prs.add(new MyTraversalPolicyReader(tp, nfi.getNffgSet().getNffg()));
		}
		
		return prs;
	}

	@Override
	public Set<PolicyReader> getPolicies(String nffg) {
		
		if(nffg == null)
			return null;

		Set<PolicyReader> prs = new HashSet<>();
		List<RchPolicyType> rpl = nfi.getPolicySet().getRchPolicySet().getRchPolicy();
		List<TrvPolicyType> tpl = nfi.getPolicySet().getTrvPolicySet().getTrvPolicy();
		for(RchPolicyType rp : rpl){
			if(rp.getNffg().equals(nffg))
				prs.add(new MyReachabilityPolicyReader(rp, nfi.getNffgSet().getNffg()));
		}
		for(TrvPolicyType tp : tpl){
			if(tp.getNffg().equals(nffg))
				prs.add(new MyTraversalPolicyReader(tp, nfi.getNffgSet().getNffg()));
		}
		
		if(prs.isEmpty()){
			return null;
		}
		
		return prs;
	}

	@Override
	public Set<PolicyReader> getPolicies(Calendar date) {
		
		if(date == null)
			return null;
		
		Set<PolicyReader> prs = new HashSet<>();
		Calendar c = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
		
		List<RchPolicyType> rpl = nfi.getPolicySet().getRchPolicySet().getRchPolicy();
		List<TrvPolicyType> tpl = nfi.getPolicySet().getTrvPolicySet().getTrvPolicy();
		for(RchPolicyType rp : rpl){	
			if(rp.getVerification() != null){
				c.clear();
				c = rp.getVerification().getDate().toGregorianCalendar();
				if(c.after(date))
					prs.add(new MyReachabilityPolicyReader(rp, nfi.getNffgSet().getNffg()));
			}
		}
		for(TrvPolicyType tp : tpl){
			if(tp.getVerification() != null){
				c.clear();
				c = tp.getVerification().getDate().toGregorianCalendar();
				if(c.after(date))
					prs.add(new MyTraversalPolicyReader(tp, nfi.getNffgSet().getNffg()));
			}
		}
		
		if(prs.isEmpty()){
			return null;
		}
		
		return prs;
	}

}
