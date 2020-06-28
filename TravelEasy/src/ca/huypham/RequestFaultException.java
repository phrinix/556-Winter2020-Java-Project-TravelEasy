// Do Gia Huy Pham
package ca.huypham;

import javax.servlet.ServletException;

public class RequestFaultException extends ServletException{
	private static final long serialVersionUID = 1L;
	   public RequestFaultException()
	   {
	      super(); // Call the super class constructor
	   }

	   public RequestFaultException(String message)
	   {
	      super(message); // Call the super class constructor
	   }

	   public RequestFaultException(Throwable rootCause)
	   {
	      super(rootCause); // Call the super class constructor
	   }

	   public RequestFaultException(String message, Throwable rootCause)
	   {
	      super(message, rootCause); // Call the super class constructor
	   }
}
