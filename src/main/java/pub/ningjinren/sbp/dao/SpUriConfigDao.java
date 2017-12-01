package pub.ningjinren.sbp.dao;

import pub.ningjinren.sbp.entity.SpUriConfig;

//@Mapper
public interface SpUriConfigDao {
	
//	@Results({
//		@Result(property = "spid", column = "SPID"),
//		@Result(property = "spname", column = "SPNAME"),
//		@Result(property = "symbol", column = "SYMBOL"),
//		@Result(property = "operator", column = "OPERATOR"),
//		@Result(property = "flag", column = "FLAG"),
//		@Result(property = "spnumber", column = "SPNUMBER"),
//		@Result(property = "mo", column = "MO"),
//		@Result(property = "state", column = "STATE"),
//		@Result(property = "sucstate", column = "SUCSTATE"),
//		@Result(property = "statemsg", column = "STATEMSG"),
//		@Result(property = "linkid", column = "LINKID"),
//		@Result(property = "fee", column = "FEE"),
//		@Result(property = "mobile", column = "MOBILE"),
//		@Result(property = "callbackdata", column = "CALLBACKDATA"),
//		@Result(property = "returnword", column = "RETURNWORD"),
//		})
//	@Select("SELECT * FROM spuriconfig")
	SpUriConfig findById(String spid); 
	
}
