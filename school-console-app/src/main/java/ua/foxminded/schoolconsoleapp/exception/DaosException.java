package ua.foxminded.schoolconsoleapp.exception;

public class DaosException extends Exception {
    public DaosException() {
	super();
    }

    public DaosException(String message) {
	super(message);
    }
    
    public DaosException(String message, Throwable throwable) {
	super(message, throwable);
    }
    
    public DaosException(Throwable throwable) {
	super(throwable);
    }
}
