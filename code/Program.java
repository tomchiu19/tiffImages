import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.*;

import java.io.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.*;
import java.lang.Math.*;

//TODO: for lossy compression, run length encoding, LZW encoding, then decoding + stats (compression ratio etc)

public class Program {
	public JFrame frame = new JFrame("Tiff Image Reading and Compression");
	public JFrame optionsFrame = new JFrame();
	public JFrame textFrame = new JFrame();
	public static JLabel picture = new JLabel();
	public static JLabel picture2 = new JLabel();
	public static JLabel displayCompressionRatio = new JLabel();
	public JButton open = new JButton("Open");
	public JButton next = new JButton("Next");
	public JButton quit = new JButton("Quit");
	public JButton parrots = new JButton("parrots");
	public JButton board = new JButton("board");
	public JButton lena = new JButton("lena");
	public JButton landscape = new JButton("landscape");
	public static int STATE;
	public Program() {
		STATE = 0;
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1555,850);
		JPanel panel = new JPanel();
		panel.add(open);
		panel.add(next);
		panel.add(quit);
		JPanel options = new JPanel();
		options.add(parrots);
		options.add(board);
		options.add(lena);
		options.add(landscape);

		optionsFrame.setSize(500, 50);
		optionsFrame.setUndecorated(true);
		optionsFrame.getContentPane().add(BorderLayout.CENTER, options);
		optionsFrame.setLocation(525, 760);

