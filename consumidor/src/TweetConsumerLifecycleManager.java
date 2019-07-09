import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.Serializable;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TweetConsumerLifecycleManager implements LifecycleManager, Serializable {
    private static final String KAFKA_CLUSTER = System.getenv().getOrDefault("KAFKA_CLUSTER", "localhost:9092");
    private static final String CONSUMER_GROUP = "tweet-application";
    private static final String TOPIC_NAME = "tweets-input";
    private static final Logger logger = LoggerFactory.getLogger(TweetConsumerLifecycleManager.class.getName());
    private final AtomicBoolean running = new AtomicBoolean(false);
    private KafkaConsumer<String, Tweet> consumer;
    private Future<?> future;
    Cluster cluster = null;
    TweetRepository tr;
    KeyspaceRepository kr;

    public TweetConsumerLifecycleManager() {
        Properties kafkaProps = new Properties();
        kafkaProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_CLUSTER);
        kafkaProps.put(ConsumerConfig.GROUP_ID_CONFIG, CONSUMER_GROUP);
        kafkaProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        kafkaProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, TweetDeserializer.class.getName());
        this.consumer = new KafkaConsumer<>(kafkaProps);
    }

    public void start()  {

        //cassandra
        cluster = Cluster.builder()
                .addContactPoint("localhost")
                .build();

        Session session = cluster.connect();

        ResultSet rs = session.execute("select release_version from system.local");
        Row row = rs.one();
        System.out.println(((Row) row).getString("release_version"));

        this.kr = new KeyspaceRepository(session);
        kr.createKeyspace("library", "SimpleStrategy", 1);
        System.out.println("Create repository");
        kr.useKeyspace("library");
        System.out.println("Using repository library");
        this.tr = new TweetRepository(session);

        tr.createTableTweetsByCategoria();
        //cassandra

        if (running.compareAndSet(false, true)) {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            future = executor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        consumer.subscribe(Arrays.asList(TOPIC_NAME));
                        logger.info("Consumidor subscrito no tópico: ", TOPIC_NAME);
                        while (true) {
                            ConsumerRecords<String, Tweet> records = consumer.poll(Duration.ofMillis(10000));
                            for (ConsumerRecord<String, Tweet> record : records) {
                                Tweet tweet = record.value();
                                logger.info("Consumindo do Kafka o Tweet: " + tweet);
                                tr.insertTweetbycategoria(tweet);
                            }
                            tr.selectAll();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error ("Erro no consumo dos tweets do Kafka", e);
                    } finally {
                        consumer.close();
                    }
                }
            });
            logger.info("Serviço iniciado");
        } else {
            logger.warn("O serviço já está executando.");
        }
    }

    public void stop()  {
        if (running.compareAndSet(true, false)) {
            if (future.cancel(true)) {
                consumer.wakeup();
            }

            tr.deleteTable("tweetsbycategoria");
            System.out.println("Delete table tweetsbycategoria!");

            kr.deleteKeyspace("library");
            System.out.println("Delete keyspace!");


            logger.info("Serviço finalizado");
        } else {
            logger.warn("O serviço não está executando. Não pode ser parado.");
        }
    }

}
