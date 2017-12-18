package it.polito.dp2.NFFG.sol3.service;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import it.polito.dp2.NFFG.NffgReader;
import it.polito.dp2.NFFG.NodeReader;

public class MyNffgReader implements NffgReader {
	
	private NffgType nf;
	
	public MyNffgReader(NffgType nf){
		this.nf = nf;
	}

	@Override
	public String getName() {
		return nf.getName();
	}

	@Override
	public NodeReader getNode(String arg0) {
		List<NodeType> nl = nf.getNodeSet().getNode();
		for(NodeType n : nl){
			if(n.getName().equals(arg0))
				return new MyNodeReader(n, nf);
		}
		
		return null;
	}

	@Override
	public Set<NodeReader> getNodes() {
		Set<NodeReader> nrs = new HashSet<>();
		List<NodeType> nl = nf.getNodeSet().getNode();
		for(NodeType n : nl){
			nrs.add(new MyNodeReader(n,nf));
		}
		return nrs;
	}

	@Override
	public Calendar getUpdateTime() {
		Calendar c = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
		c.clear();
		c = nf.getDate().toGregorianCalendar();
		return c;
	}

}
