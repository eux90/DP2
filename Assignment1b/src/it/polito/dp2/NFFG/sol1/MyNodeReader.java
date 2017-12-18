package it.polito.dp2.NFFG.sol1;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.dp2.NFFG.FunctionalType;
import it.polito.dp2.NFFG.LinkReader;
import it.polito.dp2.NFFG.NodeReader;
import it.polito.dp2.NFFG.sol1.jaxb.LinkType;
import it.polito.dp2.NFFG.sol1.jaxb.NffgType;
import it.polito.dp2.NFFG.sol1.jaxb.NodeType;

public class MyNodeReader implements NodeReader{
	
	private NodeType n;
	private NffgType nf;
	
	public MyNodeReader(NodeType n, NffgType nf){
		this.n = n;
		this.nf = nf;
	}

	@Override
	public String getName() {
		return n.getName();
	}

	@Override
	public FunctionalType getFuncType() {
		return FunctionalType.fromValue(n.getType().name());
	}

	@Override
	public Set<LinkReader> getLinks() {

		Set<LinkReader> slr = new HashSet<>();
		List<LinkType> ll = nf.getLinkSet().getLink();
		for(LinkType l : ll){
			if(n.getName().equals(l.getSrcNode())){
				slr.add(new MyLinkReader(l,nf));
			}
		}
		return slr;
	}


}
