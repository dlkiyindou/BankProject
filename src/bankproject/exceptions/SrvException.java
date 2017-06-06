package bankproject.exceptions;

public class SrvException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public SrvException() {
		// TODO Auto-generated constructor stub
	}

	public SrvException(String arg0) {
		super(arg0);
	}

	public SrvException(Throwable arg0) {
		super(arg0);
	}

	public SrvException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public SrvException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
