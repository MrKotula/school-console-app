package ua.foxminded.schoolconsoleapp.exception;

@SuppressWarnings("serial")
public class DAOException extends Exception {
    public DAOException() {
	super();
    }

    public DAOException(String message) {
	super(message);
    }
    
    public DAOException(String message, Throwable throwable) {
	super(message, throwable);
    }
    
    public DAOException(Throwable throwable) {
	super(throwable);
    }
}
