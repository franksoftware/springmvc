package proxy.cglibproxy;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

//CGLIB内部使用到ASM，所以我们下面的例子需要引入asm-3.3.jar、cglib-2.2.2.jar
public class AccountCglibProxyFactory implements MethodInterceptor {

	private Object target;

	public Object getInstance(Object target) {
		this.target = target;
		// Enhancer enhancer=new Enhancer();//该类用于生成代理对象
		// enhancer.setSuperclass(this.target.getClass());//设置父类
		// enhancer.setCallback(this);//设置回调用对象为本身
		return Enhancer.create(this.target.getClass(), this);
	}

	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		// TODO Auto-generated method stub
		// 排除Object类中的toString等方法
		boolean objFlag = method.getDeclaringClass().getName().equals("java.lang.Object");
		if (!objFlag) {
			System.out.println("before");
		}
		Object result = null;
		// 我们一般使用proxy.invokeSuper(obj,args)方法。这个很好理解，就是执行原始类的方法。还有一个方法proxy.invoke(obj,args)，这是执行生成子类的方法。
		// 如果传入的obj就是子类的话，会发生内存溢出，因为子类的方法不挺地进入intercept方法，而这个方法又去调用子类的方法，两个方法直接循环调用了。
		result = proxy.invokeSuper(obj, args);
		// result = methodProxy.invoke(obj, args);
		if (!objFlag) {
			System.out.println("after");
		}
		return result;
	}

}
