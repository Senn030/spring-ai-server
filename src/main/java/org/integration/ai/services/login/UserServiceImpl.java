package org.integration.ai.services.login;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.integration.ai.domain.login.entity.User;
import org.integration.ai.domain.login.mapper.UserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.integration.ai.exception.BusinessException;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yangsen
 * @since 2025-04-19
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;

    public User findById(String id){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                .eq(User::getId,id);
        return userMapper.selectOne(queryWrapper);
    }

    public User findByPhone(String phone){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                .eq(User::getPhone,phone);
        return userMapper.selectOne(queryWrapper);
    }

    public SaTokenInfo login(String phone, String password){
        User loginUser = findByPhone(phone);

        if (!BCrypt.checkpw(password, loginUser.getPassword())) {
            throw new BusinessException("用户名/密码错误");
        }
        StpUtil.login(loginUser.getId());
        return StpUtil.getTokenInfo();
    }


    public SaTokenInfo register(String phone, String password){
        User loginUser = findByPhone(phone);
        if (loginUser != null) {
            throw new BusinessException("手机号已存在");
        }
        User user = new User();
        user.setPhone(phone);
        user.setPassword(BCrypt.hashpw(password));
        user.setCreatedTime(LocalDateTime.now());
        user.setEditedTime(LocalDateTime.now());

        userMapper.insert(user);
        StpUtil.login(user.getId());
        return StpUtil.getTokenInfo();
    }
}
