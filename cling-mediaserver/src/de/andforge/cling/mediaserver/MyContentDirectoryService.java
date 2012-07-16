package de.andforge.cling.mediaserver;

import org.fourthline.cling.support.contentdirectory.AbstractContentDirectoryService;
import org.fourthline.cling.support.contentdirectory.ContentDirectoryErrorCode;
import org.fourthline.cling.support.contentdirectory.ContentDirectoryException;
import org.fourthline.cling.support.contentdirectory.DIDLParser;
import org.fourthline.cling.support.model.BrowseFlag;
import org.fourthline.cling.support.model.BrowseResult;
import org.fourthline.cling.support.model.DIDLContent;
import org.fourthline.cling.support.model.PersonWithRole;
import org.fourthline.cling.support.model.Res;
import org.fourthline.cling.support.model.SortCriterion;
import org.fourthline.cling.support.model.container.Container;
import org.fourthline.cling.support.model.container.StorageFolder;
import org.fourthline.cling.support.model.item.MusicTrack;
import org.seamless.util.MimeType;

import android.util.Log;

public class MyContentDirectoryService extends AbstractContentDirectoryService {

	@Override
	public BrowseResult browse(String objectID, BrowseFlag browseFlag, String filter, long firstResult,
			long maxResults, SortCriterion[] orderby) throws ContentDirectoryException {

		Log.i("MyContentDirectoryService", "browse(): objectID = " + objectID + ", browseFlag = " + browseFlag);
		Log.i("MyContentDirectoryService", "browse(): filter = " + filter + ", firstResult = " + firstResult
				+ ", maxResults = " + maxResults);
		Log.i("MyContentDirectoryService", "browse(): orderby = " + orderby);

		int resultCount = 0;

		try {

			// create the DIDL content dynamically ...

			DIDLContent didl = new DIDLContent();

			if (BrowseFlag.METADATA.equals(browseFlag)) {

				if ("0".equalsIgnoreCase(objectID)) {
					didl.addContainer(new StorageFolder("0", "-1", "root", "me", 2, null));
					resultCount = 1;
				}

			} else {

				if ("0".equalsIgnoreCase(objectID)) {
					didl.addContainer(new StorageFolder("1", "0", "Webradio", "me", 3, null));
					didl.addContainer(new StorageFolder("2", "0", "Mediathek", "me", 0, null));
					resultCount = 2;
				}

				if ("1".equalsIgnoreCase(objectID)) {

					String album = ("Nothing But The Beat");
					String creator = "David Guetta"; // Required
					PersonWithRole artist = new PersonWithRole(creator, "Performer");
					MimeType mimeType = new MimeType("audio", "mpeg");

					didl.addItem(new MusicTrack("101", "1", "Planet Radio - The Club", creator, album, artist, new Res(
							mimeType, new Long(99999999l), "05:00:00", new Long(8192l),
							"http://streams.planetradio.de/plrchannels/mp3/hqdjbeats.mp3")));

					didl.addItem(new MusicTrack("102", "1", "Sweat (Snoop Dogg vs. David Guetta) [Remix]", creator,
							album, artist, new Res(mimeType, 2222222l, "00:03:16", 8192l,
									"http://open.spotify.com/track/6NqmVXFFJ75ne1BnDiPSfi")));

					didl.addItem(new MusicTrack("103", "1", "The Alphabeat (Radio Edit)", creator, album, artist,
							new Res(mimeType, 500000000l, "05:00:00", 16000l, "http://87.230.103.85:80")));

				}
			}

			return new BrowseResult(new DIDLParser().generate(didl), resultCount, resultCount);

		} catch (Exception ex) {
			throw new ContentDirectoryException(ContentDirectoryErrorCode.CANNOT_PROCESS, ex.toString());
		}
	}

	@Override
	public BrowseResult search(String containerId, String searchCriteria, String filter, long firstResult,
			long maxResults, SortCriterion[] orderBy) throws ContentDirectoryException {
		// You can override this method to implement searching!

		Log.i("MyContentDirectoryService", "search(): containerId = " + containerId + ", searchCriteria = "
				+ searchCriteria);

		return super.search(containerId, searchCriteria, filter, firstResult, maxResults, orderBy);
	}

}
