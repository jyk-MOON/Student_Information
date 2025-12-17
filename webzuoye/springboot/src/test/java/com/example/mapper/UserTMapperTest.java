package com.example.mapper;

import com.example.entity.UserT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
@Rollback
public class UserTMapperTest {
    @Autowired
    private UserTMapper userTMapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 测试 selectById，ID 存在
    @Test
    public void Should_ReturnUserT_When_IdExists() {
        // Given: 向 userT 表插入数据
        jdbcTemplate.update(
                "INSERT INTO userT (id, name, email) VALUES (?, ?, ?)",
                1, "张三", "zhangsan@example.com"
        );

        // When
        UserT user = userTMapper.selectById(1); // 或 userMapper.selectById(1)，看你的 Mapper 定义

        // Then
        assertThat(user).isNotNull();
        assertThat(user.getName()).isEqualTo("张三");
        assertThat(user.getId()).isEqualTo(1);
    }

    // 测试 ID 不存在
    @Test
    public void Should_ReturnNull_When_IdDoesNotExist() {
        UserT user = userTMapper.selectById(999);
        assertThat(user).isNull();
    }

    // 测试 ID 为 null
    @Test
    public void Should_ReturnNull_When_IdIsNull() {
        UserT user = userTMapper.selectById(null);
        assertThat(user).isNull();
    }

    // 测试边界值
    @Test
    public void Should_ReturnNull_When_IdIsBoundary() {
        assertThat(userTMapper.selectById(0)).isNull();
        assertThat(userTMapper.selectById(-1)).isNull();
        assertThat(userTMapper.selectById(Integer.MAX_VALUE)).isNull();
    }




    // 测试 insert，满字段
    @Test
    public void Should_InsertSuccessfully_When_AllFieldsProvided() {
        UserT user = new UserT();
        user.setName("张三");
        user.setEmail("zhangsan@example.com");
        user.setPassword("123456");
        user.setBirthday("1990-01-01");
        user.setAvatar("avatar.png");

        int result = userTMapper.insert(user);

        assertThat(result).isEqualTo(1);
        assertThat(user.getId()).isNotNull().isGreaterThan(0);

        UserT dbUser = userTMapper.selectById(user.getId());
        assertThat(dbUser.getName()).isEqualTo("张三");
        assertThat(dbUser.getEmail()).isEqualTo("zhangsan@example.com");
        // ... 其他字段
    }

    // 测试 insert，部分字段
    @Test
    public void Should_InsertOnlyNonNullFields_When_SomeAreNull() {
        UserT user = new UserT();
        user.setName("李四"); // 只设 name

        int result = userTMapper.insert(user);

        assertThat(result).isEqualTo(1);
        UserT dbUser = userTMapper.selectById(user.getId());
        assertThat(dbUser.getName()).isEqualTo("李四");
        assertThat(dbUser.getEmail()).isNull(); // 未设置的字段应为 NULL
    }

    // 测试 ID 为 null → 自动生成
    @Test
    public void Should_GenerateId_When_IdIsNull() {
        UserT user = new UserT();
        user.setName("王五");
        user.setId(null); // 显式设 null

        userTMapper.insert(user);
        assertThat(user.getId()).isNotNull().isGreaterThan(0);
    }

    // 测试显式指定 ID
    @Test
    public void Should_UseSpecifiedId_When_IdIsNotNull() {
        jdbcTemplate.update("DELETE FROM userT WHERE id = ?", 999);

        UserT user = new UserT();
        user.setId(999);
        user.setName("赵六");

        userTMapper.insert(user);
        assertThat(user.getId()).isEqualTo(999L);

        UserT dbUser = userTMapper.selectById(999);
        assertThat(dbUser.getName()).isEqualTo("赵六");
    }



    // 测试 deleteById，ID 存在
    @Test
    public void Should_DeleteSuccessfully_When_IdExists() {
        // Given: 插入一条 userT 数据
        jdbcTemplate.update("INSERT INTO userT (id, name) VALUES (?, ?)", 1, "张三");

        // When
        int result = userTMapper.deleteById(1);

        // Then
        assertThat(result).isEqualTo(1); // 删除 1 行

        // 验证已删除
        UserT user = userTMapper.selectById(1); // 假设你有 selectById for UserT
        assertThat(user).isNull();
    }

