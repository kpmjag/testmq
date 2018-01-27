package com.jag.jmsFileTxr;

import com.ibm.mq.*;
import com.ibm.mq.constants.MQConstants;
import java.io.*;

public class JSMFileTxrSample {

    private static boolean send = false;
    private static boolean retrieve = !send;
    private static final String qManager = "OMS_TEST_QM";
    private static final String qName = "OMS_TEST_QUEUE";

    public static void main(String args[]) {
        String sourcePath = "C:\\Users\\IBM_ADMIN\\Desktop\\yousuf js\\Utemporary-161862035-js.zip";
        String destinationPath = "C:\\Users\\IBM_ADMIN\\Desktop\\yousuf js\\fromQueue\\test.zip";
        try {
            MQQueueManager qMgr = new MQQueueManager(qManager);
            int openOptions = MQConstants.MQOO_INPUT_AS_Q_DEF | MQConstants.MQOO_OUTPUT;
            MQQueue queue = qMgr.accessQueue(qName, openOptions);
            System.out.println("Accessing Queue is successful");

            if(send) {
                MQMessage msg = new MQMessage();
                msg.putApplicationType = MQConstants.MQAT_JAVA;
                msg.write(loadBinaryFile(sourcePath));
                MQPutMessageOptions pmo = new MQPutMessageOptions();
                queue.put(msg, pmo);
                System.out.println("Sending file to the queue is successful");
            }
            if(retrieve){
                MQMessage rcvdMessage = new MQMessage();
                MQGetMessageOptions gmo = new MQGetMessageOptions();
                String ret = null;
                gmo.matchOptions = MQConstants.MQMO_NONE;
                gmo.options = MQConstants.MQGMO_WAIT;
                gmo.waitInterval = MQConstants.MQEI_UNLIMITED;
                queue.get(rcvdMessage, gmo);
                System.out.println("Reading message from Queue is successful");
                writeBinaryFile(destinationPath, rcvdMessage);
            }
            // disconnect the queue
            queue.close();
            qMgr.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MQException e) {
            e.printStackTrace();
        }
        return;
    }

    /**
     * Read the source file, convert it to bytearray stream and sending it to the MQ
     * @param fileSourceName
     * @return
     */
    private static byte[] loadBinaryFile(String fileSourceName) throws IOException {
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream(fileSourceName));
            byte[] theBytes = new byte[dis.available()];
            dis.read(theBytes, 0, dis.available());
            dis.close();
            System.out.println("Reading from the File is successful");
            return theBytes;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * This method write the Queue message, to the specified destination
     * @param fileDestinationName
     * @param rcvdMessage
     */
    private static void writeBinaryFile(String fileDestinationName, MQMessage rcvdMessage) throws IOException {
        try {
            OutputStream out = new FileOutputStream(new File(fileDestinationName));
            int sizeMessage = rcvdMessage.getDataLength();
            int index = 0;
            while (index <  sizeMessage) {
                out.write(rcvdMessage.readByte());
                index = ++index;
            }
            out.close();
            System.out.println("Writing the message to the destination is successful");
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
