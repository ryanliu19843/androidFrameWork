//
//  Index
//
//  Created by ryan on 2017-11-04 14:01:42
//  Copyright (c) ryan All rights reserved.


/**
   
*/

package com.udows.wgh.proto;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Index {


	/*****  *****/
	public static class Entrep extends MJsonData{
		private static final long serialVersionUID = 1L;

		public String mid; //编号
		public String pictrue; //图片
		public String href; //链接

		public Entrep() throws Exception {
		}

		public Entrep(JSONObject json) throws Exception {
			this.build(json);
		}

		public void build(JSONObject json) throws Exception{
			mid= getJsonString(json,"id"); //编号
			pictrue= getJsonString(json,"pictrue"); //图片
			href= getJsonString(json,"href"); //链接
		}

	}

	/*****  *****/
	public static class Ask extends MJsonData{
		private static final long serialVersionUID = 1L;

		public String mid; //编号
		public String title; //标题
		public String pictrue;
		public String content;

		public Ask() throws Exception {
		}

		public Ask(JSONObject json) throws Exception {
			this.build(json);
		}

		public void build(JSONObject json) throws Exception{
			mid= getJsonString(json,"id"); //编号
			title= getJsonString(json,"title"); //标题
			pictrue= getJsonString(json,"pictrue");
			content= getJsonString(json,"content");
		}

	}

	/*****  *****/
	public static class IndexData extends MJsonData{
		private static final long serialVersionUID = 1L;

		public IndexMsg data; //data

		public IndexData() throws Exception {
		}

		public IndexData(JSONObject json) throws Exception {
			this.build(json);
		}

		public void build(JSONObject json) throws Exception{
			 if (json.has("data")) {
         data=new IndexMsg (json.getJSONObject("data"));
			 } //data
		}

	}

	/*****  *****/
	public static class Company extends MJsonData{
		private static final long serialVersionUID = 1L;

		public String mid; //编号
		public String title; //公司名称

		public Company() throws Exception {
		}

		public Company(JSONObject json) throws Exception {
			this.build(json);
		}

		public void build(JSONObject json) throws Exception{
			mid= getJsonString(json,"id"); //编号
			title= getJsonString(json,"title"); //公司名称
		}

	}

	/*****  *****/
	public static class Statute extends MJsonData{
		private static final long serialVersionUID = 1L;

		public String mid; //编号
		public String title; //标题

		public Statute() throws Exception {
		}

		public Statute(JSONObject json) throws Exception {
			this.build(json);
		}

		public void build(JSONObject json) throws Exception{
			mid= getJsonString(json,"id"); //编号
			title= getJsonString(json,"title"); //标题
		}

	}

	/*****  *****/
	public static class Supervise extends MJsonData{
		private static final long serialVersionUID = 1L;

		public String mid; //编号
		public String title; //标题
		public String create_time; //时间戳

		public Supervise() throws Exception {
		}

		public Supervise(JSONObject json) throws Exception {
			this.build(json);
		}

		public void build(JSONObject json) throws Exception{
			mid= getJsonString(json,"id"); //编号
			title= getJsonString(json,"title"); //标题
			create_time= getJsonString(json,"create_time"); //时间戳
		}

	}

	/*****  *****/
	public static class Site extends MJsonData{
		private static final long serialVersionUID = 1L;

		public String title; //站点名称
		public String keywords; //关键词
		public String description; //内容摘要
		public String address; //联系地址
		public String sponsor; //主办单位
		public String organizer; //承办单位
		public String hotline; //客服电话

		public Site() throws Exception {
		}

		public Site(JSONObject json) throws Exception {
			this.build(json);
		}

		public void build(JSONObject json) throws Exception{
			title= getJsonString(json,"title"); //站点名称
			keywords= getJsonString(json,"keywords"); //关键词
			description= getJsonString(json,"description"); //内容摘要
			address= getJsonString(json,"address"); //联系地址
			sponsor= getJsonString(json,"sponsor"); //主办单位
			organizer= getJsonString(json,"organizer"); //承办单位
			hotline= getJsonString(json,"hotline"); //客服电话
		}

	}

	/*****  *****/
	public static class Financings extends MJsonData{
		private static final long serialVersionUID = 1L;

		public String mid; //编号
		public String title; //图片路径
		public String create_time; //链接

		public Financings() throws Exception {
		}

		public Financings(JSONObject json) throws Exception {
			this.build(json);
		}

		public void build(JSONObject json) throws Exception{
			mid= getJsonString(json,"id"); //编号
			title= getJsonString(json,"title"); //图片路径
			create_time= getJsonString(json,"create_time"); //链接
		}

	}

	/*****  *****/
	public static class Train extends MJsonData{
		private static final long serialVersionUID = 1L;

		public String mid; //编号
		public String title; //标题

		public Train() throws Exception {
		}

		public Train(JSONObject json) throws Exception {
			this.build(json);
		}

		public void build(JSONObject json) throws Exception{
			mid= getJsonString(json,"id"); //编号
			title= getJsonString(json,"title"); //标题
		}

	}

	/*****  *****/
	public static class IndexMsg extends MJsonData{
		private static final long serialVersionUID = 1L;

		public Site site; //站点信息
		public List<Banners > banners=new ArrayList<Banners > (); //横幅3张
		public Hotspot hotspot; //热点焦点1条
		public Entrep entrep; // 创业吧 1 张
		public List<Skill > skill=new ArrayList<Skill > (); //创业技巧5条
		public List<Finabanner > finabanner=new ArrayList<Finabanner > (); ///投融资讯4张图
		public List<Financials > financials=new ArrayList<Financials > (); ///投融资讯
		public List<Financings > financings=new ArrayList<Financings > (); //融资需求
		public List<Trainbs > trainbs=new ArrayList<Trainbs > (); //培训需求4张图
		public List<Train > train=new ArrayList<Train > (); //培训需求
		public List<Industry > industry=new ArrayList<Industry > (); //行业资讯
		public List<Expert > expert=new ArrayList<Expert > (); //专家团队
		public List<Supervise > supervise=new ArrayList<Supervise > (); //管理需求
		public List<Company > company=new ArrayList<Company > (); ///入住平台企业
		public List<Technique > technique=new ArrayList<Technique > (); //技术创新
		public List<Coupon > coupon=new ArrayList<Coupon > (); //优惠券
		public List<Unscramble > unscramble=new ArrayList<Unscramble > (); //政策解读
		public List<Models > models=new ArrayList<Models > (); //范本下载
		public List<Statute > statute=new ArrayList<Statute > (); //法律法规
		public List<Problem > problem=new ArrayList<Problem > (); //热门问题
		public List<Ask > ask=new ArrayList<Ask > (); //图片要闻
		public List<Advert > advert=new ArrayList<Advert > (); //广告图
		public List<Notice > notice=new ArrayList<Notice > (); //通知公告
		public List<Fellow > fellow=new ArrayList<Fellow > (); //友情链接

		public IndexMsg() throws Exception {
		}

		public IndexMsg(JSONObject json) throws Exception {
			this.build(json);
		}

		public void build(JSONObject json) throws Exception{
			 if (json.has("site")) {
         site=new Site (json.getJSONObject("site"));
			 } //站点信息
			getJsonArray(json, "banners", Banners .class, banners); //横幅3张
			 if (json.has("hotspot")) {
         hotspot=new Hotspot (json.getJSONObject("hotspot"));
			 } //热点焦点1条
			 if (json.has("entrep")) {
         entrep=new Entrep (json.getJSONObject("entrep"));
			 } // 创业吧 1 张
			getJsonArray(json, "skill", Skill .class, skill); //创业技巧5条
			getJsonArray(json, "finabanner", Finabanner .class, finabanner); ///投融资讯4张图
			getJsonArray(json, "financials", Financials .class, financials); ///投融资讯
			getJsonArray(json, "financings", Financings .class, financings); //融资需求
			getJsonArray(json, "trainbs", Trainbs .class, trainbs); //培训需求4张图
			getJsonArray(json, "train", Train .class, train); //培训需求
			getJsonArray(json, "industry", Industry .class, industry); //行业资讯
			getJsonArray(json, "expert", Expert .class, expert); //专家团队
			getJsonArray(json, "supervise", Supervise .class, supervise); //管理需求
			getJsonArray(json, "company", Company .class, company); ///入住平台企业
			getJsonArray(json, "technique", Technique .class, technique); //技术创新
			getJsonArray(json, "coupon", Coupon .class, coupon); //优惠券
			getJsonArray(json, "unscramble", Unscramble .class, unscramble); //政策解读
			getJsonArray(json, "models", Models .class, models); //范本下载
			getJsonArray(json, "statute", Statute .class, statute); //法律法规
			getJsonArray(json, "problem", Problem .class, problem); //热门问题
			getJsonArray(json, "ask", Ask .class, ask); //图片要闻
			getJsonArray(json, "advert", Advert .class, advert); //广告图
			getJsonArray(json, "notice", Notice .class, notice); //通知公告
			getJsonArray(json, "fellow", Fellow .class, fellow); //友情链接
		}

	}

	/*****  *****/
	public static class Finabanner extends MJsonData{
		private static final long serialVersionUID = 1L;

		public String mid; //编号
		public String pictrue; //图片路径
		public String href; //链接

		public Finabanner() throws Exception {
		}

		public Finabanner(JSONObject json) throws Exception {
			this.build(json);
		}

		public void build(JSONObject json) throws Exception{
			mid= getJsonString(json,"id"); //编号
			pictrue= getJsonString(json,"pictrue"); //图片路径
			href= getJsonString(json,"href"); //链接
		}

	}

	/*****  *****/
	public static class Models extends MJsonData{
		private static final long serialVersionUID = 1L;

		public String mid; //编号
		public String title; //标题
		public String attach; ///文件路径

		public Models() throws Exception {
		}

		public Models(JSONObject json) throws Exception {
			this.build(json);
		}

		public void build(JSONObject json) throws Exception{
			mid= getJsonString(json,"id"); //编号
			title= getJsonString(json,"title"); //标题
			attach= getJsonString(json,"attach"); ///文件路径
		}

	}

	/*****  *****/
	public static class Problem extends MJsonData{
		private static final long serialVersionUID = 1L;

		public String mid; //编号
		public String title; //标题
		public String create_time;

		public Problem() throws Exception {
		}

		public Problem(JSONObject json) throws Exception {
			this.build(json);
		}

		public void build(JSONObject json) throws Exception{
			mid= getJsonString(json,"id"); //编号
			title= getJsonString(json,"title"); //标题
			create_time= getJsonString(json,"create_time");
		}

	}

	/*****  *****/
	public static class Banners extends MJsonData{
		private static final long serialVersionUID = 1L;

		public String mid; //编号
		public String title; //标题
		public String subtitle; //副标题
		public String pictrue; //图片地址
		public String href; //链接

		public Banners() throws Exception {
		}

		public Banners(JSONObject json) throws Exception {
			this.build(json);
		}

		public void build(JSONObject json) throws Exception{
			mid= getJsonString(json,"id"); //编号
			title= getJsonString(json,"title"); //标题
			subtitle= getJsonString(json,"subtitle"); //副标题
			pictrue= getJsonString(json,"pictrue"); //图片地址
			href= getJsonString(json,"href"); //链接
		}

	}

	/*****  *****/
	public static class Industry extends MJsonData{
		private static final long serialVersionUID = 1L;

		public String mid; //编号
		public String title; //标题
		public String create_time; //链接

		public Industry() throws Exception {
		}

		public Industry(JSONObject json) throws Exception {
			this.build(json);
		}

		public void build(JSONObject json) throws Exception{
			mid= getJsonString(json,"id"); //编号
			title= getJsonString(json,"title"); //标题
			create_time= getJsonString(json,"create_time"); //链接
		}

	}

	/*****  *****/
	public static class Financials extends MJsonData{
		private static final long serialVersionUID = 1L;

		public String mid; //编号
		public String title; //图片路径

		public Financials() throws Exception {
		}

		public Financials(JSONObject json) throws Exception {
			this.build(json);
		}

		public void build(JSONObject json) throws Exception{
			mid= getJsonString(json,"id"); //编号
			title= getJsonString(json,"title"); //图片路径
		}

	}

	/*****  *****/
	public static class Fellow extends MJsonData{
		private static final long serialVersionUID = 1L;

		public String mid; //编号
		public String title; //标题
		public String href; // 链接

		public Fellow() throws Exception {
		}

		public Fellow(JSONObject json) throws Exception {
			this.build(json);
		}

		public void build(JSONObject json) throws Exception{
			mid= getJsonString(json,"id"); //编号
			title= getJsonString(json,"title"); //标题
			href= getJsonString(json,"href"); // 链接
		}

	}

	/*****  *****/
	public static class Advert extends MJsonData{
		private static final long serialVersionUID = 1L;

		public String mid; //编号
		public String pictrue; //图片
		public String href; // 链接

		public Advert() throws Exception {
		}

		public Advert(JSONObject json) throws Exception {
			this.build(json);
		}

		public void build(JSONObject json) throws Exception{
			mid= getJsonString(json,"id"); //编号
			pictrue= getJsonString(json,"pictrue"); //图片
			href= getJsonString(json,"href"); // 链接
		}

	}

	/*****  *****/
	public static class Skill extends MJsonData{
		private static final long serialVersionUID = 1L;

		public String mid; //编号
		public String title; //标题
		public String browse; //点击次数
		public String create_time; //创建时间

		public Skill() throws Exception {
		}

		public Skill(JSONObject json) throws Exception {
			this.build(json);
		}

		public void build(JSONObject json) throws Exception{
			mid= getJsonString(json,"id"); //编号
			title= getJsonString(json,"title"); //标题
			browse= getJsonString(json,"browse"); //点击次数
			create_time= getJsonString(json,"create_time"); //创建时间
		}

	}

	/*****  *****/
	public static class Trainbs extends MJsonData{
		private static final long serialVersionUID = 1L;

		public String mid; //编号
		public String title; //图片路径
		public String href; //链接

		public Trainbs() throws Exception {
		}

		public Trainbs(JSONObject json) throws Exception {
			this.build(json);
		}

		public void build(JSONObject json) throws Exception{
			mid= getJsonString(json,"id"); //编号
			title= getJsonString(json,"title"); //图片路径
			href= getJsonString(json,"href"); //链接
		}

	}

	/*****  *****/
	public static class Unscramble extends MJsonData{
		private static final long serialVersionUID = 1L;

		public String mid; //编号
		public String title; //标题

		public Unscramble() throws Exception {
		}

		public Unscramble(JSONObject json) throws Exception {
			this.build(json);
		}

		public void build(JSONObject json) throws Exception{
			mid= getJsonString(json,"id"); //编号
			title= getJsonString(json,"title"); //标题
		}

	}

	/*****  *****/
	public static class Coupon extends MJsonData{
		private static final long serialVersionUID = 1L;

		public String mid; //编号
		public String exp_name; //姓名
		public String exp_comp; ///所属企业
		public String coupon; //优惠券

		public Coupon() throws Exception {
		}

		public Coupon(JSONObject json) throws Exception {
			this.build(json);
		}

		public void build(JSONObject json) throws Exception{
			mid= getJsonString(json,"id"); //编号
			exp_name= getJsonString(json,"exp_name"); //姓名
			exp_comp= getJsonString(json,"exp_comp"); ///所属企业
			coupon= getJsonString(json,"coupon"); //优惠券
		}

	}

	/*****  *****/
	public static class Hotspot extends MJsonData{
		private static final long serialVersionUID = 1L;

		public String mid; //编号
		public String title; //标题

		public Hotspot() throws Exception {
		}

		public Hotspot(JSONObject json) throws Exception {
			this.build(json);
		}

		public void build(JSONObject json) throws Exception{
			mid= getJsonString(json,"id"); //编号
			title= getJsonString(json,"title"); //标题
		}

	}

	/*****  *****/
	public static class Expert extends MJsonData{
		private static final long serialVersionUID = 1L;

		public String mid; //编号
		public String exp_name; //姓名
		public String exp_pic; //头像

		public Expert() throws Exception {
		}

		public Expert(JSONObject json) throws Exception {
			this.build(json);
		}

		public void build(JSONObject json) throws Exception{
			mid= getJsonString(json,"id"); //编号
			exp_name= getJsonString(json,"exp_name"); //姓名
			exp_pic= getJsonString(json,"exp_pic"); //头像
		}

	}

	/*****  *****/
	public static class Notice extends MJsonData{
		private static final long serialVersionUID = 1L;

		public String mid; //编号
		public String title; //标题
		public String create_time; // 时间

		public Notice() throws Exception {
		}

		public Notice(JSONObject json) throws Exception {
			this.build(json);
		}

		public void build(JSONObject json) throws Exception{
			mid= getJsonString(json,"id"); //编号
			title= getJsonString(json,"title"); //标题
			create_time= getJsonString(json,"create_time"); // 时间
		}

	}

	/*****  *****/
	public static class Technique extends MJsonData{
		private static final long serialVersionUID = 1L;

		public String mid; //编号
		public String title; //标题
		public String pictrue;
		public String create_time; //时间戳
		public String content; //内容

		public Technique() throws Exception {
		}

		public Technique(JSONObject json) throws Exception {
			this.build(json);
		}

		public void build(JSONObject json) throws Exception{
			mid= getJsonString(json,"id"); //编号
			title= getJsonString(json,"title"); //标题
			pictrue= getJsonString(json,"pictrue");
			create_time= getJsonString(json,"create_time"); //时间戳
			content= getJsonString(json,"content"); //内容
		}

	}


}
