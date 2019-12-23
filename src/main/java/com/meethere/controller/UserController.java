package com.meethere.controller;
import com.meethere.util.IMoocJSONResult;
import com.meethere.util.ImageUtil;
import com.meethere.util.MD5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/users")
@Api(value="用户注册登录的接口", tags= {"注册和登录的controller"})
public class UserController extends BasicController{
	@Autowired
    UserService userService;

    @ApiOperation(value="分页查询所有用户", notes="管理员分页查看用户信息的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="start", value="页码", required = true, dataType="Integer"),
            @ApiImplicitParam(name="size", value="每页的显示个数",  dataType="Integer")
    })
    @GetMapping("")
    public IMoocJSONResult list(@RequestParam(value = "start", defaultValue = "0") Integer start,
                                     Integer size)
            throws Exception {
    	start = start<0?0:start;
    	if(size == null) size = PAGE_SIZE;
    	Page4Navigator<User> page = userService.list(start,size,5); 
        return IMoocJSONResult.ok(page);
    }
    @GetMapping("/{id}")
    public IMoocJSONResult get(@PathVariable("id") int id) throws Exception {
        User user = userService.get(id);
        return IMoocJSONResult.ok(user);
    }

    @ApiOperation(value="查找用户", notes="管理员根据条件查找用户的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="start", value="页码", required = true, dataType="Integer"),
            @ApiImplicitParam(name="size", value="每页的显示个数",  dataType="Integer")
    })
    @PostMapping("")
    public IMoocJSONResult search(@RequestBody User user,@RequestParam(value = "start", defaultValue = "0") Integer start,
                                Integer size)
            throws Exception {
        start = start<0?0:start;
        if(size == null) size = PAGE_SIZE;
        Page4Navigator<User> page = userService.search(user,start,size,5);
        IMoocJSONResult result = IMoocJSONResult.ok(page);
        return  result;
    }

    @ApiOperation(value="更新用户个人信息", notes="用户更新自己的个人信息的接口")
    @PutMapping("")
    public IMoocJSONResult update(@RequestBody User user,HttpSession session) throws Exception {
        User user1 = (User)session.getAttribute("user");

        if(user1 == null)
            return IMoocJSONResult.build(501,"未登录",null);
        userService.update(user);
        return IMoocJSONResult.ok(user);
    }

//    @ApiOperation(value="更新用户密码", notes="用户修改密码的接口")
    @PutMapping("/password")
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
//    @PostMapping("/upload")
//    public IMoocJSONResult upload(@RequestParam("image") MultipartFile image, HttpSession session) throws Exception {
//        User user = (User)session.getAttribute("user");
//        String folder = FILE_SPACE + "/faceImage";
//        String fName = image.getOriginalFilename();
////        String path = folder + "/" + user.getId()+".jpg";
////        String pathDB =  "img/faceImage/" + user.getId()+".jpg";
//        String path = folder + "/" + fName;
//        String pathDB =  "img/faceImage/" + fName;
//        User userDB = new User();
//        BeanUtils.copyProperties(user,userDB);
//        userDB.setFaceImage(pathDB);
//        userService.update(user);
//        System.out.println(path);
//        System.out.println(pathDB);
//        File file = new File(path);
//        String fileName = file.getName();
//        if(!file.getParentFile().exists())
//            file.getParentFile().mkdirs();
//        try {
//            image.transferTo(file);
//            BufferedImage img = ImageUtil.change2jpg(file);
//            ImageIO.write(img, "jpg", file);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String imageFolder_small= FILE_SPACE+"/faceImage_small";
//        File f_small = new File(imageFolder_small, fileName);
//        f_small.getParentFile().mkdirs();
//        ImageUtil.resizeImage(file, 56, 56, f_small);
//        return IMoocJSONResult.ok(pathDB);
//    }
}