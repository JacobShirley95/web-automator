package org.auriferous.bot.tabs;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.auriferous.bot.gui.swing.tabs.JTabView;
import org.auriferous.bot.tabs.view.TabView;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserContext;
import com.teamdev.jxbrowser.chromium.events.TitleEvent;
import com.teamdev.jxbrowser.chromium.events.TitleListener;

public class Tab {
	private static final List<Browser> BROWSER_INSTANCES = new ArrayList<Browser>();
	
	private static final BrowserContext DEFAULT_CONTEXT = new BrowserContext("test");
	
	private int id;
	private String originalURL;
	
	private Browser browser;
	
	private List<TabListener> tabListeners = new LinkedList<TabListener>();

	private TabView tabView;
	
	public Tab(int id, String url) {
		this.id = id;
		
		this.browser = new Browser(DEFAULT_CONTEXT);
		this.browser.getPreferences().setLocalStorageEnabled(true);
		
		BROWSER_INSTANCES.add(browser);
		
		browser.addTitleListener(new TitleListener() {
            @Override
			public void onTitleChange(TitleEvent event) {
            	for (TabListener listener : tabListeners) 
					listener.onTitleChange(event.getTitle());
            }
        });
		
		loadURL(url);
	}
	
	public Tab(String url) {
		this(-1, url);
	}
	
	public void goBack() {
		this.browser.goBack();
	}
	
	public void goForward() {
		this.browser.goForward();
	}
	
	public void loadURL(String url) {
		if (!this.browser.isDisposed()) {
			this.originalURL = url;
		
			this.browser.loadURL(url);
		
			for (TabListener listener : tabListeners) 
				listener.onTabUpdating();
		}
	}
	
	public void reload() {
		if (!this.browser.isDisposed()) {
			this.browser.reload();
		
			for (TabListener listener : tabListeners) 
				listener.onTabReloaded();
		}
	}
	
	public String getTitle() {
		if (!browser.isDisposed())
			return browser.getTitle();
		return "";
	}
	
	public String getURL() {
		if (!browser.isDisposed())
			return browser.getURL();
		return "";
	}
	
	public String getOriginalURL() {
		return originalURL;
	}
	
	public int getID() {
		return id;
	}
	
	public void setID(int id) {
		this.id = id;
	}
	
	public Browser getBrowserWindow() {
		return browser;
	}
	
	public void setTabView(TabView tabView) {
		this.tabView = tabView;
	}
	
	public TabView getTabView() {
		return tabView;
	}
	
	public void addTabListener(TabListener tabListener) {
		this.tabListeners.add(tabListener);
	}
	
	public void removeTabListener(TabListener tabListener) {
		this.tabListeners.remove(tabListener);
	}
	
	static {
		Thread t = new Thread(new Runnable() {
		    @Override
			public void run() {
		    	for (Browser browser : BROWSER_INSTANCES) {
		    		if (!browser.isDisposed())
		    			browser.dispose();
		    	}
		    }
		});
		t.setDaemon(true);
		
		Runtime.getRuntime().addShutdownHook(t);
	}
}
