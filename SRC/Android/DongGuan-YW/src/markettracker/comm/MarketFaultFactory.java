package markettracker.comm;

public final class MarketFaultFactory extends FaultFactory {
	public static ApiFault getSoapFault(String faultType, Response response) {
		final FaultSoapResponse fsr = (FaultSoapResponse) response;

		FaultResult fr = fsr.getResult();
		FaultDetail fd = fr.getFaultDetail();
		String excpCode = (fd != null) ? fd.getExceptionCode() : fr
				.getFaultCode();
		String excpMsg = (fd != null) ? fd.getExceptionMessage() : fr
				.getFaultString();
		ExceptionCode exceptionCode = new ExceptionCode(excpCode);
		return new ApiFault(exceptionCode, excpMsg);

	}
}
