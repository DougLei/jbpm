package com.douglei.bpm.core.process;

import java.io.File;

import com.douglei.bpm.core.process.executer.Process;
import com.douglei.tools.utils.IOUtil;
import com.douglei.tools.utils.serialize.JdkSerializeProcessor;

/**
 * 流程实例序列化处理器
 * @author DougLei
 */
class ProcessSerializationHandler {
	private static final String VERSION = "v202009101"; // 序列化版本; 每次修改了流程相关的类的serialVersionUID值时, 都必须更新该值后, 再发布新版本; 版本值使用 "v+年月日+序列值" 来定义, 其中序列值每日从1开始, 同一日时递增
	private String folderPath;
	
	public ProcessSerializationHandler(String processEngineId) {
		folderPath = System.getProperty("user.home") + File.separatorChar + ".bpm"  + File.separatorChar + processEngineId + File.separatorChar + VERSION + File.separatorChar;
		
		File folder = new File(folderPath);
		if(!folder.exists()) 
			folder.mkdirs();
		
		File parentFolder = folder.getParentFile();
		String[] list = parentFolder.list();
		if(list != null && list.length > 1) {
			for (String f : list) {
				if(!f.equals(VERSION)) {
					IOUtil.delete(new File(parentFolder.getAbsolutePath() + File.separatorChar + f));
				}
			}
		}
	}

	// 获取对应的bpm序列化文件夹路径, 包括文件名
	private String getBpmFilePath(String code, String version, int subversion) {
		return folderPath + code+"."+version+"."+subversion;
	}
	
	/**
	 * 创建序列化文件
	 * @param process
	 */
	public void createFile(Process process) {
		JdkSerializeProcessor.serialize2File(process, getBpmFilePath(process.getCode(), process.getVersion(), process.getSubversion()));
	}

	/**
	 * 删除序列化文件
	 * @param code
	 * @param version
	 * @param subversion
	 */
	public void deleteFile(String code, String version, int subversion) {
		File file = new File(getBpmFilePath(code, version, subversion));
		if(file.exists()) 
			file.delete();
	}
	
	/**
	 * 反序列化获取Process实例
	 * @param code
	 * @param version
	 * @param subversion
	 * @return
	 */
	public Process deserialize(String code, String version, int subversion) {
		return JdkSerializeProcessor.deserializeFromFile(Process.class, getBpmFilePath(code, version, subversion));
	}

	/**
	 * 删除序列化文件夹
	 */
	public void deleteFolder() {
		IOUtil.delete(new File(folderPath).getParentFile());
	}
}
