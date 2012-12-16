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
    final String firstLetter = String.valueOf(path.charAt(1));
    final Id FIRST_USER_LETTER = new Id("user", firstLetter);
    ACL acl = new ACL(Perms.ALL, FIRST_USER_LETTER);
    return Collections.singletonList(acl);
  }

}
