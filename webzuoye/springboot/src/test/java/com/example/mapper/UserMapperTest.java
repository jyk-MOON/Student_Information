package com.example.mapper;

import com.example.entity.User;
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
@Transactional  //每个测试方法都开启事务
@Rollback       //测试方法执行完成之后回滚
public class UserMapperTest {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    //测试selectById，id存在
    @Test
    public void Should_ReturnUser_When_IdExists(){
        // Given: 用 JdbcTemplate 插入（绕过 MyBatis Mapper）
        // 这里只插入数据，具体插入到那个数据库，根据yml文件中的url决定
        jdbcTemplate.update(
                "INSERT INTO user (id, date, name, province, city, address, zip) VALUES (?, ?, ?, ?, ?, ?, ?)",
                1, "2025-01-10", "张三", "北京市", "北京市", "朝阳区建国路88号", "100022"
        );

        // When
        User user = userMapper.selectById(1);

        // Then
        assertThat(user).isNotNull();
        assertThat(user.getName()).isEqualTo("张三");
        assertThat(user.getDate()).isEqualTo("2025-01-10");
        assertThat(user.getProvince()).isEqualTo("北京市");
        assertThat(user.getCity()).isEqualTo("北京市");
        assertThat(user.getAddress()).isEqualTo("朝阳区建国路88号");
        assertThat(user.getZip()).isEqualTo("100022");
        assertThat(user.getId()).isEqualTo(1);
    }

    //测试selectById，id不存在
    @Test
    public void Should_ReturnNull_When_IdDoesNotExist(){
        //删除数据
        jdbcTemplate.update(
                "delete from user where id = ? ", 999);
        User user = userMapper.selectById(999);
        //没有数据返回null
        assertThat(user).isNull();
    }

    //测试selectById，id为null
    @Test
    public void Should_ReturnNull_When_IdIsNull(){
        User user = userMapper.selectById(null);
        //没有数据返回null
        assertThat(user).isNull();
    }

    //测试selectById,边界查询 0 -1 int最大值
    @Test
    public void Should_ReturnNull_When_IdIsBoundary(){
        User user = userMapper.selectById(0);
        //没有数据返回null
        assertThat(user).isNull();
        user = userMapper.selectById(-1);
        //没有数据返回null
        assertThat(user).isNull();
        user = userMapper.selectById(Integer.MAX_VALUE);
        //没有数据返回null
        assertThat(user).isNull();
    }


    //测试insert，满字段
    @Test
    public void Should_InsertSuccessfully_When_AllFieldsProvided() {
        // Given
        User user = new User();
        user.setDate("2025-01-10");
        user.setName("张三");
        user.setProvince("北京市");
        user.setCity("北京市");
        user.setAddress("朝阳区建国路88号");
        user.setZip("100022");
        // When
        int result = userMapper.insert(user);

        // Then
        assertThat(result).isEqualTo(1); // 影响一行
        assertThat(user.getId()).isNotNull(); // 主键已回填
        assertThat(user.getId()).isGreaterThan(0);

        // 查询验证
        User dbUser = userMapper.selectById(user.getId());
        assertThat(dbUser).isNotNull();
        assertThat(dbUser.getName()).isEqualTo("张三");
    }

    //测试insert，部分字段
    @Test
    public void Should_InsertOnlyNonNullFields_When_SomeAreNull() {
        // Given: 只提供必要字段
        User user = new User();
        user.setName("李四");
        user.setProvince("广东省");

        // When
        int result = userMapper.insert(user);

        // Then
        assertThat(result).isEqualTo(1);
        assertThat(user.getId()).isNotNull();

        // 查看数据库记录
        User dbUser = userMapper.selectById(user.getId());
        assertThat(dbUser.getName()).isEqualTo("李四");
        assertThat(dbUser.getProvince()).isEqualTo("广东省");
        assertThat(dbUser.getCity()).isNull();
        assertThat(dbUser.getAddress()).isNull();
    }

    //测试insert，id显式指定为null
    @Test
    public void Should_GenerateId_When_IdIsNull() {
        // Given
        User user = new User();
        user.setName("王五");
        user.setId(null); // 显式设为 null

        // When
        int result = userMapper.insert(user);

        // Then
        assertThat(result).isEqualTo(1);
        assertThat(user.getId()).isNotNull();
        assertThat(user.getId()).isGreaterThan(0); // 自增成功
    }

