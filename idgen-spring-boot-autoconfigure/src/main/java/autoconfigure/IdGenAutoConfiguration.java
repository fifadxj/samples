package autoconfigure;

import com.dangdang.ddframe.rdb.sharding.id.generator.IdGenerator;
import com.dangdang.ddframe.rdb.sharding.id.generator.self.IPIdGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by za-daixiaojun on 2017/12/11.
 */
@Configuration
@ConditionalOnClass(IdGenerator.class)
@ConditionalOnMissingBean(IdGenerator.class)
public class IdGenAutoConfiguration {

    @Bean
    public IdGenerator IdGenerator() {
        IdGenerator idGenerator = new IPIdGenerator();

        return idGenerator;
    }
}
