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

    // internal
    private boolean hasNewMessage() {
        return !(oldMessage == newMessage);
    }

    // meant for MainController
    public String getMessage() {
        if (hasNewMessage()) {
            oldMessage = newMessage;
            return newMessage;
        }
        else {
            return "";
        }
    }

    // meant for VirtualComputer
    public void setMessage(String newMessage) {
        this.newMessage = newMessage;
    }
}
