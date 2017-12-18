package it.polito.dp2.NFFG.sol3.client2;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import it.polito.dp2.NFFG.PolicyReader;
import it.polito.dp2.NFFG.VerificationResultReader;

public class MyVerificationResultReader implements VerificationResultReader {
	
	private GeneralPolicyType gp;
	private List<NffgType> nfl;
	
	public MyVerificationResultReader(GeneralPolicyType gp, List<NffgType> nfl){
		this.gp = gp;
		this.nfl = nfl;
	}
	

	@Override
	public PolicyReader getPolicy() {
		return new MyPolicyReader(gp, nfl);
	}

	@Override
	public Boolean getVerificationResult() {
		if(gp.getVerification() != null){
			return gp.getVerification().isResult();
		}
		else {
			return null;
		}
	}

	@Override
	public String getVerificationResultMsg() {
		if(gp.getVerification() != null){
			return gp.getVerification().getMessage();
		}
		else{
			return null;
		}
	}

	@Override
	public Calendar getVerificationTime() {
		if(gp.getVerification() != null){
			Calendar c = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
			c.clear();
			c = gp.getVerification().getDate().toGregorianCalendar();
			return c;
		}
		else{
			return null;
		}
	}

}
