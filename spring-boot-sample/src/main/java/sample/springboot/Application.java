package sample.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;


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
}