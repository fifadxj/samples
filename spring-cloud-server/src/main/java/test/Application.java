package test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Application {
    public static void main(String[] args) throws Exception {
        System.setProperty("DEPLOY_ENV", "test");
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