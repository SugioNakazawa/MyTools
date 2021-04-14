package com.hoge;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Sample {

	public static void main(String[] args) {
		String s = "漢字名称タイトルサンプル";
		Random rnd = new Random();
		System.out.println(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());

		List<Hoge> strList = new ArrayList<Hoge>();
		for (int i = 0; i < 50000000; i++) {
			strList.add(
					new Hoge(
							new String(Integer.toString(rnd.nextInt(100000)) + s)
							)
					);
		}
		System.out.println(s);
		System.out.println(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
	}
}
