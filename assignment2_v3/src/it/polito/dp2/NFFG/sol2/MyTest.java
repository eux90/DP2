package it.polito.dp2.NFFG.sol2;

import it.polito.dp2.NFFG.lab2.NoGraphException;
import it.polito.dp2.NFFG.lab2.ReachabilityTester;
import it.polito.dp2.NFFG.lab2.ReachabilityTesterException;
import it.polito.dp2.NFFG.lab2.ServiceException;
import it.polito.dp2.NFFG.lab2.UnknownNameException;

public class MyTest {
	
	public static void main(String[] args) throws ReachabilityTesterException, UnknownNameException, ServiceException, NoGraphException {
		
		
		
		
		ReachabilityTester t = ReachabilityTesterFactory.newInstance().newReachabilityTester();
		t.loadNFFG("Nffg0");
		System.out.println(t.testReachability("VPN8", "VPN6"));
		System.out.println(t.testReachability("CACHE0", "SPAM5"));
		System.out.println(t.testReachability("CACHE0", "CACHE0"));
		
		
	}

}
