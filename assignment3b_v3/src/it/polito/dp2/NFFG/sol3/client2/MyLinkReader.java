package it.polito.dp2.NFFG.sol3.client2;

import it.polito.dp2.NFFG.LinkReader;
import it.polito.dp2.NFFG.NodeReader;

public class MyLinkReader implements LinkReader{
	
	private LinkType l;
	private NffgType nf;
	
	public MyLinkReader(LinkType l, NffgType nf){
		this.l = l;
		this.nf = nf;
	}

	@Override
	public String getName() {
		return l.getName();
	}

	@Override
	public NodeReader getDestinationNode() {
		for (NodeType n : nf.getNodeSet().getNode()){
			if(l.getDstNode().equals(n.getName()))
				return new MyNodeReader(n,nf);
		}
		return null;
	}

	@Override
	public NodeReader getSourceNode() {
		for (NodeType n : nf.getNodeSet().getNode()){
			if(l.getSrcNode().equals(n.getName()))
				return new MyNodeReader(n,nf);
		}
		return null;
	}

}
