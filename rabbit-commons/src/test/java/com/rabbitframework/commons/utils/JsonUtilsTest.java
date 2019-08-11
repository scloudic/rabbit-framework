package com.rabbitframework.commons.utils;

import java.util.List;

import com.rabbitframework.commons.bean.DataResponse;
import com.rabbitframework.commons.bean.DetailData;
import com.rabbitframework.commons.bean.IData;
import com.rabbitframework.commons.bean.UserData;

public class JsonUtilsTest {
	public static void main(String[] args) {
		UserData userData = new UserData();
		userData.setUserName("userdata");
		DetailData detailData = new DetailData();
		detailData.setDetailName("detail");
		DataResponse dataResponse = new DataResponse();
		List<IData> idatas = dataResponse.getData();
		idatas.add(userData);
		idatas.add(detailData);
		String s = JsonUtils.nullConvertToJsonStr(dataResponse);
		System.out.println(s);
//		DataResponse d = JsonUtils.getObject(s, DataResponse.class);
//		List<IData> idataResult = d.getData();
//		for (IData iData : idataResult) {
//			System.out.println(iData.getClass().getName());
//		}
		// UserData iData = (UserData) idataResult.get(0);
		// System.out.println(iData.getUserName());
		// DetailData detailData2 = (DetailData) idataResult.get(1);
		// System.out.println(detailData2.getDetailName());
	}
}
