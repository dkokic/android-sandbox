package de.andforge.cling.mediaserver;

import org.teleal.cling.support.contentdirectory.AbstractContentDirectoryService;
import org.teleal.cling.support.contentdirectory.ContentDirectoryErrorCode;
import org.teleal.cling.support.contentdirectory.ContentDirectoryException;
import org.teleal.cling.support.contentdirectory.DIDLParser;
import org.teleal.cling.support.model.BrowseFlag;
import org.teleal.cling.support.model.BrowseResult;
import org.teleal.cling.support.model.DIDLContent;
import org.teleal.cling.support.model.PersonWithRole;
import org.teleal.cling.support.model.Res;
import org.teleal.cling.support.model.SortCriterion;
import org.teleal.cling.support.model.item.MusicTrack;
import org.teleal.common.util.MimeType;

public class MyContentDirectoryService extends AbstractContentDirectoryService {

    @Override
    public BrowseResult browse(String objectID, BrowseFlag browseFlag,
                               String filter,
                               long firstResult, long maxResults,
                               SortCriterion[] orderby) throws ContentDirectoryException {
        try {

            // create the DIDL content dynamically ...

            DIDLContent didl = new DIDLContent();

            String album = ("Nothing But The Beat");
            String creator = "David Guetta"; // Required
            PersonWithRole artist = new PersonWithRole(creator, "Performer");
            MimeType mimeType = new MimeType("audio", "mpeg");

            didl.addItem(new MusicTrack(
                    "101", "3", // 101 is the Item ID, 3 is the parent Container ID
                    "Planet Radio - The Club",
                    creator, album, artist,
                    new Res(mimeType, 99999999l, "05:00:00", 8192l, "http://192.168.1.109:9999/http://streams.planetradio.de/plrchannels/mp3/hqdjbeats.mp3")
            ));

            didl.addItem(new MusicTrack(
                    "102", "3",
                    "Sweat (Snoop Dogg vs. David Guetta) [Remix]",
                    creator, album, artist,
                    new Res(mimeType, 2222222l, "00:03:16", 8192l, "http://open.spotify.com/track/6NqmVXFFJ75ne1BnDiPSfi")
            ));

            didl.addItem(new MusicTrack(
                    "103", "3", // 101 is the Item ID, 3 is the parent Container ID
                    "The Alphabeat (Radio Edit)",
                    creator, album, artist,
                    new Res(mimeType, 123456l, "00:03:26", 8192l, "http://87.230.103.85:80")
            ));

            // 

            // Create more tracks...

            // Count and total matches is 2
            return new BrowseResult(new DIDLParser().generate(didl), 3, 3);

        } catch (Exception ex) {
            throw new ContentDirectoryException(
                    ContentDirectoryErrorCode.CANNOT_PROCESS,
                    ex.toString()
            );
        }
    }

    @Override
    public BrowseResult search(String containerId,
                               String searchCriteria, String filter,
                               long firstResult, long maxResults,
                               SortCriterion[] orderBy) throws ContentDirectoryException {
        // You can override this method to implement searching!
        return super.search(containerId, searchCriteria, filter, firstResult, maxResults, orderBy);
    }

}
