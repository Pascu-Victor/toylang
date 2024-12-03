package exceptions;

public class TypeException extends Exception {
    public TypeException(String message) {
        super(message);
    }
    
    @Override
    public String toString() {
        return super.getMessage();
    }
}
