package sample.autoconfigure;

import org.apache.shardingsphere.core.strategy.keygen.SnowflakeShardingKeyGenerator;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sample.idgen.IdGen;

import java.util.UUID;

@Configuration
public class IdGenAutoConfiguration {
    @ConditionalOnMissingBean(IdGen.class)
    @Bean
    public IdGen uuidGen() {
        IdGen idGen = () -> UUID.randomUUID().toString();

        return idGen;
    }

    @ConditionalOnClass(SnowflakeShardingKeyGenerator.class)
    private static class SnowflakeShardingKeyGeneratorConfiguration {
        @Bean
        public IdGen snowflakeSharingKeyGenerator() {
            SnowflakeShardingKeyGenerator  shardingKeyGenerator = new SnowflakeShardingKeyGenerator();

            IdGen idGen = () -> (String) shardingKeyGenerator.generateKey().toString();

            return idGen;
        }
    }
}
