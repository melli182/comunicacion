package uy.edu.um.telmovil.msg;

public class RegistrationConfirmationMsg extends Msg {

	public final static String RESPONSE_OK = "OK";
	public final static String RESPONSE_FAIL = "FAIL";
	public final static String RESPONSE_INVALID = "INVALID";
	
	private String response;
	private String id;
 
	public String getResponse() {
		return response;
	}
	
	public void setResponse(String response) {
		this.response = response;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
