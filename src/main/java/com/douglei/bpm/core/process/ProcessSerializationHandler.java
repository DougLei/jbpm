package com.douglei.bpm.core.process;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.douglei.bpm.core.process.executer.Process;
import com.douglei.tools.utils.IOUtil;
import com.douglei.tools.utils.serialize.JdkSerializeProcessor;

/**
 * 流程实例序列化处理器
 * @author DougLei
 */
class ProcessSerializationHandler {
	private static final String VERSION = "v202009101"; // 序列化版本; 每次修改了流程相关的类的serialVersionUID值时, 都必须更新该值后, 再发布新版本; 版本值使用 "v+年月日+序列值" 来定义, 其中序列值每日从1开始, 同一日时递增
	private static final String FOLDER_NAME = ".bpm"; // 文件夹名称
	private static final String FILE_SUFFIX = FOLDER_NAME; // 序列化文件的后缀, 和文件夹名称一致
	private static final Map<String, String> FOLDER_PATH_MAP = new HashMap<String, String>(8); // bpm序列化文件的根路径map, key是processEngine id, value是对应的路径
	
	private String processEngineId;
	public ProcessSerializationHandler(String processEngineId) {
		this.processEngineId = processEngineId;
	}

	// 获取对应的bpm序列化文件夹路径, 包括文件名
	private String getBpmFilePath(String code, String version, int subversion) {
		String folderPath = FOLDER_PATH_MAP.get(processEngineId);
		if(folderPath == null) {
			folderPath = System.getProperty("user.home") + File.separatorChar + FOLDER_NAME + File.separatorChar + processEngineId + File.separatorChar + VERSION + File.separatorChar;
			File folder = new File(folderPath);
			if(!folder.exists()) 
				folder.mkdirs();
			deletePreviousVersionOfFolder(folder.getParentFile());
			FOLDER_PATH_MAP.put(processEngineId, folderPath);
		}
		return folderPath + code+"."+version+"."+subversion + FILE_SUFFIX;
	}
	
	/**
	 * 删除之前版本的文件夹; 在更新序列化版本后, 将之前版本创建的文件夹删除掉
	 * @param parentFolder
	 */
	private void deletePreviousVersionOfFolder(File parentFolder) {
		String[] folders = parentFolder.list();
		if(folders.length > 1) {
			for (String folder : folders) {
				if(!folder.equals(VERSION)) {
					IOUtil.delete(new File(parentFolder.getAbsolutePath() + File.separatorChar + folder));
				}
			}
		}
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
}
