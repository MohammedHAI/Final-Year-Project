/*
    Author: Mohammed Abdul Wadud Hamza Al-Islam
    Date created: 06/03/2024
 */

package components;

// holds the next line for the output area
// always access message in a synchronized block
public class OutputBuffer {
    private String oldMessage = "";
    private String newMessage = "";
    private boolean locked;

    // internal
    private boolean hasNewMessage() {
        return !(oldMessage == newMessage);
    }

    // meant for MainController
    public String getMessage() {
        if (hasNewMessage() && !locked) {
            oldMessage = newMessage;
            return newMessage;
        }
        else {
            return "";
        }
    }

    // meant for VirtualComputer
    public void setMessage(String newMessage) {
        if (!locked) {
            this.newMessage = newMessage;
        }
    }

    // necessary to prevent overwriting
    public void lockMessage() { locked = true;}
    public void unlockMessage() { locked = false; }
}
