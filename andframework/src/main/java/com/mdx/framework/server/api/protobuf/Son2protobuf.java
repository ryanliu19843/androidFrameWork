package com.mdx.framework.server.api.protobuf;

import com.mdx.framework.commons.data.Method;
import com.mdx.framework.commons.verify.Md5;
import com.mdx.framework.log.MLog;
import com.mdx.framework.server.api.Son;
import com.mdx.framework.server.api.UpdateOne;
import com.mdx.framework.server.api.base.Msg_Retn;

import java.io.ByteArrayInputStream;
import java.util.HashMap;

import javax.crypto.IllegalBlockSizeException;

public class Son2protobuf extends Son {
	private static final long serialVersionUID = 1L;

	// String metod, String buildMd5, Object build, int type,int errorType, HashMap<String, String> map
	public Son2protobuf(byte[] ins, UpdateOne updateone) {

		String metod = updateone.getId();
		Object build = updateone.mbuild;
		int type = updateone.getType();
		this.Md5str=updateone.getMd5str();
		try {
			this.sonMd5= Md5.md5(ins);
		} catch (Exception e) {
			MLog.D(e);
		}
		int errorType = updateone.getErrorType();
		HashMap<String, String> map = updateone.getMap();

		this.mMethod = metod;
		this.mErrMethod = mMethod;
		this.mErrorType = errorType;
		this.type = type;
		this.mParams = map;

		Msg_Retn retn = null;
		try {
			retn = (Msg_Retn) Method.unprotobufSeralizeDes(new ByteArrayInputStream(ins), Msg_Retn.class);
			this.mError = retn.errorCode;
			this.msg = retn.errorMsg;
			this.mServerMethod = retn.returnMethod;
			if (build == null) {
				mBuild = retn;
			} else if (retn.retnMessage != null && retn.retnMessage.size() > 0) {
				byte[] inp = retn.retnMessage.toByteArray();
				build = Method.unprotobufSeralize(new ByteArrayInputStream(inp), build);
				mBuild = build;
			} else {
				mBuild = build;
			}
			for (int i = 0; i < retn.retns.size(); i++) {
				Msg_Retn mr = retn.retns.get(i);
				UpdateOne uo = updateone.updateones.get(i);
				Son son = new Son();
				son.mMethod = uo.getId();
				son.mErrMethod = son.mMethod;
				son.mErrorType = uo.getErrorType();
				build = uo.mbuild;
				son.type = uo.getType();
				son.mParams = uo.getMap();

				son.mError = mr.errorCode;
				son.msg = mr.errorMsg;
				son.mServerMethod = mr.returnMethod;
				try {
					if (build == null) {
						son.mBuild = mr;
					} else if (mr.retnMessage != null && mr.retnMessage.size() > 0) {
						byte[] inp = mr.retnMessage.toByteArray();
						try {
							this.sonMd5= Md5.md5(inp);
						} catch (Exception e) {
							MLog.D(e);
						}

						son.mBuild = Method.unprotobufSeralize(new ByteArrayInputStream(inp), uo.mbuild);
					} else {
						son.mBuild = build;
					}

					this.Md5str=uo.getMd5str();
					try {
						this.sonMd5= Md5.md5(ins);
					} catch (Exception e) {
						MLog.D(e);
					}

				} catch (Exception e) {
					if (e instanceof IllegalBlockSizeException) {
						son.mError = 97;
						son.msg = e.toString();
						son.mBuild = build;
					} else {
						son.mError = 98;
						son.msg = e.toString();
						son.mBuild = build;
					}
				}
				this.sons.add(son);
			}
		} catch (Exception e) {
			if (e instanceof IllegalBlockSizeException) {
				this.mError = 97;
				this.msg = e.toString();
				this.mBuild = build;
			} else {
				this.mError = 98;
				this.msg = e.toString();
				this.mBuild = build;
			}
		}

	}
}
