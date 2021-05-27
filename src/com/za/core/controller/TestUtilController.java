package com.za.core.controller;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rs.core.za.testUtil.annotation.ZA_InterfaceNotes;
import com.rs.core.za.testUtil.annotation.ZA_InterfaceNotesParameter;
import com.rs.core.za.testUtil.entity.ZAInterfaceNotesEntity;
import com.rs.core.za.testUtil.entity.ZAInterfaceNotesSimpleEntity;
import com.rs.core.za.testUtil.util.ZACreateHtml;

@Controller
@RequestMapping("/testUtil")
public class TestUtilController {
	private static final String host = "http://localhost:8080/ZA_DEMO/";
	private static final String[] CLASS_PACKAGES = new String[] { "com.za.core.controller" };// 控制器的包名

	/**
	 * 通过JAVA的反射加载控制器类(class)
	 */
	private List<Method> filterInterface(Class<?> controller) throws ClassNotFoundException {
		List<Method> interfaces = new ArrayList<Method>();// 接口的方法集合
		try {
			// 反射获取某个类中的方法对象数组 Method是方法对象，也是接口
			Method[] ms = controller.getMethods();
			for (Method method : ms) {
				// 取方法注解，如果有@ZA_InterfaceNotes注解意味着是一个接口
				if (method.isAnnotationPresent(ZA_InterfaceNotes.class)) {
					interfaces.add(method);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return interfaces;
	}

	/**
	 * 判断名称是否带有$符号,是否是后缀.class
	 */
	private boolean checkClassName(File file) {
		return file.getName().indexOf("$") < 0 && file.exists() && file.isFile()
				&& file.getPath().indexOf(".class") > -1;
	}

	/**
	 * 获取控制器在WEB容器中的路径
	 */
	private File getControllerPath(HttpServletRequest request, String packageName) {
		// 获取WEB项目在WEB容器的根路径
		String basePath = request.getSession().getServletContext().getRealPath("/");
		String separator = System.getProperty("file.separator");// 获取文件分隔符
		// 因为JAVA语言编译后会将.class文件输出到这，所以需要手动拼接定位到该文件夹
		String classFolderPath = "WEB-INF" + separator + "classes";
		if (separator.equals("\\")) {
			return new File(
					basePath + separator + classFolderPath + separator + (packageName.replaceAll("\\.", "\\\\")));
		} else {
			return new File(
					basePath + separator + classFolderPath + separator + (packageName.replaceAll("\\.", separator)));
		}
	}

	/**
	 * 输出接口文本到页面(返回HTML页面)
	 */
	@RequestMapping("/interfaceText.html")
	public String interfaceText(HttpServletRequest request) {
		try {
			// 本接口主要是完成【接口反射】，【参数识别】，【页面输出】三项操作 下面开始第一步，将接口转成页面能识别的集合
			List<ZAInterfaceNotesSimpleEntity> list = new ArrayList<ZAInterfaceNotesSimpleEntity>();
			List<Class<?>> controllerClasss = reflectController(request);// 通过反射加载项目中所有控制器
			for (Class<?> controller : controllerClasss) { // 遍历控制器
				List<Method> interfaces = filterInterface(controller);// 该控制器内所有的接口
				// 取RequestMapping注解是因为Spring框架中 接口与控制器的路径写在这个注解内
				RequestMapping requestMapping = (RequestMapping) controller.getAnnotation(RequestMapping.class);
				String controllerURL = requestMapping.value()[0];// 获取控制器路径
				for (Method method : interfaces) {
					// 取接口的RequestMapping也是为了取接口的路径
					RequestMapping mapping = method.getAnnotation(RequestMapping.class);
					// 封装接口对象与参数对象
					ZA_InterfaceNotes interfaceNotes = method.getAnnotation(ZA_InterfaceNotes.class);
					ZA_InterfaceNotesParameter[] interfaceNotesParameters = method
							.getAnnotationsByType(ZA_InterfaceNotesParameter.class);
					String url = controllerURL + mapping.value()[0];// 接口路径
					// 把所有接口放入集合以便输出HTML
					list.add(new ZAInterfaceNotesSimpleEntity(interfaceNotes, interfaceNotesParameters, url));
				}
			}
			Collections.sort(list);// 内部实现了根据接口时间表示进行排序
			request.setAttribute("str", ZACreateHtml.createInterfaceHTML(list));
			return "interfaceText";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 输出单个接口的调试页面(通过接口路径) 调试页面未考虑性能优化，因此每次进入均会重新识别遍历 gotoUrl可制定是对哪个路径的接口进行调试
	 */
	@RequestMapping("/gotoIndex.html")
	public String gotoIndex(HttpServletRequest request, String interfaceUrl) {
		try {
			// 把接口的url,参数名称,参数类型,参数的描述信息输出到指定接口的调试页面

			// 第一步 遍历所有找到我们指定的url接口
			List<ZAInterfaceNotesEntity> list = new ArrayList<ZAInterfaceNotesEntity>();// 每一个对象代表一个接口
			List<Class<?>> controllerClasss = reflectController(request); // 通过反射加载项目中所有控制器
			for (Class<?> controller : controllerClasss) { // 遍历控制器
				List<Method> interfaces = filterInterface(controller);// 该控制器内所有的接口
				// 取RequestMapping注解是因为Spring框架中 接口与控制器的url写在这个注解内
				RequestMapping requestMapping = (RequestMapping) controller.getAnnotation(RequestMapping.class);
				String controllerURL = requestMapping.value()[0]; // 获取控制器路径
				for (Method method : interfaces) {
					// 取接口的RequestMapping也是为了取接口的路径
					RequestMapping mapping = method.getAnnotation(RequestMapping.class);
					// 封装接口对象与参数对象
					ZA_InterfaceNotes interfaceNotes = method.getAnnotation(ZA_InterfaceNotes.class);
					ZA_InterfaceNotesParameter[] interfaceNotesParameters = method
							.getAnnotationsByType(ZA_InterfaceNotesParameter.class);
					String url = controllerURL + mapping.value()[0]; // 拼接接口URL
					if (StringUtils.isEmpty(interfaceUrl) || interfaceUrl.equals(url)) { // 判断是否是指定的接口
						// 第二步 【参数识别】
						list.add(new ZAInterfaceNotesEntity(interfaceNotes, interfaceNotesParameters, url));
					}
				}
			}
			request.setAttribute("list", list);
			return "index";
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 通过JAVA的反射加载所有的控制器类(class)
	 */
	private List<Class<?>> reflectController(HttpServletRequest request) throws ClassNotFoundException {
		List<Class<?>> controllerClasss = new ArrayList<Class<?>>();
		// 接口反射开始 (这步如果是其他语言需要改成其他方式去读取接口)

		for (String classPackage : CLASS_PACKAGES) {
			File classFolder = getControllerPath(request, classPackage);// 这个路径是WEB容器里面的路径，也就是部署后运行的路径
			if (classFolder.exists()) {// 如果路径存在
				File[] classFiles = classFolder.listFiles();// 获取路径下的文件列表
				for (File classFile : classFiles) {// 遍历class文件
					// 后缀必须是.class,同时跳过带有$符号的文件名，因为
					// JAVA编译时内部类和匿名类会生成带$的标识文件，这里我不需要解析它们，因此跳过
					if (checkClassName(classFile)) {
						// 接下来就是通过反射将class路径加载为Class文件了
						// 这步是JAVA语言的，如果是其他语言请用其他方式转换，总之就是加载的过程

						Class<?> c = Class.forName(classPackage + "."
								+ classFile.getName().substring(0, classFile.getName().indexOf(".")));
						// 加载完类后，下一步要判断这个类是不是控制器
						// 我这里直接判断了这个类有没有打上一个叫“RequestMapping”的注解
						// 因为使用了Spring框架中带有@Controller注解的是控制器
						if (c.isAnnotationPresent(Controller.class)) {
							controllerClasss.add(c);
							System.out.println(c.toString() + " 加载");
						} else {
							System.out.println(c.toString() + " 跳过");
						}
					}

				}
			}
		}

		return controllerClasss;
	}

}
