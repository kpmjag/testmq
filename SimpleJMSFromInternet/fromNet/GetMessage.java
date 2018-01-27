package fromNet;

import com.ibm.mq.*;
import com.ibm.mq.constants.MQConstants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
 
public class GetMessage {
 
    UtilWSMQ utilWSMQ = new UtilWSMQ();
    String fileDestName = "/DESTNATION_PATH_NAME/NAME_FILE.XXX";
 
    public void receiveFiles(String hostname, String queueManager, int port, String channel, String queueName) throws MQException {
        utilWSMQ.mqConfig(hostname, queueManager, port, channel);
        MQMessage message = new MQMessage();
        MQQueue queue;
        String ret = null;
        MQGetMessageOptions gmo = new MQGetMessageOptions();
        gmo.matchOptions = MQConstants.MQMO_NONE;
        gmo.options = MQConstants.MQGMO_WAIT;
        gmo.waitInterval = MQConstants.MQEI_UNLIMITED;
        queue = utilWSMQ.openQueueForOutput(queueName);
        queue.get(message, gmo);
        try {
            OutputStream out = new FileOutputStream(new File(fileDestName));
            int sizeMessage = message.getDataLength();
            int index = 0;
            while (index <  sizeMessage) {
                out.write(message.readByte());
                index = ++index;
                System.out.println(sizeMessage + "--" + index); //If this line was commented will increase performance visibly!
            }
            out.close();
        } catch (IOException e) {
            System.out.println("A leitura da mensagem terminou abruptamente");
        } finally {
            utilWSMQ.qMgr.commit();
            utilWSMQ.qMgr.close();
        }
    }
}
