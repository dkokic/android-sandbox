package de.andforge.cling.mediaserver;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.fourthline.cling.android.AndroidUpnpService;
import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.android.AndroidUpnpServiceConfiguration;
import org.fourthline.cling.android.AndroidUpnpServiceImpl;
import org.fourthline.cling.binding.LocalServiceBindingException;
import org.fourthline.cling.model.ValidationError;
import org.fourthline.cling.model.ValidationException;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.registry.RegistrationException;
import org.fourthline.cling.registry.Registry;
import org.fourthline.cling.registry.RegistryListener;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {

	private static final String TAG = MainActivity.class.getSimpleName();

	private UpnpService upnpService;
	private AndroidUpnpService androidUpnpService;


	private ServiceConnection serviceConnection = new ServiceConnection() {

	    public void onServiceConnected(ComponentName className, IBinder service) {

	    	androidUpnpService = (AndroidUpnpService) service;
	    	try {
				androidUpnpService.getRegistry().addDevice(MyMediaServer.create());
			} catch (RegistrationException e) {
				Log.e("MainActivity", e.getMessage());
				e.printStackTrace();
			} catch (LocalServiceBindingException e) {
				Log.e("MainActivity", e.getMessage());
				e.printStackTrace();
			} catch (ValidationException validationeException) {
				Log.e("MainActivity", "Exception caught in MainActivity.onCreate(): ", validationeException);
				for (ValidationError ve : validationeException.getErrors()) {
					Log.e("MainActivity", ve.getMessage());
				}
				validationeException.printStackTrace();
			} catch (IOException e) {
				Log.e("MainActivity", e.getMessage());
				e.printStackTrace();
			}
/*
	        // Refresh the list with all known devices
	        listAdapter.clear();
	        for (Device device : upnpService.getRegistry().getDevices()) {
	            registryListener.deviceAdded(device);
	        }

	        // Getting ready for future device advertisements
	        upnpService.getRegistry().addListener(registryListener);

	        // Search asynchronously for all devices
	        upnpService.getControlPoint().search();
*/
	    }

	    public void onServiceDisconnected(ComponentName className) {
	    	androidUpnpService = null;
	    }

	};


	@Override
	public void onCreate(Bundle savedInstanceState) {

		Log.d(TAG, "onCreate()");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

//		LogManager.getLogManager().getLogger("").addHandler(new MyAndroidLogHandler());
//		Logger.getLogger(UpnpServiceImpl.class.getName()).setLevel(Level.FINE);

		getApplicationContext().bindService(
	            new Intent(this, AndroidUpnpServiceImpl.class),
	            serviceConnection,
	            Context.BIND_AUTO_CREATE
	        );

	}


	private void oldUpnpStartup() {

		System.out.println("Starting Cling UpnpService ...");
		// System.setProperty("org.teleal.cling.network.useInterfaces", "en1");
		WifiManager wifiManager = (WifiManager) this.getSystemService(WIFI_SERVICE);
		upnpService = new UpnpServiceImpl(new AndroidUpnpServiceConfiguration(wifiManager));

		// Add the bound local device to the registry
		try {
			upnpService.getRegistry().addDevice(MyMediaServer.create());
		} catch (RegistrationException e) {
			Log.e("MainActivity", e.getMessage());
			e.printStackTrace();
		} catch (LocalServiceBindingException e) {
			Log.e("MainActivity", e.getMessage());
			e.printStackTrace();
		} catch (ValidationException validationeException) {
			Log.e("MainActivity", "Exception caught in MainActivity.onCreate(): ", validationeException);
			for (ValidationError ve : validationeException.getErrors()) {
				Log.e("MainActivity", ve.getMessage());
			}
			validationeException.printStackTrace();
		} catch (IOException e) {
			Log.e("MainActivity", e.getMessage());
			e.printStackTrace();
		}

	}


	@Override
	protected void onDestroy() {

		Log.d(TAG, "onDestroy()");
		super.onDestroy();

		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
		getApplicationContext().unbindService(serviceConnection);
	};


	private void oldUpnpShutdown() {

		if (isFinishing()) {
			new Thread() {
				@Override
				public void run() {
					upnpService.shutdown();
				}
			}.start();
		}

	}


	@Override
	protected void onPause() {
		Log.d(TAG, "onPause()");
		super.onPause();
	}


	@Override
	protected void onResume() {
		Log.d(TAG, "onResume()");
		super.onResume();
	}


	@Override
	public void onStart() {
		Log.d(TAG, "onStart()");
		super.onStart();
	}


	@Override
	protected void onStop() {
		Log.d(TAG, "onStop()");
		super.onStop();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}


	private class A implements RegistryListener {

		public void afterShutdown() {
			// TODO Auto-generated method stub

		}

		public void beforeShutdown(Registry pArg0) {
			// TODO Auto-generated method stub

		}

		public void localDeviceAdded(Registry pArg0, LocalDevice pArg1) {
			// TODO Auto-generated method stub

		}

		public void localDeviceRemoved(Registry pArg0, LocalDevice pArg1) {
			// TODO Auto-generated method stub

		}

		public void remoteDeviceAdded(Registry pArg0, RemoteDevice pArg1) {
			// TODO Auto-generated method stub

		}

		public void remoteDeviceDiscoveryFailed(Registry pArg0, RemoteDevice pArg1, Exception pArg2) {
			// TODO Auto-generated method stub

		}

		public void remoteDeviceDiscoveryStarted(Registry pArg0, RemoteDevice pArg1) {
			// TODO Auto-generated method stub

		}

		public void remoteDeviceRemoved(Registry pArg0, RemoteDevice pArg1) {
			// TODO Auto-generated method stub

		}

		public void remoteDeviceUpdated(Registry pArg0, RemoteDevice pArg1) {
			// TODO Auto-generated method stub

		}

	}

}