    // 测试 ID 不存在
    @Test
    public void Should_ReturnZero_When_DeleteIdDoesNotExist() {
        jdbcTemplate.update("DELETE FROM userT WHERE id = 999"); // 确保干净

        int result = userTMapper.deleteById(999);
        assertThat(result).isEqualTo(0);
    }

    // 测试 ID 为 null
    @Test
    public void Should_ReturnZero_When_IdIsNull() {
        jdbcTemplate.update("INSERT INTO userT (id, name) VALUES (1, '李四')");

        int result = userTMapper.deleteById(null);
        assertThat(result).isEqualTo(0); // 不应删除任何行

        // 验证数据还在
        assertThat(userTMapper.selectById(1)).isNotNull();
    }

    // 测试幂等性（删两次）
    @Test
    public void Should_BeIdempotent_When_DeleteTwice() {
        jdbcTemplate.update("INSERT INTO userT (id, name) VALUES (1, '王五')");

        int r1 = userTMapper.deleteById(1);
        assertThat(r1).isEqualTo(1);

        int r2 = userTMapper.deleteById(1);
        assertThat(r2).isEqualTo(0); // 第二次无影响
    }



    // 测试 updateById，部分字段更新
    @Test
    public void Should_UpdateOnlyNonNullFields_When_PartialData() {
        // Given: 插入初始数据
        jdbcTemplate.update(
                "INSERT INTO userT (id, name, email, avatar) VALUES (?, ?, ?, ?)",
                1, "张三", "zhangsan@example.com", "old_avatar.png"
        );

        // When: 只更新 name 和 email
        UserT user = new UserT();
        user.setId(1);
        user.setName("张三丰");      // 修改
        user.setEmail("new@example.com"); // 修改
        user.setAvatar(null);       // 显式 null，不应更新

        int result = userTMapper.updateById(user);

        assertThat(result).isEqualTo(1);

        // Then: 验证变更 & 未变更字段
        UserT dbUser = userTMapper.selectById(1);
        assertThat(dbUser.getName()).isEqualTo("张三丰");
        assertThat(dbUser.getEmail()).isEqualTo("new@example.com");
        assertThat(dbUser.getAvatar()).isEqualTo("old_avatar.png"); // 保持不变
    }

    // 测试 updateById，所有字段都更新
    @Test
    public void Should_UpdateAllFields_When_AllAreProvided() {
        // Given: 插入初始数据（包含所有字段）
        jdbcTemplate.update(
                "INSERT INTO userT (id, name, password, email, birthday, avatar) VALUES (?, ?, ?, ?, ?, ?)",
                1,
                "原始姓名",
                "oldPass123",
                "old@example.com",
                "1990-01-01",
                "old_avatar.png"
        );

        // When: 提供所有字段的新值（全部非 null）
        UserT user = new UserT();
        user.setId(1);
        user.setName("新姓名");
        user.setPassword("newPass456");
        user.setEmail("new@example.com");
        user.setBirthday("1995-12-31");
        user.setAvatar("new_avatar.jpg");

        int result = userTMapper.updateById(user);

        assertThat(result).isEqualTo(1);

        // Then: 验证所有字段都被更新
        UserT dbUser = userTMapper.selectById(1);
        assertThat(dbUser.getName()).isEqualTo("新姓名");
        assertThat(dbUser.getPassword()).isEqualTo("newPass456");
        assertThat(dbUser.getEmail()).isEqualTo("new@example.com");
        assertThat(dbUser.getBirthday()).isEqualTo("1995-12-31");
        assertThat(dbUser.getAvatar()).isEqualTo("new_avatar.jpg");
    }


    // 测试 ID 不存在
    @Test
    public void Should_ReturnZero_When_UpdateIdDoesNotExist() {
        UserT user = new UserT();
        user.setId(999);
        user.setName("不存在");

        int result = userTMapper.updateById(user);
        assertThat(result).isEqualTo(0);
    }

    // 测试 ID 为 null
    @Test
    public void Should_NotUpdateAnyRecord_When_IdIsNull() {
        jdbcTemplate.update("INSERT INTO userT (id, name) VALUES (1, '李四')");

        UserT user = new UserT();
        user.setId(null); // 关键：id 为 null
        user.setName("新名字");

        int result = userTMapper.updateById(user);
        assertThat(result).isEqualTo(0); // 不应更新任何行

        // 原数据不变
        assertThat(userTMapper.selectById(1).getName()).isEqualTo("李四");
    }

