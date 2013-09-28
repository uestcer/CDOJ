package cn.edu.uestc.acmicpc.db;

import cn.edu.uestc.acmicpc.db.dto.impl.UserDTO;
import cn.edu.uestc.acmicpc.db.dto.impl.UserRegisterDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.edu.uestc.acmicpc.config.IntegrationTestContext;
import cn.edu.uestc.acmicpc.util.exception.AppException;

/**
 * Test cases for DTO entities.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { IntegrationTestContext.class })
public class DTOITTest {

  @Test
  public void testUserDTO() throws AppException {
    @SuppressWarnings("unused")
    UserDTO userDTO = UserDTO.builder()
        .setUserId(1)
        .setUserName("userName")
        .setDepartmentId(1)
        .setEmail("email@email.com")
        .setPassword("password")
        .setNickName("nickName")
        .setSchool("school")
        .setStudentId("123456789")
        .setType(2)
        .build();
    /*
     * @TODO wuwu User user = userRegisterDTO.getEntity(); Assert.assertEquals(Integer.valueOf(1),
     * user.getUserId()); Assert.assertEquals("userName", user.getUserName());
     * Assert.assertEquals("nickName", user.getNickName());
     * Assert.assertEquals(StringUtil.encodeSHA1("password"), user.getPassword());
     * Assert.assertEquals(Integer.valueOf(2), user.getType());
     */
  }
}
