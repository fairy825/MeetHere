package com.meethere.controller;
import com.meethere.util.IMoocJSONResult;
import com.meethere.util.ImageUtil;
import com.meethere.util.MD5Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.meethere.pojo.User;
import com.meethere.service.UserService;
import com.meethere.util.Page4Navigator;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@RestController
public class UserController extends BasicController{
	@Autowired
    UserService userService;

    @GetMapping("/users")
    public IMoocJSONResult list(@RequestParam(value = "start", defaultValue = "0") Integer start,
                                     Integer size)
            throws Exception {
    	start = start<0?0:start;
    	if(size == null) size = PAGE_SIZE;
    	Page4Navigator<User> page = userService.list(start,size,5); 
        return IMoocJSONResult.ok(page);
    }
    @GetMapping("/users/{id}")
    public IMoocJSONResult get(@PathVariable("id") int id) throws Exception {
        User user = userService.get(id);
        return IMoocJSONResult.ok(user);
    }
    @PostMapping("/users")
    public IMoocJSONResult search(@RequestBody User user,@RequestParam(value = "start", defaultValue = "0") Integer start,
                                Integer size)
            throws Exception {
        start = start<0?0:start;
        if(size == null) size = PAGE_SIZE;
        Page4Navigator<User> page = userService.search(user,start,size,5);
        return IMoocJSONResult.ok(page);
    }
    @PutMapping("/users")
    public IMoocJSONResult update(@RequestBody User user) throws Exception {
        userService.update(user);
        return IMoocJSONResult.ok(user);
    }
    @PutMapping("/users/password")
    public IMoocJSONResult changePassword(@RequestBody User user,@RequestParam(value = "newPassword")String newPassword) throws Exception {
        String password = user.getPassword();
        User user1 = userService.get(user.getId());
        if (StringUtils.isBlank(password)) {
            return IMoocJSONResult.errorMsg("原密码不能为空");
        }
        if (StringUtils.isBlank(newPassword)) {
            return IMoocJSONResult.errorMsg("新密码不能为空");
        }
        if(userService.queryUserForLogin(user1.getName(), MD5Utils.getMD5Str(user.getPassword()))!=null) {
            user1.setPassword(MD5Utils.getMD5Str(newPassword));
            userService.update(user1);
            return IMoocJSONResult.ok(user1);
        }else{
            return IMoocJSONResult.errorMsg("密码错误");

        }
    }
    @PostMapping("/users/upload")
    public IMoocJSONResult upload(@RequestParam("image") MultipartFile image, HttpSession session) throws Exception {
        User user = (User)session.getAttribute("user");
        String folder = FILE_SPACE + "/faceImage";
        String fName = image.getOriginalFilename();
//        String path = folder + "/" + user.getId()+".jpg";
//        String pathDB =  "img/faceImage/" + user.getId()+".jpg";
        String path = folder + "/" + fName;
        String pathDB =  "img/faceImage/" + fName;
        User userDB = new User();
        BeanUtils.copyProperties(user,userDB);
        userDB.setFaceImage(pathDB);
        update(userDB);
        System.out.println(path);
        System.out.println(pathDB);
        File file = new File(path);
        String fileName = file.getName();
        if(!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        try {

            image.transferTo(file);
            BufferedImage img = ImageUtil.change2jpg(file);
            ImageIO.write(img, "jpg", file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String imageFolder_small= FILE_SPACE+"/faceImage_small";
        File f_small = new File(imageFolder_small, fileName);
        f_small.getParentFile().mkdirs();
        ImageUtil.resizeImage(file, 56, 56, f_small);
        return IMoocJSONResult.ok(pathDB);
    }
}