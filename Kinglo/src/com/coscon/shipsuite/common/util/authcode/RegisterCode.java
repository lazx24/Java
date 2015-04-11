package com.coscon.shipsuite.common.util.authcode;

import java.awt.*;
import java.awt.image.*;
import java.util.*;
import java.io.*;
import javax.imageio.*;

/**
 * 自动生成验证码
 * @author zou
 * 2015-2-4
 */
public class RegisterCode {

    private Color getRandColor(int fc, int bc) { // 给定范围获得随机颜色
	Random random = new Random();
	if (fc > 255)
	    fc = 255;
	if (bc > 255)
	    bc = 255;
	int r = fc + random.nextInt(bc - fc);
	int g = fc + random.nextInt(bc - fc);
	int b = fc + random.nextInt(bc - fc);
	return new Color(r, g, b);
    }

    public BufferedImage getBufferedImage(int width, int height) {
	return new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    public BufferedImage getBufferedImage(InputStream in) throws Exception {
	return ImageIO.read(in);
    }

    public void create(int imageWidth, int imageHeight, String randNumber,
	    String fontType, int fontSize, int x, int y, OutputStream out) {
	BufferedImage image = getBufferedImage(imageWidth, imageHeight);
	generate(image, randNumber, fontType, fontSize, x, y, out);
    }

    public void generate(BufferedImage image, String randNumber,
	    String fontType, int fontSize, int x, int y, OutputStream out) {

	try {

	    int width = image.getWidth();
	    int height = image.getHeight();

	    // 获取图形上下文
	    Graphics g = image.getGraphics();

	    // 设定背景色
	    g.setColor(getRandColor(200, 250));
	    g.fillRect(0, 0, width, height);

	    // 设定字体
	    g.setFont(new Font(fontType, Font.PLAIN, fontSize));

	    // 画边框
	    // g.setColor(new Color());
	    // g.drawRect(0,0,width-1,height-1);

	    // 随机产生155条干扰线，使图象中的认证码不易被其它程序探测到
	    g.setColor(getRandColor(160, 200));
	    // 生成随机类
	    Random random = new Random();
	    for (int i = 0; i < 155; i++) {
		int x2 = random.nextInt(width);
		int y2 = random.nextInt(height);
		int x3 = random.nextInt(12);
		int y3 = random.nextInt(12);
		g.drawLine(x2, y2, x2 + x3, y2 + y3);
	    }

	    // 将认证码显示到图象中
	    g.setColor(new Color(20 + random.nextInt(110), 20 + random
		    .nextInt(110), 20 + random.nextInt(110)));

	    g.drawString(randNumber, x, y);

	    // 图象生效
	    g.dispose();

	    // 输出图象到页面
	    ImageIO.write((BufferedImage) image, "JPEG", out);
	} catch (Exception ex) {
	    System.err.println("generate image error: " + ex);
	}
    }
}