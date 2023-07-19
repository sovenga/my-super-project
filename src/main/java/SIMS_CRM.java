import com.azure.messaging.servicebus.*;
import com.stadio.daos.StadioDao;
import com.stadio.daos.StadioStageDao;
import com.stadio.models.MyServiceBusMessage;
import com.stadio.models.Student;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

class SIMS_CRM {
    static String connectionString = "Endpoint=sb://stadio-integration-san-dev-sbus.servicebus.windows.net/;SharedAccessKeyName=sbs;SharedAccessKey=qoceGySQAo8Sida6GReBjARhAgR73SeSLqASLPHu5NY=;EntityPath=businessschool";
    static String queueName = "businessschool";
    static void sendMessage() throws SQLException, ClassNotFoundException, IOException {
        StadioStageDao stadioStageDao = new StadioStageDao();
        StadioDao dao = new StadioDao();
        String message = "";
        FileWriter SIMS_FILE = new FileWriter("C:\\sbs\\service-bus\\SIMS-CRM\\SIMS_DATA.txt");
        ArrayList<Student> studentsList = (ArrayList<Student>) dao.getStudents();
        JSONObject studentJson = new JSONObject();

        // create a Service Bus Sender client for the queue
        ServiceBusSenderClient senderClient = new ServiceBusClientBuilder()
                .connectionString(connectionString)
                .sender()
                .queueName(queueName)
                .buildClient();
        //System.exit(0);


        //File myFileObj = null;
        for (Student student : studentsList) {

            studentJson.put("Student Number", student.getStudent_pk());
            studentJson.put("ID Number", student.getId_number());
            studentJson.put("Last Name", student.getLast_name());
            studentJson.put("First Name", student.getFirst_name());
            studentJson.put("Gender", student.getGender_fk());
            message = studentJson.toString();
            System.out.println(message);
            SIMS_FILE.append(message + "\n");
            senderClient.sendMessage(new ServiceBusMessage(message));
        }
        System.out.println("Sent messages to the queue: " + queueName);
    }

    static List<ServiceBusMessage> createMessages() {
        // create a list of messages and return it to the caller
        ServiceBusMessage[] messages = {new ServiceBusMessage("First message")};
        return Arrays.asList(messages);
    }

    static void sendMessageBatch() {
        // create a Service Bus Sender client for the queue
        ServiceBusSenderClient senderClient = new ServiceBusClientBuilder()
                .connectionString(connectionString)
                .sender()
                .queueName(queueName)
                .buildClient();

        // Creates an ServiceBusMessageBatch where the ServiceBus.
        ServiceBusMessageBatch messageBatch = senderClient.createMessageBatch();

        // create a list of messages
        List<ServiceBusMessage> listOfMessages = createMessages();

        // We try to add as many messages as a batch can fit based on the maximum size and send to Service Bus when
        // the batch can hold no more messages. Create a new batch for next set of messages and repeat until all
        // messages are sent.
        for (ServiceBusMessage message : listOfMessages) {
            if (messageBatch.tryAddMessage(message)) {
                continue;
            }

            // The batch is full, so we create a new batch and send the batch.
            senderClient.sendMessages(messageBatch);
            System.out.println("Sent a batch of messages to the queue: " + queueName);

            // create a new batch
            messageBatch = senderClient.createMessageBatch();

            // Add that message that we couldn't before.
            if (!messageBatch.tryAddMessage(message)) {
                System.err.printf("Message is too large for an empty batch. Skipping. Max size: %s.", messageBatch.getMaxSizeInBytes());
            }
        }

        if (messageBatch.getCount() > 0) {
            senderClient.sendMessages(messageBatch);
            System.out.println("Sent a batch of messages to the queue: " + queueName);
        }

        //close the client
        senderClient.close();
    }

    // handles received messages
    static void receiveMessages() throws InterruptedException {
        CountDownLatch countdownLatch = new CountDownLatch(1);

        // Create an instance of the processor through the ServiceBusClientBuilder
        ServiceBusProcessorClient processorClient = new ServiceBusClientBuilder()
                .connectionString(connectionString)
                .processor()
                .queueName(queueName)
                .processMessage(SIMS_CRM::processMessage)
                .processError(context -> processError(context, countdownLatch))
                .buildProcessorClient();

        System.out.println("Starting the processor");
        processorClient.start();

        TimeUnit.SECONDS.sleep(10);
        System.out.println("Stopping and closing the processor");
        processorClient.close();
    }

    private static void processMessage(ServiceBusReceivedMessageContext context) {
        ServiceBusReceivedMessage message = context.getMessage();
        System.out.printf("Processing message. Session: %s, Sequence #: %s. Contents: %s%n", message.getMessageId(),
                message.getSequenceNumber(), message.getBody());
    }

    private static void processError(ServiceBusErrorContext context, CountDownLatch countdownLatch) {
        System.out.printf("Error when receiving messages from namespace: '%s'. Entity: '%s'%n",
                context.getFullyQualifiedNamespace(), context.getEntityPath());

        if (!(context.getException() instanceof ServiceBusException)) {
            System.out.printf("Non-ServiceBusException occurred: %s%n", context.getException());
            return;
        }

        ServiceBusException exception = (ServiceBusException) context.getException();
        ServiceBusFailureReason reason = exception.getReason();

        if (reason == ServiceBusFailureReason.MESSAGING_ENTITY_DISABLED
                || reason == ServiceBusFailureReason.MESSAGING_ENTITY_NOT_FOUND
                || reason == ServiceBusFailureReason.UNAUTHORIZED) {
            System.out.printf("An unrecoverable error occurred. Stopping processing with reason %s: %s%n",
                    reason, exception.getMessage());

            countdownLatch.countDown();
        } else if (reason == ServiceBusFailureReason.MESSAGE_LOCK_LOST) {
            System.out.printf("Message lock lost for message: %s%n", context.getException());
        } else if (reason == ServiceBusFailureReason.SERVICE_BUSY) {
            try {
                // Choosing an arbitrary amount of time to wait until trying again.
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                System.err.println("Unable to sleep for period of time");
            }
        } else {
            System.out.printf("Error source %s, reason %s, message: %s%n", context.getErrorSource(),
                    reason, context.getException());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        try {
            sendMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //sendMessageBatch();
        //receiveMessages();
    }
}
