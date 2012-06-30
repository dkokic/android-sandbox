package de.andforge.cling.mediaserver;

import java.io.IOException;

import org.teleal.cling.UpnpService;
import org.teleal.cling.UpnpServiceImpl;
import org.teleal.cling.android.AndroidUpnpServiceConfiguration;
import org.teleal.cling.binding.LocalServiceBindingException;
import org.teleal.cling.model.ValidationError;
import org.teleal.cling.model.ValidationException;
import org.teleal.cling.model.meta.LocalDevice;
import org.teleal.cling.model.meta.RemoteDevice;
import org.teleal.cling.registry.RegistrationException;
import org.teleal.cling.registry.Registry;
import org.teleal.cling.registry.RegistryListener;

import android.app.Activity;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {

	private UpnpService upnpService;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		System.out.println("Starting Cling UpnpService ...");
		// System.setProperty("org.teleal.cling.network.useInterfaces", "en1");
		WifiManager wifiManager = (WifiManager) this.getSystemService(WIFI_SERVICE);
		upnpService = new UpnpServiceImpl(new AndroidUpnpServiceConfiguration(wifiManager));

		// Add the bound local device to the registry
		try {
			upnpService.getRegistry().addDevice(MyMediaServer.create());
		} catch (RegistrationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LocalServiceBindingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ValidationException validationeException) {
			Log.e("MainActivity", "Exception caught in MainActivity.onCreate(): ", validationeException);
			for (ValidationError ve : validationeException.getErrors()) {
				Log.e("MainActivity", ve.getMessage());
			}
			validationeException.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected void onDestroy() {

		if (isFinishing()) {
			new Thread() {
				@Override
				public void run() {
					upnpService.shutdown();
				}
			}.start();
		}
		super.onDestroy();
	};

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
