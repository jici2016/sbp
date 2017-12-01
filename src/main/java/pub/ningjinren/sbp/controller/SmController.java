package pub.ningjinren.sbp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pub.ningjinren.sbp.entity.SpUriConfig;
import pub.ningjinren.sbp.props.TradeProperty;
import pub.ningjinren.sbp.utils.GWTools;

@RestController
@RequestMapping("/sm")
public class SmController {
	
	@Autowired
	private TradeProperty tradeProperty;
	
//	@Autowired
//	private SpUriConfigService spUriConfigService;
	
	
	
	private StringBuffer sb = new StringBuffer(); 
	/**
	 * 获取没有上行号码和指令的Mo
	 * @return
	 */
	@RequestMapping(value={"/mr/get/{spid}","/mr/post/{spid}"})
	public String getReqWithoutSpNumAndMo(@PathVariable(name="spid")String spid,HttpServletRequest req) {
		String ret=spid;
		String param = req.getQueryString();
		String uri=req.getRequestURI();
		System.out.println("param:"+param);
		System.out.println("uri:"+uri);
		sb.delete(0, sb.length());
		
		
		SpUriConfig config = null;// spUriConfigService.findById(spid);
		String price = req.getParameter(config.getFee());
		sb.append("spnumber=").append("106666").append(spid).append("&mo=").append(price).append("&").append(param);
		String newparam = sb.toString().trim();
		sb.delete(0, sb.length());
		System.out.println("newparam:"+newparam);
		String url = "http://"+tradeProperty.getHost()+":"+tradeProperty.getPort()+uri;
		//增加长号码和参数
		ret=GWTools.sendPost(url, newparam, 1000);
		
		return ret==null?"":ret;
	}
	
}
