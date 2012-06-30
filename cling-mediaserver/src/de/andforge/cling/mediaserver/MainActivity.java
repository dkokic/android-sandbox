package de.andforge.cling.mediaserver;

import java.io.IOException;

import org.teleal.cling.UpnpService;
import org.teleal.cling.UpnpServiceImpl;
import org.teleal.cling.android.AndroidUpnpServiceConfiguration;
import org.teleal.cling.binding.LocalServiceBindingException;
import org.teleal.cling.model.ValidationError;
import org.teleal.cling.model.ValidationException;
import org.teleal.cling.registry.RegistrationException;

import android.app.Activity;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println("Starting Cling UpnpService ...");
//		System.setProperty("org.teleal.cling.network.useInterfaces", "en1");
		WifiManager wifiManager = (WifiManager)this.getSystemService(WIFI_SERVICE);
		final UpnpService upnpService = new UpnpServiceImpl(new AndroidUpnpServiceConfiguration(wifiManager));

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				upnpService.shutdown();
			}
		});

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
    	super.onDestroy();
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    
}