		textFrame.setSize(500, 50);
		textFrame.setUndecorated(true);
		textFrame.getContentPane().add(BorderLayout.CENTER, displayCompressionRatio);
		textFrame.setLocation(675, 700);

		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				STATE = 0;
				optionsFrame.setVisible(true);
			}
		});

		next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (STATE <= 3 & STATE > 0) {
					try {
						TiffToImage("parrots.tif");
						optionsFrame.setVisible(false);
						textFrame.setVisible(true);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else if (STATE <= 6 & STATE > 0) {
					try {
						TiffToImage("board.tif");
						optionsFrame.setVisible(false);
						textFrame.setVisible(true);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else if (STATE <= 9 & STATE > 0) {
					try {
						TiffToImage("lena.tif");
						optionsFrame.setVisible(false);
						textFrame.setVisible(true);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else if (STATE <= 12 & STATE > 0) {
					try {
						TiffToImage("landscape.tiff");
						optionsFrame.setVisible(false);
						textFrame.setVisible(true);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});


		parrots.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					STATE = 1;
					TiffToImage("parrots.tif");
					optionsFrame.setVisible(false);
					textFrame.setVisible(true);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		board.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					STATE = 4;
					TiffToImage("board.tif");
					optionsFrame.setVisible(false);
					textFrame.setVisible(true);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		lena.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					STATE = 7;
					TiffToImage("lena.tif");
					optionsFrame.setVisible(false);
					textFrame.setVisible(true);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		landscape.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					STATE = 10;
					TiffToImage("landscape.tiff");
					optionsFrame.setVisible(false);
					textFrame.setVisible(true);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});


		frame.getContentPane().add(BorderLayout.EAST, picture);
		frame.getContentPane().add(BorderLayout.WEST, picture2);
		frame.getContentPane().add(BorderLayout.SOUTH, panel);
		frame.setVisible(true);
	}

	public static void main(String[] args) throws IOException {
		new Program();
	}

	public static int[] RunLengthEncoding(int[] input) {
		int[] tmp = new int[120];
		int count = 0;
		int index = 0;
		int val = 0;

		for(int i = 0; i < input.length; i++) {
			if(input[i] == 0) {
				count++;
			}
			else {
				val = input[i];
				tmp[index++] = count;
				tmp[index++] = val;
				val = 0;
				count = 0;
			}
		}

		int[] output = new int[index + 1];
		output[0] = index;

		for(int i = 0; i < index; i++) {
			output[i+1] = tmp[i];
		}

		return output;

	}

	public static String IntArrayToBinaryString(int[][] input, int length, int width) throws IOException {
		String output = "";
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < length; i++) {
			for(int j = 0; j < width; j++) {
				int val = input[i][j];
				sb.append(IntToByte(val));
			}
		}

		output = sb.toString();
		//System.out.println(sb.length());

		return output;
	}

	public static String IntArrayToBinaryString1D(int[] input) throws IOException {
		String output = "";
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < input.length; i++) {
			int val = input[i];
			sb.append(IntToByte(val));
		}

		output = sb.toString();
		//System.out.println(sb.length());

		return output;
	}

	public static String SignedIntArrayToBinaryString1D(int[] input) throws IOException {
		String output = "";
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < input.length; i++) {
			int val = input[i];
			sb.append(SignedIntToByte(val));
		}

		output = sb.toString();
		//System.out.println(sb.length());

		return output;
	}

	public static String IntArrayToTwoByteBinaryString(int[][] input, int length, int width) throws IOException {
		String output = "";
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < length; i++) {
			for(int j = 0; j < width; j++) {
				int val = input[i][j];
				sb.append(IntToTwoBytes(val));
			}
		}

		output = sb.toString();
		//System.out.println(sb.length());

		return output;
	}

	public static int[][] BinaryStringToIntArray(String input, int length, int width) throws IOException {
		int[][] output = new int[length][width];
		for(int i = 0; i < length; i++) {
			for(int j = 0; j < width; j++) {
				int val = 0;
				for(int k = 0; k < 8; k++) {
					if(input.charAt(i*8*width + j*8 + k) == '1') {
						val += Math.pow(2, (7-k));
					}
				}
				output[i][j] = val;
			}
		}


		return output;
	}

	public static int[][] TwoByteBinaryStringToIntArray(String input, int length, int width) throws IOException {
		int[][] output = new int[length][width];
		boolean positive;
		if(input.charAt(0) == '0') {
			positive = true;
		}
		else {
			positive = false;
		}
		for(int i = 0; i < length; i++) {
			for(int j = 0; j < width; j++) {
				int val = 0;
				for(int k = 1; k < 16; k++) {
					if(input.charAt(i*16*width + j*16 + k) == '1') {
						val += Math.pow(2, (15-k));
					}
				}
				if(input.charAt(i*16*width + j*16) == '0') {
					output[i][j] = val;
				}
				else {
					output[i][j] = val * -1;
				}
			}
		}

		return output;
	}

	public static int[] BinaryStringToIntArray1D(String input, int length) throws IOException {
		int[] output = new int[length];
		for(int i = 0; i < length; i++) {
				int val = 0;
				for(int k = 0; k < 8; k++) {
					if(input.charAt(i*8 + k) == '1') {
						val += Math.pow(2, (7-k));
					}
				}
				output[i] = val;
		}


		return output;
	}

	public static int[] SignedBinaryStringToIntArray1D(String input, int length) throws IOException {
		int[] output = new int[length];
		for(int i = 0; i < length; i++) {
				int val = 0;
				for(int k = 1; k < 9; k++) {
					if(input.charAt(i*9 + k) == '1') {
						val += Math.pow(2, (8-k));
					}
				}
				if(input.charAt(i*9) == '0') {
					output[i] = val;
				}
				else {
					output[i] = val * -1;
				}
		}


		return output;
	}

	public static int BinaryStringToInt(String input) throws IOException {
		int length = input.length();
		int output = 0;
		for(int i = 0; i < length; i++) {
			if(input.charAt(i) == '1') {
				output += Math.pow(2, ((length-1)-i));
			}
		}
		return output;
	}

	public static String IntToByte(int input) {
		StringBuilder sb = new StringBuilder();
		String output = "";
		while(input > 0) {
			if (input % 2 == 1) {
				sb.append("1");
			}
			else {
				sb.append("0");
			}
			input = input / 2;
		}
		while (sb.length() < 8) {
			sb.append("0");
		}

		output = sb.reverse().toString();
		return output;
	}

	public static String SignedIntToByte(int input) {
		int val = Math.abs(input);
		if(val > 255) {
			val = 255;
		}
		if(val < -255) {
			val = -255;
		}
		boolean positive;
		if(input >= 0) {
			positive = true;
		}
		else {
			positive = false;
		}
		StringBuilder sb = new StringBuilder();
		String output = "";
		while(val > 0) {
			if (val % 2 == 1) {
				sb.append("1");
			}
			else {
				sb.append("0");
			}
			val = val / 2;
		}
		while (sb.length() < 8) {
			sb.append("0");
		}
		if(positive) {
			sb.append("0");
		}
		else {
			sb.append("1");
		}

		output = sb.reverse().toString();
		return output;
	}

	public static String IntToTwoBytes(int input) {
		int val = Math.abs(input);
		boolean positive;
		if(input >= 0) {
			positive = true;
		}
		else {
			positive = false;
		}
		StringBuilder sb = new StringBuilder();
		String output = "";
		while(val > 0) {
			if (val % 2 == 1) {
				sb.append("1");
			}
			else {
				sb.append("0");
			}
			val = val / 2;
		}
		while (sb.length() < 15) {
			sb.append("0");
		}
		if(positive) {
			sb.append("0");
		}
		else {
			sb.append("1");
		}

		output = sb.reverse().toString();
		return output;
	}

	public static String IntToThreeBytes(int input) {
		StringBuilder sb = new StringBuilder();
		String output = "";
		while(input > 0) {
			if (input % 2 == 1) {
				sb.append("1");
			}
			else {
				sb.append("0");
			}
			input = input / 2;
		}
		while (sb.length() < 24) {
			sb.append("0");
		}

		output = sb.reverse().toString();
		return output;
	}

	/*public static int[][] createQMatrix() {
		int[][] qMatrix = {{1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1},
												{1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1}};

		return qMatrix;
	}*/

	public static int[][] createQMatrix1() {
		int[][] qMatrix = {{1, 1, 2, 4, 8, 16, 32, 64}, {1, 1, 2, 4, 8, 16, 32, 64}, {2, 2, 2, 4, 8, 16, 32, 64}, {4, 4, 4, 4, 8, 16, 32, 64},
												{8, 8, 8, 8, 8, 16, 32, 64}, {16, 16, 16, 16, 16, 16, 32, 64}, {32, 32, 32, 32, 32, 32, 32, 64}, {64, 64, 64, 64, 64, 64, 64, 64}};

		return qMatrix;
	}

	public static int[][] createQMatrix3() {
		int[][] qMatrix = {{1, 1, 8, 24, 40, 56, 72, 88}, {1, 1, 8, 24, 40, 56, 72, 88}, {24, 24, 24, 24, 40, 56, 72, 88}, {24, 24, 24, 24, 40, 56, 72, 88},
												{40, 40, 40, 40, 40, 56, 72, 88}, {56, 56, 56, 56, 56, 56, 72, 88}, {72, 72, 72, 72, 72, 72, 72, 88}, {88, 88, 88, 88, 88, 88, 88, 88}};

		return qMatrix;
	}

	public static int[][] createQMatrix4() {
		int[][] qMatrix = {{1, 1, 4, 8, 16, 24, 32, 40}, {1, 1, 4, 8, 16, 24, 32, 40}, {4, 4, 4, 8, 16, 24, 32, 40}, {8, 8, 8, 8, 16, 24, 32, 40},
												{16, 16, 16, 16, 16, 24, 32, 40}, {24, 24, 24, 24, 24, 24, 32, 40}, {32, 32, 32, 32, 32, 32, 32, 40}, {40, 40, 40, 40, 40, 40, 40, 40}};

		return qMatrix;
	}

	public static int[][] createQMatrix5() {
		int[][] qMatrix = {{1, 1, 32, 64, 128, 256, 512, 1024}, {1, 1, 32, 64, 128, 256, 512, 1024}, {32, 32, 32, 64, 128, 256, 512, 1024}, {64, 64, 64, 64, 128, 256, 512, 1024},
												{128, 128, 128, 128, 128, 256, 512, 1024}, {256, 256, 256, 256, 256, 256, 512, 1024}, {512, 512, 512, 512, 512, 512, 512, 1024}, {1024, 1024, 1024, 1024, 1024, 1024, 1024, 1024}};

		return qMatrix;
	}

	public static int[][] createQMatrix6() {
		int[][] qMatrix = {{1, 1, 64, 128, 256, 512, 1024, 2048}, {1, 1, 64, 128, 256, 512, 1024, 2048}, {64, 64, 64, 128, 256, 512, 1024, 2048}, {128, 128, 128, 128, 256, 512, 1024, 2048},
												{256, 256, 256, 256, 256, 512, 1024, 1024}, {512, 512, 512, 512, 512, 512, 1024, 2048}, {1024, 1024, 1024, 1024, 1024, 1024, 1024, 2048}, {2048, 2048, 2048, 2048, 2048, 2048, 2048, 2048}};

		return qMatrix;
	}

	public static int[][] createQMatrix7() {
		int[][] qMatrix = {{1, 1, 256, 512, 1024, 2048, 2048, 2048}, {1, 1, 256, 512, 1024, 2048, 2048, 2048}, {256, 256, 256, 512, 1024, 2048, 2048, 2048}, {512, 512, 512, 512, 1024, 2048, 2048, 2048}, {1024, 1024, 1024, 1024, 1024, 2048, 2048, 2048}, {2048, 2048, 2048, 2048, 2048, 2048, 2048, 2048}, {2048, 2048, 2048, 2048, 2048, 2048, 2048, 2048}, {2048, 2048, 2048, 2048, 2048, 2048, 2048, 2048}};

		return qMatrix;
	}

	public static int[][] createQMatrix2() {
		int[][] qMatrix = {{1, 1, 4, 8, 16, 32, 64, 128}, {1, 1, 4, 8, 16, 32, 64, 128}, {4, 4, 4, 8, 16, 32, 64, 128}, {8, 8, 8, 8, 16, 32, 64, 128},
												{16, 16, 16, 16, 16, 32, 64, 128}, {32, 32, 32, 32, 32, 32, 64, 128}, {64, 64, 64, 64, 64, 64, 64, 128}, {128, 128, 128, 128, 128, 128, 128, 128}};

		return qMatrix;
	}

	/*public static int[][] createQMatrix() {
		int[][] qMatrix = {{64, 64, 64, 64, 64, 64, 64, 64}, {64, 64, 64, 64, 64, 64, 64, 64}, {64, 64, 64, 64, 64, 64, 64, 64}, {64, 64, 64, 64, 64, 64, 64, 64},
												{64, 64, 64, 64, 64, 64, 64, 64}, {64, 64, 64, 64, 64, 64, 64, 64}, {64, 64, 64, 64, 64, 64, 64, 64}, {64, 64, 64, 64, 64, 64, 64, 64}};

		return qMatrix;
	}*/

	public static int[][] DCT(int[][] input) {
		int n = 8;

		double[][] DCTMatrix = new double[n][n];
		double a1 = Math.sqrt((1.0)/n);
		double a2 = Math.sqrt((2.0)/n);

		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				if (i == 0) {
					DCTMatrix[i][j] = a1*Math.cos((((2*j) + 1) * i * Math.PI)/(2.0*n));
				}
				else {
					DCTMatrix[i][j] = a2*Math.cos((((2*j) + 1) * i * Math.PI)/(2.0*n));
				}
			}
		}

		double[][] outputMatrix1 = new double[n][n];
		int[][] outputMatrix2 = new int[n][n];

		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				double tmp = 0.0;
				for(int k = 0; k < n; k++) {
					tmp += DCTMatrix[i][k] * input[k][j];
				}
				outputMatrix1[i][j] = tmp;
			}
		}

		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				double tmp = 0.0;
				for(int k = 0; k < n; k++) {
					tmp += outputMatrix1[i][k] * DCTMatrix[j][k];
				}
				outputMatrix2[i][j] = (int)Math.round(tmp);
				//System.out.print(outputMatrix2[i][j]);
				//System.out.print(" ");
			}
			//System.out.println();
		}

		return outputMatrix2;
	}

	public static int[][] inverseDCT(int[][] input) {
		int n = 8;

		double[][] DCTMatrix = new double[n][n];
		double a1 = Math.sqrt((1.0)/n);
		double a2 = Math.sqrt((2.0)/n);

		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				if (i == 0) {
					DCTMatrix[i][j] = a1*Math.cos((((2*j) + 1) * i * Math.PI)/(2.0*n));
				}
				else {
					DCTMatrix[i][j] = a2*Math.cos((((2*j) + 1) * i * Math.PI)/(2.0*n));
				}
			}
		}

		double[][] outputMatrix1 = new double[n][n];
		int[][] outputMatrix2 = new int[n][n];

		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				double tmp = 0.0;
				for(int k = 0; k < n; k++) {
					tmp += DCTMatrix[k][i] * input[k][j];
				}
				outputMatrix1[i][j] = tmp;
			}
		}

		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				double tmp = 0.0;
				for(int k = 0; k < n; k++) {
					tmp += outputMatrix1[i][k] * DCTMatrix[k][j];
				}
				outputMatrix2[i][j] = (int)Math.round(tmp);
				//System.out.print(outputMatrix2[i][j]);
				//System.out.print(" ");
			}
			//System.out.println();
		}

		return outputMatrix2;
	}

	public static void TiffToImage(String file) throws IOException {
		long startTime = System.nanoTime();
		Path path = Paths.get(file);
		byte[] fileContents = Files.readAllBytes(path);
		byte[] compressedFileContents = new byte[fileContents.length];
		//String uncompressedBinary = binaryToString(fileContents);
		int format = fileContents[0] & 0xff ;
		long numTags;
		//System.out.println(format);
		int[] num2 = new int[2];
		int[] num4 = new int[4];
		int[] num12 = new int[12];
		int[] imageWidthArray = new int[4];
		int[] imageLengthArray = new int[4];
		int[] bitsPerSampleOffsetArray = new int[4];
		int[] numbitsPerSampleArray = new int[4];
		int[] compressionArray = new int[4];
		int[] photoInterpArray = new int[4];
		int[] numstripOffsetsArray = new int[4];
		int[] offset1Array = new int[4];
		int[] samplesPerPixArray = new int[4];
		int[] rowsPerStripArray = new int[4];
		int[] stripByteCountOffsetArray = new int[4];
		int[] xResOffsetArray = new int[4];
		int[] yResOffsetArray = new int[4];

		long imageWidth = 0, imageLength = 0, numbitsPerSample = 0, bitsPerSampleOffset = 0, compression, photoInterp, numstripOffsets = 0;
		long samplesPerPix = 0, rowsPerStrip, stripByteCountOffset = 0, xResOffset, yResOffset;
		long stripOffsetsAddress = 0, sizestripByteCounts = 0;

		String simageWidth, simageLength, snumbitsPerSample, sbitsPerSampleOffset, scompression, sphotoInterp, snumstripOffsets;
		String ssamplesPerPix, srowsPerStrip, sstripByteCountOffset, sxResOffset, syResOffset;
		String sstripOffsetsAddress;

		StringBuilder sb = new StringBuilder();
		//little endian = 73, initial offset
		if(format == 73) {
			//System.out.println("little endian");
			num4[0] = fileContents[7] & 0xff;
			num4[1] = fileContents[6] & 0xff;
			num4[2] = fileContents[5] & 0xff;
			num4[3]= fileContents[4] & 0xff;
			for (int i : num4) {
				sb.append(String.format("%02X",  i));
			}
		}
		//big endian = 77, initial offset
		else {
			//System.out.println("big endian");
			num4[0] = fileContents[4] & 0xff;
			num4[1] = fileContents[5] & 0xff;
			num4[2] = fileContents[6] & 0xff;
			num4[3]= fileContents[7] & 0xff;
			for (int i : num4) {
				sb.append(String.format("%02X",  i));
			}
		}
		String hexoffset1 = sb.toString();
		sb.setLength(0);
		long ioffset1 = Long.parseLong(hexoffset1, 16);
		//little endian
		if(format == 73) {
			for (int i = 0; i < 2; i++) {
				num2[i] = fileContents[(int) ioffset1 + 1 - i] & 0xff;
			}
		}
		else {
			for (int i = 0; i < 2; i++) {
				num2[i] = fileContents[(int) ioffset1 + i] & 0xff;
			}
		}
		for (int i : num2) {
			sb.append(String.format("%02X", i));
		}
		String snumTags = sb.toString();
		sb.setLength(0);
		//System.out.println("num tags is " + snumTags);
		numTags = Long.parseLong(snumTags, 16);
		//System.out.println(numTags);

		String[] allTags = new String[(int) numTags];

		for (int i = 0; i < (int) numTags; i++) {
			if(format == 73) {
				num2[1] = fileContents[(int) ioffset1 + 2 + i*12] & 0xff;
				num2[0] = fileContents[(int) ioffset1 + 3 + i*12] & 0xff;
				for (int n : num2) {
					sb.append(String.format("%02X", n));
				}
				String tmpTag = sb.toString();
				sb.setLength(0);
				allTags[i] = tmpTag;
				//System.out.println(allTags[i]);
			}
			else {
				num2[0] = fileContents[(int) ioffset1 + 2 + i*12] & 0xff;
				num2[1] = fileContents[(int) ioffset1 + 3 + i*12] & 0xff;
				for (int n : num2) {
					sb.append(String.format("%02X", n));
				}
				String tmpTag = sb.toString();
				sb.setLength(0);
				allTags[i] = tmpTag;
				//System.out.println(allTags[i]);
			}
		}

		if (format == 73) {
			for (int i = 0; i < (int) numTags; i++) {
				//imageWidth tag
				if (allTags[i].equals("0100")) {
					for (int j = 0; j < 2; j++) {
						num2[j] = fileContents[(int) ioffset1 + i*12 - j + 2 + 9] & 0xff;
					}
					for (int n : num2) {
						sb.append(String.format("%02X", n));
					}
					simageWidth = sb.toString();
					sb.setLength(0);
					imageWidth = Long.parseLong(simageWidth, 16);
					//System.out.println("image width is " + imageWidth);
				}
				//imageLength tag
				if (allTags[i].equals("0101")) {
					for (int j = 0; j < 2; j++) {
						num2[j] = fileContents[(int) ioffset1 + i*12 - j + 2 + 9] & 0xff;
					}
					for (int n : num2) {
						sb.append(String.format("%02X", n));
					}
					simageLength = sb.toString();
					sb.setLength(0);
					imageLength = Long.parseLong(simageLength, 16);
					//System.out.println("image length is " + imageLength);
				}
				//BitsPerSample offset tag
				if (allTags[i].equals("0102")) {
					//number of Bits per Sample
					for (int j = 0; j < 4; j++) {
						num4[j] = fileContents[(int) ioffset1 + i*12 - j + 2 + 7] & 0xff;
					}
					for (int n : num4) {
						sb.append(String.format("%02X", n));
					}
					snumbitsPerSample = sb.toString();
					//System.out.println(snumbitsPerSample);
					sb.setLength(0);
					numbitsPerSample = Long.parseLong(snumbitsPerSample, 16);
					//System.out.println("number of bits per sample is " + numbitsPerSample);

					//Bits per sample offset
					for (int j = 0; j < 4; j++) {
						num4[j] = fileContents[(int) ioffset1 + i*12 - j + 2 + 11] & 0xff;
					}
					for (int n : num4) {
						sb.append(String.format("%02X", n));
					}
					sbitsPerSampleOffset = sb.toString();
					sb.setLength(0);
					bitsPerSampleOffset = Long.parseLong(sbitsPerSampleOffset, 16);
					//System.out.println("bits per sample offset is " + bitsPerSampleOffset);
				}
				//StripOffsets tag
				if (allTags[i].equals("0111")) {
					//number of Strip Offsets
					for (int j = 0; j < 4; j++) {
						num4[j] = fileContents[(int) ioffset1 + i*12 - j + 2 + 7] & 0xff;
					}
					for (int n : num4) {
						sb.append(String.format("%02X", n));
					}
					snumstripOffsets = sb.toString();
					//System.out.println(snumstripOffsets);
					sb.setLength(0);
					numstripOffsets = Long.parseLong(snumstripOffsets, 16);
					//System.out.println("number of strip offsets is " + numstripOffsets);

					//address of strip offsets
					for (int j = 0; j < 4; j++) {
						num4[j] = fileContents[(int) ioffset1 + i*12 - j + 2 + 11] & 0xff;
					}
					for (int n : num4) {
						sb.append(String.format("%02X", n));
					}
					sstripOffsetsAddress = sb.toString();
					//System.out.println(sstripOffsetsAddress);
					sb.setLength(0);
					stripOffsetsAddress = Long.parseLong(sstripOffsetsAddress, 16);
					//System.out.println("strip offsets address is " + stripOffsetsAddress);
				}
				//SamplesPerPixel tag
				if (allTags[i].equals("0115")) {
					for (int j = 0; j < 2; j++) {
						num2[j] = fileContents[(int) ioffset1 + i*12 - j + 2 + 9] & 0xff;
					}
					for (int n : num2) {
						sb.append(String.format("%02X", n));
					}
					ssamplesPerPix = sb.toString();
					//System.out.println(ssamplesPerPix);
					sb.setLength(0);
					samplesPerPix = Long.parseLong(ssamplesPerPix, 16);
					//System.out.println("samples per pixel is " + samplesPerPix);
				}
				//RowsPerStrip tag
				if (allTags[i].equals("0116")) {
					for (int j = 0; j < 2; j++) {
						num2[j] = fileContents[(int) ioffset1 + i*12 - j + 2 + 9] & 0xff;
					}
					for (int n : num2) {
						sb.append(String.format("%02X", n));
					}
					srowsPerStrip = sb.toString();
					//System.out.println(srowsPerStrip);
					sb.setLength(0);
					rowsPerStrip = Long.parseLong(srowsPerStrip, 16);
					//System.out.println("number of rows per strip is " + rowsPerStrip);
				}
				//StripByteCounts tag
				if (allTags[i].equals("0117")) {
					//size of strip bite counts
					for (int j = 0; j < 2; j++) {
						num2[j] = fileContents[(int) ioffset1 + i*12 - j + 2 + 3] & 0xff;
					}
					for (int n : num2) {
						sb.append(String.format("%02X", n));
					}
					String ssizestripByteCounts = sb.toString();
					//System.out.println(ssizestripByteCounts);
					sb.setLength(0);
					sizestripByteCounts = Long.parseLong(ssizestripByteCounts, 16);
					//System.out.println("size of strips is " + sizestripByteCounts);

					//number of strip byte counts
					for (int j = 0; j < 4; j++) {
						num4[j] = fileContents[(int) ioffset1 + i*12 - j + 2 + 7] & 0xff;
					}
					for (int n : num4) {
						sb.append(String.format("%02X", n));
					}
					String snumstripByteCounts = sb.toString();
					//System.out.println(snumstripByteCounts);
					sb.setLength(0);
					long numstripByteCounts = Long.parseLong(snumstripByteCounts, 16);
					//System.out.println("number of strips is " + numstripByteCounts);

					//address of strip byte counts
					for (int j = 0; j < 4; j++) {
						num4[j] = fileContents[(int) ioffset1 + i*12 - j + 2 + 11] & 0xff;
					}
					for (int n : num4) {
						sb.append(String.format("%02X", n));
					}
					sstripByteCountOffset = sb.toString();
					//System.out.println(sstripByteCountOffset);
					sb.setLength(0);
					stripByteCountOffset = Long.parseLong(sstripByteCountOffset, 16);
					//System.out.println("strip bytecount offset is " + stripByteCountOffset);
				}

			}
		}
		else {
			for (int i = 0; i < (int) numTags; i++) {
				//imageWidth tag
				if (allTags[i].equals("0100")) {
					for (int j = 0; j < 2; j++) {
						num2[j] = fileContents[(int) ioffset1 + i*12 + j + 2 + 8] & 0xff;
					}
					for (int n : num2) {
						sb.append(String.format("%02X", n));
					}
					simageWidth = sb.toString();
					sb.setLength(0);
					imageWidth = Long.parseLong(simageWidth, 16);
					//System.out.println("image width is " + imageWidth);
				}
				//imageLength tag
				if (allTags[i].equals("0101")) {
					for (int j = 0; j < 2; j++) {
						num2[j] = fileContents[(int) ioffset1 + i*12 + j + 2 + 8] & 0xff;
					}
					for (int n : num2) {
						sb.append(String.format("%02X", n));
					}
					simageLength = sb.toString();
					sb.setLength(0);
					imageLength = Long.parseLong(simageLength, 16);
					//System.out.println("image length is " + imageLength);
				}
				//BitsPerSample offset tag
				if (allTags[i].equals("0102")) {
					//number of Bits per Sample
					for (int j = 0; j < 4; j++) {
						num4[j] = fileContents[(int) ioffset1 + i*12 + j + 2 + 4] & 0xff;
					}
					for (int n : num4) {
						sb.append(String.format("%02X", n));
					}
					snumbitsPerSample = sb.toString();
					//System.out.println(snumbitsPerSample);
					sb.setLength(0);
					numbitsPerSample = Long.parseLong(snumbitsPerSample, 16);
					//System.out.println("number of bits per sample is " + numbitsPerSample);

					//Bits per sample offset
					for (int j = 0; j < 4; j++) {
						num4[j] = fileContents[(int) ioffset1 + i*12 + j + 2 + 8] & 0xff;
					}
					for (int n : num4) {
						sb.append(String.format("%02X", n));
					}
					sbitsPerSampleOffset = sb.toString();
					sb.setLength(0);
					bitsPerSampleOffset = Long.parseLong(sbitsPerSampleOffset, 16);
					//System.out.println("bits per sample offset is " + bitsPerSampleOffset);
				}
				//StripOffsets tag
				if (allTags[i].equals("0111")) {
					//number of Strip Offsets
					for (int j = 0; j < 4; j++) {
						num4[j] = fileContents[(int) ioffset1 + i*12 + j + 2 + 4] & 0xff;
					}
					for (int n : num4) {
						sb.append(String.format("%02X", n));
					}
					snumstripOffsets = sb.toString();
					//System.out.println(snumstripOffsets);
					sb.setLength(0);
					numstripOffsets = Long.parseLong(snumstripOffsets, 16);
					//System.out.println("number of strip offsets is " + numstripOffsets);

					//address of strip offsets
					for (int j = 0; j < 4; j++) {
						num4[j] = fileContents[(int) ioffset1 + i*12 + j + 2 + 8] & 0xff;
					}
					for (int n : num4) {
						sb.append(String.format("%02X", n));
					}
					sstripOffsetsAddress = sb.toString();
					//System.out.println(sstripOffsetsAddress);
					sb.setLength(0);
					stripOffsetsAddress = Long.parseLong(sstripOffsetsAddress, 16);
					//System.out.println("strip offsets address is " + stripOffsetsAddress);
				}
				//SamplesPerPixel tag
				if (allTags[i].equals("0115")) {
					for (int j = 0; j < 2; j++) {
						num2[j] = fileContents[(int) ioffset1 + i*12 + j + 2 + 8] & 0xff;
					}
					for (int n : num2) {
						sb.append(String.format("%02X", n));
					}
					ssamplesPerPix = sb.toString();
					//System.out.println(ssamplesPerPix);
					sb.setLength(0);
					samplesPerPix = Long.parseLong(ssamplesPerPix, 16);
					//System.out.println("samples per pixel is " + samplesPerPix);
				}
				//RowsPerStrip tag
				if (allTags[i].equals("0116")) {
					for (int j = 0; j < 2; j++) {
						num2[j] = fileContents[(int) ioffset1 + i*12 + j + 2 + 8] & 0xff;
					}
					for (int n : num2) {
						sb.append(String.format("%02X", n));
					}
					srowsPerStrip = sb.toString();
					//System.out.println(srowsPerStrip);
					sb.setLength(0);
					rowsPerStrip = Long.parseLong(srowsPerStrip, 16);
					//System.out.println("number of rows per strip is " + rowsPerStrip);
				}
				//StripByteCounts tag
				if (allTags[i].equals("0117")) {
					//size of strip bite counts
					for (int j = 0; j < 2; j++) {
						num2[j] = fileContents[(int) ioffset1 + i*12 + j + 2 + 2] & 0xff;
					}
					for (int n : num2) {
						sb.append(String.format("%02X", n));
					}
					String ssizestripByteCounts = sb.toString();
					//System.out.println(ssizestripByteCounts);
					sb.setLength(0);
					sizestripByteCounts = Long.parseLong(ssizestripByteCounts, 16);
					//System.out.println("size of strips is " + sizestripByteCounts);

					//number of strip byte counts
					for (int j = 0; j < 4; j++) {
						num4[j] = fileContents[(int) ioffset1 + i*12 + j + 2 + 4] & 0xff;
					}
					for (int n : num4) {
						sb.append(String.format("%02X", n));
					}
					String snumstripByteCounts = sb.toString();
					//System.out.println(snumstripByteCounts);
					sb.setLength(0);
					long numstripByteCounts = Long.parseLong(snumstripByteCounts, 16);
					//System.out.println("number of strips is " + numstripByteCounts);

					//address of strip byte counts
					for (int j = 0; j < 4; j++) {
						num4[j] = fileContents[(int) ioffset1 + i*12 + j + 2 + 8] & 0xff;
					}
					for (int n : num4) {
						sb.append(String.format("%02X", n));
					}
					sstripByteCountOffset = sb.toString();
					//System.out.println(sstripByteCountOffset);
					sb.setLength(0);
					stripByteCountOffset = Long.parseLong(sstripByteCountOffset, 16);
					//System.out.println("strip bytecount offset is " + stripByteCountOffset);
				}

			}
		}


		long[] allBitsPerSample = new long[(int) numbitsPerSample];

		for(int j = 0; j < numbitsPerSample; j++) {

			if(format == 73) {
				for (int i = 0; i < 2; i++) {
					num2[i] = fileContents[(int) bitsPerSampleOffset + 3 + j*2 - i] & 0xff;
				}
			}
			else {
				for (int i = 0; i < 2; i++) {
					num2[i] = fileContents[(int) bitsPerSampleOffset + j*2 + i] & 0xff;
				}
			}

			for (int i : num2) {
				sb.append(String.format("%02X", i));
			}
			String sjthBitPerSample = sb.toString();
			sb.setLength(0);
			//System.out.println("jth bit per sample" + j);
			//System.out.println(sjthBitPerSample);
			long jthBitPerSample = Long.parseLong(sjthBitPerSample, 16);
			//System.out.println(jthBitPerSample);

			allBitsPerSample[j] = jthBitPerSample;

		}

		long[] allOffsets = new long[(int) numstripOffsets];

		if ((int)numstripOffsets > 1) {
			for(int j = 0; j < numstripOffsets; j++) {

				if(format == 73) {
					for (int i = 0; i < 4; i++) {
						num4[i] = fileContents[(int) stripOffsetsAddress + 3 + j*4 - i] & 0xff;
					}
				}
				else {
					for (int i = 0; i < 4; i++) {
						num4[i] = fileContents[(int) stripOffsetsAddress + j*4 + i] & 0xff;
					}
				}

				for (int i : num4) {
					sb.append(String.format("%02X", i));
				}
				String sjthOffset = sb.toString();
				sb.setLength(0);
				//System.out.println("jth offset" + j);
				//System.out.println(sjthOffset);
				long jthOffset = Long.parseLong(sjthOffset, 16);
				//System.out.println(jthOffset);

				allOffsets[j] = jthOffset;

			}
		}
		else {
			allOffsets[0] = stripOffsetsAddress;
			//System.out.println("my only offset is the following");
			//System.out.println(allOffsets[0]);
		}

		long[] allStripByteCounts = new long[(int) numstripOffsets];
		int BytesPerStrip = 0;
		if((int) sizestripByteCounts == 3) {
			BytesPerStrip = 2;
		}
		if((int) sizestripByteCounts == 4) {
			BytesPerStrip = 4;
		}
		//System.out.println(BytesPerStrip);

		if ((int)numstripOffsets > 1) {
			if (BytesPerStrip == 2) {
				for(int j = 0; j < numstripOffsets; j++) {

					if(format == 73) {
						for (int i = 0; i < 2; i++) {
							num2[i] = fileContents[(int) stripByteCountOffset + 1 + j*2 - i] & 0xff;
						}
					}
					else {
						for (int i = 0; i < 2; i++) {
							num2[i] = fileContents[(int) stripByteCountOffset + j*2 + i] & 0xff;
						}
					}

					for (int i : num2) {
						sb.append(String.format("%02X", i));
					}
					String sjthByteCount = sb.toString();
					sb.setLength(0);
					//System.out.println("jth bytecount" + j);
					//System.out.println(sjthByteCount);
					long jthByteCount = Long.parseLong(sjthByteCount, 16);
					//System.out.println("the " + j + "th byte count is " + jthByteCount);

					allStripByteCounts[j] = jthByteCount;
				}
			}
			else if (BytesPerStrip == 4) {
				for(int j = 0; j < numstripOffsets; j++) {

					if(format == 73) {
						for (int i = 0; i < 4; i++) {
							num4[i] = fileContents[(int) stripByteCountOffset + 3 + j*4 - i] & 0xff;
						}
					}
					else {
						for (int i = 0; i < 4; i++) {
							num4[i] = fileContents[(int) stripByteCountOffset + j*4 + i] & 0xff;
						}
					}

					for (int i : num4) {
						sb.append(String.format("%02X", i));
					}
					String sjthByteCount = sb.toString();
					sb.setLength(0);
					//System.out.println("jth bytecount" + j);
					//System.out.println(sjthByteCount);
					long jthByteCount = Long.parseLong(sjthByteCount, 16);
					//System.out.println("the " + j + "th byte count is " + jthByteCount);

					allStripByteCounts[j] = jthByteCount;
				}
			}

		}
		else {
			allStripByteCounts[0] = stripByteCountOffset;
		}

		BufferedImage image1 = new BufferedImage((int) imageWidth, (int) imageLength, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = image1.createGraphics();
		BufferedImage image2 = new BufferedImage((int) imageWidth, (int) imageLength, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d2 = image2.createGraphics();
		int r, g, b, col, grayCol;
		double doubleR, doubleG, doubleB;
		int grayR, grayG, grayB;
		int[][] rArray = new int[(int)imageLength][(int)imageWidth];
		int[][] gArray = new int[(int)imageLength][(int)imageWidth];
		int[][] bArray = new int[(int)imageLength][(int)imageWidth];
		for (int i = 0; i < (int) imageLength; i++) {
			for (int j = 0; j < (int) imageWidth; j++) {
				r = fileContents[(int) allOffsets[0] + (int) samplesPerPix*i* (int) imageWidth + (int) samplesPerPix*j]  & 0xff;
				g = fileContents[(int) allOffsets[0] + (int) samplesPerPix*i* (int) imageWidth + (int) samplesPerPix*j + 1]  & 0xff;
				b = fileContents[(int) allOffsets[0] + (int) samplesPerPix*i* (int) imageWidth + (int) samplesPerPix*j + 2]  & 0xff;

				rArray[i][j] = r;
				gArray[i][j] = g;
				bArray[i][j] = b;

				doubleR = (double) r;
				doubleG = (double) g;
				doubleB = (double) b;
				grayR = (int) (doubleR * 0.299);
				grayG = (int) (doubleG * 0.587);
				grayB = (int) (doubleB * 0.114);
				grayCol = grayR + grayG + grayB;

				col = (r << 16) | (g << 8) | b;
				image1.setRGB(j, i, col);

			}
		}

		long readingInInitialImage = System.nanoTime() - startTime;
		startTime = System.nanoTime();

		if(STATE % 3 == 1) {
			startTime = System.nanoTime();
			int tmpImageLength = (int)imageLength;
			int tmpImageWidth = (int)imageWidth;

			int initialSize = (int)imageLength*(int)imageWidth;

			while(tmpImageLength % 8 != 0) {
				tmpImageLength++;
			}
			while(tmpImageWidth % 8 != 0) {
				tmpImageWidth++;
			}

			int[][] tmpRArray = new int[tmpImageLength][tmpImageWidth];
			int[][] tmpGArray = new int[tmpImageLength][tmpImageWidth];
			int[][] tmpBArray = new int[tmpImageLength][tmpImageWidth];

			for(int i = 0; i < tmpImageLength; i++) {
				for(int j = 0; j < tmpImageWidth; j++) {
					if(i > (int)(imageLength-1) && j < (int)imageWidth) {
						tmpRArray[i][j] = rArray[(int)(imageLength-1)][j];
						tmpGArray[i][j] = gArray[(int)(imageLength-1)][j];
						tmpBArray[i][j] = bArray[(int)(imageLength-1)][j];
					}
					else if(i < (int)(imageLength) && j > (int)(imageWidth-1)) {
						tmpRArray[i][j] = rArray[i][(int)(imageWidth-1)];
						tmpGArray[i][j] = gArray[i][(int)(imageWidth-1)];
						tmpBArray[i][j] = bArray[i][(int)(imageWidth-1)];
					}
					else if(i > (int)(imageLength-1) && j > (int)(imageWidth-1)) {
						tmpRArray[i][j] = rArray[(int)(imageLength-1)][(int)(imageWidth-1)];
						tmpGArray[i][j] = gArray[(int)(imageLength-1)][(int)(imageWidth-1)];
						tmpBArray[i][j] = bArray[(int)(imageLength-1)][(int)(imageWidth-1)];
					}
					else {
						tmpRArray[i][j] = rArray[i][j];
						tmpGArray[i][j] = gArray[i][j];
						tmpBArray[i][j] = bArray[i][j];
					}
				}
			}

			int numBlocksLength = tmpImageLength / 8;
			int numBlocksWidth = tmpImageWidth / 8;
			int totalBlocks = numBlocksLength * numBlocksWidth;

			int[][][] originalRBlocks = new int[totalBlocks][8][8];
			int[][][] originalGBlocks = new int[totalBlocks][8][8];
			int[][][] originalBBlocks = new int[totalBlocks][8][8];

			int blockWidthIndex = 0;
			int blockLengthIndex = 0;

			for(int k = 0; k < totalBlocks; k++) {
				for(int i = 0; i < 8; i++) {
					for(int j = 0; j < 8; j++) {
						originalRBlocks[k][i][j] = tmpRArray[blockLengthIndex*8 + i][blockWidthIndex*8 + j];
						originalGBlocks[k][i][j] = tmpGArray[blockLengthIndex*8 + i][blockWidthIndex*8 + j];
						originalBBlocks[k][i][j] = tmpBArray[blockLengthIndex*8 + i][blockWidthIndex*8 + j];
					}
				}
				blockWidthIndex++;
				if(blockWidthIndex >= numBlocksWidth) {
					blockWidthIndex = 0;
					blockLengthIndex++;
				}
			}

			long initialStructuring = System.nanoTime() - startTime;
			startTime = System.nanoTime();

			int[][][] DCTRBlocks = new int[totalBlocks][8][8];
			int[][][] DCTGBlocks = new int[totalBlocks][8][8];
			int[][][] DCTBBlocks = new int[totalBlocks][8][8];

			for(int i = 0; i < totalBlocks; i++) {
				DCTRBlocks[i] = DCT(originalRBlocks[i]);
				DCTGBlocks[i] = DCT(originalGBlocks[i]);
				DCTBBlocks[i] = DCT(originalBBlocks[i]);
			}

			long dct = System.nanoTime() - startTime;
			startTime = System.nanoTime();

			int[][][] DCTRBlocksLF = new int[totalBlocks][2][2];
			int[][][] DCTGBlocksLF = new int[totalBlocks][2][2];
			int[][][] DCTBBlocksLF = new int[totalBlocks][2][2];

			for(int k = 0; k < totalBlocks; k++) {
				for(int i = 0; i < 2; i++) {
					for(int j = 0; j < 2; j++) {
						DCTRBlocksLF[k][i][j] = DCTRBlocks[k][i][j];
						DCTGBlocksLF[k][i][j] = DCTGBlocks[k][i][j];
						DCTBBlocksLF[k][i][j] = DCTBBlocks[k][i][j];
					}
				}
			}

			int[][] DCTRBlocksHF =  new int[totalBlocks][60];
			int[][] DCTGBlocksHF =  new int[totalBlocks][60];
			int[][] DCTBBlocksHF =  new int[totalBlocks][60];

			for(int k = 0; k < totalBlocks; k++) {
				int curr = 0;
				for(int i = 0; i < 8; i++) {
					for(int j = 0; j < 8; j++) {
						if( i > 1 || j > 1) {
							DCTRBlocksHF[k][curr] = DCTRBlocks[k][i][j];
							DCTGBlocksHF[k][curr] = DCTGBlocks[k][i][j];
							DCTBBlocksHF[k][curr] = DCTBBlocks[k][i][j];
							curr++;
						}
					}
				}
			}

			int[][] DCTRBlocksRL = new int[totalBlocks][];
			int[][] DCTGBlocksRL = new int[totalBlocks][];
			int[][] DCTBBlocksRL = new int[totalBlocks][];

			for(int i = 0; i < totalBlocks; i++) {
				DCTRBlocksRL[i] = RunLengthEncoding(DCTRBlocksHF[i]);
				DCTGBlocksRL[i] = RunLengthEncoding(DCTGBlocksHF[i]);
				DCTBBlocksRL[i] = RunLengthEncoding(DCTBBlocksHF[i]);
			}

			StringBuilder losslessR = new StringBuilder();
			StringBuilder losslessG = new StringBuilder();
			StringBuilder losslessB = new StringBuilder();

			for(int k = 0; k < totalBlocks; k++) {
				losslessR.append(IntArrayToTwoByteBinaryString(DCTRBlocksLF[k], 2, 2));
				losslessG.append(IntArrayToTwoByteBinaryString(DCTGBlocksLF[k], 2, 2));
				losslessB.append(IntArrayToTwoByteBinaryString(DCTBBlocksLF[k], 2, 2));
			}

			for(int k = 0; k < totalBlocks; k++) {
				losslessR.append(SignedIntArrayToBinaryString1D(DCTRBlocksRL[k]));
				losslessG.append(SignedIntArrayToBinaryString1D(DCTGBlocksRL[k]));
				losslessB.append(SignedIntArrayToBinaryString1D(DCTBBlocksRL[k]));
			}

			String uncompressedLosslessR = losslessR.toString();
			String uncompressedLosslessG = losslessG.toString();
			String uncompressedLosslessB = losslessB.toString();

			String gTag = IntToThreeBytes(48 + uncompressedLosslessR.length());
			String bTag = IntToThreeBytes(48 + uncompressedLosslessR.length() + uncompressedLosslessG.length());
			String uncompressedLosslessRGB = gTag + bTag + uncompressedLosslessR + uncompressedLosslessG + uncompressedLosslessB;


			double size = (int)imageLength * (int)imageWidth * 3;
			double uncompressedSize = uncompressedLosslessRGB.length()/8;
			double compressionRatio = size / uncompressedSize;

			LZW losslessColorsLZW = new LZW(uncompressedLosslessRGB, size, STATE);
			losslessColorsLZW.encode();

			long losslessEncoding = System.nanoTime() - startTime;
			startTime = System.nanoTime();

			String decodedLosslessColorsLZW = losslessColorsLZW.decode();

			long losslessLZWDecoding = System.nanoTime() - startTime;
			startTime = System.nanoTime();

			int totalLength = decodedLosslessColorsLZW.length();

			int rIndex = 48;

			String gTagSubstring = decodedLosslessColorsLZW.substring(0, 24);
			String bTagSubstring = decodedLosslessColorsLZW.substring(24, 48);

			int gIndex = BinaryStringToInt(gTagSubstring);
			int bIndex = BinaryStringToInt(bTagSubstring);

			String rSubstring = decodedLosslessColorsLZW.substring(rIndex, gIndex);
			String gSubstring = decodedLosslessColorsLZW.substring(gIndex, bIndex);
			String bSubstring = decodedLosslessColorsLZW.substring(bIndex, totalLength);

			int[][][] decodedDCTRBlocksLF = new int[totalBlocks][2][2];
			int[][][] decodedDCTGBlocksLF = new int[totalBlocks][2][2];
			int[][][] decodedDCTBBlocksLF = new int[totalBlocks][2][2];

			int startIndex = 0;
			int endIndex = 0;

			for(int k = 0; k < totalBlocks; k++) {
				startIndex = k*64;
				endIndex = startIndex + 64;
				decodedDCTRBlocksLF[k] = TwoByteBinaryStringToIntArray(rSubstring.substring(startIndex, endIndex), 2, 2);
				decodedDCTGBlocksLF[k] = TwoByteBinaryStringToIntArray(gSubstring.substring(startIndex, endIndex), 2, 2);
				decodedDCTBBlocksLF[k] = TwoByteBinaryStringToIntArray(bSubstring.substring(startIndex, endIndex), 2, 2);
			}


			startIndex = endIndex;
			int rStartIndex = startIndex;
			int gStartIndex = startIndex;
			int bStartIndex = startIndex;

			int rEndIndex, gEndIndex, bEndIndex;

			int rLength = 0;
			int gLength = 0;
			int bLength = 0;

			int[] rRunLengthIndices = new int[totalBlocks+1];
			int[] gRunLengthIndices = new int[totalBlocks+1];
			int[] bRunLengthIndices = new int[totalBlocks+1];
			int currRIndex = endIndex;
			int currGIndex = endIndex;
			int currBIndex = endIndex;

			int[] rRunLengthSizes = new int[totalBlocks];
			int[] gRunLengthSizes = new int[totalBlocks];
			int[] bRunLengthSizes = new int[totalBlocks];

			for(int k = 0; k < totalBlocks; k++) {
				rRunLengthIndices[k] = currRIndex;
				gRunLengthIndices[k] = currGIndex;
				bRunLengthIndices[k] = currBIndex;
				rLength = BinaryStringToInt(rSubstring.substring(currRIndex, currRIndex + 9));
				gLength = BinaryStringToInt(gSubstring.substring(currGIndex, currGIndex + 9));
				bLength = BinaryStringToInt(bSubstring.substring(currBIndex, currBIndex + 9));
				rRunLengthSizes[k] = rLength;
				gRunLengthSizes[k] = gLength;
				bRunLengthSizes[k] = bLength;
				currRIndex += (9*rLength + 9);
				currGIndex += (9*gLength + 9);
				currBIndex += (9*bLength + 9);
			}
			rRunLengthIndices[totalBlocks] = currRIndex;
			gRunLengthIndices[totalBlocks] = currGIndex;
			bRunLengthIndices[totalBlocks] = currBIndex;

			int[][] decodedDCTRBlocksRL = new int[totalBlocks][];
			int[][] decodedDCTGBlocksRL = new int[totalBlocks][];
			int[][] decodedDCTBBlocksRL = new int[totalBlocks][];

			for(int k = 0; k < totalBlocks; k++) {
				decodedDCTRBlocksRL[k] = SignedBinaryStringToIntArray1D(rSubstring.substring(rRunLengthIndices[k], rRunLengthIndices[k+1]), rRunLengthSizes[k]+1);
				decodedDCTGBlocksRL[k] = SignedBinaryStringToIntArray1D(gSubstring.substring(gRunLengthIndices[k], gRunLengthIndices[k+1]), gRunLengthSizes[k]+1);
				decodedDCTBBlocksRL[k] = SignedBinaryStringToIntArray1D(bSubstring.substring(bRunLengthIndices[k], bRunLengthIndices[k+1]), bRunLengthSizes[k]+1);
			}

			int[][] decodedDCTRBlocksHF =  new int[totalBlocks][60];
			int[][] decodedDCTGBlocksHF =  new int[totalBlocks][60];
			int[][] decodedDCTBBlocksHF =  new int[totalBlocks][60];

			for(int k = 0; k < totalBlocks; k++) {
				int rCurr = 0;
				int gCurr = 0;
				int bCurr = 0;
				for(int i = 1; i <= decodedDCTRBlocksRL[k][0]; i+=2) {
					int zerosToAdd = decodedDCTRBlocksRL[k][i];
					for(int j = 0; j < zerosToAdd; j++) {
						decodedDCTRBlocksHF[k][rCurr++] = 0;
					}
					decodedDCTRBlocksHF[k][rCurr++] = decodedDCTRBlocksRL[k][i+1];
				}
				for(int i = rCurr; i < 60; i++) {
					decodedDCTRBlocksHF[k][i] = 0;
				}
				for(int i = 1; i <= decodedDCTGBlocksRL[k][0]; i+=2) {
					int zerosToAdd = decodedDCTGBlocksRL[k][i];
					for(int j = 0; j < zerosToAdd; j++) {
						decodedDCTGBlocksHF[k][gCurr++] = 0;
					}
					decodedDCTGBlocksHF[k][gCurr++] = decodedDCTGBlocksRL[k][i+1];
				}
				for(int i = gCurr; i < 60; i++) {
					decodedDCTGBlocksHF[k][i] = 0;
				}
				for(int i = 1; i <= decodedDCTBBlocksRL[k][0]; i+=2) {
					int zerosToAdd = decodedDCTBBlocksRL[k][i];
					for(int j = 0; j < zerosToAdd; j++) {
						decodedDCTBBlocksHF[k][bCurr++] = 0;
					}
					decodedDCTBBlocksHF[k][bCurr++] = decodedDCTBBlocksRL[k][i+1];
				}
				for(int i = bCurr; i < 60; i++) {
					decodedDCTBBlocksHF[k][i] = 0;
				}
			}

			int[][][] decodedDCTRBlocks = new int[totalBlocks][8][8];
			int[][][] decodedDCTGBlocks = new int[totalBlocks][8][8];
			int[][][] decodedDCTBBlocks = new int[totalBlocks][8][8];

			for(int k = 0; k < totalBlocks; k++) {
				int rCurr = 0;
				int gCurr = 0;
				int bCurr = 0;
				for(int i = 0; i < 8; i++) {
					for(int j = 0; j < 8; j++) {
						if(i < 2 && j < 2) {
							decodedDCTRBlocks[k][i][j] = decodedDCTRBlocksLF[k][i][j];
							decodedDCTGBlocks[k][i][j] = decodedDCTGBlocksLF[k][i][j];
							decodedDCTBBlocks[k][i][j] = decodedDCTBBlocksLF[k][i][j];
						}
						else {
							decodedDCTRBlocks[k][i][j] = decodedDCTRBlocksHF[k][rCurr++];
							decodedDCTGBlocks[k][i][j] = decodedDCTGBlocksHF[k][gCurr++];
							decodedDCTBBlocks[k][i][j] = decodedDCTBBlocksHF[k][bCurr++];
						}
					}
				}
			}

			int[][][] finalRBlocks = new int[totalBlocks][8][8];
			int[][][] finalGBlocks = new int[totalBlocks][8][8];
			int[][][] finalBBlocks = new int[totalBlocks][8][8];

			for(int k = 0; k < totalBlocks; k++) {
				finalRBlocks[k] = inverseDCT(decodedDCTRBlocks[k]);
				finalGBlocks[k] = inverseDCT(decodedDCTGBlocks[k]);
				finalBBlocks[k] = inverseDCT(decodedDCTBBlocks[k]);
			}


			int[][] expandedRMatrix = new int[tmpImageLength][tmpImageWidth];
			int[][] expandedGMatrix = new int[tmpImageLength][tmpImageWidth];
			int[][] expandedBMatrix = new int[tmpImageLength][tmpImageWidth];
			blockWidthIndex = 0;
			blockLengthIndex = 0;

			for(int k = 0; k < totalBlocks; k++) {
				for(int i = 0; i < 8; i++) {
					for(int j = 0; j < 8; j++) {
						expandedRMatrix[blockLengthIndex*8 + i][blockWidthIndex*8 + j] = finalRBlocks[k][i][j];
						expandedGMatrix[blockLengthIndex*8 + i][blockWidthIndex*8 + j] = finalGBlocks[k][i][j];
						expandedBMatrix[blockLengthIndex*8 + i][blockWidthIndex*8 + j] = finalBBlocks[k][i][j];
					}
				}
				blockWidthIndex++;
				if(blockWidthIndex >= numBlocksWidth) {
					blockWidthIndex = 0;
					blockLengthIndex++;
				}
			}

			int[][] finalRMatrix = new int[(int)imageLength][(int)imageWidth];
			int[][] finalGMatrix = new int[(int)imageLength][(int)imageWidth];
			int[][] finalBMatrix = new int[(int)imageLength][(int)imageWidth];

			for(int i = 0; i < (int)imageLength; i++) {
				for(int j = 0; j < (int)imageWidth; j++) {
					finalRMatrix[i][j] = expandedRMatrix[i][j];
					finalGMatrix[i][j] = expandedGMatrix[i][j];
					finalBMatrix[i][j] = expandedBMatrix[i][j];
				}
			}

			long losslessDecoding = System.nanoTime() - startTime;

			for(int i = 0; i < (int)imageLength; i++) {
				for(int j = 0; j < (int)imageWidth; j++) {
					r = finalRMatrix[i][j];
					g = finalGMatrix[i][j];
					b = finalBMatrix[i][j];
					col = (r << 16) | (g << 8) | b;
					image2.setRGB(j, i, col);
				}
			}

			double currCompressionRatio = losslessColorsLZW.getCompressionRatio();

			String cr = "Compression ratio: " + currCompressionRatio;
			displayCompressionRatio.setText(cr);

			for(int i = 0; i < fileContents.length;i++) {
				compressedFileContents[i] = fileContents[i];
			}

			for (int i = 0; i < (int) imageLength; i++) {
				for (int j = 0; j < (int) imageWidth; j++) {
					compressedFileContents[(int) allOffsets[0] + (int) samplesPerPix*i* (int) imageWidth + (int) samplesPerPix*j] = (byte)(finalRMatrix[i][j] & 0xff);
					compressedFileContents[(int) allOffsets[0] + (int) samplesPerPix*i* (int) imageWidth + (int) samplesPerPix*j + 1] = (byte)(finalGMatrix[i][j] & 0xff);
					compressedFileContents[(int) allOffsets[0] + (int) samplesPerPix*i* (int) imageWidth + (int) samplesPerPix*j + 2] = (byte)(finalBMatrix[i][j] & 0xff);
				}
			}

			float total = (float)initialStructuring + (float)dct + (float)losslessEncoding + (float)losslessDecoding + (float)losslessLZWDecoding;
			System.out.println(file + " lossless initial structuring: " + (float)initialStructuring/total*100 + "%");
			System.out.println(file + " lossless DCT: " + (float)dct/total*100 + "%");
			System.out.println(file + " lossless encoding: " + (float)losslessEncoding/total*100 + "%");
			System.out.println(file + " lossless LZW decoding: " + (float)losslessLZWDecoding/total*100 + "%");
			System.out.println(file + " lossless reconstruction: " + (float)losslessDecoding/total*100 + "%");
			System.out.println();

		}
		else if (STATE % 3 == 2) {
			startTime = System.nanoTime();
			int tmpImageLength = (int)imageLength;
			int tmpImageWidth = (int)imageWidth;

			int initialSize = (int)imageLength*(int)imageWidth;

			while(tmpImageLength % 8 != 0) {
				tmpImageLength++;
			}
			while(tmpImageWidth % 8 != 0) {
				tmpImageWidth++;
			}

			int[][] tmpRArray = new int[tmpImageLength][tmpImageWidth];
			int[][] tmpGArray = new int[tmpImageLength][tmpImageWidth];
			int[][] tmpBArray = new int[tmpImageLength][tmpImageWidth];

			for(int i = 0; i < tmpImageLength; i++) {
				for(int j = 0; j < tmpImageWidth; j++) {
					if(i > (int)(imageLength-1) && j < (int)imageWidth) {
						tmpRArray[i][j] = rArray[(int)(imageLength-1)][j];
						tmpGArray[i][j] = gArray[(int)(imageLength-1)][j];
						tmpBArray[i][j] = bArray[(int)(imageLength-1)][j];
					}
					else if(i < (int)(imageLength) && j > (int)(imageWidth-1)) {
						tmpRArray[i][j] = rArray[i][(int)(imageWidth-1)];
						tmpGArray[i][j] = gArray[i][(int)(imageWidth-1)];
						tmpBArray[i][j] = bArray[i][(int)(imageWidth-1)];
					}
					else if(i > (int)(imageLength-1) && j > (int)(imageWidth-1)) {
						tmpRArray[i][j] = rArray[(int)(imageLength-1)][(int)(imageWidth-1)];
						tmpGArray[i][j] = gArray[(int)(imageLength-1)][(int)(imageWidth-1)];
						tmpBArray[i][j] = bArray[(int)(imageLength-1)][(int)(imageWidth-1)];
					}
					else {
						tmpRArray[i][j] = rArray[i][j];
						tmpGArray[i][j] = gArray[i][j];
						tmpBArray[i][j] = bArray[i][j];
					}
				}
			}

			int numBlocksLength = tmpImageLength / 8;
			int numBlocksWidth = tmpImageWidth / 8;
			int totalBlocks = numBlocksLength * numBlocksWidth;

			int[][][] originalRBlocks = new int[totalBlocks][8][8];
			int[][][] originalGBlocks = new int[totalBlocks][8][8];
			int[][][] originalBBlocks = new int[totalBlocks][8][8];

			int blockWidthIndex = 0;
			int blockLengthIndex = 0;

			for(int k = 0; k < totalBlocks; k++) {
				for(int i = 0; i < 8; i++) {
					for(int j = 0; j < 8; j++) {
						originalRBlocks[k][i][j] = tmpRArray[blockLengthIndex*8 + i][blockWidthIndex*8 + j];
						originalGBlocks[k][i][j] = tmpGArray[blockLengthIndex*8 + i][blockWidthIndex*8 + j];
						originalBBlocks[k][i][j] = tmpBArray[blockLengthIndex*8 + i][blockWidthIndex*8 + j];
					}
				}
				blockWidthIndex++;
				if(blockWidthIndex >= numBlocksWidth) {
					blockWidthIndex = 0;
					blockLengthIndex++;
				}
			}

			long initialStructuring = System.nanoTime() - startTime;
			startTime = System.nanoTime();

			int[][][] DCTRBlocks = new int[totalBlocks][8][8];
			int[][][] DCTGBlocks = new int[totalBlocks][8][8];
			int[][][] DCTBBlocks = new int[totalBlocks][8][8];

			for(int i = 0; i < totalBlocks; i++) {
				DCTRBlocks[i] = DCT(originalRBlocks[i]);
				DCTGBlocks[i] = DCT(originalGBlocks[i]);
				DCTBBlocks[i] = DCT(originalBBlocks[i]);
			}

			int[][][] qDCTRBlocks = new int[totalBlocks][8][8];
			int[][][] qDCTGBlocks = new int[totalBlocks][8][8];
			int[][][] qDCTBBlocks = new int[totalBlocks][8][8];
			int[][]qMatrix = new int[8][8];
			if(STATE == 2) {
				qMatrix = createQMatrix1();
			}
			else if(STATE == 5) {
				qMatrix = createQMatrix5();
			}
			else if(STATE == 8) {
				qMatrix = createQMatrix4();
			}
			else if(STATE == 11) {
				qMatrix = createQMatrix3();
			}

			float x, y;

			for(int k = 0; k < totalBlocks; k++) {
				for(int i = 0; i < 8; i++) {
					for(int j = 0; j < 8; j++) {
						x = (float)DCTRBlocks[k][i][j];
						y = (float)qMatrix[i][j];
						qDCTRBlocks[k][i][j] = Math.round(x/y);
						x = (float)DCTGBlocks[k][i][j];
						qDCTGBlocks[k][i][j] = Math.round(x/y);
						x = (float)DCTBBlocks[k][i][j];
						qDCTBBlocks[k][i][j] = Math.round(x/y);
					}
				}
			}

			long dctQ = System.nanoTime() - startTime;
			startTime = System.nanoTime();

			int[][][] qDCTRBlocksLF = new int[totalBlocks][2][2];
			int[][][] qDCTGBlocksLF = new int[totalBlocks][2][2];
			int[][][] qDCTBBlocksLF = new int[totalBlocks][2][2];

			for(int k = 0; k < totalBlocks; k++) {
				for(int i = 0; i < 2; i++) {
					for(int j = 0; j < 2; j++) {
						qDCTRBlocksLF[k][i][j] = qDCTRBlocks[k][i][j];
						qDCTGBlocksLF[k][i][j] = qDCTGBlocks[k][i][j];
						qDCTBBlocksLF[k][i][j] = qDCTBBlocks[k][i][j];
					}
				}
			}

			int[][] qDCTRBlocksHF =  new int[totalBlocks][60];
			int[][] qDCTGBlocksHF =  new int[totalBlocks][60];
			int[][] qDCTBBlocksHF =  new int[totalBlocks][60];

			for(int k = 0; k < totalBlocks; k++) {
				int curr = 0;
				for(int i = 0; i < 8; i++) {
					for(int j = 0; j < 8; j++) {
						if( i > 1 || j > 1) {
							qDCTRBlocksHF[k][curr] = qDCTRBlocks[k][i][j];
							qDCTGBlocksHF[k][curr] = qDCTGBlocks[k][i][j];
							qDCTBBlocksHF[k][curr] = qDCTBBlocks[k][i][j];
							curr++;
						}
					}
				}
			}

			int[][] qDCTRBlocksRL = new int[totalBlocks][];
			int[][] qDCTGBlocksRL = new int[totalBlocks][];
			int[][] qDCTBBlocksRL = new int[totalBlocks][];

			for(int i = 0; i < totalBlocks; i++) {
				qDCTRBlocksRL[i] = RunLengthEncoding(qDCTRBlocksHF[i]);
				qDCTGBlocksRL[i] = RunLengthEncoding(qDCTGBlocksHF[i]);
				qDCTBBlocksRL[i] = RunLengthEncoding(qDCTBBlocksHF[i]);
			}

			StringBuilder lossyR = new StringBuilder();
			StringBuilder lossyG = new StringBuilder();
			StringBuilder lossyB = new StringBuilder();

			for(int k = 0; k < totalBlocks; k++) {
				lossyR.append(IntArrayToTwoByteBinaryString(qDCTRBlocksLF[k], 2, 2));
				lossyG.append(IntArrayToTwoByteBinaryString(qDCTGBlocksLF[k], 2, 2));
				lossyB.append(IntArrayToTwoByteBinaryString(qDCTBBlocksLF[k], 2, 2));
			}

			for(int k = 0; k < totalBlocks; k++) {
				lossyR.append(SignedIntArrayToBinaryString1D(qDCTRBlocksRL[k]));
				lossyG.append(SignedIntArrayToBinaryString1D(qDCTGBlocksRL[k]));
				lossyB.append(SignedIntArrayToBinaryString1D(qDCTBBlocksRL[k]));
			}

			String uncompressedLossyR = lossyR.toString();
			String uncompressedLossyG = lossyG.toString();
			String uncompressedLossyB = lossyB.toString();

			String gTag = IntToThreeBytes(48 + uncompressedLossyR.length());
			String bTag = IntToThreeBytes(48 + uncompressedLossyR.length() + uncompressedLossyG.length());
			String uncompressedLossyRGB = gTag + bTag + uncompressedLossyR + uncompressedLossyG + uncompressedLossyB;


			double size = (int)imageLength * (int)imageWidth * 3;
			double uncompressedSize = uncompressedLossyRGB.length()/8;
			double compressionRatio = size / uncompressedSize;

			LZW lossyColorsLZW = new LZW(uncompressedLossyRGB, size, STATE);
			lossyColorsLZW.encode();

			long lossyEncoding = System.nanoTime() - startTime;
			startTime = System.nanoTime();

			String decodedLossyColorsLZW = lossyColorsLZW.decode();

			long lossyLZWDecoding = System.nanoTime() - startTime;
			startTime = System.nanoTime();


			int totalLength = decodedLossyColorsLZW.length();

			int rIndex = 48;

			String gTagSubstring = decodedLossyColorsLZW.substring(0, 24);
			String bTagSubstring = decodedLossyColorsLZW.substring(24, 48);

			int gIndex = BinaryStringToInt(gTagSubstring);
			int bIndex = BinaryStringToInt(bTagSubstring);

			String rSubstring = decodedLossyColorsLZW.substring(rIndex, gIndex);
			String gSubstring = decodedLossyColorsLZW.substring(gIndex, bIndex);
			String bSubstring = decodedLossyColorsLZW.substring(bIndex, totalLength);

			int[][][] decodedqDCTRBlocksLF = new int[totalBlocks][2][2];
			int[][][] decodedqDCTGBlocksLF = new int[totalBlocks][2][2];
			int[][][] decodedqDCTBBlocksLF = new int[totalBlocks][2][2];

			int startIndex = 0;
			int endIndex = 0;

			for(int k = 0; k < totalBlocks; k++) {
				startIndex = k*64;
				endIndex = startIndex + 64;
				decodedqDCTRBlocksLF[k] = TwoByteBinaryStringToIntArray(rSubstring.substring(startIndex, endIndex), 2, 2);
				decodedqDCTGBlocksLF[k] = TwoByteBinaryStringToIntArray(gSubstring.substring(startIndex, endIndex), 2, 2);
				decodedqDCTBBlocksLF[k] = TwoByteBinaryStringToIntArray(bSubstring.substring(startIndex, endIndex), 2, 2);
			}


			startIndex = endIndex;
			int rStartIndex = startIndex;
			int gStartIndex = startIndex;
			int bStartIndex = startIndex;

			int rEndIndex, gEndIndex, bEndIndex;

			int rLength = 0;
			int gLength = 0;
			int bLength = 0;

			int[] rRunLengthIndices = new int[totalBlocks+1];
			int[] gRunLengthIndices = new int[totalBlocks+1];
			int[] bRunLengthIndices = new int[totalBlocks+1];
			int currRIndex = endIndex;
			int currGIndex = endIndex;
			int currBIndex = endIndex;

			int[] rRunLengthSizes = new int[totalBlocks];
			int[] gRunLengthSizes = new int[totalBlocks];
			int[] bRunLengthSizes = new int[totalBlocks];

			for(int k = 0; k < totalBlocks; k++) {
				rRunLengthIndices[k] = currRIndex;
				gRunLengthIndices[k] = currGIndex;
				bRunLengthIndices[k] = currBIndex;
				rLength = BinaryStringToInt(rSubstring.substring(currRIndex, currRIndex + 9));
				gLength = BinaryStringToInt(gSubstring.substring(currGIndex, currGIndex + 9));
				bLength = BinaryStringToInt(bSubstring.substring(currBIndex, currBIndex + 9));
				rRunLengthSizes[k] = rLength;
				gRunLengthSizes[k] = gLength;
				bRunLengthSizes[k] = bLength;
				currRIndex += (9*rLength + 9);
				currGIndex += (9*gLength + 9);
				currBIndex += (9*bLength + 9);
			}
			rRunLengthIndices[totalBlocks] = currRIndex;
			gRunLengthIndices[totalBlocks] = currGIndex;
			bRunLengthIndices[totalBlocks] = currBIndex;

			int[][] decodedqDCTRBlocksRL = new int[totalBlocks][];
			int[][] decodedqDCTGBlocksRL = new int[totalBlocks][];
			int[][] decodedqDCTBBlocksRL = new int[totalBlocks][];

			for(int k = 0; k < totalBlocks; k++) {
				decodedqDCTRBlocksRL[k] = SignedBinaryStringToIntArray1D(rSubstring.substring(rRunLengthIndices[k], rRunLengthIndices[k+1]), rRunLengthSizes[k]+1);
				decodedqDCTGBlocksRL[k] = SignedBinaryStringToIntArray1D(gSubstring.substring(gRunLengthIndices[k], gRunLengthIndices[k+1]), gRunLengthSizes[k]+1);
				decodedqDCTBBlocksRL[k] = SignedBinaryStringToIntArray1D(bSubstring.substring(bRunLengthIndices[k], bRunLengthIndices[k+1]), bRunLengthSizes[k]+1);
			}

			int[][] decodedqDCTRBlocksHF =  new int[totalBlocks][60];
			int[][] decodedqDCTGBlocksHF =  new int[totalBlocks][60];
			int[][] decodedqDCTBBlocksHF =  new int[totalBlocks][60];

			for(int k = 0; k < totalBlocks; k++) {
				int rCurr = 0;
				int gCurr = 0;
				int bCurr = 0;
				for(int i = 1; i <= decodedqDCTRBlocksRL[k][0]; i+=2) {
					int zerosToAdd = decodedqDCTRBlocksRL[k][i];
					for(int j = 0; j < zerosToAdd; j++) {
						decodedqDCTRBlocksHF[k][rCurr++] = 0;
					}
					decodedqDCTRBlocksHF[k][rCurr++] = decodedqDCTRBlocksRL[k][i+1];
				}
				for(int i = rCurr; i < 60; i++) {
					decodedqDCTRBlocksHF[k][i] = 0;
				}
				for(int i = 1; i <= decodedqDCTGBlocksRL[k][0]; i+=2) {
					int zerosToAdd = decodedqDCTGBlocksRL[k][i];
					for(int j = 0; j < zerosToAdd; j++) {
						decodedqDCTGBlocksHF[k][gCurr++] = 0;
					}
					decodedqDCTGBlocksHF[k][gCurr++] = decodedqDCTGBlocksRL[k][i+1];
				}
				for(int i = gCurr; i < 60; i++) {
					decodedqDCTGBlocksHF[k][i] = 0;
				}
				for(int i = 1; i <= decodedqDCTBBlocksRL[k][0]; i+=2) {
					int zerosToAdd = decodedqDCTBBlocksRL[k][i];
					for(int j = 0; j < zerosToAdd; j++) {
						decodedqDCTBBlocksHF[k][bCurr++] = 0;
					}
					decodedqDCTBBlocksHF[k][bCurr++] = decodedqDCTBBlocksRL[k][i+1];
				}
				for(int i = bCurr; i < 60; i++) {
					decodedqDCTBBlocksHF[k][i] = 0;
				}
			}

			int[][][] decodedqDCTRBlocks = new int[totalBlocks][8][8];
			int[][][] decodedqDCTGBlocks = new int[totalBlocks][8][8];
			int[][][] decodedqDCTBBlocks = new int[totalBlocks][8][8];

			for(int k = 0; k < totalBlocks; k++) {
				int rCurr = 0;
				int gCurr = 0;
				int bCurr = 0;
				for(int i = 0; i < 8; i++) {
					for(int j = 0; j < 8; j++) {
						if(i < 2 && j < 2) {
							decodedqDCTRBlocks[k][i][j] = decodedqDCTRBlocksLF[k][i][j];
							decodedqDCTGBlocks[k][i][j] = decodedqDCTGBlocksLF[k][i][j];
							decodedqDCTBBlocks[k][i][j] = decodedqDCTBBlocksLF[k][i][j];
						}
						else {
							decodedqDCTRBlocks[k][i][j] = decodedqDCTRBlocksHF[k][rCurr++];
							decodedqDCTGBlocks[k][i][j] = decodedqDCTGBlocksHF[k][gCurr++];
							decodedqDCTBBlocks[k][i][j] = decodedqDCTBBlocksHF[k][bCurr++];
						}
					}
				}
			}

			int[][][] finalRBlocks = new int[totalBlocks][8][8];
			int[][][] finalGBlocks = new int[totalBlocks][8][8];
			int[][][] finalBBlocks = new int[totalBlocks][8][8];

			for(int k = 0; k < totalBlocks; k++) {
				finalRBlocks[k] = inverseDCT(decodedqDCTRBlocks[k]);
				finalGBlocks[k] = inverseDCT(decodedqDCTGBlocks[k]);
				finalBBlocks[k] = inverseDCT(decodedqDCTBBlocks[k]);
			}


			int[][] expandedRMatrix = new int[tmpImageLength][tmpImageWidth];
			int[][] expandedGMatrix = new int[tmpImageLength][tmpImageWidth];
			int[][] expandedBMatrix = new int[tmpImageLength][tmpImageWidth];
			blockWidthIndex = 0;
			blockLengthIndex = 0;

			for(int k = 0; k < totalBlocks; k++) {
				for(int i = 0; i < 8; i++) {
					for(int j = 0; j < 8; j++) {
						expandedRMatrix[blockLengthIndex*8 + i][blockWidthIndex*8 + j] = finalRBlocks[k][i][j];
						expandedGMatrix[blockLengthIndex*8 + i][blockWidthIndex*8 + j] = finalGBlocks[k][i][j];
						expandedBMatrix[blockLengthIndex*8 + i][blockWidthIndex*8 + j] = finalBBlocks[k][i][j];
					}
				}
				blockWidthIndex++;
				if(blockWidthIndex >= numBlocksWidth) {
					blockWidthIndex = 0;
					blockLengthIndex++;
				}
			}

			int[][] finalRMatrix = new int[(int)imageLength][(int)imageWidth];
			int[][] finalGMatrix = new int[(int)imageLength][(int)imageWidth];
			int[][] finalBMatrix = new int[(int)imageLength][(int)imageWidth];

			for(int i = 0; i < (int)imageLength; i++) {
				for(int j = 0; j < (int)imageWidth; j++) {
					finalRMatrix[i][j] = expandedRMatrix[i][j];
					finalGMatrix[i][j] = expandedGMatrix[i][j];
					finalBMatrix[i][j] = expandedBMatrix[i][j];
				}
			}

			long lossyDecoding = System.nanoTime() - startTime;

			for(int i = 0; i < (int)imageLength; i++) {
				for(int j = 0; j < (int)imageWidth; j++) {
					r = finalRMatrix[i][j];
					g = finalGMatrix[i][j];
					b = finalBMatrix[i][j];
					col = (r << 16) | (g << 8) | b;
					image2.setRGB(j, i, col);
				}
			}

			double currCompressionRatio = lossyColorsLZW.getCompressionRatio();

			String cr = "Compression ratio: " + currCompressionRatio;
			displayCompressionRatio.setText(cr);

			for(int i = 0; i < fileContents.length;i++) {
				compressedFileContents[i] = fileContents[i];
			}

			for (int i = 0; i < (int) imageLength; i++) {
				for (int j = 0; j < (int) imageWidth; j++) {
					compressedFileContents[(int) allOffsets[0] + (int) samplesPerPix*i* (int) imageWidth + (int) samplesPerPix*j] = (byte)(finalRMatrix[i][j] & 0xff);
					compressedFileContents[(int) allOffsets[0] + (int) samplesPerPix*i* (int) imageWidth + (int) samplesPerPix*j + 1] = (byte)(finalGMatrix[i][j] & 0xff);
					compressedFileContents[(int) allOffsets[0] + (int) samplesPerPix*i* (int) imageWidth + (int) samplesPerPix*j + 2] = (byte)(finalBMatrix[i][j] & 0xff);
				}
			}

			float total = (float)initialStructuring + (float)dctQ + (float)lossyEncoding + (float)lossyDecoding + (float)lossyLZWDecoding;
			System.out.println(file + " lossy 10 initial structuring: " + (float)initialStructuring/total*100 + "%");
			System.out.println(file + " lossy 10 DCT + Quantization: " + (float)dctQ/total*100 + "%");
			System.out.println(file + " lossy 10 encoding: " + (float)lossyEncoding/total*100 + "%");
			System.out.println(file + " lossy 10 LZW decoding: " + (float)lossyLZWDecoding/total*100 + "%");
			System.out.println(file + " lossy 10 reconstruction: " + (float)lossyDecoding/total*100 + "%");
			System.out.println();


		}
		else if (STATE % 3 == 0) {
			startTime = System.nanoTime();
			int tmpImageLength = (int)imageLength;
			int tmpImageWidth = (int)imageWidth;

			int initialSize = (int)imageLength*(int)imageWidth;

			while(tmpImageLength % 8 != 0) {
				tmpImageLength++;
			}
			while(tmpImageWidth % 8 != 0) {
				tmpImageWidth++;
			}

			int[][] tmpRArray = new int[tmpImageLength][tmpImageWidth];
			int[][] tmpGArray = new int[tmpImageLength][tmpImageWidth];
			int[][] tmpBArray = new int[tmpImageLength][tmpImageWidth];

			for(int i = 0; i < tmpImageLength; i++) {
				for(int j = 0; j < tmpImageWidth; j++) {
					if(i > (int)(imageLength-1) && j < (int)imageWidth) {
						tmpRArray[i][j] = rArray[(int)(imageLength-1)][j];
						tmpGArray[i][j] = gArray[(int)(imageLength-1)][j];
						tmpBArray[i][j] = bArray[(int)(imageLength-1)][j];
					}
					else if(i < (int)(imageLength) && j > (int)(imageWidth-1)) {
						tmpRArray[i][j] = rArray[i][(int)(imageWidth-1)];
						tmpGArray[i][j] = gArray[i][(int)(imageWidth-1)];
						tmpBArray[i][j] = bArray[i][(int)(imageWidth-1)];
					}
					else if(i > (int)(imageLength-1) && j > (int)(imageWidth-1)) {
						tmpRArray[i][j] = rArray[(int)(imageLength-1)][(int)(imageWidth-1)];
						tmpGArray[i][j] = gArray[(int)(imageLength-1)][(int)(imageWidth-1)];
						tmpBArray[i][j] = bArray[(int)(imageLength-1)][(int)(imageWidth-1)];
					}
					else {
						tmpRArray[i][j] = rArray[i][j];
						tmpGArray[i][j] = gArray[i][j];
						tmpBArray[i][j] = bArray[i][j];
					}
				}
			}

			int numBlocksLength = tmpImageLength / 8;
			int numBlocksWidth = tmpImageWidth / 8;
			int totalBlocks = numBlocksLength * numBlocksWidth;

			int[][][] originalRBlocks = new int[totalBlocks][8][8];
			int[][][] originalGBlocks = new int[totalBlocks][8][8];
			int[][][] originalBBlocks = new int[totalBlocks][8][8];

			int blockWidthIndex = 0;
			int blockLengthIndex = 0;

			for(int k = 0; k < totalBlocks; k++) {
				for(int i = 0; i < 8; i++) {
					for(int j = 0; j < 8; j++) {
						originalRBlocks[k][i][j] = tmpRArray[blockLengthIndex*8 + i][blockWidthIndex*8 + j];
						originalGBlocks[k][i][j] = tmpGArray[blockLengthIndex*8 + i][blockWidthIndex*8 + j];
						originalBBlocks[k][i][j] = tmpBArray[blockLengthIndex*8 + i][blockWidthIndex*8 + j];
					}
				}
				blockWidthIndex++;
				if(blockWidthIndex >= numBlocksWidth) {
					blockWidthIndex = 0;
					blockLengthIndex++;
				}
			}

			long initialStructuring = System.nanoTime() - startTime;
			startTime = System.nanoTime();

			int[][][] DCTRBlocks = new int[totalBlocks][8][8];
			int[][][] DCTGBlocks = new int[totalBlocks][8][8];
			int[][][] DCTBBlocks = new int[totalBlocks][8][8];

			for(int i = 0; i < totalBlocks; i++) {
				DCTRBlocks[i] = DCT(originalRBlocks[i]);
				DCTGBlocks[i] = DCT(originalGBlocks[i]);
				DCTBBlocks[i] = DCT(originalBBlocks[i]);
			}

			int[][][] qDCTRBlocks = new int[totalBlocks][8][8];
			int[][][] qDCTGBlocks = new int[totalBlocks][8][8];
			int[][][] qDCTBBlocks = new int[totalBlocks][8][8];
			int[][] qMatrix = new int[8][8];
			if(STATE == 3) {
				qMatrix = createQMatrix3();
			}
			else if(STATE == 6) {
				qMatrix = createQMatrix7();
			}
			else if(STATE == 9) {
				qMatrix = createQMatrix5();
			}
			else if(STATE == 12) {
				qMatrix = createQMatrix6();
			}

			float x, y;

			for(int k = 0; k < totalBlocks; k++) {
				for(int i = 0; i < 8; i++) {
					for(int j = 0; j < 8; j++) {
						x = (float)DCTRBlocks[k][i][j];
						y = (float)qMatrix[i][j];
						qDCTRBlocks[k][i][j] = Math.round(x/y);
						x = (float)DCTGBlocks[k][i][j];
						qDCTGBlocks[k][i][j] = Math.round(x/y);
						x = (float)DCTBBlocks[k][i][j];
						qDCTBBlocks[k][i][j] = Math.round(x/y);
					}
				}
			}

			long dctQ = System.nanoTime() - startTime;
			startTime = System.nanoTime();

			int[][][] qDCTRBlocksLF = new int[totalBlocks][2][2];
			int[][][] qDCTGBlocksLF = new int[totalBlocks][2][2];
			int[][][] qDCTBBlocksLF = new int[totalBlocks][2][2];

			for(int k = 0; k < totalBlocks; k++) {
				for(int i = 0; i < 2; i++) {
					for(int j = 0; j < 2; j++) {
						qDCTRBlocksLF[k][i][j] = qDCTRBlocks[k][i][j];
						qDCTGBlocksLF[k][i][j] = qDCTGBlocks[k][i][j];
						qDCTBBlocksLF[k][i][j] = qDCTBBlocks[k][i][j];
					}
				}
			}

			int[][] qDCTRBlocksHF =  new int[totalBlocks][60];
			int[][] qDCTGBlocksHF =  new int[totalBlocks][60];
			int[][] qDCTBBlocksHF =  new int[totalBlocks][60];

			for(int k = 0; k < totalBlocks; k++) {
				int curr = 0;
				for(int i = 0; i < 8; i++) {
					for(int j = 0; j < 8; j++) {
						if( i > 1 || j > 1) {
							qDCTRBlocksHF[k][curr] = qDCTRBlocks[k][i][j];
							qDCTGBlocksHF[k][curr] = qDCTGBlocks[k][i][j];
							qDCTBBlocksHF[k][curr] = qDCTBBlocks[k][i][j];
							curr++;
						}
					}
				}
			}

			int[][] qDCTRBlocksRL = new int[totalBlocks][];
			int[][] qDCTGBlocksRL = new int[totalBlocks][];
			int[][] qDCTBBlocksRL = new int[totalBlocks][];

			for(int i = 0; i < totalBlocks; i++) {
				qDCTRBlocksRL[i] = RunLengthEncoding(qDCTRBlocksHF[i]);
				qDCTGBlocksRL[i] = RunLengthEncoding(qDCTGBlocksHF[i]);
				qDCTBBlocksRL[i] = RunLengthEncoding(qDCTBBlocksHF[i]);
			}

			StringBuilder lossyR = new StringBuilder();
			StringBuilder lossyG = new StringBuilder();
			StringBuilder lossyB = new StringBuilder();

			for(int k = 0; k < totalBlocks; k++) {
				lossyR.append(IntArrayToTwoByteBinaryString(qDCTRBlocksLF[k], 2, 2));
				lossyG.append(IntArrayToTwoByteBinaryString(qDCTGBlocksLF[k], 2, 2));
				lossyB.append(IntArrayToTwoByteBinaryString(qDCTBBlocksLF[k], 2, 2));
			}

			for(int k = 0; k < totalBlocks; k++) {
				lossyR.append(SignedIntArrayToBinaryString1D(qDCTRBlocksRL[k]));
				lossyG.append(SignedIntArrayToBinaryString1D(qDCTGBlocksRL[k]));
				lossyB.append(SignedIntArrayToBinaryString1D(qDCTBBlocksRL[k]));
			}

			String uncompressedLossyR = lossyR.toString();
			String uncompressedLossyG = lossyG.toString();
			String uncompressedLossyB = lossyB.toString();

			String gTag = IntToThreeBytes(48 + uncompressedLossyR.length());
			String bTag = IntToThreeBytes(48 + uncompressedLossyR.length() + uncompressedLossyG.length());
			String uncompressedLossyRGB = gTag + bTag + uncompressedLossyR + uncompressedLossyG + uncompressedLossyB;


			double size = (int)imageLength * (int)imageWidth * 3;
			double uncompressedSize = uncompressedLossyRGB.length()/8;
			double compressionRatio = size / uncompressedSize;

			LZW lossyColorsLZW = new LZW(uncompressedLossyRGB, size, STATE);
			lossyColorsLZW.encode();

			long lossyEncoding = System.nanoTime() - startTime;
			startTime = System.nanoTime();

			String decodedLossyColorsLZW = lossyColorsLZW.decode();

			long lossyLZWDecoding = System.nanoTime() - startTime;
			startTime = System.nanoTime();

			int totalLength = decodedLossyColorsLZW.length();

			int rIndex = 48;

			String gTagSubstring = decodedLossyColorsLZW.substring(0, 24);
			String bTagSubstring = decodedLossyColorsLZW.substring(24, 48);

			int gIndex = BinaryStringToInt(gTagSubstring);
			int bIndex = BinaryStringToInt(bTagSubstring);

			String rSubstring = decodedLossyColorsLZW.substring(rIndex, gIndex);
			String gSubstring = decodedLossyColorsLZW.substring(gIndex, bIndex);
			String bSubstring = decodedLossyColorsLZW.substring(bIndex, totalLength);

			int[][][] decodedqDCTRBlocksLF = new int[totalBlocks][2][2];
			int[][][] decodedqDCTGBlocksLF = new int[totalBlocks][2][2];
			int[][][] decodedqDCTBBlocksLF = new int[totalBlocks][2][2];

			int startIndex = 0;
			int endIndex = 0;

			for(int k = 0; k < totalBlocks; k++) {
				startIndex = k*64;
				endIndex = startIndex + 64;
				decodedqDCTRBlocksLF[k] = TwoByteBinaryStringToIntArray(rSubstring.substring(startIndex, endIndex), 2, 2);
				decodedqDCTGBlocksLF[k] = TwoByteBinaryStringToIntArray(gSubstring.substring(startIndex, endIndex), 2, 2);
				decodedqDCTBBlocksLF[k] = TwoByteBinaryStringToIntArray(bSubstring.substring(startIndex, endIndex), 2, 2);
			}

			startIndex = endIndex;
			int rStartIndex = startIndex;
			int gStartIndex = startIndex;
			int bStartIndex = startIndex;

			int rEndIndex, gEndIndex, bEndIndex;

			int rLength = 0;
			int gLength = 0;
			int bLength = 0;

			int[] rRunLengthIndices = new int[totalBlocks+1];
			int[] gRunLengthIndices = new int[totalBlocks+1];
			int[] bRunLengthIndices = new int[totalBlocks+1];
			int currRIndex = endIndex;
			int currGIndex = endIndex;
			int currBIndex = endIndex;

			int[] rRunLengthSizes = new int[totalBlocks];
			int[] gRunLengthSizes = new int[totalBlocks];
			int[] bRunLengthSizes = new int[totalBlocks];

			for(int k = 0; k < totalBlocks; k++) {
				rRunLengthIndices[k] = currRIndex;
				gRunLengthIndices[k] = currGIndex;
				bRunLengthIndices[k] = currBIndex;
				rLength = BinaryStringToInt(rSubstring.substring(currRIndex, currRIndex + 9));
				gLength = BinaryStringToInt(gSubstring.substring(currGIndex, currGIndex + 9));
				bLength = BinaryStringToInt(bSubstring.substring(currBIndex, currBIndex + 9));
				rRunLengthSizes[k] = rLength;
				gRunLengthSizes[k] = gLength;
				bRunLengthSizes[k] = bLength;
				currRIndex += (9*rLength + 9);
				currGIndex += (9*gLength + 9);
				currBIndex += (9*bLength + 9);
			}
			rRunLengthIndices[totalBlocks] = currRIndex;
			gRunLengthIndices[totalBlocks] = currGIndex;
			bRunLengthIndices[totalBlocks] = currBIndex;

			int[][] decodedqDCTRBlocksRL = new int[totalBlocks][];
			int[][] decodedqDCTGBlocksRL = new int[totalBlocks][];
			int[][] decodedqDCTBBlocksRL = new int[totalBlocks][];

			for(int k = 0; k < totalBlocks; k++) {
				decodedqDCTRBlocksRL[k] = SignedBinaryStringToIntArray1D(rSubstring.substring(rRunLengthIndices[k], rRunLengthIndices[k+1]), rRunLengthSizes[k]+1);
				decodedqDCTGBlocksRL[k] = SignedBinaryStringToIntArray1D(gSubstring.substring(gRunLengthIndices[k], gRunLengthIndices[k+1]), gRunLengthSizes[k]+1);
				decodedqDCTBBlocksRL[k] = SignedBinaryStringToIntArray1D(bSubstring.substring(bRunLengthIndices[k], bRunLengthIndices[k+1]), bRunLengthSizes[k]+1);
			}

			int[][] decodedqDCTRBlocksHF =  new int[totalBlocks][60];
			int[][] decodedqDCTGBlocksHF =  new int[totalBlocks][60];
			int[][] decodedqDCTBBlocksHF =  new int[totalBlocks][60];

			for(int k = 0; k < totalBlocks; k++) {
				int rCurr = 0;
				int gCurr = 0;
				int bCurr = 0;
				for(int i = 1; i <= decodedqDCTRBlocksRL[k][0]; i+=2) {
					int zerosToAdd = decodedqDCTRBlocksRL[k][i];
					for(int j = 0; j < zerosToAdd; j++) {
						decodedqDCTRBlocksHF[k][rCurr++] = 0;
					}
					decodedqDCTRBlocksHF[k][rCurr++] = decodedqDCTRBlocksRL[k][i+1];
				}
				for(int i = rCurr; i < 60; i++) {
					decodedqDCTRBlocksHF[k][i] = 0;
				}
				for(int i = 1; i <= decodedqDCTGBlocksRL[k][0]; i+=2) {
					int zerosToAdd = decodedqDCTGBlocksRL[k][i];
					for(int j = 0; j < zerosToAdd; j++) {
						decodedqDCTGBlocksHF[k][gCurr++] = 0;
					}
					decodedqDCTGBlocksHF[k][gCurr++] = decodedqDCTGBlocksRL[k][i+1];
				}
				for(int i = gCurr; i < 60; i++) {
					decodedqDCTGBlocksHF[k][i] = 0;
				}
				for(int i = 1; i <= decodedqDCTBBlocksRL[k][0]; i+=2) {
					int zerosToAdd = decodedqDCTBBlocksRL[k][i];
					for(int j = 0; j < zerosToAdd; j++) {
						decodedqDCTBBlocksHF[k][bCurr++] = 0;
					}
					decodedqDCTBBlocksHF[k][bCurr++] = decodedqDCTBBlocksRL[k][i+1];
				}
				for(int i = bCurr; i < 60; i++) {
					decodedqDCTBBlocksHF[k][i] = 0;
				}
			}

			int[][][] decodedqDCTRBlocks = new int[totalBlocks][8][8];
			int[][][] decodedqDCTGBlocks = new int[totalBlocks][8][8];
			int[][][] decodedqDCTBBlocks = new int[totalBlocks][8][8];

			for(int k = 0; k < totalBlocks; k++) {
				int rCurr = 0;
				int gCurr = 0;
				int bCurr = 0;
				for(int i = 0; i < 8; i++) {
					for(int j = 0; j < 8; j++) {
						if(i < 2 && j < 2) {
							decodedqDCTRBlocks[k][i][j] = decodedqDCTRBlocksLF[k][i][j];
							decodedqDCTGBlocks[k][i][j] = decodedqDCTGBlocksLF[k][i][j];
							decodedqDCTBBlocks[k][i][j] = decodedqDCTBBlocksLF[k][i][j];
						}
						else {
							decodedqDCTRBlocks[k][i][j] = decodedqDCTRBlocksHF[k][rCurr++];
							decodedqDCTGBlocks[k][i][j] = decodedqDCTGBlocksHF[k][gCurr++];
							decodedqDCTBBlocks[k][i][j] = decodedqDCTBBlocksHF[k][bCurr++];
						}
					}
				}
			}

			int[][][] finalRBlocks = new int[totalBlocks][8][8];
			int[][][] finalGBlocks = new int[totalBlocks][8][8];
			int[][][] finalBBlocks = new int[totalBlocks][8][8];

			for(int k = 0; k < totalBlocks; k++) {
				finalRBlocks[k] = inverseDCT(decodedqDCTRBlocks[k]);
				finalGBlocks[k] = inverseDCT(decodedqDCTGBlocks[k]);
				finalBBlocks[k] = inverseDCT(decodedqDCTBBlocks[k]);
			}

			int[][] expandedRMatrix = new int[tmpImageLength][tmpImageWidth];
			int[][] expandedGMatrix = new int[tmpImageLength][tmpImageWidth];
			int[][] expandedBMatrix = new int[tmpImageLength][tmpImageWidth];
			blockWidthIndex = 0;
			blockLengthIndex = 0;

			for(int k = 0; k < totalBlocks; k++) {
				for(int i = 0; i < 8; i++) {
					for(int j = 0; j < 8; j++) {
						expandedRMatrix[blockLengthIndex*8 + i][blockWidthIndex*8 + j] = finalRBlocks[k][i][j];
						expandedGMatrix[blockLengthIndex*8 + i][blockWidthIndex*8 + j] = finalGBlocks[k][i][j];
						expandedBMatrix[blockLengthIndex*8 + i][blockWidthIndex*8 + j] = finalBBlocks[k][i][j];
					}
				}
				blockWidthIndex++;
				if(blockWidthIndex >= numBlocksWidth) {
					blockWidthIndex = 0;
					blockLengthIndex++;
				}
			}

			int[][] finalRMatrix = new int[(int)imageLength][(int)imageWidth];
			int[][] finalGMatrix = new int[(int)imageLength][(int)imageWidth];
			int[][] finalBMatrix = new int[(int)imageLength][(int)imageWidth];

			for(int i = 0; i < (int)imageLength; i++) {
				for(int j = 0; j < (int)imageWidth; j++) {
					finalRMatrix[i][j] = expandedRMatrix[i][j];
					finalGMatrix[i][j] = expandedGMatrix[i][j];
					finalBMatrix[i][j] = expandedBMatrix[i][j];
				}
			}

			long lossyDecoding = System.nanoTime() - startTime;
			startTime = System.nanoTime();

			for(int i = 0; i < (int)imageLength; i++) {
				for(int j = 0; j < (int)imageWidth; j++) {
					r = finalRMatrix[i][j];
					g = finalGMatrix[i][j];
					b = finalBMatrix[i][j];
					col = (r << 16) | (g << 8) | b;
					image2.setRGB(j, i, col);
				}
			}

			double currCompressionRatio = lossyColorsLZW.getCompressionRatio();

			String cr = "Compression ratio: " + currCompressionRatio;
			displayCompressionRatio.setText(cr);

			for(int i = 0; i < fileContents.length;i++) {
				compressedFileContents[i] = fileContents[i];
			}

			for (int i = 0; i < (int) imageLength; i++) {
				for (int j = 0; j < (int) imageWidth; j++) {
					compressedFileContents[(int) allOffsets[0] + (int) samplesPerPix*i* (int) imageWidth + (int) samplesPerPix*j] = (byte)(finalRMatrix[i][j] & 0xff);
					compressedFileContents[(int) allOffsets[0] + (int) samplesPerPix*i* (int) imageWidth + (int) samplesPerPix*j + 1] = (byte)(finalGMatrix[i][j] & 0xff);
					compressedFileContents[(int) allOffsets[0] + (int) samplesPerPix*i* (int) imageWidth + (int) samplesPerPix*j + 2] = (byte)(finalBMatrix[i][j] & 0xff);
				}
			}

			float total = (float)initialStructuring + (float)dctQ + (float)lossyEncoding + (float)lossyDecoding + (float)lossyLZWDecoding;
			System.out.println(file + " lossy 20 initial structuring: " + (float)initialStructuring/total*100 + "%");
			System.out.println(file + " lossy 20 DCT + Quantization: " + (float)dctQ/total*100 + "%");
			System.out.println(file + " lossy 20 encoding: " + (float)lossyEncoding/total*100 + "%");
			System.out.println(file + " lossy 20 LZW decoding: " + (float)lossyLZWDecoding/total*100 + "%");
			System.out.println(file + " lossy 20 reconstruction: " + (float)lossyDecoding/total*100 + "%");
			System.out.println();

		}

		picture.setIcon(new ImageIcon(image1));
		picture2.setIcon(new ImageIcon(image2));


		STATE++;
		if(STATE == 4) {
			STATE = 1;
		}
		if(STATE == 7) {
			STATE = 4;
		}
		if(STATE == 10) {
			STATE = 7;
		}
		if(STATE == 13) {
			STATE = 10;
		}

	}
}

class LZW {
	String input;
	String output;
	Hashtable<String, String> dictionary;
	double compressionRatio;
	int currState;
	double originalSize;
	public LZW(String i, double z, int s) {
		input = i;
		dictionary = new Hashtable<String, String>();
		dictionary.put("0", "0");
		dictionary.put("1", "1");
		output = "Nothing encoded yet";
		compressionRatio = 0;
		currState = s;
		originalSize = z;
	}

	public double getCompressionRatio() {
		return compressionRatio;
	}
	public void encode() {
		int inputLength = input.length();
    String s = ""; String c = "";
    StringBuilder sbOutput = new StringBuilder();
    if(inputLength > 0) {
      char tmp = input.charAt(0);
      s = Character.toString(tmp);
    }

    int i = 1;
    int newCode = 2;

    while(i < inputLength) {
      char tmp = input.charAt(i++);
      c = Character.toString(tmp);

      StringBuilder sbInput = new StringBuilder();
      sbInput.append(s);
      sbInput.append(c);
      String newSC = sbInput.toString();

      if(dictionary.containsKey(newSC)) {
        s = newSC;
      }
      else {
        sbOutput.append(dictionary.get(s));
        sbOutput.append(" ");
        String sNewCode = Integer.toString(newCode);
        dictionary.put(newSC, sNewCode);
        newCode++;
        s = c;
      }
    }

    sbOutput.append(dictionary.get(s));

    String myOutput = sbOutput.toString();
		output = myOutput;
		double compressedLength = (double)myOutput.length()/8;
		compressionRatio = originalSize/compressedLength;
		//System.out.println("original image size is " + originalSize + " bytes");
		//System.out.println("final compressed size length is " + compressedLength + " bytes");
		//System.out.println("final compression ratio is " + compressionRatio);


	}
	public String decode() {
		dictionary = new Hashtable<String, String>();
		dictionary.put("0", "0");
		dictionary.put("1", "1");

		String decodedString = "";

		String[] outputCodes = output.split(" ");
		int numCodes = outputCodes.length;

		int outputLength = output.length();
		StringBuilder sbDecoded = new StringBuilder();

		String entry = new String();
		String prevCode = new String();
		String currCode = new String();
		String sNewCode = new String();
		int newCode = 2;

		prevCode = outputCodes[0];
		sbDecoded.append(dictionary.get(prevCode));

		for(int i = 1; i < numCodes; i++) {
			StringBuilder sbOutput = new StringBuilder();
			currCode = outputCodes[i];
			if(dictionary.containsKey(currCode)) {
				entry = dictionary.get(currCode);
				sbDecoded.append(entry);
				char ch = entry.charAt(0);
				sNewCode = Integer.toString(newCode);
				newCode++;
				sbOutput.append(dictionary.get(prevCode));
				sbOutput.append(ch);
				String newDictEntry = sbOutput.toString();
				dictionary.put(sNewCode, newDictEntry);
				prevCode = currCode;
			}
			else {
				sNewCode = Integer.toString(newCode);
				newCode++;
				sbOutput.append(dictionary.get(prevCode));
				sbOutput.append(dictionary.get(prevCode).charAt(0));
				String newDictEntry = sbOutput.toString();
				dictionary.put(sNewCode, newDictEntry);
				entry = dictionary.get(currCode);
				sbDecoded.append(entry);
				prevCode = currCode;
			}

		}

		decodedString = sbDecoded.toString();

		return decodedString;
	}
}
