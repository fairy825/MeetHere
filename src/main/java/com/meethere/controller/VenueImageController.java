package com.meethere.controller;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import com.meethere.pojo.Venue;
import com.meethere.pojo.VenueImage;
import com.meethere.service.VenueImageService;
import com.meethere.service.VenueService;
import com.meethere.util.IMoocJSONResult;
import com.meethere.util.ImageUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.meethere.service.VenueService;
 
@RestController
public class VenueImageController extends BasicController{
	@Autowired
	VenueService venueService;
	@Autowired
	VenueImageService venueImageService;
	@GetMapping("/venues/{vid}/venueImages")
    public IMoocJSONResult list(@PathVariable("vid") int vid) throws Exception {
//        Venue venue = venueService.get(vid);
        List<VenueImage> venueImages =  venueImageService.listByVenue(vid);
        return IMoocJSONResult.ok(venueImages);

    }
    
    @PostMapping("/venueImages")
    public IMoocJSONResult add(@RequestParam("vid") int vid,  MultipartFile image, HttpServletRequest request) throws Exception {
		VenueImage bean = new VenueImage();
		Venue venue = venueService.get(vid);
    	bean.setVenue(venue);
		venueImageService.add(bean);

		String folder = FILE_SPACE + "/venueImage/" + vid ;
		String path = folder + "/" + bean.getId()+".jpg";
		System.out.println(path);
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

        	String imageFolder_small= FILE_SPACE+"/venueImage_small/"+ vid;
        	String imageFolder_middle= FILE_SPACE+"/venueImage_middle/"+ vid;
        	File f_small = new File(imageFolder_small, fileName);
        	File f_middle = new File(imageFolder_middle, fileName);
        	f_small.getParentFile().mkdirs();
        	f_middle.getParentFile().mkdirs();
        	ImageUtil.resizeImage(file, 56, 56, f_small);
        	ImageUtil.resizeImage(file, 217, 190, f_middle);

        return IMoocJSONResult.ok();
    }
    
    @DeleteMapping("/venueImages/{id}")
    public IMoocJSONResult delete(@PathVariable("id") int id, HttpServletRequest request)  throws Exception {
		VenueImage bean = venueImageService.get(id);
		venueImageService.delete(id);

		String folder = FILE_SPACE + "/venueImage/" + bean.getVenue().getId() ;

//		File  imageFolder= new File(request.getServletContext().getRealPath(folder));

		String path = folder + "/" + bean.getId()+".jpg";
		File file = new File(path);
		String fileName = file.getName();
		file.delete();
		String imageFolder_small= FILE_SPACE+"/venueImage_small/"+ bean.getVenue().getId();
		String imageFolder_middle= FILE_SPACE+"/venueImage_middle/"+ bean.getVenue().getId();
		File f_small = new File(imageFolder_small, fileName);
		File f_middle = new File(imageFolder_middle, fileName);
		f_small.delete();
		f_middle.delete();

		return IMoocJSONResult.ok();
    }
}