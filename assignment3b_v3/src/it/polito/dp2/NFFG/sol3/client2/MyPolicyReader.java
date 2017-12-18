package it.polito.dp2.NFFG.sol3.client2;

import java.util.List;

import it.polito.dp2.NFFG.NffgReader;
import it.polito.dp2.NFFG.PolicyReader;
import it.polito.dp2.NFFG.VerificationResultReader;

public class MyPolicyReader implements PolicyReader{
	
	private GeneralPolicyType gp;
	private List<NffgType> nfl;
	
	public MyPolicyReader(GeneralPolicyType gp, List<NffgType> nfl){
		this.gp = gp;
		this.nfl = nfl;
	}

	@Override
	public String getName() {
		return gp.getName();
	}

	@Override
	public NffgReader getNffg() {
		for(NffgType nf : nfl){
			if(nf.getName().equals(gp.getNffg()))
				return new MyNffgReader(nf);
		}
		return null;
	}

	@Override
	public VerificationResultReader getResult() {
		if(gp.getVerification() != null)
			return new MyVerificationResultReader(gp, nfl);
		else
			return null;
	}

	@Override
	public Boolean isPositive() {
		return gp.isIsPositive();
	}


}