    //测试insert，id指定
    @Test
    public void Should_UseSpecifiedId_When_IdIsNotNull() {
        // Given
        User user = new User();
        user.setId(999);
        user.setName("赵六");

        //删除数据
        jdbcTemplate.update("DELETE FROM user WHERE id = ?", 999);

        // When
        int result = userMapper.insert(user);

        // Then
        assertThat(result).isEqualTo(1);
        assertThat(user.getId()).isEqualTo(999); // 不变

        User dbUser = userMapper.selectById(999);
        assertThat(dbUser).isNotNull();
        assertThat(dbUser.getName()).isEqualTo("赵六");
    }

    //测试insert，所有字段为null
    @Test
    public void Should_ThrowException_When_AllFieldsNull() {
        // Given
        User user = new User(); // 所有属性都是 null

        assertThatThrownBy(() -> userMapper.insert(user))
                .isInstanceOf(Exception.class);
    }


    //测试deleteById，id存在
    @Test
    public void Should_DeleteSuccessfully_When_IdExists() {
        // Given: 插入一条数据
        jdbcTemplate.update("INSERT INTO user (id, name) VALUES (?, ?)", 1, "张三");

        // When: 删除
        int result = userMapper.deleteById(1);

        // Then
        assertThat(result).isEqualTo(1); // 影响一行

        // 再查一次，确认已被删除
        User user = userMapper.selectById(1);
        assertThat(user).isNull();
    }

    //测试deleteById，id不存在
    @Test
    public void Should_ReturnZero_When_DeleteIdDoesNotExist() {
        // Given: 确保没有 id=999 的数据
        jdbcTemplate.update("DELETE FROM user WHERE id = 999");

        // When
        int result = userMapper.deleteById(999);

        // Then
        assertThat(result).isEqualTo(0); // 没有影响任何行，正常
    }

    //测试deleteById，id为null
    @Test
    public void Should_ReturnZero_When_IdIsNull() {
        // Given: 插入两条数据
        jdbcTemplate.update("INSERT INTO user (id, name) VALUES (1, '张三')");
        jdbcTemplate.update("INSERT INTO user (id, name) VALUES (2, '李四')");

        // When
        int result = userMapper.deleteById(null);

        // Then
        assertThat(result).isEqualTo(0); // 没删任何东西
    }

    //测试deleteById，幂等性
    @Test
    public void Should_BeIdempotent_When_DeleteTwice() {
        jdbcTemplate.update("INSERT INTO user (id, name) VALUES (1, '张三')");

        // 第一次删除
        int result1 = userMapper.deleteById(1);
        assertThat(result1).isEqualTo(1);

        // 第二次删除
        int result2 = userMapper.deleteById(1);
        assertThat(result2).isEqualTo(0); // 正常，不影响行数
    }

    //测试updateById，id存在
    @Test
    public void Should_UpdateAllFields_When_IdExists() {
        // Given: 先插入一条数据
        jdbcTemplate.update(
                "INSERT INTO user (id, name, province, city) VALUES (?, ?, ?, ?)",
                1, "张三", "广东省", "深圳市"
        );

        // 准备更新对象
        User user = new User();
        user.setId(1);
        user.setDate("2025-01-10");
        user.setName("李四");
        user.setProvince("江苏省");
        user.setCity("南京市");
        user.setAddress("中山路123号");
        user.setZip("210000");

        // When
        int result = userMapper.updateById(user);

        // Then
        assertThat(result).isEqualTo(1); // 影响一行

        // 查询验证
        User dbUser = userMapper.selectById(1);
        assertThat(dbUser.getName()).isEqualTo("李四");
        assertThat(dbUser.getProvince()).isEqualTo("江苏省");
        assertThat(dbUser.getAddress()).isEqualTo("中山路123号");
    }


    //测试updateById，部分字段
    @Test
    public void Should_UpdateOnlyNonNullFields_When_PartialData() {
        // Given: 插入初始数据
        jdbcTemplate.update(
                "INSERT INTO user (id, name, province, city) VALUES (?, ?, ?, ?)",
                1, "张三", "广东省", "广州市"
        );

        // 只设置两个字段
        User user = new User();
        user.setId(1);
        user.setName("王五"); // 修改 name
        user.setAddress(null); // 显式 null，不应更新

        // When
        int result = userMapper.updateById(user);

        // Then
        assertThat(result).isEqualTo(1);

        User dbUser = userMapper.selectById(1);
        assertThat(dbUser.getName()).isEqualTo("王五");     // 被更新
        assertThat(dbUser.getProvince()).isEqualTo("广东省"); // 保持不变
        assertThat(dbUser.getCity()).isEqualTo("广州市");     // 保持不变
    }

