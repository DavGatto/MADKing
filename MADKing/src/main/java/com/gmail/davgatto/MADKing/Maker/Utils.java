package com.gmail.davgatto.MADKing.Maker;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import org.apache.commons.io.FileUtils;

public class Utils {
	
	public final static boolean IS_NOT_UTF8 = !"UTF-8".equals(Charset.defaultCharset().toString());
	
	public static void encodeFile(String filename) throws IOException {
        File file = new File(filename);
        String encoding = Charset.defaultCharset().toString();
        String content = FileUtils.readFileToString(file, encoding);
        FileUtils.write(file, content, "UTF-8");
        System.out.println("File " + filename + " encoded in UTF-8");
    }
}
