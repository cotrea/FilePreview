package cn.keking.web.controller;

import cn.keking.entity.dao.PreviewAttachRepository;
import cn.keking.entity.model.PreviewAttachment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import cn.keking.model.ReturnResponse;
import cn.keking.utils.FileUtils;
import com.ivo.core.decryption.DecryptException;
import com.ivo.core.decryption.IVODecryption;
import com.ivo.core.decryption.IVODecryptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author yudian-it
 * @date 2017/12/1
 */
@RestController
public class FileController {
    @Value("${file.dir}")
    String fileDir;
    @Autowired
    FileUtils fileUtils;
    @Autowired
    PreviewAttachRepository preAttach;
    String fpDir = "filepreview";
    String fpPath = fpDir + File.separator;

    @RequestMapping(value = "fileUpload", method = RequestMethod.POST)
    public String fileUpload(@RequestParam("upfile") MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        // 只允许上传PDF文件
        List<String> selfType = new ArrayList<>();
        selfType.add("pdf");
        selfType.add("txt");

        selfType.add("jpg");
        selfType.add("jpeg");

        selfType.add("doc");
        selfType.add("docx");

        selfType.add("ppt");
        selfType.add("pptx");

        selfType.add("xls");
        selfType.add("xlsx");

        if (existsSelfTypeFile(fileName,selfType)) {
            String selfTypes = selfType.toString().substring(1,selfType.toString().length()-1);
            return new ObjectMapper().writeValueAsString(new ReturnResponse<String>(1, "目前只开放上传"+selfTypes+"文件入口!", null));
        }

        //维护至hisdb
        String baseUrl= (String) RequestContextHolder.currentRequestAttributes().getAttribute("baseUrl",0);
        String fileprePath = baseUrl+"onlinePreview?url="+ fpPath + fileName;
        String splitName = "-";
        String memo = "signedlist";
        if(fileName.contains(splitName)){
            memo = fileName.split(splitName)[1];
            splitName = ".";
            if(memo.contains(splitName)){
                memo = memo.split("\\.")[0];
            }
        }

        PreviewAttachment preAttacho = preAttach.findPreviewAttach(fileName);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        if(preAttacho!=null){
            preAttacho.setPreviewAttachID("Ver:"+ sdf.format(new Date()));
        }else{
            PreviewAttachment preAttachs = new PreviewAttachment();
            preAttachs.setPreviewName(fileName);
            preAttachs.setFileLocation(fileprePath);
            preAttachs.setPreviewAttachID("Ver:"+ sdf.format(new Date()));
            preAttachs.setMemo(memo);
            preAttach.save(preAttachs);
        }

        File outFile = new File(fileDir + fpPath);
        if (!outFile.exists()) {
            outFile.mkdirs();
        }

        File f = new File(fileDir + fpPath + file.getOriginalFilename());
        try {
            file.transferTo(f);
            //file decode
            byte[] b  = IVODecryptionUtils.decrypt(f);
            FileOutputStream out = new FileOutputStream(f);
            out.write(b);
            out.flush();
            out.close();
            return new ObjectMapper().writeValueAsString(new ReturnResponse<String>(0, "success", null));
        } catch (DecryptException e) {
            e.printStackTrace();
            return new ObjectMapper().writeValueAsString(new ReturnResponse<String>(1, "failure", null));
        }
    }

    @RequestMapping(value = "deleteFile", method = RequestMethod.GET)
    public String deleteFile(String fileName) throws JsonProcessingException {
        if (fileName.contains("/")) {
            fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
        }
        File file = new File(fileDir + fpPath + fileName);
        if (file.exists()) {
            file.delete();
        }
        return new ObjectMapper().writeValueAsString(new ReturnResponse<String>(0, "del success", null));
    }

    @RequestMapping(value = "listFiles", method = RequestMethod.GET)
    public String getFiles() throws JsonProcessingException {
        List<Map<String, String>> list = Lists.newArrayList();
        File file = new File(fileDir + fpPath);
        if (file.exists()) {
            Arrays.stream(file.listFiles()).forEach(file1 -> list.add(ImmutableMap.of("fileName", fpDir + "/" + file1.getName())));
        }
        return new ObjectMapper().writeValueAsString(list);
    }

    private String getFileName(String name) {
        String suffix = name.substring(name.lastIndexOf("."));
        String nameNoSuffix = name.substring(0, name.lastIndexOf("."));
        String uuid = UUID.randomUUID().toString();
        return uuid + "-" + nameNoSuffix + suffix;
    }

    /**
     * 是否存在该类型的文件
     * @return
     * @param fileName
     */
    private boolean existsTypeFile(String fileName) {
        boolean result = false;
        String suffix = fileUtils.getSuffixFromFileName(fileName);
        File file = new File(fileDir + fpPath);
        if (file.exists()) {
            for(File file1 : file.listFiles()){
                String existsFileSuffix = fileUtils.getSuffixFromFileName(file1.getName());
                if (suffix.equals(existsFileSuffix)) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 是否为PDF类型的文件
     * @return
     * @param fileName
     */
    private boolean existsSelfTypeFile(String fileName,List<String> selfType) {
        boolean result = false;
        String splitValue = ".";
        if(fileName.contains(splitValue)){
            String type = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            if(!selfType.contains(type)){
                result = true;
            }
        }
        return result;
    }
}
