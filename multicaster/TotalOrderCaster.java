package multicaster;

import java.util.HashMap;
import java.util.Map;

import mcgui.*;

public class TotalOrderCaster {

    int id;
    int hosts;
    MulticasterUI mcui;
    BasicCommunicator bcom;

    int msgOrder;
    Map<String, MultiMessage> msgMap;
    // Map<Integer, MultiMessage> totalOrderQueue;
    Map<Integer, String> orderQueue;

    public TotalOrderCaster(int id, int hosts, MulticasterUI mcui, BasicCommunicator bcom){
        this.id = id;
        this.hosts = hosts;
        this.mcui = mcui;
        this.bcom = bcom;

        msgOrder = 0;
        if(id == 0){
            msgMap = null;
            orderQueue = null;
        }
        else{
            msgMap = new HashMap<>();
            orderQueue = new HashMap<>();
        }
    }

    public void t_receive(int peer, Message message){
        MultiMessage multiMessage = (MultiMessage) message;
        mcui.debug("");
        mcui.debug("T-Receive messge: " + String.valueOf(multiMessage.getMessageType()) + " " + message.getSender() + " " + String.valueOf(multiMessage.getSequenceNum()) + " " + multiMessage.totalOrder);

        // sequencer process
        if(id == 0){
            mcui.debug("Sequencer process");
            if(multiMessage.getMessageType() == false){
                mcui.debug("Normal message: send order message to all neighbors");
                String msgInditifier = String.valueOf(multiMessage.getSender()) + String.valueOf(multiMessage.getSequenceNum());
                MultiMessage orderMsg = new MultiMessage(id, msgInditifier, MultiCaster.sequenceNum, true, msgOrder);
                // send out message
                for(int i=0; i < hosts; i++) {
                    /* Sends to everyone except itself */
                    if(i != id) {
                        bcom.basicsend(i, orderMsg);
                    }
                }
                MultiCaster.sequenceNum ++;
                msgOrder ++;

                // deliver message
                mcui.deliver(peer, multiMessage.text);
                mcui.debug("Deliver message: " + multiMessage.getSender() + " " + multiMessage.getSequenceNum());
            }
            else{
                mcui.debug("As sequencer, ignore order message");
            }
        }
        else{ // normal process
            mcui.debug("Normal process");
            // mcui.debug(String.valueOf(message.getSender()));
            // mcui.debug(multiMessage.text);
            // mcui.debug(String.valueOf(multiMessage.getMessageType()));
            if(multiMessage.getMessageType() == false){
                mcui.debug("Add normal msg to msg map");
                String msgIdentifier = String.valueOf(multiMessage.getSender()) + String.valueOf(multiMessage.getSequenceNum());
                msgMap.put(msgIdentifier, multiMessage);

                while(orderQueue.containsKey(msgOrder) && msgMap.containsKey(orderQueue.get(msgOrder))){
                    // deliver message
                    String msgId = orderQueue.get(msgOrder);
                    MultiMessage msg = msgMap.get(msgId);
                    mcui.deliver(peer, msg.text);
                    orderQueue.remove(msgOrder);
                    msgMap.remove(msgId);
                    msgOrder ++;
                    mcui.debug("Deliver message: " + msg.getSender() + " " + msg.getSequenceNum());
                }
            }
            else{
                mcui.debug("Add order to order queue");
                String msgIdentifier = multiMessage.text;
                int totalOrder = multiMessage.totalOrder;
                orderQueue.put(totalOrder, msgIdentifier);
                msgMap.remove(msgIdentifier);

                while(orderQueue.containsKey(msgOrder) && msgMap.containsKey(orderQueue.get(msgOrder))){
                    // deliver message
                    String msgId = orderQueue.get(msgOrder);
                    MultiMessage msg = msgMap.get(msgId);
                    mcui.deliver(peer, msg.text);
                    orderQueue.remove(msgOrder);
                    msgMap.remove(msgId);
                    msgOrder ++;
                    mcui.debug("Deliver message: " + msg.getSender() + " " + msg.getSequenceNum());
                }
            }
        }
    }
}
