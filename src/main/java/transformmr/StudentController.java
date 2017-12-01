package transformmr;

import java.util.concurrent.atomic.AtomicInteger;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class StudentController {
	
	@Autowired
	TradeProperty tradeProperty;
	
	private static final String template = "Hello,%s!";
	private final AtomicInteger counter = new AtomicInteger();
	
	@RequestMapping("/hello")
	public Student hello(@RequestParam(value="name",defaultValue="LiuMing")String name){
		return new Student(String.format(template, name),counter.incrementAndGet());
	}
	
	
	@RequestMapping("/haha")
	public String hello2(@RequestParam(value="name",defaultValue="LiuMing")String name){
		return String.format(template, name);
	}
	
	@RequestMapping("/trade")
	public String hello5(){
		return tradeProperty.getHosturl();
	}
	
	
	@RequestMapping("/mr/get")
	public String hello3(){
		String url=tradeProperty.getHosturl()+"/mr/get/14";
		String param="haha=123&hehe=134";
		String ret = GWTools.sendPost(url, param, 1000);
		return ret;
	}
}
