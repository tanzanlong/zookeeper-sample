import org.springframework.web.client.RestTemplate;

/**
 * Created by l_yy on 2017/3/24.
 */
public class RestTemplateTest {
    public static void main(String[] args){
        RestTemplate restTemplate = new RestTemplate();
        String str = restTemplate.getForObject(
                "http://localhost:8080/crsWebApp/web/RateQuery/result.do?unitinfoId=22&rmtypeId=4&ratedefId=10026",
                String.class);

        System.out.println(str);
    }
}
