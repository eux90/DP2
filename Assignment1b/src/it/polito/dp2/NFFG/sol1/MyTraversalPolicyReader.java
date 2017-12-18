package it.polito.dp2.NFFG.sol1;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.dp2.NFFG.FunctionalType;
import it.polito.dp2.NFFG.NffgReader;
import it.polito.dp2.NFFG.NodeReader;
import it.polito.dp2.NFFG.TraversalPolicyReader;
import it.polito.dp2.NFFG.VerificationResultReader;
import it.polito.dp2.NFFG.sol1.jaxb.FuncType;
import it.polito.dp2.NFFG.sol1.jaxb.NffgType;
import it.polito.dp2.NFFG.sol1.jaxb.NodeType;
import it.polito.dp2.NFFG.sol1.jaxb.TrvPolicyType;

public class MyTraversalPolicyReader implements TraversalPolicyReader {
	
	private TrvPolicyType tp;
	private List<NffgType> nfl;
	
	public MyTraversalPolicyReader(TrvPolicyType tp, List<NffgType> nfl){
		this.tp = tp;
		this.nfl = nfl;
	}

	@Override
	public NodeReader getDestinationNode() {
	
		for(NffgType nf : nfl){
			if(nf.getName().equals(tp.getNffg())){ //if NFFG is the one of policy
				//search the destination node and return it
				for(NodeType n : nf.getNodeSet().getNode()){
					if(n.getName().equals(tp.getDstNode()))
						return new MyNodeReader(n,nf);
				}
			}
		}
		return null;
	}

	@Override
	public NodeReader getSourceNode() {

		for(NffgType nf : nfl){
			if(nf.getName().equals(tp.getNffg())){ //if NFFG is the one of policy
				//search the source node and return it
				for(NodeType n : nf.getNodeSet().getNode()){
					if(n.getName().equals(tp.getSrcNode()))
						return new MyNodeReader(n,nf);
				}
			}
		}
		return null;
	}

	@Override
	public NffgReader getNffg() {

		for(NffgType nf : nfl){
			if(nf.getName().equals(tp.getNffg())) //if NFFG is the one of policy return it
				return new MyNffgReader(nf);
		}
		return null;
	}

	@Override
	public VerificationResultReader getResult() {

		if(tp.getVerification() != null)
			return new MyVerificationResultReader(tp, nfl);
		else
			return null;
	}

	@Override
	public Boolean isPositive() {
		return tp.isIsPositive();
	}

	@Override
	public String getName() {
		return tp.getName();
	}

	@Override
	public Set<FunctionalType> getTraversedFuctionalTypes() {

		Set<FunctionalType> sf = new HashSet<>();
		List<FuncType> lt = tp.getFunctionList();
		for(FuncType t : lt){
			FunctionalType ft = FunctionalType.fromValue(t.name());
			sf.add(ft);
		}
		return sf;
	}

}
