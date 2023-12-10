package cn.edu.bupt.sa.kwic.source;

import cn.edu.bupt.sa.kwic.Pipe;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class KafkaSource extends Source<String>{
    private String bootstrapServerConfig;
    private String groupIdConfig;
    private String keyDeserializerClassConfig;
    private String valueDeserializerClassConfig;
    private int maxPollRecordsConfig;
    private int pollTimes;


    // TODO: kafka配置可以使用配置文件读取
    public KafkaSource(
            Pipe<String> input,
            String bootstrapServerConfig,
            String groupIdConfig,
            String keyDeserializerClassConfig,
            String valueDeserializerClassConfig,
            int maxPollRecordsConfig,
            int pollTimes
            ){
        super(input);
        if(bootstrapServerConfig == null || bootstrapServerConfig.isEmpty()) {
            throw new IllegalArgumentException("bootstrapServerConfig is null or empty.");
        }
        if(groupIdConfig == null || groupIdConfig.isEmpty()) {
            throw new IllegalArgumentException("groupIdConfig is null or empty.");
        }
        if(keyDeserializerClassConfig == null || keyDeserializerClassConfig.isEmpty()) {
            throw new IllegalArgumentException("keyDeserializerClassConfig is null or empty.");
        }
        if(valueDeserializerClassConfig == null || valueDeserializerClassConfig.isEmpty()) {
            throw new IllegalArgumentException("valueDeserializerClassConfig is null or empty.");
        }
        if(maxPollRecordsConfig <= 0 || pollTimes <= 0){
            throw new IllegalArgumentException("maxPollRecordsConfig or pollTimes is non-positive.");
        }
        this.bootstrapServerConfig = bootstrapServerConfig;
        this.groupIdConfig = groupIdConfig;
        this.keyDeserializerClassConfig = keyDeserializerClassConfig;
        this.valueDeserializerClassConfig =valueDeserializerClassConfig;
        this.maxPollRecordsConfig = maxPollRecordsConfig;
        this.pollTimes = pollTimes;
    }
    @Override
    protected void handleInput() {
        // Kafka配置
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.bootstrapServerConfig);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, this.groupIdConfig);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, this.keyDeserializerClassConfig);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, this.valueDeserializerClassConfig);
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, this.maxPollRecordsConfig);

        // 创建Kafka消费者
        try (Consumer<String, String> consumer = new KafkaConsumer<>(properties)){
            // 订阅Topic
            // TODO: topic不能写死
            consumer.subscribe(Collections.singletonList("line_topic"));

            // 循环读取消息
            while (this.pollTimes > 0) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

                // 处理每条消息
                records.forEach(record -> {
                    String line = record.value();
                    System.out.printf(line);
                    // 在这里进行消息的处理逻辑
                    this.outPipe.put(line);
                });

                this.pollTimes = this.pollTimes - 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
