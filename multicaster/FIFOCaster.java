package multicaster;

import mcgui.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class FIFOCaster{

    int id;
    int hosts;
    MulticasterUI mcui;
    BasicCommunicator bcom;

    Map<Integer, Integer> nextMap;
    Map<Integer, LinkedList<Message>> msgBag;

    TotalOrderCaster totalOrderCaster;

    FIFOCaster(int id, int hosts, MulticasterUI mcui, BasicCommunicator bcom){
        this.id = id;
        this.hosts = hosts;
        this.mcui = mcui;
        this.bcom = bcom;

        nextMap = new HashMap<>();
        for(int i = 0; i < hosts; i++){
            nextMap.put(i, 0);
        }
        msgBag = new HashMap<>();

        totalOrderCaster = new TotalOrderCaster(id, hosts, mcui, bcom);
    }

    public void f_receive(int peer, Message message){
        // put msgs to msgBag in order
        mcui.debug("");
        mcui.debug("F-Receive messge " + message.getSender() + " " + String.valueOf(((MultiMessage)message).getSequenceNum()));
        mcui.debug("Add messge to msg bag");
        int sender = message.getSender();
        if(!msgBag.containsKey(sender)){
            LinkedList<Message> msgList = new LinkedList<>();
            msgList.add(message);
            msgBag.put(sender, msgList);
        }
        else{
            LinkedList<Message> msgList = msgBag.get(sender);
            int seqNum = ((MultiMessage)message).getSequenceNum();
            for(int i = msgList.size() - 1; i >= -1; i--){
                if(i == -1)
                    msgList.add(i + 1, message);
                else if(seqNum < ((MultiMessage)msgList.get(i)).getSequenceNum())
                    continue;
                else
                    msgList.add(i + 1, message);
            }
        }

        // deliver msg according to nextMap
        LinkedList<Message> msgList = msgBag.get(sender);
        mcui.debug("Sender: " + sender);
        mcui.debug("Next msg num of this sender: " + nextMap.get(sender));
        printMsgBag(sender);
        while(msgList.size() > 0 && ((MultiMessage)msgList.getFirst()).getSequenceNum() == nextMap.get(sender)){
            mcui.deliver(peer, ((MultiMessage)message).text);
            int current_seq = nextMap.get(sender);
            nextMap.put(sender, current_seq + 1);
            msgList.removeFirst();
            mcui.debug("F-Deliver message: " + sender + " " + ((MultiMessage)message).getSequenceNum());
            // totalOrderCaster.t_receive(peer, message);
        }
        return;
    }

    public void printMsgBag(int sender){
        LinkedList<Message> msgList = msgBag.get(sender);
        mcui.debug("Print msg bag of the sender:");
        for(Message msg : msgList){
            mcui.debug("" + ((MultiMessage)msg).getSequenceNum());
        }
    }
    
}
