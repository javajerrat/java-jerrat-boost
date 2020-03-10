/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.javajerrat.boost.os.network;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2020/1/15
 */
@Slf4j
public class Networks {

    /**
     * Detect an unused port.
     * If the port given by defaultPort parameter is not in use, use this port otherwise try to detect other ports.
     * @param defaultPort defaultPort
     * @return An unused port
     * @throws NoSuchElementException If no available port is found, this exception is thrown.
     */
    public static int findAvailablePort(int defaultPort) {
        return findAvailablePort(defaultPort, 0, 65535);
    }

    /**
     * Detect an unused port.
     * If the port given by defaultPort parameter is not in use, use this port otherwise try to detect other ports.
     * Detection range is between minPort (inclusive) and maxPort (exclusive).
     * @param defaultPort defaultPort
     * @param minPort minPort
     * @param maxPort maxPort
     * @return An unused port
     * @throws NoSuchElementException If no available port is found, this exception is thrown.
     */
    public static int findAvailablePort(int defaultPort, int minPort, int maxPort) {
        Preconditions.checkArgument(defaultPort >= minPort && defaultPort < maxPort);
        for (int port = defaultPort; port < maxPort; port++) {
            if (!isPortUsed(port)) {
                return port;
            }
        }

        for (int port = defaultPort - 1; port > minPort; port--) {
            if (!isPortUsed(port)) {
                return port;
            }
        }
        throw new NoSuchElementException("no available port.");
    }

    /**
     * Determine if a given port is used.
     * @param port port
     * @return Return true if used otherwise false
     */
    public static boolean isPortUsed(int port) {
        try (ServerSocket ignored = new ServerSocket(port)) {
            return false;
        } catch (IOException e) {
            return true;
        }
    }
}
