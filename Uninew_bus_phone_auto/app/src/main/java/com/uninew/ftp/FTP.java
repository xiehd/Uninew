package com.uninew.ftp;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import android.util.Log;

import com.uninew.json.JsonFileContants;

public class FTP {
	/**
	 * 服务器名.
	 */
	private static String hostName = "120.77.169.159";

	/**
	 * 端口号
	 */
	private static int serverPort = 21;

	/**
	 * 用户名.
	 */
	private static String userName = "AdminUser";

	/**
	 * 密码.
	 */
	private static String password = "Uninew123";

	/**
	 * FTP连接.
	 */
	private FTPClient ftpClient;

	public FTP(String hostNameid, String userName, String password) {
		this.hostName = hostNameid;
		this.serverPort = 21;
		this.userName = userName;
		this.password = password;

		// this.hostName = "120.77.169.159";
		// this.serverPort = 21;
		// this.userName = "AdminUser";
		// this.password = "Uninew123";

		this.ftpClient = new FTPClient();
	}

	public void setHost(String hostNameid, String userName, String password) {
		this.hostName = hostNameid;
		this.serverPort = 21;
		this.userName = userName;
		this.password = password;
	}

	// -------------------------------------------------------文件上传方法------------------------------------------------

	/**
	 * 上传单个文件.
	 * 
	 * @param singleFile
	 *            本地文件
	 * @param remotePath
	 *            FTP目录
	 * @param listener
	 *            监听器
	 * @throws IOException
	 */
	public void uploadSingleFile(File singleFile, String remotePath,
			UploadProgressListener listener) throws IOException {

		// 上传之前初始化
		this.uploadBeforeOperate(remotePath, listener);

		boolean flag;
		flag = uploadingSingle(singleFile, listener,remotePath);
		if (flag) {
			listener.onUploadProgress(JsonFileContants.FTP_UPLOAD_SUCCESS, 0,
					singleFile);
		} else {
			listener.onUploadProgress(JsonFileContants.FTP_UPLOAD_FAIL, 0,
					singleFile);
		}

		// 上传完成之后关闭连接
		this.uploadAfterOperate(listener);
	}

	private LinkedList<File> fileList = new LinkedList<>();

	private void readDirectory(File[] files) {
		fileList.clear();
		for (File file : files) {
			if (file.isDirectory()) {
				readDirectory(file.listFiles());
			} else {
				fileList.add(file);
			}
		}
	}

	/**
	 * 上传多个文件.
	 * 
	 * @param locatePath
	 *            本地文件夹
	 * @param remotePath
	 *            FTP目录
	 * @param listener
	 *            监听器
	 * @throws IOException
	 */
	public void uploadMultiFile(File locatePath, String remotePath,
			UploadProgressListener listener) throws IOException {

		// 上传之前初始化
		this.uploadBeforeOperate(remotePath, listener);
		boolean flag;
		// readDirectory(files);
		ftpClient.enterLocalPassiveMode();
		for (File file : locatePath.listFiles()) {
			Log.i("Test:", "上传文件:"+file.getName());
			if (file.isDirectory()) {
				uploadMultiFile(file, remotePath + "/" + file.getName(),
						listener);
			} else {
				flag = uploadingSingle(file, listener,remotePath);
				if (flag) {
					Log.i("Test:", "上传成功:"+file.getName());
					listener.onUploadProgress(JsonFileContants.FTP_UPLOAD_SUCCESS, 0, file);
				} else {
					listener.onUploadProgress(JsonFileContants.FTP_UPLOAD_FAIL,
							0, file);
				}
			}
		}

		// if (fileList != null && fileList.size() >= 0) {
		// for (File singleFile : fileList) {
		// flag = uploadingSingle(singleFile, listener);
		// if (flag) {
		// listener.onUploadProgress(
		// JsonFileContants.FTP_UPLOAD_SUCCESS, 0, singleFile);
		// } else {
		// listener.onUploadProgress(JsonFileContants.FTP_UPLOAD_FAIL,
		// 0, singleFile);
		// }
		// }
		// }else {
		// listener.onUploadProgress(JsonFileContants.FTP_UPLOAD_FAIL_NO,
		// 0, null);
		// }

		// 上传完成之后关闭连接
		//this.uploadAfterOperate(listener);
	}

