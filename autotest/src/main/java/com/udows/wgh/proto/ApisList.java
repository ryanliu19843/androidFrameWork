//
//  ApisList
//
//  Created by ryan on 2017-11-04 14:01:42
//  Copyright (c) ryan All rights reserved.


/**
   
*/

package com.udows.wgh.proto;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ApisList {


	/*****  *****/
	public static class ModelMsgsMsgData extends MJsonData{
		private static final long serialVersionUID = 1L;

		public List<ModelMsg > data=new ArrayList<ModelMsg > (); //data
		public int count; //总数

		public ModelMsgsMsgData() throws Exception {
		}

		public ModelMsgsMsgData(JSONObject json) throws Exception {
			this.build(json);
		}

		public void build(JSONObject json) throws Exception{
			getJsonArray(json, "data", ModelMsg .class, data); //data
			count= getJsonInt(json,"count"); //总数
		}

	}

	/*****  *****/
	public static class CasePic extends MJsonData{
		private static final long serialVersionUID = 1L;

		public String case_pic; //案例图片

		public CasePic() throws Exception {
		}

		public CasePic(JSONObject json) throws Exception {
			this.build(json);
		}

		public void build(JSONObject json) throws Exception{
			case_pic= getJsonString(json,"case_pic"); //案例图片
		}

	}

	/*****  *****/
	public static class ExpertWordMsgData extends MJsonData{
		private static final long serialVersionUID = 1L;

		public ExpertWordMsg data; //data

		public ExpertWordMsgData() throws Exception {
		}

		public ExpertWordMsgData(JSONObject json) throws Exception {
			this.build(json);
		}

		public void build(JSONObject json) throws Exception{
			 if (json.has("data")) {
         data=new ExpertWordMsg (json.getJSONObject("data"));
			 } //data
		}

	}

	/*****  *****/
	public static class ExperMsgData extends MJsonData{
		private static final long serialVersionUID = 1L;

		public ExpertMsg data; //data

		public ExperMsgData() throws Exception {
		}

		public ExperMsgData(JSONObject json) throws Exception {
			this.build(json);
		}

		public void build(JSONObject json) throws Exception{
			 if (json.has("data")) {
         data=new ExpertMsg (json.getJSONObject("data"));
			 } //data
		}

	}

	/*****  *****/
	public static class LogMsgData extends MJsonData{
		private static final long serialVersionUID = 1L;

		public Account data; //data

		public LogMsgData() throws Exception {
		}

		public LogMsgData(JSONObject json) throws Exception {
			this.build(json);
		}

		public void build(JSONObject json) throws Exception{
			 if (json.has("data")) {
         data=new Account (json.getJSONObject("data"));
			 } //data
		}

	}

	/*****  *****/
	public static class ExpertsMsgData extends MJsonData{
		private static final long serialVersionUID = 1L;

		public List<ExpertMsg > data=new ArrayList<ExpertMsg > (); //data
		public int count; //总数

		public ExpertsMsgData() throws Exception {
		}

		public ExpertsMsgData(JSONObject json) throws Exception {
			this.build(json);
		}

		public void build(JSONObject json) throws Exception{
			getJsonArray(json, "data", ExpertMsg .class, data); //data
			count= getJsonInt(json,"count"); //总数
		}

	}

	/*****  *****/
	public static class Account extends MJsonData{
		private static final long serialVersionUID = 1L;

		public String account; //名称
		public String mobile; //电话
		public String realname; //身份证
		public String status; //状态	
		public String license; //营业执照号	
		public String legalname; //营业名称	

		public Account() throws Exception {
		}

		public Account(JSONObject json) throws Exception {
			this.build(json);
		}

		public void build(JSONObject json) throws Exception{
			account= getJsonString(json,"account"); //名称
			mobile= getJsonString(json,"mobile"); //电话
			realname= getJsonString(json,"realname"); //身份证
			status= getJsonString(json,"status"); //状态	
			license= getJsonString(json,"license"); //营业执照号	
			legalname= getJsonString(json,"legalname"); //营业名称	
		}

	}

	/*****  *****/
	public static class RecruitMsg extends MJsonData{
		private static final long serialVersionUID = 1L;

		public String mid; // 22,
		public String title; // 程序员10人, //标题
		public String gongzi; // 3000, //工资
		public String member_name; // 内蒙古先诚, //招聘单位
		public String gs_name; // 程序员, //招聘职位
		public String touchaddress; // 内蒙古呼和浩特,//工作地址
		public String create_time; // 1509707533 //发布时间
		public String num; // 20, //人数
		public String xueli; // 2,//学历1为不限，2为本科，3为大专
		public String nianxian; // 3, //工作年限
		public String fresh; // 1, //是否可接受应届生，1可，0不可
		public String content_post; // , //岗位职责

		public RecruitMsg() throws Exception {
		}

		public RecruitMsg(JSONObject json) throws Exception {
			this.build(json);
		}

		public void build(JSONObject json) throws Exception{
			mid= getJsonString(json,"id"); // 22,
			title= getJsonString(json,"title"); // 程序员10人, //标题
			gongzi= getJsonString(json,"gongzi"); // 3000, //工资
			member_name= getJsonString(json,"member_name"); // 内蒙古先诚, //招聘单位
			gs_name= getJsonString(json,"gs_name"); // 程序员, //招聘职位
			touchaddress= getJsonString(json,"touchaddress"); // 内蒙古呼和浩特,//工作地址
			create_time= getJsonString(json,"create_time"); // 1509707533 //发布时间
			num= getJsonString(json,"num"); // 20, //人数
			xueli= getJsonString(json,"xueli"); // 2,//学历1为不限，2为本科，3为大专
			nianxian= getJsonString(json,"nianxian"); // 3, //工作年限
			fresh= getJsonString(json,"fresh"); // 1, //是否可接受应届生，1可，0不可
			content_post= getJsonString(json,"content_post"); // , //岗位职责
		}

	}

	/*****  *****/
	public static class ResumesMsg extends MJsonData{
		private static final long serialVersionUID = 1L;

		public String mid; // 4, //编号
		public String username; // 杨彦飞, //姓名
		public String sex; // 男, //性别
		public String birth; // 1989, //出生年月日
		public String life; // 2, //工作年限
		public String education; // 2,//最高学历1为本科，2为研究生，3为专科
		public String wanted; // 程序员, //求职意向
		public String desaddress; // 内蒙古呼和浩特市, //期望地址
		public String wages; // 3000, //期望工资
		public String place; // 内蒙古呼和浩特市, //籍贯
		public String address; // 内蒙古呼和浩特市, //地址
		public String create_time; // 1509436156 //发布时间

		public ResumesMsg() throws Exception {
		}

		public ResumesMsg(JSONObject json) throws Exception {
			this.build(json);
		}

		public void build(JSONObject json) throws Exception{
			mid= getJsonString(json,"id"); // 4, //编号
			username= getJsonString(json,"username"); // 杨彦飞, //姓名
			sex= getJsonString(json,"sex"); // 男, //性别
			birth= getJsonString(json,"birth"); // 1989, //出生年月日
			life= getJsonString(json,"life"); // 2, //工作年限
			education= getJsonString(json,"education"); // 2,//最高学历1为本科，2为研究生，3为专科
			wanted= getJsonString(json,"wanted"); // 程序员, //求职意向
			desaddress= getJsonString(json,"desaddress"); // 内蒙古呼和浩特市, //期望地址
			wages= getJsonString(json,"wages"); // 3000, //期望工资
			place= getJsonString(json,"place"); // 内蒙古呼和浩特市, //籍贯
			address= getJsonString(json,"address"); // 内蒙古呼和浩特市, //地址
			create_time= getJsonString(json,"create_time"); // 1509436156 //发布时间
		}

	}

	/*****  *****/
	public static class RecruitMsgData extends MJsonData{
		private static final long serialVersionUID = 1L;

		public RecruitMsg data; //data

		public RecruitMsgData() throws Exception {
		}

		public RecruitMsgData(JSONObject json) throws Exception {
			this.build(json);
		}

		public void build(JSONObject json) throws Exception{
			 if (json.has("data")) {
         data=new RecruitMsg (json.getJSONObject("data"));
			 } //data
		}

	}

	/*****  *****/
	public static class ResumesesMsgData extends MJsonData{
		private static final long serialVersionUID = 1L;

		public List<ResumesMsg > data=new ArrayList<ResumesMsg > (); //data
		public int count; //总数

		public ResumesesMsgData() throws Exception {
		}

		public ResumesesMsgData(JSONObject json) throws Exception {
			this.build(json);
		}

		public void build(JSONObject json) throws Exception{
			getJsonArray(json, "data", ResumesMsg .class, data); //data
			count= getJsonInt(json,"count"); //总数
		}

	}

	/*****  *****/
	public static class ModelMsgMsgData extends MJsonData{
		private static final long serialVersionUID = 1L;

		public ModelMsg data; //data

		public ModelMsgMsgData() throws Exception {
		}

		public ModelMsgMsgData(JSONObject json) throws Exception {
			this.build(json);
		}

		public void build(JSONObject json) throws Exception{
			 if (json.has("data")) {
         data=new ModelMsg (json.getJSONObject("data"));
			 } //data
		}

	}

	/*****  *****/
	public static class News extends MJsonData{
		private static final long serialVersionUID = 1L;

		public String mid; //编号
		public String title; //标题
		public String content; //内容
		public String create_time; //时间
		public String browse; //浏览次数
		public String loginid; //来源

		public News() throws Exception {
		}

		public News(JSONObject json) throws Exception {
			this.build(json);
		}

		public void build(JSONObject json) throws Exception{
			mid= getJsonString(json,"id"); //编号
			title= getJsonString(json,"title"); //标题
			content= getJsonString(json,"content"); //内容
			create_time= getJsonString(json,"create_time"); //时间
			browse= getJsonString(json,"browse"); //浏览次数
			loginid= getJsonString(json,"loginid"); //来源
		}

	}

	/*****  *****/
	public static class NewsDetailData extends MJsonData{
		private static final long serialVersionUID = 1L;

		public News data; //data

		public NewsDetailData() throws Exception {
		}

		public NewsDetailData(JSONObject json) throws Exception {
			this.build(json);
		}

		public void build(JSONObject json) throws Exception{
			 if (json.has("data")) {
         data=new News (json.getJSONObject("data"));
			 } //data
		}

	}

	/*****  *****/
	public static class ResumesMsgData extends MJsonData{
		private static final long serialVersionUID = 1L;

		public ResumesMsg data; //data

		public ResumesMsgData() throws Exception {
		}

		public ResumesMsgData(JSONObject json) throws Exception {
			this.build(json);
		}

		public void build(JSONObject json) throws Exception{
			 if (json.has("data")) {
         data=new ResumesMsg (json.getJSONObject("data"));
			 } //data
		}

	}

	/*****  *****/
	public static class ModelMsg extends MJsonData{
		private static final long serialVersionUID = 1L;

		public String mid; // 78, //编号
		public String title; // 测试范本一本本, //标题
		public String attach; //  //文件路径：path路径，name;文件名称

		public ModelMsg() throws Exception {
		}

		public ModelMsg(JSONObject json) throws Exception {
			this.build(json);
		}

		public void build(JSONObject json) throws Exception{
			mid= getJsonString(json,"id"); // 78, //编号
			title= getJsonString(json,"title"); // 测试范本一本本, //标题
			attach= getJsonString(json,"attach"); //  //文件路径：path路径，name;文件名称
		}

	}

	/*****  *****/
	public static class ExpertMsg extends MJsonData{
		private static final long serialVersionUID = 1L;

		public String mid; //编号
		public String exp_name; //姓名
		public String exp_thumpic; //头像
		public String exp_forage; //从业年限	
		public String exp_comp; //所属单位	
		public String exp_address; //联系地址	
		public String exp_content; //专家简介	
		public CasePic mcase; //案例图片
		public String exp_good; //所属行业		
		public String exp_phone; //电话		

		public ExpertMsg() throws Exception {
		}

		public ExpertMsg(JSONObject json) throws Exception {
			this.build(json);
		}

		public void build(JSONObject json) throws Exception{
			mid= getJsonString(json,"id"); //编号
			exp_name= getJsonString(json,"exp_name"); //姓名
			exp_thumpic= getJsonString(json,"exp_thumpic"); //头像
			exp_forage= getJsonString(json,"exp_forage"); //从业年限	
			exp_comp= getJsonString(json,"exp_comp"); //所属单位	
			exp_address= getJsonString(json,"exp_address"); //联系地址	
			exp_content= getJsonString(json,"exp_content"); //专家简介	
			 if (json.has("case")) {
         mcase=new CasePic (json.getJSONObject("case"));
			 } //案例图片		
			exp_good= getJsonString(json,"exp_good"); //所属行业		
			exp_phone= getJsonString(json,"exp_phone"); //电话		
		}

	}

	/*****  *****/
	public static class RecruitsMsgData extends MJsonData{
		private static final long serialVersionUID = 1L;

		public List<RecruitMsg > data=new ArrayList<RecruitMsg > (); //data
		public int count; //总数

		public RecruitsMsgData() throws Exception {
		}

		public RecruitsMsgData(JSONObject json) throws Exception {
			this.build(json);
		}

		public void build(JSONObject json) throws Exception{
			getJsonArray(json, "data", RecruitMsg .class, data); //data
			count= getJsonInt(json,"count"); //总数
		}

	}

	/*****  *****/
	public static class ExpertWordMsg extends MJsonData{
		private static final long serialVersionUID = 1L;

		public String code; // 200,
		public String status; // true,
		public String data; // {
		public String xid; // , //专家编号
		public String com_title; // , //公司名称
		public String com_phone; // , //法人电话
		public String com_legal; // , //法人姓名
		public String title; // , //留言标题
		public String content; //   //留言内容

		public ExpertWordMsg() throws Exception {
		}

		public ExpertWordMsg(JSONObject json) throws Exception {
			this.build(json);
		}

		public void build(JSONObject json) throws Exception{
			code= getJsonString(json,"code"); // 200,
			status= getJsonString(json,"status"); // true,
			data= getJsonString(json,"data"); // {
			xid= getJsonString(json,"xid"); // , //专家编号
			com_title= getJsonString(json,"com_title"); // , //公司名称
			com_phone= getJsonString(json,"com_phone"); // , //法人电话
			com_legal= getJsonString(json,"com_legal"); // , //法人姓名
			title= getJsonString(json,"title"); // , //留言标题
			content= getJsonString(json,"content"); //   //留言内容
		}

	}

	/*****  *****/
	public static class NewsData extends MJsonData{
		private static final long serialVersionUID = 1L;

		public List<News > data=new ArrayList<News > (); //data

		public NewsData() throws Exception {
		}

		public NewsData(JSONObject json) throws Exception {
			this.build(json);
		}

		public void build(JSONObject json) throws Exception{
			getJsonArray(json, "data", News .class, data); //data
		}

	}


}
