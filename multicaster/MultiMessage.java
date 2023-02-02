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
        
    public MultiMessage(int sender, String text, int sequenceNum) {
        super(sender);
        this.text = text;
        this.sequenceNum = sequenceNum;
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
    
    public static final long serialVersionUID = 0;
}