	/**
	 * 上传单个文件(文件夹)
	 * 
	 * @param localFile
	 *            本地文件
	 * @return true上传成功, false上传失败
	 * @throws IOException
	 */
	private boolean uploadingSingle(File localFile,
			UploadProgressListener listener,String remotePath) throws IOException {
		//if (ftpClient != null && !ftpClient.isConnected()) {
			// 打开FTP服务
//			try {
//				this.openConnect();
//				listener.onUploadProgress(JsonFileContants.FTP_CONNECT_SUCCESSS, 0,
//						null);
//			} catch (IOException e1) {
//				e1.printStackTrace();
//				listener.onUploadProgress(JsonFileContants.FTP_CONNECT_FAIL, 0,
//						null);
//				return false;
//			}
		//}
		boolean flag = true;
		// 不带进度的方式
		// // 创建输入流
		// InputStream inputStream = new FileInputStream(localFile);
		// // 上传单个文件
		// flag = ftpClient.storeFile(localFile.getName(), inputStream);
		// // 关闭文件流
		// inputStream.close();

		// 带有进度的方式
		// 改变FTP目录
		ftpClient.changeWorkingDirectory(remotePath);
		BufferedInputStream buffIn = new BufferedInputStream(
				new FileInputStream(localFile),1024);
		ProgressInputStream progressInput = new ProgressInputStream(buffIn,
				listener, localFile);
		ftpClient.enterLocalPassiveMode();
		flag = ftpClient.storeFile(localFile.getName(), progressInput);
		buffIn.close();

		return flag;
	}

	/**
	 * 上传文件之前初始化相关参数
	 * 
	 * @param remotePath
	 *            FTP目录
	 * @param listener
	 *            监听器
	 * @throws IOException
	 */
	private void uploadBeforeOperate(String remotePath,
			UploadProgressListener listener) throws IOException {

		// 打开FTP服务
		try {
			this.openConnect();
			listener.onUploadProgress(JsonFileContants.FTP_CONNECT_SUCCESSS, 0,
					null);
		} catch (IOException e1) {
			e1.printStackTrace();
			listener.onUploadProgress(JsonFileContants.FTP_CONNECT_FAIL, 0,
					null);
			return;
		}

		// 设置模式
		ftpClient.setFileTransferMode(org.apache.commons.net.ftp.FTP.STREAM_TRANSFER_MODE);
		// FTP下创建文件夹
		ftpClient.makeDirectory(remotePath);


	}

	/**
	 * 上传完成之后关闭连接
	 * 
	 * @param listener
	 * @throws IOException
	 */
	public void uploadAfterOperate(UploadProgressListener listener)
			throws IOException {
		this.closeConnect();
		listener.onUploadProgress(JsonFileContants.FTP_DISCONNECT_SUCCESS, 0,
				null);
	}

	// -------------------------------------------------------文件下载方法------------------------------------------------

