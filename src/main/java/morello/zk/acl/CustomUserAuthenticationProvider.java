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

import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.ServerCnxn;
import org.apache.zookeeper.server.auth.AuthenticationProvider;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;

public class CustomUserAuthenticationProvider implements AuthenticationProvider {

  @Override
  public String getScheme() {
    return "user";
  }

  @Override
  public Code handleAuthentication(ServerCnxn cnxn, byte[] authData) {
    final String userName = new String(authData, Charsets.UTF_8);
    // A non null or empty user name must be provided
    if (!Strings.isNullOrEmpty(userName)) {
      // This line is VERY important ! return code is not enough
      cnxn.addAuthInfo(new Id(getScheme(), userName));
      return Code.OK;
    }
    return Code.AUTHFAILED;
  }

  @Override
  public boolean matches(String id, String aclExpr) {
    if (Strings.isNullOrEmpty(id) || Strings.isNullOrEmpty(aclExpr)) {
      return false;
    }
    // Check if the first letter of the user name match the one in the acl
    return id.charAt(0) == aclExpr.charAt(0);
  }

  @Override
  public boolean isAuthenticated() {
    return true;
  }

  @Override
  public boolean isValid(String id) {
    // A valid user name is at least 1 char length
    return !Strings.isNullOrEmpty(id) && id.length() == 1;
  }

}
