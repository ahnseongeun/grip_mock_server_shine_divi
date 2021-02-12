package com.app.grip.src.video;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

@Slf4j
@RestController
@RequestMapping("/api")
public class VideoController {

    private static final String IMAGE_PNG_FORMAT = "png";

//    @GetMapping("/videos/{name}/full")
//    public ResponseEntity<UrlResource> getFullVideo(@PathVariable String name) throws MalformedURLException {
//        UrlResource video = new UrlResource("file:${video.location}/${name}");
//        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
//                .contentType(MediaTypeFactory.getMediaType(video).orElse(MediaType.APPLICATION_OCTET_STREAM))
//                .body(video);
//    }

    @GetMapping("/videos/{name}")
    @ApiOperation(value = "영상 불러오기(테스트 중)", notes = "영상 리스트 조회")
    public ResponseEntity<ResourceRegion> getVideo(@PathVariable String name,
                                                   @RequestHeader HttpHeaders headers) throws IOException {
        String path = "/home/ubuntu/video/";
        UrlResource video = new UrlResource("file:"+path+name);
        ResourceRegion region = resourceRegion(video, headers);
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                            .contentType(MediaTypeFactory.getMediaType(video).orElse(MediaType.APPLICATION_OCTET_STREAM))
                            .body(region);
    }

    @GetMapping("/videos/thumb-nail/{name}")
    @ApiOperation(value = "영상 썸네일 만들기(테스트 중)", notes = "영상 리스트 조회")
    public File getSumNail(@PathVariable String name,
                                                   @RequestHeader HttpHeaders headers) throws IOException, JCodecException {

        log.info("getThumbNail");
        String videoPath = "/home/ubuntu/video/";
        String thumbNailName = name.substring(0,name.length()-4)+".png";
        String thumbNailPath = "/home/ubuntu/image/"+thumbNailName;
        File thumbNailFile = new File(thumbNailPath);
        File videoFile = new File(videoPath + name);
        return getThumbnail(videoFile,thumbNailFile);
    }

    @PostMapping("/upload-video")
    @ApiOperation(value = "영상 업로드(테스트 중)", notes = "영상 업로드(테스트 중)")
    public void upload(
            @RequestParam("file") MultipartFile multipartFile
    ) throws IOException {
        log.info("### upload");
        File targetFile;

        String videoPath = "/home/ubuntu/video/";
        targetFile = new File(videoPath + multipartFile.getOriginalFilename());
        log.info(String.valueOf(targetFile));

        try {
            InputStream fileStream = multipartFile.getInputStream();
            FileUtils.copyInputStreamToFile(fileStream, targetFile);
            log.info("저장에 성공했습니다.");
        } catch (IOException e) {
            FileUtils.deleteQuietly(targetFile);
            log.info("저장에 실패했습니다.");
        }
    }

    private ResourceRegion resourceRegion(UrlResource video, HttpHeaders headers) throws IOException {

        final long chunkSize = 1000000L;
        long contentLength = video.contentLength();

        HttpRange httpRange = headers.getRange().stream().findFirst().get();
        if(httpRange != null) {
            long start = httpRange.getRangeStart(contentLength);
            long end = httpRange.getRangeEnd(contentLength);
            long rangeLength = Long.min(chunkSize, end - start + 1);
            return new ResourceRegion(video, start, rangeLength);
        } else {
            long rangeLength = Long.min(chunkSize, contentLength);
            return new ResourceRegion(video, 0, rangeLength);
        }
    }

    public File getThumbnail(File source, File thumbnail) throws IOException, JCodecException {
        log.debug("extracting thumbnail from video");
        int frameNumber = 0;

        Picture picture = FrameGrab.getFrameFromFile(source, frameNumber);

        BufferedImage bufferedImage = AWTUtil.toBufferedImage(picture);
        ImageIO.write(bufferedImage, IMAGE_PNG_FORMAT, thumbnail);
        return thumbnail;
    }

}
