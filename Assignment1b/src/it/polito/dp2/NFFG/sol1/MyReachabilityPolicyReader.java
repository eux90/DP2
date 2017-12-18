package it.polito.dp2.NFFG.sol1;

import java.util.List;

import it.polito.dp2.NFFG.NffgReader;
import it.polito.dp2.NFFG.NodeReader;
import it.polito.dp2.NFFG.ReachabilityPolicyReader;
import it.polito.dp2.NFFG.VerificationResultReader;
import it.polito.dp2.NFFG.sol1.jaxb.NffgType;
import it.polito.dp2.NFFG.sol1.jaxb.NodeType;
import it.polito.dp2.NFFG.sol1.jaxb.RchPolicyType;

public class MyReachabilityPolicyReader implements ReachabilityPolicyReader{
	
	private RchPolicyType rp;
	private List<NffgType> nfl;
	
	public MyReachabilityPolicyReader(RchPolicyType rp, List<NffgType> nfl){
		this.rp = rp;
		this.nfl = nfl;
	}

	@Override
	public NffgReader getNffg() {
					
		for(NffgType nf : nfl){
			if(nf.getName().equals(rp.getNffg()))
				return new MyNffgReader(nf);
		}
		return null;
	}

	@Override
	public VerificationResultReader getResult() {
		
		if(rp.getVerification() != null)
			return new MyVerificationResultReader(rp, nfl);
		else
			return null;
	}

	@Override
	public Boolean isPositive() {
		return rp.isIsPositive();
	}

	@Override
	public String getName() {
		return rp.getName();
	}

	@Override
	public NodeReader getDestinationNode() {

		for(NffgType nf : nfl){
			if(nf.getName().equals(rp.getNffg())){
				for(NodeType n : nf.getNodeSet().getNode()){
					if(n.getName().equals(rp.getDstNode()))
						return new MyNodeReader(n,nf);
				}
			}
		}
		return null;
	}

	@Override
	public NodeReader getSourceNode() {

		for(NffgType nf : nfl){
			if(nf.getName().equals(rp.getNffg())){
				for(NodeType n : nf.getNodeSet().getNode()){
					if(n.getName().equals(rp.getSrcNode()))
						return new MyNodeReader(n,nf);
				}
			}
		}
		return null;
	}

}
