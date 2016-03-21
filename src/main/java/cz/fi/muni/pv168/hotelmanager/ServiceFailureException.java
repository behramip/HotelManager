package cz.fi.muni.pv168.hotelmanager;

/**
 * This exception shows at failure of service.
 *
 * @author Filip Dubnička <dubnickaf@gmail.com>
 */
public class ServiceFailureException extends RuntimeException {

    public ServiceFailureException(String msg) {
        super(msg);
    }

    public ServiceFailureException(Throwable cause) {
        super(cause);
    }

    public ServiceFailureException(String message, Throwable cause) {
        super(message, cause);
    }

}
