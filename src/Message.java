import java.io.Serializable;
import java.security.PublicKey;
import java.util.Arrays;

public class Message implements Serializable {

    private String typeMessage;
    private String message;
    private byte[] encData;
    private PublicKey publicKey;

    public Message(String typeMessage, String message) {
        this.typeMessage = typeMessage;
        this.message = message;
    }

    public Message(String typeMessage, byte[] encData) {
        this.typeMessage = typeMessage;
        this.encData = encData;
    }

    public Message(String typeMessage, PublicKey publicKey, String message) {
        this.typeMessage = typeMessage;
        this.publicKey = publicKey;
        this.message = message;

    }

    public String getTypeMessage() {
        return typeMessage;
    }

    public void setTypeMessage(String typeMessage) {
        this.typeMessage = typeMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public byte[] getEncData() {
        return encData;
    }

    public void setEncData(byte[] encData) {
        this.encData = encData;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public String toString() {
        return "Message{" +
                "typeMessage='" + typeMessage + '\'' +
                ", message='" + message + '\'' +
                ", encData=" + Arrays.toString(encData) +
                '}';
    }
}
