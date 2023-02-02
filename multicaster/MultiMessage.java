package multicaster;

import mcgui.*;

/**
 * Message implementation for ExampleCaster.
 *
 * @author Andreas Larsson &lt;larandr@chalmers.se&gt;
 */
public class MultiMessage extends Message {
        
    String text;
    int sequenceNum;

    boolean messageType; // false for normal message, true for order message
    int totalOrder;
        
    public MultiMessage(int sender, String text, int sequenceNum) {
        super(sender);
        this.text = text;
        this.sequenceNum = sequenceNum;
        this.messageType = false;
    }

    public MultiMessage(int sender, String text, int sequenceNum, boolean messageType, int totalOrder) {
        super(sender);
        this.text = text;
        this.sequenceNum = sequenceNum;
        this.messageType = messageType;
        this.totalOrder = totalOrder;
    }
    
    /**
     * Returns the text of the message only. The toString method can
     * be implemented to show additional things useful for debugging
     * purposes.
     */
    public String getText() {
        return text;
    }

    /**
     * Reture sequence number
     */
    public int getSequenceNum(){
        return sequenceNum;
    }

    public boolean getMessageType(){
        return messageType;
    }

    public int getTotalOrder(){
        return totalOrder;
    }
    
    public static final long serialVersionUID = 0;
}
