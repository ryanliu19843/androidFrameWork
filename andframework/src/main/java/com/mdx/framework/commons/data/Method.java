package com.mdx.framework.commons.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import com.mdx.framework.commons.verify.DES;

public class Method {
    public static void serializeZip(Object obj, OutputStream out) throws IOException {
        Serialize slz = new Serialize();
        Zip zip = new Zip();
        ByteArrayOutputStream outstream = new ByteArrayOutputStream();
        slz.serialize(obj, outstream);
        InputStream in = new ByteArrayInputStream(outstream.toByteArray());
        zip.zip(in, out);
    }
    
    public static Object unserializeZip(String path) throws IOException, ClassNotFoundException {
        return unserializeZip(new File(path));
    }
    
    public static Object unserializeZip(File file) throws IOException, ClassNotFoundException {
        Serialize slz = new Serialize();
        Zip zip = new Zip();
        ByteArrayOutputStream outstream = new ByteArrayOutputStream();
        zip.unzip(file, outstream);
        return slz.unSerialize(new ByteArrayInputStream(outstream.toByteArray()));
    }
    
    public static void protobufZip(Object obj, OutputStream out) throws IOException {
        ProtoBuffer pbf = new ProtoBuffer();
        Zip zip = new Zip();
        ByteArrayOutputStream outstream = new ByteArrayOutputStream();
        outstream.write(pbf.protobuf(obj));
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
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        o.write(pbf.protobuf(obj));
        DES des = new DES();
        byte[] bytes = des.desEncrypt(o.toByteArray());
        out.write(bytes);
        out.close();
    }
    
    public static void protobufSeralize(Object obj, OutputStream out) throws Exception {
        ProtoBuffer pbf = new ProtoBuffer();
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        o.write(pbf.protobuf(obj));
        out.write(o.toByteArray());
        out.close();
    }
    
    public static byte[] protobufSeralize(Object obj) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        protobufSeralize(obj, out);
        return out.toByteArray();
    }
    
    public static byte[] protobufSeralizeDes(Object obj) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        protobufSeralizeDes(obj, out);
        return out.toByteArray();
    }
    
    public static Object unprotobufSeralize(InputStream in, Object build) throws Exception {
        ProtoBuffer pbf = new ProtoBuffer();
        return pbf.unprotobuf(in, build);
    }
    
    public static Object unprotobufSeralize(byte[] bytes, Object build) throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        return unprotobufSeralize(in, build);
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
