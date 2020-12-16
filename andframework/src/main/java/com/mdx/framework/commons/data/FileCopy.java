package com.mdx.framework.commons.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class FileCopy {

	public void copyFile(String from,String to) throws IOException{
		this.copyFile(new File(from), new File(to));
	}
	
	public void copy(String from,String to) throws IOException{
		this.copy(new File(from), new File(to));
	}
	
	public void copyFile(File from,File to) throws IOException{
		InputStream in=new FileInputStream(from);
		OutputStream out=new FileOutputStream(to);
		try{
		byte[] bt=new byte[1024*5]; 
		int ind = 0;
		while ((ind = in.read(bt)) >= 0) {
			out.write(bt, 0, ind);
			out.flush();
		}
		}catch(IOException e){
			throw e;
		}finally{
			in.close();
			out.close();
		}
	}
	
	public void copy(File from,File to) throws IOException{
		if(from.exists()){
			if(from.isDirectory()){
				if(!to.exists()){
					to.mkdirs();
				}else if(!to.isDirectory()){
					to.mkdirs();
				}
				File files[]=from.listFiles();
				for(File fl:files){
					if(fl.isDirectory()){
						copy(fl,new File(to.getPath()+"/"+fl.getName()));
					}else{
						copyFile(fl, new File(to.getPath()+"/"+fl.getName()));
					}
				}
			}else{
				copyFile(from,to);
			}
		}
	}
}
