package com.newcoder.communitydemo.controller;

import com.newcoder.communitydemo.annotation.LoginRequired;
import com.newcoder.communitydemo.entity.User;
import com.newcoder.communitydemo.service.FollowService;
import com.newcoder.communitydemo.service.LikeService;
import com.newcoder.communitydemo.service.UserService;
import com.newcoder.communitydemo.util.CommunityConstant;
import com.newcoder.communitydemo.util.CommunityUtil;
import com.newcoder.communitydemo.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 用户相关业务
 */
@Controller
@RequestMapping("/user")
public class UserController implements CommunityConstant {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${community.path.uploadPath}")
    private String uploadPath;
    @Value("${community.path.domain}")
    private String domain;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private LikeService likeService;

    @Autowired
    private FollowService followService;
    /**
     * 设置页面
     * @return
     */
    @GetMapping("/setting")
    @LoginRequired
    public String getSettingPage() {
        return "/site/setting";
    }

    /**
     * 配置头像
     */
    @PostMapping("/upload")
    @LoginRequired
    public String uploadHeader(MultipartFile headerImage, Model model) {
        if (headerImage == null) {
            model.addAttribute("error", "还没上传图片");
            return "/site/setting";
        }
        String originalFilename = headerImage.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("error", "文件格式不对");
            return "/site/setting";
        }
        // 随机生成
        originalFilename = CommunityUtil.generateUUID() + "." + suffix;
        // 存放路径
        File dest = new File(uploadPath + "/" + originalFilename);
        try {
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传文件失败" + e.getMessage());
            throw new RuntimeException("上传文件失败，服务器异常！", e);
        }
        // 更新用户头像路径(web访问路径
        // http://localhost:8080/user/header/xxx.png
        User user = hostHolder.getUser();
        String headerUrl = domain + "/user/header/" + originalFilename;
        userService.updateHeader(user.getId(), headerUrl);

        return "redirect:/index";
    }

    /**
     * 根据URL获取本地图像
     * @param fileName
     * @param response
     */
    @GetMapping("/header/{fineName}")
    public void getHeader(@PathVariable("fineName") String fileName, HttpServletResponse response) {
        // 服务器存放路径
        String s = uploadPath + "/" + fileName;
        // 获取后缀
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        // 响应图片
        response.setContentType("image/" + suffix);
        try (FileInputStream fis = new FileInputStream(s);
        ServletOutputStream outputStream = response.getOutputStream();){
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1) {
                outputStream.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error("读取头像失败" + e);
        }

    }

    /**
     * 修改密码
     */
    @PostMapping("/modifyPassword")
    @LoginRequired
    public String modifyPassword(String oldPassword, String newPassword, Model model) {
        User user = hostHolder.getUser();

        String result = userService.modifyPassword(user.getId(), user.getSalt(), user.getPassword(), oldPassword, newPassword);
        if (!result.equals("修改成功")) {
            model.addAttribute("error", result);
            return "/site/setting";
        }
        return "redirect:/index";
    }

    // 个人主页
    @GetMapping("/profile/{userId}")
    public String getProfilePage(@PathVariable("userId") int userId, Model model) {
        User user = userService.findUserById(userId);
        System.out.println(user.toString());
        if (user == null) {
            throw new RuntimeException("该用户不存在");
        }
        // 用户的基本信息
        model.addAttribute("user", user);
        // 点赞数
        int userLikeCount = likeService.findUserLikeCount(userId);
        model.addAttribute("likeCount", userLikeCount);

        // 关注数量
        long followeeCount = followService.findFolloweeCount(userId, ENTITY_TYPE_USER);
        model.addAttribute("followeeCount", followeeCount);
        // 粉丝的数量
        long followerCount = followService.findFollowerCount(ENTITY_TYPE_USER, userId);
        model.addAttribute("followerCount", followerCount);
        // 当前用户有无关注
        boolean hasFollowed = false;
        if (hostHolder.getUser() != null) {
            hasFollowed = followService.hasFollowed(hostHolder.getUser().getId(), ENTITY_TYPE_USER, userId);
        }
        model.addAttribute("hasFollowed", hasFollowed);
        return "/site/profile";
    }
}
