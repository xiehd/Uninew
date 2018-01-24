package com.uninew.maintanence.presenter;

import java.util.List;
import java.util.Map;

public interface IUpdateResultCallBack {

	/**
	 * 升级请求回调接口
	 * @param result 请求结果：0-
	 * @param updateMsgs 保存查询到的文件信息，具体参考接口文档
	 */
	void updateResultCallBack(int result, List<Map<String, String>> updateMsgs);
	
}