    //测试updateById，id不存在
    @Test
    public void Should_ReturnZero_When_UpdataIdDoesNotExist() {
        User user = new User();
        user.setId(999); // 不存在的 ID
        user.setName("不存在用户的更新");

        //删除数据
        jdbcTemplate.update("DELETE FROM user WHERE id = 999");

        int result = userMapper.updateById(user);

        assertThat(result).isEqualTo(0); // 没有匹配记录，不影响任何行
    }

    //测试updateById，id为null
    @Test
    public void Should_NotUpdateAnyRecord_When_IdIsNull() {
        // Given: 插入一条数据作为观察对象
        jdbcTemplate.update("INSERT INTO user (id, name) VALUES (1, '张三')");

        // When: id = null
        User user = new User();
        user.setId(null);
        user.setName("id为空");

        int result = userMapper.updateById(user);

        // Then
        assertThat(result).isEqualTo(0); // 不应影响任何行

        // 原数据未变
        User dbUser = userMapper.selectById(1);
        assertThat(dbUser.getName()).isEqualTo("张三");
    }

    //测试updateById，处理null
    @Test
    public void Should_ThrowException_When_UserNull() {
        // Given: user 为 null
        User user = null;

        // When & Then
        assertThatThrownBy(() -> userMapper.updateById(user))
                .isInstanceOf(Exception.class);
    }


    //测试selectAll，查询所有
    @Test
    public void Should_ReturnMatchingUser_When_AllFieldsAreProvided() {
        // Given: 插入一条完全匹配的数据
        jdbcTemplate.update(
                "INSERT INTO user (id, date, province, name, city, address, zip) VALUES (?, ?, ?, ?, ?, ?, ?)",
                1, "2023-10-01", "广东省", "张三丰", "深圳市", "南山区科技园", "518000"
        );
        // 插入一条不匹配的数据
        jdbcTemplate.update(
                "INSERT INTO user (id, date, province, name, city, address, zip) VALUES (?, ?, ?, ?, ?, ?, ?)",
                2, "2023-10-02", "北京市", "李四", "北京市", "朝阳区", "100000"
        );

        // When: 构造包含所有条件的查询对象
        User query = new User();
        query.setId(1);
        query.setDate("2023-10-01");
        query.setProvince("广东省");
        query.setName("三"); // 模糊匹配
        query.setCity("深圳市");
        query.setAddress("南山区科技园");
        query.setZip("518000");

        List<User> result = userMapper.selectAll(query);

        // Then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getName()).isEqualTo("张三丰");
    }


    //测试selectAll，查询部分字段
    @Test
    public void Should_ReturnUsers_When_OnlySomeFieldsAreProvided() {
        // Given: 插入多条数据
        jdbcTemplate.update("INSERT INTO user (id, province, name) VALUES (1, '浙江省', '王小明')");
        jdbcTemplate.update("INSERT INTO user (id, province, name) VALUES (2, '浙江省', '王大明')");
        jdbcTemplate.update("INSERT INTO user (id, province, name) VALUES (3, '江苏省', '李小红')");

        // When: 只提供 province 和 name（模糊）
        User query = new User();
        query.setProvince("浙江省");
        query.setName("明");

        List<User> result = userMapper.selectAll(query);

        // Then
        assertThat(result.size()).isEqualTo(2);

    }

    //测试selectAll，查询所有字段为null，返回所有数据
    @Test
    public void Should_ReturnAllUsers_When_QueryObjectHasAllNullFields() {
        // Given: 插入两条数据
        jdbcTemplate.update("INSERT INTO user (id, name) VALUES (1, '用户A')");
        jdbcTemplate.update("INSERT INTO user (id, name) VALUES (2, '用户B')");

        // When: 创建一个空查询对象（所有字段为 null）
        User query = new User(); // 默认所有字段为 null

        List<User> result = userMapper.selectAll(query);

        // Then: 应返回全部记录
        assertThat(result.size()).isEqualTo(2);
    }



}