	/**
	 * 下载文件，可实现断点下载.
	 * 
	 * @param serverPath
	 *            Ftp目录
	 * @param ftpfile
	 *            要下载的当前文件，为null表示下载Ftp目录所有文件
	 * @param localPath
	 *            本地存放目录
	 * @param fileName
	 *            下载之后的文件名称,为null表示与服务器文件名相同
	 * @param listener
	 *            监听器
	 * @throws IOException
	 */
	public void downloadFile(String serverPath, String ftpfile,
			String localPath, String fileName, DownLoadProgressListener listener)
			throws Exception {

		if (ftpClient != null && !ftpClient.isConnected()) {
			// 打开FTP服务
			try {
				this.openConnect();
				listener.onDownLoadProgress(
						JsonFileContants.FTP_CONNECT_SUCCESSS, 0, null);
			} catch (IOException e1) {
				e1.printStackTrace();
				listener.onDownLoadProgress(JsonFileContants.FTP_CONNECT_FAIL,
						0, null);
				return;
			}
		}

		// 更改FTP当前目录
		ftpClient.changeWorkingDirectory(serverPath);
		// 先判断服务器文件是否存在
		FTPFile[] files = ftpClient.listFiles(serverPath);
		if (files.length == 0) {
			listener.onDownLoadProgress(JsonFileContants.FTP_FILE_NOTEXISTS, 0,
					null);
			return;
		}
		// 创建本地文件夹
		File mkFile = new File(localPath);
		if (!mkFile.exists()) {
			mkFile.mkdirs();
		}
		if (ftpfile == null) {
			for (FTPFile ftpFile : files) {
				// 创建文件
				File file = new File(serverPath + "/" + ftpFile.getName());
				String newpath = serverPath + "/" + ftpFile.getName();
				Log.i("下载服务器文件：", newpath);
				if (ftpFile.isDirectory()) {
					// 下载多个文件
					downloadFile(newpath, null,
							localPath + "/" + ftpFile.getName(), null, listener);
				} else {
					String localPath2 = localPath;
					if (fileName == null) {
						localPath2 += "/" + ftpFile.getName();
					} else {
						localPath2 += "/" + fileName;
					}
					// 下载单个文件
					downloadSingleFile(newpath, ftpFile, localPath2, listener);
				}
			}

		} else {

		}
		// 下载完成之后关闭连接
		this.closeConnect();
		listener.onDownLoadProgress(JsonFileContants.FTP_DISCONNECT_SUCCESS, 0,
				null);

		return;
	}

	// 下载多个文件

	/**
	 * 下载单个文件
	 * 
	 * @param serverPath
	 * @param ftpfile
	 * @param localPath
	 * @param listener
	 * @throws Exception
	 */
	private void downloadSingleFile(String serverPath, FTPFile ftpfile,
			String localPath, DownLoadProgressListener listener)
			throws Exception {
			// 打开FTP服务
			try {
				this.openConnect();
				listener.onDownLoadProgress(
						JsonFileContants.FTP_CONNECT_SUCCESSS, 0, null);
			} catch (IOException e1) {
				e1.printStackTrace();
				listener.onDownLoadProgress(JsonFileContants.FTP_CONNECT_FAIL,
						0, null);
				return;
			}
		// 接着判断下载的文件是否能断点下载
		long serverSize = ftpfile.getSize(); // 获取远程文件的长度
		File localFile = new File(localPath);
		long localSize = 0;
		if (localFile.exists()) {
			localSize = localFile.length(); // 如果本地文件存在，获取本地文件的长度
			if (localSize >= serverSize) {
				File file2 = new File(localPath);
				file2.delete();
				localSize = 0;
				localFile.createNewFile();
			}
		} else {
			localFile.createNewFile();
		}
		// 进度
		long step = serverSize / 100;
		int process = 0;
		long currentSize = 0;
		// 开始准备下载文件
		OutputStream out = new FileOutputStream(localFile, true);
		ftpClient.setRestartOffset(localSize);
		InputStream input = ftpClient.retrieveFileStream(serverPath);
		byte[] b = new byte[1024];
		int length = 0;
		while ((length = input.read(b)) != -1) {
			out.write(b, 0, length);
			currentSize = currentSize + length;
			if (step > 0) {
				if (currentSize / step != process) {
					process = (int) (currentSize / step);
					if (process % 1 == 0) { // 每隔%5的进度返回一次
						listener.onDownLoadProgress(
								JsonFileContants.FTP_DOWN_LOADING, process,
								null);
					}
				}
			}
		}
		out.flush();
		out.close();
		input.close();

		// 此方法是来确保流处理完毕，如果没有此方法，可能会造成现程序死掉
		if (ftpClient.completePendingCommand()) {
			listener.onDownLoadProgress(JsonFileContants.FTP_DOWN_SUCCESS, 0,
					new File(localPath));
		} else {
			listener.onDownLoadProgress(JsonFileContants.FTP_DOWN_FAIL, 0, null);
		}

	}

