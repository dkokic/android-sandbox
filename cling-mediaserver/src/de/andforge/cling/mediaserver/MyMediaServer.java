package de.andforge.cling.mediaserver;

import java.io.IOException;
import java.net.URI;

import org.teleal.cling.binding.LocalServiceBindingException;
import org.teleal.cling.binding.annotations.AnnotationLocalServiceBinder;
import org.teleal.cling.model.DefaultServiceManager;
import org.teleal.cling.model.ValidationException;
import org.teleal.cling.model.meta.DeviceDetails;
import org.teleal.cling.model.meta.DeviceIdentity;
import org.teleal.cling.model.meta.Icon;
import org.teleal.cling.model.meta.LocalDevice;
import org.teleal.cling.model.meta.LocalService;
import org.teleal.cling.model.meta.ManufacturerDetails;
import org.teleal.cling.model.meta.ModelDetails;
import org.teleal.cling.model.types.DeviceType;
import org.teleal.cling.model.types.UDADeviceType;
import org.teleal.cling.model.types.UDN;
import org.teleal.cling.support.connectionmanager.ConnectionManagerService;
import org.teleal.cling.support.model.Protocol;
import org.teleal.cling.support.model.ProtocolInfo;
import org.teleal.cling.support.model.ProtocolInfos;

import android.net.Uri;

public class MyMediaServer extends LocalDevice {

	private MyMediaServer(DeviceIdentity pIdentity, DeviceType pType, DeviceDetails pDetails, Icon pIcon,
			LocalService[] pMyLocalServices) throws ValidationException {
		super(pIdentity, pType, pDetails, pIcon, pMyLocalServices);
	}

	static LocalDevice create() throws ValidationException, LocalServiceBindingException, IOException {

		DeviceIdentity identity = new DeviceIdentity(UDN.uniqueSystemIdentifier("Demo Media Server"));

		DeviceType type = new UDADeviceType("MediaServer", 1);

		ManufacturerDetails manufacturerDetails = new ManufacturerDetails("kodra");
		ModelDetails modelDetails = new ModelDetails("DemoMediaServer", "A demo media server.", "v1");
		DeviceDetails details = new DeviceDetails("MyUPnP", manufacturerDetails, modelDetails);

		// Icon icon = new Icon("image/png", 48, 48, 8, ???);

		AnnotationLocalServiceBinder binder = new AnnotationLocalServiceBinder();
		LocalService<MyContentDirectoryService> contentDirectoryService = createContentDirectoryService(binder);
		LocalService<ConnectionManagerService> connectionManagerService = createConnectionManagerService(binder);

		LocalService[] myLocalServices = new LocalService[] { contentDirectoryService, connectionManagerService };

		return new MyMediaServer(identity, type, details, null, myLocalServices);

		/*
		 * Several services can be bound to the same device: return new LocalDevice( identity, type, details, icon, new
		 * LocalService[] {switchPowerService, myOtherService} );
		 */

	}

	// ContentDirectory
	private static LocalService<MyContentDirectoryService> createContentDirectoryService(
			AnnotationLocalServiceBinder binder) {
		LocalService<MyContentDirectoryService> contentDirectoryService = binder.read(MyContentDirectoryService.class);
		contentDirectoryService.setManager(new DefaultServiceManager<MyContentDirectoryService>(
				contentDirectoryService, MyContentDirectoryService.class));
		return contentDirectoryService;
	}

	// ConnectionManager
	private static LocalService<ConnectionManagerService> createConnectionManagerService(
			AnnotationLocalServiceBinder binder) {
		LocalService<ConnectionManagerService> connectionManagerService = binder.read(ConnectionManagerService.class);

		ProtocolInfo protocolInfo1 = new ProtocolInfo(Protocol.HTTP_GET, ProtocolInfo.WILDCARD, "audio/mpeg",
				"DLNA.ORG_PN=MP3;DLNA.ORG_OP=01");
		ProtocolInfo protocolInfo2 = new ProtocolInfo(Protocol.HTTP_GET, ProtocolInfo.WILDCARD, "video/mpeg",
				"DLNA.ORG_PN=MPEG1;DLNA.ORG_OP=01;DLNA.ORG_CI=0");
		final ProtocolInfos sourceProtocols = new ProtocolInfos(protocolInfo1, protocolInfo2);

		connectionManagerService.setManager(new DefaultServiceManager<ConnectionManagerService>(
				connectionManagerService, null) {
			@Override
			protected ConnectionManagerService createServiceInstance() throws Exception {
				return new ConnectionManagerService(sourceProtocols, null);
			}
		});

		return connectionManagerService;
	}

}
