package cn.edu.uestc.acmicpc.service.impl;

import cn.edu.uestc.acmicpc.service.iface.FileService;
import cn.edu.uestc.acmicpc.util.FileUtil;
import cn.edu.uestc.acmicpc.util.Settings;
import cn.edu.uestc.acmicpc.util.StringUtil;
import cn.edu.uestc.acmicpc.util.ZipUtil;
import cn.edu.uestc.acmicpc.util.checker.ZipDataChecker;
import cn.edu.uestc.acmicpc.util.exception.AppException;
import cn.edu.uestc.acmicpc.util.exception.AppExceptionUtil;
import cn.edu.uestc.acmicpc.web.dto.FileInformationDTO;
import cn.edu.uestc.acmicpc.web.dto.FileUploadDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.zip.ZipFile;

/**
 * Implement for {@link FileService}
 */
@Service
@Primary
public class FileServiceImpl extends AbstractService implements FileService {

  private final Settings settings;

  @Autowired
  public FileServiceImpl(Settings settings) {
    this.settings = settings;
  }

  @Override
  public ArrayList<String> getProblemPictures(Integer problemId) throws AppException {
    File dir = new File(settings.SETTING_PROBLEM_PICTURE_FOLDER_ABSOLUTE + problemId + "/");
    if (!dir.exists())
      if (!dir.mkdirs())
        throw new AppException("Error while make picture directory!");
    File files[] = dir.listFiles();
    if (files == null)
      throw new AppException("Error while list pictures!");
    ArrayList<String> pictures = new ArrayList<>();
    pictures.clear();
    for (File file : files) {
      String fileName = file.getName();
      ImageInputStream iis = null;
      try {
        iis = ImageIO.createImageInputStream(file);
      } catch (IOException e) {
        throw new AppException("Error while create image input stream.");
      }
      Iterator<?> iter = ImageIO.getImageReaders(iis);
      if (iter.hasNext())
        pictures.add(settings.SETTING_PROBLEM_PICTURE_FOLDER + problemId + "/" + fileName);
    }
    return pictures;
  }

  @Override
  public FileInformationDTO uploadProblemPictures(FileUploadDTO fileUploadDTO,
                                                  Integer problemId) throws AppException {
    List<MultipartFile> files = fileUploadDTO.getFiles();
    if (files == null || files.size() > 1)
      throw new AppException("Fetch uploaded file error.");
    MultipartFile file = files.get(0);
    File dir = new File(settings.SETTING_PROBLEM_PICTURE_FOLDER_ABSOLUTE + problemId + "/");
    if (!dir.exists())
      if (!dir.mkdirs())
        throw new AppException("Error while make picture directory!");

    String newName = StringUtil.generateFileName(file.getOriginalFilename());
    File newFile =
        new File(settings.SETTING_PROBLEM_PICTURE_FOLDER_ABSOLUTE + problemId + "/"
            + newName);
    try {
      file.transferTo(newFile);
    } catch (IOException e) {
      throw new AppException("Error while save files");
    }

    return FileInformationDTO.builder()
        .setFileName(file.getOriginalFilename())
        .setFileURL(settings.SETTING_PROBLEM_PICTURE_FOLDER + problemId + "/" + newName)
        .build();
  }

  private String getDataZipFileName(Integer problemId) {
    return settings.SETTING_UPLOAD_FOLDER + "/problem_" + problemId + ".zip";
  }

  @Override
  public Integer uploadProblemDataFile(FileUploadDTO fileUploadDTO, Integer problemId)
      throws AppException {
    List<MultipartFile> files = fileUploadDTO.getFiles();
    if (files == null || files.size() > 1)
      throw new AppException("Fetch uploaded file error.");
    MultipartFile file = files.get(0);
    File targetFile = new File(getDataZipFileName(problemId));
    if (targetFile.exists() && !targetFile.delete())
      throw new AppException("Internal exception: target file exists and can not be deleted.");
    try {
      file.transferTo(targetFile);
    } catch (IOException e) {
      throw new AppException("Error while save files");
    }

    try {
      ZipFile zipFile = new ZipFile(getDataZipFileName(problemId));
      String tempDirectory = settings.SETTING_UPLOAD_FOLDER + "/" + problemId;
      ZipUtil.unzipFile(zipFile, tempDirectory, new ZipDataChecker());

      File dataPath = new File(tempDirectory);
      File[] dataFiles = dataPath.listFiles();
      AppExceptionUtil.assertNotNull(dataFiles);
      return dataFiles.length / 2;
    } catch (IOException e) {
      throw new AppException("Error while unzip file");
    }
  }

  @Override
  public Integer moveProblemDataFile(Integer problemId) throws AppException {
    String dataPath = settings.JUDGE_DATA_PATH + "/" + problemId;
    String tempDirectory = settings.SETTING_UPLOAD_FOLDER + "/" + problemId;
    File targetFile = new File(dataPath);
    File currentFile = new File(tempDirectory);
    // If the uploaded file list is empty, that means we don't update
    // the data folder.
    int dataCount = 0;
    boolean foundSpj = false;
    File[] files = currentFile.listFiles();
    Map<String, Integer> fileMap = new HashMap<>();
    if (files != null) {
      for (File file : files) {
        if (file.getName().endsWith(".in")) {
          fileMap.put(FileUtil.getFileName(file), ++dataCount);
        } else if (file.getName().equals("spj.cc")) {
          foundSpj = true;
        }
      }
    }

    if (dataCount != 0) {
      FileUtil.clearDirectory(dataPath);
      try {
        FileUtil.moveDirectory(currentFile, targetFile);
      } catch (IOException e) {
        throw new AppException("Error while move data files");
      }
      for (String file : fileMap.keySet()) {
        File fromFile = new File(dataPath + '/' + file + ".in");
        File toFile = new File(dataPath + '/' + fileMap.get(file) + ".in");
        if (!fromFile.renameTo(toFile))
          throw new AppException("Cannot rename file: " + file + ".in");
        fromFile = new File(dataPath + '/' + file + ".out");
        toFile = new File(dataPath + '/' + fileMap.get(file) + ".out");
        if (!fromFile.renameTo(toFile))
          throw new AppException("Cannot rename file: " + file + ".out");
      }
      FileUtil.clearDirectory(tempDirectory);
    }

    if (foundSpj) {
      Runtime runtime = Runtime.getRuntime();
      try {
        Process process = runtime.exec(String.format("g++ %s/spj.cc -o %s/spj -O2", dataPath, dataPath));
        process.waitFor();
      } catch (Exception e) {
        throw new AppException("Error while compile spj.cc");
      }
      File source = new File(String.format("%s/spj.cc", dataPath));
      if (!source.delete())
        throw new AppException("Cannot remove spj source file");
    }
    return dataCount;
  }

}
