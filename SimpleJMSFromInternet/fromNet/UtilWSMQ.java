package fromNet;

import com.ibm.mq.*;
import com.ibm.mq.constants.MQConstants;

public class UtilWSMQ {
   
    MQQueueManager qMgr = null;
 
    public void mqConfig(String hostname, String queueManager, int port, String channel) throws MQException {
 
        MQEnvironment.hostname = hostname;
        MQEnvironment.port = port;
        MQEnvironment.channel = channel;  
        qMgr = new MQQueueManager(queueManager);
 
    }
 
    public void disconnect() throws MQException {
 
        if (qMgr != null) {
            if (qMgr.isConnected()) {
                qMgr.disconnect();
            }
        }
    }
   
    protected MQQueue openQueueForOutput(String queueName) throws MQException {
       
        int openOptions = MQConstants.MQOO_INPUT_EXCLUSIVE| MQConstants.MQOO_FAIL_IF_QUIESCING |MQConstants.MQOO_INQUIRE;
        return qMgr.accessQueue(queueName, openOptions);
    }
 
    protected MQQueue openQueueForInput(String queueName) throws MQException {
       
        int openOptions = MQConstants.MQQT_REMOTE;
        return qMgr.accessQueue(queueName, openOptions, null, null, null);
    }
}