/*
 * Copyright 2012 Michael Morello
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package morello.zk.acl;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.apache.zookeeper.KeeperException.AuthFailedException;
import org.apache.zookeeper.KeeperException.NoAuthException;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Closeables;
import com.netflix.curator.framework.CuratorFramework;

public class SecurityTest extends BaseClassForSecurityTests {

  @Test(expected = NoAuthException.class)
  public void securityKO() throws Exception {
    CuratorFramework client = getClientBuilder().authorization("user",
        "toto".getBytes(Charsets.UTF_8)).build();
    try {
      client.start();
      client.getData().forPath("/f");
    } finally {
      Closeables.closeQuietly(client);
    }
  }

  @Test
  public void securityOK() throws Exception {
    CuratorFramework client = getClientBuilder().authorization("user",
        "foo".getBytes(Charsets.UTF_8)).build();
    try {
      client.start();
      byte[] data = client.getData().forPath("/f");
      assertTrue(Arrays.equals(data, DATA_F));
      String forPath = client.create().forPath("/f/1",
          "Hello".getBytes(Charsets.UTF_8));
      assertEquals("/f/1", forPath);
    } finally {
      Closeables.closeQuietly(client);
    }
  }

}
