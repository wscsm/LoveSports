package mobi.imuse.lovesports.QiNiu;


import mobi.imuse.lovesports.Constants;

public class Config {

	public static String ACCESS_KEY	= Constants.QINIU_ACCESS_KEY ;
	public static String SECRET_KEY	= Constants.QINIU_SECRET_KEY ;

	public static String REDIRECT_URI  = "http://family.u.qiniudn.com";
	public static String AUTHORIZATION_ENDPOINT = "<AuthURL>";
	public static String TOKEN_ENDPOINT = "https://acc.qiniu.com/oauth2/token";

	public static String IO_HOST = "http://iovip.qiniu.com";
	public static String FS_HOST = "https://fs.qiniu.com";
	public static String RS_HOST = "http://rs.qiniu.com";
	public static String UP_HOST = "http://up.qiniu.com";

	public static int BLOCK_SIZE = 4 * 1024 * 1024;	 // 4M
	public static int PUT_CHUNK_SIZE = 20 * 1024;	 // 256K
	public static int PUT_RETRY_TIMES = 3;
	public static int PUT_TIMEOUT = 300000; 		 // 300s = 5m
}