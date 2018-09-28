/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.server.wifi.hotspot2.soap;

import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.URL;
import java.util.Random;

/**
 * Redirect message listener to listen for the redirect message from server.
 */
public class RedirectListener extends Thread {
    private static final String TAG = "RedirectListener";
    private final ServerSocket mServerSocket;
    private final String mPath;
    private final URL mURL;

    private RedirectListener() throws IOException {
        mServerSocket = new ServerSocket(0, 5, InetAddress.getLocalHost());
        Random rnd = new Random(System.currentTimeMillis());
        mPath = "rnd" + Integer.toString(Math.abs(rnd.nextInt()), Character.MAX_RADIX);
        mURL = new URL("http", mServerSocket.getInetAddress().getHostAddress(),
                mServerSocket.getLocalPort(), mPath);
        setName("HS20-Redirect-Listener");
        setDaemon(true);
    }

    /**
     * Create an instance of {@link RedirectListener}
     *
     * @return Instance of {@link RedirectListener}, {@code null} in any failure.
     */
    public static RedirectListener createInstance() {
        RedirectListener redirectListener;
        try {
            redirectListener = new RedirectListener();
        } catch (IOException e) {
            Log.e(TAG, "fails to create an instance: " + e);
            return null;
        }
        return redirectListener;
    }

    public URL getURL() {
        return mURL;
    }
}
