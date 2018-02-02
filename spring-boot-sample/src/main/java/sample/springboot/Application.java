package sample.springboot;

import cn.sccfc.config.client.ApolloPropertiesFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Properties;


@SpringBootApplication
@ImportResource({"classpath:/spring.xml"})
public class Application {
    public static void main(String[] args) throws Exception {
        System.setProperty("env", "DEV");
        SpringApplication.run(Application.class, args);

//        ObjectMapper mapper = new ObjectMapper();
//        //mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
//        DateFormat df = mapper.getDateFormat();
////        String text = mapper.writeValueAsString(new Date());
////        System.out.println(text);
//        Date date = mapper.readValue("1506071711113", Date.class);
//        System.out.println(date);
    }

    @Autowired
    private RefreshScope refreshScope;

    //@Bean
    @org.springframework.cloud.context.config.annotation.RefreshScope
    public Properties env() {
        ApolloPropertiesFactory factory = new ApolloPropertiesFactory();
        factory.setRefreshScope(refreshScope);
        Properties props = factory.create();

        return props;
    }
}