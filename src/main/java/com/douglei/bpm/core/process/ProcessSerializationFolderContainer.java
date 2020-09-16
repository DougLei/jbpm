package com.douglei.bpm.core.process;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.douglei.tools.utils.IOUtil;

/**
 * 
 * @author DougLei
 */
public class ProcessSerializationFolderContainer {
	private static final String VERSION = "v202009151"; // 序列化版本; 每次修改了流程相关的类的serialVersionUID值时, 都必须更新该值后, 再发布新版本; 版本值使用 "v+年月日+序列值" 来定义, 其中序列值每日从1开始, 同一日时递增
	private static final String FOLDER = System.getProperty("user.home") + File.separatorChar + ".bpm" + File.separatorChar;
	private static final Map<String, String> FOLDER_MAP = new HashMap<String, String>(8); // bpm序列化文件的根路径map, key是ProcessEngine id, value是对应的路径
	
	/**
	 * 创建文件夹
	 * @param processEngineId
	 */
	public static synchronized void createFolder(String processEngineId) {
		if(!FOLDER_MAP.containsKey(processEngineId)) {
			String folderPath = FOLDER + processEngineId + File.separatorChar + VERSION + File.separatorChar;
			FOLDER_MAP.put(processEngineId, folderPath);
			
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
	}

	/**
	 * 删除文件夹
	 * @param processEngineId
	 */
	public static synchronized void deleteFolder(String processEngineId) {
		String folder = FOLDER_MAP.remove(processEngineId);
		if(folder != null)
			IOUtil.delete(new File(folder).getParentFile());
	}
	
	/**
	 * 获取文件夹
	 * @param processEngineId
	 * @return
	 */
	static String getFolder(String processEngineId) {
		return FOLDER_MAP.get(processEngineId);
	}
}
