
package com.android.volley.toolbox;

import android.net.Uri;

import com.android.volley.toolbox.DiskBasedCache;

import java.io.File;
import java.util.Set;

public class LocationUnawareDiskBasedCache extends DiskBasedCache {
    public LocationUnawareDiskBasedCache(File rootDirectory, int maxCacheSizeInBytes) {
        super(rootDirectory, maxCacheSizeInBytes);
    }

    public LocationUnawareDiskBasedCache(File rootDirectory) {
        super(rootDirectory);
    }

    @Override
    public synchronized Entry get(String key) {
        return super.get(getUrl(key));
    }

    @Override
    public synchronized void put(String key, Entry entry) {
        super.put(getUrl(key), entry);
    }

    private String getUrl(String key) {
        Uri uri = Uri.parse(key);
        Uri.Builder builder = Uri.parse(
                uri.getScheme() + "://" + uri.getAuthority() + uri.getPath()).buildUpon();
        Set<String> names = uri.getQueryParameterNames();
        if (names != null) {
            for (String param : names) {
                if ("latitude".equals(param) || "longitude".equals(param)) {
                    continue;
                }
                builder.appendQueryParameter(param, uri.getQueryParameter(param));
            }
        }

        return builder.build().toString();
    }

}
