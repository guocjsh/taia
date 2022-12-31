package io.github.guocjsh;

import cn.hutool.core.lang.ConsoleTable;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;

import java.util.Set;

public class taia {

	public static final String AUTHOR = "iron.guo";

	private taia() {
	}

	public static Set<Class<?>> getAllUtils() {
		return ClassUtil.scanPackage("io.github.guocjsh",
				(clazz) -> (false == clazz.isInterface()) && StrUtil.endWith(clazz.getSimpleName(), "Util"));
	}


	public static void printAllUtils() {
		final Set<Class<?>> allUtils = getAllUtils();
		final ConsoleTable consoleTable = ConsoleTable.create().addHeader("工具类名", "所在包");
		for (Class<?> clazz : allUtils) {
			consoleTable.addBody(clazz.getSimpleName(), clazz.getPackage().getName());
		}
		consoleTable.print();
	}
}