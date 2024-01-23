package net.montoyo.mcef.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.montoyo.mcef.remote.Mirror;

public class Util {
	
	private static final DummyProgressListener DPH = new DummyProgressListener();
	private static final String HEX = "0123456789abcdef";
	
	/**
	 * Clamps d between min and max.
	 * 
	 * @param d The value to clamp.
	 * @param min The minimum.
	 * @param max The maximum.
	 * @return The clamped value.
	 */
	public static double clamp(double d, double min, double max) {
		if(d < min)
			return min;
		else if(d > max)
			return max;
		else
			return d;
	}
	
	/**
	 * Extracts a ZIP archive into a folder.
	 * 
	 * @param zip The ZIP archive file to extract.
	 * @param out The output directory for the ZIP content.
	 * @return true if the extraction was successful.
	 */
	public static boolean extract(File zip, File out) {
		ZipInputStream zis;
		
		try {
			zis = new ZipInputStream(new FileInputStream(zip));
		} catch(FileNotFoundException e) {
			Log.error("Couldn't extract %s: File not found.", zip.getName());
			e.printStackTrace();
			return false;
		}
		
		try {
			ZipEntry ze;
			while((ze = zis.getNextEntry()) != null) {
				if(ze.isDirectory())
					continue;
				
				File dst = new File(out, ze.getName());
				delete(dst);
				mkdirs(dst);
				
				FileOutputStream fos = new FileOutputStream(dst);
				byte[] data = new byte[65536];
				int read;
				
				while((read = zis.read(data)) > 0)
					fos.write(data, 0, read);
				
				close(fos);
			}
			
			return true;
		} catch(FileNotFoundException e) {
			Log.error("Couldn't extract a file from %s. Maybe you're missing some permissions?", zip.getName());
			e.printStackTrace();
			return false;
		} catch(IOException e) {
			Log.error("IOException while extracting %s.", zip.getName());
			e.printStackTrace();
			return false;	
		} finally {
			close(zis);
		}
	}
	
	/**
	 * Returns the SHA-1 checksum of a file.
	 * 
	 * @param fle The file to be hashed.
	 * @return The hash of the file or null if an error occurred.
	 */
	public static String hash(File fle) {
		FileInputStream fis;
		
		try {
			fis = new FileInputStream(fle);
		} catch(FileNotFoundException e) {
			Log.error("Couldn't hash %s: File not found.", fle.getName());
			e.printStackTrace();
			return null;
		}
		
		try {
			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			sha.reset();
			
			int read = 0;
			byte buffer[] = new byte[65536];
			
			while((read = fis.read(buffer)) > 0)
				sha.update(buffer, 0, read);
			
			byte digest[] = sha.digest();
			String hash = "";
			
			for(int i = 0; i < digest.length; i++) {
				int b = digest[i] & 0xFF;
				int left = b >>> 4;
				int right = b & 0x0F;
				
				hash += HEX.charAt(left);
				hash += HEX.charAt(right);
			}
			
			return hash;
		} catch(IOException e) {
			Log.error("IOException while hashing file %s", fle.getName());
			e.printStackTrace();
			return null;
		} catch(NoSuchAlgorithmException e) {
			Log.error("Holy crap this shouldn't happen. SHA-1 not found!!!!");
			e.printStackTrace();
			return null;
		} finally {
			close(fis);
		}
	}
	
	/**
	 * Downloads a remote resource.
	 * 
	 * @param res The filename of the resource relative to the mirror root.
	 * @param dst The destination file.
	 * @param ph The progress handler. May be null.
	 * @return true if the download was successful.
	 */
	public static boolean download(String res, File dst, IProgressListener ph) {
		String err = "Couldn't download " + dst.getName() + "!";
		
		ph = secure(ph);
		ph.onTaskChanged("Downloading " + dst.getName());
		
		SizedInputStream is = openStream(res, err);
		if(is == null)
			return false;
		
		delete(dst);
		mkdirs(dst);
		
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(dst);
		} catch(FileNotFoundException e) {
			Log.error("%s Couldn't open the destination file. Maybe you're missing rights.", err);
			e.printStackTrace();
			close(is);
			return false;
		}
		
