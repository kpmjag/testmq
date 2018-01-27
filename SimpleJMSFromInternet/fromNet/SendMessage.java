package fromNet;

import com.ibm.mq.*;
import com.ibm.mq.constants.MQConstants;

import java.io.IOException;


public class SendMessage {
 
 
    UtilWSMQ utilWSMQ = new UtilWSMQ();
    String fileSourceName = "data/name.txt";
    LoadFiles lfs = new LoadFiles();
 
    public void send(String hostname, String queueManager, int port,
            String channel, String queueName) throws MQException, IOException {
       
        utilWSMQ.mqConfig(hostname, queueManager, port, channel);
        MQPutMessageOptions pmo = new MQPutMessageOptions();
        MQMessage messages = new MQMessage();
        messages.putApplicationType = MQConstants.MQAT_JAVA;
        messages.write(lfs.loadBinaryFile(fileSourceName));
        utilWSMQ.qMgr.put(queueName, queueManager, messages);
        utilWSMQ.qMgr.commit();
        utilWSMQ.qMgr.close();
 
     }


} 
