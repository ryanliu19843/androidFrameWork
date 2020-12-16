package com.mdx.framework.commons.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

import com.mdx.framework.commons.verify.DES;
import com.squareup.wire.Message;
import com.squareup.wire.Wire;

public class ProtoBuffer {

	public byte[] protobuf(Object obj) throws IOException {
		if (obj instanceof Message) {
			Message builder = (Message) obj;
			return builder.toByteArray();
		} else if (obj instanceof Message.Builder) {
			Message.Builder<?> builder = (Message.Builder<?>) obj;
			Message bid = builder.build();
			return bid.toByteArray();
		} else {
			/*try {
				if (obj instanceof com.google.protobuf.GeneratedMessage.Builder) {
					com.google.protobuf.GeneratedMessage.Builder<?> builder = (com.google.protobuf.GeneratedMessage.Builder<?>) obj;
					return builder.build().toByteArray();
				}
			} catch (Throwable e) {
			}*/
			if (obj instanceof Serializable) {
				ByteArrayOutputStream o = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(o);
				oos.writeObject(obj);
				oos.flush();
				oos.close();
				o.close();
				return o.toByteArray();
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public Object unprotobuf(InputStream in, Object build) throws IOException {
		if (build instanceof Message) {
			Message builder = (Message) build;
			Object msg = new Wire().parseFrom(in, builder.getClass());
			in.close();
			return msg;
		} else if (build instanceof Message.Builder) {
			Message.Builder<?> builder = (Message.Builder<?>) build;
			Object msg = new Wire().parseFrom(in, (Class<Message>) builder.getClass().getEnclosingClass());
			in.close();
			return msg;
		} else if (build instanceof Class) {
			Object msg = new Wire().parseFrom(in, (Class<Message>) build);
			in.close();
			return msg;
		} else {
			/*try {
				if (build instanceof com.google.protobuf.GeneratedMessage.Builder) {
					com.google.protobuf.GeneratedMessage.Builder<?> builder = (com.google.protobuf.GeneratedMessage.Builder<?>) build;
					builder.mergeFrom(in);
					in.close();
					return builder;
				}
			} catch (Throwable e) {
			}*/
			if (build instanceof Serializable) {
				ObjectInputStream ois = new ObjectInputStream(in);
				try {
					return ois.readObject();
				} catch (ClassNotFoundException e) {
				}
			}
		}

		return null;
	}

	public static void protobufZip(Object obj, OutputStream out) throws IOException {
		ProtoBuffer pbf = new ProtoBuffer();
		Zip zip = new Zip();
		ByteArrayOutputStream outstream = new ByteArrayOutputStream();
		out.write(pbf.protobuf(obj));
		InputStream in = new ByteArrayInputStream(outstream.toByteArray());
		zip.zip(in, out);
	}

	public static Object unprotobufZip(String path, Object build) throws IOException {
		return unprotobufZip(new FileInputStream(new File(path)), build);
	}

	public static Object unprotobufZip(File file, Object build) throws IOException {
		return unprotobufZip(new FileInputStream(file), build);
	}

	public static Object unprotobufZip(InputStream in, Object build) throws IOException {
		ProtoBuffer pbf = new ProtoBuffer();
		Zip zip = new Zip();
		ByteArrayOutputStream outstream = new ByteArrayOutputStream();
		zip.unzip(in, outstream);
		return pbf.unprotobuf(new ByteArrayInputStream(outstream.toByteArray()), build);
	}

	public static void protobufSeralizeDes(Object obj, OutputStream out) throws Exception {
		ProtoBuffer pbf = new ProtoBuffer();
		byte[] byts = pbf.protobuf(obj);
		DES des = new DES();
		byte[] bytes = des.desEncrypt(byts);
		out.write(bytes);
		out.close();
	}

	public static void protobufSeralize(Object obj, OutputStream out) throws Exception {
		ProtoBuffer pbf = new ProtoBuffer();
		byte[] byts = pbf.protobuf(obj);
		out.write(byts);
		out.close();
	}

	public static Object unprotobufSeralize(InputStream in, Object build) throws Exception {
		ProtoBuffer pbf = new ProtoBuffer();
		return pbf.unprotobuf(in, build);
	}

	public static Object unprotobufSeralizeDes(InputStream in, Object build) throws Exception {
		ProtoBuffer pbf = new ProtoBuffer();
		ByteArrayOutputStream fos = new ByteArrayOutputStream();
		byte[] bt = new byte[1024];
		int ind = 0;
		while ((ind = in.read(bt)) >= 0) {
			fos.write(bt, 0, ind);
		}
		fos.close();
		DES des = new DES();
		byte[] bytes = des.desDecrypt(fos.toByteArray());
		ByteArrayInputStream bytein = new ByteArrayInputStream(bytes);
		return pbf.unprotobuf(bytein, build);
	}
}
