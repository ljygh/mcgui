package multicaster;

import mcgui.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Simple example of how to use the Multicaster interface.
 *
 * @author Andreas Larsson &lt;larandr@chalmers.se&gt;
 */
public class MultiCaster extends Multicaster {

    static int sequenceNum;
    Map<Integer, List<Integer>> deliveredMsgMap;
    FIFOCaster fifoCaster;

    /**
     * No initializations needed for this simple one
     */
    public void init() {
        sequenceNum = 0;
        deliveredMsgMap = new HashMap<>();
        fifoCaster = new FIFOCaster(id, hosts, mcui, bcom);
        mcui.debug("The network has "+hosts+" hosts!");
    }
        
    /**
     * The GUI calls this module to multicast a message
     */
    public void cast(String messagetext) {
        // deliver message
        // mcui.deliver(id, messagetext, "from myself!");
        // if(deliveredMsgMap.containsKey(id))
        //     deliveredMsgMap.get(id).add(sequenceNum);
        // else{
        //     List<Integer> list = new ArrayList<>();
        //     list.add(sequenceNum);
        //     deliveredMsgMap.put(id, list);
        // }
        // mcui.debug("R-Deliver message");
        // fifoCaster.f_receive(id, new MultiMessage(id, messagetext, sequenceNum));

        // send out message
        for(int i=0; i < hosts; i++) {
            /* Sends to everyone except itself */
            if(i != id) {
                bcom.basicsend(i, new MultiMessage(id, messagetext, sequenceNum));
            }
        }
        mcui.debug("Sent out: \""+messagetext+"\"");
        mcui.debug("Sequence number of message: " + String.valueOf(sequenceNum));

        sequenceNum ++;
        // Test reliable
        // try {
		// 	Thread.sleep(1000);
		// } catch (InterruptedException e) {
		// 	e.printStackTrace();
		// }
    }
    
    /**
     * Receive a basic message
     * @param message  The message received
     */
    public void basicreceive(int peer,Message message) {
        mcui.debug("");
        mcui.debug("Receive message " + String.valueOf(message.getSender()) + " " + String.valueOf(((MultiMessage)message).getSequenceNum()));
        if(!deliveredMsgMap.containsKey(message.getSender()) || 
        !deliveredMsgMap.get(message.getSender()).contains(((MultiMessage)message).getSequenceNum())){
            mcui.debug("Not R-delivered");
            // send to all neighbors
            if(message.getSender() != id){
                for(int i=0; i < hosts; i++) {
                    /* Sends to everyone except itself */
                    if(i != id) {
                        bcom.basicsend(i, message);
                    }
                }
                mcui.debug("I am not the sender, sent to all neighbours");
            }

            // deliver the message and save in map
            // mcui.deliver(message.getSender(), ((MultiMessage)message).text);
            mcui.debug("R-Deliver message");
            fifoCaster.f_receive(message.getSender(), message);
            if(deliveredMsgMap.containsKey(message.getSender()))
                deliveredMsgMap.get(message.getSender()).add(((MultiMessage)message).getSequenceNum());
            else{
                List<Integer> list = new ArrayList<>();
                list.add(((MultiMessage)message).getSequenceNum());
                deliveredMsgMap.put(message.getSender(), list);
            }
        }
        else
            mcui.debug("R-Deliverd");
    }

    /**
     * Signals that a peer is down and has been down for a while to
     * allow for messages taking different paths from this peer to
     * arrive.
     * @param peer	The dead peer
     */
    public void basicpeerdown(int peer) {
        mcui.debug("Peer "+peer+" has been dead for a while now!");
    }
}
