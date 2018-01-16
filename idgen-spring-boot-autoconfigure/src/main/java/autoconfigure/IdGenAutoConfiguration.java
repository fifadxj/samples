package autoconfigure;

import io.shardingjdbc.core.keygen.DefaultKeyGenerator;
import io.shardingjdbc.core.keygen.KeyGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by za-daixiaojun on 2017/12/11.
 */
@Configuration
@ConditionalOnClass(KeyGenerator.class)
@ConditionalOnMissingBean(KeyGenerator.class)
public class IdGenAutoConfiguration {

    @Bean
    public KeyGenerator IdGenerator() {
        DefaultKeyGenerator idGenerator = new DefaultKeyGenerator();

        return idGenerator;
    }
}
