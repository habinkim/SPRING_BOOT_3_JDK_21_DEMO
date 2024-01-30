package com.habin.demo.base.docker;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@TestConfiguration
@Profile("test_standalone")
public class TestContainersConfig {

    private static final String ENV_TZ = "TZ";
    private static final String ASIA_SEOUL = "Asia/Seoul";

    private static final String MYSQL_IMAGE_NAME = "mysql:latest";
    @Container
    public static final MariaDBContainer<?> MARIADB_CONTAINER =
            new MariaDBContainer<>(DockerImageName.parse(MYSQL_IMAGE_NAME))
                    .withEnv(ENV_TZ, ASIA_SEOUL)
                    .withEnv("MARIADB_DATABASE", "test")
                    .withEnv("MARIADB_USER", "test")
                    .withEnv("MARIADB_PASSWORD", "test")
                    .withCommand(
                            "--character-set-server=utf8mb4",
                            "--collation-server=utf8mb4_unicode_ci",
                            "--skip-character-set-client-handshake",
                            "--default-time-zone=+09:00"
                    );

    private static final String REDIS_IMAGE_NAME = "redis:latest";
    @Container
    public static final GenericContainer<?> REDIS_CONTAINER =
            new GenericContainer<>(DockerImageName.parse(REDIS_IMAGE_NAME))
                    .withEnv(ENV_TZ, ASIA_SEOUL)
                    .withExposedPorts(6379);

    private static final DockerImageName KAFKA_IMAGE = DockerImageName.parse("confluentinc/cp-kafka:latest");

    @Container
    public static final KafkaContainer KAFKA_CONTAINER = new KafkaContainer(KAFKA_IMAGE);

    private static final DockerImageName LOCAL_STACK_IMAGE = DockerImageName.parse("localstack/localstack:latest");
    private static final String LOCAL_STACK_S3_BUCKET_NAME = "test";

    @Bean(initMethod = "start", destroyMethod = "stop")
    public static LocalStackContainer localStackContainer() {
        try (LocalStackContainer localStackContainer =
                     new LocalStackContainer(LOCAL_STACK_IMAGE).withServices(LocalStackContainer.Service.S3)) {
            return localStackContainer;
        }
    }

    @Bean
    protected S3Client s3Client() {
        S3Client s3Client = S3Client.builder()
                .endpointOverride(localStackContainer().getEndpointOverride(LocalStackContainer.Service.S3))
                .credentialsProvider(getCredentialsProvider())
                .region(Region.of(localStackContainer().getRegion()))
                .build();

        s3Client.createBucket(builder -> builder.bucket(LOCAL_STACK_S3_BUCKET_NAME));
        return s3Client;
    }

    @NotNull
    private StaticCredentialsProvider getCredentialsProvider() {
        return StaticCredentialsProvider.create(
                AwsBasicCredentials.create(
                        localStackContainer().getAccessKey(),
                        localStackContainer().getSecretKey()
                )
        );
    }

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        MARIADB_CONTAINER.start();
        registry.add("spring.datasource.url",
                () -> String.format("jdbc:mysql://localhost:%d/%s", MARIADB_CONTAINER.getFirstMappedPort(), MARIADB_CONTAINER.getDatabaseName()));
        registry.add("spring.datasource.username", MARIADB_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MARIADB_CONTAINER::getPassword);

        REDIS_CONTAINER.start();
        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
        registry.add("spring.data.redis.port", () -> REDIS_CONTAINER.getMappedPort(6379).toString());
        registry.add("spring.data.redis.database", () -> 0);

        KAFKA_CONTAINER.start();
        registry.add("spring.kafka.bootstrap-servers", KAFKA_CONTAINER::getBootstrapServers);
        registry.add("spring.kafka.consumer.auto-offset-reset", () -> "earliest");
        registry.add("spring.kafka.consumer.group-id", () -> "test");
        registry.add("spring.kafka.consumer.key-deserializer", () -> "org.apache.kafka.common.serialization.StringDeserializer");
        registry.add("spring.kafka.consumer.value-deserializer", () -> "org.apache.kafka.common.serialization.StringDeserializer");
        registry.add("spring.kafka.producer.key-serializer", () -> "org.apache.kafka.common.serialization.StringSerializer");
        registry.add("spring.kafka.producer.value-serializer", () -> "org.apache.kafka.common.serialization.StringSerializer");

        registry.add("aws.access-key-id", localStackContainer()::getAccessKey);
        registry.add("aws.secret-access-key", localStackContainer()::getSecretKey);
        registry.add("aws.default-region", localStackContainer()::getRegion);
        registry.add("aws.s3-bucket-name", () -> LOCAL_STACK_S3_BUCKET_NAME);
    }

}
