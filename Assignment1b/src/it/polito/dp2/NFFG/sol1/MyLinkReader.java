package it.polito.dp2.NFFG.sol1;

import it.polito.dp2.NFFG.LinkReader;
import it.polito.dp2.NFFG.NodeReader;
import it.polito.dp2.NFFG.sol1.jaxb.LinkType;
import it.polito.dp2.NFFG.sol1.jaxb.NffgType;
import it.polito.dp2.NFFG.sol1.jaxb.NodeType;

public class MyLinkReader implements LinkReader{
	
	private LinkType l;
	private NffgType nf;
	
	public MyLinkReader(LinkType l, NffgType nf){
		this.l = l;
		this.nf = nf;
	}

	@Override
	public String getName() {
		if(l == null)
			return null;
		
		return l.getName();
	}

	@Override
	public NodeReader getDestinationNode() {
		if(l == null || nf == null)
			return null;
		
		for (NodeType n : nf.getNodeSet().getNode()){
			if(l.getDstNode().equals(n.getName()))
				return new MyNodeReader(n,nf);
		}
		return null;
	}

	@Override
	public NodeReader getSourceNode() {
		if(l == null || nf == null)
			return null;
		
		for (NodeType n : nf.getNodeSet().getNode()){
			if(l.getSrcNode().equals(n.getName()))
				return new MyNodeReader(n,nf);
		}
		return null;
	}

}
