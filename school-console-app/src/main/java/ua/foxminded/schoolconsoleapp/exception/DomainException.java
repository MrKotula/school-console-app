package ua.foxminded.schoolconsoleapp.exception;

@SuppressWarnings("serial")
public class DomainException extends RuntimeException {
    public DomainException() {
	super();
    }
    
    public DomainException(String message) {
	super(message);
    }
    
    public DomainException(String message, Throwable throwable) {
	super(message, throwable);
    }
    
    public DomainException(Throwable throwable) {
	super(throwable);
    }
}
