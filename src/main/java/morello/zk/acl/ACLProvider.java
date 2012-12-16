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

import java.util.Collections;
import java.util.List;

import org.apache.zookeeper.ZooDefs.Perms;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class ACLProvider implements
    com.netflix.curator.framework.api.ACLProvider {

  @Override
  public List<ACL> getDefaultAcl() {
    throw new NotImplementedException();
  }

  @Override
  public List<ACL> getAclForPath(String path) {
    // The first letter of the path is the ACL id
    final String firstLetter = String.valueOf(path.charAt(1));
    final Id FIRST_USER_LETTER = new Id("user", firstLetter);
    // Create a new ACL with the first letter of the path as an ID and give all
    // permissions for users
    ACL acl = new ACL(Perms.ALL, FIRST_USER_LETTER);
    return Collections.singletonList(acl);
  }

}
