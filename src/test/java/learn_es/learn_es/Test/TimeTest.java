package learn_es.learn_es.Test;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by LearnDH on 2019/1/17.
 */
public class TimeTest {

    @Test
    public void test(){
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime now = LocalDateTime.now();
        String format = dateFormat.format(now);
        System.out.println(format);
    }
}
