package Mts.Crud.Exceptions;


    import java.util.List;
    import lombok.Getter;
    
    public class InvalidEntity extends RuntimeException {
    
      @Getter
      private ErrorCodes errorCode;
      @Getter
      private List<String> errors;
    
      public InvalidEntity(String message) {
        super(message);
      }
    
      public InvalidEntity(String message, Throwable cause) {
        super(message, cause);
      }
    
      public InvalidEntity(String message, Throwable cause, ErrorCodes errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
      }
    
      public InvalidEntity(String message, ErrorCodes errorCode) {
        super(message);
        this.errorCode = errorCode;
      }
    
      public InvalidEntity(String message, ErrorCodes errorCode, List<String> errors) {
        super(message);
        this.errorCode = errorCode;
        this.errors = errors;
      }
    
    }
