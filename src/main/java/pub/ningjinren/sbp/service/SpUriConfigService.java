package pub.ningjinren.sbp.service;

import pub.ningjinren.sbp.dao.SpUriConfigDao;
import pub.ningjinren.sbp.entity.SpUriConfig;


public class SpUriConfigService {


//	@Autowired
	private SpUriConfigDao spUriConfigDao;
	
	public SpUriConfig findById(String spid) {
		return spUriConfigDao.findById(spid);
	}
}