	// -------------------------------------------------------文件删除方法------------------------------------------------

	/**
	 * 删除Ftp下的文件.
	 * 
	 * @param serverPath
	 *            Ftp目录及文件路径
	 * @param listener
	 *            监听器
	 * @throws IOException
	 */
	public void deleteSingleFile(String serverPath,
			DeleteFileProgressListener listener) throws Exception {

		// 打开FTP服务
		try {
			this.openConnect();
			listener.onDeleteProgress(JsonFileContants.FTP_CONNECT_SUCCESSS);
		} catch (IOException e1) {
			e1.printStackTrace();
			listener.onDeleteProgress(JsonFileContants.FTP_CONNECT_FAIL);
			return;
		}

		// 先判断服务器文件是否存在
		FTPFile[] files = ftpClient.listFiles(serverPath);
		if (files.length == 0) {
			listener.onDeleteProgress(JsonFileContants.FTP_FILE_NOTEXISTS);
			return;
		}

		// 进行删除操作
		boolean flag = true;
		flag = ftpClient.deleteFile(serverPath);
		if (flag) {
			listener.onDeleteProgress(JsonFileContants.FTP_DELETEFILE_SUCCESS);
		} else {
			listener.onDeleteProgress(JsonFileContants.FTP_DELETEFILE_FAIL);
		}

		// 删除完成之后关闭连接
		this.closeConnect();
		listener.onDeleteProgress(JsonFileContants.FTP_DISCONNECT_SUCCESS);

		return;
	}

	// -------------------------------------------------------打开关闭连接------------------------------------------------

	/**
	 * 打开FTP服务.
	 * 
	 * @throws IOException
	 */
	public void openConnect() throws IOException {
		// 中文转码
		// ftpClient.setControlEncoding("UTF-8");
		ftpClient.setControlEncoding("GBK");
		int reply; // 服务器响应值
		// 连接至服务器
		ftpClient.connect(hostName, serverPort);
		// 获取响应值
		reply = ftpClient.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			// 断开连接
			ftpClient.disconnect();
			throw new IOException("connect fail: " + reply);
		}
		// 登录到服务器
		ftpClient.login(userName, password);
		// 获取响应值
		reply = ftpClient.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			// 断开连接
			ftpClient.disconnect();
			throw new IOException("connect fail: " + reply);
		} else {
			// 获取登录信息
			FTPClientConfig config = new FTPClientConfig(ftpClient
					.getSystemType().split(" ")[0]);
			config.setServerLanguageCode("zh");
			ftpClient.configure(config);
			// 使用被动模式设为默认
			ftpClient.enterLocalPassiveMode();
			// ftpClient.setConnectTimeout(60000);
			ftpClient.setSoTimeout(30*1000);
			// 二进制文件支持
			ftpClient.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
		}
	}

	/**
	 * 关闭FTP服务.
	 * 
	 * @throws IOException
	 */
	public void closeConnect() throws IOException {
		if (ftpClient != null) {
			// 退出FTP
			ftpClient.logout();
			// 断开连接
			ftpClient.disconnect();
		}
	}

	// ---------------------------------------------------上传、下载、删除监听---------------------------------------------

	/*
	 * 上传进度监听
	 */
	public interface UploadProgressListener {
		public void onUploadProgress(String currentStep, long uploadSize,
                                     File file);
	}

	/*
	 * 下载进度监听
	 */
	public interface DownLoadProgressListener {
		public void onDownLoadProgress(String currentStep, long downProcess,
                                       File file);
	}

	/*
	 * 文件删除监听
	 */
	public interface DeleteFileProgressListener {
		public void onDeleteProgress(String currentStep);
	}

}
