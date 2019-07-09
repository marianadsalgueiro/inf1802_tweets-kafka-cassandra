import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import twitter4j.*;

import java.time.LocalDate;
import java.util.Properties;
import java.util.logging.Logger;

public class TweetListener implements StatusListener {
    private static final Logger logger = Logger.getLogger(TweetListener.class.getName());

    private KafkaProducer<String, Tweet> producer;
    private String topicName = "tweets-input";
    private String boostrap_server = "localhost:9092";

    public TweetListener() {
        // Criar as propriedades do produtor
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, boostrap_server);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, TweetSerializer.class.getName());
        // Criar o produtor
        this.producer = new KafkaProducer<>(properties);
    }

    public void close() {
        producer.close();
    }

    @Override
    public void onStatus(Status status) {
        //Tweet t = new Tweet(status.getId(), status.getText(), status.getText(), LocalDate.of(status.getCreatedAt().getYear(), status.getCreatedAt().getMonth(), status.getCreatedAt().getDay()), status.getText(), status.isTruncated(), status.getGeoLocation(), status.isFavorited(), "politica");
        Tweet t = new Tweet(status.getId(), status.getUser().getName(), status.getText(), status.getText(), status.isTruncated(), status.isFavorited(), "politica");
        // Enviar as mensagens
        ProducerRecord<String, Tweet> record = new ProducerRecord<>(topicName, t);
        producer.send(record);
        logger.info(t.getuser());
        //logger.info(t.getLanguage() + "--> " + t.toString());
    }

    @Override
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

    }

    @Override
    public void onTrackLimitationNotice(int i) {
    }

    @Override
    public void onScrubGeo(long l, long l1) {

    }

    @Override
    public void onStallWarning(StallWarning stallWarning) {

    }

    @Override
    public void onException(Exception e) {

    }
}