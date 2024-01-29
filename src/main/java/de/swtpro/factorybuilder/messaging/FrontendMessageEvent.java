package de.swtpro.factorybuilder.messaging;

public class FrontendMessageEvent {
    public enum MessageEventType {
        FACTORY, ENTITY
    }
    public enum MessageOperationType {
        UPDATE, DELETE
    }
    private MessageEventType eventType;

    private long eventID;

    private MessageOperationType operationType;

    public FrontendMessageEvent(MessageEventType eventType, long eventID, MessageOperationType operationType) {
        this.eventType = eventType;
        this.eventID = eventID;
        this.operationType = operationType;
    }
    
    public MessageEventType getEventType() {
        return eventType;
    }

    public void setEventType(MessageEventType eventType) {
        this.eventType = eventType;
    }
    public long getEventID() {
        return eventID;
    }

    public void setEventID(long eventID) {
        this.eventID = eventID;
    }

    public MessageOperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(MessageOperationType operationType) {
        this.operationType = operationType;
    }


}
