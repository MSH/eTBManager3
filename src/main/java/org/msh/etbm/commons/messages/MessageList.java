package org.msh.etbm.commons.messages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Store list of messages
 * Created by rmemoria on 27/10/15.
 */
public class MessageList {

    // point to the keys in the messages.properties files
    public static final String MSGKEY_REQUIRED = "{javax.validation.constraints.NotNull.message}";
    public static final String MSGKEY_NOTUNIQUE = "{validation.duplicatedname}";

    public static final String MSG_GROUP_REQUIRED = "NOT_NULL";
    public static final String MSG_GROUP_NOTUNIQUE = "NOT_UNIQUE";

    /**
     * Store the list of messages
     */
    private List<Message> messages = new ArrayList<>();

    /**
     * Object that will resolve the message keys
     */
    private MessageKeyResolver resolver;

    /**
     * Default constructor. Protected because it needs a message resolver
     * @param resolver
     */
    protected MessageList(MessageKeyResolver resolver) {
        this.resolver = resolver;
    }


    /**
     * Add an error to an specific field
     * @param field
     * @param message
     */
    public void add(String field, String message) {
        add(field, message, null);
    }

    /**
     * Add a message
     * @param field the name of the field
     * @param message the message to be sent
     * @param group the message group
     */
    public void add(String field, String message, String group) {
        messages.add(new Message(field, message, group));
    }

    /**
     * Add a required message
     * @param field
     */
    public void addRequired(String field) {
        add(field, MSGKEY_REQUIRED, MSG_GROUP_REQUIRED);
    }

    /**
     * Add a not unique message
     * @param field
     */
    public void addNotUnique(String field) {
        add(field, MSGKEY_NOTUNIQUE, MSG_GROUP_NOTUNIQUE);
    }

    /**
     * Add a global error
     * @param message
     */
    public void addGlobalError(String message) {
    }

    /**
     * Return the number of error messages
     * @return
     */
    public int size() {
        return messages.size();
    }

    /**
     * Remove all messages from the list
     */
    public void clear() {
        messages.clear();
    }

    /**
     * Return the list of messages. The list cannot be modified
     * @return
     */
    public List<Message> getMessages() {
        return Collections.unmodifiableList( messages );
    }

}
