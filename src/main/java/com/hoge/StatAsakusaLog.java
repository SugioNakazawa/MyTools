package com.hoge;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

/**
 * Asakusaバッチログを読み込んでIOごとのレコード数、サイズを出力。
 * 
 * @author nakazawasugio
 *
 */
public class StatAsakusaLog {

	static private Logger logger = (Logger) LoggerFactory.getLogger(StatAsakusaLog.class);

	String batchName;
	List<String[]> statRec;
	NumberFormat nf;

	public static void main(String[] args) throws IOException {
//		String[] a = { "/Users/nakazawasugio/Downloads/バッチ実行ログ(ノーチラス)/BTSE007/BTSE007-sh.log" };
//		args = a;
		if (args.length != 1) {
			throw new IllegalArgumentException("Usage StatAsakusaLog filepath.");
		}
		File file = new File(args[0]);
		if (!file.isFile()) {
			throw new IllegalArgumentException(args[0] + " is not File.");
		}
		StatAsakusaLog obj = new StatAsakusaLog();
		obj.exec(file);
	}

	public StatAsakusaLog() {
		statRec = new ArrayList<String[]>();
		nf = NumberFormat.getInstance();
	}

	private void exec(File file) throws IOException {
		// 全レコード読み込み
		List<String> lines = readAllLines(file);
		// バッチ名
		getBatchName(lines);
	}

	private void getBatchName(List<String> lines) {
		boolean inTarget = false;
		String prevLine = "";
		String jobName = "";
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			if (!inTarget) {
				if (line.startsWith("Starting YAESS")) {
					inTarget = true;
				}
			} else if (this.batchName == null) {
				String[] cols = line.trim().split(":");
				if (cols[0].equals("Batch ID")) {
					logger.debug("batchId=" + cols[1].trim());
					this.batchName = cols[1].trim();
				}
			} else {
				// バッチあり
				String[] cols = line.trim().split(" +");
				if (cols.length > 7) {
					if (cols[2].equals("Direct") && (cols[3].equals("I/O"))) {
						if (cols[5].equals("input:")) {
							jobName = prevLine.split(" +")[6].trim();
						}
//						logger.debug("num=" + Integer.parseInt(cols[6]));
						for (int j = 0; j < Integer.parseInt(cols[6]); j++) {
							String dataName = lines.get(++i).split(" +")[2].trim();
							try {
								long rec = (long) nf.parse(lines.get(++i).split(" +")[6]);
								long size = (long) nf.parse(lines.get(++i).split(" +")[7]);
								System.out.println(this.batchName + "\t" + jobName + "\t" + cols[5] + "\t" + dataName
										+ "\t" + rec + "\t" + size);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								logger.debug("line=" + lines.get(i));
								e.printStackTrace();
							}
						}
						// TOtalをスキップ
						i += 3;
					}
				}
			}
			prevLine = line;
		}
		if (this.batchName == null) {
			throw new RuntimeException("not found batch name.");
		}
	}

	private List<String> readAllLines(File file) throws IOException {
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String data;
		List<String> lines = new ArrayList<String>();
		while ((data = br.readLine()) != null) {
//			System.out.println(data);
			lines.add(data);
		}
		br.close();
		return lines;
	}
}
