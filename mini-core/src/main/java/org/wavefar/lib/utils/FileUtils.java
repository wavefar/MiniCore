package org.wavefar.lib.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * 文件操作的工具类
 * @author summer
 */
public class FileUtils {
	
	/**
	 * 存档到data/data/files
	 * 保存文件
	 * @param context 上下文对象
	 * @param file 文件名
	 * @param msg 保存的数据
	 * @return 成功与否
	 * @throws IOException
	 */
	public static boolean writeLocalFile(Context context, String file,
                                         byte[] msg) throws IOException {
		boolean res;
		FileOutputStream stream = context.openFileOutput(file,
				Context.MODE_PRIVATE);
		stream.write(msg);
		stream.flush();
		stream.close();
		res = true;
		return res;
	}
	
	/**
	 * 读取文件data/data/files
	 * @param context 上下文对象
	 * @param file 文件名
	 * @return 字节数组
	 * @throws IOException
	 */
	public static byte[] readLocalFile(Context context, String file)
			throws IOException {
		byte[] data = null;
		if (checkFileIsExists(context, file)) {
			FileInputStream stream = context.openFileInput(file);
			BufferedInputStream bis = new BufferedInputStream(stream);
			byte[] buffer = new byte[1024]; 
			int ch;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			while ((ch = bis.read(buffer)) != -1) {
				out.write(buffer,0,ch);
			}
			data = out.toByteArray();
			bis.close();
			out.close();
		}
		return data;
	}

	/**
	 * 从流内容读取文件
	 * @param is 输入流
	 * @return 字节数组
	 * @throws IOException io异常
	 */
	public static byte[] readStreamFile(InputStream is) throws IOException {
		byte[] data;
		byte[] buffer = new byte[1024];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		BufferedInputStream bis = new BufferedInputStream(is);
		int ch;
		while ((ch = bis.read(buffer)) != -1) {
			baos.write(buffer, 0, ch);
		}
		data = baos.toByteArray();
		baos.close();
		baos = null;
		bis.close();
		bis = null;

		return data;
	}
	
