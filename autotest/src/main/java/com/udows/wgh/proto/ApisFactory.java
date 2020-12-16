//
//  ApisFactory
//
//  Created by ryan on 2017-11-04 10:57:57
//  Copyright (c) ryan All rights reserved.


/**
   apis集合
*/

package com.udows.wgh.proto;


import com.udows.wgh.proto.apis.Apiindex;

public class ApisFactory {


	
	/**
	 * 首页
	 */
	public static Apiindex getApiindex(){
		return new Apiindex();
	}

}