    // （可选）测试全 null 更新 —— 验证 MyBatis <set> 是否安全
    @Test
    public void Should_NotGenerateInvalidSql_When_AllFieldsNull() {
        jdbcTemplate.update("INSERT INTO userT (id, name) VALUES (1, '王五')");

        UserT user = null;

        // When & Then
        assertThatThrownBy(() -> userTMapper.updateById(user))
                .isInstanceOf(Exception.class);
    }


    // 测试 selectAll，所有字段提供（精确匹配）
    @Test
    public void Should_ReturnExactUser_When_AllSearchCriteriaProvided() {
        // Given: 插入匹配数据 + 干扰项
        jdbcTemplate.update(
                "INSERT INTO userT (id, name, email) VALUES (?, ?, ?)",
                1, "张三丰", "zhang@example.com"
        );
        jdbcTemplate.update(
                "INSERT INTO userT (id, name, email) VALUES (?, ?, ?)",
                2, "李四", "li@example.com"
        );

        // When: 提供完整条件
        UserT query = new UserT();
        query.setId(1);
        query.setName("三"); // 模糊匹配
        query.setEmail("zhang@example.com");

        List<UserT> result = userTMapper.selectAll(query);

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getName()).isEqualTo("张三丰");
    }

    // 测试 selectAll，部分字段（如 name + email）
    @Test
    public void Should_ReturnUsersMatchingNameAndEmail_When_PartialCriteriaProvided() {
        jdbcTemplate.update("INSERT INTO userT (id, name, email) VALUES (1, '王小明', 'wang@example.com')");
        jdbcTemplate.update("INSERT INTO userT (id, name, email) VALUES (2, '王大明', 'wang@example.com')");
        jdbcTemplate.update("INSERT INTO userT (id, name, email) VALUES (3, '李小红', 'li@example.com')");

        UserT query = new UserT();
        query.setName("明");
        query.setEmail("wang@example.com");

        List<UserT> result = userTMapper.selectAll(query);

        assertThat(result.size()).isEqualTo(2);
    }

    // 测试 selectAll，无任何条件 → 返回全部（按 id desc）
    @Test
    public void Should_ReturnAllUsersOrderedByIdDesc_When_NoFiltersApplied() {
        jdbcTemplate.update("INSERT INTO userT (id, name) VALUES (1, '用户A')");
        jdbcTemplate.update("INSERT INTO userT (id, name) VALUES (2, '用户B')");

        UserT query = new UserT(); // 所有字段 null

        List<UserT> result = userTMapper.selectAll(query);

        assertThat(result.size()).isEqualTo(2);
        // 验证排序：id 2 在前，id 1 在后
        assertThat(result.get(0).getId()).isEqualTo(2);
        assertThat(result.get(1).getId()).isEqualTo(1);
    }

    // 测试查不存在的数据
    @Test
    public void Should_ReturnEmptyList_When_NoUserMatchesCriteria() {
        UserT query = new UserT();
        query.setName("不存在");

        List<UserT> result = userTMapper.selectAll(query);
        assertThat(result.size()).isEqualTo(0);
    }



    // 测试 selectByName name存在
    @Test
    public void Should_ReturnUser_When_NameExists() {
        // Given
        jdbcTemplate.update(
                "INSERT INTO userT (id, name, email) VALUES (?, ?, ?)",
                1, "张三", "zhang@example.com"
        );

        // When
        UserT user = userTMapper.selectByName("张三");

        // Then
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getEmail()).isEqualTo("zhang@example.com");
    }


    // 测试 selectByName name不存在
    @Test
    public void Should_ReturnNull_When_NameDoesNotExist() {
        UserT user = userTMapper.selectByName("不存在的名字");
        assertThat(user).isNull();
    }


    // 测试 selectByName name为null
    @Test
    public void Should_ReturnNull_When_NameIsNull() {
        UserT user = userTMapper.selectByName(null);
        assertThat(user).isNull();
    }

    // 测试 selectByName  测试sql注入，白盒测试
    @Test
    public void Should_NotBeVulnerableToSQLInjection_When_NameContainsMaliciousInput() {
        // Given: 插入一个名字为 "admin" 的用户
        jdbcTemplate.update("INSERT INTO userT (name) VALUES (?)", "admin");

        // When: 传入恶意字符串
        UserT user = userTMapper.selectByName("' OR '1'='1");

        // Then: 不应返回 admin（因为查的是字面量 "' OR '1'='1"）
        assertThat(user).isNull();
    }


}
