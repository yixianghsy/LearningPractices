package net.xdclass.demo.listener;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class RequestListener implements ServletRequestListener {

	@Override
	public void requestDestroyed(ServletRequestEvent sre) {
		// TODO Auto-generated method stub
		System.out.println("======requestDestroyed========");
	}

	@Override
	public void requestInitialized(ServletRequestEvent sre) {
		System.out.println("======requestInitialized========");
		
	}

   
}