	/**
	 * 写文件数据  可以指定任意目录
	 * @param file 文件名称
	 * @param msg 数据
	 */
	public static void writeFile(String file, byte[] msg) {
		
		try {
			FileOutputStream stream = new FileOutputStream(file);
			stream.write(msg);
			stream.flush();
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 读取文件数据 可以指定任意目录
	 * @param file 文件名称
	 * @return 字节数组
	 */
	public static byte[] readFile(String file) {
		byte[] data = null;
		try {
			byte[] buffer = new byte[1024];
			FileInputStream stream = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(stream);
			int ch = 0;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			while ((ch = bis.read(buffer)) != -1) {
				out.write(buffer, 0, ch);
			}
			data = out.toByteArray();
			bis.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	/**
	 *
	 * 判断文件是否存在 data/data/files
	 * @param context 上下文对象
	 * @param fileName 文件名
	 * @return 成功与否
	 */
	public static boolean checkFileIsExists(Context context, String fileName) {
		java.io.File fileDir = context.getFilesDir();
		String sFileName = fileDir.getParent() + java.io.File.separator
				+ fileDir.getName() + java.io.File.separator + fileName;
		java.io.File file = new java.io.File(sFileName);
		return file.exists();
	}

	/**
	 * 取得文件最后修改日期
	 * @param context 上下文对象
	 * @param fileName 文件名
	 * @return 返回时间格式化  yyyy-MM-dd HH:mm:ss
	 */
	public static String getFileDatetime(Context context, String fileName) {
		String dt = "";
		java.io.File fileDir = context.getFilesDir();
		String sFileName = fileDir.getParent() + java.io.File.separator
				+ fileDir.getName() + java.io.File.separator + fileName;
		java.io.File file = new java.io.File(sFileName);
		if (file.isFile()) {
			dt = TimeUtil.getTime2String(file.lastModified(), "");
		}
		return dt;
	}

	/**
	 * 判断目录是否存在
	 * @param context 上下文对象
	 * @param dirName 文件夹
	 * @return 成功与否
	 */
	public static boolean checkDirectoryIsExists(Context context,
			String dirName) {
		java.io.File fileDir = context.getFilesDir();
		String sFileName = fileDir.getParent() + java.io.File.separator
				+ fileDir.getName() + java.io.File.separator + dirName;
		java.io.File file = new java.io.File(sFileName);
		return file.isDirectory();
	}

	/**
	 * 创建新的目录
	 * @param context 上下文
	 * @param dirName 目录名称
	 * @return 成功与否
	 */
	public static boolean createDirectory(Context context, String dirName) {
		java.io.File fileDir = context.getFilesDir();
		String sFileName = fileDir.getParent() + java.io.File.separator
				+ fileDir.getName() + java.io.File.separator + dirName;
		java.io.File file = new java.io.File(sFileName);
		return file.mkdir();
	}

	/**
	 * 通过本地图片路径返回bitmap
	 * @param context 上下文对象
	 * @param fileName 图片文件名
	 * @param defaultImage 未解析成功后显示的错误图片资源
	 * @return 返回bitmap
	 */
	public static Bitmap loadBitmapFromFile(Context context, String fileName,
                                            int defaultImage) {
		Bitmap bm;
		String bmpPath = context.getFilesDir() + java.io.File.separator
				+ fileName;
		bm = BitmapFactory.decodeFile(bmpPath);
		if (bm == null) {
			bm = BitmapFactory.decodeResource(context.getResources(),
					defaultImage);
		}
		return bm;
	}

	/**
	 * 检查sd卡是否存在如果存在
	 *
	 * @return 返回sd File卡路径
	 */
	public static File getSDRootPath() {
		File sdDir = null;
		// 判断sd卡是否存在
		boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
		if (sdCardExist) {
			// 获取跟目录
			sdDir = Environment.getExternalStorageDirectory();
		}
		return sdDir;
	}

	/**
	 * 清除文件
	 * @param file 文件
	 */
	public static void deleteFile(File file) {
		if (file.exists()) {
			if (file.isFile()) {
				file.delete();
			} else if (file.isDirectory()) {
				for (File files :file.listFiles()) {
					deleteFile(files);
				}
			}
			file.deleteOnExit();
		}
	}

	/**
	 * 删除小于给定时间的缓存数据
	 * @param dir 指定文件或目录
	 * @param time 时间戳
	 * @return 清除文件数量
	 */
	public static int clearCacheFolder(File dir, long time) {
	    int deletedFiles = 0;
	    if (dir!= null && dir.isDirectory()) {
	        try {
	            for (File child:dir.listFiles()) {
	                if (child.isDirectory()) {
	                    deletedFiles += clearCacheFolder(child, time);
	                }
	                if (child.lastModified() < time) {
	                    if (child.delete()) {
	                        deletedFiles++;
	                    }
	                }
	            }
	        } catch(Exception e) {
	            e.printStackTrace();
	        }
	    }
	    return deletedFiles;
	}

	/**
	 * 获取缓存文件大小（Data+sdcard目录的）
	 * 格式化字符串
	 * @param context 上下文对象
	 * @param cacheRoot 缓存目录
	 * @return 返回格式化大小
	 */
	public static String getCacheSize(Context context, String cacheRoot) {
		File fileCache = context.getCacheDir();
		//sdcard 目录缓存大小
		File temp = new File(FileUtils.getSDRootPath(), cacheRoot);
		long size = getFileSize(fileCache);
			size += getFileSize(temp);
		return formatFileSize(size);
	}

	/**
	 * 获取文件/文件夹大小
	 * @param f
	 * @return
	 */
	public static long getFileSize(File f){
		long size = 0;
		if (f.exists()) {
			if(f.isFile()) {
				size+=f.length();
			} else {//文件夹
				File[] fList = f.listFiles();
				for (File file : fList) {
					if (file.isDirectory()) {
						size += getFileSize(file);
					} else if(file.isFile()){
						size += file.length();
					}
				}
			}
		}
		return size;
	}

	/**
	 * 文件大小格式化显示
	 * @param fileSize 文件大小
	 * @return 格式化显示B、KB、MB、GB
	 */
	public static String formatFileSize(long fileSize) {
		DecimalFormat df = new DecimalFormat("0.00");
		String fileSizeString;
		int b  = 1024;
		int kb = 1048576;
		int mb = 1073741824;
		if (fileSize < b) {
			fileSizeString = df.format((double) fileSize) + "B";
		} else if (fileSize < kb) {
			fileSizeString = df.format((double) fileSize / 1024) + "KB";
		} else if (fileSize < mb) {
			fileSizeString = df.format((double) fileSize / 1048576) + "MB";
		} else {
			fileSizeString = df.format((double) fileSize / 1073741824) + "GB";
		}
		return fileSizeString;
	}

	/**
	 * 递归求取目录文件个数
	 * @param file 指定目录
	 * @return 返回文件个数
	 */
	public static long getFileNum(File file) {
		long size;
		File[] fList = file.listFiles();
		size = fList.length;
		for (File aFList : fList) {
			if (aFList.isDirectory()) {
				size = size + getFileNum(aFList);
				size--;
			}
		}
		return size;
	}

	/**
	 * 获取本地图片路径
	 * @param cacheDir 缓存根目录
	 * @param httpUrl 网络地址
	 * @return 本地缓存文件
	 */
	public static File getLocalFilePath(Context context, String cacheDir, String httpUrl){
		String bitmapName = getFileName(httpUrl);
		return new File(getCachePath(context,cacheDir),bitmapName);
	}

	/**
	 * 返回缓存文件目录对象
	 * @param context
	 * @param path 文件目录
	 * @return file sdcard 或cache 目录dir
	 */
	public static File getCachePath(Context context, String path) {
		File picFile;
		if (null == FileUtils.getSDRootPath()) {
			picFile = new File(context.getCacheDir() + path);
		} else {
			picFile = new File(FileUtils.getSDRootPath() + path);
		}
		if (!picFile.exists()) {
            picFile.mkdirs();
        }
		return picFile;
	}

	/**
	 * 根据文件绝对路径获取文件名
	 * @param filePath
	 * @return
	 */
	public static String getFileName(String filePath )
	{
		if ( filePath == null || "".equals( filePath ) ) {
			return "";
		}
		return filePath.substring( filePath.lastIndexOf( File.separator )+1 );
	}

	/**
	 * 写图片文件到存储卡中
	 * @param filePath 全路径名
	 * @param bitmap 图片数据
	 * @param quality 压缩比例
	 * @throws IOException
	 */
	public static void saveImageToStorage(String filePath, Bitmap bitmap, int quality) throws IOException
	{
		if(bitmap != null) {
			File bitmapFile=new File(filePath);
			FileOutputStream fos = new FileOutputStream(bitmapFile);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.JPEG, quality, stream);
			byte[] bytes = stream.toByteArray();
			fos.write(bytes);
			fos.close();
		}
	}

	/**
	 * 文件拷贝
	 * @param fromFile 待复制的文件
	 * @param toFile 复制的文件
	 */
	public static void copyfile(File fromFile, File toFile) {
		if (!fromFile.exists()) {
			return;
		}
		if (!fromFile.isFile()) {
			return;
		}
		if (!fromFile.canRead()) {
			return;
		}
		try {
			java.io.FileInputStream fosFrom = new java.io.FileInputStream(fromFile);
			java.io.FileOutputStream fosTo = new FileOutputStream(toFile);
			byte[] bt = new byte[1024];
			int c;
			while ((c = fosFrom.read(bt)) > 0) {
				// 将内容写到新文件当中
				fosTo.write(bt, 0, c);
			}
			fosFrom.close();
			fosTo.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取apk或其他文件大小
	 * @param apk 文件名
	 */
	public static String getApkSize(File apk)
	{
		return formatFileSize(getFileSize(apk));
	}
	
	 /**
     * 文件解压解压缩
     * @param zipFile：需要解压缩的文件如：apk等zip包
     * @param descDir：解压后的目标目录
	 * @throws IOException 异常
     */
    public static void unCompress(File zipFile, String descDir) throws IOException {
             ZipFile zf = new ZipFile(zipFile);
             for(Enumeration entries = zf.entries(); entries.hasMoreElements();){
                 ZipEntry entry = (ZipEntry) entries.nextElement();
                 String zipEntryName=entry.getName();
                 InputStream in = zf.getInputStream(entry);
                 File file = new File(descDir,zipEntryName);
                 if(!file.exists()){
					 //是文件
                 	if(!file.isDirectory()){
						//获取文件的父目录
                        File dirFile = file.getParentFile();
                        if(!dirFile.exists()) {
                            dirFile.mkdirs();
                        }
                 	} else {
                 		file.mkdirs();
                 	}
                 } 
                 //输出文件路径信息 
                 OutputStream out = new FileOutputStream(file);
                 byte[] buf1 = new byte[1024];
                 int len;
                 while ((len = in.read(buf1))>0) {
                     out.write(buf1,0,len);
                 }
                 in.close();
                 out.close();
             }
    }

	/**
	 * -----------------------------文件压缩begin----------------------------------------------
	 */
	private static final String BASE_DIR = "";

	/**
	 * 符号"/"用来作为目录标识判断符
 	 */
	private static final String PATH = File.separator;
	private static final int BUFFER = 1024;
    
    /**
	 * 默认压缩
	 * @param srcFile 源路径
	 * @throws Exception 异常
	 */
	public static void compress(File srcFile) throws Exception {
		String name = srcFile.getName();
		String basePath = srcFile.getParent();
		//默认为当前被压缩的文件名
		String destPath = String.format("%s%s%s.zip",basePath,File.separator,name);
		compress(srcFile, destPath);
	}

	/**
	 * 压缩
	 * 
	 * @param srcFile
	 *            源路径
	 * @param destFile
	 *            目标路径
	 * @throws Exception 异常
	 */
	public static void compress(File srcFile, File destFile) throws Exception {

		// 对输出文件做CRC32校验
		CheckedOutputStream cos = new CheckedOutputStream(new FileOutputStream(
				destFile), new CRC32());

		ZipOutputStream zos = new ZipOutputStream(cos);

		compress(srcFile, zos, BASE_DIR);

		zos.flush();
		zos.close();
	}

	/**
	 * 压缩文件
	 * 
	 * @param srcFile 待压缩文件
	 * @param destPath 目标路径
	 * @throws Exception 异常
	 */
	public static void compress(File srcFile, String destPath) throws Exception {
		compress(srcFile, new File(destPath));
	}

	/**
	 * 压缩
	 * 
	 * @param srcFile
	 *            源路径
	 * @param zos
	 *            ZipOutputStream
	 * @param basePath
	 *            压缩包内相对路径
	 * @throws Exception 异常
	 */
	private static void compress(File srcFile, ZipOutputStream zos,
                                 String basePath) throws Exception {
		if (srcFile.isDirectory()) {
			compressDir(srcFile, zos, basePath);
		} else {
			compressFile(srcFile, zos, basePath);
		}
	}

	/**
	 * 压缩
	 * 
	 * @param srcPath
	 * @throws Exception 异常
	 */
	public static void compress(String srcPath) throws Exception {
		File srcFile = new File(srcPath);

		compress(srcFile);
	}

	/**
	 * 文件压缩
	 * 
	 * @param srcPath
	 *            源文件路径
	 * @param destPath
	 *            目标文件路径
	 * @throws Exception 异常
	 * 
	 */
	public static void compress(String srcPath, String destPath)
			throws Exception {
		File srcFile = new File(srcPath);

		compress(srcFile, destPath);
	}

	/**
	 * 压缩目录
	 * 
	 * @param dir 目录
	 * @param zos
	 * @param basePath 根目录
	 * @throws Exception 异常
	 */
	private static void compressDir(File dir, ZipOutputStream zos,
                                    String basePath) throws Exception {

		File[] files = dir.listFiles();

		// 构建空目录
		if (files.length < 1) {
			ZipEntry entry = new ZipEntry(basePath + dir.getName() + PATH);

			zos.putNextEntry(entry);
			zos.closeEntry();
		}

		for (File file : files) {

			// 递归压缩
			compress(file, zos, basePath + dir.getName() + PATH);

		}
	}

	/**
	 * 文件压缩
	 *
	 * 压缩包内文件名定义
	 *
	 * <pre>
	 * 如果有多级目录，那么这里就需要给出包含目录的文件名
	 * 如果用WinRAR打开压缩包，中文名将显示为乱码
	 * </pre>
	 *
	 * @param file
	 *            待压缩文件
	 * @param zos
	 *            ZipOutputStream
	 * @param dir
	 *            压缩文件中的当前路径
	 * @throws Exception 异常
	 */
	private static void compressFile(File file, ZipOutputStream zos, String dir)
			throws Exception {

		ZipEntry entry = new ZipEntry(dir + file.getName());

		zos.putNextEntry(entry);

		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(
				file));

		int count;
		byte[] data = new byte[BUFFER];
		while ((count = bis.read(data, 0, BUFFER)) != -1) {
			zos.write(data, 0, count);
		}
		bis.close();

		zos.closeEntry();
	}
	//-----------------------------文件压缩end----------------------------------------------

	/**
	 * 读取assets 文件内容
	 * @param context 上下文对象
	 * @param fileName 文件名
	 * @return 字符串内容
	 */
	 public static String getFromAssets(Context context, String fileName) {
		return getFromAssets(context, fileName, false);
	 } 
	 
	/**
	 * 读取assets 文件内容
	 * @param context 上下文对象
	 * @param fileName 文件名
	 * @param isCode 是否是代码 如：js 【主要可以控制去除代码中的注释//】
	 * @return 字符串内容
	 */
	 public static String getFromAssets(Context context, String fileName, boolean isCode) {
		InputStream is = null;
		StringBuilder sb = new StringBuilder();
		try {
			String line;
			is = context.getResources().getAssets().open(fileName);
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			while ((line = reader.readLine()) != null) {
				if (line.length() > 0) {
					//屏蔽到代码中的注释
					if (isCode) {
						if (!line.trim().startsWith("//")) {
							sb.append(line);
						}
					} else {
						sb.append(line);
					} 
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	 } 
}