		int read;
		byte[] data = new byte[65536];
		double total = (double) is.getContentLength();
		double cur = .0d;
		
		try {
			while((read = is.read(data)) > 0) {
				fos.write(data, 0, read);
				
				cur += (double) read;
				ph.onProgressed(cur / total * 100.d);
			}
			
			return true;
		} catch(IOException e) {
			Log.error("%s IOException while downloading.", err);
			e.printStackTrace();
			return false;
		} finally {
			close(is);
			close(fos);
		}
	}
	
	/**
	 * Convenience function. Secures a progress listener.
	 * If pl is null, then a dummy empty progress listener will be returned.
	 * 
	 * @param pl The progress handler to secure.
	 * @return A progress handler that is never null.
	 * @see IProgressListener
	 */
	public static IProgressListener secure(IProgressListener pl) {
		return (pl == null) ? DPH : pl;
	}
	
	/**
	 * Renames a file using a string.
	 * 
	 * @param src The file to rename.
	 * @param name The new name of the file.
	 * @return the new file or null if it failed.
	 */
	public static File rename(File src, String name) {
		File ret = new File(src.getParentFile(), name);
		
		if(src.renameTo(ret))
			return ret;
		else
			return null;
	}
	
	/**
	 * Makes sure that the directory in which the file is exists.
	 * If this one doesn't exist, i'll be created.
	 * 
	 * @param f The file.
	 */
	public static void mkdirs(File f) {
		File p = f.getParentFile();
		if(!p.exists())
			p.mkdirs();
	}
	
	/**
	 * Tries to delete a file in an advanced way.
	 * Does a warning in log if it couldn't delete it neither rename it.
	 * 
	 * @param f The file to be deleted.
	 * @see #delete(File)
	 */
	public static void delete(String f) {
		delete(new File(f));
	}
	
	/**
	 * Tries to delete a file in an advanced way.
	 * Does a warning in log if it couldn't delete it neither rename it.
	 * 
	 * @param f The file to be deleted.
	 * @see #delete(String)
	 */
	public static void delete(File f) {
		if(!f.exists() || f.delete())
			return;
		
		File mv = new File(f.getParentFile(), "deleteme" + ((int) (Math.random() * 100000.d)));
		if(f.renameTo(mv)) {	
			if(!mv.delete())
				mv.deleteOnExit();
			
			return;
		}
		
		Log.warning("Couldn't delete file! If there's any problems, please try to remove it yourself. Path: %s", f.getAbsolutePath());
	}
	
	/**
	 * Tries to open an InputStream to the following remote resource.
	 * Automatically handles broken mirrors and other errors. 
	 * 
	 * @param res The resource filename relative to the root of the mirror.
	 * @param err An error string in case it fails.
	 * @return The opened input stream.
	 */
	public static SizedInputStream openStream(String res, String err) {
		while(Mirror.getCurrent() != null) {
			HttpURLConnection conn;
			
			try {
				conn = Mirror.getCurrent().getResource(res);
			} catch(MalformedURLException e) {
				Log.error("%s Is the mirror list broken?", err);
				e.printStackTrace();
				return null;
			} catch(IOException e) {
				Log.error("%s Is your antivirus or firewall blocking the connection?", err);
				e.printStackTrace();
				return null;
			}
			
			try {
				long len = conn.getContentLengthLong();
				return new SizedInputStream(conn.getInputStream(), len);
			} catch(IOException e) {
				int rc;
				
				try {
					rc = conn.getResponseCode();
				} catch(IOException ie) {
					Log.error("%s Couldn't even get the HTTP response code!", err);
					return null;
				}
				
				Log.error("%s HTTP response is %d; trying with another mirror.", err, rc);
			}
			
			Mirror.markAsBroken();
		}
		
		Log.error("%s All mirrors seems broken.", err);
		return null;
	}
	
	/**
	 * Calls "close" on the specified object without throwing any exceptions.
	 * This is usefull with input and output streams.
	 * 
	 * @param o The object to call close on.
	 */
	public static void close(Object o) {
		try {
			o.getClass().getMethod("close").invoke(o);
		} catch(Throwable t) {}
	}

}
