import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.common.serialization.Deserializer;
import java.io.IOException;
import java.util.Map;


public class TweetDeserializer implements Deserializer<Tweet> {
    static final ObjectMapper mapper = new ObjectMapper();

    public TweetDeserializer() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        mapper.registerModule(javaTimeModule);
    }

    @Override
    public void configure(Map<String, ?> map, boolean bln) {
    }

    @Override
    public Tweet deserialize(String topic, byte[] bytes) {
        Tweet tweet = null;
        try {
            tweet = mapper.readValue(bytes, Tweet.class);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return tweet;
    }

    @Override
    public void close() {
    }
}
