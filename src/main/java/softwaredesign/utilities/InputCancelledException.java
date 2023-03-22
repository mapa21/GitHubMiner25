package softwaredesign.utilities;

import java.io.IOException;

public class InputCancelledException extends IOException {
    public InputCancelledException() {
        super("Input was cancelled");
    }
    public InputCancelledException(String message) {
        super(message);
    }
    public InputCancelledException(String message, Throwable cause) {
        super(message, cause);
    }
    public InputCancelledException(Throwable cause) {
        super(cause);
    }
}
