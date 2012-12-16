/*
 * Copyright 2012 Michael Morello
 * Copyright 2012 Netflix, Inc.
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

import org.junit.After;
import org.junit.Before;

import com.google.common.base.Charsets;
import com.google.common.io.Closeables;
import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.CuratorFrameworkFactory;
import com.netflix.curator.retry.RetryOneTime;
import com.netflix.curator.test.TestingServer;
import com.netflix.curator.utils.DebugUtils;

public class BaseClassForSecurityTests {
  public static final byte[] DATA_F = "data for znode /f"
      .getBytes(Charsets.UTF_8);
  public static final byte[] DATA_B = "data for znode /b"
      .getBytes(Charsets.UTF_8);
  protected TestingServer server;

  @Before
  public void setup() throws Exception {
    // Start a Zookeeper unit test server
    System.setProperty(DebugUtils.PROPERTY_DONT_LOG_CONNECTION_ISSUES, "true");
    // Register our custom authentication provider
    System.setProperty("zookeeper.authProvider.1",
        "morello.zk.acl.CustomUserAuthenticationProvider");
    // Ok, create a testing Zookeeper server with Curator
    server = new TestingServer();

    // Now feed our server with a sample znode tree
    CuratorFramework zkClient = getClientBuilder().build();
    zkClient.start();
    zkClient.create().forPath("/f", DATA_F);
    zkClient.create().forPath("/b", DATA_B);
    Closeables.closeQuietly(zkClient);
  }

  protected CuratorFrameworkFactory.Builder getClientBuilder() {
    // Create a client builder
    CuratorFrameworkFactory.Builder clientBuilder = CuratorFrameworkFactory
        .builder().connectString(server.getConnectString())
        .retryPolicy(new RetryOneTime(2000)).aclProvider(new ACLProvider());
    return clientBuilder;
  }

  @After
  public void teardown() throws Exception {
    System.clearProperty("zookeeeper.authProvider.1");
    server.close();
  }

}
